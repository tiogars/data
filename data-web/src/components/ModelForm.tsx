import Stack from '@mui/material/Stack';
import TextField from '@mui/material/TextField';
import { useFormContext } from 'react-hook-form';

export type ModelFormValues = {
  name: string;
  description: string;
};

type ModelFormProps = {
  disabled?: boolean;
};

const ModelForm = ({ disabled = false }: ModelFormProps) => {
  const {
    register,
    formState: { errors },
  } = useFormContext<ModelFormValues>();

  return (
    <Stack spacing={2.5}>
      <TextField
        label="Nom du modele"
        fullWidth
        disabled={disabled}
        error={Boolean(errors.name)}
        helperText={errors.name?.message ?? 'Nom unique du modele (texte libre).'}
        {...register('name', {
          required: 'Le nom du modele est obligatoire.',
          validate: (value) => value.trim().length > 0 || 'Le nom du modele est obligatoire.',
        })}
      />
      <TextField
        label="Description"
        fullWidth
        multiline
        minRows={3}
        disabled={disabled}
        error={Boolean(errors.description)}
        helperText={errors.description?.message ?? 'Description optionnelle du modele.'}
        {...register('description')}
      />
    </Stack>
  );
};

export default ModelForm;
