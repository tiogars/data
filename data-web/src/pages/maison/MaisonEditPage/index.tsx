import { useEffect } from 'react';
import { useForm } from 'react-hook-form';
import type { FC } from 'react';
import { useNavigate } from 'react-router-dom';
import MaisonForm, { type MaisonFormValues } from '../../../components/MaisonForm';
import { CrudFormPageShell } from '../../../components/CrudFormPageShell';
import { useGetMaisonQuery, useUpdateMaisonMutation } from '../../../services/maisonApi';
import type { MaisonEditPageProps } from './MaisonEditPage.types';

const emptyValues: MaisonFormValues = {
  name: '',
  website: '',
};

export const MaisonEditPage: FC<MaisonEditPageProps> = ({ id }) => {
  const navigate = useNavigate();
  const { data, isLoading, error } = useGetMaisonQuery({ id });
  const [updateMaison, { isLoading: isSaving, error: saveError, isSuccess }] = useUpdateMaisonMutation();
  const methods = useForm<MaisonFormValues>({ defaultValues: emptyValues });
  const { reset } = methods;

  useEffect(() => {
    if (data) {
      reset({
        name: data.name ?? '',
        website: data.website ?? '',
      });
    }
  }, [data, reset]);

  const onSubmit = async (values: MaisonFormValues) => {
    await updateMaison({
      id,
      maison: {
        id,
        name: values.name,
        website: values.website || undefined,
      },
    }).unwrap();

    navigate(`/maison/${id}`);
  };

  if (isLoading) return <div>Chargement...</div>;
  if (error) return <div>Erreur lors du chargement de la maison</div>;
  if (!data) return <div>Maison introuvable</div>;

  return (
    <CrudFormPageShell
      methods={methods}
      title="Modifier une maison"
      subtitle="Mettez a jour le nom et le site web de la maison."
      submitLabel="Enregistrer"
      onSubmit={onSubmit}
      isSubmitting={isSaving}
      showSuccess={isSuccess}
      successMessage="Maison modifiee avec succes."
      showError={Boolean(saveError)}
      errorMessage="Erreur lors de la modification de la maison."
    >
      <MaisonForm disabled={isSaving} />
    </CrudFormPageShell>
  );
};
