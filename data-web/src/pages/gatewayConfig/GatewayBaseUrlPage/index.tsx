import { useMemo, useState } from 'react';
import Alert from '@mui/material/Alert';
import Box from '@mui/material/Box';
import Button from '@mui/material/Button';
import Chip from '@mui/material/Chip';
import Paper from '@mui/material/Paper';
import Stack from '@mui/material/Stack';
import TextField from '@mui/material/TextField';
import Typography from '@mui/material/Typography';
import {
  API_BASE_URL_DEFAULT,
  getGatewayBaseUrl,
  isGatewayBaseUrlOverriddenByEnv,
  resetGatewayBaseUrl,
  setGatewayBaseUrl,
} from '../../../services/emptyApi';

type GatewayHealthPayload = {
  status?: string;
};

type GatewayHealthCheckState = {
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

const buildGatewayHealthUrl = (value: string) => `${stripTrailingSlashes(value.trim())}/actuator/health`;

const validateGatewayUrl = (value: string) => {
  const normalized = value.trim();
  if (!normalized) {
    return 'L URL de la gateway est obligatoire.';
  }

  try {
    const parsed = new URL(normalized);
    if (!parsed.protocol.startsWith('http')) {
      return 'L URL doit commencer par http:// ou https://';
    }
    return null;
  } catch {
    return 'URL invalide. Exemple: http://localhost:8081';
  }
};

export const GatewayBaseUrlPage = () => {
  const envOverrideEnabled = isGatewayBaseUrlOverriddenByEnv();

  const [draftUrl, setDraftUrl] = useState(() => getGatewayBaseUrl());
  const [savedUrl, setSavedUrl] = useState(() => getGatewayBaseUrl());
  const [status, setStatus] = useState<'idle' | 'saved' | 'reset'>('idle');
  const [isHealthChecking, setIsHealthChecking] = useState(false);
  const [healthCheckState, setHealthCheckState] = useState<GatewayHealthCheckState>({ type: 'idle', message: '' });

  const validationError = useMemo(() => validateGatewayUrl(draftUrl), [draftUrl]);

  const handleSave = () => {
    if (envOverrideEnabled || validationError) {
      return;
    }

    setGatewayBaseUrl(draftUrl);
    const effective = getGatewayBaseUrl();
    setSavedUrl(effective);
    setDraftUrl(effective);
    setStatus('saved');
  };

  const handleReset = () => {
    if (envOverrideEnabled) {
      return;
    }

    resetGatewayBaseUrl();
    setDraftUrl(API_BASE_URL_DEFAULT);
    setSavedUrl(API_BASE_URL_DEFAULT);
    setStatus('reset');
    setHealthCheckState({ type: 'idle', message: '' });
  };

  const handleHealthCheck = async () => {
    if (validationError) {
      setHealthCheckState({ type: 'error', message: validationError });
      return;
    }

    const endpoint = buildGatewayHealthUrl(draftUrl);
    setIsHealthChecking(true);
    setHealthCheckState({ type: 'idle', message: '' });

    try {
      const response = await fetch(endpoint, {
        method: 'GET',
        headers: {
          Accept: 'application/json',
        },
      });

      let payload: GatewayHealthPayload | null = null;
      try {
        payload = (await response.json()) as GatewayHealthPayload;
      } catch {
        payload = null;
      }

      if (!response.ok) {
        setHealthCheckState({
          type: 'error',
          message: `Test echoue (${response.status} ${response.statusText}). Endpoint teste: ${endpoint}`,
        });
        return;
      }

      const healthStatus = payload?.status?.toUpperCase();
      if (healthStatus && healthStatus !== 'UP') {
        setHealthCheckState({
          type: 'error',
          message: `Gateway joignable mais statut de sante '${healthStatus}' (endpoint: ${endpoint}).`,
        });
        return;
      }

      const healthStatusSuffix = healthStatus ? ` (status: ${healthStatus})` : '';

      setHealthCheckState({
        type: 'success',
        message: `Gateway joignable. /actuator/health repond correctement${healthStatusSuffix}.`,
      });
    } catch (error) {
      const message = error instanceof Error ? error.message : 'Erreur reseau inconnue';
      setHealthCheckState({
        type: 'error',
        message: `Impossible de joindre ${endpoint}. ${message}`,
      });
    } finally {
      setIsHealthChecking(false);
    }
  };

  return (
    <Stack spacing={3} sx={{ p: { xs: 2, md: 3 }, maxWidth: 880, mx: 'auto' }}>
      <Box>
        <Typography variant="h4" component="h1">
          Configuration de la gateway
        </Typography>
        <Typography variant="body2" color="text.secondary">
          Definissez l URL du backend utilisee par les appels API du frontend.
        </Typography>
      </Box>

      {envOverrideEnabled ? (
        <Alert severity="info">
          La variable d environnement VITE_API_BASE_URL est definie. Cette valeur surcharge la configuration locale.
        </Alert>
      ) : (
        <Alert severity="info">
          La valeur est stockee dans le Local Storage du navigateur (cle: api-base-url).
        </Alert>
      )}

      <Paper variant="outlined" sx={{ p: { xs: 2, md: 2.5 } }}>
        <Stack spacing={2}>
          <Stack direction={{ xs: 'column', sm: 'row' }} spacing={1.2} sx={{ alignItems: { xs: 'flex-start', sm: 'center' } }}>
            <Typography variant="h6">URL de gateway active</Typography>
            <Chip size="small" color={envOverrideEnabled ? 'warning' : 'default'} label={envOverrideEnabled ? 'Surcharge ENV' : 'Local Storage'} />
          </Stack>

          <TextField
            label="URL de la gateway"
            value={draftUrl}
            onChange={(event) => {
              setDraftUrl(event.target.value);
              setStatus('idle');
            }}
            placeholder="http://localhost:8081"
            fullWidth
            disabled={envOverrideEnabled}
            error={Boolean(validationError)}
            helperText={validationError ?? 'Exemple: http://localhost:8081'}
          />

          <Stack direction={{ xs: 'column', sm: 'row' }} spacing={1.2}>
            <Button variant="contained" onClick={handleSave} disabled={envOverrideEnabled || Boolean(validationError)}>
              Enregistrer
            </Button>
            <Button variant="outlined" onClick={handleReset} disabled={envOverrideEnabled}>
              Reinitialiser
            </Button>
            <Button variant="outlined" onClick={handleHealthCheck} disabled={Boolean(validationError) || isHealthChecking}>
              {isHealthChecking ? 'Test en cours...' : 'Tester /actuator/health'}
            </Button>
          </Stack>

          {status === 'saved' && (
            <Alert severity="success">URL enregistree. Les prochaines requetes API utiliseront cette gateway.</Alert>
          )}

          {status === 'reset' && (
            <Alert severity="success">URL reinitialisee sur la valeur par defaut.</Alert>
          )}

          {healthCheckState.type === 'success' && (
            <Alert severity="success">{healthCheckState.message}</Alert>
          )}

          {healthCheckState.type === 'error' && (
            <Alert severity="error">{healthCheckState.message}</Alert>
          )}

          <Typography variant="body2" color="text.secondary">
            URL actuellement appliquee: {savedUrl}
          </Typography>
        </Stack>
      </Paper>
    </Stack>
  );
};
