import { useEffect } from 'react';
import { useForm } from 'react-hook-form';
import type { FC } from 'react';
import { useNavigate } from 'react-router-dom';
import MenuItemForm, { type MenuItemFormValues } from '../../../components/MenuItemForm';
import { CrudFormPageShell } from '../../../components/CrudFormPageShell';
import { useGetMenuItemByIdQuery, useUpdateMenuItemMutation } from '../../../services/menuItemApi';
import type { MenuItemEditPageProps } from './MenuItemEditPage.types';

const emptyValues: MenuItemFormValues = {
  label: '',
  path: '',
  icon: 'menu',
  displayOrder: 10,
  parentId: undefined,
};

export const MenuItemEditPage: FC<MenuItemEditPageProps> = ({ id }) => {
  const navigate = useNavigate();
  const { data, isLoading, error } = useGetMenuItemByIdQuery({ id });
  const [updateMenuItem, { isLoading: isSaving, error: saveError, isSuccess }] = useUpdateMenuItemMutation();
  const methods = useForm<MenuItemFormValues>({ defaultValues: emptyValues });
  const { reset } = methods;

  useEffect(() => {
    if (data) {
      reset({
        label: data.label ?? '',
        path: data.path ?? '',
        icon: data.icon ?? 'menu',
        displayOrder: data.displayOrder ?? 0,
        parentId: data.parentId,
      });
    }
  }, [data, reset]);

  const onSubmit = async (values: MenuItemFormValues) => {
    await updateMenuItem({
      id,
      menuItem: {
        id,
        defaultLoaded: data?.defaultLoaded,
        ...values,
      },
    }).unwrap();

    navigate(`/menu-item/${id}`);
  };

  if (isLoading) return <div>Chargement...</div>;
  if (error) return <div>Erreur lors du chargement de l'entree de menu</div>;
  if (!data) return <div>Entree de menu introuvable</div>;

  return (
    <CrudFormPageShell
      methods={methods}
      title="Modifier l'entree de menu"
      subtitle="Mettez a jour les informations de navigation chargees dans la sidebar."
      submitLabel="Enregistrer"
      onSubmit={onSubmit}
      isSubmitting={isSaving}
      showSuccess={isSuccess}
      successMessage="Entree modifiee avec succes."
      showError={Boolean(saveError)}
      errorMessage="Erreur lors de la modification de l'entree."
    >
      <MenuItemForm disabled={isSaving} />
    </CrudFormPageShell>
  );
};
