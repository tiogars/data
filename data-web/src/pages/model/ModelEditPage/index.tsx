import { useEffect } from 'react';
import { useForm } from 'react-hook-form';
import type { FC } from 'react';
import { useNavigate } from 'react-router-dom';
import ModelForm, { type ModelFormValues } from '../../../components/ModelForm';
import { CrudFormPageShell } from '../../../components/CrudFormPageShell';
import { useGetModelQuery, useUpdateModelMutation } from '../../../services/modelApi';
import type { ModelEditPageProps } from './ModelEditPage.types';

const emptyValues: ModelFormValues = {
  name: '',
  description: '',
  modelAttributes: [],
};

export const ModelEditPage: FC<ModelEditPageProps> = ({ id }) => {
  const navigate = useNavigate();
  const { data, isLoading, error } = useGetModelQuery({ id });
  const [updateModel, { isLoading: isSaving, error: saveError, isSuccess }] = useUpdateModelMutation();
  const methods = useForm<ModelFormValues>({ defaultValues: emptyValues });
  const { reset } = methods;

  useEffect(() => {
    if (data) {
      reset({
        name: data.name ?? '',
        description: data.description ?? '',
        modelAttributes: (data.modelAttributes ?? []).map((attribute) => ({
          name: attribute.name ?? '',
          description: attribute.description ?? '',
        })),
      });
    }
  }, [data, reset]);

  const onSubmit = async (values: ModelFormValues) => {
    await updateModel({
      id,
      model: {
        id,
        ...values,
      },
    }).unwrap();

    navigate(`/model/${id}`);
  };

  if (isLoading) return <div>Chargement...</div>;
  if (error) return <div>Erreur lors du chargement du modele</div>;
  if (!data) return <div>Modele introuvable</div>;

  return (
    <CrudFormPageShell
      methods={methods}
      title="Modifier un modele"
      subtitle="Mettez a jour le nom et la description du modele."
      submitLabel="Enregistrer"
      onSubmit={onSubmit}
      isSubmitting={isSaving}
      showSuccess={isSuccess}
      successMessage="Modele modifie avec succes."
      showError={Boolean(saveError)}
      errorMessage="Erreur lors de la modification du modele."
    >
      <ModelForm disabled={isSaving} />
    </CrudFormPageShell>
  );
};
