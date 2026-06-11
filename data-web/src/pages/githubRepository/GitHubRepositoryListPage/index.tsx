import { useEffect, useMemo, useState, type FC } from 'react';
import { Link as RouterLink } from 'react-router-dom';
import Alert from '@mui/material/Alert';
import Box from '@mui/material/Box';
import Button from '@mui/material/Button';
import Card from '@mui/material/Card';
import CardActions from '@mui/material/CardActions';
import CardContent from '@mui/material/CardContent';
import Chip, { type ChipProps } from '@mui/material/Chip';
import Dialog from '@mui/material/Dialog';
import DialogActions from '@mui/material/DialogActions';
import DialogContent from '@mui/material/DialogContent';
import DialogContentText from '@mui/material/DialogContentText';
import DialogTitle from '@mui/material/DialogTitle';
import IconButton from '@mui/material/IconButton';
import Link from '@mui/material/Link';
import Paper from '@mui/material/Paper';
import Stack from '@mui/material/Stack';
import TablePagination from '@mui/material/TablePagination';
import TextField from '@mui/material/TextField';
import Typography from '@mui/material/Typography';
import useMediaQuery from '@mui/material/useMediaQuery';
import { useTheme } from '@mui/material/styles';
import DeleteIcon from '@mui/icons-material/Delete';
import EditOutlinedIcon from '@mui/icons-material/EditOutlined';
import SyncIcon from '@mui/icons-material/Sync';
import VisibilityOutlinedIcon from '@mui/icons-material/VisibilityOutlined';
import { DataGrid, type GridColDef, type GridPaginationModel } from '@mui/x-data-grid';
import {
  type GitHubRepository,
  useDeleteGitHubRepositoryByIdMutation,
  useSearchGitHubRepositoriesQuery,
} from '../../../services/githubRepositoryApi';
import { githubRestConfigApi } from '../../../services/githubRestConfigApi';
import { useSyncGitHubRepositoryMutation, useSyncAllGitHubRepositoriesMutation } from '../../../services/githubRepositorySyncApi';
import type { GitHubRepositoryListPageProps } from './GitHubRepositoryListPage.types';

type GitHubRepositoryRow = GitHubRepository & { id: string; existsOnGitHub?: boolean };

type SyncFeedback = {
  severity: 'success' | 'warning' | 'error';
  message: string;
};

function toRows(items: GitHubRepository[] | undefined): GitHubRepositoryRow[] {
  return (items ?? []).filter((item): item is GitHubRepositoryRow => Boolean(item.id));
}

const resolveRepositoryStatusLabel = (repository: GitHubRepositoryRow): string => {
  if (repository.existsOnGitHub === false) {
    return 'N\'existe plus';
  }

  return repository.archived ? 'Archive' : 'Actif';
};

const resolveRepositoryStatusColor = (repository: GitHubRepositoryRow): ChipProps['color'] => {
  if (repository.existsOnGitHub === false) {
    return 'error';
  }

  if (repository.archived) {
    return 'warning';
  }

  return 'success';
};

