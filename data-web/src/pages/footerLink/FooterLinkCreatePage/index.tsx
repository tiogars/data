import { useForm } from 'react-hook-form';
import type { FC } from 'react';
import { useNavigate } from 'react-router-dom';
import FooterLinkForm, { type FooterLinkFormValues } from '../../../components/FooterLinkForm';
import { CrudFormPageShell } from '../../../components/CrudFormPageShell';
import { useCreateFooterLinkMutation } from '../../../services/footerLinkApi';
import type { FooterLinkCreatePageProps } from './FooterLinkCreatePage.types';

const defaultValues: FooterLinkFormValues = {
  label: '',
  url: '',
  icon: 'react',
  displayOrder: 10,
};

export const FooterLinkCreatePage: FC<FooterLinkCreatePageProps> = ({ onCreated }) => {
  const navigate = useNavigate();
  const [createFooterLink, { isLoading, error, isSuccess }] = useCreateFooterLinkMutation();
  const methods = useForm<FooterLinkFormValues>({ defaultValues });
  const { reset } = methods;

  const onSubmit = async (values: FooterLinkFormValues) => {
    const createdFooterLink = await createFooterLink({
      footerLinkCreationForm: values,
    }).unwrap();

    reset(defaultValues);

    if (createdFooterLink.id) {
      await onCreated?.(createdFooterLink.id);
      navigate(`/footer-link/${createdFooterLink.id}`);
    }
  };

  return (
    <CrudFormPageShell
      methods={methods}
      title="Créer un lien de footer"
      subtitle="Ajoutez un nouvel élément qui sera chargé automatiquement dans le footer de l'application."
      submitLabel="Créer"
      onSubmit={onSubmit}
      isSubmitting={isLoading}
      showSuccess={isSuccess}
      successMessage="Lien créé avec succès."
      showError={Boolean(error)}
      errorMessage="Erreur lors de la création du lien."
    >
      <FooterLinkForm disabled={isLoading} />
    </CrudFormPageShell>
  );
};