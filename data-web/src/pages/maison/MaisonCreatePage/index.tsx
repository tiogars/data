import { useForm } from 'react-hook-form';
import type { FC } from 'react';
import { useNavigate } from 'react-router-dom';
import MaisonForm, { type MaisonFormValues } from '../../../components/MaisonForm';
import { CrudFormPageShell } from '../../../components/CrudFormPageShell';
import { useCreateMaisonMutation } from '../../../services/maisonApi';
import type { MaisonCreatePageProps } from './MaisonCreatePage.types';

const defaultValues: MaisonFormValues = {
  name: '',
  website: '',
};

export const MaisonCreatePage: FC<MaisonCreatePageProps> = ({ onCreated }) => {
  const navigate = useNavigate();
  const [createMaison, { isLoading, error, isSuccess }] = useCreateMaisonMutation();
  const methods = useForm<MaisonFormValues>({ defaultValues });
  const { reset } = methods;

  const onSubmit = async (values: MaisonFormValues) => {
    const createdMaison = await createMaison({
      maisonCreationForm: {
        name: values.name,
        website: values.website || undefined,
      },
    }).unwrap();

    reset(defaultValues);

    if (createdMaison.id) {
      await onCreated?.(createdMaison.id);
      navigate(`/maison/${createdMaison.id}`);
    }
  };

  return (
    <CrudFormPageShell
      methods={methods}
      title="Creer une maison"
      subtitle="Ajoutez le nom et le site web de la maison."
      submitLabel="Creer"
      onSubmit={onSubmit}
      isSubmitting={isLoading}
      showSuccess={isSuccess}
      successMessage="Maison creee avec succes."
      showError={Boolean(error)}
      errorMessage="Erreur lors de la creation de la maison."
    >
      <MaisonForm disabled={isLoading} />
    </CrudFormPageShell>
  );
};
