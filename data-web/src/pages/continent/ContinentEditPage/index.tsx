import { useEffect } from 'react';
import { useForm } from 'react-hook-form';
import type { FC } from 'react';
import { useNavigate } from 'react-router-dom';
import ContinentForm, { type ContinentFormValues } from '../../../components/ContinentForm';
import { CrudFormPageShell } from '../../../components/CrudFormPageShell';
import { useGetContinentQuery, useUpdateContinentMutation } from '../../../services/continentApi';
import type { ContinentEditPageProps } from './ContinentEditPage.types';

const emptyValues: ContinentFormValues = {
  code: '',
  name: '',
};

export const ContinentEditPage: FC<ContinentEditPageProps> = ({ id }) => {
  const navigate = useNavigate();
  const { data, isLoading, error } = useGetContinentQuery({ id });
  const [updateContinent, { isLoading: isSaving, error: saveError, isSuccess }] = useUpdateContinentMutation();
  const methods = useForm<ContinentFormValues>({ defaultValues: emptyValues });
  const { reset } = methods;

  useEffect(() => {
    if (data) {
      reset({
        code: data.code ?? '',
        name: data.name ?? '',
      });
    }
  }, [data, reset]);

  const onSubmit = async (values: ContinentFormValues) => {
    await updateContinent({
      id,
      continentUpdateForm: {
        id,
        ...values,
      },
    }).unwrap();

    navigate(`/continent/${id}`);
  };

  if (isLoading) return <div>Chargement...</div>;
  if (error) return <div>Erreur lors du chargement du continent</div>;
  if (!data) return <div>Continent introuvable</div>;

  return (
    <CrudFormPageShell
      methods={methods}
      title="Modifier un continent"
      subtitle="Mettez a jour le code et le nom du continent."
      submitLabel="Enregistrer"
      onSubmit={onSubmit}
      isSubmitting={isSaving}
      showSuccess={isSuccess}
      successMessage="Continent modifie avec succes."
      showError={Boolean(saveError)}
      errorMessage="Erreur lors de la modification du continent."
    >
      <ContinentForm disabled={isSaving} />
    </CrudFormPageShell>
  );
}
