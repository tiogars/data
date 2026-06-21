import type { FC } from 'react';
import { Link as RouterLink } from 'react-router-dom';
import Box from '@mui/material/Box';
import Button from '@mui/material/Button';
import Divider from '@mui/material/Divider';
import Paper from '@mui/material/Paper';
import Stack from '@mui/material/Stack';
import Typography from '@mui/material/Typography';
import { useGetUserAccountQuery } from '../../../services/userAccountApi';
import type { UserAccountDetailPageProps } from './UserAccountDetailPage.types';

export const UserAccountDetailPage: FC<UserAccountDetailPageProps> = ({ id }) => {
  const { data, isLoading, error } = useGetUserAccountQuery({ id });

  if (isLoading) return <div>Chargement...</div>;
  if (error) return <div>Erreur lors du chargement du compte utilisateur</div>;
  if (!data) return <div>Compte utilisateur introuvable</div>;

  return (
    <Paper variant="outlined" sx={{ p: { xs: 2.5, md: 3 }, maxWidth: 760, mx: 'auto', mt: 3 }}>
      <Stack spacing={2}>
        <Stack direction={{ xs: 'column', sm: 'row' }} spacing={1.5} sx={{ justifyContent: 'space-between', alignItems: { sm: 'center' } }}>
          <Typography variant="h4" component="h1">
            {data.username || 'Compte utilisateur'}
          </Typography>
          <Button component={RouterLink} to={`/user-account/${id}/edit`} variant="contained">
            Modifier
          </Button>
        </Stack>
        <Divider />
        <Box>
          <Typography variant="overline" color="text.secondary">Nom utilisateur</Typography>
          <Typography>{data.username}</Typography>
        </Box>
        <Box>
          <Typography variant="overline" color="text.secondary">Role</Typography>
          <Typography>{data.role}</Typography>
        </Box>
        <Box>
          <Typography variant="overline" color="text.secondary">Etat</Typography>
          <Typography>{data.enabled ? 'Actif' : 'Desactive'}</Typography>
        </Box>
        <Box>
          <Typography variant="overline" color="text.secondary">Identifiant</Typography>
          <Typography color="text.secondary">{data.id}</Typography>
        </Box>
      </Stack>
    </Paper>
  );
};
