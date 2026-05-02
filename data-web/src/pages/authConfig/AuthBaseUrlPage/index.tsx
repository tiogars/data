import { useMemo, useState } from 'react';
import Alert from '@mui/material/Alert';
import Box from '@mui/material/Box';
import Button from '@mui/material/Button';
import List from '@mui/material/List';
import ListItem from '@mui/material/ListItem';
import Paper from '@mui/material/Paper';
import Stack from '@mui/material/Stack';
import TextField from '@mui/material/TextField';
import Typography from '@mui/material/Typography';
import {
  AUTH_BASE_URL_DEFAULT,
  getAuthBaseUrl,
  resetAuthBaseUrl,
  setAuthBaseUrl,
} from '../../../services/emptyApi';

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
  const [draftUrl, setDraftUrl] = useState(() => getAuthBaseUrl());
  const [savedUrl, setSavedUrl] = useState(() => getAuthBaseUrl());
  const [status, setStatus] = useState<'idle' | 'saved' | 'reset'>('idle');

  const validationError = useMemo(() => validateAuthUrl(draftUrl), [draftUrl]);

  const handleSave = () => {
    if (validationError) {
      return;
    }

    setAuthBaseUrl(draftUrl);
    const effective = getAuthBaseUrl();
    setSavedUrl(effective);
    setDraftUrl(effective);
    setStatus('saved');
  };

  const handleReset = () => {
    resetAuthBaseUrl();
    setDraftUrl(AUTH_BASE_URL_DEFAULT);
    setSavedUrl(AUTH_BASE_URL_DEFAULT);
    setStatus('reset');
  };

  return (
    <Stack spacing={3} sx={{ p: { xs: 2, md: 3 }, maxWidth: 980, mx: 'auto' }}>
      <Box>
        <Typography variant="h4" component="h1">
          Configuration URL d authentification
        </Typography>
        <Typography variant="body2" color="text.secondary">
          Definissez l URL de base du serveur d authentification. Cette valeur est stockee dans le Local Storage.
        </Typography>
      </Box>

      <Alert severity="info">
        Cle Local Storage: auth-base-url. Valeur par defaut: {AUTH_BASE_URL_DEFAULT}
      </Alert>

      <Paper variant="outlined" sx={{ p: { xs: 2, md: 2.5 } }}>
        <Stack spacing={2}>
          <Typography variant="h6">URL d authentification active</Typography>

          <TextField
            label="URL d authentification"
            value={draftUrl}
            onChange={(event) => {
              setDraftUrl(event.target.value);
              setStatus('idle');
            }}
            placeholder="https://auth2.tiogars.fr/"
            fullWidth
            error={Boolean(validationError)}
            helperText={validationError ?? 'Exemple: https://auth2.tiogars.fr/'}
          />

          <Stack direction={{ xs: 'column', sm: 'row' }} spacing={1.2}>
            <Button variant="contained" onClick={handleSave} disabled={Boolean(validationError)}>
              Enregistrer
            </Button>
            <Button variant="outlined" onClick={handleReset}>
              Reinitialiser
            </Button>
          </Stack>

          {status === 'saved' && (
            <Alert severity="success">URL d authentification enregistree.</Alert>
          )}

          {status === 'reset' && (
            <Alert severity="success">URL reinitialisee sur la valeur par defaut.</Alert>
          )}

          <Typography variant="body2" color="text.secondary">
            URL actuellement appliquee: {savedUrl}
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
              4. Activer Service Accounts pour le client si necessaire pour des appels machine-to-machine.
            </ListItem>
            <ListItem sx={{ display: 'list-item', py: 0.5 }}>
              5. Verifier l issuer du royaume: {savedUrl}realms/data
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
