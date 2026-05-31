import Stack from '@mui/material/Stack';
import TextField from '@mui/material/TextField';
import { useFormContext } from 'react-hook-form';

export type AndroidFormValues = {
  name: string;
  packageName: string;
  categoriesText: string;
  description: string;
  icon: string;
};

type AndroidFormProps = {
  disabled?: boolean;
};

export function formatCategoriesText(categories: string[] | undefined): string {
  return (categories ?? []).join(', ');
}

export function parseCategoriesText(value: string): string[] {
  return value
    .split(/[,|;]/)
    .map((item) => item.trim())
    .filter((item) => item.length > 0);
}

const AndroidForm = ({ disabled = false }: AndroidFormProps) => {
  const {
    register,
    formState: { errors },
  } = useFormContext<AndroidFormValues>();

  return (
    <Stack spacing={2.5}>
      <TextField
        label="Nom de l'application"
        fullWidth
        disabled={disabled}
        error={Boolean(errors.name)}
        helperText={errors.name?.message ?? "Nom lisible de l'application Android."}
        {...register('name', {
          required: "Le nom de l'application Android est obligatoire.",
          validate: (value) => value.trim().length > 0 || "Le nom de l'application Android est obligatoire.",
        })}
      />
      <TextField
        label="Package Android"
        fullWidth
        disabled={disabled}
        error={Boolean(errors.packageName)}
        helperText={errors.packageName?.message ?? 'Nom technique unique du package Android.'}
        {...register('packageName', {
          required: 'Le package Android est obligatoire.',
          validate: (value) => value.trim().length > 0 || 'Le package Android est obligatoire.',
        })}
      />
      <TextField
        label="Categories"
        fullWidth
        disabled={disabled}
        error={Boolean(errors.categoriesText)}
        helperText={errors.categoriesText?.message ?? 'Separer les categories par des virgules.'}
        {...register('categoriesText')}
      />
      <TextField
        label="Description"
        fullWidth
        multiline
        minRows={3}
        disabled={disabled}
        error={Boolean(errors.description)}
        helperText={errors.description?.message ?? "Description optionnelle de l'application."}
        {...register('description')}
      />
      <TextField
        label="Icone"
        fullWidth
        multiline
        minRows={4}
        disabled={disabled}
        error={Boolean(errors.icon)}
        helperText={errors.icon?.message ?? "URL, data URI ou base64 de l'icone optionnelle."}
        {...register('icon')}
      />
    </Stack>
  );
};

export default AndroidForm;