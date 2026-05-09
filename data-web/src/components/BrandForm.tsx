import Stack from '@mui/material/Stack';
import TextField from '@mui/material/TextField';
import { useFormContext } from 'react-hook-form';

export type BrandFormValues = {
  name: string;
  description: string;
};

type BrandFormProps = {
  disabled?: boolean;
};

const BrandForm = ({ disabled = false }: BrandFormProps) => {
  const {
    register,
    formState: { errors },
  } = useFormContext<BrandFormValues>();

  return (
    <Stack spacing={2.5}>
      <TextField
        label="Nom de la marque"
        fullWidth
        disabled={disabled}
        error={Boolean(errors.name)}
        helperText={errors.name?.message ?? 'Nom unique de la marque (texte libre).'}
        {...register('name', {
          required: 'Le nom de la marque est obligatoire.',
          validate: (value) => value.trim().length > 0 || 'Le nom de la marque est obligatoire.',
        })}
      />
      <TextField
        label="Description"
        fullWidth
        multiline
        minRows={3}
        disabled={disabled}
        error={Boolean(errors.description)}
        helperText={errors.description?.message ?? 'Description optionnelle de la marque.'}
        {...register('description')}
      />
    </Stack>
  );
};

export default BrandForm;
