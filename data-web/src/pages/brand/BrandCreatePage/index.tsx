import { useForm } from 'react-hook-form';
import type { FC } from 'react';
import { useNavigate } from 'react-router-dom';
import BrandForm, { type BrandFormValues } from '../../../components/BrandForm';
import { CrudFormPageShell } from '../../../components/CrudFormPageShell';
import { useCreateBrandMutation } from '../../../services/brandApi';
import type { BrandCreatePageProps } from './BrandCreatePage.types';

const defaultValues: BrandFormValues = {
  name: '',
  description: '',
};

export const BrandCreatePage: FC<BrandCreatePageProps> = ({ onCreated }) => {
  const navigate = useNavigate();
  const [createBrand, { isLoading, error, isSuccess }] = useCreateBrandMutation();
  const methods = useForm<BrandFormValues>({ defaultValues });
  const { reset } = methods;

  const onSubmit = async (values: BrandFormValues) => {
    const createdBrand = await createBrand({
      brandCreationForm: values,
    }).unwrap();

    reset(defaultValues);

    if (createdBrand.id) {
      await onCreated?.(createdBrand.id);
      navigate(`/brand/${createdBrand.id}`);
    }
  };

  return (
    <CrudFormPageShell
      methods={methods}
      title="Creer une marque"
      subtitle="Ajoutez un Nom de la marque et sa description."
      submitLabel="Creer"
      onSubmit={onSubmit}
      isSubmitting={isLoading}
      showSuccess={isSuccess}
      successMessage="Marque creee avec succes."
      showError={Boolean(error)}
      errorMessage="Erreur lors de la creation de la marque."
    >
      <BrandForm disabled={isLoading} />
    </CrudFormPageShell>
  );
};
