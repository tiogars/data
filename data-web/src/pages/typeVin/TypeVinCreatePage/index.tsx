import { useForm } from 'react-hook-form';
import type { FC } from 'react';
import { useNavigate } from 'react-router-dom';
import TypeVinForm, { type TypeVinFormValues } from '../../../components/TypeVinForm';
import { CrudFormPageShell } from '../../../components/CrudFormPageShell';
import { useCreateTypeVinMutation } from '../../../services/typeVinApi';
import type { TypeVinCreatePageProps } from './TypeVinCreatePage.types';
const defaultValues: TypeVinFormValues = { name: '' };
export const TypeVinCreatePage: FC<TypeVinCreatePageProps> = ({ onCreated }) => {
  const navigate = useNavigate();
  const [createItem, { isLoading, error, isSuccess }] = useCreateTypeVinMutation();
  const methods = useForm<TypeVinFormValues>({ defaultValues });
  const { reset } = methods;
  const onSubmit = async (values: TypeVinFormValues) => { const createdItem = await createItem({ typeVinCreationForm: values }).unwrap(); reset(defaultValues); if (createdItem.id) { await onCreated?.(createdItem.id); navigate(`/type-vin/${createdItem.id}`); } };
  return <CrudFormPageShell methods={methods} title="Creer le type de vin" subtitle="Ajoutez le nom de le type de vin." submitLabel="Creer" onSubmit={onSubmit} isSubmitting={isLoading} showSuccess={isSuccess} successMessage="Type de vin cree avec succes." showError={Boolean(error)} errorMessage="Erreur lors de la creation de le type de vin." ><TypeVinForm disabled={isLoading} /></CrudFormPageShell>;
};
