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
import { useCreateModelMutation } from '../../../services/modelApi';
import type { ModelCreatePageProps } from './ModelCreatePage.types';

const defaultValues: ModelFormValues = {
  name: '',
  description: '',
};

export const ModelCreatePage: FC<ModelCreatePageProps> = ({ onCreated }) => {
  const navigate = useNavigate();
  const [createModel, { isLoading, error, isSuccess }] = useCreateModelMutation();
  const methods = useForm<ModelFormValues>({ defaultValues });
  const { handleSubmit, reset } = methods;

  const onSubmit = async (values: ModelFormValues) => {
    const createdModel = await createModel({
      modelCreationForm: values,
    }).unwrap();

    reset(defaultValues);

    if (createdModel.id) {
      await onCreated?.(createdModel.id);
      navigate(`/model/${createdModel.id}`);
    }
  };

  return (
    <Box sx={{ maxWidth: 640, mx: 'auto', width: '100%' }}>
      <Paper sx={{ p: { xs: 2.5, md: 3 }, mt: 3 }}>
        <Stack spacing={3}>
          <Box>
            <Typography variant="h4" component="h1">
              Creer un modele
            </Typography>
            <Typography variant="body2" color="text.secondary">
              Ajoutez le nom et la description du modele.
            </Typography>
          </Box>
          <FormProvider {...methods}>
            <form onSubmit={handleSubmit(onSubmit)}>
              <Stack spacing={2.5}>
                <ModelForm disabled={isLoading} />
                <Button type="submit" variant="contained" disabled={isLoading} fullWidth>
                  Creer
                </Button>
              </Stack>
            </form>
          </FormProvider>
          {isSuccess && <Alert severity="success">Modele cree avec succes.</Alert>}
          {error && <Alert severity="error">Erreur lors de la creation du modele.</Alert>}
        </Stack>
      </Paper>
    </Box>
  );
};
