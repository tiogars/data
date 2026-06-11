import { useEffect } from 'react';
import { useForm } from 'react-hook-form';
import type { FC } from 'react';
import { useNavigate } from 'react-router-dom';
import BrandForm, { type BrandFormValues } from '../../../components/BrandForm';
import { CrudFormPageShell } from '../../../components/CrudFormPageShell';
import { useGetBrandQuery, useUpdateBrandMutation } from '../../../services/brandApi';
import type { BrandEditPageProps } from './BrandEditPage.types';

const emptyValues: BrandFormValues = {
  name: '',
  description: '',
};

export const BrandEditPage: FC<BrandEditPageProps> = ({ id }) => {
  const navigate = useNavigate();
  const { data, isLoading, error } = useGetBrandQuery({ id });
  const [updateBrand, { isLoading: isSaving, error: saveError, isSuccess }] = useUpdateBrandMutation();
  const methods = useForm<BrandFormValues>({ defaultValues: emptyValues });
  const { reset } = methods;

  useEffect(() => {
    if (data) {
      reset({
        name: data.name ?? '',
        description: data.description ?? '',
      });
    }
  }, [data, reset]);

  const onSubmit = async (values: BrandFormValues) => {
    await updateBrand({
      id,
      brand: {
        id,
        ...values,
      },
    }).unwrap();

    navigate(`/brand/${id}`);
  };

  if (isLoading) return <div>Chargement...</div>;
  if (error) return <div>Erreur lors du chargement de la marque</div>;
  if (!data) return <div>Marque introuvable</div>;

  return (
    <CrudFormPageShell
      methods={methods}
      title="Modifier une marque"
      subtitle="Mettez a jour le nom et la description de la marque."
      submitLabel="Enregistrer"
      onSubmit={onSubmit}
      isSubmitting={isSaving}
      showSuccess={isSuccess}
      successMessage="Marque modifiee avec succes."
      showError={Boolean(saveError)}
      errorMessage="Erreur lors de la modification de la marque."
    >
      <BrandForm disabled={isSaving} />
    </CrudFormPageShell>
  );
};
