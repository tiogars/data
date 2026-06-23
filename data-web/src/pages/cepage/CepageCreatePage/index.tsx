import { useForm } from 'react-hook-form';
import type { FC } from 'react';
import { useNavigate } from 'react-router-dom';
import CepageForm, { type CepageFormValues } from '../../../components/CepageForm';
import { CrudFormPageShell } from '../../../components/CrudFormPageShell';
import { useCreateCepageMutation } from '../../../services/cepageApi';
import type { CepageCreatePageProps } from './CepageCreatePage.types';
const defaultValues: CepageFormValues = { name: '' };
export const CepageCreatePage: FC<CepageCreatePageProps> = ({ onCreated }) => {
  const navigate = useNavigate();
  const [createItem, { isLoading, error, isSuccess }] = useCreateCepageMutation();
  const methods = useForm<CepageFormValues>({ defaultValues });
  const { reset } = methods;
  const onSubmit = async (values: CepageFormValues) => { const createdItem = await createItem({ cepageCreationForm: values }).unwrap(); reset(defaultValues); if (createdItem.id) { await onCreated?.(createdItem.id); navigate(`/cepage/${createdItem.id}`); } };
  return <CrudFormPageShell methods={methods} title="Creer le cepage" subtitle="Ajoutez le nom de le cepage." submitLabel="Creer" onSubmit={onSubmit} isSubmitting={isLoading} showSuccess={isSuccess} successMessage="Cepage cree avec succes." showError={Boolean(error)} errorMessage="Erreur lors de la creation de le cepage." ><CepageForm disabled={isLoading} /></CrudFormPageShell>;
};
