import { useForm } from 'react-hook-form';
import type { FC } from 'react';
import { useNavigate } from 'react-router-dom';
import ContenantForm, { type ContenantFormValues } from '../../../components/ContenantForm';
import { CrudFormPageShell } from '../../../components/CrudFormPageShell';
import { useCreateContenantMutation } from '../../../services/contenantApi';
import type { ContenantCreatePageProps } from './ContenantCreatePage.types';

const defaultValues: ContenantFormValues = { name: '', volumeCl: '' };

export const ContenantCreatePage: FC<ContenantCreatePageProps> = ({ onCreated }) => {
  const navigate = useNavigate();
  const [createContenant, { isLoading, error, isSuccess }] = useCreateContenantMutation();
  const methods = useForm<ContenantFormValues>({ defaultValues });
  const { reset } = methods;
  const onSubmit = async (values: ContenantFormValues) => {
    const createdContenant = await createContenant({ contenantCreationForm: { name: values.name, volumeCl: values.volumeCl ? Number(values.volumeCl) : undefined } }).unwrap();
    reset(defaultValues);
    if (createdContenant.id) { await onCreated?.(createdContenant.id); navigate(`/contenant/${createdContenant.id}`); }
  };
  return <CrudFormPageShell methods={methods} title="Creer un contenant" subtitle="Ajoutez le nom et le volume du contenant." submitLabel="Creer" onSubmit={onSubmit} isSubmitting={isLoading} showSuccess={isSuccess} successMessage="Contenant cree avec succes." showError={Boolean(error)} errorMessage="Erreur lors de la creation du contenant."><ContenantForm disabled={isLoading} /></CrudFormPageShell>;
};
