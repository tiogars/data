import { Alert, Box, Button, Stack, Typography } from '@mui/material';
import OpenInNewIcon from '@mui/icons-material/OpenInNew';
import { useOidcAuth } from '../../auth/OidcAuthProvider';
import { buildProviderAccountUrl, getAuthRealm } from '../../services/runtimeConfig';

export const OidcUserAccountPage = () => {
  const { user, isAuthenticated, login, isLoading } = useOidcAuth();
  const preferredUsername = typeof user?.profile?.preferred_username === 'string' ? user.profile.preferred_username : null;
  const realm = getAuthRealm();
  const providerAccountPath = `/realms/${realm}/account`;
  const providerAccountUrl = buildProviderAccountUrl();

  const handleLogin = () => {
    void login();
  };

  return (
    <Box sx={{ p: { xs: 2, md: 3 } }}>
      <Stack spacing={2} sx={{ maxWidth: 760 }}>
        <Typography variant="h4">Compte utilisateur</Typography>
        <Typography color="text.secondary">
          Cette page donne acces au compte de l'utilisateur connecte dans le provider OIDC.
        </Typography>

        {!isAuthenticated && (
          <Alert
            severity="info"
            action={
              <Button size="small" variant="contained" onClick={handleLogin} disabled={isLoading}>
                Connexion
              </Button>
            }
          >
            Vous devez etre connecte pour gerer votre compte.
          </Alert>
        )}

        {isAuthenticated && (
          <Stack spacing={1.5}>
            <Typography>
              Utilisateur connecte : <strong>{preferredUsername ?? 'Inconnu'}</strong>
            </Typography>
            <Typography variant="body2" color="text.secondary">
              Chemin provider attendu : {providerAccountPath}
            </Typography>
            <Button
              variant="contained"
              color="primary"
              href={providerAccountUrl}
              target="_blank"
              rel="noreferrer"
              startIcon={<OpenInNewIcon />}
              sx={{ alignSelf: 'flex-start' }}
            >
              Ouvrir mon compte sur le provider
            </Button>
          </Stack>
        )}
      </Stack>
    </Box>
  );
};
