import { useEffect, useMemo, useState, type FC } from 'react';
import { Link as RouterLink } from 'react-router-dom';
import Alert from '@mui/material/Alert';
import Box from '@mui/material/Box';
import Button from '@mui/material/Button';
import Card from '@mui/material/Card';
import CardActions from '@mui/material/CardActions';
import CardContent from '@mui/material/CardContent';
import Chip from '@mui/material/Chip';
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
import useMediaQuery from '@mui/material/useMediaQuery';
import { useTheme } from '@mui/material/styles';
import DeleteIcon from '@mui/icons-material/Delete';
import EditOutlinedIcon from '@mui/icons-material/EditOutlined';
import VisibilityOutlinedIcon from '@mui/icons-material/VisibilityOutlined';
import ContentCopyIcon from '@mui/icons-material/ContentCopy';
import { type Winget, useDeleteWingetMutation, useSearchWingetsQuery } from '../../../services/wingetApi';
import type { WingetListPageProps } from './WingetListPage.types';

type WingetRow = Winget & { id: string };

function toWingetRows(items: Winget[] | undefined): WingetRow[] {
  return (items ?? []).filter((item): item is WingetRow => Boolean(item.id));
}

export const WingetListPage: FC<WingetListPageProps> = () => {
  const theme = useTheme();
  const isDesktop = useMediaQuery(theme.breakpoints.up('md'));
  const [searchInput, setSearchInput] = useState('');
  const [searchQuery, setSearchQuery] = useState('');
  const [page, setPage] = useState(0);
  const [pageSize, setPageSize] = useState(10);
  const queryArgs = useMemo(() => ({
    page,
    size: pageSize,
    q: searchQuery || undefined,
  }), [page, pageSize, searchQuery]);
  const { data, isLoading, error, refetch } = useSearchWingetsQuery(queryArgs, { refetchOnMountOrArgChange: true });
  const [deleteWinget, { isLoading: isDeleting }] = useDeleteWingetMutation();
  const [wingetToDelete, setWingetToDelete] = useState<WingetRow | null>(null);

  const wingets = useMemo(() => toWingetRows(data?.items), [data?.items]);

  useEffect(() => {
    const timeout = setTimeout(() => {
      setSearchQuery(searchInput.trim());
      setPage(0);
    }, 300);
    return () => clearTimeout(timeout);
  }, [searchInput]);

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

      {isDesktop ? (
        <TableContainer component={Paper} variant="outlined">
          <Table>
            <TableHead>
              <TableRow>
                <TableCell>Nom</TableCell>
                <TableCell>Winget ID</TableCell>
                <TableCell>Commande</TableCell>
                <TableCell>Tags</TableCell>
                <TableCell align="right">Actions</TableCell>
              </TableRow>
            </TableHead>
            <TableBody>
              {wingets.map((winget) => (
                <TableRow key={winget.id} hover>
                  <TableCell>
                    <Typography sx={{ fontWeight: 600 }}>{winget.name}</Typography>
                  </TableCell>
                  <TableCell>{winget.wingetId || '-'}</TableCell>
                  <TableCell sx={{ maxWidth: 320 }}>
                    <Stack direction="row" spacing={1} sx={{ alignItems: 'flex-start' }}>
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
                  </TableCell>
                  <TableCell>
                    <Stack direction="row" spacing={0.5} useFlexGap sx={{ flexWrap: 'wrap' }}>
                      {(winget.tags ?? []).length > 0 ? winget.tags?.map((tag) => (
                        <Chip key={tag} label={tag} size="small" variant="outlined" />
                      )) : '-'}
                    </Stack>
                  </TableCell>
                  <TableCell align="right">
                    <IconButton component={RouterLink} to={`/winget/${winget.id}`} aria-label="Voir l'application Winget">
                      <VisibilityOutlinedIcon fontSize="small" />
                    </IconButton>
                    <IconButton component={RouterLink} to={`/winget/${winget.id}/edit`} aria-label="Modifier l'application Winget">
                      <EditOutlinedIcon fontSize="small" />
                    </IconButton>
                    <IconButton aria-label="Supprimer l'application Winget" color="error" onClick={() => setWingetToDelete(winget)}>
                      <DeleteIcon fontSize="small" />
                    </IconButton>
                  </TableCell>
                </TableRow>
              ))}
            </TableBody>
          </Table>
          <TablePagination
            component="div"
            count={data?.count ?? 0}
            page={page}
            onPageChange={(_event, nextPage) => setPage(nextPage)}
            rowsPerPage={pageSize}
            onRowsPerPageChange={(event) => {
              const nextSize = Number(event.target.value);
              setPageSize(nextSize);
              setPage(0);
            }}
            rowsPerPageOptions={[10, 20, 50]}
          />
        </TableContainer>
      ) : (
        <Stack spacing={2}>
          {wingets.map((winget) => (
            <Card key={winget.id} variant="outlined">
              <CardContent>
                <Stack spacing={1}>
                  <Typography variant="h6">{winget.name}</Typography>
                  <Typography variant="body2" color="text.secondary">
                    Winget ID: {winget.wingetId || '-'}
                  </Typography>
                  <Typography variant="body2" color="text.secondary">
                    Commande: {winget.installCommand || '-'}
                  </Typography>
                </Stack>
              </CardContent>
              <CardActions sx={{ px: 2, pb: 2, pt: 0, gap: 1, flexWrap: 'wrap' }}>
                <Button component={RouterLink} to={`/winget/${winget.id}`} size="small" variant="outlined">
                  Voir
                </Button>
                <Button component={RouterLink} to={`/winget/${winget.id}/edit`} size="small" variant="outlined">
                  Modifier
                </Button>
                <Button size="small" color="error" variant="outlined" onClick={() => setWingetToDelete(winget)}>
                  Supprimer
                </Button>
              </CardActions>
            </Card>
          ))}
        </Stack>
      )}

      {!isDesktop && (
        <TablePagination
          component="div"
          count={data?.count ?? 0}
          page={page}
          onPageChange={(_event, nextPage) => setPage(nextPage)}
          rowsPerPage={pageSize}
          onRowsPerPageChange={(event) => {
            const nextSize = Number(event.target.value);
            setPageSize(nextSize);
            setPage(0);
          }}
          rowsPerPageOptions={[10, 20, 50]}
        />
      )}

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
