import { useMemo, useRef, useState, type ChangeEvent, type FC } from 'react';
import { Link as RouterLink } from 'react-router-dom';
import Alert from '@mui/material/Alert';
import Box from '@mui/material/Box';
import Button from '@mui/material/Button';
import Card from '@mui/material/Card';
import CardActions from '@mui/material/CardActions';
import CardContent from '@mui/material/CardContent';
import Chip from '@mui/material/Chip';
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
import DownloadIcon from '@mui/icons-material/Download';
import EditOutlinedIcon from '@mui/icons-material/EditOutlined';
import UploadFileIcon from '@mui/icons-material/UploadFile';
import { usePaginatedSearch } from '../../../hooks/usePaginatedSearch';
import { useDeleteCarMutation, useSearchCarsQuery, type Car } from '../../../services/carApi';
import {
  useImportCarsCsvMutation,
  useImportCarsMutation,
  useLazyExportCarsCsvTextQuery,
  useLazyExportCarsQuery,
} from '../../../services/carImportExportApi';
import type { CarListPageProps } from './CarListPage.types';

type CarRow = Car & { id: string };

function toCarRows(items: Car[] | undefined): CarRow[] {
  return (items ?? []).filter((item): item is CarRow => Boolean(item.id));
}

function createExportFileName() {
  const date = new Date();
  const yyyy = date.getFullYear();
  const mm = String(date.getMonth() + 1).padStart(2, '0');
  const dd = String(date.getDate()).padStart(2, '0');
  return `car-export-${yyyy}${mm}${dd}.json`;
}

function createExportCsvFileName() {
  const date = new Date();
  const yyyy = date.getFullYear();
  const mm = String(date.getMonth() + 1).padStart(2, '0');
  const dd = String(date.getDate()).padStart(2, '0');
  return `car-export-${yyyy}${mm}${dd}.csv`;
}

