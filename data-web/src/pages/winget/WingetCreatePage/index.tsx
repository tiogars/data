import { useForm } from 'react-hook-form';
import type { FC } from 'react';
import { useNavigate } from 'react-router-dom';
import WingetForm, { type WingetFormValues } from '../../../components/WingetForm';
import { parseTagsText } from '../../../components/WingetForm.helpers';
import { CrudFormPageShell } from '../../../components/CrudFormPageShell';
import { useCreateWingetMutation } from '../../../services/wingetApi';
import type { WingetCreatePageProps } from './WingetCreatePage.types';

const defaultValues: WingetFormValues = {
  name: '',
  description: '',
  wingetId: '',
  installCommand: '',
  tagsText: '',
};

export const WingetCreatePage: FC<WingetCreatePageProps> = ({ onCreated }) => {
  const navigate = useNavigate();
  const [createWinget, { isLoading, error, isSuccess }] = useCreateWingetMutation();
  const methods = useForm<WingetFormValues>({ defaultValues });
  const { reset } = methods;

  const onSubmit = async (values: WingetFormValues) => {
    const created = await createWinget({
      wingetCreationForm: {
        name: values.name,
        description: values.description,
        wingetId: values.wingetId,
        installCommand: values.installCommand,
        tags: parseTagsText(values.tagsText),
      },
    }).unwrap();

    reset(defaultValues);

    if (created.id) {
      await onCreated?.(created.id);
      navigate(`/winget/${created.id}`);
    }
  };

  return (
    <CrudFormPageShell
      methods={methods}
      title="Creer une application Winget"
      subtitle="Ajoutez le nom, l'identifiant Winget, la commande d'installation et les tags."
      submitLabel="Creer"
      onSubmit={onSubmit}
      isSubmitting={isLoading}
      showSuccess={isSuccess}
      successMessage="Application Winget creee avec succes."
      showError={Boolean(error)}
      errorMessage="Erreur lors de la creation de l'application Winget."
    >
      <WingetForm disabled={isLoading} />
    </CrudFormPageShell>
  );
};
