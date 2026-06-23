
import Stack from '@mui/material/Stack';
import TextField from '@mui/material/TextField';
import { useFormContext } from 'react-hook-form';

export type CepageFormValues = {
  name: string;
};

type CepageFormProps = {
  disabled?: boolean;
};

const CepageForm = ({ disabled = false }: CepageFormProps) => {
  const { register, formState: { errors } } = useFormContext<CepageFormValues>();

  return (
    <Stack spacing={2.5}>
      <TextField
        label="Nom"
        fullWidth
        disabled={disabled}
        error={Boolean(errors.name)}
        helperText={errors.name?.message ?? 'Nom du cepage.'}
        {...register('name', {
          required: 'Le nom est obligatoire.',
          validate: (value) => value.trim().length > 0 || 'Le nom est obligatoire.',
        })}
      />
    </Stack>
  );
};

export default CepageForm;
