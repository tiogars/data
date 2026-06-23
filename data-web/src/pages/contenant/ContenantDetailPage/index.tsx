import type { FC } from 'react';
import { Link as RouterLink } from 'react-router-dom';
import Box from '@mui/material/Box';
import Button from '@mui/material/Button';
import Divider from '@mui/material/Divider';
import Paper from '@mui/material/Paper';
import Stack from '@mui/material/Stack';
import Typography from '@mui/material/Typography';
import { useGetContenantQuery } from '../../../services/contenantApi';
import type { ContenantDetailPageProps } from './ContenantDetailPage.types';

export const ContenantDetailPage: FC<ContenantDetailPageProps> = ({ id }) => {
  const { data, isLoading, error } = useGetContenantQuery({ id });
  if (isLoading) return <div>Chargement...</div>;
  if (error) return <div>Erreur lors du chargement du contenant</div>;
  if (!data) return <div>Contenant introuvable</div>;
  return <Paper variant="outlined" sx={{ p: { xs: 2.5, md: 3 }, maxWidth: 760, mx: 'auto', mt: 3 }}><Stack spacing={2}><Stack direction={{ xs: 'column', sm: 'row' }} spacing={1.5} sx={{ justifyContent: 'space-between', alignItems: { sm: 'center' } }}><Typography variant="h4" component="h1">{data.name || 'Contenant'}</Typography><Button component={RouterLink} to={`/contenant/${id}/edit`} variant="contained">Modifier</Button></Stack><Divider /><Box><Typography variant="overline" color="text.secondary">Nom</Typography><Typography>{data.name || '-'}</Typography></Box><Box><Typography variant="overline" color="text.secondary">Volume (cl)</Typography><Typography>{data.volumeCl ?? '-'}</Typography></Box><Box><Typography variant="overline" color="text.secondary">Identifiant</Typography><Typography color="text.secondary">{data.id}</Typography></Box></Stack></Paper>;
};
