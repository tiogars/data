import { useForm } from 'react-hook-form';
import type { FC } from 'react';
import { useNavigate } from 'react-router-dom';
import ModelForm, { type ModelFormValues } from '../../../components/ModelForm';
import { CrudFormPageShell } from '../../../components/CrudFormPageShell';
import { useCreateModelMutation } from '../../../services/modelApi';
import type { ModelCreatePageProps } from './ModelCreatePage.types';

const defaultValues: ModelFormValues = {
  name: '',
  description: '',
  modelAttributes: [],
};

export const ModelCreatePage: FC<ModelCreatePageProps> = ({ onCreated }) => {
  const navigate = useNavigate();
  const [createModel, { isLoading, error, isSuccess }] = useCreateModelMutation();
  const methods = useForm<ModelFormValues>({ defaultValues });
  const { reset } = methods;

  const onSubmit = async (values: ModelFormValues) => {
    const createdModel = await createModel({
      modelCreationForm: values,
    }).unwrap();

    reset(defaultValues);

    if (createdModel.id) {
      await onCreated?.(createdModel.id);
      navigate(`/model/${createdModel.id}`);
    }
  };

  return (
    <CrudFormPageShell
      methods={methods}
      title="Creer un modele"
      subtitle="Ajoutez le nom et la description du modele."
      submitLabel="Creer"
      onSubmit={onSubmit}
      isSubmitting={isLoading}
      showSuccess={isSuccess}
      successMessage="Modele cree avec succes."
      showError={Boolean(error)}
      errorMessage="Erreur lors de la creation du modele."
    >
      <ModelForm disabled={isLoading} />
    </CrudFormPageShell>
  );
};