export const GitHubRepositoryListPage: FC<GitHubRepositoryListPageProps> = () => {
  const theme = useTheme();
  const isDesktop = useMediaQuery(theme.breakpoints.up('md'));
  const [paginationModel, setPaginationModel] = useState<GridPaginationModel>({ page: 0, pageSize: 10 });
  const [searchInput, setSearchInput] = useState('');
  const [searchQuery, setSearchQuery] = useState('');
  const [repoToDelete, setRepoToDelete] = useState<GitHubRepositoryRow | null>(null);
  const [repoToSync, setRepoToSync] = useState<GitHubRepositoryRow | null>(null);
  const [syncConfigIdentifier, setSyncConfigIdentifier] = useState('');
  const [confirmedSyncConfigIdentifier, setConfirmedSyncConfigIdentifier] = useState<string | null>(null);
  const [syncFeedback, setSyncFeedback] = useState<SyncFeedback | null>(null);
  const [bulkSyncDialogOpen, setBulkSyncDialogOpen] = useState(false);
  const [bulkSyncConfigIdentifier, setBulkSyncConfigIdentifier] = useState('');
  const [confirmedBulkSyncConfigIdentifier, setConfirmedBulkSyncConfigIdentifier] = useState<string | null>(null);
  const [deleteGitHubRepositoryById, { isLoading: isDeleting }] = useDeleteGitHubRepositoryByIdMutation();
  const [syncGitHubRepository, { isLoading: isSyncing }] = useSyncGitHubRepositoryMutation();
  const [syncAllGitHubRepositories, { isLoading: isBulkSyncing }] = useSyncAllGitHubRepositoriesMutation();
  const [loadGitHubConfig, syncConfigLookupState] = githubRestConfigApi.useLazyGetByIdentifierQuery();
  const [loadBulkSyncConfig, bulkSyncConfigLookupState] = githubRestConfigApi.useLazyGetByIdentifierQuery();

  useEffect(() => {
    const timeoutId = globalThis.setTimeout(() => {
      setSearchQuery(searchInput.trim());
      setPaginationModel((current) => ({ ...current, page: 0 }));
    }, 350);

    return () => globalThis.clearTimeout(timeoutId);
  }, [searchInput]);

  const queryArgs = useMemo(
    () => ({
      page: paginationModel.page,
      size: paginationModel.pageSize,
      q: searchQuery || undefined,
    }),
    [paginationModel.page, paginationModel.pageSize, searchQuery],
  );

  const { data, isLoading, isFetching, error, refetch } = useSearchGitHubRepositoriesQuery(queryArgs, {
    refetchOnMountOrArgChange: true,
  });

  const rows = useMemo(() => toRows(data?.items), [data?.items]);
  const totalCount = data?.count ?? 0;

  const normalizedSyncConfigIdentifier = syncConfigIdentifier.trim();
  const isSyncConfigConfirmed =
    confirmedSyncConfigIdentifier?.toLowerCase() === normalizedSyncConfigIdentifier.toLowerCase() &&
    normalizedSyncConfigIdentifier.length > 0;

  const openSyncDialog = (repository: GitHubRepositoryRow) => {
    setRepoToSync(repository);
    setConfirmedSyncConfigIdentifier(null);
  };

  const closeSyncDialog = () => {
    setRepoToSync(null);
    setConfirmedSyncConfigIdentifier(null);
  };

  const handleConfirmSyncConfig = async () => {
    const identifier = normalizedSyncConfigIdentifier;

    if (!identifier) {
      return;
    }

    const config = await loadGitHubConfig({ identifier }).unwrap();
    setConfirmedSyncConfigIdentifier(config.identifier ?? identifier);
  };

  const handleSyncRepository = async () => {
    if (!repoToSync || !isSyncConfigConfirmed) {
      return;
    }

    const owner = repoToSync.owner?.trim();
    const name = repoToSync.name?.trim();
    if (!owner || !name) {
      setSyncFeedback({
        severity: 'error',
        message: 'Synchronisation impossible: owner ou nom manquant sur le repository local.',
      });
      return;
    }

    const result = await syncGitHubRepository({
      gitHubRepositorySyncForm: {
        owner,
        name,
        gitHubRestConfigIdentifier: normalizedSyncConfigIdentifier,
      },
    }).unwrap();

    const statusMessageByType = {
      CREATED: 'Repository cree depuis GitHub.',
      UPDATED: 'Repository mis a jour depuis GitHub.',
      MARKED_AS_MISSING: 'Repository marque comme inexistant sur GitHub.',
    } as const;

    const statusMessage = result.status ? statusMessageByType[result.status] : 'Synchronisation terminee.';

    setSyncFeedback({
      severity: result.status === 'MARKED_AS_MISSING' ? 'warning' : 'success',
      message: `${statusMessage} Configuration utilisee: ${result.usedConfigIdentifier ?? normalizedSyncConfigIdentifier}.`,
    });

    closeSyncDialog();
    await refetch();
  };

  const normalizedBulkSyncConfigIdentifier = bulkSyncConfigIdentifier.trim();
  const isBulkSyncConfigConfirmed =
    confirmedBulkSyncConfigIdentifier?.toLowerCase() === normalizedBulkSyncConfigIdentifier.toLowerCase() &&
    normalizedBulkSyncConfigIdentifier.length > 0;

  const openBulkSyncDialog = () => {
    setBulkSyncDialogOpen(true);
    setConfirmedBulkSyncConfigIdentifier(null);
  };

  const closeBulkSyncDialog = () => {
    setBulkSyncDialogOpen(false);
    setConfirmedBulkSyncConfigIdentifier(null);
  };

  const handleConfirmBulkSyncConfig = async () => {
    const identifier = normalizedBulkSyncConfigIdentifier;
    if (!identifier) return;
    const config = await loadBulkSyncConfig({ identifier }).unwrap();
    setConfirmedBulkSyncConfigIdentifier(config.identifier ?? identifier);
  };

  const handleSyncAll = async () => {
    if (!isBulkSyncConfigConfirmed) return;

    const result = await syncAllGitHubRepositories({
      gitHubRepositoryBulkSyncForm: { gitHubRestConfigIdentifier: normalizedBulkSyncConfigIdentifier },
    }).unwrap();

    setSyncFeedback({
      severity: 'success',
      message: `Synchronisation terminee: ${result.created ?? 0} cree(s), ${result.updated ?? 0} mis a jour, ${result.markedAsMissing ?? 0} marque(s) inexistant(s). Configuration: ${result.usedConfigIdentifier}.`,
    });

    closeBulkSyncDialog();
    await refetch();
  };

  const handleDelete = async () => {
    if (!repoToDelete) return;

    await deleteGitHubRepositoryById({ id: repoToDelete.id }).unwrap();
    setRepoToDelete(null);
    await refetch();
  };

  const columns = useMemo<GridColDef<GitHubRepositoryRow>[]>(() => [
    {
      field: 'fullName',
      headerName: 'Repository',
      flex: 1.2,
      minWidth: 220,
      renderCell: (params) => (
        <Box sx={{ minWidth: 0 }}>
          <Typography variant="body2" sx={{ fontWeight: 600 }} noWrap>
            {params.row.fullName || `${params.row.owner}/${params.row.name}`}
          </Typography>
          <Typography variant="caption" color="text.secondary" noWrap>
            {params.row.defaultBranch ? `Branche: ${params.row.defaultBranch}` : 'Branche: main'}
          </Typography>
        </Box>
      ),
    },
    {
      field: 'description',
      headerName: 'Description',
      flex: 1.2,
      minWidth: 250,
      sortable: false,
      renderCell: (params) => (
        <Typography variant="body2" color="text.secondary" noWrap>
          {params.row.description || 'Aucune description'}
        </Typography>
      ),
    },
    {
      field: 'language',
      headerName: 'Langage',
      minWidth: 120,
      flex: 0.5,
      renderCell: (params) => params.row.language || '-',
    },
    {
      field: 'stars',
      headerName: 'Etoiles',
      minWidth: 110,
      type: 'number',
      flex: 0.4,
      renderCell: (params) => params.row.stars ?? 0,
    },
    {
      field: 'archived',
      headerName: 'Statut',
      minWidth: 120,
      flex: 0.5,
      sortable: false,
      renderCell: (params) => resolveRepositoryStatusLabel(params.row),
    },
    {
      field: 'actions',
      headerName: 'Actions',
      sortable: false,
      filterable: false,
      align: 'right',
      headerAlign: 'right',
      minWidth: 180,
      renderCell: (params) => (
        <>
          <IconButton component={RouterLink} to={`/github-repository/${params.row.id}`} aria-label="Voir le repository">
            <VisibilityOutlinedIcon fontSize="small" />
          </IconButton>
          <IconButton component={RouterLink} to={`/github-repository/${params.row.id}/edit`} aria-label="Modifier le repository">
            <EditOutlinedIcon fontSize="small" />
          </IconButton>
          <IconButton
            aria-label="Synchroniser depuis GitHub"
            color="primary"
            onClick={() => openSyncDialog(params.row)}
          >
            <SyncIcon fontSize="small" />
          </IconButton>
          <IconButton aria-label="Supprimer le repository" color="error" onClick={() => setRepoToDelete(params.row)}>
            <DeleteIcon fontSize="small" />
          </IconButton>
        </>
      ),
    },
  ], []);

  if (isLoading) return <div>Chargement...</div>;
  if (error) return <div>Erreur lors du chargement des repositories GitHub</div>;

  return (
    <Stack spacing={3} sx={{ p: { xs: 2, md: 3 } }}>
      <Stack direction={{ xs: 'column', md: 'row' }} spacing={1.5} useFlexGap sx={{ alignItems: { md: 'center' }, justifyContent: 'space-between' }}>
        <Box>
          <Typography variant="h4" component="h1">
            Repositories GitHub
          </Typography>
          <Typography variant="body2" color="text.secondary">
            Catalogue des repositories avec recherche et pagination serveur.
          </Typography>
        </Box>
        <Stack direction="row" spacing={1}>
          <Chip label={`${totalCount} element${totalCount > 1 ? 's' : ''}`} color="primary" variant="outlined" />
          <Button
            startIcon={<SyncIcon />}
            variant="outlined"
            onClick={openBulkSyncDialog}
            disabled={isBulkSyncing}
          >
            Tout synchroniser
          </Button>
          <Button component={RouterLink} to="/github-repository/create" variant="contained">
            Nouveau repository
          </Button>
        </Stack>
      </Stack>

      <TextField
        label="Rechercher"
        placeholder="owner, nom, langage, description..."
        value={searchInput}
        onChange={(event) => setSearchInput(event.target.value)}
        fullWidth
      />

      {syncFeedback && (
        <Alert severity={syncFeedback.severity}>{syncFeedback.message}</Alert>
      )}

      {rows.length === 0 && (
        <Alert severity="info">
          Aucun repository ne correspond a votre recherche.
        </Alert>
      )}

      {isDesktop ? (
        <Paper variant="outlined" sx={{ minHeight: 500 }}>
          <DataGrid
            rows={rows}
            columns={columns}
            paginationMode="server"
            rowCount={totalCount}
            paginationModel={paginationModel}
            onPaginationModelChange={setPaginationModel}
            pageSizeOptions={[10, 20, 50]}
            disableRowSelectionOnClick
            loading={isFetching}
            sx={{ border: 0 }}
          />
        </Paper>
      ) : (
        <>
          <Stack spacing={2}>
            {rows.map((repo) => (
              <Card key={repo.id} variant="outlined">
                <CardContent>
                  <Stack spacing={1.2}>
                    <Typography variant="h6">{repo.fullName || `${repo.owner}/${repo.name}`}</Typography>
                    <Typography variant="body2" color="text.secondary">
                      {repo.description || 'Aucune description'}
                    </Typography>
                    <Stack direction="row" spacing={1} sx={{ flexWrap: 'wrap' }}>
                      <Chip size="small" label={repo.language || 'N/A'} variant="outlined" />
                      <Chip size="small" label={`${repo.stars ?? 0} etoiles`} />
                      <Chip
                        size="small"
                        color={resolveRepositoryStatusColor(repo)}
                        label={resolveRepositoryStatusLabel(repo)}
                      />
                    </Stack>
                    <Link href={repo.url} target="_blank" rel="noopener">
                      {repo.url}
                    </Link>
                  </Stack>
                </CardContent>
                <CardActions sx={{ px: 2, pb: 2, pt: 0, gap: 1, flexWrap: 'wrap' }}>
                  <Button component={RouterLink} to={`/github-repository/${repo.id}`} size="small" variant="outlined">
                    Voir
                  </Button>
                  <Button component={RouterLink} to={`/github-repository/${repo.id}/edit`} size="small" variant="outlined">
                    Modifier
                  </Button>
                  <Button size="small" variant="outlined" onClick={() => openSyncDialog(repo)}>
                    Synchroniser
                  </Button>
                  <Button size="small" color="error" variant="outlined" onClick={() => setRepoToDelete(repo)}>
                    Supprimer
                  </Button>
                </CardActions>
              </Card>
            ))}
          </Stack>
          <Paper variant="outlined" sx={{ mt: 1 }}>
            <TablePagination
              component="div"
              count={totalCount}
              page={paginationModel.page}
              onPageChange={(_event, nextPage) => setPaginationModel((current) => ({ ...current, page: nextPage }))}
              rowsPerPage={paginationModel.pageSize}
              onRowsPerPageChange={(event) => {
                const nextSize = Number(event.target.value);
                setPaginationModel({ page: 0, pageSize: nextSize });
              }}
              rowsPerPageOptions={[10, 20, 50]}
            />
          </Paper>
        </>
      )}

      <Dialog open={Boolean(repoToDelete)} onClose={() => setRepoToDelete(null)}>
        <DialogTitle>Supprimer le repository</DialogTitle>
        <DialogContent>
          <DialogContentText>
            Voulez-vous vraiment supprimer {repoToDelete?.fullName || 'ce repository'} ?
          </DialogContentText>
        </DialogContent>
        <DialogActions>
          <Button onClick={() => setRepoToDelete(null)} disabled={isDeleting}>Annuler</Button>
          <Button color="error" onClick={handleDelete} disabled={isDeleting}>Supprimer</Button>
        </DialogActions>
      </Dialog>

      <Dialog open={Boolean(repoToSync)} onClose={closeSyncDialog} fullWidth maxWidth="sm">
        <DialogTitle>Synchroniser un repository GitHub</DialogTitle>
        <DialogContent>
          <Stack spacing={2} sx={{ pt: 1 }}>
            <DialogContentText>
              Repository cible: {repoToSync?.fullName || `${repoToSync?.owner}/${repoToSync?.name}`}
            </DialogContentText>
            <TextField
              label="Configuration GitHub REST"
              value={syncConfigIdentifier}
              onChange={(event) => {
                setSyncConfigIdentifier(event.target.value);
                setConfirmedSyncConfigIdentifier(null);
              }}
              placeholder="integration-ci"
              fullWidth
            />
            <Stack direction={{ xs: 'column', sm: 'row' }} spacing={1}>
              <Button
                variant="outlined"
                onClick={handleConfirmSyncConfig}
                disabled={syncConfigLookupState.isFetching || normalizedSyncConfigIdentifier.length === 0}
              >
                Confirmer la configuration
              </Button>
            </Stack>

            {syncConfigLookupState.isError && (
              <Alert severity="error">Configuration GitHub REST introuvable pour cet identifiant.</Alert>
            )}

            {isSyncConfigConfirmed && syncConfigLookupState.data && (
              <Alert severity="success">
                Configuration confirmee: {syncConfigLookupState.data.identifier} (token: {syncConfigLookupState.data.tokenPreview})
              </Alert>
            )}
          </Stack>
        </DialogContent>
        <DialogActions>
          <Button onClick={closeSyncDialog} disabled={isSyncing}>Annuler</Button>
          <Button onClick={handleSyncRepository} disabled={!isSyncConfigConfirmed || isSyncing} variant="contained">
            Synchroniser
          </Button>
        </DialogActions>
      </Dialog>
      <Dialog open={bulkSyncDialogOpen} onClose={closeBulkSyncDialog} fullWidth maxWidth="sm">
        <DialogTitle>Synchroniser tous les repositories GitHub</DialogTitle>
        <DialogContent>
          <Stack spacing={2} sx={{ pt: 1 }}>
            <DialogContentText>
              Récupère tous les repositories de l'utilisateur associé au token de la configuration et les synchronise localement.
            </DialogContentText>
            <TextField
              label="Configuration GitHub REST"
              value={bulkSyncConfigIdentifier}
              onChange={(event) => {
                setBulkSyncConfigIdentifier(event.target.value);
                setConfirmedBulkSyncConfigIdentifier(null);
              }}
              placeholder="integration-ci"
              fullWidth
            />
            <Stack direction={{ xs: 'column', sm: 'row' }} spacing={1}>
              <Button
                variant="outlined"
                onClick={handleConfirmBulkSyncConfig}
                disabled={bulkSyncConfigLookupState.isFetching || normalizedBulkSyncConfigIdentifier.length === 0}
              >
                Confirmer la configuration
              </Button>
            </Stack>

            {bulkSyncConfigLookupState.isError && (
              <Alert severity="error">Configuration GitHub REST introuvable pour cet identifiant.</Alert>
            )}

            {isBulkSyncConfigConfirmed && bulkSyncConfigLookupState.data && (
              <Alert severity="success">
                Configuration confirmée: {bulkSyncConfigLookupState.data.identifier} (token: {bulkSyncConfigLookupState.data.tokenPreview})
              </Alert>
            )}
          </Stack>
        </DialogContent>
        <DialogActions>
          <Button onClick={closeBulkSyncDialog} disabled={isBulkSyncing}>Annuler</Button>
          <Button onClick={handleSyncAll} disabled={!isBulkSyncConfigConfirmed || isBulkSyncing} variant="contained">
            Synchroniser tout
          </Button>
        </DialogActions>
      </Dialog>
    </Stack>
  );
};
