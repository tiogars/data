import Stack from '@mui/material/Stack';
import TextField from '@mui/material/TextField';
import { useFormContext } from 'react-hook-form';

export type GtinFormValues = {
  code: string;
  description: string;
};

type GtinFormProps = {
  disabled?: boolean;
};

const GtinForm = ({ disabled = false }: GtinFormProps) => {
  const {
    register,
    formState: { errors },
  } = useFormContext<GtinFormValues>();

  return (
    <Stack spacing={2.5}>
      <TextField
        label="Code GTIN"
        fullWidth
        disabled={disabled}
        error={Boolean(errors.code)}
        helperText={errors.code?.message ?? 'Code unique du GTIN (texte libre).'}
        {...register('code', {
          required: 'Le code GTIN est obligatoire.',
          validate: (value) => value.trim().length > 0 || 'Le code GTIN est obligatoire.',
        })}
      />
      <TextField
        label="Description"
        fullWidth
        multiline
        minRows={3}
        disabled={disabled}
        error={Boolean(errors.description)}
        helperText={errors.description?.message ?? 'Description optionnelle du GTIN.'}
        {...register('description')}
      />
    </Stack>
  );
};

export default GtinForm;
