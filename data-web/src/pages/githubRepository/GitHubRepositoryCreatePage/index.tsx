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
import { useCreateGitHubRepositoryMutation } from '../../../services/githubRepositoryApi';
import type { GitHubRepositoryCreatePageProps } from './GitHubRepositoryCreatePage.types';

const defaultValues: GitHubRepositoryFormValues = {
  owner: '',
  name: '',
  url: '',
  description: '',
  defaultBranch: 'main',
  language: '',
  stars: 0,
  archived: false,
};

export const GitHubRepositoryCreatePage: FC<GitHubRepositoryCreatePageProps> = ({ onCreated }) => {
  const navigate = useNavigate();
  const [createGitHubRepository, { isLoading, error, isSuccess }] = useCreateGitHubRepositoryMutation();
  const methods = useForm<GitHubRepositoryFormValues>({ defaultValues });
  const { handleSubmit, reset } = methods;

  const onSubmit = async (values: GitHubRepositoryFormValues) => {
    const created = await createGitHubRepository({
      gitHubRepositoryCreationForm: values,
    }).unwrap();

    reset(defaultValues);

    if (created.id) {
      await onCreated?.(created.id);
      navigate(`/github-repository/${created.id}`);
    }
  };

  return (
    <Box sx={{ maxWidth: 760, mx: 'auto', width: '100%' }}>
      <Paper sx={{ p: { xs: 2.5, md: 3 }, mt: 3 }}>
        <Stack spacing={3}>
          <Box>
            <Typography variant="h4" component="h1">
              Créer un repository GitHub
            </Typography>
            <Typography variant="body2" color="text.secondary">
              Ajoutez un repository à suivre dans l'application.
            </Typography>
          </Box>
          <FormProvider {...methods}>
            <form onSubmit={handleSubmit(onSubmit)}>
              <Stack spacing={2.5}>
                <GitHubRepositoryForm disabled={isLoading} />
                <Button type="submit" variant="contained" disabled={isLoading} fullWidth>
                  Créer
                </Button>
              </Stack>
            </form>
          </FormProvider>
          {isSuccess && <Alert severity="success">Repository créé avec succès.</Alert>}
          {error && <Alert severity="error">Erreur lors de la création du repository.</Alert>}
        </Stack>
      </Paper>
    </Box>
  );
};
