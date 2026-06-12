import { useEffect } from 'react';
import { useForm } from 'react-hook-form';
import type { FC } from 'react';
import CarForm, { type CarFormValues } from '../../../components/CarForm';
import { CrudFormPageShell } from '../../../components/CrudFormPageShell';
import { useGetCarQuery, useUpdateCarMutation } from '../../../services/carApi';
import type { CarEditPageProps } from './CarEditPage.types';

const emptyValues: CarFormValues = {
  name: '',
  vehicleRegistrationPlate: '',
  description: '',
};

export const CarEditPage: FC<CarEditPageProps> = ({ id }) => {
  const { data, isLoading, error } = useGetCarQuery({ id });
  const [updateCar, { isLoading: isSaving, error: saveError, isSuccess }] = useUpdateCarMutation();
  const methods = useForm<CarFormValues>({ defaultValues: emptyValues });
  const { reset } = methods;

  useEffect(() => {
    if (data) {
      reset({
        name: data.name ?? '',
        description: data.description ?? '',
      });
    }
  }, [data, reset]);

  const onSubmit = async (values: CarFormValues) => {
    await updateCar({
      id,
      car: {
        id,
        ...values,
      },
    }).unwrap();
  };

  if (isLoading) return <div>Chargement...</div>;
  if (error) return <div>Erreur lors du chargement de la voiture</div>;
  if (!data) return <div>Voiture introuvable</div>;

  return (
    <CrudFormPageShell
      methods={methods}
      title="Modifier une voiture"
      subtitle="Mettez a jour le nom et la description de la voiture."
      submitLabel="Enregistrer"
      onSubmit={onSubmit}
      isSubmitting={isSaving}
      showSuccess={isSuccess}
      successMessage="Voiture modifiee avec succes."
      showError={Boolean(saveError)}
      errorMessage="Erreur lors de la modification de la voiture."
    >
      <CarForm disabled={isSaving} />
    </CrudFormPageShell>
  );
};
