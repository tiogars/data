import Stack from '@mui/material/Stack';
import TextField from '@mui/material/TextField';
import { useFormContext } from 'react-hook-form';

export type ContinentFormValues = {
  code: string;
  name: string;
};

type ContinentFormProps = {
  disabled?: boolean;
};

const ContinentForm = ({ disabled = false }: ContinentFormProps) => {
  const {
    register,
    formState: { errors },
  } = useFormContext<ContinentFormValues>();

  return (
    <Stack spacing={2.5}>
      <TextField
        label="Code"
        fullWidth
        disabled={disabled}
        error={Boolean(errors.code)}
        helperText={errors.code?.message ?? 'Code unique du continent (ex: eu).'}
        {...register('code', {
          required: 'Le code du continent est obligatoire.',
          validate: (value) => value.trim().length > 0 || 'Le code du continent est obligatoire.',
        })}
      />
      <TextField
        label="Nom"
        fullWidth
        disabled={disabled}
        error={Boolean(errors.name)}
        helperText={errors.name?.message ?? 'Nom du continent (ex: Europe).'}
        {...register('name', {
          required: 'Le nom du continent est obligatoire.',
          validate: (value) => value.trim().length > 0 || 'Le nom du continent est obligatoire.',
        })}
      />
    </Stack>
  );
};

export default ContinentForm;
