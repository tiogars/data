import { useMemo, useState } from 'react';
import Alert from '@mui/material/Alert';
import Box from '@mui/material/Box';
import Button from '@mui/material/Button';
import FormControlLabel from '@mui/material/FormControlLabel';
import List from '@mui/material/List';
import ListItem from '@mui/material/ListItem';
import Paper from '@mui/material/Paper';
import Stack from '@mui/material/Stack';
import Switch from '@mui/material/Switch';
import TextField from '@mui/material/TextField';
import Typography from '@mui/material/Typography';
import { useOidcAuth } from '../../../auth/OidcAuthProvider';
import {
  AUTH_CLIENT_ID_DEFAULT,
  AUTH_OIDC_ENABLED_DEFAULT,
  AUTH_REALM_DEFAULT,
  AUTH_SCOPE_DEFAULT,
  AUTH_BASE_URL_DEFAULT,
  getAuthBaseUrl,
  getAuthClientId,
  getAuthRealm,
  getAuthScope,
  isOidcAuthEnabled,
  resetAuthClientId,
  resetAuthRealm,
  resetAuthScope,
  resetAuthBaseUrl,
  resetOidcAuthEnabled,
  setAuthBaseUrl,
  setAuthClientId,
  setAuthRealm,
  setAuthScope,
  setOidcAuthEnabled,
} from '../../../services/runtimeConfig';

type OpenIdDiscoveryPayload = {
  issuer?: string;
  authorization_endpoint?: string;
  token_endpoint?: string;
  jwks_uri?: string;
};

type OpenIdDiscoveryCheckState = {
  type: 'idle' | 'success' | 'error';
  message: string;
};

const stripTrailingSlashes = (value: string) => {
  let normalized = value;
  while (normalized.endsWith('/')) {
    normalized = normalized.slice(0, -1);
  }
  return normalized;
};

const normalizeRealm = (value: string) => {
  let normalized = value.trim();
  while (normalized.startsWith('/')) {
    normalized = normalized.slice(1);
  }
  while (normalized.endsWith('/')) {
    normalized = normalized.slice(0, -1);
  }
  return normalized;
};

const buildOpenIdDiscoveryUrl = (baseUrl: string, realm: string) => {
  const normalizedBaseUrl = stripTrailingSlashes(baseUrl.trim());
  const normalizedRealm = normalizeRealm(realm);
  return `${normalizedBaseUrl}/realms/${normalizedRealm}/.well-known/openid-configuration`;
};

const validateAuthUrl = (value: string) => {
  const normalized = value.trim();
  if (!normalized) {
    return 'L URL d authentification est obligatoire.';
  }

  try {
    const parsed = new URL(normalized);
    if (!parsed.protocol.startsWith('http')) {
      return 'L URL doit commencer par http:// ou https://';
    }
    return null;
  } catch {
    return 'URL invalide. Exemple: https://auth2.tiogars.fr/';
  }
};

