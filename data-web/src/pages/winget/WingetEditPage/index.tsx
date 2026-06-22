import { useEffect } from 'react';
import { useForm } from 'react-hook-form';
import type { FC } from 'react';
import { useNavigate } from 'react-router-dom';
import WingetForm, { type WingetFormValues } from '../../../components/WingetForm';
import { formatTagsText, parseTagsText } from '../../../components/WingetForm.helpers';
import { CrudFormPageShell } from '../../../components/CrudFormPageShell';
import { useGetWingetQuery, useUpdateWingetMutation } from '../../../services/wingetApi';
import type { WingetEditPageProps } from './WingetEditPage.types';

const emptyValues: WingetFormValues = {
  name: '',
  description: '',
  wingetId: '',
  installCommand: '',
  tagsText: '',
};

export const WingetEditPage: FC<WingetEditPageProps> = ({ id }) => {
  const navigate = useNavigate();
  const { data, isLoading, error } = useGetWingetQuery({ id });
  const [updateWinget, { isLoading: isSaving, error: saveError, isSuccess }] = useUpdateWingetMutation();
  const methods = useForm<WingetFormValues>({ defaultValues: emptyValues });
  const { reset } = methods;

  useEffect(() => {
    if (data) {
      reset({
        name: data.name ?? '',
        description: data.description ?? '',
        wingetId: data.wingetId ?? '',
        installCommand: data.installCommand ?? '',
        tagsText: formatTagsText(data.tags),
      });
    }
  }, [data, reset]);

  const onSubmit = async (values: WingetFormValues) => {
    await updateWinget({
      id,
      wingetUpdateForm: {
        id,
        name: values.name,
        description: values.description,
        wingetId: values.wingetId,
        installCommand: values.installCommand,
        tags: parseTagsText(values.tagsText),
      },
    }).unwrap();

    navigate(`/winget/${id}`);
  };

  if (isLoading) return <div>Chargement...</div>;
  if (error) return <div>Erreur lors du chargement de l'application Winget</div>;
  if (!data) return <div>Application Winget introuvable</div>;

  return (
    <CrudFormPageShell
      methods={methods}
      title="Modifier une application Winget"
      subtitle="Mettez a jour les informations Winget et la commande d'installation."
      submitLabel="Enregistrer"
      onSubmit={onSubmit}
      isSubmitting={isSaving}
      showSuccess={isSuccess}
      successMessage="Application Winget modifiee avec succes."
      showError={Boolean(saveError)}
      errorMessage="Erreur lors de la modification de l'application Winget."
    >
      <WingetForm disabled={isSaving} />
    </CrudFormPageShell>
  );
};
