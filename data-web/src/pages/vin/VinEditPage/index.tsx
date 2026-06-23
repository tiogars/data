import { useEffect } from 'react';
import { useForm } from 'react-hook-form';
import type { FC } from 'react';
import { useNavigate } from 'react-router-dom';
import VinForm, { type VinFormValues } from '../../../components/VinForm';
import { CrudFormPageShell } from '../../../components/CrudFormPageShell';
import { type Vin, useGetVinQuery, useUpdateVinMutation } from '../../../services/vinApi';
import type { VinEditPageProps } from './VinEditPage.types';

const emptyValues: VinFormValues = { appellationId: '', couleurId: '', typeVinId: '', maisonId: '', vinNomId: '', contenantId: '', annee: '', commune: '', region: '', commentaires: '', accordsMetsVins: '', cepages: [], circonstanceIds: [], tagIds: [] };

export const VinEditPage: FC<VinEditPageProps> = ({ id }) => {
  const navigate = useNavigate();
  const { data, isLoading, error } = useGetVinQuery({ id });
  const [updateVin, { isLoading: isSaving, error: saveError, isSuccess }] = useUpdateVinMutation();
  const methods = useForm<VinFormValues>({ defaultValues: emptyValues });
  const { reset } = methods;
  useEffect(() => { if (data) reset({ appellationId: data.appellationId ?? '', couleurId: data.couleurId ?? '', typeVinId: data.typeVinId ?? '', maisonId: data.maisonId ?? '', vinNomId: data.vinNomId ?? '', contenantId: data.contenantId ?? '', annee: data.annee?.toString() ?? '', commune: data.commune ?? '', region: data.region ?? '', commentaires: data.commentaires ?? '', accordsMetsVins: data.accordsMetsVins ?? '', cepages: (data.cepages ?? []).map((c) => ({ cepageId: c.cepageId ?? '', pourcentage: c.pourcentage?.toString() ?? '' })), circonstanceIds: data.circonstances ?? [], tagIds: data.tags ?? [] }); }, [data, reset]);
  const onSubmit = async (values: VinFormValues) => { const payload = { id, appellationId: values.appellationId || undefined, couleurId: values.couleurId || undefined, typeVinId: values.typeVinId || undefined, maisonId: values.maisonId || undefined, vinNomId: values.vinNomId || undefined, contenantId: values.contenantId || undefined, annee: values.annee ? Number(values.annee) : undefined, commune: values.commune || undefined, region: values.region || undefined, commentaires: values.commentaires || undefined, accordsMetsVins: values.accordsMetsVins || undefined, cepages: values.cepages.map((c) => ({ cepageId: c.cepageId, pourcentage: c.pourcentage ? Number(c.pourcentage) : undefined })), circonstanceIds: values.circonstanceIds, tagIds: values.tagIds }; await updateVin({ id, vin: payload as unknown as Vin }).unwrap(); navigate(`/vin/${id}`); };
  if (isLoading) return <div>Chargement...</div>;
  if (error) return <div>Erreur lors du chargement du vin</div>;
  if (!data) return <div>Vin introuvable</div>;
  return <CrudFormPageShell methods={methods} title="Modifier un vin" subtitle="Mettez a jour les informations completes du vin." submitLabel="Enregistrer" onSubmit={onSubmit} isSubmitting={isSaving} showSuccess={isSuccess} successMessage="Vin modifie avec succes." showError={Boolean(saveError)} errorMessage="Erreur lors de la modification du vin."><VinForm disabled={isSaving} /></CrudFormPageShell>;
};
