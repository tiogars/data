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
import GitHubRepositoryForm, { type GitHubRepositoryFormValues } from '../../../components/GitHubRepositoryForm';
import { useGetGitHubRepositoryByIdQuery, useUpdateGitHubRepositoryMutation } from '../../../services/githubRepositoryApi';
import type { GitHubRepositoryEditPageProps } from './GitHubRepositoryEditPage.types';

const emptyValues: GitHubRepositoryFormValues = {
  owner: '',
  name: '',
  url: '',
  description: '',
  defaultBranch: 'main',
  language: '',
  stars: 0,
  archived: false,
};

export const GitHubRepositoryEditPage: FC<GitHubRepositoryEditPageProps> = ({ id }) => {
  const navigate = useNavigate();
  const { data, isLoading, error } = useGetGitHubRepositoryByIdQuery({ id });
  const [updateGitHubRepository, { isLoading: isSaving, error: saveError, isSuccess }] = useUpdateGitHubRepositoryMutation();
  const methods = useForm<GitHubRepositoryFormValues>({ defaultValues: emptyValues });
  const { handleSubmit, reset } = methods;

  useEffect(() => {
    if (data) {
      reset({
        owner: data.owner ?? '',
        name: data.name ?? '',
        url: data.url ?? '',
        description: data.description ?? '',
        defaultBranch: data.defaultBranch ?? 'main',
        language: data.language ?? '',
        stars: data.stars ?? 0,
        archived: data.archived ?? false,
      });
    }
  }, [data, reset]);

  const onSubmit = async (values: GitHubRepositoryFormValues) => {
    await updateGitHubRepository({
      id,
      gitHubRepository: {
        id,
        ...values,
      },
    }).unwrap();

    navigate(`/github-repository/${id}`);
  };

  if (isLoading) return <div>Chargement...</div>;
  if (error) return <div>Erreur lors du chargement du repository GitHub</div>;
  if (!data) return <div>Repository GitHub introuvable</div>;

  return (
    <Box sx={{ maxWidth: 760, mx: 'auto', width: '100%' }}>
      <Paper sx={{ p: { xs: 2.5, md: 3 }, mt: 3 }}>
        <Stack spacing={3}>
          <Box>
            <Typography variant="h4" component="h1">
              Modifier le repository GitHub
            </Typography>
            <Typography variant="body2" color="text.secondary">
              Mettez à jour les données du repository.
            </Typography>
          </Box>
          <FormProvider {...methods}>
            <form onSubmit={handleSubmit(onSubmit)}>
              <Stack spacing={2.5}>
                <GitHubRepositoryForm disabled={isSaving} />
                <Button type="submit" variant="contained" disabled={isSaving} fullWidth>
                  Enregistrer
                </Button>
              </Stack>
            </form>
          </FormProvider>
          {isSuccess && <Alert severity="success">Repository modifié avec succès.</Alert>}
          {saveError && <Alert severity="error">Erreur lors de la modification du repository.</Alert>}
        </Stack>
      </Paper>
    </Box>
  );
};
