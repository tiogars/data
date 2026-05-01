import type { FC } from 'react';
import { Link as RouterLink } from 'react-router-dom';
import Box from '@mui/material/Box';
import Button from '@mui/material/Button';
import Chip from '@mui/material/Chip';
import Divider from '@mui/material/Divider';
import Link from '@mui/material/Link';
import Paper from '@mui/material/Paper';
import Stack from '@mui/material/Stack';
import Typography from '@mui/material/Typography';
import { useGetGitHubRepositoryByIdQuery } from '../../../services/githubRepositoryApi';
import type { GitHubRepositoryDetailPageProps } from './GitHubRepositoryDetailPage.types';

export const GitHubRepositoryDetailPage: FC<GitHubRepositoryDetailPageProps> = ({ id }) => {
  const { data, isLoading, error } = useGetGitHubRepositoryByIdQuery({ id });

  if (isLoading) return <div>Chargement...</div>;
  if (error) return <div>Erreur lors du chargement du repository GitHub</div>;
  if (!data) return <div>Repository GitHub introuvable</div>;

  return (
    <Paper variant="outlined" sx={{ p: { xs: 2.5, md: 3 }, maxWidth: 860, mx: 'auto', mt: 3 }}>
      <Stack spacing={2}>
        <Stack direction={{ xs: 'column', sm: 'row' }} spacing={1.5} sx={{ justifyContent: 'space-between', alignItems: { sm: 'center' } }}>
          <Box>
            <Typography variant="h4" component="h1">
              {data.fullName || 'Repository GitHub'}
            </Typography>
            <Stack direction="row" spacing={1} sx={{ mt: 1, flexWrap: 'wrap' }}>
              <Chip label={`${data.stars ?? 0} étoiles`} size="small" />
              {data.language && <Chip label={data.language} size="small" variant="outlined" />}
              {data.archived && <Chip label="Archivé" color="warning" size="small" />}
            </Stack>
          </Box>
          <Button component={RouterLink} to={`/github-repository/${id}/edit`} variant="contained">
            Modifier
          </Button>
        </Stack>
        <Divider />
        <Box>
          <Typography variant="overline" color="text.secondary">Description</Typography>
          <Typography>{data.description || 'Aucune description'}</Typography>
        </Box>
        <Box>
          <Typography variant="overline" color="text.secondary">URL</Typography>
          <Typography>
            <Link href={data.url} target="_blank" rel="noopener">
              {data.url}
            </Link>
          </Typography>
        </Box>
        <Box>
          <Typography variant="overline" color="text.secondary">Owner</Typography>
          <Typography>{data.owner}</Typography>
        </Box>
        <Box>
          <Typography variant="overline" color="text.secondary">Nom</Typography>
          <Typography>{data.name}</Typography>
        </Box>
        <Box>
          <Typography variant="overline" color="text.secondary">Branche par défaut</Typography>
          <Typography>{data.defaultBranch || 'main'}</Typography>
        </Box>
        <Box>
          <Typography variant="overline" color="text.secondary">Identifiant</Typography>
          <Typography color="text.secondary">{data.id}</Typography>
        </Box>
      </Stack>
    </Paper>
  );
};
