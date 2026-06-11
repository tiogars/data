import { useEffect } from 'react';
import { useForm } from 'react-hook-form';
import type { FC } from 'react';
import { useNavigate } from 'react-router-dom';
import GtinForm, { type GtinFormValues } from '../../../components/GtinForm';
import { CrudFormPageShell } from '../../../components/CrudFormPageShell';
import { useGetGtinQuery, useUpdateGtinMutation } from '../../../services/gtinApi';
import type { GtinEditPageProps } from './GtinEditPage.types';

const emptyValues: GtinFormValues = {
  code: '',
  description: '',
};

export const GtinEditPage: FC<GtinEditPageProps> = ({ id }) => {
  const navigate = useNavigate();
  const { data, isLoading, error } = useGetGtinQuery({ id });
  const [updateGtin, { isLoading: isSaving, error: saveError, isSuccess }] = useUpdateGtinMutation();
  const methods = useForm<GtinFormValues>({ defaultValues: emptyValues });
  const { reset } = methods;

  useEffect(() => {
    if (data) {
      reset({
        code: data.code ?? '',
        description: data.description ?? '',
      });
    }
  }, [data, reset]);

  const onSubmit = async (values: GtinFormValues) => {
    await updateGtin({
      id,
      gtin: {
        id,
        ...values,
      },
    }).unwrap();

    navigate(`/gtin/${id}`);
  };

  if (isLoading) return <div>Chargement...</div>;
  if (error) return <div>Erreur lors du chargement du GTIN</div>;
  if (!data) return <div>GTIN introuvable</div>;

  return (
    <CrudFormPageShell
      methods={methods}
      title="Modifier un GTIN"
      subtitle="Mettez a jour le code et la description du GTIN."
      submitLabel="Enregistrer"
      onSubmit={onSubmit}
      isSubmitting={isSaving}
      showSuccess={isSuccess}
      successMessage="GTIN modifie avec succes."
      showError={Boolean(saveError)}
      errorMessage="Erreur lors de la modification du GTIN."
    >
      <GtinForm disabled={isSaving} />
    </CrudFormPageShell>
  );
};
