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
import { renderMenuItemIcon } from '../../../features/menuItem/iconRegistry';
import { type MenuItem, useDeleteMenuItemByIdMutation, useSearchMenuItemsQuery } from '../../../services/menuItemApi';
import type { MenuItemListPageProps } from './MenuItemListPage.types';

type MenuItemRow = MenuItem & { id: string };

function toMenuItemRows(items: MenuItem[] | undefined): MenuItemRow[] {
  return (items ?? []).filter((item): item is MenuItemRow => Boolean(item.id));
}

const menuItemColumns: CrudListColumn<MenuItemRow>[] = [
  {
    key: 'icon',
    header: 'Icone',
    render: (menuItem) => <Box sx={{ width: 80 }}>{renderMenuItemIcon(menuItem.icon)}</Box>,
  },
  {
    key: 'label',
    header: 'Libelle',
    render: (menuItem) => menuItem.label || 'Sans libelle',
  },
  {
    key: 'path',
    header: 'Chemin',
    render: (menuItem) => menuItem.path,
  },
  {
    key: 'displayOrder',
    header: 'Ordre',
    render: (menuItem) => menuItem.displayOrder ?? 0,
  },
  {
    key: 'defaultLoaded',
    header: 'Par defaut',
    render: (menuItem) => (
      <Chip size="small" label={menuItem.defaultLoaded ? 'Oui' : 'Non'} color={menuItem.defaultLoaded ? 'success' : 'default'} variant={menuItem.defaultLoaded ? 'filled' : 'outlined'} />
    ),
  },
];

export const MenuItemListPage: FC<MenuItemListPageProps> = () => {
  const {
    searchInput,
    setSearchInput,
    page,
    pageSize,
    queryArgs,
    handlePageChange,
    handlePageSizeChange,
  } = usePaginatedSearch();
  const { data, isLoading, error, refetch } = useSearchMenuItemsQuery(queryArgs, { refetchOnMountOrArgChange: true });
  const [deleteMenuItemById, { isLoading: isDeleting }] = useDeleteMenuItemByIdMutation();
  const [menuItemToDelete, setMenuItemToDelete] = useState<MenuItemRow | null>(null);

  const menuItems = useMemo(() => toMenuItemRows(data?.items), [data?.items]);

  const handleDelete = async () => {
    if (!menuItemToDelete) return;

    await deleteMenuItemById({ id: menuItemToDelete.id }).unwrap();
    setMenuItemToDelete(null);
    await refetch();
  };

  if (isLoading) return <div>Chargement...</div>;
  if (error) return <div>Erreur lors du chargement des entrees de menu</div>;

  return (
    <Stack spacing={3} sx={{ p: { xs: 2, md: 3 } }}>
      <Stack direction={{ xs: 'column', sm: 'row' }} spacing={1.5} useFlexGap sx={{ alignItems: { sm: 'center' }, justifyContent: 'space-between' }}>
        <Box>
          <Typography variant="h4" component="h1">
            Menu gauche
          </Typography>
          <Typography variant="body2" color="text.secondary">
            Les entrees sont stockees en base. Le serveur charge des valeurs par defaut au premier demarrage si la table est vide.
          </Typography>
        </Box>
        <Stack direction="row" spacing={1}>
          <Chip label={`${data?.count ?? 0} entree${(data?.count ?? 0) > 1 ? 's' : ''}`} color="primary" variant="outlined" />
          <Button component={RouterLink} to="/menu-item/create" variant="contained">
            Nouvelle entree
          </Button>
        </Stack>
      </Stack>

      <TextField
        fullWidth
        label="Recherche"
        placeholder="Rechercher par libelle, chemin ou icone"
        value={searchInput}
        onChange={(event) => setSearchInput(event.target.value)}
      />

      {menuItems.length === 0 && (
        <Alert severity="info">
          Aucune entree de menu n'est configuree en base.
        </Alert>
      )}

      <ResponsiveCrudList
        items={menuItems}
        getRowKey={(menuItem) => menuItem.id}
        columns={menuItemColumns}
        getDetailPath={(menuItem) => `/menu-item/${menuItem.id}`}
        getEditPath={(menuItem) => `/menu-item/${menuItem.id}/edit`}
        onDelete={setMenuItemToDelete}
        actionLabels={{
          view: "Voir l'entree",
          edit: "Modifier l'entree",
          remove: "Supprimer l'entree",
        }}
        renderCardTitle={(menuItem) => (
          <Box component="span" sx={{ display: 'inline-flex', alignItems: 'center', gap: 1.5 }}>
            {renderMenuItemIcon(menuItem.icon, 'medium')}
            {menuItem.label || 'Sans libelle'}
          </Box>
        )}
        renderCardContent={(menuItem) => (
          <>
            <Typography variant="body2" color="text.secondary">
              {menuItem.path}
            </Typography>
            <Stack direction="row" spacing={1} sx={{ alignItems: 'center' }}>
              <Chip size="small" label={`Ordre ${menuItem.displayOrder ?? 0}`} variant="outlined" />
              <Chip size="small" label={menuItem.defaultLoaded ? 'Charge par defaut' : 'Ajoute manuellement'} color={menuItem.defaultLoaded ? 'success' : 'default'} variant={menuItem.defaultLoaded ? 'filled' : 'outlined'} />
            </Stack>
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

      <Dialog open={Boolean(menuItemToDelete)} onClose={() => setMenuItemToDelete(null)}>
        <DialogTitle>Supprimer l'entree</DialogTitle>
        <DialogContent>
          <DialogContentText>
            Voulez-vous vraiment supprimer {menuItemToDelete?.label || 'cette entree'} du menu ?
          </DialogContentText>
        </DialogContent>
        <DialogActions>
          <Button onClick={() => setMenuItemToDelete(null)} disabled={isDeleting}>Annuler</Button>
          <Button color="error" onClick={handleDelete} disabled={isDeleting}>Supprimer</Button>
        </DialogActions>
      </Dialog>
    </Stack>
  );
};
