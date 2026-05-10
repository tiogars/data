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
import { useCreateContinentMutation } from '../../../services/continentApi';
import type { ContinentCreatePageProps } from './ContinentCreatePage.types';

const defaultValues: ContinentFormValues = {
  code: '',
  name: '',
};

export const ContinentCreatePage: FC<ContinentCreatePageProps> = ({ onCreated }) => {
  const navigate = useNavigate();
  const [createContinent, { isLoading, error, isSuccess }] = useCreateContinentMutation();
  const methods = useForm<ContinentFormValues>({ defaultValues });
  const { handleSubmit, reset } = methods;

  const onSubmit = async (values: ContinentFormValues) => {
    const createdContinent = await createContinent({
      continentCreationForm: values,
    }).unwrap();

    reset(defaultValues);

    if (createdContinent.id) {
      await onCreated?.(createdContinent.id);
      navigate(`/continent/${createdContinent.id}`);
    }
  };

  return (
    <Box sx={{ maxWidth: 640, mx: 'auto', width: '100%' }}>
      <Paper sx={{ p: { xs: 2.5, md: 3 }, mt: 3 }}>
        <Stack spacing={3}>
          <Box>
            <Typography variant="h4" component="h1">
              Creer un continent
            </Typography>
            <Typography variant="body2" color="text.secondary">
              Ajoutez le code et le nom du continent.
            </Typography>
          </Box>
          <FormProvider {...methods}>
            <form onSubmit={handleSubmit(onSubmit)}>
              <Stack spacing={2.5}>
                <ContinentForm disabled={isLoading} />
                <Button type="submit" variant="contained" disabled={isLoading} fullWidth>
                  Creer
                </Button>
              </Stack>
            </form>
          </FormProvider>
          {isSuccess && <Alert severity="success">Continent cree avec succes.</Alert>}
          {error && <Alert severity="error">Erreur lors de la creation du continent.</Alert>}
        </Stack>
      </Paper>
    </Box>
  );
};
