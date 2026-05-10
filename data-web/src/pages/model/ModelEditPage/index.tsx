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
import ModelForm, { type ModelFormValues } from '../../../components/ModelForm';
import { useGetModelByIdQuery, useUpdateModelMutation } from '../../../services/modelApi';
import type { ModelEditPageProps } from './ModelEditPage.types';

const emptyValues: ModelFormValues = {
  name: '',
  description: '',
};

export const ModelEditPage: FC<ModelEditPageProps> = ({ id }) => {
  const navigate = useNavigate();
  const { data, isLoading, error } = useGetModelByIdQuery({ id });
  const [updateModel, { isLoading: isSaving, error: saveError, isSuccess }] = useUpdateModelMutation();
  const methods = useForm<ModelFormValues>({ defaultValues: emptyValues });
  const { handleSubmit, reset } = methods;

  useEffect(() => {
    if (data) {
      reset({
        name: data.name ?? '',
        description: data.description ?? '',
      });
    }
  }, [data, reset]);

  const onSubmit = async (values: ModelFormValues) => {
    await updateModel({
      id,
      model: {
        id,
        ...values,
      },
    }).unwrap();

    navigate(`/model/${id}`);
  };

  if (isLoading) return <div>Chargement...</div>;
  if (error) return <div>Erreur lors du chargement du modele</div>;
  if (!data) return <div>Modele introuvable</div>;

  return (
    <Box sx={{ maxWidth: 640, mx: 'auto', width: '100%' }}>
      <Paper sx={{ p: { xs: 2.5, md: 3 }, mt: 3 }}>
        <Stack spacing={3}>
          <Box>
            <Typography variant="h4" component="h1">
              Modifier un modele
            </Typography>
            <Typography variant="body2" color="text.secondary">
              Mettez a jour le nom et la description du modele.
            </Typography>
          </Box>
          <FormProvider {...methods}>
            <form onSubmit={handleSubmit(onSubmit)}>
              <Stack spacing={2.5}>
                <ModelForm disabled={isSaving} />
                <Button type="submit" variant="contained" disabled={isSaving} fullWidth>
                  Enregistrer
                </Button>
              </Stack>
            </form>
          </FormProvider>
          {isSuccess && <Alert severity="success">Modele modifie avec succes.</Alert>}
          {saveError && <Alert severity="error">Erreur lors de la modification du modele.</Alert>}
        </Stack>
      </Paper>
    </Box>
  );
};
