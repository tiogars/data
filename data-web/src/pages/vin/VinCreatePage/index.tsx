import { useForm } from 'react-hook-form';
import type { FC } from 'react';
import { useNavigate } from 'react-router-dom';
import VinForm, { type VinFormValues } from '../../../components/VinForm';
import { CrudFormPageShell } from '../../../components/CrudFormPageShell';
import { useCreateVinMutation } from '../../../services/vinApi';
import type { VinCreatePageProps } from './VinCreatePage.types';

const defaultValues: VinFormValues = {
  appellationId: '',
  couleurId: '',
  typeVinId: '',
  maisonId: '',
  vinNomId: '',
  contenantId: '',
  annee: '',
  degorgementMois: '',
  degorgementAnnee: '',
  dosageGrammesParLitre: '',
  commune: '',
  region: '',
  commentaires: '',
  accordsMetsVins: '',
  cepages: [],
  circonstanceIds: [],
  tagIds: [],
};

export const VinCreatePage: FC<VinCreatePageProps> = ({ onCreated }) => {
  const navigate = useNavigate();
  const [createVin, { isLoading, error, isSuccess }] = useCreateVinMutation();
  const methods = useForm<VinFormValues>({ defaultValues });
  const { reset } = methods;
  const onSubmit = async (values: VinFormValues) => {
    const createdVin = await createVin({
      vinCreationForm: {
        appellationId: values.appellationId || undefined,
        couleurId: values.couleurId || undefined,
        typeVinId: values.typeVinId || undefined,
        maisonId: values.maisonId || undefined,
        vinNomId: values.vinNomId || undefined,
        contenantId: values.contenantId || undefined,
        annee: values.annee ? Number(values.annee) : undefined,
        degorgementMois: values.degorgementMois ? Number(values.degorgementMois) : undefined,
        degorgementAnnee: values.degorgementAnnee ? Number(values.degorgementAnnee) : undefined,
        dosageGrammesParLitre: values.dosageGrammesParLitre ? Number(values.dosageGrammesParLitre) : undefined,
        commune: values.commune || undefined,
        region: values.region || undefined,
        commentaires: values.commentaires || undefined,
        accordsMetsVins: values.accordsMetsVins || undefined,
        cepages: values.cepages.map((c) => ({ cepageId: c.cepageId, pourcentage: c.pourcentage ? Number(c.pourcentage) : undefined })),
        circonstanceIds: values.circonstanceIds,
        tagIds: values.tagIds,
      },
    }).unwrap();
    reset(defaultValues);
    if (createdVin.id) { await onCreated?.(createdVin.id); navigate(`/vin/${createdVin.id}`); }
  };
  return <CrudFormPageShell methods={methods} title="Creer un vin" subtitle="Ajoutez les informations completes du vin." submitLabel="Creer" onSubmit={onSubmit} isSubmitting={isLoading} showSuccess={isSuccess} successMessage="Vin cree avec succes." showError={Boolean(error)} errorMessage="Erreur lors de la creation du vin."><VinForm disabled={isLoading} /></CrudFormPageShell>;
};
