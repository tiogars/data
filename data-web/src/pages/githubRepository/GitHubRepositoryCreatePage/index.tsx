import { useForm } from 'react-hook-form';
import type { FC } from 'react';
import { useNavigate } from 'react-router-dom';
import GitHubRepositoryForm, { type GitHubRepositoryFormValues } from '../../../components/GitHubRepositoryForm';
import { CrudFormPageShell } from '../../../components/CrudFormPageShell';
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
  const { reset } = methods;

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
    <CrudFormPageShell
      methods={methods}
      maxWidth={760}
      title="Créer un repository GitHub"
      subtitle="Ajoutez un repository à suivre dans l'application."
      submitLabel="Créer"
      onSubmit={onSubmit}
      isSubmitting={isLoading}
      showSuccess={isSuccess}
      successMessage="Repository créé avec succès."
      showError={Boolean(error)}
      errorMessage="Erreur lors de la création du repository."
    >
      <GitHubRepositoryForm disabled={isLoading} />
    </CrudFormPageShell>
  );
};
