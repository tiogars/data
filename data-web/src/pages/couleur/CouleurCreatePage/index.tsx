import { useForm } from 'react-hook-form';
import type { FC } from 'react';
import { useNavigate } from 'react-router-dom';
import CouleurForm, { type CouleurFormValues } from '../../../components/CouleurForm';
import { CrudFormPageShell } from '../../../components/CrudFormPageShell';
import { useCreateCouleurMutation } from '../../../services/couleurApi';
import type { CouleurCreatePageProps } from './CouleurCreatePage.types';
const defaultValues: CouleurFormValues = { name: '' };
export const CouleurCreatePage: FC<CouleurCreatePageProps> = ({ onCreated }) => {
  const navigate = useNavigate();
  const [createItem, { isLoading, error, isSuccess }] = useCreateCouleurMutation();
  const methods = useForm<CouleurFormValues>({ defaultValues });
  const { reset } = methods;
  const onSubmit = async (values: CouleurFormValues) => { const createdItem = await createItem({ couleurCreationForm: values }).unwrap(); reset(defaultValues); if (createdItem.id) { await onCreated?.(createdItem.id); navigate(`/couleur/${createdItem.id}`); } };
  return <CrudFormPageShell methods={methods} title="Creer la couleur" subtitle="Ajoutez le nom de la couleur." submitLabel="Creer" onSubmit={onSubmit} isSubmitting={isLoading} showSuccess={isSuccess} successMessage="Couleur cree avec succes." showError={Boolean(error)} errorMessage="Erreur lors de la creation de la couleur." ><CouleurForm disabled={isLoading} /></CrudFormPageShell>;
};
