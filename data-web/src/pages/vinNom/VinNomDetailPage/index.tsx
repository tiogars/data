import type { FC } from 'react';
import { Link as RouterLink } from 'react-router-dom';
import Box from '@mui/material/Box';
import Button from '@mui/material/Button';
import Divider from '@mui/material/Divider';
import Paper from '@mui/material/Paper';
import Stack from '@mui/material/Stack';
import Typography from '@mui/material/Typography';
import { MaisonReference } from '../../../components/MaisonReference';
import { useGetVinNomQuery } from '../../../services/vinNomApi';
import type { VinNomDetailPageProps } from './VinNomDetailPage.types';

export const VinNomDetailPage: FC<VinNomDetailPageProps> = ({ id }) => {
  const { data, isLoading, error } = useGetVinNomQuery({ id });
  if (isLoading) return <div>Chargement...</div>;
  if (error) return <div>Erreur lors du chargement du nom de vin</div>;
  if (!data) return <div>Nom de vin introuvable</div>;
  return <Paper variant="outlined" sx={{ p: { xs: 2.5, md: 3 }, maxWidth: 760, mx: 'auto', mt: 3 }}><Stack spacing={2}><Stack direction={{ xs: 'column', sm: 'row' }} spacing={1.5} sx={{ justifyContent: 'space-between', alignItems: { sm: 'center' } }}><Typography variant="h4" component="h1">{data.name || 'Nom de vin'}</Typography><Button component={RouterLink} to={`/vin-nom/${id}/edit`} variant="contained">Modifier</Button></Stack><Divider /><Box><Typography variant="overline" color="text.secondary">Nom</Typography><Typography>{data.name || '-'}</Typography></Box><Box><Typography variant="overline" color="text.secondary">Maison</Typography><Typography><MaisonReference maisonId={data.maisonId} maisonName={data.maisonName} showWebsite /></Typography></Box><Box><Typography variant="overline" color="text.secondary">Identifiant</Typography><Typography color="text.secondary">{data.id}</Typography></Box></Stack></Paper>;
};
