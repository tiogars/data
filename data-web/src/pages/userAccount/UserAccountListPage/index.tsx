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
import Stack from '@mui/material/Stack';
import TextField from '@mui/material/TextField';
import Typography from '@mui/material/Typography';
import { ResponsiveCrudList, type CrudListColumn } from '../../../components/ResponsiveCrudList';
import { usePaginatedSearch } from '../../../hooks/usePaginatedSearch';
import { type UserAccount, useDeleteUserAccountMutation, useSearchUserAccountsQuery } from '../../../services/userAccountApi';
import type { UserAccountListPageProps } from './UserAccountListPage.types';

type UserAccountRow = UserAccount & { id: string };

function toUserAccountRows(items: UserAccount[] | undefined): UserAccountRow[] {
  return (items ?? []).filter((item): item is UserAccountRow => Boolean(item.id));
}

const userAccountColumns: CrudListColumn<UserAccountRow>[] = [
  {
    key: 'username',
    header: 'Nom utilisateur',
    render: (account) => <Typography sx={{ fontWeight: 600 }}>{account.username}</Typography>,
  },
  {
    key: 'role',
    header: 'Role',
    render: (account) => account.role,
  },
  {
    key: 'enabled',
    header: 'Etat',
    render: (account) => (account.enabled ? 'Actif' : 'Desactive'),
  },
];

export const UserAccountListPage: FC<UserAccountListPageProps> = () => {
  const {
    searchInput,
    setSearchInput,
    page,
    pageSize,
    queryArgs,
    handlePageChange,
    handlePageSizeChange,
  } = usePaginatedSearch();
  const { data, isLoading, error, refetch } = useSearchUserAccountsQuery(queryArgs, { refetchOnMountOrArgChange: true });
  const [deleteUserAccount, { isLoading: isDeleting }] = useDeleteUserAccountMutation();
  const [accountToDelete, setAccountToDelete] = useState<UserAccountRow | null>(null);

  const accounts = useMemo(() => toUserAccountRows(data?.items), [data?.items]);

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

      <ResponsiveCrudList
        items={accounts}
        getRowKey={(account) => account.id}
        columns={userAccountColumns}
        getDetailPath={(account) => `/user-account/${account.id}`}
        getEditPath={(account) => `/user-account/${account.id}/edit`}
        onDelete={setAccountToDelete}
        actionLabels={{
          view: 'Voir le compte utilisateur',
          edit: 'Modifier le compte utilisateur',
          remove: 'Supprimer le compte utilisateur',
        }}
        renderCardTitle={(account) => account.username}
        renderCardContent={(account) => (
          <>
            <Typography variant="body2" color="text.secondary">
              Role: {account.role || '-'}
            </Typography>
            <Typography variant="body2" color="text.secondary">
              Etat: {account.enabled ? 'Actif' : 'Desactive'}
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
