import { useForm, FormProvider } from 'react-hook-form';
import type { FC } from 'react';
import { useNavigate } from 'react-router-dom';
import Alert from '@mui/material/Alert';
import Box from '@mui/material/Box';
import Button from '@mui/material/Button';
import Paper from '@mui/material/Paper';
import Stack from '@mui/material/Stack';
import Typography from '@mui/material/Typography';
import GtinForm, { type GtinFormValues } from '../../../components/GtinForm';
import { useCreateGtinMutation } from '../../../services/gtinApi';
import type { GtinCreatePageProps } from './GtinCreatePage.types';

const defaultValues: GtinFormValues = {
  code: '',
  description: '',
};

export const GtinCreatePage: FC<GtinCreatePageProps> = ({ onCreated }) => {
  const navigate = useNavigate();
  const [createGtin, { isLoading, error, isSuccess }] = useCreateGtinMutation();
  const methods = useForm<GtinFormValues>({ defaultValues });
  const { handleSubmit, reset } = methods;

  const onSubmit = async (values: GtinFormValues) => {
    const createdGtin = await createGtin({
      gtinCreationForm: values,
    }).unwrap();

    reset(defaultValues);

    if (createdGtin.id) {
      await onCreated?.(createdGtin.id);
      navigate(`/gtin/${createdGtin.id}`);
    }
  };

  return (
    <Box sx={{ maxWidth: 640, mx: 'auto', width: '100%' }}>
      <Paper sx={{ p: { xs: 2.5, md: 3 }, mt: 3 }}>
        <Stack spacing={3}>
          <Box>
            <Typography variant="h4" component="h1">
              Creer un GTIN
            </Typography>
            <Typography variant="body2" color="text.secondary">
              Ajoutez un code GTIN et sa description.
            </Typography>
          </Box>
          <FormProvider {...methods}>
            <form onSubmit={handleSubmit(onSubmit)}>
              <Stack spacing={2.5}>
                <GtinForm disabled={isLoading} />
                <Button type="submit" variant="contained" disabled={isLoading} fullWidth>
                  Creer
                </Button>
              </Stack>
            </form>
          </FormProvider>
          {isSuccess && <Alert severity="success">GTIN cree avec succes.</Alert>}
          {error && <Alert severity="error">Erreur lors de la creation du GTIN.</Alert>}
        </Stack>
      </Paper>
    </Box>
  );
};
