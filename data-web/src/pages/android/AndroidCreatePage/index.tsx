import { useForm } from 'react-hook-form';
import type { FC } from 'react';
import { useNavigate } from 'react-router-dom';
import AndroidForm, { type AndroidFormValues, parseCategoriesText } from '../../../components/AndroidForm';
import { CrudFormPageShell } from '../../../components/CrudFormPageShell';
import { useCreateAndroidMutation } from '../../../services/androidApi';
import type { AndroidCreatePageProps } from './AndroidCreatePage.types';

const defaultValues: AndroidFormValues = {
  name: '',
  packageName: '',
  categoriesText: '',
  description: '',
  icon: '',
};

export const AndroidCreatePage: FC<AndroidCreatePageProps> = ({ onCreated }) => {
  const navigate = useNavigate();
  const [createAndroid, { isLoading, error, isSuccess }] = useCreateAndroidMutation();
  const methods = useForm<AndroidFormValues>({ defaultValues });
  const { reset } = methods;

  const onSubmit = async (values: AndroidFormValues) => {
    const createdAndroid = await createAndroid({
      androidCreationForm: {
        name: values.name,
        packageName: values.packageName,
        category: parseCategoriesText(values.categoriesText),
        description: values.description,
        icon: values.icon,
      },
    }).unwrap();

    reset(defaultValues);

    if (createdAndroid.id) {
      await onCreated?.(createdAndroid.id);
      navigate(`/android/${createdAndroid.id}`);
    }
  };

  return (
    <CrudFormPageShell
      methods={methods}
      title="Creer une application Android"
      subtitle="Ajoutez un nom, un package, des categories et une description."
      submitLabel="Creer"
      onSubmit={onSubmit}
      isSubmitting={isLoading}
      showSuccess={isSuccess}
      successMessage="Application Android creee avec succes."
      showError={Boolean(error)}
      errorMessage="Erreur lors de la creation de l'application Android."
    >
      <AndroidForm disabled={isLoading} />
    </CrudFormPageShell>
  );
};