export const AuthBaseUrlPage = () => {
  const { refreshUser } = useOidcAuth();
  const redirectUriExample = globalThis.window === undefined ? '/auth/callback' : `${globalThis.window.location.origin}/auth/callback`;
  const [draftEnabled, setDraftEnabled] = useState(() => isOidcAuthEnabled());
  const [savedEnabled, setSavedEnabled] = useState(() => isOidcAuthEnabled());
  const [draftUrl, setDraftUrl] = useState(() => getAuthBaseUrl());
  const [draftRealm, setDraftRealm] = useState(() => getAuthRealm());
  const [draftClientId, setDraftClientId] = useState(() => getAuthClientId());
  const [draftScope, setDraftScope] = useState(() => getAuthScope());
  const [savedUrl, setSavedUrl] = useState(() => getAuthBaseUrl());
  const [savedRealm, setSavedRealm] = useState(() => getAuthRealm());
  const [savedClientId, setSavedClientId] = useState(() => getAuthClientId());
  const [savedScope, setSavedScope] = useState(() => getAuthScope());
  const [status, setStatus] = useState<'idle' | 'saved' | 'reset'>('idle');
  const [isDiscoveryChecking, setIsDiscoveryChecking] = useState(false);
  const [discoveryCheckState, setDiscoveryCheckState] = useState<OpenIdDiscoveryCheckState>({ type: 'idle', message: '' });

  const validationError = useMemo(() => validateAuthUrl(draftUrl), [draftUrl]);
  const isRealmInvalid = !draftRealm.trim();
  const isClientIdInvalid = !draftClientId.trim();
  const isScopeInvalid = !draftScope.trim();
  // Les champs OIDC ne sont obligatoires que si l authentification par URL est active.
  const isFormInvalid = draftEnabled && (Boolean(validationError) || isRealmInvalid || isClientIdInvalid || isScopeInvalid);

  const handleSave = async () => {
    if (isFormInvalid) {
      return;
    }

    if (draftEnabled) {
      setAuthBaseUrl(draftUrl);
      setAuthRealm(draftRealm);
      setAuthClientId(draftClientId);
      setAuthScope(draftScope);
    }
    setOidcAuthEnabled(draftEnabled);

    const effective = getAuthBaseUrl();
    setSavedUrl(effective);
    setDraftUrl(effective);
    setSavedRealm(getAuthRealm());
    setDraftRealm(getAuthRealm());
    setSavedClientId(getAuthClientId());
    setDraftClientId(getAuthClientId());
    setSavedScope(getAuthScope());
    setDraftScope(getAuthScope());
    setSavedEnabled(isOidcAuthEnabled());
    setStatus('saved');
    await refreshUser();
  };

  const handleReset = async () => {
    resetAuthBaseUrl();
    resetAuthRealm();
    resetAuthClientId();
    resetAuthScope();
    resetOidcAuthEnabled();

    setDraftEnabled(AUTH_OIDC_ENABLED_DEFAULT);
    setSavedEnabled(AUTH_OIDC_ENABLED_DEFAULT);
    setDraftUrl(AUTH_BASE_URL_DEFAULT);
    setDraftRealm(AUTH_REALM_DEFAULT);
    setDraftClientId(AUTH_CLIENT_ID_DEFAULT);
    setDraftScope(AUTH_SCOPE_DEFAULT);
    setSavedUrl(AUTH_BASE_URL_DEFAULT);
    setSavedRealm(AUTH_REALM_DEFAULT);
    setSavedClientId(AUTH_CLIENT_ID_DEFAULT);
    setSavedScope(AUTH_SCOPE_DEFAULT);
    setStatus('reset');
    setDiscoveryCheckState({ type: 'idle', message: '' });
    await refreshUser();
  };

  const handleDiscoveryCheck = async () => {
    if (validationError) {
      setDiscoveryCheckState({ type: 'error', message: validationError });
      return;
    }

    const endpoint = buildOpenIdDiscoveryUrl(draftUrl, draftRealm);
    setIsDiscoveryChecking(true);
    setDiscoveryCheckState({ type: 'idle', message: '' });

    try {
      const response = await fetch(endpoint, {
        method: 'GET',
        headers: {
          Accept: 'application/json',
        },
      });

      let payload: OpenIdDiscoveryPayload | null = null;
      try {
        payload = (await response.json()) as OpenIdDiscoveryPayload;
      } catch {
        payload = null;
      }

      if (!response.ok) {
        setDiscoveryCheckState({
          type: 'error',
          message: `Test echoue (${response.status} ${response.statusText}). Endpoint teste: ${endpoint}`,
        });
        return;
      }

      if (!payload?.issuer) {
        setDiscoveryCheckState({
          type: 'error',
          message: `Configuration OpenID recue mais champ 'issuer' absent (endpoint: ${endpoint}).`,
        });
        return;
      }

      setDiscoveryCheckState({
        type: 'success',
        message: `Configuration OpenID chargee avec succes. Issuer: ${payload.issuer}`,
      });
    } catch (error) {
      const message = error instanceof Error ? error.message : 'Erreur reseau inconnue';
      setDiscoveryCheckState({
        type: 'error',
        message: `Impossible de recuperer la configuration OpenID sur ${endpoint}. ${message}`,
      });
    } finally {
      setIsDiscoveryChecking(false);
    }
  };

  return (
    <Stack spacing={3} sx={{ p: { xs: 2, md: 3 }, maxWidth: 980, mx: 'auto' }}>
      <Box>
        <Typography variant="h4" component="h1">
          Configuration URL d authentification
        </Typography>
        <Typography variant="body2" color="text.secondary">
          Definissez l URL de base du serveur d authentification ou desactivez-la pour utiliser un compte utilisateur
          enregistre en base de donnees. Ces valeurs sont stockees dans le Local Storage.
        </Typography>
      </Box>

      <Alert severity="info">
        Cles Local Storage: auth-oidc-enabled, auth-base-url, auth-realm, auth-client-id, auth-scope.
      </Alert>

      <Paper variant="outlined" sx={{ p: { xs: 2, md: 2.5 } }}>
        <Stack spacing={2}>
          <Typography variant="h6">URL d authentification active</Typography>

          <FormControlLabel
            control={
              <Switch
                checked={draftEnabled}
                onChange={(event) => {
                  setDraftEnabled(event.target.checked);
                  setStatus('idle');
                }}
                slotProps={{ input: { 'aria-label': 'URL d authentification active' } }}
              />
            }
            label={draftEnabled ? 'Authentification par URL d authentification (OIDC)' : 'Authentification par URL desactivee'}
          />

          {!draftEnabled && (
            <Alert severity="warning">
              L authentification par URL est desactivee. Une fois enregistre, la connexion se fait avec un compte
              utilisateur enregistre en base de donnees depuis la page de connexion.
            </Alert>
          )}

          <TextField
            label="URL d authentification"
            value={draftUrl}
            onChange={(event) => {
              setDraftUrl(event.target.value);
              setStatus('idle');
            }}
            placeholder="https://auth2.tiogars.fr/"
            fullWidth
            disabled={!draftEnabled}
            error={draftEnabled && Boolean(validationError)}
            helperText={(draftEnabled && validationError) || 'Exemple: https://auth2.tiogars.fr/'}
          />

          <TextField
            label="Realm OIDC"
            value={draftRealm}
            onChange={(event) => {
              setDraftRealm(event.target.value);
              setStatus('idle');
            }}
            placeholder="data"
            fullWidth
            disabled={!draftEnabled}
            error={draftEnabled && isRealmInvalid}
            helperText={draftEnabled && isRealmInvalid ? 'Le realm est obligatoire.' : 'Exemple: data'}
          />

          <TextField
            label="Client ID OIDC"
            value={draftClientId}
            onChange={(event) => {
              setDraftClientId(event.target.value);
              setStatus('idle');
            }}
            placeholder="data-web"
            fullWidth
            disabled={!draftEnabled}
            error={draftEnabled && isClientIdInvalid}
            helperText={draftEnabled && isClientIdInvalid ? 'Le client id est obligatoire.' : 'Exemple: data-web'}
          />

          <TextField
            label="Scope OIDC"
            value={draftScope}
            onChange={(event) => {
              setDraftScope(event.target.value);
              setStatus('idle');
            }}
            placeholder="openid profile email"
            fullWidth
            disabled={!draftEnabled}
            error={draftEnabled && isScopeInvalid}
            helperText={draftEnabled && isScopeInvalid ? 'Le scope est obligatoire.' : 'Exemple: openid profile email'}
          />

          <Stack direction={{ xs: 'column', sm: 'row' }} spacing={1.2}>
            <Button variant="contained" onClick={() => void handleSave()} disabled={isFormInvalid}>
              Enregistrer
            </Button>
            <Button variant="outlined" onClick={() => void handleReset()}>
              Reinitialiser
            </Button>
            <Button
              variant="outlined"
              onClick={handleDiscoveryCheck}
              disabled={!draftEnabled || Boolean(validationError) || isDiscoveryChecking}
            >
              {isDiscoveryChecking ? 'Test en cours...' : 'Tester /.well-known/openid-configuration'}
            </Button>
          </Stack>

          {status === 'saved' && (
            <Alert severity="success">
              {draftEnabled
                ? 'URL d authentification enregistree.'
                : 'Authentification par URL desactivee. Connectez-vous avec un compte utilisateur base de donnees.'}
            </Alert>
          )}

          {status === 'reset' && (
            <Alert severity="success">URL reinitialisee sur la valeur par defaut.</Alert>
          )}

          {discoveryCheckState.type === 'success' && (
            <Alert severity="success">{discoveryCheckState.message}</Alert>
          )}

          {discoveryCheckState.type === 'error' && (
            <Alert severity="error">{discoveryCheckState.message}</Alert>
          )}

          <Typography variant="body2" color="text.secondary">
            Authentification actuellement appliquee:{' '}
            {savedEnabled ? 'URL d authentification (OIDC)' : 'compte utilisateur base de donnees'}
          </Typography>
          <Typography variant="body2" color="text.secondary">
            URL actuellement appliquee: {savedUrl}
          </Typography>
          <Typography variant="body2" color="text.secondary">
            Realm actuellement applique: {savedRealm}
          </Typography>
          <Typography variant="body2" color="text.secondary">
            Client ID actuellement applique: {savedClientId}
          </Typography>
          <Typography variant="body2" color="text.secondary">
            Scope actuellement applique: {savedScope}
          </Typography>
        </Stack>
      </Paper>

      <Paper variant="outlined" sx={{ p: { xs: 2, md: 2.5 } }}>
        <Stack spacing={1.5}>
          <Typography variant="h6">Parametrage Keycloak recommande</Typography>
          <Typography variant="body2" color="text.secondary">
            Suivez ces etapes, du royaume au client, pour permettre a la gateway de valider les JWT.
          </Typography>

          <List disablePadding>
            <ListItem sx={{ display: 'list-item', py: 0.5 }}>
              1. Creer un royaume, par exemple data.
            </ListItem>
            <ListItem sx={{ display: 'list-item', py: 0.5 }}>
              2. Creer les roles de realm (exemple: gateway-user, gateway-admin).
            </ListItem>
            <ListItem sx={{ display: 'list-item', py: 0.5 }}>
              3. Creer un client confidentiel nomme data-gateway (OpenID Connect).
            </ListItem>
            <ListItem sx={{ display: 'list-item', py: 0.5 }}>
              3.b Creer aussi un client public {savedClientId} pour ce frontend avec redirect URI: {redirectUriExample}
            </ListItem>
            <ListItem sx={{ display: 'list-item', py: 0.5 }}>
              4. Activer Service Accounts pour le client si necessaire pour des appels machine-to-machine.
            </ListItem>
            <ListItem sx={{ display: 'list-item', py: 0.5 }}>
              5. Verifier l issuer du royaume: {savedUrl}realms/{savedRealm}
            </ListItem>
            <ListItem sx={{ display: 'list-item', py: 0.5 }}>
              6. Assigner les roles aux utilisateurs (ou groupes) qui accederont a la gateway.
            </ListItem>
            <ListItem sx={{ display: 'list-item', py: 0.5 }}>
              7. Dans la gateway Docker, definir DATA_GATEWAY_AUTH_URL, DATA_GATEWAY_AUTH_REALM, DATA_GATEWAY_AUTH_CLIENT_ID et DATA_GATEWAY_ALLOWED_ROLES.
            </ListItem>
          </List>
        </Stack>
      </Paper>
    </Stack>
  );
};
