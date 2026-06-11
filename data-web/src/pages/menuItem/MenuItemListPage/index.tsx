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
import { renderMenuItemIcon } from '../../../features/menuItem/iconRegistry';
import { type MenuItem, useDeleteMenuItemByIdMutation, useSearchMenuItemsQuery } from '../../../services/menuItemApi';
import type { MenuItemListPageProps } from './MenuItemListPage.types';

type MenuItemRow = MenuItem & { id: string };

function toMenuItemRows(items: MenuItem[] | undefined): MenuItemRow[] {
  return (items ?? []).filter((item): item is MenuItemRow => Boolean(item.id));
}

export const MenuItemListPage: FC<MenuItemListPageProps> = () => {
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
  const { data, isLoading, error, refetch } = useSearchMenuItemsQuery(queryArgs, { refetchOnMountOrArgChange: true });
  const [deleteMenuItemById, { isLoading: isDeleting }] = useDeleteMenuItemByIdMutation();
  const [menuItemToDelete, setMenuItemToDelete] = useState<MenuItemRow | null>(null);

  const menuItems = useMemo(() => toMenuItemRows(data?.items), [data?.items]);

  useEffect(() => {
    const timeout = setTimeout(() => {
      setSearchQuery(searchInput.trim());
      setPage(0);
    }, 300);
    return () => clearTimeout(timeout);
  }, [searchInput]);

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

      {isDesktop ? (
        <TableContainer component={Paper} variant="outlined">
          <Table>
            <TableHead>
              <TableRow>
                <TableCell>Icone</TableCell>
                <TableCell>Libelle</TableCell>
                <TableCell>Chemin</TableCell>
                <TableCell>Ordre</TableCell>
                <TableCell>Par defaut</TableCell>
                <TableCell align="right">Actions</TableCell>
              </TableRow>
            </TableHead>
            <TableBody>
              {menuItems.map((menuItem) => (
                <TableRow key={menuItem.id} hover>
                  <TableCell sx={{ width: 80 }}>{renderMenuItemIcon(menuItem.icon)}</TableCell>
                  <TableCell>{menuItem.label || 'Sans libelle'}</TableCell>
                  <TableCell>{menuItem.path}</TableCell>
                  <TableCell>{menuItem.displayOrder ?? 0}</TableCell>
                  <TableCell>
                    <Chip size="small" label={menuItem.defaultLoaded ? 'Oui' : 'Non'} color={menuItem.defaultLoaded ? 'success' : 'default'} variant={menuItem.defaultLoaded ? 'filled' : 'outlined'} />
                  </TableCell>
                  <TableCell align="right">
                    <IconButton component={RouterLink} to={`/menu-item/${menuItem.id}`} aria-label="Voir l'entree">
                      <VisibilityOutlinedIcon fontSize="small" />
                    </IconButton>
                    <IconButton component={RouterLink} to={`/menu-item/${menuItem.id}/edit`} aria-label="Modifier l'entree">
                      <EditOutlinedIcon fontSize="small" />
                    </IconButton>
                    <IconButton aria-label="Supprimer l'entree" color="error" onClick={() => setMenuItemToDelete(menuItem)}>
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
          {menuItems.map((menuItem) => (
            <Card key={menuItem.id} variant="outlined">
              <CardContent>
                <Stack spacing={1.5}>
                  <Stack direction="row" spacing={1.5} sx={{ alignItems: 'center' }}>
                    {renderMenuItemIcon(menuItem.icon, 'medium')}
                    <Box>
                      <Typography variant="h6">{menuItem.label || 'Sans libelle'}</Typography>
                      <Typography variant="body2" color="text.secondary">
                        {menuItem.path}
                      </Typography>
                    </Box>
                  </Stack>
                  <Stack direction="row" spacing={1} sx={{ alignItems: 'center' }}>
                    <Chip size="small" label={`Ordre ${menuItem.displayOrder ?? 0}`} variant="outlined" />
                    <Chip size="small" label={menuItem.defaultLoaded ? 'Charge par defaut' : 'Ajoute manuellement'} color={menuItem.defaultLoaded ? 'success' : 'default'} variant={menuItem.defaultLoaded ? 'filled' : 'outlined'} />
                  </Stack>
                </Stack>
              </CardContent>
              <CardActions sx={{ px: 2, pb: 2, pt: 0, gap: 1, flexWrap: 'wrap' }}>
                <Button component={RouterLink} to={`/menu-item/${menuItem.id}`} size="small" variant="outlined">
                  Voir
                </Button>
                <Button component={RouterLink} to={`/menu-item/${menuItem.id}/edit`} size="small" variant="outlined">
                  Modifier
                </Button>
                <Button size="small" color="error" variant="outlined" onClick={() => setMenuItemToDelete(menuItem)}>
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
