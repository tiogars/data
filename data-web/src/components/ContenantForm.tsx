import Stack from '@mui/material/Stack';
import TextField from '@mui/material/TextField';
import { useFormContext } from 'react-hook-form';

export type ContenantFormValues = {
  name: string;
  volumeCl: string;
};

type ContenantFormProps = {
  disabled?: boolean;
};

const ContenantForm = ({ disabled = false }: ContenantFormProps) => {
  const { register, formState: { errors } } = useFormContext<ContenantFormValues>();

  return (
    <Stack spacing={2.5}>
      <TextField
        label="Nom"
        fullWidth
        disabled={disabled}
        error={Boolean(errors.name)}
        helperText={errors.name?.message ?? 'Nom du contenant.'}
        {...register('name', {
          required: 'Le nom est obligatoire.',
          validate: (value) => value.trim().length > 0 || 'Le nom est obligatoire.',
        })}
      />
      <TextField
        label="Volume (cl)"
        type="number"
        fullWidth
        disabled={disabled}
        error={Boolean(errors.volumeCl)}
        helperText={errors.volumeCl?.message ?? 'Volume optionnel en centilitres.'}
        {...register('volumeCl', {
          validate: (value) => value === '' || Number(value) >= 0 || 'Le volume doit etre positif.',
        })}
      />
    </Stack>
  );
};

export default ContenantForm;
