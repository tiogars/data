import type { FC } from 'react';
import { Link as RouterLink } from 'react-router-dom';
import Box from '@mui/material/Box';
import Button from '@mui/material/Button';
import Chip from '@mui/material/Chip';
import Divider from '@mui/material/Divider';
import Paper from '@mui/material/Paper';
import Stack from '@mui/material/Stack';
import Typography from '@mui/material/Typography';
import { MaisonReference } from '../../../components/MaisonReference';
import { useGetVinQuery } from '../../../services/vinApi';
import type { VinDetailPageProps } from './VinDetailPage.types';

const renderValue = (value?: string | number | null) => value ?? '-';

const formatDegorgement = (mois?: number, annee?: number) => {
  if (mois == null && annee == null) {
    return '-';
  }
  if (mois == null || annee == null) {
    return renderValue(annee ?? mois);
  }
  return `${String(mois).padStart(2, '0')} / ${annee}`;
};

const formatDosage = (dosage?: number) => {
  if (dosage == null) {
    return '-';
  }
  return `${dosage} g/l`;
};

const formatCepageLabel = (name?: string, id?: string, pourcentage?: number) => {
  const baseLabel = name || id || 'Cepage';
  if (pourcentage === undefined) {
    return baseLabel;
  }
  return `${baseLabel} (${pourcentage}%)`;
};

export const VinDetailPage: FC<VinDetailPageProps> = ({ id }) => {
  const { data, isLoading, error } = useGetVinQuery({ id });

  if (isLoading) return <div>Chargement...</div>;
  if (error) return <div>Erreur lors du chargement du vin</div>;
  if (!data) return <div>Vin introuvable</div>;

  const circonstances = data.circonstanceNames?.length ? data.circonstanceNames : data.circonstances ?? [];
  const tags = data.tagNames?.length ? data.tagNames : data.tags ?? [];

  return (
    <Paper variant="outlined" sx={{ p: { xs: 2.5, md: 3 }, maxWidth: 960, mx: 'auto', mt: 3 }}>
      <Stack spacing={2}>
        <Stack direction={{ xs: 'column', sm: 'row' }} spacing={1.5} sx={{ justifyContent: 'space-between', alignItems: { sm: 'center' } }}>
          <Typography variant="h4" component="h1">{data.vinNomName || data.maisonName || data.appellationName || 'Vin'}</Typography>
          <Button component={RouterLink} to={`/vin/${id}/edit`} variant="contained">Modifier</Button>
        </Stack>
        <Divider />
        <Box><Typography variant="overline" color="text.secondary">Appellation</Typography><Typography>{renderValue(data.appellationName)}</Typography></Box>
        <Box><Typography variant="overline" color="text.secondary">Couleur</Typography><Typography>{renderValue(data.couleurName)}</Typography></Box>
        <Box><Typography variant="overline" color="text.secondary">Type de vin</Typography><Typography>{renderValue(data.typeVinName)}</Typography></Box>
        <Box><Typography variant="overline" color="text.secondary">Maison</Typography><Typography><MaisonReference maisonId={data.maisonId} maisonName={data.maisonName} showWebsite /></Typography></Box>
        <Box><Typography variant="overline" color="text.secondary">Nom du vin</Typography><Typography>{renderValue(data.vinNomName)}</Typography></Box>
        <Box><Typography variant="overline" color="text.secondary">Contenant</Typography><Typography>{renderValue(data.contenantName)}</Typography></Box>
        <Box><Typography variant="overline" color="text.secondary">Annee</Typography><Typography>{renderValue(data.annee)}</Typography></Box>
        <Box><Typography variant="overline" color="text.secondary">Date de degorgement</Typography><Typography>{formatDegorgement(data.degorgementMois, data.degorgementAnnee)}</Typography></Box>
        <Box><Typography variant="overline" color="text.secondary">Dosage</Typography><Typography>{formatDosage(data.dosageGrammesParLitre)}</Typography></Box>
        <Box><Typography variant="overline" color="text.secondary">Commune</Typography><Typography>{renderValue(data.commune)}</Typography></Box>
        <Box><Typography variant="overline" color="text.secondary">Region</Typography><Typography>{renderValue(data.region)}</Typography></Box>
        <Box><Typography variant="overline" color="text.secondary">Commentaires</Typography><Typography>{data.commentaires || 'Aucun commentaire'}</Typography></Box>
        <Box><Typography variant="overline" color="text.secondary">Accords mets / vins</Typography><Typography>{data.accordsMetsVins || 'Aucun accord renseigne'}</Typography></Box>
        <Box>
          <Typography variant="overline" color="text.secondary">Cepages</Typography>
          <Stack direction="row" spacing={1} useFlexGap sx={{ flexWrap: 'wrap', mt: 1 }}>
            {(data.cepages ?? []).length > 0
              ? (data.cepages ?? []).map((cepage, index) => (
                  <Chip
                    key={`${cepage.cepageId ?? index}-${cepage.pourcentage ?? 'x'}`}
                    label={formatCepageLabel(cepage.cepageName, cepage.cepageId, cepage.pourcentage)}
                  />
                ))
              : <Typography>Aucun cepage</Typography>}
          </Stack>
        </Box>
        <Box>
          <Typography variant="overline" color="text.secondary">Circonstances</Typography>
          <Stack direction="row" spacing={1} useFlexGap sx={{ flexWrap: 'wrap', mt: 1 }}>
            {circonstances.length > 0 ? circonstances.map((item) => <Chip key={item} label={item} />) : <Typography>Aucune circonstance</Typography>}
          </Stack>
        </Box>
        <Box>
          <Typography variant="overline" color="text.secondary">Tags</Typography>
          <Stack direction="row" spacing={1} useFlexGap sx={{ flexWrap: 'wrap', mt: 1 }}>
            {tags.length > 0 ? tags.map((item) => <Chip key={item} label={item} />) : <Typography>Aucun tag</Typography>}
          </Stack>
        </Box>
        <Box><Typography variant="overline" color="text.secondary">Identifiant</Typography><Typography color="text.secondary">{data.id}</Typography></Box>
      </Stack>
    </Paper>
  );
};
