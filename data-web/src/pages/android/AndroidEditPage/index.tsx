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
import AndroidForm, { type AndroidFormValues, formatCategoriesText, parseCategoriesText } from '../../../components/AndroidForm';
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
  const { handleSubmit, reset } = methods;

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
    <Box sx={{ maxWidth: 640, mx: 'auto', width: '100%' }}>
      <Paper sx={{ p: { xs: 2.5, md: 3 }, mt: 3 }}>
        <Stack spacing={3}>
          <Box>
            <Typography variant="h4" component="h1">
              Modifier une application Android
            </Typography>
            <Typography variant="body2" color="text.secondary">
              Mettez a jour le nom, le package, les categories, la description et l'icone.
            </Typography>
          </Box>
          <FormProvider {...methods}>
            <form onSubmit={handleSubmit(onSubmit)}>
              <Stack spacing={2.5}>
                <AndroidForm disabled={isSaving} />
                <Button type="submit" variant="contained" disabled={isSaving} fullWidth>
                  Enregistrer
                </Button>
              </Stack>
            </form>
          </FormProvider>
          {isSuccess && <Alert severity="success">Application Android modifiee avec succes.</Alert>}
          {saveError && <Alert severity="error">Erreur lors de la modification de l'application Android.</Alert>}
        </Stack>
      </Paper>
    </Box>
  );
};