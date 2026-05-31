import { useEffect, useMemo, useState, type FC } from 'react';
import Alert from '@mui/material/Alert';
import Box from '@mui/material/Box';
import Button from '@mui/material/Button';
import Card from '@mui/material/Card';
import CardActions from '@mui/material/CardActions';
import CardContent from '@mui/material/CardContent';
import Chip from '@mui/material/Chip';
import Divider from '@mui/material/Divider';
import Dialog from '@mui/material/Dialog';
import DialogActions from '@mui/material/DialogActions';
import DialogContent from '@mui/material/DialogContent';
import DialogContentText from '@mui/material/DialogContentText';
import DialogTitle from '@mui/material/DialogTitle';
import IconButton from '@mui/material/IconButton';
import Paper from '@mui/material/Paper';
import Stack from '@mui/material/Stack';
import Table from '@mui/material/Table';
import TableBody from '@mui/material/TableBody';
import TableCell from '@mui/material/TableCell';
import TableContainer from '@mui/material/TableContainer';
import TableHead from '@mui/material/TableHead';
import TablePagination from '@mui/material/TablePagination';
import TableRow from '@mui/material/TableRow';
import TextField from '@mui/material/TextField';
import Typography from '@mui/material/Typography';
import DeleteIcon from '@mui/icons-material/Delete';
import EditOutlinedIcon from '@mui/icons-material/EditOutlined';
import AddIcon from '@mui/icons-material/Add';
import { DataGrid, type GridColDef, type GridPaginationModel } from '@mui/x-data-grid';
import useMediaQuery from '@mui/material/useMediaQuery';
import { useTheme } from '@mui/material/styles';
import {
  useDeleteByIdentifierMutation,
  useCreateMutation,
  useListGitHubRestConfigsQuery,
  useListRequiredPermissionsMutation,
  useUpdateByIdentifierMutation,
  type GitHubRestConfig,
  type GitHubTokenPermission,
} from '../../../services/githubRestConfigApi';
import type { GitHubTokenConfigPageProps } from './GitHubTokenConfigPage.types';

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

  const renderPermissionListMobile = (items: GitHubTokenPermission[]) => (
    <Stack spacing={1.2}>
      {items.map((permission, index) => (
        <Paper key={`${permission.permission}-${permission.access}-${index}`} variant="outlined" sx={{ p: 1.5 }}>
          <Stack spacing={0.7}>
            <Typography variant="subtitle2">{permission.permission}</Typography>
            <Chip
              size="small"
              label={(permission.access ?? 'read').toUpperCase()}
              color={permission.access === 'write' ? 'warning' : 'default'}
              sx={{ width: 'fit-content' }}
            />
            <Typography variant="body2" color="text.secondary">
              {permission.reason}
            </Typography>
          </Stack>
        </Paper>
      ))}
    </Stack>
  );

  const columns = useMemo<GridColDef<GitHubRestConfig>[]>(() => [
    {
      field: 'identifier',
      headerName: 'Identifiant',
      flex: 0.8,
      minWidth: 200,
      renderCell: (params) => (
        <Typography variant="body2" sx={{ fontWeight: 600 }} noWrap>
          {params.row.identifier}
        </Typography>
      ),
    },
    {
      field: 'tokenPreview',
      headerName: 'Token',
      flex: 0.7,
      minWidth: 180,
      renderCell: (params) => params.row.tokenPreview || '****',
    },
    {
      field: 'comment',
      headerName: 'Commentaire',
      flex: 1.3,
      minWidth: 250,
      sortable: false,
      renderCell: (params) => (
        <Typography variant="body2" color="text.secondary" noWrap>
          {params.row.comment || 'Aucun commentaire'}
        </Typography>
      ),
    },
    {
      field: 'actions',
      headerName: 'Actions',
      sortable: false,
      filterable: false,
      align: 'right',
      headerAlign: 'right',
      minWidth: 140,
      renderCell: (params) => (
        <>
          <IconButton aria-label="Modifier la configuration" onClick={() => openEditDialog(params.row)}>
            <EditOutlinedIcon fontSize="small" />
          </IconButton>
          <IconButton aria-label="Supprimer la configuration" color="error" onClick={() => setConfigToDelete(params.row)}>
            <DeleteIcon fontSize="small" />
          </IconButton>
        </>
      ),
    },
  ], []);

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

      <Paper variant="outlined" sx={{ p: { xs: 2, md: 2.5 } }}>
        <Stack spacing={2}>
          <Stack direction={{ xs: 'column', sm: 'row' }} spacing={1.2} sx={{ justifyContent: 'space-between', alignItems: { sm: 'center' } }}>
            <Typography variant="h6">Liste des configurations</Typography>
            <Button variant="contained" startIcon={<AddIcon />} onClick={() => setCreateDialogOpen(true)}>
              Nouvelle configuration
            </Button>
          </Stack>

          <TextField
            label="Rechercher"
            placeholder="Identifiant ou commentaire..."
            value={searchInput}
            onChange={(event) => setSearchInput(event.target.value)}
            fullWidth
          />

          {crudFeedback && <Alert severity={crudFeedback.severity}>{crudFeedback.message}</Alert>}
          {createState.isError && <Alert severity="error">Echec de la creation de la configuration.</Alert>}
          {updateState.isError && <Alert severity="error">Echec de la mise a jour de la configuration.</Alert>}
          {deleteState.isError && <Alert severity="error">Echec de la suppression de la configuration.</Alert>}

          {rows.length === 0 && (
            <Alert severity="info">Aucune configuration ne correspond a votre recherche.</Alert>
          )}

          {isDesktop ? (
            <Paper variant="outlined" sx={{ minHeight: 420 }}>
              <DataGrid
                rows={rows}
                columns={columns}
                getRowId={(row) => row.identifier ?? row.id ?? ''}
                rowCount={totalCount}
                loading={isLoading}
                paginationMode="server"
                paginationModel={paginationModel}
                onPaginationModelChange={setPaginationModel}
                pageSizeOptions={[5, 10, 25, 50]}
                disableRowSelectionOnClick
              />
            </Paper>
          ) : (
            <Stack spacing={1.2}>
              {rows.map((config) => (
                <Card key={config.identifier ?? config.id} variant="outlined">
                  <CardContent>
                    <Stack spacing={0.8}>
                      <Typography variant="subtitle1" sx={{ fontWeight: 600 }}>
                        {config.identifier}
                      </Typography>
                      <Typography variant="body2" color="text.secondary">
                        Token: {config.tokenPreview}
                      </Typography>
                      <Typography variant="body2" color="text.secondary">
                        Commentaire: {config.comment || 'Aucun commentaire'}
                      </Typography>
                    </Stack>
                  </CardContent>
                  <CardActions sx={{ justifyContent: 'flex-end', px: 2, pb: 1.5 }}>
                    <Button size="small" startIcon={<EditOutlinedIcon />} onClick={() => openEditDialog(config)}>
                      Modifier
                    </Button>
                    <Button size="small" color="error" startIcon={<DeleteIcon />} onClick={() => setConfigToDelete(config)}>
                      Supprimer
                    </Button>
                  </CardActions>
                </Card>
              ))}
              <TablePagination
                component="div"
                count={totalCount}
                page={paginationModel.page}
                onPageChange={(_event, page) => setPaginationModel((current) => ({ ...current, page }))}
                rowsPerPage={paginationModel.pageSize}
                onRowsPerPageChange={(event) => {
                  const pageSize = Number(event.target.value);
                  setPaginationModel({ page: 0, pageSize });
                }}
                rowsPerPageOptions={[5, 10, 25, 50]}
              />
            </Stack>
          )}
        </Stack>
      </Paper>

      <Paper variant="outlined" sx={{ p: { xs: 2, md: 2.5 } }}>
        <Stack spacing={2}>
          <Typography variant="h6">Droits GitHub requis pour le token</Typography>
          <Typography variant="body2" color="text.secondary">
            Renseignez les operations REST appelees, puis obtenez les permissions minimales a appliquer au PAT fine-grained.
          </Typography>
          <TextField
            label="Operations"
            value={operationsInput}
            onChange={(event) => setOperationsInput(event.target.value)}
            multiline
            minRows={4}
            helperText={helperText}
            fullWidth
          />
          <Stack direction="row" spacing={1.2} sx={{ flexWrap: 'wrap' }}>
            {knownOperations.map((operation) => (
              <Chip
                key={operation}
                label={operation}
                size="small"
                onClick={() => {
                  const current = new Set(parseOperations(operationsInput));
                  if (current.has(operation)) {
                    current.delete(operation);
                  } else {
                    current.add(operation);
                  }
                  setOperationsInput(Array.from(current).join('\n'));
                }}
              />
            ))}
          </Stack>
          <Button variant="contained" onClick={handleResolvePermissions} disabled={permissionState.isLoading}>
            Calculer les droits
          </Button>

          {permissionState.isError && (
            <Alert severity="error">Impossible de calculer les droits avec les operations saisies.</Alert>
          )}

          {permissionState.data && (
            <Stack spacing={1.5}>
              <Divider />
              <Typography variant="subtitle1">Permissions a configurer</Typography>

              {requiredPermissions.length === 0 && (
                <Alert severity="info">Aucune permission n'a ete calculee.</Alert>
              )}

              {requiredPermissions.length > 0 && (
                isDesktop ? (
                  <TableContainer component={Paper} variant="outlined">
                    <Table size="small" aria-label="permissions github requises">
                      <TableHead>
                        <TableRow>
                          <TableCell>Permission</TableCell>
                          <TableCell>Acces</TableCell>
                          <TableCell>Raison</TableCell>
                        </TableRow>
                      </TableHead>
                      <TableBody>
                        {requiredPermissions.map((permission, index) => (
                          <TableRow key={`${permission.permission}-${permission.access}-${index}`}>
                            <TableCell>{permission.permission}</TableCell>
                            <TableCell>
                              <Chip
                                size="small"
                                label={(permission.access ?? 'read').toUpperCase()}
                                color={permission.access === 'write' ? 'warning' : 'default'}
                              />
                            </TableCell>
                            <TableCell>{permission.reason}</TableCell>
                          </TableRow>
                        ))}
                      </TableBody>
                    </Table>
                  </TableContainer>
                ) : renderPermissionListMobile(requiredPermissions)
              )}

              {unknownOperations.length > 0 && (
                <Alert severity="warning">
                  Operations inconnues: {unknownOperations.join(', ')}
                </Alert>
              )}
            </Stack>
          )}
        </Stack>
      </Paper>

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
