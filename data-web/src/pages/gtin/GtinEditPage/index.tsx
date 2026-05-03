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
import GtinForm, { type GtinFormValues } from '../../../components/GtinForm';
import { useGetGtinByIdQuery, useUpdateGtinMutation } from '../../../services/gtinApi';
import type { GtinEditPageProps } from './GtinEditPage.types';

const emptyValues: GtinFormValues = {
  code: '',
  description: '',
};

export const GtinEditPage: FC<GtinEditPageProps> = ({ id }) => {
  const navigate = useNavigate();
  const { data, isLoading, error } = useGetGtinByIdQuery({ id });
  const [updateGtin, { isLoading: isSaving, error: saveError, isSuccess }] = useUpdateGtinMutation();
  const methods = useForm<GtinFormValues>({ defaultValues: emptyValues });
  const { handleSubmit, reset } = methods;

  useEffect(() => {
    if (data) {
      reset({
        code: data.code ?? '',
        description: data.description ?? '',
      });
    }
  }, [data, reset]);

  const onSubmit = async (values: GtinFormValues) => {
    await updateGtin({
      id,
      gtin: {
        id,
        ...values,
      },
    }).unwrap();

    navigate(`/gtin/${id}`);
  };

  if (isLoading) return <div>Chargement...</div>;
  if (error) return <div>Erreur lors du chargement du GTIN</div>;
  if (!data) return <div>GTIN introuvable</div>;

  return (
    <Box sx={{ maxWidth: 640, mx: 'auto', width: '100%' }}>
      <Paper sx={{ p: { xs: 2.5, md: 3 }, mt: 3 }}>
        <Stack spacing={3}>
          <Box>
            <Typography variant="h4" component="h1">
              Modifier un GTIN
            </Typography>
            <Typography variant="body2" color="text.secondary">
              Mettez a jour le code et la description du GTIN.
            </Typography>
          </Box>
          <FormProvider {...methods}>
            <form onSubmit={handleSubmit(onSubmit)}>
              <Stack spacing={2.5}>
                <GtinForm disabled={isSaving} />
                <Button type="submit" variant="contained" disabled={isSaving} fullWidth>
                  Enregistrer
                </Button>
              </Stack>
            </form>
          </FormProvider>
          {isSuccess && <Alert severity="success">GTIN modifie avec succes.</Alert>}
          {saveError && <Alert severity="error">Erreur lors de la modification du GTIN.</Alert>}
        </Stack>
      </Paper>
    </Box>
  );
};
