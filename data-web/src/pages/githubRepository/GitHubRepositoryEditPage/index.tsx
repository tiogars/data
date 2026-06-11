import { useEffect } from 'react';
import { useForm } from 'react-hook-form';
import type { FC } from 'react';
import { useNavigate } from 'react-router-dom';
import GitHubRepositoryForm, { type GitHubRepositoryFormValues } from '../../../components/GitHubRepositoryForm';
import { CrudFormPageShell } from '../../../components/CrudFormPageShell';
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
  const { reset } = methods;

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
    <CrudFormPageShell
      methods={methods}
      maxWidth={760}
      title="Modifier le repository GitHub"
      subtitle="Mettez à jour les données du repository."
      submitLabel="Enregistrer"
      onSubmit={onSubmit}
      isSubmitting={isSaving}
      showSuccess={isSuccess}
      successMessage="Repository modifié avec succès."
      showError={Boolean(saveError)}
      errorMessage="Erreur lors de la modification du repository."
    >
      <GitHubRepositoryForm disabled={isSaving} />
    </CrudFormPageShell>
  );
};
