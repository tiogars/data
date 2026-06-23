import { useForm } from 'react-hook-form';
import type { FC } from 'react';
import { useNavigate } from 'react-router-dom';
import VinTagForm, { type VinTagFormValues } from '../../../components/VinTagForm';
import { CrudFormPageShell } from '../../../components/CrudFormPageShell';
import { useCreateVinTagMutation } from '../../../services/vinTagApi';
import type { VinTagCreatePageProps } from './VinTagCreatePage.types';
const defaultValues: VinTagFormValues = { name: '' };
export const VinTagCreatePage: FC<VinTagCreatePageProps> = ({ onCreated }) => {
  const navigate = useNavigate();
  const [createItem, { isLoading, error, isSuccess }] = useCreateVinTagMutation();
  const methods = useForm<VinTagFormValues>({ defaultValues });
  const { reset } = methods;
  const onSubmit = async (values: VinTagFormValues) => { const createdItem = await createItem({ vinTagCreationForm: values }).unwrap(); reset(defaultValues); if (createdItem.id) { await onCreated?.(createdItem.id); navigate(`/vin-tag/${createdItem.id}`); } };
  return <CrudFormPageShell methods={methods} title="Creer le tag de vin" subtitle="Ajoutez le nom de le tag de vin." submitLabel="Creer" onSubmit={onSubmit} isSubmitting={isLoading} showSuccess={isSuccess} successMessage="Tag de vin cree avec succes." showError={Boolean(error)} errorMessage="Erreur lors de la creation de le tag de vin." ><VinTagForm disabled={isLoading} /></CrudFormPageShell>;
};
