import { useMemo, type FC } from 'react';
import Alert from '@mui/material/Alert';
import Button from '@mui/material/Button';
import Card from '@mui/material/Card';
import CardActions from '@mui/material/CardActions';
import CardContent from '@mui/material/CardContent';
import Paper from '@mui/material/Paper';
import Stack from '@mui/material/Stack';
import TablePagination from '@mui/material/TablePagination';
import TextField from '@mui/material/TextField';
import Typography from '@mui/material/Typography';
import AddIcon from '@mui/icons-material/Add';
import DeleteIcon from '@mui/icons-material/Delete';
import EditOutlinedIcon from '@mui/icons-material/EditOutlined';
import IconButton from '@mui/material/IconButton';
import { DataGrid, type GridColDef, type GridPaginationModel } from '@mui/x-data-grid';
import type { GitHubRestConfig } from '../../../services/githubRestConfigApi';

type GitHubTokenConfigListSectionProps = {
  isDesktop: boolean;
  rows: GitHubRestConfig[];
  totalCount: number;
  isLoading: boolean;
  searchInput: string;
  onSearchInputChange: (value: string) => void;
  paginationModel: GridPaginationModel;
  onPaginationModelChange: (model: GridPaginationModel) => void;
  onOpenCreate: () => void;
  onOpenEdit: (config: GitHubRestConfig) => void;
  onAskDelete: (config: GitHubRestConfig) => void;
  crudFeedback: { severity: 'success' | 'error'; message: string } | null;
  createError: boolean;
  updateError: boolean;
  deleteError: boolean;
};

export const GitHubTokenConfigListSection: FC<GitHubTokenConfigListSectionProps> = ({
  isDesktop,
  rows,
  totalCount,
  isLoading,
  searchInput,
  onSearchInputChange,
  paginationModel,
  onPaginationModelChange,
  onOpenCreate,
  onOpenEdit,
  onAskDelete,
  crudFeedback,
  createError,
  updateError,
  deleteError,
}) => {
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
          <IconButton aria-label="Modifier la configuration" onClick={() => onOpenEdit(params.row)}>
            <EditOutlinedIcon fontSize="small" />
          </IconButton>
          <IconButton aria-label="Supprimer la configuration" color="error" onClick={() => onAskDelete(params.row)}>
            <DeleteIcon fontSize="small" />
          </IconButton>
        </>
      ),
    },
  ], [onAskDelete, onOpenEdit]);

  return (
    <Paper variant="outlined" sx={{ p: { xs: 2, md: 2.5 } }}>
      <Stack spacing={2}>
        <Stack direction={{ xs: 'column', sm: 'row' }} spacing={1.2} sx={{ justifyContent: 'space-between', alignItems: { sm: 'center' } }}>
          <Typography variant="h6">Liste des configurations</Typography>
          <Button variant="contained" startIcon={<AddIcon />} onClick={onOpenCreate}>
            Nouvelle configuration
          </Button>
        </Stack>

        <TextField
          label="Rechercher"
          placeholder="Identifiant ou commentaire..."
          value={searchInput}
          onChange={(event) => onSearchInputChange(event.target.value)}
          fullWidth
        />

        {crudFeedback && <Alert severity={crudFeedback.severity}>{crudFeedback.message}</Alert>}
        {createError && <Alert severity="error">Echec de la creation de la configuration.</Alert>}
        {updateError && <Alert severity="error">Echec de la mise a jour de la configuration.</Alert>}
        {deleteError && <Alert severity="error">Echec de la suppression de la configuration.</Alert>}

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
              onPaginationModelChange={onPaginationModelChange}
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
                  <Button size="small" startIcon={<EditOutlinedIcon />} onClick={() => onOpenEdit(config)}>
                    Modifier
                  </Button>
                  <Button size="small" color="error" startIcon={<DeleteIcon />} onClick={() => onAskDelete(config)}>
                    Supprimer
                  </Button>
                </CardActions>
              </Card>
            ))}
            <TablePagination
              component="div"
              count={totalCount}
              page={paginationModel.page}
              onPageChange={(_event, page) => onPaginationModelChange({ ...paginationModel, page })}
              rowsPerPage={paginationModel.pageSize}
              onRowsPerPageChange={(event) => {
                const pageSize = Number(event.target.value);
                onPaginationModelChange({ page: 0, pageSize });
              }}
              rowsPerPageOptions={[5, 10, 25, 50]}
            />
          </Stack>
        )}
      </Stack>
    </Paper>
  );
};
