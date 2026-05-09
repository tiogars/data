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
import BrandForm, { type BrandFormValues } from '../../../components/BrandForm';
import { useGetBrandByIdQuery, useUpdateBrandMutation } from '../../../services/brandApi';
import type { BrandEditPageProps } from './BrandEditPage.types';

const emptyValues: BrandFormValues = {
  name: '',
  description: '',
};

export const BrandEditPage: FC<BrandEditPageProps> = ({ id }) => {
  const navigate = useNavigate();
  const { data, isLoading, error } = useGetBrandByIdQuery({ id });
  const [updateBrand, { isLoading: isSaving, error: saveError, isSuccess }] = useUpdateBrandMutation();
  const methods = useForm<BrandFormValues>({ defaultValues: emptyValues });
  const { handleSubmit, reset } = methods;

  useEffect(() => {
    if (data) {
      reset({
        name: data.name ?? '',
        description: data.description ?? '',
      });
    }
  }, [data, reset]);

  const onSubmit = async (values: BrandFormValues) => {
    await updateBrand({
      id,
      brand: {
        id,
        ...values,
      },
    }).unwrap();

    navigate(`/brand/${id}`);
  };

  if (isLoading) return <div>Chargement...</div>;
  if (error) return <div>Erreur lors du chargement de la marque</div>;
  if (!data) return <div>Marque introuvable</div>;

  return (
    <Box sx={{ maxWidth: 640, mx: 'auto', width: '100%' }}>
      <Paper sx={{ p: { xs: 2.5, md: 3 }, mt: 3 }}>
        <Stack spacing={3}>
          <Box>
            <Typography variant="h4" component="h1">
              Modifier une marque
            </Typography>
            <Typography variant="body2" color="text.secondary">
              Mettez a jour le nom et la description de la marque.
            </Typography>
          </Box>
          <FormProvider {...methods}>
            <form onSubmit={handleSubmit(onSubmit)}>
              <Stack spacing={2.5}>
                <BrandForm disabled={isSaving} />
                <Button type="submit" variant="contained" disabled={isSaving} fullWidth>
                  Enregistrer
                </Button>
              </Stack>
            </form>
          </FormProvider>
          {isSuccess && <Alert severity="success">Marque modifiee avec succes.</Alert>}
          {saveError && <Alert severity="error">Erreur lors de la modification de la marque.</Alert>}
        </Stack>
      </Paper>
    </Box>
  );
};
