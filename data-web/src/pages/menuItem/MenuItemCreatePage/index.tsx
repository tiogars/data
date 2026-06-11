import { useForm } from 'react-hook-form';
import type { FC } from 'react';
import { useNavigate } from 'react-router-dom';
import MenuItemForm, { type MenuItemFormValues } from '../../../components/MenuItemForm';
import { CrudFormPageShell } from '../../../components/CrudFormPageShell';
import { useCreateMenuItemMutation } from '../../../services/menuItemApi';
import type { MenuItemCreatePageProps } from './MenuItemCreatePage.types';

const defaultValues: MenuItemFormValues = {
  label: '',
  path: '',
  icon: 'menu',
  displayOrder: 10,
  parentId: undefined,
};

export const MenuItemCreatePage: FC<MenuItemCreatePageProps> = ({ onCreated }) => {
  const navigate = useNavigate();
  const [createMenuItem, { isLoading, error, isSuccess }] = useCreateMenuItemMutation();
  const methods = useForm<MenuItemFormValues>({ defaultValues });
  const { reset } = methods;

  const onSubmit = async (values: MenuItemFormValues) => {
    const createdMenuItem = await createMenuItem({
      menuItemCreationForm: values,
    }).unwrap();

    reset(defaultValues);

    if (createdMenuItem.id) {
      await onCreated?.(createdMenuItem.id);
      navigate(`/menu-item/${createdMenuItem.id}`);
    }
  };

  return (
    <CrudFormPageShell
      methods={methods}
      title="Creer une entree de menu"
      subtitle="Ajoutez un element de navigation charge depuis la base de donnees."
      submitLabel="Creer"
      onSubmit={onSubmit}
      isSubmitting={isLoading}
      showSuccess={isSuccess}
      successMessage="Entree creee avec succes."
      showError={Boolean(error)}
      errorMessage="Erreur lors de la creation de l'entree."
    >
      <MenuItemForm disabled={isLoading} />
    </CrudFormPageShell>
  );
};
