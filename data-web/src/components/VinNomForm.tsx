import MenuItem from '@mui/material/MenuItem';
import Stack from '@mui/material/Stack';
import TextField from '@mui/material/TextField';
import { Controller, useFormContext } from 'react-hook-form';
import { useListMaisonsQuery } from '../services/maisonApi';

export type VinNomFormValues = {
  name: string;
  maisonId: string;
};

type VinNomFormProps = { disabled?: boolean };

const VinNomForm = ({ disabled = false }: VinNomFormProps) => {
  const { register, control, formState: { errors } } = useFormContext<VinNomFormValues>();
  const { data } = useListMaisonsQuery(undefined, { refetchOnMountOrArgChange: true });
  const maisons = (data?.items ?? []).filter((item): item is { id: string; name?: string } => Boolean(item.id));

  return (
    <Stack spacing={2.5}>
      <TextField
        label="Nom"
        fullWidth
        disabled={disabled}
        error={Boolean(errors.name)}
        helperText={errors.name?.message ?? 'Nom du vin.'}
        {...register('name', {
          required: 'Le nom est obligatoire.',
          validate: (value) => value.trim().length > 0 || 'Le nom est obligatoire.',
        })}
      />
      <Controller
        name="maisonId"
        control={control}
        render={({ field }) => (
          <TextField
            {...field}
            select
            label="Maison"
            fullWidth
            disabled={disabled}
            error={Boolean(errors.maisonId)}
            helperText={errors.maisonId?.message ?? 'Selectionnez la maison associee.'}
          >
            <MenuItem value=""><em>Aucune</em></MenuItem>
            {maisons.map((maison) => <MenuItem key={maison.id} value={maison.id}>{maison.name || maison.id}</MenuItem>)}
          </TextField>
        )}
      />
    </Stack>
  );
};

export default VinNomForm;
