import { useForm } from 'react-hook-form';
import type { FC } from 'react';
import { useNavigate } from 'react-router-dom';
import CirconstanceForm, { type CirconstanceFormValues } from '../../../components/CirconstanceForm';
import { CrudFormPageShell } from '../../../components/CrudFormPageShell';
import { useCreateCirconstanceMutation } from '../../../services/circonstanceApi';
import type { CirconstanceCreatePageProps } from './CirconstanceCreatePage.types';
const defaultValues: CirconstanceFormValues = { name: '' };
export const CirconstanceCreatePage: FC<CirconstanceCreatePageProps> = ({ onCreated }) => {
  const navigate = useNavigate();
  const [createItem, { isLoading, error, isSuccess }] = useCreateCirconstanceMutation();
  const methods = useForm<CirconstanceFormValues>({ defaultValues });
  const { reset } = methods;
  const onSubmit = async (values: CirconstanceFormValues) => { const createdItem = await createItem({ circonstanceCreationForm: values }).unwrap(); reset(defaultValues); if (createdItem.id) { await onCreated?.(createdItem.id); navigate(`/circonstance/${createdItem.id}`); } };
  return <CrudFormPageShell methods={methods} title="Creer la circonstance" subtitle="Ajoutez le nom de la circonstance." submitLabel="Creer" onSubmit={onSubmit} isSubmitting={isLoading} showSuccess={isSuccess} successMessage="Circonstance cree avec succes." showError={Boolean(error)} errorMessage="Erreur lors de la creation de la circonstance." ><CirconstanceForm disabled={isLoading} /></CrudFormPageShell>;
};
