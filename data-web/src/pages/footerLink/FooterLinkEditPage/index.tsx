import { useEffect } from 'react';
import { useForm } from 'react-hook-form';
import type { FC } from 'react';
import { useNavigate } from 'react-router-dom';
import FooterLinkForm, { type FooterLinkFormValues } from '../../../components/FooterLinkForm';
import { CrudFormPageShell } from '../../../components/CrudFormPageShell';
import { useGetFooterLinkByIdQuery, useUpdateFooterLinkMutation } from '../../../services/footerLinkApi';
import type { FooterLinkEditPageProps } from './FooterLinkEditPage.types';

const emptyValues: FooterLinkFormValues = {
  label: '',
  url: '',
  icon: 'react',
  displayOrder: 10,
};

export const FooterLinkEditPage: FC<FooterLinkEditPageProps> = ({ id }) => {
  const navigate = useNavigate();
  const { data, isLoading, error } = useGetFooterLinkByIdQuery({ id });
  const [updateFooterLink, { isLoading: isSaving, error: saveError, isSuccess }] = useUpdateFooterLinkMutation();
  const methods = useForm<FooterLinkFormValues>({ defaultValues: emptyValues });
  const { reset } = methods;

  useEffect(() => {
    if (data) {
      reset({
        label: data.label ?? '',
        url: data.url ?? '',
        icon: data.icon ?? 'react',
        displayOrder: data.displayOrder ?? 0,
      });
    }
  }, [data, reset]);

  const onSubmit = async (values: FooterLinkFormValues) => {
    await updateFooterLink({
      id,
      footerLink: {
        id,
        ...values,
      },
    }).unwrap();

    navigate(`/footer-link/${id}`);
  };

  if (isLoading) return <div>Chargement...</div>;
  if (error) return <div>Erreur lors du chargement du lien footer</div>;
  if (!data) return <div>Lien footer introuvable</div>;

  return (
    <CrudFormPageShell
      methods={methods}
      title="Modifier le lien de footer"
      subtitle="Mettez à jour les informations exposées dans le footer applicatif."
      submitLabel="Enregistrer"
      onSubmit={onSubmit}
      isSubmitting={isSaving}
      showSuccess={isSuccess}
      successMessage="Lien modifié avec succès."
      showError={Boolean(saveError)}
      errorMessage="Erreur lors de la modification du lien."
    >
      <FooterLinkForm disabled={isSaving} />
    </CrudFormPageShell>
  );
};