import type { FC } from 'react';
import { Link as RouterLink } from 'react-router-dom';
import Box from '@mui/material/Box';
import Button from '@mui/material/Button';
import Chip from '@mui/material/Chip';
import Divider from '@mui/material/Divider';
import Paper from '@mui/material/Paper';
import Stack from '@mui/material/Stack';
import Typography from '@mui/material/Typography';
import { useGetAndroidQuery } from '../../../services/androidApi';
import type { AndroidDetailPageProps } from './AndroidDetailPage.types';

export const AndroidDetailPage: FC<AndroidDetailPageProps> = ({ id }) => {
  const { data, isLoading, error } = useGetAndroidQuery({ id });

  if (isLoading) return <div>Chargement...</div>;
  if (error) return <div>Erreur lors du chargement de l'application Android</div>;
  if (!data) return <div>Application Android introuvable</div>;

  return (
    <Paper variant="outlined" sx={{ p: { xs: 2.5, md: 3 }, maxWidth: 760, mx: 'auto', mt: 3 }}>
      <Stack spacing={2}>
        <Stack direction={{ xs: 'column', sm: 'row' }} spacing={1.5} sx={{ justifyContent: 'space-between', alignItems: { sm: 'center' } }}>
          <Typography variant="h4" component="h1">
            {data.name || 'Application Android'}
          </Typography>
          <Button component={RouterLink} to={`/android/${id}/edit`} variant="contained">
            Modifier
          </Button>
        </Stack>
        <Divider />
        {data.icon && (
          <Box
            component="img"
            src={data.icon}
            alt={data.name ? `Icone de ${data.name}` : 'Icone de lapplication Android'}
            sx={{ width: 96, height: 96, objectFit: 'contain', borderRadius: 2, border: '1px solid', borderColor: 'divider', p: 1 }}
          />
        )}
        <Box>
          <Typography variant="overline" color="text.secondary">Nom</Typography>
          <Typography>{data.name}</Typography>
        </Box>
        <Box>
          <Typography variant="overline" color="text.secondary">Package</Typography>
          <Typography>{data.packageName || 'Aucun package'}</Typography>
        </Box>
        <Box>
          <Typography variant="overline" color="text.secondary">Categories</Typography>
          <Stack direction="row" spacing={0.5} useFlexGap sx={{ flexWrap: 'wrap', mt: 0.5 }}>
            {(data.category ?? []).length > 0 ? data.category?.map((category) => (
              <Chip key={category} label={category} size="small" variant="outlined" />
            )) : <Typography>Aucune categorie</Typography>}
          </Stack>
        </Box>
        <Box>
          <Typography variant="overline" color="text.secondary">Description</Typography>
          <Typography>{data.description || 'Aucune description'}</Typography>
        </Box>
        <Box>
          <Typography variant="overline" color="text.secondary">Identifiant</Typography>
          <Typography color="text.secondary">{data.id}</Typography>
        </Box>
      </Stack>
    </Paper>
  );
};