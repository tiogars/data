import type { FC } from 'react';
import { Link as RouterLink } from 'react-router-dom';
import Box from '@mui/material/Box';
import Button from '@mui/material/Button';
import Chip from '@mui/material/Chip';
import Divider from '@mui/material/Divider';
import Paper from '@mui/material/Paper';
import Stack from '@mui/material/Stack';
import Typography from '@mui/material/Typography';
import { renderMenuItemIcon } from '../../../features/menuItem/iconRegistry';
import { useGetMenuItemByIdQuery } from '../../../services/menuItemApi';
import type { MenuItemDetailPageProps } from './MenuItemDetailPage.types';

export const MenuItemDetailPage: FC<MenuItemDetailPageProps> = ({ id }) => {
  const { data, isLoading, error } = useGetMenuItemByIdQuery({ id });

  if (isLoading) return <div>Chargement...</div>;
  if (error) return <div>Erreur lors du chargement de l'entree de menu</div>;
  if (!data) return <div>Entree de menu introuvable</div>;

  return (
    <Paper variant="outlined" sx={{ p: { xs: 2.5, md: 3 }, maxWidth: 760, mx: 'auto', mt: 3 }}>
      <Stack spacing={2}>
        <Stack direction={{ xs: 'column', sm: 'row' }} spacing={1.5} sx={{ justifyContent: 'space-between', alignItems: { sm: 'center' } }}>
          <Typography variant="h4" component="h1">
            {data.label || 'Entree de menu'}
          </Typography>
          <Button component={RouterLink} to={`/menu-item/${id}/edit`} variant="contained">
            Modifier
          </Button>
        </Stack>
        <Divider />
        <Box>
          <Typography variant="overline" color="text.secondary">Icone</Typography>
          <Box sx={{ mt: 0.5 }}>{renderMenuItemIcon(data.icon, 'medium')}</Box>
        </Box>
        <Box>
          <Typography variant="overline" color="text.secondary">Chemin</Typography>
          <Typography>{data.path}</Typography>
        </Box>
        <Box>
          <Typography variant="overline" color="text.secondary">Ordre d'affichage</Typography>
          <Typography>{data.displayOrder ?? 0}</Typography>
        </Box>
        <Box>
          <Typography variant="overline" color="text.secondary">Charge par defaut</Typography>
          <Box sx={{ mt: 0.5 }}>
            <Chip size="small" label={data.defaultLoaded ? 'Oui' : 'Non'} color={data.defaultLoaded ? 'success' : 'default'} variant={data.defaultLoaded ? 'filled' : 'outlined'} />
          </Box>
        </Box>
        <Box>
          <Typography variant="overline" color="text.secondary">Identifiant</Typography>
          <Typography color="text.secondary">{data.id}</Typography>
        </Box>
      </Stack>
    </Paper>
  );
};
