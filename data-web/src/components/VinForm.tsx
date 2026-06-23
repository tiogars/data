import AddIcon from '@mui/icons-material/Add';
import DeleteIcon from '@mui/icons-material/Delete';
import Button from '@mui/material/Button';
import FormControl from '@mui/material/FormControl';
import FormHelperText from '@mui/material/FormHelperText';
import IconButton from '@mui/material/IconButton';
import InputLabel from '@mui/material/InputLabel';
import MenuItem from '@mui/material/MenuItem';
import Paper from '@mui/material/Paper';
import Select from '@mui/material/Select';
import Stack from '@mui/material/Stack';
import TextField from '@mui/material/TextField';
import Typography from '@mui/material/Typography';
import { useMemo } from 'react';
import { Controller, useFieldArray, useFormContext } from 'react-hook-form';
import { MaisonReference } from './MaisonReference';
import { useListAppellationsQuery } from '../services/appellationApi';
import { useListCepagesQuery } from '../services/cepageApi';
import { useListCirconstancesQuery } from '../services/circonstanceApi';
import { useListContenantsQuery } from '../services/contenantApi';
import { useListCouleursQuery } from '../services/couleurApi';
import { useListMaisonsQuery } from '../services/maisonApi';
import { useListTypeVinsQuery } from '../services/typeVinApi';
import { useListVinNomsQuery } from '../services/vinNomApi';
import { useListVinTagsQuery } from '../services/vinTagApi';

export type VinFormValues = {
  appellationId: string;
  couleurId: string;
  typeVinId: string;
  maisonId: string;
  vinNomId: string;
  contenantId: string;
  annee: string;
  degorgementMois: string;
  degorgementAnnee: string;
  dosageGrammesParLitre: string;
  commune: string;
  region: string;
  commentaires: string;
  accordsMetsVins: string;
  cepages: { cepageId: string; pourcentage: string }[];
  circonstanceIds: string[];
  tagIds: string[];
};

type VinFormProps = { disabled?: boolean };

const renderSelectedNames = (
  selected: unknown,
  items: ReadonlyArray<{ id: string; name?: string }>,
) => {
  const selectedIds = Array.isArray(selected) ? selected : [];
  return selectedIds
    .map(String)
    .map((id) => items.find((item) => item.id === id)?.name ?? id)
    .join(', ');
};

