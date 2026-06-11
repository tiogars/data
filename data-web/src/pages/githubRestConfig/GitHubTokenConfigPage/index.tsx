import { useEffect, useMemo, useState, type FC } from 'react';
import Box from '@mui/material/Box';
import Button from '@mui/material/Button';
import Dialog from '@mui/material/Dialog';
import DialogActions from '@mui/material/DialogActions';
import DialogContent from '@mui/material/DialogContent';
import DialogContentText from '@mui/material/DialogContentText';
import DialogTitle from '@mui/material/DialogTitle';
import Stack from '@mui/material/Stack';
import TextField from '@mui/material/TextField';
import Typography from '@mui/material/Typography';
import { type GridPaginationModel } from '@mui/x-data-grid';
import useMediaQuery from '@mui/material/useMediaQuery';
import { useTheme } from '@mui/material/styles';
import {
  useDeleteByIdentifierMutation,
  useCreateMutation,
  useListGitHubRestConfigsQuery,
  useListRequiredPermissionsMutation,
  useUpdateByIdentifierMutation,
  type GitHubRestConfig,
} from '../../../services/githubRestConfigApi';
import type { GitHubTokenConfigPageProps } from './GitHubTokenConfigPage.types';
import { GitHubTokenConfigListSection } from './GitHubTokenConfigListSection';
import { GitHubTokenPermissionsSection } from './GitHubTokenPermissionsSection';

const knownOperations = [
  'repository.read',
  'repository.write',
  'issues.read',
  'issues.write',
  'pull-requests.read',
  'pull-requests.write',
  'actions.read',
  'actions.write',
  'webhooks.read',
  'webhooks.write',
];

const parseOperations = (value: string): string[] => {
  const separators = /[\n,;]+/;

  return value
    .split(separators)
    .map((item) => item.trim())
    .filter(Boolean);
};

