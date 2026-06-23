import { useForm } from 'react-hook-form';
import type { FC } from 'react';
import { useNavigate } from 'react-router-dom';
import AppellationForm, { type AppellationFormValues } from '../../../components/AppellationForm';
import { CrudFormPageShell } from '../../../components/CrudFormPageShell';
import { useCreateAppellationMutation } from '../../../services/appellationApi';
import type { AppellationCreatePageProps } from './AppellationCreatePage.types';
const defaultValues: AppellationFormValues = { name: '' };
export const AppellationCreatePage: FC<AppellationCreatePageProps> = ({ onCreated }) => {
  const navigate = useNavigate();
  const [createItem, { isLoading, error, isSuccess }] = useCreateAppellationMutation();
  const methods = useForm<AppellationFormValues>({ defaultValues });
  const { reset } = methods;
  const onSubmit = async (values: AppellationFormValues) => { const createdItem = await createItem({ appellationCreationForm: values }).unwrap(); reset(defaultValues); if (createdItem.id) { await onCreated?.(createdItem.id); navigate(`/appellation/${createdItem.id}`); } };
  return <CrudFormPageShell methods={methods} title="Creer l'appellation" subtitle="Ajoutez le nom de l'appellation." submitLabel="Creer" onSubmit={onSubmit} isSubmitting={isLoading} showSuccess={isSuccess} successMessage="Appellation cree avec succes." showError={Boolean(error)} errorMessage="Erreur lors de la creation de l'appellation." ><AppellationForm disabled={isLoading} /></CrudFormPageShell>;
};
