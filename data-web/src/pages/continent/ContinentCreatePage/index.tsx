import { useForm } from 'react-hook-form';
import type { FC } from 'react';
import { useNavigate } from 'react-router-dom';
import ContinentForm, { type ContinentFormValues } from '../../../components/ContinentForm';
import { CrudFormPageShell } from '../../../components/CrudFormPageShell';
import { useCreateContinentMutation } from '../../../services/continentApi';
import type { ContinentCreatePageProps } from './ContinentCreatePage.types';

const defaultValues: ContinentFormValues = {
  code: '',
  name: '',
};

export const ContinentCreatePage: FC<ContinentCreatePageProps> = ({ onCreated }) => {
  const navigate = useNavigate();
  const [createContinent, { isLoading, error, isSuccess }] = useCreateContinentMutation();
  const methods = useForm<ContinentFormValues>({ defaultValues });
  const { reset } = methods;

  const onSubmit = async (values: ContinentFormValues) => {
    const createdContinent = await createContinent({
      continentCreationForm: values,
    }).unwrap();

    reset(defaultValues);

    if (createdContinent.id) {
      await onCreated?.(createdContinent.id);
      navigate(`/continent/${createdContinent.id}`);
    }
  };

  return (
    <CrudFormPageShell
      methods={methods}
      title="Creer un continent"
      subtitle="Ajoutez le code et le nom du continent."
      submitLabel="Creer"
      onSubmit={onSubmit}
      isSubmitting={isLoading}
      showSuccess={isSuccess}
      successMessage="Continent cree avec succes."
      showError={Boolean(error)}
      errorMessage="Erreur lors de la creation du continent."
    >
      <ContinentForm disabled={isLoading} />
    </CrudFormPageShell>
  );
};
