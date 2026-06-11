import { useForm } from 'react-hook-form';
import type { FC } from 'react';
import { useNavigate } from 'react-router-dom';
import GtinForm, { type GtinFormValues } from '../../../components/GtinForm';
import { CrudFormPageShell } from '../../../components/CrudFormPageShell';
import { useCreateGtinMutation } from '../../../services/gtinApi';
import type { GtinCreatePageProps } from './GtinCreatePage.types';

const defaultValues: GtinFormValues = {
  code: '',
  description: '',
};

export const GtinCreatePage: FC<GtinCreatePageProps> = ({ onCreated }) => {
  const navigate = useNavigate();
  const [createGtin, { isLoading, error, isSuccess }] = useCreateGtinMutation();
  const methods = useForm<GtinFormValues>({ defaultValues });
  const { reset } = methods;

  const onSubmit = async (values: GtinFormValues) => {
    const createdGtin = await createGtin({
      gtinCreationForm: values,
    }).unwrap();

    reset(defaultValues);

    if (createdGtin.id) {
      await onCreated?.(createdGtin.id);
      navigate(`/gtin/${createdGtin.id}`);
    }
  };

  return (
    <CrudFormPageShell
      methods={methods}
      title="Creer un GTIN"
      subtitle="Ajoutez un code GTIN et sa description."
      submitLabel="Creer"
      onSubmit={onSubmit}
      isSubmitting={isLoading}
      showSuccess={isSuccess}
      successMessage="GTIN cree avec succes."
      showError={Boolean(error)}
      errorMessage="Erreur lors de la creation du GTIN."
    >
      <GtinForm disabled={isLoading} />
    </CrudFormPageShell>
  );
};
