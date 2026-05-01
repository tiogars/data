import FormControlLabel from '@mui/material/FormControlLabel';
import Stack from '@mui/material/Stack';
import Switch from '@mui/material/Switch';
import TextField from '@mui/material/TextField';
import { Controller, useFormContext } from 'react-hook-form';

export type GitHubRepositoryFormValues = {
  owner: string;
  name: string;
  url: string;
  description: string;
  defaultBranch: string;
  language: string;
  stars: number;
  archived: boolean;
};

type GitHubRepositoryFormProps = {
  disabled?: boolean;
};

const GitHubRepositoryForm = ({ disabled = false }: GitHubRepositoryFormProps) => {
  const {
    register,
    control,
    formState: { errors },
  } = useFormContext<GitHubRepositoryFormValues>();

  return (
    <Stack spacing={2.5}>
      <TextField
        label="Owner"
        fullWidth
        disabled={disabled}
        error={Boolean(errors.owner)}
        helperText={errors.owner?.message ?? 'Compte utilisateur ou organisation GitHub.'}
        {...register('owner', {
          required: 'Le owner est obligatoire.',
          validate: (value) => value.trim().length > 0 || 'Le owner est obligatoire.',
        })}
      />
      <TextField
        label="Nom du repository"
        fullWidth
        disabled={disabled}
        error={Boolean(errors.name)}
        helperText={errors.name?.message ?? 'Nom court du repository, sans owner.'}
        {...register('name', {
          required: 'Le nom du repository est obligatoire.',
          validate: (value) => value.trim().length > 0 || 'Le nom du repository est obligatoire.',
        })}
      />
      <TextField
        label="URL"
        type="url"
        fullWidth
        disabled={disabled}
        error={Boolean(errors.url)}
        helperText={errors.url?.message ?? 'Adresse absolue du repository GitHub.'}
        {...register('url', {
          required: 'L\'URL est obligatoire.',
          pattern: {
            value: /^https?:\/\/.+/i,
            message: 'L\'URL doit commencer par http:// ou https://.',
          },
        })}
      />
      <TextField
        label="Description"
        fullWidth
        multiline
        minRows={3}
        disabled={disabled}
        error={Boolean(errors.description)}
        helperText={errors.description?.message ?? 'Résumé optionnel du repository.'}
        {...register('description')}
      />
      <TextField
        label="Branche par défaut"
        fullWidth
        disabled={disabled}
        error={Boolean(errors.defaultBranch)}
        helperText={errors.defaultBranch?.message ?? 'Exemple: main, master, develop.'}
        {...register('defaultBranch', {
          required: 'La branche par défaut est obligatoire.',
          validate: (value) => value.trim().length > 0 || 'La branche par défaut est obligatoire.',
        })}
      />
      <TextField
        label="Langage principal"
        fullWidth
        disabled={disabled}
        error={Boolean(errors.language)}
        helperText={errors.language?.message ?? 'Optionnel: TypeScript, Java, Python...'}
        {...register('language')}
      />
      <TextField
        label="Étoiles"
        type="number"
        fullWidth
        disabled={disabled}
        error={Boolean(errors.stars)}
        helperText={errors.stars?.message ?? 'Doit être supérieur ou égal à 0.'}
        {...register('stars', {
          required: 'Le nombre d\'étoiles est obligatoire.',
          setValueAs: (value) => Number(value),
          validate: (value) => Number.isFinite(value) && value >= 0 || 'Le nombre d\'étoiles doit être supérieur ou égal à 0.',
        })}
      />
      <Controller
        control={control}
        name="archived"
        render={({ field }) => (
          <FormControlLabel
            label="Repository archivé"
            control={(
              <Switch
                checked={Boolean(field.value)}
                onChange={(_event, checked) => field.onChange(checked)}
                disabled={disabled}
              />
            )}
          />
        )}
      />
    </Stack>
  );
};

export default GitHubRepositoryForm;
