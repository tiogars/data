import { useEffect, useMemo, useRef, useState, type ChangeEvent, type FC } from 'react';
import { Link as RouterLink } from 'react-router-dom';
import Alert from '@mui/material/Alert';
import Box from '@mui/material/Box';
import Button from '@mui/material/Button';
import Card from '@mui/material/Card';
import CardActions from '@mui/material/CardActions';
import CardContent from '@mui/material/CardContent';
import Chip from '@mui/material/Chip';
import IconButton from '@mui/material/IconButton';
import MenuItem from '@mui/material/MenuItem';
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
import PrintIcon from '@mui/icons-material/Print';
import UploadFileIcon from '@mui/icons-material/UploadFile';
import { useListCarsQuery } from '../../../services/carApi';
import { useDeleteCarMileageMutation, useSearchCarMileagesQuery, type CarMileage } from '../../../services/carMileageApi';
import {
  useImportCarMileagesCsvMutation,
  useImportCarMileagesMutation,
  useLazyExportCarMileagesCsvTextQuery,
  useLazyExportCarMileagesQuery,
} from '../../../services/carMileageImportExportApi';
import type { CarMileageTablePageProps } from './CarMileageTablePage.types';

function createExportFileName() {
  const date = new Date();
  const yyyy = date.getFullYear();
  const mm = String(date.getMonth() + 1).padStart(2, '0');
  const dd = String(date.getDate()).padStart(2, '0');
  return `car-mileage-export-${yyyy}${mm}${dd}.json`;
}

function createExportCsvFileName() {
  const date = new Date();
  const yyyy = date.getFullYear();
  const mm = String(date.getMonth() + 1).padStart(2, '0');
  const dd = String(date.getDate()).padStart(2, '0');
  return `car-mileage-export-${yyyy}${mm}${dd}.csv`;
}

function escapeHtml(value: string): string {
  return value
    .replaceAll('&', '&amp;')
    .replaceAll('<', '&lt;')
    .replaceAll('>', '&gt;')
    .replaceAll('"', '&quot;')
    .replaceAll("'", '&#39;');
}

function printUnitFormHtml(): string {
  const date = new Date().toLocaleDateString('fr-FR');
  const fields = ['Voiture', 'Date et heure', 'Kilométrage (km)', 'Carburant ajouté (L)'];
  const fieldRows = fields
    .map((f) => `<div class="field"><label>${escapeHtml(f)} :</label><span class="line"></span></div>`)
    .join('');
  return `<!doctype html>
<html>
<head>
  <meta charset="utf-8" />
  <title>Formulaire de relevé kilométrique</title>
  <style>
    body { font-family: Arial, sans-serif; margin: 24px; }
    h1 { margin: 0 0 4px 0; font-size: 18px; }
    .meta { margin-bottom: 24px; color: #666; font-size: 12px; }
    .field { margin-bottom: 20px; }
    label { display: block; font-size: 13px; color: #333; margin-bottom: 4px; }
    .line { border: none; border-bottom: 1px solid #999; width: 100%; height: 28px; display: block; }
    .checkbox-row { display: flex; align-items: center; gap: 12px; margin-top: 4px; }
    .checkbox-box { width: 18px; height: 18px; border: 1px solid #999; display: inline-block; flex-shrink: 0; }
    @media print { body { margin: 12mm; } }
  </style>
</head>
<body>
  <h1>Relevé de kilométrage</h1>
  <div class="meta">Imprime le : ${escapeHtml(date)}</div>
  ${fieldRows}
  <div class="field">
    <label>Plein complet :</label>
    <div class="checkbox-row"><span class="checkbox-box"></span> Oui &nbsp;&nbsp; <span class="checkbox-box"></span> Non</div>
  </div>
</body>
</html>`;
}

function printListingFormHtml(rowCount = 20): string {
  const date = new Date().toLocaleDateString('fr-FR');
  const columns = ['Voiture', 'Date / heure', 'Kilométrage (km)', 'Carburant (L)', 'Plein complet'];
  const headerCells = columns.map((c) => `<th>${escapeHtml(c)}</th>`).join('');
  const emptyRow = `<tr>${columns.map(() => '<td>&nbsp;</td>').join('')}</tr>`;
  const emptyRows = Array.from({ length: rowCount }, () => emptyRow).join('');
  return `<!doctype html>
<html>
<head>
  <meta charset="utf-8" />
  <title>Listing - Relevés kilométriques</title>
  <style>
    body { font-family: Arial, sans-serif; margin: 24px; }
    h1 { margin: 0 0 4px 0; font-size: 18px; }
    .meta { margin-bottom: 16px; color: #666; font-size: 12px; }
    table { border-collapse: collapse; width: 100%; }
    th, td { border: 1px solid #999; padding: 8px; text-align: left; height: 28px; }
    th { background: #f5f5f5; font-size: 13px; }
    td { font-size: 12px; }
    @media print { body { margin: 12mm; } }
  </style>
</head>
<body>
  <h1>Listing - Relevés kilométriques</h1>
  <div class="meta">Imprime le : ${escapeHtml(date)}</div>
  <table>
    <thead><tr>${headerCells}</tr></thead>
    <tbody>${emptyRows}</tbody>
  </table>
</body>
</html>`;
}

