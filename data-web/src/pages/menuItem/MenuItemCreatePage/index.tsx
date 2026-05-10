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
  const { handleSubmit, reset } = methods;

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
    <Box sx={{ maxWidth: 640, mx: 'auto', width: '100%' }}>
      <Paper sx={{ p: { xs: 2.5, md: 3 }, mt: 3 }}>
        <Stack spacing={3}>
          <Box>
            <Typography variant="h4" component="h1">
              Creer une entree de menu
            </Typography>
            <Typography variant="body2" color="text.secondary">
              Ajoutez un element de navigation charge depuis la base de donnees.
            </Typography>
          </Box>
          <FormProvider {...methods}>
            <form onSubmit={handleSubmit(onSubmit)}>
              <Stack spacing={2.5}>
                <MenuItemForm disabled={isLoading} />
                <Button type="submit" variant="contained" disabled={isLoading} fullWidth>
                  Creer
                </Button>
              </Stack>
            </form>
          </FormProvider>
          {isSuccess && <Alert severity="success">Entree creee avec succes.</Alert>}
          {error && <Alert severity="error">Erreur lors de la creation de l'entree.</Alert>}
        </Stack>
      </Paper>
    </Box>
  );
};
