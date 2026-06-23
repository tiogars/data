import { useForm } from 'react-hook-form';
import type { FC } from 'react';
import { useNavigate } from 'react-router-dom';
import VinNomForm, { type VinNomFormValues } from '../../../components/VinNomForm';
import { CrudFormPageShell } from '../../../components/CrudFormPageShell';
import { useCreateVinNomMutation } from '../../../services/vinNomApi';
import type { VinNomCreatePageProps } from './VinNomCreatePage.types';

const defaultValues: VinNomFormValues = { name: '', maisonId: '' };

export const VinNomCreatePage: FC<VinNomCreatePageProps> = ({ onCreated }) => {
  const navigate = useNavigate();
  const [createVinNom, { isLoading, error, isSuccess }] = useCreateVinNomMutation();
  const methods = useForm<VinNomFormValues>({ defaultValues });
  const { reset } = methods;
  const onSubmit = async (values: VinNomFormValues) => {
    const createdVinNom = await createVinNom({ vinNomCreationForm: { name: values.name, maisonId: values.maisonId || undefined } }).unwrap();
    reset(defaultValues);
    if (createdVinNom.id) { await onCreated?.(createdVinNom.id); navigate(`/vin-nom/${createdVinNom.id}`); }
  };
  return <CrudFormPageShell methods={methods} title="Creer un nom de vin" subtitle="Ajoutez le nom du vin et sa maison." submitLabel="Creer" onSubmit={onSubmit} isSubmitting={isLoading} showSuccess={isSuccess} successMessage="Nom de vin cree avec succes." showError={Boolean(error)} errorMessage="Erreur lors de la creation du nom de vin."><VinNomForm disabled={isLoading} /></CrudFormPageShell>;
};
