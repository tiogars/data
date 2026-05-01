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
import FooterLinkForm, { type FooterLinkFormValues } from '../../../components/FooterLinkForm';
import { useGetFooterLinkByIdQuery, useUpdateFooterLinkMutation } from '../../../services/footerLinkApi';
import type { FooterLinkEditPageProps } from './FooterLinkEditPage.types';

const emptyValues: FooterLinkFormValues = {
  label: '',
  url: '',
  icon: 'react',
  displayOrder: 10,
};

export const FooterLinkEditPage: FC<FooterLinkEditPageProps> = ({ id }) => {
  const navigate = useNavigate();
  const { data, isLoading, error } = useGetFooterLinkByIdQuery({ id });
  const [updateFooterLink, { isLoading: isSaving, error: saveError, isSuccess }] = useUpdateFooterLinkMutation();
  const methods = useForm<FooterLinkFormValues>({ defaultValues: emptyValues });
  const { handleSubmit, reset } = methods;

  useEffect(() => {
    if (data) {
      reset({
        label: data.label ?? '',
        url: data.url ?? '',
        icon: data.icon ?? 'react',
        displayOrder: data.displayOrder ?? 0,
      });
    }
  }, [data, reset]);

  const onSubmit = async (values: FooterLinkFormValues) => {
    await updateFooterLink({
      id,
      footerLink: {
        id,
        ...values,
      },
    }).unwrap();

    navigate(`/footer-link/${id}`);
  };

  if (isLoading) return <div>Chargement...</div>;
  if (error) return <div>Erreur lors du chargement du lien footer</div>;
  if (!data) return <div>Lien footer introuvable</div>;

  return (
    <Box sx={{ maxWidth: 640, mx: 'auto', width: '100%' }}>
      <Paper sx={{ p: { xs: 2.5, md: 3 }, mt: 3 }}>
        <Stack spacing={3}>
          <Box>
            <Typography variant="h4" component="h1">
              Modifier le lien de footer
            </Typography>
            <Typography variant="body2" color="text.secondary">
              Mettez à jour les informations exposées dans le footer applicatif.
            </Typography>
          </Box>
          <FormProvider {...methods}>
            <form onSubmit={handleSubmit(onSubmit)}>
              <Stack spacing={2.5}>
                <FooterLinkForm disabled={isSaving} />
                <Button type="submit" variant="contained" disabled={isSaving} fullWidth>
                  Enregistrer
                </Button>
              </Stack>
            </form>
          </FormProvider>
          {isSuccess && <Alert severity="success">Lien modifié avec succès.</Alert>}
          {saveError && <Alert severity="error">Erreur lors de la modification du lien.</Alert>}
        </Stack>
      </Paper>
    </Box>
  );
};