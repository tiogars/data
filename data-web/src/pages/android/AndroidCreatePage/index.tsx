import { useForm, FormProvider } from 'react-hook-form';
import type { FC } from 'react';
import { useNavigate } from 'react-router-dom';
import Alert from '@mui/material/Alert';
import Box from '@mui/material/Box';
import Button from '@mui/material/Button';
import Paper from '@mui/material/Paper';
import Stack from '@mui/material/Stack';
import Typography from '@mui/material/Typography';
import AndroidForm, { type AndroidFormValues, parseCategoriesText } from '../../../components/AndroidForm';
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
  const { handleSubmit, reset } = methods;

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
    <Box sx={{ maxWidth: 640, mx: 'auto', width: '100%' }}>
      <Paper sx={{ p: { xs: 2.5, md: 3 }, mt: 3 }}>
        <Stack spacing={3}>
          <Box>
            <Typography variant="h4" component="h1">
              Creer une application Android
            </Typography>
            <Typography variant="body2" color="text.secondary">
              Ajoutez un nom, un package, des categories et une description.
            </Typography>
          </Box>
          <FormProvider {...methods}>
            <form onSubmit={handleSubmit(onSubmit)}>
              <Stack spacing={2.5}>
                <AndroidForm disabled={isLoading} />
                <Button type="submit" variant="contained" disabled={isLoading} fullWidth>
                  Creer
                </Button>
              </Stack>
            </form>
          </FormProvider>
          {isSuccess && <Alert severity="success">Application Android creee avec succes.</Alert>}
          {error && <Alert severity="error">Erreur lors de la creation de l'application Android.</Alert>}
        </Stack>
      </Paper>
    </Box>
  );
};