export const CarListPage: FC<CarListPageProps> = () => {
  const theme = useTheme();
  const isDesktop = useMediaQuery(theme.breakpoints.up('md'));
  const {
    searchInput,
    setSearchInput,
    page,
    pageSize,
    queryArgs,
    handlePageChange,
    handlePageSizeChange,
  } = usePaginatedSearch();
  const { data, isLoading, error, refetch } = useSearchCarsQuery(queryArgs, { refetchOnMountOrArgChange: true });
  const [deleteCar, { isLoading: isDeleting }] = useDeleteCarMutation();
  const [importCars, { isLoading: isImporting }] = useImportCarsMutation();
  const [importCarsCsv, { isLoading: isImportingCsv }] = useImportCarsCsvMutation();
  const [exportCarsTrigger, { isFetching: isExporting }] = useLazyExportCarsQuery();
  const [exportCarsCsvTrigger, { isFetching: isExportingCsv }] = useLazyExportCarsCsvTextQuery();
  const [importError, setImportError] = useState<string | null>(null);
  const [importSummary, setImportSummary] = useState<string | null>(null);
  const importJsonInputRef = useRef<HTMLInputElement | null>(null);
  const importCsvInputRef = useRef<HTMLInputElement | null>(null);

  const cars = useMemo(() => toCarRows(data?.items), [data?.items]);

  const handleDelete = async (id: string) => {
    await deleteCar({ id }).unwrap();
    await refetch();
  };

  const handleExport = async () => {
    const payload = await exportCarsTrigger().unwrap();
    const blob = new Blob([JSON.stringify(payload, null, 2)], { type: 'application/json' });
    const href = URL.createObjectURL(blob);
    const anchor = document.createElement('a');
    anchor.href = href;
    anchor.download = createExportFileName();
    anchor.click();
    URL.revokeObjectURL(href);
  };

  const handleExportCsv = async () => {
    const payload = await exportCarsCsvTrigger().unwrap();
    const blob = new Blob([payload], { type: 'text/csv;charset=utf-8' });
    const href = URL.createObjectURL(blob);
    const anchor = document.createElement('a');
    anchor.href = href;
    anchor.download = createExportCsvFileName();
    anchor.click();
    URL.revokeObjectURL(href);
  };

  const handleImportFile = async (event: ChangeEvent<HTMLInputElement>) => {
    const file = event.target.files?.[0];
    if (!file) {
      return;
    }

    try {
      setImportError(null);
      setImportSummary(null);
      const raw = await file.text();
      const parsed = JSON.parse(raw) as { items?: Car[] };
      const result = await importCars({ items: Array.isArray(parsed.items) ? parsed.items : [] }).unwrap();
      setImportSummary(
        `Import JSON termine: ${result.addedCount ?? 0} ajoutee(s), ${result.notAddedCount ?? 0} non ajoutee(s).`,
      );
      await refetch();
    } catch {
      setImportError('Le fichier JSON est invalide ou incompatible.');
    } finally {
      event.target.value = '';
    }
  };

  const handleImportCsvFile = async (event: ChangeEvent<HTMLInputElement>) => {
    const file = event.target.files?.[0];
    if (!file) {
      return;
    }

    try {
      setImportError(null);
      setImportSummary(null);
      const raw = await file.text();
      const result = await importCarsCsv(raw).unwrap();
      setImportSummary(
        `Import CSV termine: ${result.addedCount ?? 0} ajoutee(s), ${result.notAddedCount ?? 0} non ajoutee(s).`,
      );
      await refetch();
    } catch {
      setImportError('Le fichier CSV est invalide ou incompatible.');
    } finally {
      event.target.value = '';
    }
  };

  if (isLoading) return <div>Chargement...</div>;
  if (error) return <div>Erreur lors du chargement des voitures</div>;

  return (
    <Stack spacing={3} sx={{ p: { xs: 2, md: 3 } }}>
      <Stack direction={{ xs: 'column', sm: 'row' }} spacing={1.5} useFlexGap sx={{ alignItems: { sm: 'center' }, justifyContent: 'space-between' }}>
        <Box>
          <Typography variant="h4" component="h1">
            Voitures
          </Typography>
          <Typography variant="body2" color="text.secondary">
            Gere le parc des voitures disponibles pour la saisie des releves.
          </Typography>
        </Box>
        <Stack direction="row" spacing={1} useFlexGap sx={{ flexWrap: 'wrap' }}>
          <Chip label={`${data?.count ?? 0} voiture${(data?.count ?? 0) > 1 ? 's' : ''}`} color="primary" variant="outlined" />
          <Button variant="outlined" startIcon={<DownloadIcon />} onClick={handleExport} disabled={isExporting}>
            Export JSON
          </Button>
          <Button variant="outlined" startIcon={<DownloadIcon />} onClick={handleExportCsv} disabled={isExportingCsv}>
            Export CSV
          </Button>
          <Button variant="outlined" startIcon={<UploadFileIcon />} onClick={() => importJsonInputRef.current?.click()} disabled={isImporting}>
            Import JSON
          </Button>
          <Button variant="outlined" startIcon={<UploadFileIcon />} onClick={() => importCsvInputRef.current?.click()} disabled={isImportingCsv}>
            Import CSV
          </Button>
          <Button component={RouterLink} to="/car/create" variant="contained">
            Nouvelle voiture
          </Button>
          <input ref={importJsonInputRef} type="file" accept="application/json" hidden onChange={handleImportFile} />
          <input ref={importCsvInputRef} type="file" accept="text/csv,.csv" hidden onChange={handleImportCsvFile} />
        </Stack>
      </Stack>

      <TextField
        fullWidth
        label="Recherche"
        placeholder="Rechercher par nom, numéro d'immatriculation ou description"
        value={searchInput}
        onChange={(event) => setSearchInput(event.target.value)}
      />

      {importError && <Alert severity="error">{importError}</Alert>}
      {importSummary && <Alert severity="success">{importSummary}</Alert>}

      {cars.length === 0 && <Alert severity="info">Aucune voiture configuree.</Alert>}

      {isDesktop ? (
        <TableContainer component={Paper} variant="outlined">
          <Table>
            <TableHead>
              <TableRow>
                <TableCell>Nom</TableCell>
                <TableCell>Immatriculation</TableCell>
                <TableCell>Description</TableCell>
                <TableCell align="right">Actions</TableCell>
              </TableRow>
            </TableHead>
            <TableBody>
              {cars.map((car) => (
                <TableRow key={car.id} hover>
                  <TableCell>
                    <Typography sx={{ fontWeight: 600 }}>{car.name}</Typography>
                  </TableCell>
                  <TableCell>{car.vehicleRegistrationPlate || '-'}</TableCell>
                  <TableCell>{car.description || '-'}</TableCell>
                  <TableCell align="right">
                    <IconButton component={RouterLink} to={`/car/${car.id}/edit`} aria-label="Modifier la voiture">
                      <EditOutlinedIcon fontSize="small" />
                    </IconButton>
                    <IconButton aria-label="Supprimer la voiture" color="error" onClick={() => void handleDelete(car.id)} disabled={isDeleting}>
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
            onPageChange={handlePageChange}
            rowsPerPage={pageSize}
            onRowsPerPageChange={handlePageSizeChange}
            rowsPerPageOptions={[10, 20, 50]}
          />
        </TableContainer>
      ) : (
        <Stack spacing={2}>
          {cars.map((car) => (
            <Card key={car.id} variant="outlined">
              <CardContent>
                <Stack spacing={1}>
                  <Typography variant="h6">{car.name}</Typography>
                  <Typography variant="body2" color="text.secondary">
                    {car.vehicleRegistrationPlate || 'Aucune immatriculation'}
                  </Typography>
                  <Typography variant="body2" color="text.secondary">
                    {car.description || 'Aucune description'}
                  </Typography>
                </Stack>
              </CardContent>
              <CardActions sx={{ px: 2, pb: 2, pt: 0, gap: 1, flexWrap: 'wrap' }}>
                <Button component={RouterLink} to={`/car/${car.id}/edit`} size="small" variant="outlined">
                  Modifier
                </Button>
                <Button size="small" color="error" variant="outlined" onClick={() => void handleDelete(car.id)} disabled={isDeleting}>
                  Supprimer
                </Button>
              </CardActions>
            </Card>
          ))}
        </Stack>
      )}
    </Stack>
  );
};
