import Checkbox from '@mui/material/Checkbox';
import FormControl from '@mui/material/FormControl';
import FormControlLabel from '@mui/material/FormControlLabel';
import FormHelperText from '@mui/material/FormHelperText';
import InputLabel from '@mui/material/InputLabel';
import MenuItem from '@mui/material/MenuItem';
import Select from '@mui/material/Select';
import Stack from '@mui/material/Stack';
import TextField from '@mui/material/TextField';
import { Controller, useFormContext } from 'react-hook-form';

export type CarMileageFormValues = {
  carId: string;
  readingAt: string;
  odometerKm: number;
  fuelVolumeLiters: number | undefined;
  fullTank: boolean;
};

export type CarOption = {
  id: string;
  name: string;
};

type CarMileageFormProps = {
  carOptions: CarOption[];
  disabled?: boolean;
};

const CarMileageForm = ({ carOptions, disabled = false }: CarMileageFormProps) => {
  const {
    register,
    control,
    formState: { errors },
  } = useFormContext<CarMileageFormValues>();

  return (
    <Stack spacing={2.5}>
      <FormControl fullWidth error={Boolean(errors.carId)}>
        <InputLabel id="car-mileage-car-label">Voiture</InputLabel>
        <Controller
          name="carId"
          control={control}
          rules={{ required: 'La voiture est obligatoire.' }}
          render={({ field }) => (
            <Select
              {...field}
              labelId="car-mileage-car-label"
              label="Voiture"
              disabled={disabled}
            >
              {carOptions.map((car) => (
                <MenuItem key={car.id} value={car.id}>
                  {car.name}
                </MenuItem>
              ))}
            </Select>
          )}
        />
        <FormHelperText>{errors.carId?.message ?? 'Selectionnez la voiture concernee.'}</FormHelperText>
      </FormControl>

      <TextField
        label="Date et heure"
        type="datetime-local"
        fullWidth
        disabled={disabled}
        slotProps={{ inputLabel: { shrink: true } }}
        error={Boolean(errors.readingAt)}
        helperText={errors.readingAt?.message ?? 'Par defaut: date/heure courante.'}
        {...register('readingAt', {
          required: 'La date et l\'heure sont obligatoires.',
        })}
      />

      <TextField
        label="Kilometrage (km)"
        type="number"
        fullWidth
        disabled={disabled}
        error={Boolean(errors.odometerKm)}
        helperText={errors.odometerKm?.message ?? 'Valeur obligatoire.'}
        {...register('odometerKm', {
          required: 'Le kilometrage est obligatoire.',
          valueAsNumber: true,
          validate: (value) => Number.isFinite(value) && value >= 0 || 'Le kilometrage doit etre positif.',
        })}
      />

      <TextField
        label="Volume carburant (litres)"
        type="number"
        fullWidth
        disabled={disabled}
        error={Boolean(errors.fuelVolumeLiters)}
        helperText={errors.fuelVolumeLiters?.message ?? 'Champ facultatif.'}
        {...register('fuelVolumeLiters', {
          setValueAs: (value) => {
            if (value === '' || value === null || value === undefined) {
              return undefined;
            }
            return Number(value);
          },
          validate: (value) => value === undefined || value >= 0 || 'Le volume doit etre positif.',
        })}
      />

      <Controller
        name="fullTank"
        control={control}
        render={({ field }) => (
          <FormControlLabel
            control={<Checkbox checked={Boolean(field.value)} onChange={(_event, checked) => field.onChange(checked)} disabled={disabled} />}
            label="Plein complet effectue"
          />
        )}
      />
    </Stack>
  );
};

export default CarMileageForm;
