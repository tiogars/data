import { useMemo, type FC } from 'react';
import { useForm } from 'react-hook-form';
import Alert from '@mui/material/Alert';
import { CrudFormPageShell } from '../../../components/CrudFormPageShell';
import CarMileageForm, { type CarMileageFormValues } from '../../../components/CarMileageForm';
import { useListCarsQuery } from '../../../services/carApi';
import { useCreateCarMileageMutation } from '../../../services/carMileageApi';
import type { CarMileageFormPageProps } from './CarMileageFormPage.types';

function nowForDateTimeLocal() {
  const now = new Date();
  now.setMinutes(now.getMinutes() - now.getTimezoneOffset());
  return now.toISOString().slice(0, 16);
}

const defaultValues: CarMileageFormValues = {
  carId: '',
  readingAt: nowForDateTimeLocal(),
  odometerKm: 0,
  fuelVolumeLiters: undefined,
  fullTank: false,
};

export const CarMileageFormPage: FC<CarMileageFormPageProps> = () => {
  const { data: carsData, isLoading: carsLoading, error: carsError } = useListCarsQuery();
  const [createCarMileage, { isLoading, error, isSuccess }] = useCreateCarMileageMutation();
  const methods = useForm<CarMileageFormValues>({ defaultValues });
  const { reset } = methods;

  const carOptions = useMemo(
    () =>
      (carsData?.items ?? [])
        .filter((car): car is { id: string; name: string } => Boolean(car.id && car.name))
        .map((car) => ({ id: car.id, name: car.name })),
    [carsData?.items],
  );

  const onSubmit = async (values: CarMileageFormValues) => {
    await createCarMileage({
      carMileageCreationForm: {
        carId: values.carId,
        readingAt: values.readingAt,
        odometerKm: values.odometerKm,
        fuelVolumeLiters: values.fuelVolumeLiters,
        fullTank: values.fullTank,
      },
    }).unwrap();

    reset({
      ...defaultValues,
      carId: values.carId,
    });
  };

  if (carsLoading) return <div>Chargement...</div>;
  if (carsError) return <div>Erreur lors du chargement des voitures</div>;

  return (
    <>
      {carOptions.length === 0 && <Alert severity="info">Ajoutez d'abord une voiture avant de saisir des releves.</Alert>}
      <CrudFormPageShell
        methods={methods}
        title="Formulaire de saisie"
        subtitle="Enregistrez un releve de kilometrage pour une voiture."
        submitLabel="Enregistrer le releve"
        onSubmit={onSubmit}
        isSubmitting={isLoading || carOptions.length === 0}
        showSuccess={isSuccess}
        successMessage="Releve enregistre avec succes."
        showError={Boolean(error)}
        errorMessage="Erreur lors de l'enregistrement du releve."
      >
        <CarMileageForm carOptions={carOptions} disabled={isLoading || carOptions.length === 0} />
      </CrudFormPageShell>
    </>
  );
};
