import { useForm } from 'react-hook-form';
import type { FC } from 'react';
import { useNavigate } from 'react-router-dom';
import CarForm, { type CarFormValues } from '../../../components/CarForm';
import { CrudFormPageShell } from '../../../components/CrudFormPageShell';
import { useCreateCarMutation } from '../../../services/carApi';
import type { CarCreatePageProps } from './CarCreatePage.types';

const defaultValues: CarFormValues = {
  name: '',
  description: '',
};

export const CarCreatePage: FC<CarCreatePageProps> = ({ onCreated }) => {
  const navigate = useNavigate();
  const [createCar, { isLoading, error, isSuccess }] = useCreateCarMutation();
  const methods = useForm<CarFormValues>({ defaultValues });
  const { reset } = methods;

  const onSubmit = async (values: CarFormValues) => {
    const createdCar = await createCar({
      carCreationForm: values,
    }).unwrap();

    reset(defaultValues);

    if (createdCar.id) {
      await onCreated?.(createdCar.id);
      navigate(`/car/${createdCar.id}/edit`);
    }
  };

  return (
    <CrudFormPageShell
      methods={methods}
      title="Creer une voiture"
      subtitle="Ajoutez une voiture pour commencer la saisie des releves de kilometrage."
      submitLabel="Creer"
      onSubmit={onSubmit}
      isSubmitting={isLoading}
      showSuccess={isSuccess}
      successMessage="Voiture creee avec succes."
      showError={Boolean(error)}
      errorMessage="Erreur lors de la creation de la voiture."
    >
      <CarForm disabled={isLoading} />
    </CrudFormPageShell>
  );
};
