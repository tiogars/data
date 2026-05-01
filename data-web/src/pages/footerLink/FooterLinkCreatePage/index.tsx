import { useForm, FormProvider } from 'react-hook-form';
import type { FC } from 'react';
import { useNavigate } from 'react-router-dom';
import Alert from '@mui/material/Alert';
import Box from '@mui/material/Box';
import Button from '@mui/material/Button';
import Paper from '@mui/material/Paper';
import Stack from '@mui/material/Stack';
import Typography from '@mui/material/Typography';
import FooterLinkForm, { type FooterLinkFormValues } from '../../../components/FooterLinkForm';
import { useCreateFooterLinkMutation } from '../../../services/footerLinkApi';
import type { FooterLinkCreatePageProps } from './FooterLinkCreatePage.types';

const defaultValues: FooterLinkFormValues = {
  label: '',
  url: '',
  icon: 'react',
  displayOrder: 10,
};

export const FooterLinkCreatePage: FC<FooterLinkCreatePageProps> = ({ onCreated }) => {
  const navigate = useNavigate();
  const [createFooterLink, { isLoading, error, isSuccess }] = useCreateFooterLinkMutation();
  const methods = useForm<FooterLinkFormValues>({ defaultValues });
  const { handleSubmit, reset } = methods;

  const onSubmit = async (values: FooterLinkFormValues) => {
    const createdFooterLink = await createFooterLink({
      footerLinkCreationForm: values,
    }).unwrap();

    reset(defaultValues);

    if (createdFooterLink.id) {
      await onCreated?.(createdFooterLink.id);
      navigate(`/footer-link/${createdFooterLink.id}`);
    }
  };

  return (
    <Box sx={{ maxWidth: 640, mx: 'auto', width: '100%' }}>
      <Paper sx={{ p: { xs: 2.5, md: 3 }, mt: 3 }}>
        <Stack spacing={3}>
          <Box>
            <Typography variant="h4" component="h1">
              Créer un lien de footer
            </Typography>
            <Typography variant="body2" color="text.secondary">
              Ajoutez un nouvel élément qui sera chargé automatiquement dans le footer de l'application.
            </Typography>
          </Box>
          <FormProvider {...methods}>
            <form onSubmit={handleSubmit(onSubmit)}>
              <Stack spacing={2.5}>
                <FooterLinkForm disabled={isLoading} />
                <Button type="submit" variant="contained" disabled={isLoading} fullWidth>
                  Créer
                </Button>
              </Stack>
            </form>
          </FormProvider>
          {isSuccess && <Alert severity="success">Lien créé avec succès.</Alert>}
          {error && <Alert severity="error">Erreur lors de la création du lien.</Alert>}
        </Stack>
      </Paper>
    </Box>
  );
};