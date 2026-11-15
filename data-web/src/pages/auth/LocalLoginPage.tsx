import { useState } from 'react';
import { useForm } from 'react-hook-form';
import { Link as RouterLink, Navigate, useNavigate } from 'react-router-dom';
import { Alert, Box, Button, Paper, Stack, TextField, Typography } from '@mui/material';
import { useOidcAuth } from '../../auth/OidcAuthProvider';

interface LocalLoginFormValues {
  username: string;
  password: string;
}

export const LocalLoginPage = () => {
  const { authMode, isAuthenticated, isLoading, login, loginWithCredentials } = useOidcAuth();
  const navigate = useNavigate();
  const [submitError, setSubmitError] = useState<string | null>(null);
  const {
    register,
    handleSubmit,
    formState: { errors, isSubmitting },
  } = useForm<LocalLoginFormValues>({ defaultValues: { username: '', password: '' } });

  if (isAuthenticated) {
    return <Navigate to="/" replace />;
  }

  const onSubmit = async (values: LocalLoginFormValues) => {
    setSubmitError(null);
    try {
      await loginWithCredentials(values.username.trim(), values.password);
      navigate('/', { replace: true });
    } catch (error) {
      setSubmitError(error instanceof Error ? error.message : 'Echec de la connexion.');
    }
  };

  if (authMode === 'oidc') {
    return (
      <Box sx={{ p: { xs: 2, md: 3 } }}>
        <Stack spacing={2} sx={{ maxWidth: 480, mx: 'auto' }}>
          <Typography variant="h4" component="h1">
            Connexion
          </Typography>
          <Alert severity="info">
            L authentification par URL d authentification (OIDC) est active. Pour utiliser un compte utilisateur
            enregistre en base, desactivez-la depuis la page{' '}
            <RouterLink to="/auth-config">Configuration URL d authentification</RouterLink>.
          </Alert>
          <Button
            variant="contained"
            onClick={() => void login()}
            disabled={isLoading}
            sx={{ alignSelf: 'flex-start' }}
          >
            Connexion OIDC
          </Button>
        </Stack>
      </Box>
    );
  }

  return (
    <Box sx={{ p: { xs: 2, md: 3 } }}>
      <Paper variant="outlined" sx={{ p: { xs: 2, md: 3 }, maxWidth: 480, mx: 'auto' }}>
        <Stack component="form" spacing={2} onSubmit={handleSubmit(onSubmit)} noValidate>
          <Typography variant="h4" component="h1">
            Connexion
          </Typography>
          <Typography variant="body2" color="text.secondary">
            Connectez-vous avec votre compte utilisateur.
          </Typography>

          {submitError && <Alert severity="error">{submitError}</Alert>}

          <TextField
            label="Nom d'utilisateur"
            autoComplete="username"
            autoFocus
            fullWidth
            error={Boolean(errors.username)}
            helperText={errors.username?.message}
            {...register('username', { required: "Le nom d'utilisateur est obligatoire." })}
          />

          <TextField
            label="Mot de passe"
            type="password"
            autoComplete="current-password"
            fullWidth
            error={Boolean(errors.password)}
            helperText={errors.password?.message}
            {...register('password', { required: 'Le mot de passe est obligatoire.' })}
          />

          <Button type="submit" variant="contained" disabled={isSubmitting}>
            {isSubmitting ? 'Connexion en cours...' : 'Se connecter'}
          </Button>
        </Stack>
      </Paper>
    </Box>
  );
};
