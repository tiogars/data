import { useEffect } from 'react';
import Stack from '@mui/material/Stack';
import TextField from '@mui/material/TextField';
import { useFormContext } from 'react-hook-form';

export type WingetFormValues = {
  name: string;
  description: string;
  wingetId: string;
  installCommand: string;
  tagsText: string;
};

type WingetFormProps = {
  disabled?: boolean;
};

const WingetForm = ({ disabled = false }: WingetFormProps) => {
  const {
    register,
    setValue,
    watch,
    formState: { errors },
  } = useFormContext<WingetFormValues>();

  const wingetId = watch('wingetId');

  useEffect(() => {
    const trimmedWingetId = wingetId.trim();
    const generatedInstallCommand = trimmedWingetId.length > 0 ? `winget install -e --id ${trimmedWingetId}` : '';

    setValue('installCommand', generatedInstallCommand, {
      shouldDirty: false,
      shouldValidate: true,
    });
  }, [wingetId, setValue]);

  return (
    <Stack spacing={2.5}>
      <TextField
        label="Nom de l'application"
        fullWidth
        disabled={disabled}
        error={Boolean(errors.name)}
        helperText={errors.name?.message ?? "Nom lisible de l'application Windows."}
        {...register('name', {
          required: "Le nom de l'application est obligatoire.",
          validate: (value) => value.trim().length > 0 || "Le nom de l'application est obligatoire.",
        })}
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
        label="Identifiant Winget"
        fullWidth
        disabled={disabled}
        error={Boolean(errors.wingetId)}
        helperText={errors.wingetId?.message ?? 'Identifiant unique Winget (ex: Notepad++.Notepad++).'}
        {...register('wingetId', {
          required: "L'identifiant Winget est obligatoire.",
          validate: (value) => value.trim().length > 0 || "L'identifiant Winget est obligatoire.",
        })}
      />
      <TextField
        label="Commande d'installation"
        fullWidth
        disabled={disabled}
        error={Boolean(errors.installCommand)}
        helperText={errors.installCommand?.message ?? 'Commande prête à copier-coller dans le terminal.'}
        {...register('installCommand', {
          required: "La commande d'installation est obligatoire.",
          validate: (value) => value.trim().length > 0 || "La commande d'installation est obligatoire.",
        })}
      />
      <TextField
        label="Tags"
        fullWidth
        disabled={disabled}
        error={Boolean(errors.tagsText)}
        helperText={errors.tagsText?.message ?? 'Séparer les tags par des virgules.'}
        {...register('tagsText')}
      />
    </Stack>
  );
};

export default WingetForm;
