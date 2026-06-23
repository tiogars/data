
import Stack from '@mui/material/Stack';
import TextField from '@mui/material/TextField';
import { useFormContext } from 'react-hook-form';

export type CirconstanceFormValues = {
  name: string;
};

type CirconstanceFormProps = {
  disabled?: boolean;
};

const CirconstanceForm = ({ disabled = false }: CirconstanceFormProps) => {
  const { register, formState: { errors } } = useFormContext<CirconstanceFormValues>();

  return (
    <Stack spacing={2.5}>
      <TextField
        label="Nom"
        fullWidth
        disabled={disabled}
        error={Boolean(errors.name)}
        helperText={errors.name?.message ?? 'Nom de la circonstance.'}
        {...register('name', {
          required: 'Le nom est obligatoire.',
          validate: (value) => value.trim().length > 0 || 'Le nom est obligatoire.',
        })}
      />
    </Stack>
  );
};

export default CirconstanceForm;