const VinForm = ({ disabled = false }: VinFormProps) => {
  const { register, control, watch, formState: { errors } } = useFormContext<VinFormValues>();
  const { fields, append, remove } = useFieldArray({ control, name: 'cepages' });
  const { data: appellationsData } = useListAppellationsQuery(undefined, { refetchOnMountOrArgChange: true });
  const { data: couleursData } = useListCouleursQuery(undefined, { refetchOnMountOrArgChange: true });
  const { data: typeVinsData } = useListTypeVinsQuery(undefined, { refetchOnMountOrArgChange: true });
  const { data: maisonsData } = useListMaisonsQuery(undefined, { refetchOnMountOrArgChange: true });
  const { data: vinNomsData } = useListVinNomsQuery(undefined, { refetchOnMountOrArgChange: true });
  const { data: contenantsData } = useListContenantsQuery(undefined, { refetchOnMountOrArgChange: true });
  const { data: cepagesData } = useListCepagesQuery(undefined, { refetchOnMountOrArgChange: true });
  const { data: circonstancesData } = useListCirconstancesQuery(undefined, { refetchOnMountOrArgChange: true });
  const { data: tagsData } = useListVinTagsQuery(undefined, { refetchOnMountOrArgChange: true });
  const appellations = (appellationsData?.items ?? []).filter((item): item is { id: string; name?: string } => Boolean(item.id));
  const couleurs = (couleursData?.items ?? []).filter((item): item is { id: string; name?: string } => Boolean(item.id));
  const typeVins = (typeVinsData?.items ?? []).filter((item): item is { id: string; name?: string } => Boolean(item.id));
  const maisons = (maisonsData?.items ?? []).filter((item): item is { id: string; name?: string } => Boolean(item.id));
  const vinNoms = (vinNomsData?.items ?? []).filter((item): item is { id: string; name?: string; maisonId?: string } => Boolean(item.id));
  const contenants = (contenantsData?.items ?? []).filter((item): item is { id: string; name?: string } => Boolean(item.id));
  const cepages = (cepagesData?.items ?? []).filter((item): item is { id: string; name?: string } => Boolean(item.id));
  const circonstances = (circonstancesData?.items ?? []).filter((item): item is { id: string; name?: string } => Boolean(item.id));
  const tags = (tagsData?.items ?? []).filter((item): item is { id: string; name?: string } => Boolean(item.id));
  const maisonId = watch('maisonId');
  const degorgementMois = watch('degorgementMois');
  const degorgementAnnee = watch('degorgementAnnee');
  const filteredVinNoms = useMemo(() => vinNoms.filter((item) => !maisonId || !item.maisonId || item.maisonId === maisonId), [maisonId, vinNoms]);
  const primaryFields: ReadonlyArray<{
    name: 'appellationId' | 'couleurId' | 'typeVinId';
    label: string;
    items: ReadonlyArray<{ id: string; name?: string }>;
  }> = [
    { name: 'appellationId', label: 'Appellation', items: appellations },
    { name: 'couleurId', label: 'Couleur', items: couleurs },
    { name: 'typeVinId', label: 'Type de vin', items: typeVins },
  ];
  const detailFields: ReadonlyArray<{
    name: 'vinNomId' | 'contenantId';
    label: string;
    items: ReadonlyArray<{ id: string; name?: string }>;
  }> = [
    { name: 'vinNomId', label: 'Nom du vin', items: filteredVinNoms },
    { name: 'contenantId', label: 'Contenant', items: contenants },
  ];

  return (
    <Stack spacing={2.5}>
      {primaryFields.map(({ name, label, items }) => (
        <Controller
          key={name}
          name={name}
          control={control}
          render={({ field }) => (
            <TextField {...field} select label={label} fullWidth disabled={disabled}>
              <MenuItem value=""><em>Aucun</em></MenuItem>
              {items.map((item) => <MenuItem key={item.id} value={item.id}>{item.name || item.id}</MenuItem>)}
            </TextField>
          )}
        />
      ))}
      <Controller
        name="maisonId"
        control={control}
        render={({ field }) => (
          <Stack spacing={0.75}>
            <TextField {...field} select label="Maison" fullWidth disabled={disabled}>
              <MenuItem value=""><em>Aucun</em></MenuItem>
              {maisons.map((item) => <MenuItem key={item.id} value={item.id}>{item.name || item.id}</MenuItem>)}
            </TextField>
            {field.value && (
              <Typography variant="body2" color="text.secondary">
                <MaisonReference maisonId={field.value} showWebsite websiteLabel="Ouvrir le site" />
              </Typography>
            )}
          </Stack>
        )}
      />
      {detailFields.map(({ name, label, items }) => (
        <Controller
          key={name}
          name={name}
          control={control}
          render={({ field }) => (
            <TextField {...field} select label={label} fullWidth disabled={disabled}>
              <MenuItem value=""><em>Aucun</em></MenuItem>
              {items.map((item) => <MenuItem key={item.id} value={item.id}>{item.name || item.id}</MenuItem>)}
            </TextField>
          )}
        />
      ))}
      <TextField label="Annee" type="number" fullWidth disabled={disabled} error={Boolean(errors.annee)} helperText={errors.annee?.message ?? 'Annee optionnelle.'} {...register('annee', { validate: (value) => value === '' || Number(value) >= 0 || "L'annee doit etre positive." })} />
      <Stack direction={{ xs: 'column', sm: 'row' }} spacing={2}>
        <TextField
          label="Degorgement - mois"
          type="number"
          fullWidth
          disabled={disabled}
          error={Boolean(errors.degorgementMois)}
          helperText={errors.degorgementMois?.message ?? 'Valeur optionnelle entre 1 et 12.'}
          {...register('degorgementMois', {
            validate: (value) => {
              if (value === '' && degorgementAnnee === '') {
                return true;
              }
              if (value === '') {
                return "Le mois de degorgement est requis avec l'annee.";
              }
              const month = Number(value);
              return (month >= 1 && month <= 12) || 'Le mois de degorgement doit etre compris entre 1 et 12.';
            },
          })}
        />
        <TextField
          label="Degorgement - annee"
          type="number"
          fullWidth
          disabled={disabled}
          error={Boolean(errors.degorgementAnnee)}
          helperText={errors.degorgementAnnee?.message ?? 'Valeur optionnelle.'}
          {...register('degorgementAnnee', {
            validate: (value) => {
              if (value === '' && degorgementMois === '') {
                return true;
              }
              if (value === '') {
                return "L'annee de degorgement est requise avec le mois.";
              }
              return Number(value) >= 0 || "L'annee de degorgement doit etre positive.";
            },
          })}
        />
      </Stack>
      <TextField
        label="Dosage (g/l)"
        type="number"
        fullWidth
        disabled={disabled}
        error={Boolean(errors.dosageGrammesParLitre)}
        helperText={errors.dosageGrammesParLitre?.message ?? 'Dosage optionnel en grammes par litre.'}
        slotProps={{ htmlInput: { step: '0.1', min: 0 } }}
        {...register('dosageGrammesParLitre', {
          validate: (value) => value === '' || Number(value) >= 0 || 'Le dosage doit etre positif ou nul.',
        })}
      />
      <TextField label="Commune" fullWidth disabled={disabled} {...register('commune')} />
      <TextField label="Region" fullWidth disabled={disabled} {...register('region')} />
      <TextField label="Commentaires" fullWidth multiline minRows={3} disabled={disabled} {...register('commentaires')} />
      <TextField label="Accords mets / vins" fullWidth multiline minRows={3} disabled={disabled} {...register('accordsMetsVins')} />
      <Stack spacing={1.5}>
        <Stack direction="row" sx={{ justifyContent: 'space-between', alignItems: 'center' }}>
          <Typography variant="h6">Cepages</Typography>
          <Button variant="outlined" size="small" startIcon={<AddIcon />} disabled={disabled} onClick={() => append({ cepageId: '', pourcentage: '' })}>Ajouter un cepage</Button>
        </Stack>
        {fields.length === 0 && <Typography variant="body2" color="text.secondary">Aucun cepage configure.</Typography>}
        {fields.map((field, index) => (
          <Paper key={field.id} variant="outlined" sx={{ p: 1.5 }}>
            <Stack spacing={1.5}>
              <Stack direction="row" sx={{ justifyContent: 'space-between', alignItems: 'center' }}>
                <Typography variant="subtitle2">Cepage {index + 1}</Typography>
                <IconButton color="error" disabled={disabled} onClick={() => remove(index)}><DeleteIcon fontSize="small" /></IconButton>
              </Stack>
              <Controller
                name={`cepages.${index}.cepageId` as const}
                control={control}
                render={({ field: controllerField }) => (
                  <TextField {...controllerField} select label="Cepage" fullWidth disabled={disabled}>
                    <MenuItem value=""><em>Aucun</em></MenuItem>
                    {cepages.map((item) => <MenuItem key={item.id} value={item.id}>{item.name || item.id}</MenuItem>)}
                  </TextField>
                )}
              />
              <TextField label="Pourcentage" type="number" fullWidth disabled={disabled} {...register(`cepages.${index}.pourcentage` as const, { validate: (value) => value === '' || (Number(value) >= 0 && Number(value) <= 100) || 'Le pourcentage doit etre compris entre 0 et 100.' })} />
            </Stack>
          </Paper>
        ))}
      </Stack>
      <FormControl fullWidth>
        <InputLabel id="vin-circonstances-label">Circonstances</InputLabel>
        <Controller
          name="circonstanceIds"
          control={control}
          render={({ field }) => (
            <Select {...field} labelId="vin-circonstances-label" multiple label="Circonstances" disabled={disabled} renderValue={(selected) => renderSelectedNames(selected, circonstances)}>
              {circonstances.map((item) => <MenuItem key={item.id} value={item.id}>{item.name || item.id}</MenuItem>)}
            </Select>
          )}
        />
        <FormHelperText>Selection multiple autorisee.</FormHelperText>
      </FormControl>
      <FormControl fullWidth>
        <InputLabel id="vin-tags-label">Tags</InputLabel>
        <Controller
          name="tagIds"
          control={control}
          render={({ field }) => (
            <Select {...field} labelId="vin-tags-label" multiple label="Tags" disabled={disabled} renderValue={(selected) => renderSelectedNames(selected, tags)}>
              {tags.map((item) => <MenuItem key={item.id} value={item.id}>{item.name || item.id}</MenuItem>)}
            </Select>
          )}
        />
        <FormHelperText>Selection multiple autorisee.</FormHelperText>
      </FormControl>
    </Stack>
  );
};

export default VinForm;
