import { useMemo, useState, type FC } from 'react';
import { Link as RouterLink } from 'react-router-dom';
import Alert from '@mui/material/Alert';
import Box from '@mui/material/Box';
import Button from '@mui/material/Button';
import Chip from '@mui/material/Chip';
import Dialog from '@mui/material/Dialog';
import DialogActions from '@mui/material/DialogActions';
import DialogContent from '@mui/material/DialogContent';
import DialogContentText from '@mui/material/DialogContentText';
import DialogTitle from '@mui/material/DialogTitle';
import IconButton from '@mui/material/IconButton';
import Stack from '@mui/material/Stack';
import TextField from '@mui/material/TextField';
import Typography from '@mui/material/Typography';
import ContentCopyIcon from '@mui/icons-material/ContentCopy';
import { ResponsiveCrudList, type CrudListColumn } from '../../../components/ResponsiveCrudList';
import { usePaginatedSearch } from '../../../hooks/usePaginatedSearch';
import { type Winget, useDeleteWingetMutation, useSearchWingetsQuery } from '../../../services/wingetApi';
import type { WingetListPageProps } from './WingetListPage.types';

type WingetRow = Winget & { id: string };

function toWingetRows(items: Winget[] | undefined): WingetRow[] {
  return (items ?? []).filter((item): item is WingetRow => Boolean(item.id));
}

const wingetColumns: CrudListColumn<WingetRow>[] = [
  {
    key: 'name',
    header: 'Nom',
    render: (winget) => <Typography sx={{ fontWeight: 600 }}>{winget.name}</Typography>,
  },
  {
    key: 'wingetId',
    header: 'Winget ID',
    render: (winget) => winget.wingetId || '-',
  },
  {
    key: 'installCommand',
    header: 'Commande',
    render: (winget) => (
      <Stack direction="row" spacing={1} sx={{ alignItems: 'flex-start', maxWidth: 320 }}>
        <Typography variant="body2" sx={{ wordBreak: 'break-word' }}>{winget.installCommand || '-'}</Typography>
        {winget.installCommand && navigator?.clipboard && (
          <IconButton
            size="small"
            aria-label="Copier la commande"
            onClick={() => void navigator.clipboard.writeText(winget.installCommand ?? '')}
          >
            <ContentCopyIcon fontSize="small" />
          </IconButton>
        )}
      </Stack>
    ),
  },
  {
    key: 'tags',
    header: 'Tags',
    render: (winget) => (
      <Stack direction="row" spacing={0.5} useFlexGap sx={{ flexWrap: 'wrap' }}>
        {(winget.tags ?? []).length > 0 ? winget.tags?.map((tag) => (
          <Chip key={tag} label={tag} size="small" variant="outlined" />
        )) : '-'}
      </Stack>
    ),
  },
];

export const WingetListPage: FC<WingetListPageProps> = () => {
  const {
    searchInput,
    setSearchInput,
    page,
    pageSize,
    queryArgs,
    handlePageChange,
    handlePageSizeChange,
  } = usePaginatedSearch();
  const { data, isLoading, error, refetch } = useSearchWingetsQuery(queryArgs, { refetchOnMountOrArgChange: true });
  const [deleteWinget, { isLoading: isDeleting }] = useDeleteWingetMutation();
  const [wingetToDelete, setWingetToDelete] = useState<WingetRow | null>(null);

  const wingets = useMemo(() => toWingetRows(data?.items), [data?.items]);

  const handleDelete = async () => {
    if (!wingetToDelete) return;

    await deleteWinget({ id: wingetToDelete.id }).unwrap();
    setWingetToDelete(null);
    await refetch();
  };

  if (isLoading) return <div>Chargement...</div>;
  if (error) return <div>Erreur lors du chargement des applications Winget</div>;

  return (
    <Stack spacing={3} sx={{ p: { xs: 2, md: 3 } }}>
      <Stack direction={{ xs: 'column', sm: 'row' }} spacing={1.5} useFlexGap sx={{ alignItems: { sm: 'center' }, justifyContent: 'space-between' }}>
        <Box>
          <Typography variant="h4" component="h1">
            Applications Winget
          </Typography>
          <Typography variant="body2" color="text.secondary">
            Gere des applications Windows installables via Winget.
          </Typography>
        </Box>
        <Stack direction="row" spacing={1} useFlexGap sx={{ flexWrap: 'wrap' }}>
          <Chip label={`${data?.count ?? 0} application${(data?.count ?? 0) > 1 ? 's' : ''}`} color="primary" variant="outlined" />
          <Button component={RouterLink} to="/winget/create" variant="contained">
            Nouvelle application
          </Button>
        </Stack>
      </Stack>

      <TextField
        fullWidth
        label="Recherche"
        placeholder="Rechercher par nom, winget id, commande ou tag"
        value={searchInput}
        onChange={(event) => setSearchInput(event.target.value)}
      />

      {wingets.length === 0 && (
        <Alert severity="info">
          Aucune application Winget configuree.
        </Alert>
      )}

      <ResponsiveCrudList
        items={wingets}
        getRowKey={(winget) => winget.id}
        columns={wingetColumns}
        getDetailPath={(winget) => `/winget/${winget.id}`}
        getEditPath={(winget) => `/winget/${winget.id}/edit`}
        onDelete={setWingetToDelete}
        actionLabels={{
          view: "Voir l'application Winget",
          edit: "Modifier l'application Winget",
          remove: "Supprimer l'application Winget",
        }}
        renderCardTitle={(winget) => winget.name}
        renderCardContent={(winget) => (
          <>
            <Typography variant="body2" color="text.secondary">
              Winget ID: {winget.wingetId || '-'}
            </Typography>
            <Typography variant="body2" color="text.secondary">
              Commande: {winget.installCommand || '-'}
            </Typography>
          </>
        )}
        pagination={{
          count: data?.count ?? 0,
          page,
          pageSize,
          onPageChange: handlePageChange,
          onPageSizeChange: handlePageSizeChange,
        }}
      />

      <Dialog open={Boolean(wingetToDelete)} onClose={() => setWingetToDelete(null)}>
        <DialogTitle>Supprimer l'application</DialogTitle>
        <DialogContent>
          <DialogContentText>
            Voulez-vous vraiment supprimer {wingetToDelete?.name || 'cette application'} ?
          </DialogContentText>
        </DialogContent>
        <DialogActions>
          <Button onClick={() => setWingetToDelete(null)} disabled={isDeleting}>Annuler</Button>
          <Button color="error" onClick={handleDelete} disabled={isDeleting}>Supprimer</Button>
        </DialogActions>
      </Dialog>
    </Stack>
  );
};
