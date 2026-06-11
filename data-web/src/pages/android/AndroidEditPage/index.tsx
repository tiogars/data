import { useEffect } from 'react';
import { useForm } from 'react-hook-form';
import type { FC } from 'react';
import { useNavigate } from 'react-router-dom';
import AndroidForm, { type AndroidFormValues, formatCategoriesText, parseCategoriesText } from '../../../components/AndroidForm';
import { CrudFormPageShell } from '../../../components/CrudFormPageShell';
import { useGetAndroidQuery, useUpdateAndroidMutation } from '../../../services/androidApi';
import type { AndroidEditPageProps } from './AndroidEditPage.types';

const emptyValues: AndroidFormValues = {
  name: '',
  packageName: '',
  categoriesText: '',
  description: '',
  icon: '',
};

export const AndroidEditPage: FC<AndroidEditPageProps> = ({ id }) => {
  const navigate = useNavigate();
  const { data, isLoading, error } = useGetAndroidQuery({ id });
  const [updateAndroid, { isLoading: isSaving, error: saveError, isSuccess }] = useUpdateAndroidMutation();
  const methods = useForm<AndroidFormValues>({ defaultValues: emptyValues });
  const { reset } = methods;

  useEffect(() => {
    if (data) {
      reset({
        name: data.name ?? '',
        packageName: data.packageName ?? '',
        categoriesText: formatCategoriesText(data.category),
        description: data.description ?? '',
        icon: data.icon ?? '',
      });
    }
  }, [data, reset]);

  const onSubmit = async (values: AndroidFormValues) => {
    await updateAndroid({
      id,
      android: {
        id,
        name: values.name,
        packageName: values.packageName,
        category: parseCategoriesText(values.categoriesText),
        description: values.description,
        icon: values.icon,
      },
    }).unwrap();

    navigate(`/android/${id}`);
  };

  if (isLoading) return <div>Chargement...</div>;
  if (error) return <div>Erreur lors du chargement de l'application Android</div>;
  if (!data) return <div>Application Android introuvable</div>;

  return (
    <CrudFormPageShell
      methods={methods}
      title="Modifier une application Android"
      subtitle="Mettez a jour le nom, le package, les categories, la description et l'icone."
      submitLabel="Enregistrer"
      onSubmit={onSubmit}
      isSubmitting={isSaving}
      showSuccess={isSuccess}
      successMessage="Application Android modifiee avec succes."
      showError={Boolean(saveError)}
      errorMessage="Erreur lors de la modification de l'application Android."
    >
      <AndroidForm disabled={isSaving} />
    </CrudFormPageShell>
  );
};