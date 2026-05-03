import { useEffect } from 'react';
import { useForm, FormProvider } from 'react-hook-form';
import type { FC } from 'react';
import { useNavigate } from 'react-router-dom';
import Alert from '@mui/material/Alert';
import Box from '@mui/material/Box';
import Button from '@mui/material/Button';
import Paper from '@mui/material/Paper';
import Stack from '@mui/material/Stack';
import Typography from '@mui/material/Typography';
import MenuItemForm, { type MenuItemFormValues } from '../../../components/MenuItemForm';
import { useGetMenuItemByIdQuery, useUpdateMenuItemMutation } from '../../../services/menuItemApi';
import type { MenuItemEditPageProps } from './MenuItemEditPage.types';

const emptyValues: MenuItemFormValues = {
  label: '',
  path: '',
  icon: 'menu',
  displayOrder: 10,
};

export const MenuItemEditPage: FC<MenuItemEditPageProps> = ({ id }) => {
  const navigate = useNavigate();
  const { data, isLoading, error } = useGetMenuItemByIdQuery({ id });
  const [updateMenuItem, { isLoading: isSaving, error: saveError, isSuccess }] = useUpdateMenuItemMutation();
  const methods = useForm<MenuItemFormValues>({ defaultValues: emptyValues });
  const { handleSubmit, reset } = methods;

  useEffect(() => {
    if (data) {
      reset({
        label: data.label ?? '',
        path: data.path ?? '',
        icon: data.icon ?? 'menu',
        displayOrder: data.displayOrder ?? 0,
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
    <Box sx={{ maxWidth: 640, mx: 'auto', width: '100%' }}>
      <Paper sx={{ p: { xs: 2.5, md: 3 }, mt: 3 }}>
        <Stack spacing={3}>
          <Box>
            <Typography variant="h4" component="h1">
              Modifier l'entree de menu
            </Typography>
            <Typography variant="body2" color="text.secondary">
              Mettez a jour les informations de navigation chargees dans la sidebar.
            </Typography>
          </Box>
          <FormProvider {...methods}>
            <form onSubmit={handleSubmit(onSubmit)}>
              <Stack spacing={2.5}>
                <MenuItemForm disabled={isSaving} />
                <Button type="submit" variant="contained" disabled={isSaving} fullWidth>
                  Enregistrer
                </Button>
              </Stack>
            </form>
          </FormProvider>
          {isSuccess && <Alert severity="success">Entree modifiee avec succes.</Alert>}
          {saveError && <Alert severity="error">Erreur lors de la modification de l'entree.</Alert>}
        </Stack>
      </Paper>
    </Box>
  );
};