export const CarMileageTablePage: FC<CarMileageTablePageProps> = () => {
  const theme = useTheme();
  const isDesktop = useMediaQuery(theme.breakpoints.up('md'));

  const { data: carsData, isLoading: carsLoading, error: carsError } = useListCarsQuery();
  const cars = useMemo(
    () => (carsData?.items ?? []).filter((car): car is { id: string; name: string } => Boolean(car.id && car.name)),
    [carsData?.items],
  );

  const [selectedCarId, setSelectedCarId] = useState('');
  const [page, setPage] = useState(0);
  const [pageSize, setPageSize] = useState(10);

  useEffect(() => {
    if (!selectedCarId && cars.length > 0) {
      setSelectedCarId(cars[0].id);
    }
  }, [cars, selectedCarId]);

  const { data, isLoading, error, refetch } = useSearchCarMileagesQuery(
    {
      carId: selectedCarId || undefined,
      page,
      size: pageSize,
    },
    { skip: !selectedCarId },
  );

  const [deleteCarMileage, { isLoading: isDeleting }] = useDeleteCarMileageMutation();
  const [importCarMileages, { isLoading: isImporting }] = useImportCarMileagesMutation();
  const [importCarMileagesCsv, { isLoading: isImportingCsv }] = useImportCarMileagesCsvMutation();
  const [exportCarMileagesTrigger, { isFetching: isExporting }] = useLazyExportCarMileagesQuery();
  const [exportCarMileagesCsvTrigger, { isFetching: isExportingCsv }] = useLazyExportCarMileagesCsvTextQuery();
  const [importError, setImportError] = useState<string | null>(null);
  const [importSummary, setImportSummary] = useState<string | null>(null);
  const importJsonInputRef = useRef<HTMLInputElement | null>(null);
  const importCsvInputRef = useRef<HTMLInputElement | null>(null);

  const handleDelete = async (id: string) => {
    await deleteCarMileage({ id }).unwrap();
    await refetch();
  };

  const handleExport = async () => {
    const payload = await exportCarMileagesTrigger().unwrap();
    const blob = new Blob([JSON.stringify(payload, null, 2)], { type: 'application/json' });
    const href = URL.createObjectURL(blob);
    const anchor = document.createElement('a');
    anchor.href = href;
    anchor.download = createExportFileName();
    anchor.click();
    URL.revokeObjectURL(href);
  };

  const handleExportCsv = async () => {
    const payload = await exportCarMileagesCsvTrigger().unwrap();
    const blob = new Blob([payload], { type: 'text/csv;charset=utf-8' });
    const href = URL.createObjectURL(blob);
    const anchor = document.createElement('a');
    anchor.href = href;
    anchor.download = createExportCsvFileName();
    anchor.click();
    URL.revokeObjectURL(href);
  };

  const handlePrintUnitForm = () => {
    const html = printUnitFormHtml();
    const blob = new Blob([html], { type: 'text/html' });
    const url = URL.createObjectURL(blob);
    window.open(url, '_blank');
  };

  const handlePrintListing = () => {
    const html = printListingFormHtml();
    const blob = new Blob([html], { type: 'text/html' });
    const url = URL.createObjectURL(blob);
    window.open(url, '_blank');
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
      const parsed = JSON.parse(raw) as { items?: CarMileage[] };
      const result = await importCarMileages({ items: Array.isArray(parsed.items) ? parsed.items : [] }).unwrap();
      setImportSummary(
        `Import JSON termine: ${result.addedCount ?? 0} ajoute(s), ${result.notAddedCount ?? 0} non ajoute(s).`,
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
      const result = await importCarMileagesCsv(raw).unwrap();
      setImportSummary(
        `Import CSV termine: ${result.addedCount ?? 0} ajoute(s), ${result.notAddedCount ?? 0} non ajoute(s).`,
      );
      await refetch();
    } catch {
      setImportError('Le fichier CSV est invalide ou incompatible.');
    } finally {
      event.target.value = '';
    }
  };

  if (carsLoading) return <div>Chargement...</div>;
  if (carsError) return <div>Erreur lors du chargement des voitures</div>;

  return (
    <Stack spacing={3} sx={{ p: { xs: 2, md: 3 } }}>
      <Stack direction={{ xs: 'column', sm: 'row' }} spacing={1.5} useFlexGap sx={{ alignItems: { sm: 'center' }, justifyContent: 'space-between' }}>
        <Box>
          <Typography variant="h4" component="h1">
            Tableau de saisie
          </Typography>
          <Typography variant="body2" color="text.secondary">
            Saisissez et suivez les releves de kilometrage par voiture.
          </Typography>
        </Box>
        <Stack direction="row" spacing={1} useFlexGap sx={{ flexWrap: 'wrap' }}>
          <Chip label={`${data?.count ?? 0} releve${(data?.count ?? 0) > 1 ? 's' : ''}`} color="primary" variant="outlined" />
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
          <Button variant="outlined" startIcon={<PrintIcon />} onClick={handlePrintUnitForm}>
            Formulaire vierge
          </Button>
          <Button variant="outlined" startIcon={<PrintIcon />} onClick={handlePrintListing}>
            Listing vierge
          </Button>
          <Button component={RouterLink} to="/car-mileage/form" variant="contained">
            Nouveau releve
          </Button>
          <input ref={importJsonInputRef} type="file" accept="application/json" hidden onChange={handleImportFile} />
          <input ref={importCsvInputRef} type="file" accept="text/csv,.csv" hidden onChange={handleImportCsvFile} />
        </Stack>
      </Stack>

      {cars.length === 0 && <Alert severity="info">Ajoutez d'abord une voiture dans "Voitures".</Alert>}

      {importError && <Alert severity="error">{importError}</Alert>}
      {importSummary && <Alert severity="success">{importSummary}</Alert>}

      {cars.length > 0 && (
        <TextField
          select
          fullWidth
          label="Voiture"
          value={selectedCarId}
          onChange={(event) => {
            setSelectedCarId(event.target.value);
            setPage(0);
          }}
        >
          {cars.map((car) => (
            <MenuItem key={car.id} value={car.id}>
              {car.name}
            </MenuItem>
          ))}
        </TextField>
      )}

      {isLoading && <Typography>Chargement des releves...</Typography>}
      {error && <Alert severity="error">Erreur lors du chargement des releves.</Alert>}

      {!isLoading && !error && (data?.items?.length ?? 0) === 0 && selectedCarId && (
        <Alert severity="info">Aucun releve pour la voiture selectionnee.</Alert>
      )}

      {!isLoading && !error && (data?.items?.length ?? 0) > 0 && (
        isDesktop ? (
          <TableContainer component={Paper} variant="outlined">
            <Table>
              <TableHead>
                <TableRow>
                  <TableCell>Date/heure</TableCell>
                  <TableCell align="right">Kilometrage</TableCell>
                  <TableCell align="right">Carburant (L)</TableCell>
                  <TableCell>Plein complet</TableCell>
                  <TableCell align="right">Actions</TableCell>
                </TableRow>
              </TableHead>
              <TableBody>
                {(data?.items ?? []).map((item) => (
                  <TableRow key={item.id} hover>
                    <TableCell>{item.readingAt ? new Date(item.readingAt).toLocaleString('fr-FR') : '-'}</TableCell>
                    <TableCell align="right">{item.odometerKm ?? '-'}</TableCell>
                    <TableCell align="right">{item.fuelVolumeLiters ?? '-'}</TableCell>
                    <TableCell>{item.fullTank ? 'Oui' : 'Non'}</TableCell>
                    <TableCell align="right">
                      <IconButton aria-label="Supprimer le releve" color="error" onClick={() => item.id && void handleDelete(item.id)} disabled={isDeleting}>
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
            {(data?.items ?? []).map((item) => (
              <Card key={item.id} variant="outlined">
                <CardContent>
                  <Stack spacing={1}>
                    <Typography variant="subtitle1" sx={{ fontWeight: 600 }}>
                      {item.readingAt ? new Date(item.readingAt).toLocaleString('fr-FR') : '-'}
                    </Typography>
                    <Typography variant="body2">Kilometrage: {item.odometerKm ?? '-'}</Typography>
                    <Typography variant="body2">Carburant (L): {item.fuelVolumeLiters ?? '-'}</Typography>
                    <Typography variant="body2">Plein complet: {item.fullTank ? 'Oui' : 'Non'}</Typography>
                  </Stack>
                </CardContent>
                <CardActions sx={{ px: 2, pb: 2, pt: 0 }}>
                  <Button size="small" color="error" variant="outlined" onClick={() => item.id && void handleDelete(item.id)} disabled={isDeleting}>
                    Supprimer
                  </Button>
                </CardActions>
              </Card>
            ))}
          </Stack>
        )
      )}
    </Stack>
  );
};