export const GitHubTokenConfigPage: FC<GitHubTokenConfigPageProps> = () => {
  const theme = useTheme();
  const isDesktop = useMediaQuery(theme.breakpoints.up('md'));

  const [paginationModel, setPaginationModel] = useState<GridPaginationModel>({ page: 0, pageSize: 10 });
  const [searchInput, setSearchInput] = useState('');
  const [searchQuery, setSearchQuery] = useState('');

  const [createDialogOpen, setCreateDialogOpen] = useState(false);
  const [editDialogOpen, setEditDialogOpen] = useState(false);
  const [configToEdit, setConfigToEdit] = useState<GitHubRestConfig | null>(null);
  const [configToDelete, setConfigToDelete] = useState<GitHubRestConfig | null>(null);

  const [identifier, setIdentifier] = useState('');
  const [token, setToken] = useState('');
  const [comment, setComment] = useState('');

  const [editIdentifier, setEditIdentifier] = useState('');
  const [editToken, setEditToken] = useState('');
  const [editComment, setEditComment] = useState('');

  const [crudFeedback, setCrudFeedback] = useState<{ severity: 'success' | 'error'; message: string } | null>(null);

  const [operationsInput, setOperationsInput] = useState('repository.read\nissues.write');

  const [createGitHubRestConfig, createState] = useCreateMutation();
  const [updateByIdentifier, updateState] = useUpdateByIdentifierMutation();
  const [deleteByIdentifier, deleteState] = useDeleteByIdentifierMutation();
  const [resolvePermissions, permissionState] = useListRequiredPermissionsMutation();

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

  const { data, isLoading, error, refetch } = useListGitHubRestConfigsQuery(queryArgs, {
    refetchOnMountOrArgChange: true,
  });

  const rows = useMemo(
    () => (data?.items ?? []).filter((item): item is GitHubRestConfig => Boolean(item.identifier)),
    [data?.items],
  );

  const totalCount = data?.count ?? 0;

  const requiredPermissions = permissionState.data?.requiredPermissions ?? [];
  const unknownOperations = permissionState.data?.unknownOperations ?? [];

  const helperText = useMemo(
    () => `Formats acceptes: sauts de ligne, virgules ou point-virgules. Exemple: ${knownOperations[0]}, ${knownOperations[1]}`,
    [],
  );

  const resetCreateForm = () => {
    setIdentifier('');
    setToken('');
    setComment('');
  };

  const handleSave = async () => {
    const payloadIdentifier = identifier.trim();
    const payloadToken = token.trim();

    if (!payloadIdentifier || !payloadToken) {
      return;
    }

    const created = await createGitHubRestConfig({
      gitHubRestConfigCreationForm: {
        identifier: payloadIdentifier,
        token: payloadToken,
        comment: comment.trim() || undefined,
      },
    }).unwrap();

    setCrudFeedback({
      severity: 'success',
      message: `Configuration creee: ${created.identifier ?? payloadIdentifier}`,
    });
    setCreateDialogOpen(false);
    resetCreateForm();
    await refetch();
  };

  const openEditDialog = (config: GitHubRestConfig) => {
    setConfigToEdit(config);
    setEditIdentifier(config.identifier ?? '');
    setEditToken('');
    setEditComment(config.comment ?? '');
    setEditDialogOpen(true);
  };

  const closeEditDialog = () => {
    setConfigToEdit(null);
    setEditDialogOpen(false);
    setEditToken('');
  };

  const handleUpdate = async () => {
    const currentIdentifier = configToEdit?.identifier?.trim();
    const nextIdentifier = editIdentifier.trim();

    if (!currentIdentifier || !nextIdentifier) {
      return;
    }

    await updateByIdentifier({
      identifier: currentIdentifier,
      gitHubRestConfigUpdateForm: {
        identifier: nextIdentifier,
        token: editToken.trim() || undefined,
        comment: editComment.trim() || undefined,
      },
    }).unwrap();

    setCrudFeedback({
      severity: 'success',
      message: `Configuration modifiee: ${nextIdentifier}`,
    });
    closeEditDialog();
    await refetch();
  };

  const handleDelete = async () => {
    const identifierToDelete = configToDelete?.identifier?.trim();
    if (!identifierToDelete) {
      return;
    }

    await deleteByIdentifier({ identifier: identifierToDelete }).unwrap();
    setCrudFeedback({
      severity: 'success',
      message: `Configuration supprimee: ${identifierToDelete}`,
    });
    setConfigToDelete(null);
    await refetch();
  };

  const handleResolvePermissions = async () => {
    const operations = parseOperations(operationsInput);
    if (operations.length === 0) {
      return;
    }

    await resolvePermissions({
      gitHubTokenPermissionRequest: { operations },
    }).unwrap();
  };

  const handleToggleOperation = (operation: string) => {
    const current = new Set(parseOperations(operationsInput));
    if (current.has(operation)) {
      current.delete(operation);
    } else {
      current.add(operation);
    }
    setOperationsInput(Array.from(current).join('\n'));
  };

  if (isLoading) return <div>Chargement...</div>;
  if (error) return <div>Erreur lors du chargement des configurations GitHub</div>;

  return (
    <Stack spacing={3} sx={{ p: { xs: 2, md: 3 }, maxWidth: 980, mx: 'auto' }}>
      <Box>
        <Typography variant="h4" component="h1">
          Configurations token GitHub REST
        </Typography>
        <Typography variant="body2" color="text.secondary">
          Gerez les configurations token en mode CRUD et calculez les droits minimaux requis selon les operations appelees.
        </Typography>
      </Box>

      <GitHubTokenConfigListSection
        isDesktop={isDesktop}
        rows={rows}
        totalCount={totalCount}
        isLoading={isLoading}
        searchInput={searchInput}
        onSearchInputChange={setSearchInput}
        paginationModel={paginationModel}
        onPaginationModelChange={setPaginationModel}
        onOpenCreate={() => setCreateDialogOpen(true)}
        onOpenEdit={openEditDialog}
        onAskDelete={setConfigToDelete}
        crudFeedback={crudFeedback}
        createError={createState.isError}
        updateError={updateState.isError}
        deleteError={deleteState.isError}
      />

      <GitHubTokenPermissionsSection
        isDesktop={isDesktop}
        knownOperations={knownOperations}
        operationsInput={operationsInput}
        onOperationsInputChange={setOperationsInput}
        onToggleOperation={handleToggleOperation}
        onResolvePermissions={handleResolvePermissions}
        helperText={helperText}
        isResolving={permissionState.isLoading}
        resolveError={permissionState.isError}
        hasPermissionData={Boolean(permissionState.data)}
        requiredPermissions={requiredPermissions}
        unknownOperations={unknownOperations}
      />

      <Dialog open={createDialogOpen} onClose={() => setCreateDialogOpen(false)} fullWidth maxWidth="sm">
        <DialogTitle>Creer une configuration GitHub token</DialogTitle>
        <DialogContent>
          <Stack spacing={2} sx={{ mt: 1 }}>
            <TextField
              label="Identifiant"
              value={identifier}
              onChange={(event) => setIdentifier(event.target.value)}
              placeholder="integration-ci"
              fullWidth
            />
            <TextField
              label="Token"
              value={token}
              onChange={(event) => setToken(event.target.value)}
              type="password"
              placeholder="github_pat_xxx"
              fullWidth
            />
            <TextField
              label="Commentaire"
              value={comment}
              onChange={(event) => setComment(event.target.value)}
              placeholder="Usage fonctionnel du token"
              multiline
              minRows={2}
              fullWidth
            />
          </Stack>
        </DialogContent>
        <DialogActions>
          <Button onClick={() => setCreateDialogOpen(false)}>Annuler</Button>
          <Button variant="contained" onClick={handleSave} disabled={createState.isLoading}>
            Enregistrer
          </Button>
        </DialogActions>
      </Dialog>

      <Dialog open={editDialogOpen} onClose={closeEditDialog} fullWidth maxWidth="sm">
        <DialogTitle>Modifier une configuration GitHub token</DialogTitle>
        <DialogContent>
          <Stack spacing={2} sx={{ mt: 1 }}>
            <TextField
              label="Identifiant"
              value={editIdentifier}
              onChange={(event) => setEditIdentifier(event.target.value)}
              fullWidth
            />
            <TextField
              label="Nouveau token"
              value={editToken}
              onChange={(event) => setEditToken(event.target.value)}
              type="password"
              placeholder="Laisser vide pour conserver le token actuel"
              helperText="Laissez vide si vous ne souhaitez pas remplacer le token."
              fullWidth
            />
            <TextField
              label="Commentaire"
              value={editComment}
              onChange={(event) => setEditComment(event.target.value)}
              multiline
              minRows={2}
              fullWidth
            />
          </Stack>
        </DialogContent>
        <DialogActions>
          <Button onClick={closeEditDialog}>Annuler</Button>
          <Button variant="contained" onClick={handleUpdate} disabled={updateState.isLoading}>
            Enregistrer
          </Button>
        </DialogActions>
      </Dialog>

      <Dialog open={Boolean(configToDelete)} onClose={() => setConfigToDelete(null)}>
        <DialogTitle>Supprimer la configuration ?</DialogTitle>
        <DialogContent>
          <DialogContentText>
            Cette action supprimera la configuration {configToDelete?.identifier}. Elle est irreversible.
          </DialogContentText>
        </DialogContent>
        <DialogActions>
          <Button onClick={() => setConfigToDelete(null)}>Annuler</Button>
          <Button color="error" variant="contained" onClick={handleDelete} disabled={deleteState.isLoading}>
            Supprimer
          </Button>
        </DialogActions>
      </Dialog>
    </Stack>
  );
};
