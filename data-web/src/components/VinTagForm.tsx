
import Stack from '@mui/material/Stack';
import TextField from '@mui/material/TextField';
import { useFormContext } from 'react-hook-form';

export type VinTagFormValues = {
  name: string;
};

type VinTagFormProps = {
  disabled?: boolean;
};

const VinTagForm = ({ disabled = false }: VinTagFormProps) => {
  const { register, formState: { errors } } = useFormContext<VinTagFormValues>();

  return (
    <Stack spacing={2.5}>
      <TextField
        label="Nom"
        fullWidth
        disabled={disabled}
        error={Boolean(errors.name)}
        helperText={errors.name?.message ?? 'Nom du tag de vin.'}
        {...register('name', {
          required: 'Le nom est obligatoire.',
          validate: (value) => value.trim().length > 0 || 'Le nom est obligatoire.',
        })}
      />
    </Stack>
  );
};

export default VinTagForm;
