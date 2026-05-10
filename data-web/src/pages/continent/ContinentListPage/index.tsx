import { useMemo, useState, type FC } from 'react';
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
import TableRow from '@mui/material/TableRow';
import Typography from '@mui/material/Typography';
import useMediaQuery from '@mui/material/useMediaQuery';
import { useTheme } from '@mui/material/styles';
import DeleteIcon from '@mui/icons-material/Delete';
import EditOutlinedIcon from '@mui/icons-material/EditOutlined';
import VisibilityOutlinedIcon from '@mui/icons-material/VisibilityOutlined';
import { type Continent, useDeleteContinentMutation, useListContinentsQuery } from '../../../services/continentApi';
import type { ContinentListPageProps } from './ContinentListPage.types';

type ContinentRow = Continent & { id: string };

function toContinentRows(items: Continent[] | undefined): ContinentRow[] {
  return (items ?? []).filter((item): item is ContinentRow => Boolean(item.id));
}

export const ContinentListPage: FC<ContinentListPageProps> = () => {
  const theme = useTheme();
  const isDesktop = useMediaQuery(theme.breakpoints.up('md'));
  const { data, isLoading, error, refetch } = useListContinentsQuery(undefined, { refetchOnMountOrArgChange: true });
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

      {continents.length === 0 && (
        <Alert severity="info">
          Aucun continent configure.
        </Alert>
      )}

      {isDesktop ? (
        <TableContainer component={Paper} variant="outlined">
          <Table>
            <TableHead>
              <TableRow>
                <TableCell>Code</TableCell>
                <TableCell>Nom</TableCell>
                <TableCell align="right">Actions</TableCell>
              </TableRow>
            </TableHead>
            <TableBody>
              {continents.map((continent) => (
                <TableRow key={continent.id} hover>
                  <TableCell>
                    <Typography sx={{ fontWeight: 600 }}>{continent.code}</Typography>
                  </TableCell>
                  <TableCell>{continent.name}</TableCell>
                  <TableCell align="right">
                    <IconButton component={RouterLink} to={`/continent/${continent.id}`} aria-label="Voir le continent">
                      <VisibilityOutlinedIcon fontSize="small" />
                    </IconButton>
                    <IconButton component={RouterLink} to={`/continent/${continent.id}/edit`} aria-label="Modifier le continent">
                      <EditOutlinedIcon fontSize="small" />
                    </IconButton>
                    <IconButton aria-label="Supprimer le continent" color="error" onClick={() => setContinentToDelete(continent)}>
                      <DeleteIcon fontSize="small" />
                    </IconButton>
                  </TableCell>
                </TableRow>
              ))}
            </TableBody>
          </Table>
        </TableContainer>
      ) : (
        <Stack spacing={2}>
          {continents.map((continent) => (
            <Card key={continent.id} variant="outlined">
              <CardContent>
                <Stack spacing={1}>
                  <Typography variant="h6">{continent.name}</Typography>
                  <Typography variant="body2" color="text.secondary">
                    Code: {continent.code || '-'}
                  </Typography>
                </Stack>
              </CardContent>
              <CardActions sx={{ px: 2, pb: 2, pt: 0, gap: 1, flexWrap: 'wrap' }}>
                <Button component={RouterLink} to={`/continent/${continent.id}`} size="small" variant="outlined">
                  Voir
                </Button>
                <Button component={RouterLink} to={`/continent/${continent.id}/edit`} size="small" variant="outlined">
                  Modifier
                </Button>
                <Button size="small" color="error" variant="outlined" onClick={() => setContinentToDelete(continent)}>
                  Supprimer
                </Button>
              </CardActions>
            </Card>
          ))}
        </Stack>
      )}

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
