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
import { type UserAccount, useDeleteUserAccountMutation, useSearchUserAccountsQuery } from '../../../services/userAccountApi';
import type { UserAccountListPageProps } from './UserAccountListPage.types';

type UserAccountRow = UserAccount & { id: string };

function toUserAccountRows(items: UserAccount[] | undefined): UserAccountRow[] {
  return (items ?? []).filter((item): item is UserAccountRow => Boolean(item.id));
}

export const UserAccountListPage: FC<UserAccountListPageProps> = () => {
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
  const { data, isLoading, error, refetch } = useSearchUserAccountsQuery(queryArgs, { refetchOnMountOrArgChange: true });
  const [deleteUserAccount, { isLoading: isDeleting }] = useDeleteUserAccountMutation();
  const [accountToDelete, setAccountToDelete] = useState<UserAccountRow | null>(null);

  const accounts = useMemo(() => toUserAccountRows(data?.items), [data?.items]);

  useEffect(() => {
    const timeout = setTimeout(() => {
      setSearchQuery(searchInput.trim());
      setPage(0);
    }, 300);
    return () => clearTimeout(timeout);
  }, [searchInput]);

  const handleDelete = async () => {
    if (!accountToDelete) return;

    await deleteUserAccount({ id: accountToDelete.id }).unwrap();
    setAccountToDelete(null);
    await refetch();
  };

  if (isLoading) return <div>Chargement...</div>;
  if (error) return <div>Erreur lors du chargement des comptes utilisateurs</div>;

  return (
    <Stack spacing={3} sx={{ p: { xs: 2, md: 3 } }}>
      <Stack direction={{ xs: 'column', sm: 'row' }} spacing={1.5} useFlexGap sx={{ alignItems: { sm: 'center' }, justifyContent: 'space-between' }}>
        <Box>
          <Typography variant="h4" component="h1">
            Comptes utilisateurs
          </Typography>
          <Typography variant="body2" color="text.secondary">
            Gere les comptes internes avec leurs roles et leur etat d'activation.
          </Typography>
        </Box>
        <Stack direction="row" spacing={1} useFlexGap sx={{ flexWrap: 'wrap' }}>
          <Chip label={`${data?.count ?? 0} compte${(data?.count ?? 0) > 1 ? 's' : ''}`} color="primary" variant="outlined" />
          <Button component={RouterLink} to="/user-account/create" variant="contained">
            Nouveau compte
          </Button>
        </Stack>
      </Stack>

      <TextField
        fullWidth
        label="Recherche"
        placeholder="Rechercher par nom utilisateur ou role"
        value={searchInput}
        onChange={(event) => setSearchInput(event.target.value)}
      />

      {accounts.length === 0 && (
        <Alert severity="info">
          Aucun compte utilisateur configure.
        </Alert>
      )}

      {isDesktop ? (
        <TableContainer component={Paper} variant="outlined">
          <Table>
            <TableHead>
              <TableRow>
                <TableCell>Nom utilisateur</TableCell>
                <TableCell>Role</TableCell>
                <TableCell>Etat</TableCell>
                <TableCell align="right">Actions</TableCell>
              </TableRow>
            </TableHead>
            <TableBody>
              {accounts.map((account) => (
                <TableRow key={account.id} hover>
                  <TableCell>
                    <Typography sx={{ fontWeight: 600 }}>{account.username}</Typography>
                  </TableCell>
                  <TableCell>{account.role}</TableCell>
                  <TableCell>{account.enabled ? 'Actif' : 'Desactive'}</TableCell>
                  <TableCell align="right">
                    <IconButton component={RouterLink} to={`/user-account/${account.id}`} aria-label="Voir le compte utilisateur">
                      <VisibilityOutlinedIcon fontSize="small" />
                    </IconButton>
                    <IconButton component={RouterLink} to={`/user-account/${account.id}/edit`} aria-label="Modifier le compte utilisateur">
                      <EditOutlinedIcon fontSize="small" />
                    </IconButton>
                    <IconButton aria-label="Supprimer le compte utilisateur" color="error" onClick={() => setAccountToDelete(account)}>
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
          {accounts.map((account) => (
            <Card key={account.id} variant="outlined">
              <CardContent>
                <Stack spacing={1}>
                  <Typography variant="h6">{account.username}</Typography>
                  <Typography variant="body2" color="text.secondary">
                    Role: {account.role || '-'}
                  </Typography>
                  <Typography variant="body2" color="text.secondary">
                    Etat: {account.enabled ? 'Actif' : 'Desactive'}
                  </Typography>
                </Stack>
              </CardContent>
              <CardActions sx={{ px: 2, pb: 2, pt: 0, gap: 1, flexWrap: 'wrap' }}>
                <Button component={RouterLink} to={`/user-account/${account.id}`} size="small" variant="outlined">
                  Voir
                </Button>
                <Button component={RouterLink} to={`/user-account/${account.id}/edit`} size="small" variant="outlined">
                  Modifier
                </Button>
                <Button size="small" color="error" variant="outlined" onClick={() => setAccountToDelete(account)}>
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

      <Dialog open={Boolean(accountToDelete)} onClose={() => setAccountToDelete(null)}>
        <DialogTitle>Supprimer le compte utilisateur</DialogTitle>
        <DialogContent>
          <DialogContentText>
            Voulez-vous vraiment supprimer {accountToDelete?.username || 'ce compte utilisateur'} ?
          </DialogContentText>
        </DialogContent>
        <DialogActions>
          <Button onClick={() => setAccountToDelete(null)} disabled={isDeleting}>Annuler</Button>
          <Button color="error" onClick={handleDelete} disabled={isDeleting}>Supprimer</Button>
        </DialogActions>
      </Dialog>
    </Stack>
  );
};
