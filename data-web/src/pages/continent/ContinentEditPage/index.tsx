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
import ContinentForm, { type ContinentFormValues } from '../../../components/ContinentForm';
import { useGetContinentQuery, useUpdateContinentMutation } from '../../../services/continentApi';
import type { ContinentEditPageProps } from './ContinentEditPage.types';

const emptyValues: ContinentFormValues = {
  code: '',
  name: '',
};

export const ContinentEditPage: FC<ContinentEditPageProps> = ({ id }) => {
  const navigate = useNavigate();
  const { data, isLoading, error } = useGetContinentQuery({ id });
  const [updateContinent, { isLoading: isSaving, error: saveError, isSuccess }] = useUpdateContinentMutation();
  const methods = useForm<ContinentFormValues>({ defaultValues: emptyValues });
  const { handleSubmit, reset } = methods;

  useEffect(() => {
    if (data) {
      reset({
        code: data.code ?? '',
        name: data.name ?? '',
      });
    }
  }, [data, reset]);

  const onSubmit = async (values: ContinentFormValues) => {
    await updateContinent({
      id,
      continentUpdateForm: {
        id,
        ...values,
      },
    }).unwrap();

    navigate(`/continent/${id}`);
  };

  if (isLoading) return <div>Chargement...</div>;
  if (error) return <div>Erreur lors du chargement du continent</div>;
  if (!data) return <div>Continent introuvable</div>;

  return (
    <Box sx={{ maxWidth: 640, mx: 'auto', width: '100%' }}>
      <Paper sx={{ p: { xs: 2.5, md: 3 }, mt: 3 }}>
        <Stack spacing={3}>
          <Box>
            <Typography variant="h4" component="h1">
              Modifier un continent
            </Typography>
            <Typography variant="body2" color="text.secondary">
              Mettez a jour le code et le nom du continent.
            </Typography>
          </Box>
          <FormProvider {...methods}>
            <form onSubmit={handleSubmit(onSubmit)}>
              <Stack spacing={2.5}>
                <ContinentForm disabled={isSaving} />
                <Button type="submit" variant="contained" disabled={isSaving} fullWidth>
                  Enregistrer
                </Button>
              </Stack>
            </form>
          </FormProvider>
          {isSuccess && <Alert severity="success">Continent modifie avec succes.</Alert>}
          {saveError && <Alert severity="error">Erreur lors de la modification du continent.</Alert>}
        </Stack>
      </Paper>
    </Box>
  );
}
