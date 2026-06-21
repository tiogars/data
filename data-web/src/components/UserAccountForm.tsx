import Stack from '@mui/material/Stack';
import TextField from '@mui/material/TextField';
import MenuItem from '@mui/material/MenuItem';
import FormControlLabel from '@mui/material/FormControlLabel';
import Switch from '@mui/material/Switch';
import { Controller, useFormContext } from 'react-hook-form';

export type UserAccountFormValues = {
  username: string;
  password: string;
  role: 'ADMIN' | 'USER';
  enabled: boolean;
};

type UserAccountFormProps = {
  disabled?: boolean;
  passwordRequired?: boolean;
};

const UserAccountForm = ({ disabled = false, passwordRequired = true }: UserAccountFormProps) => {
  const {
    register,
    control,
    formState: { errors },
  } = useFormContext<UserAccountFormValues>();

  return (
    <Stack spacing={2.5}>
      <TextField
        label="Nom utilisateur"
        fullWidth
        disabled={disabled}
        error={Boolean(errors.username)}
        helperText={errors.username?.message ?? "Nom de connexion unique du compte utilisateur."}
        {...register('username', {
          required: 'Le nom utilisateur est obligatoire.',
          validate: (value) => value.trim().length > 0 || 'Le nom utilisateur est obligatoire.',
        })}
      />
      <TextField
        label="Mot de passe"
        type="password"
        fullWidth
        disabled={disabled}
        error={Boolean(errors.password)}
        helperText={errors.password?.message ?? (passwordRequired ? 'Mot de passe (minimum 8 caracteres).' : 'Laisser vide pour conserver le mot de passe actuel.')}
        {...register('password', {
          validate: (value) => {
            const trimmedLength = value.trim().length;
            if (passwordRequired && trimmedLength === 0) return 'Le mot de passe est obligatoire.';
            if (trimmedLength > 0 && trimmedLength < 8) return 'Le mot de passe doit contenir au moins 8 caracteres.';
            return true;
          },
        })}
      />
      <TextField
        label="Role"
        fullWidth
        select
        disabled={disabled}
        error={Boolean(errors.role)}
        helperText={errors.role?.message ?? 'Role de securite attribue au compte.'}
        {...register('role', {
          required: 'Le role est obligatoire.',
        })}
      >
        <MenuItem value="ADMIN">ADMIN</MenuItem>
        <MenuItem value="USER">USER</MenuItem>
      </TextField>
      <Controller
        control={control}
        name="enabled"
        render={({ field }) => (
          <FormControlLabel
            label="Compte actif"
            control={
              <Switch
                checked={Boolean(field.value)}
                disabled={disabled}
                onChange={(_event, checked) => field.onChange(checked)}
              />
            }
          />
        )}
      />
    </Stack>
  );
};

export default UserAccountForm;
