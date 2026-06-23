import { useEffect } from 'react';
import { useForm } from 'react-hook-form';
import type { FC } from 'react';
import { useNavigate } from 'react-router-dom';
import ContenantForm, { type ContenantFormValues } from '../../../components/ContenantForm';
import { CrudFormPageShell } from '../../../components/CrudFormPageShell';
import { useGetContenantQuery, useUpdateContenantMutation } from '../../../services/contenantApi';
import type { ContenantEditPageProps } from './ContenantEditPage.types';

const emptyValues: ContenantFormValues = { name: '', volumeCl: '' };

export const ContenantEditPage: FC<ContenantEditPageProps> = ({ id }) => {
  const navigate = useNavigate();
  const { data, isLoading, error } = useGetContenantQuery({ id });
  const [updateContenant, { isLoading: isSaving, error: saveError, isSuccess }] = useUpdateContenantMutation();
  const methods = useForm<ContenantFormValues>({ defaultValues: emptyValues });
  const { reset } = methods;
  useEffect(() => { if (data) reset({ name: data.name ?? '', volumeCl: data.volumeCl?.toString() ?? '' }); }, [data, reset]);
  const onSubmit = async (values: ContenantFormValues) => { await updateContenant({ id, contenant: { id, name: values.name, volumeCl: values.volumeCl ? Number(values.volumeCl) : undefined } }).unwrap(); navigate(`/contenant/${id}`); };
  if (isLoading) return <div>Chargement...</div>;
  if (error) return <div>Erreur lors du chargement du contenant</div>;
  if (!data) return <div>Contenant introuvable</div>;
  return <CrudFormPageShell methods={methods} title="Modifier un contenant" subtitle="Mettez a jour le nom et le volume du contenant." submitLabel="Enregistrer" onSubmit={onSubmit} isSubmitting={isSaving} showSuccess={isSuccess} successMessage="Contenant modifie avec succes." showError={Boolean(saveError)} errorMessage="Erreur lors de la modification du contenant."><ContenantForm disabled={isSaving} /></CrudFormPageShell>;
};
