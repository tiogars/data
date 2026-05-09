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
import { useCreateBrandMutation } from '../../../services/brandApi';
import type { BrandCreatePageProps } from './BrandCreatePage.types';

const defaultValues: BrandFormValues = {
  name: '',
  description: '',
};

export const BrandCreatePage: FC<BrandCreatePageProps> = ({ onCreated }) => {
  const navigate = useNavigate();
  const [createBrand, { isLoading, error, isSuccess }] = useCreateBrandMutation();
  const methods = useForm<BrandFormValues>({ defaultValues });
  const { handleSubmit, reset } = methods;

  const onSubmit = async (values: BrandFormValues) => {
    const createdBrand = await createBrand({
      brandCreationForm: values,
    }).unwrap();

    reset(defaultValues);

    if (createdBrand.id) {
      await onCreated?.(createdBrand.id);
      navigate(`/brand/${createdBrand.id}`);
    }
  };

  return (
    <Box sx={{ maxWidth: 640, mx: 'auto', width: '100%' }}>
      <Paper sx={{ p: { xs: 2.5, md: 3 }, mt: 3 }}>
        <Stack spacing={3}>
          <Box>
            <Typography variant="h4" component="h1">
              Creer une marque
            </Typography>
            <Typography variant="body2" color="text.secondary">
              Ajoutez un Nom de la marque et sa description.
            </Typography>
          </Box>
          <FormProvider {...methods}>
            <form onSubmit={handleSubmit(onSubmit)}>
              <Stack spacing={2.5}>
                <BrandForm disabled={isLoading} />
                <Button type="submit" variant="contained" disabled={isLoading} fullWidth>
                  Creer
                </Button>
              </Stack>
            </form>
          </FormProvider>
          {isSuccess && <Alert severity="success">Marque creee avec succes.</Alert>}
          {error && <Alert severity="error">Erreur lors de la creation de la marque.</Alert>}
        </Stack>
      </Paper>
    </Box>
  );
};
