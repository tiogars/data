import type { FC } from 'react';
import { Link as RouterLink } from 'react-router-dom';
import Box from '@mui/material/Box';
import Button from '@mui/material/Button';
import Divider from '@mui/material/Divider';
import List from '@mui/material/List';
import ListItem from '@mui/material/ListItem';
import ListItemText from '@mui/material/ListItemText';
import Paper from '@mui/material/Paper';
import Stack from '@mui/material/Stack';
import Typography from '@mui/material/Typography';
import { useGetModelByIdQuery } from '../../../services/modelApi';
import type { ModelDetailPageProps } from './ModelDetailPage.types';

export const ModelDetailPage: FC<ModelDetailPageProps> = ({ id }) => {
  const { data, isLoading, error } = useGetModelByIdQuery({ id });

  if (isLoading) return <div>Chargement...</div>;
  if (error) return <div>Erreur lors du chargement du modele</div>;
  if (!data) return <div>Modele introuvable</div>;

  return (
    <Paper variant="outlined" sx={{ p: { xs: 2.5, md: 3 }, maxWidth: 760, mx: 'auto', mt: 3 }}>
      <Stack spacing={2}>
        <Stack direction={{ xs: 'column', sm: 'row' }} spacing={1.5} sx={{ justifyContent: 'space-between', alignItems: { sm: 'center' } }}>
          <Typography variant="h4" component="h1">
            {data.name || 'Modele'}
          </Typography>
          <Button component={RouterLink} to={`/model/${id}/edit`} variant="contained">
            Modifier
          </Button>
        </Stack>
        <Divider />
        <Box>
          <Typography variant="overline" color="text.secondary">Nom</Typography>
          <Typography>{data.name}</Typography>
        </Box>
        <Box>
          <Typography variant="overline" color="text.secondary">Description</Typography>
          <Typography>{data.description || 'Aucune description'}</Typography>
        </Box>
        <Box>
          <Typography variant="overline" color="text.secondary">Attributs</Typography>
          {(!data.modelAttributes || data.modelAttributes.length === 0) ? (
            <Typography>Aucun attribut</Typography>
          ) : (
            <List disablePadding>
              {data.modelAttributes.map((attribute, index) => (
                <ListItem key={attribute.id ?? `${attribute.name ?? 'attr'}-${index}`} disableGutters>
                  <ListItemText
                    primary={attribute.name || 'Attribut'}
                    secondary={attribute.description || 'Aucune description'}
                  />
                </ListItem>
              ))}
            </List>
          )}
        </Box>
        <Box>
          <Typography variant="overline" color="text.secondary">Identifiant</Typography>
          <Typography color="text.secondary">{data.id}</Typography>
        </Box>
      </Stack>
    </Paper>
  );
};
