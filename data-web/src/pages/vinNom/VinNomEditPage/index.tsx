import { useEffect } from 'react';
import { useForm } from 'react-hook-form';
import type { FC } from 'react';
import { useNavigate } from 'react-router-dom';
import VinNomForm, { type VinNomFormValues } from '../../../components/VinNomForm';
import { CrudFormPageShell } from '../../../components/CrudFormPageShell';
import { useGetVinNomQuery, useUpdateVinNomMutation } from '../../../services/vinNomApi';
import type { VinNomEditPageProps } from './VinNomEditPage.types';

const emptyValues: VinNomFormValues = { name: '', maisonId: '' };

export const VinNomEditPage: FC<VinNomEditPageProps> = ({ id }) => {
  const navigate = useNavigate();
  const { data, isLoading, error } = useGetVinNomQuery({ id });
  const [updateVinNom, { isLoading: isSaving, error: saveError, isSuccess }] = useUpdateVinNomMutation();
  const methods = useForm<VinNomFormValues>({ defaultValues: emptyValues });
  const { reset } = methods;
  useEffect(() => { if (data) reset({ name: data.name ?? '', maisonId: data.maisonId ?? '' }); }, [data, reset]);
  const onSubmit = async (values: VinNomFormValues) => { await updateVinNom({ id, vinNom: { id, name: values.name, maisonId: values.maisonId || undefined, maisonName: data?.maisonName } }).unwrap(); navigate(`/vin-nom/${id}`); };
  if (isLoading) return <div>Chargement...</div>;
  if (error) return <div>Erreur lors du chargement du nom de vin</div>;
  if (!data) return <div>Nom de vin introuvable</div>;
  return <CrudFormPageShell methods={methods} title="Modifier un nom de vin" subtitle="Mettez a jour le nom du vin et sa maison." submitLabel="Enregistrer" onSubmit={onSubmit} isSubmitting={isSaving} showSuccess={isSuccess} successMessage="Nom de vin modifie avec succes." showError={Boolean(saveError)} errorMessage="Erreur lors de la modification du nom de vin."><VinNomForm disabled={isSaving} /></CrudFormPageShell>;
};
