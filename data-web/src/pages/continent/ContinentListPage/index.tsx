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
import { type Continent, useDeleteContinentMutation, useSearchContinentsQuery } from '../../../services/continentApi';
import type { ContinentListPageProps } from './ContinentListPage.types';

type ContinentRow = Continent & { id: string };

function toContinentRows(items: Continent[] | undefined): ContinentRow[] {
  return (items ?? []).filter((item): item is ContinentRow => Boolean(item.id));
}

const continentColumns: CrudListColumn<ContinentRow>[] = [
  {
    key: 'code',
    header: 'Code',
    render: (continent) => <Typography sx={{ fontWeight: 600 }}>{continent.code}</Typography>,
  },
  {
    key: 'name',
    header: 'Nom',
    render: (continent) => continent.name,
  },
];

export const ContinentListPage: FC<ContinentListPageProps> = () => {
  const {
    searchInput,
    setSearchInput,
    page,
    pageSize,
    queryArgs,
    handlePageChange,
    handlePageSizeChange,
  } = usePaginatedSearch();
  const { data, isLoading, error, refetch } = useSearchContinentsQuery(queryArgs, { refetchOnMountOrArgChange: true });
  const [deleteContinent, { isLoading: isDeleting }] = useDeleteContinentMutation();
  const [continentToDelete, setContinentToDelete] = useState<ContinentRow | null>(null);

  const continents = useMemo(() => toContinentRows(data?.items), [data?.items]);

  const handleDelete = async () => {
    if (!continentToDelete) return;

    await deleteContinent({ id: continentToDelete.id }).unwrap();
    setContinentToDelete(null);
    await refetch();
  };

  if (isLoading) return <div>Chargement...</div>;
  if (error) return <div>Erreur lors du chargement des continents</div>;

  return (
    <Stack spacing={3} sx={{ p: { xs: 2, md: 3 } }}>
      <Stack direction={{ xs: 'column', sm: 'row' }} spacing={1.5} useFlexGap sx={{ alignItems: { sm: 'center' }, justifyContent: 'space-between' }}>
        <Box>
          <Typography variant="h4" component="h1">
            Continents
          </Typography>
          <Typography variant="body2" color="text.secondary">
            Gere les continents avec code et nom.
          </Typography>
        </Box>
        <Stack direction="row" spacing={1} useFlexGap sx={{ flexWrap: 'wrap' }}>
          <Chip label={`${data?.count ?? 0} continent${(data?.count ?? 0) > 1 ? 's' : ''}`} color="primary" variant="outlined" />
          <Button component={RouterLink} to="/continent/create" variant="contained">
            Nouveau continent
          </Button>
        </Stack>
      </Stack>

      <TextField
        fullWidth
        label="Recherche"
        placeholder="Rechercher par code ou nom"
        value={searchInput}
        onChange={(event) => setSearchInput(event.target.value)}
      />

      {continents.length === 0 && (
        <Alert severity="info">
          Aucun continent configure.
        </Alert>
      )}

      <ResponsiveCrudList
        items={continents}
        getRowKey={(continent) => continent.id}
        columns={continentColumns}
        getDetailPath={(continent) => `/continent/${continent.id}`}
        getEditPath={(continent) => `/continent/${continent.id}/edit`}
        onDelete={setContinentToDelete}
        actionLabels={{
          view: 'Voir le continent',
          edit: 'Modifier le continent',
          remove: 'Supprimer le continent',
        }}
        renderCardTitle={(continent) => continent.name}
        renderCardContent={(continent) => (
          <Typography variant="body2" color="text.secondary">
            Code: {continent.code || '-'}
          </Typography>
        )}
        pagination={{
          count: data?.count ?? 0,
          page,
          pageSize,
          onPageChange: handlePageChange,
          onPageSizeChange: handlePageSizeChange,
        }}
      />

      <Dialog open={Boolean(continentToDelete)} onClose={() => setContinentToDelete(null)}>
        <DialogTitle>Supprimer le continent</DialogTitle>
        <DialogContent>
          <DialogContentText>
            Voulez-vous vraiment supprimer {continentToDelete?.name || 'ce continent'} ?
          </DialogContentText>
        </DialogContent>
        <DialogActions>
          <Button onClick={() => setContinentToDelete(null)} disabled={isDeleting}>Annuler</Button>
          <Button color="error" onClick={handleDelete} disabled={isDeleting}>Supprimer</Button>
        </DialogActions>
      </Dialog>
    </Stack>
  );
};
