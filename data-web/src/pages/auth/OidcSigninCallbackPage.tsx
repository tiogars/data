import { useEffect } from 'react';
import { Alert, Box, CircularProgress, Stack, Typography } from '@mui/material';
import { useNavigate } from 'react-router-dom';
import { useOidcAuth } from '../../auth/OidcAuthProvider';

export const OidcSigninCallbackPage = () => {
  const navigate = useNavigate();
  const { handleSigninCallback, errorMessage } = useOidcAuth();

  useEffect(() => {
    const run = async () => {
      try {
        await handleSigninCallback();
        navigate('/', { replace: true });
      } catch {
        // Keep the user on this page to display callback errors.
      }
    };

    void run();
  }, [handleSigninCallback, navigate]);

  return (
    <Stack spacing={2} sx={{ p: { xs: 2, md: 3 }, maxWidth: 640, mx: 'auto' }}>
      <Typography variant="h5" component="h1">
        Finalisation de la connexion
      </Typography>
      <Box sx={{ display: 'flex', alignItems: 'center', gap: 1.5 }}>
        <CircularProgress size={22} />
        <Typography variant="body2" color="text.secondary">
          Traitement du callback OIDC en cours...
        </Typography>
      </Box>
      {errorMessage && <Alert severity="error">{errorMessage}</Alert>}
    </Stack>
  );
};
