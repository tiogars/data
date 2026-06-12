import Stack from '@mui/material/Stack';
import TextField from '@mui/material/TextField';
import { useFormContext } from 'react-hook-form';

export type CarFormValues = {
  name: string;
  vehicleRegistrationPlate: string;
  description: string;
};

type CarFormProps = {
  disabled?: boolean;
};

const CarForm = ({ disabled = false }: CarFormProps) => {
  const {
    register,
    formState: { errors },
  } = useFormContext<CarFormValues>();

  return (
    <Stack spacing={2.5}>
      <TextField
        label="Nom de la voiture"
        fullWidth
        disabled={disabled}
        error={Boolean(errors.name)}
        helperText={errors.name?.message ?? 'Nom unique de la voiture (texte libre).'}
        {...register('name', {
          required: 'Le nom de la voiture est obligatoire.',
          validate: (value) => value.trim().length > 0 || 'Le nom de la voiture est obligatoire.',
        })}
      />
      <TextField
        label="Numéro d'immatriculation"
        fullWidth
        disabled={disabled}
        error={Boolean(errors.vehicleRegistrationPlate)}
        helperText={
          errors.vehicleRegistrationPlate?.message ?? 'Numéro d\'immatriculation de la voiture.'
        }
        {...register('vehicleRegistrationPlate', {
          required: 'Le numéro d\'immatriculation est obligatoire.',
          validate: (value) => value.trim().length > 0 || 'Le numéro d\'immatriculation est obligatoire.',
        })}
      />
      <TextField
        label="Description"
        fullWidth
        multiline
        minRows={3}
        disabled={disabled}
        error={Boolean(errors.description)}
        helperText={errors.description?.message ?? 'Description optionnelle de la voiture.'}
        {...register('description')}
      />
    </Stack>
  );
};

export default CarForm;
