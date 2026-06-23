
import Stack from '@mui/material/Stack';
import TextField from '@mui/material/TextField';
import { useFormContext } from 'react-hook-form';

export type AppellationFormValues = {
  name: string;
};

type AppellationFormProps = {
  disabled?: boolean;
};

const AppellationForm = ({ disabled = false }: AppellationFormProps) => {
  const { register, formState: { errors } } = useFormContext<AppellationFormValues>();

  return (
    <Stack spacing={2.5}>
      <TextField
        label="Nom"
        fullWidth
        disabled={disabled}
        error={Boolean(errors.name)}
        helperText={errors.name?.message ?? "Nom de l'appellation."}
        {...register('name', {
          required: 'Le nom est obligatoire.',
          validate: (value) => value.trim().length > 0 || 'Le nom est obligatoire.',
        })}
      />
    </Stack>
  );
};

export default AppellationForm;
