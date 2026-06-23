import Stack from '@mui/material/Stack';
import TextField from '@mui/material/TextField';
import { useFormContext } from 'react-hook-form';

export type MaisonFormValues = {
  name: string;
  website: string;
};

type MaisonFormProps = { disabled?: boolean };

const MaisonForm = ({ disabled = false }: MaisonFormProps) => {
  const { register, formState: { errors } } = useFormContext<MaisonFormValues>();

  return (
    <Stack spacing={2.5}>
      <TextField
        label="Nom"
        fullWidth
        disabled={disabled}
        error={Boolean(errors.name)}
        helperText={errors.name?.message ?? 'Nom de la maison.'}
        {...register('name', {
          required: 'Le nom est obligatoire.',
          validate: (value) => value.trim().length > 0 || 'Le nom est obligatoire.',
        })}
      />
      <TextField
        label="Site web"
        fullWidth
        disabled={disabled}
        error={Boolean(errors.website)}
        helperText={errors.website?.message ?? 'URL optionnelle de la maison.'}
        {...register('website', {
          validate: (value) => {
            if (!value.trim()) return true;
            try { new URL(value); return true; } catch { return 'Veuillez saisir une URL valide.'; }
          },
        })}
      />
    </Stack>
  );
};

export default MaisonForm;
