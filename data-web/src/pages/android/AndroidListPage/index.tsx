import { useMemo, useRef, useState, type ChangeEvent, type FC } from 'react';
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
import TextField from '@mui/material/TextField';
import Typography from '@mui/material/Typography';
import useMediaQuery from '@mui/material/useMediaQuery';
import { useTheme } from '@mui/material/styles';
import DeleteIcon from '@mui/icons-material/Delete';
import DownloadIcon from '@mui/icons-material/Download';
import EditOutlinedIcon from '@mui/icons-material/EditOutlined';
import OpenInNewIcon from '@mui/icons-material/OpenInNew';
import PrintIcon from '@mui/icons-material/Print';
import UploadFileIcon from '@mui/icons-material/UploadFile';
import VisibilityOutlinedIcon from '@mui/icons-material/VisibilityOutlined';
import {
  type Android,
  useDeleteAllAndroidsMutation,
  useDeleteAndroidMutation,
  useImportAndroidsMutation,
  useImportAndroidsCsvMutation,
  useListAndroidsQuery,
} from '../../../services/androidApi';
import { androidApi } from '../../../services/androidApi';
import { useLazyExportAndroidsCsvTextQuery } from '../../../services/androidCsvApi';
import type { AndroidListPageProps } from './AndroidListPage.types';

type AndroidRow = Android & { id: string };

function toAndroidRows(items: Android[] | undefined): AndroidRow[] {
  return (items ?? []).filter((item): item is AndroidRow => Boolean(item.id));
}

function createExportFileName() {
  const date = new Date();
  const yyyy = date.getFullYear();
  const mm = String(date.getMonth() + 1).padStart(2, '0');
  const dd = String(date.getDate()).padStart(2, '0');
  return `android-export-${yyyy}${mm}${dd}.json`;
}

function createExportCsvFileName() {
  const date = new Date();
  const yyyy = date.getFullYear();
  const mm = String(date.getMonth() + 1).padStart(2, '0');
  const dd = String(date.getDate()).padStart(2, '0');
  return `android-export-${yyyy}${mm}${dd}.csv`;
}

function containsIgnoreCase(value: string | undefined, filter: string): boolean {
  if (!filter.trim()) return true;
  if (!value) return false;
  return value.toLowerCase().includes(filter.trim().toLowerCase());
}

function containsCategoryIgnoreCase(categories: string[] | undefined, filter: string): boolean {
  if (!filter.trim()) return true;
  if (!categories?.length) return false;
  return categories.some((category) => containsIgnoreCase(category, filter));
}

function buildGooglePlayStoreUrl(packageName: string | undefined): string | null {
  if (!packageName?.trim()) {
    return null;
  }

  return `https://play.google.com/store/apps/details?id=${encodeURIComponent(packageName.trim())}`;
}

function escapeHtml(value: string): string {
  return value
    .replaceAll('&', '&amp;')
    .replaceAll('<', '&lt;')
    .replaceAll('>', '&gt;')
    .replaceAll('"', '&quot;')
    .replaceAll("'", '&#39;');
}

function printHtml(rows: AndroidRow[], title: string, generatedAt?: string, total?: number) {
  const bodyRows = rows
    .map(
      (item) => `<tr><td>${escapeHtml(item.name ?? '')}</td><td>${escapeHtml(item.packageName ?? '')}</td><td>${escapeHtml((item.category ?? []).join(', '))}</td><td>${escapeHtml(item.description ?? '')}</td><td>${escapeHtml(item.id)}</td></tr>`
    )
    .join('');

  return `<!doctype html>
<html>
<head>
  <meta charset="utf-8" />
  <title>${escapeHtml(title)}</title>
  <style>
    body { font-family: Arial, sans-serif; margin: 24px; }
    h1 { margin: 0 0 8px 0; }
    .meta { margin-bottom: 16px; color: #444; }
    table { border-collapse: collapse; width: 100%; }
    th, td { border: 1px solid #ccc; padding: 8px; text-align: left; vertical-align: top; }
    th { background: #f5f5f5; }
  </style>
</head>
<body>
  <h1>${escapeHtml(title)}</h1>
  <div class="meta">Genere le: ${escapeHtml(generatedAt ?? '-')} | Elements imprimes: ${rows.length} | Total: ${total ?? rows.length}</div>
  <table>
    <thead>
      <tr><th>Nom</th><th>Package</th><th>Categories</th><th>Description</th><th>Identifiant</th></tr>
    </thead>
    <tbody>${bodyRows}</tbody>
  </table>
</body>
</html>`;
}

export const AndroidListPage: FC<AndroidListPageProps> = () => {
  const theme = useTheme();
  const isDesktop = useMediaQuery(theme.breakpoints.up('md'));
  const { data, isLoading, error, refetch } = useListAndroidsQuery(undefined, { refetchOnMountOrArgChange: true });
  const [deleteAndroidById, { isLoading: isDeleting }] = useDeleteAndroidMutation();
  const [deleteAllAndroids, { isLoading: isDeletingAllAndroids }] = useDeleteAllAndroidsMutation();
  const [importAndroids, { isLoading: isImporting }] = useImportAndroidsMutation();
  const [importAndroidsCsv, { isLoading: isImportingCsv }] = useImportAndroidsCsvMutation();
  const [exportAndroidsTrigger, { isFetching: isExporting }] = androidApi.useLazyExportAndroidsQuery();
  const [printAndroidsTrigger, { isFetching: isPrinting }] = androidApi.useLazyPrintAndroidsQuery();
  const [exportAndroidsCsvTrigger, { isFetching: isExportingCsv }] = useLazyExportAndroidsCsvTextQuery();
  const [androidToDelete, setAndroidToDelete] = useState<AndroidRow | null>(null);
  const [confirmDeleteAllAndroidsOpen, setConfirmDeleteAllAndroidsOpen] = useState(false);
  const [importError, setImportError] = useState<string | null>(null);
  const [importDuplicates, setImportDuplicates] = useState<string[]>([]);
  const [nameFilter, setNameFilter] = useState('');
  const [packageNameFilter, setPackageNameFilter] = useState('');
  const [categoryFilter, setCategoryFilter] = useState('');
  const [descriptionFilter, setDescriptionFilter] = useState('');
  const jsonInputRef = useRef<HTMLInputElement | null>(null);
  const csvInputRef = useRef<HTMLInputElement | null>(null);

  const androids = useMemo(() => toAndroidRows(data?.items), [data?.items]);
  const filteredAndroids = useMemo(
    () => androids
      .filter((item) => containsIgnoreCase(item.name, nameFilter))
      .filter((item) => containsIgnoreCase(item.packageName, packageNameFilter))
      .filter((item) => containsCategoryIgnoreCase(item.category, categoryFilter))
      .filter((item) => containsIgnoreCase(item.description, descriptionFilter)),
    [androids, nameFilter, packageNameFilter, categoryFilter, descriptionFilter]
  );

  const handleDelete = async () => {
    if (!androidToDelete) return;

    await deleteAndroidById({ id: androidToDelete.id }).unwrap();
    setAndroidToDelete(null);
    await refetch();
  };

  const handleDeleteAllAndroids = async () => {
    await deleteAllAndroids().unwrap();
    await refetch();
    setConfirmDeleteAllAndroidsOpen(false);
  };

  const handleExport = async () => {
    const payload = await exportAndroidsTrigger().unwrap();
    const blob = new Blob([JSON.stringify(payload, null, 2)], { type: 'application/json' });
    const href = URL.createObjectURL(blob);
    const anchor = document.createElement('a');
    anchor.href = href;
    anchor.download = createExportFileName();
    anchor.click();
    URL.revokeObjectURL(href);
  };

  const handleExportCsv = async () => {
    const payload = await exportAndroidsCsvTrigger().unwrap();
    const blob = new Blob([payload], { type: 'text/csv;charset=utf-8' });
    const href = URL.createObjectURL(blob);
    const anchor = document.createElement('a');
    anchor.href = href;
    anchor.download = createExportCsvFileName();
    anchor.click();
    URL.revokeObjectURL(href);
  };

  const handlePrint = async (mode: 'filtered' | 'all') => {
    const payload = await printAndroidsTrigger({
      mode,
      name: mode === 'filtered' ? nameFilter : undefined,
      packageName: mode === 'filtered' ? packageNameFilter : undefined,
      category: mode === 'filtered' ? categoryFilter : undefined,
      description: mode === 'filtered' ? descriptionFilter : undefined,
    }).unwrap();

    const rows = toAndroidRows(payload.items);
    const html = printHtml(
      rows,
      mode === 'all' ? 'Impression - Toutes les applications Android' : 'Impression - Applications Android filtrees',
      payload.generatedAt,
      payload.total
    );
    const blob = new Blob([html], { type: 'text/html' });
    const url = URL.createObjectURL(blob);
    const win = window.open(url, '_blank', 'noopener,noreferrer,width=960,height=700');
    if (!win) {
      URL.revokeObjectURL(url);
      return;
    }

    const revoke = () => URL.revokeObjectURL(url);
    win.addEventListener('load', () => {
      win.focus();
      win.print();
      revoke();
    }, { once: true });
  };

  const handleImportJson = async (event: ChangeEvent<HTMLInputElement>) => {
    const file = event.target.files?.[0];

    if (!file) {
      return;
    }

    try {
      setImportError(null);
      setImportDuplicates([]);
      const raw = await file.text();
      const parsed = JSON.parse(raw) as { items?: Android[] } | Android[];
      let items: Android[] | null = null;
      if (Array.isArray(parsed)) {
        items = parsed;
      } else if (Array.isArray(parsed.items)) {
        items = parsed.items;
      }

      if (!items) {
        setImportError("Le JSON doit contenir une liste d'applications Android (tableau ou champ items).");
        return;
      }

      const result = await importAndroids({
        androidImportForm: {
          items,
        },
      }).unwrap();

      if (result.duplicatePackageNames && result.duplicatePackageNames.length > 0) {
        setImportDuplicates(result.duplicatePackageNames);
      }

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
      setImportDuplicates([]);
      const raw = await file.text();
      const result = await importAndroidsCsv({ body: raw }).unwrap();
      if (result.duplicatePackageNames && result.duplicatePackageNames.length > 0) {
        setImportDuplicates(result.duplicatePackageNames);
      }
      await refetch();
    } catch {
      setImportError('Le fichier CSV est invalide ou incompatible.');
    } finally {
      event.target.value = '';
    }
  };

  if (isLoading) return <div>Chargement...</div>;
  if (error) return <div>Erreur lors du chargement des applications Android</div>;

  return (
    <Stack spacing={3} sx={{ p: { xs: 2, md: 3 } }}>
      <Stack direction={{ xs: 'column', sm: 'row' }} spacing={1.5} useFlexGap sx={{ alignItems: { sm: 'center' }, justifyContent: 'space-between' }}>
        <Box>
          <Typography variant="h4" component="h1">
            Android
          </Typography>
          <Typography variant="body2" color="text.secondary">
            Gere les applications Android avec import/export JSON et CSV, plus impression.
          </Typography>
        </Box>
        <Stack direction="row" spacing={1} useFlexGap sx={{ flexWrap: 'wrap' }}>
          <Chip label={`${filteredAndroids.length}/${data?.count ?? 0} application${(data?.count ?? 0) > 1 ? 's' : ''}`} color="primary" variant="outlined" />
          <Button variant="outlined" startIcon={<DownloadIcon />} onClick={handleExport} disabled={isExporting}>
            Export JSON
          </Button>
          <Button variant="outlined" startIcon={<DownloadIcon />} onClick={handleExportCsv} disabled={isExportingCsv}>
            Export CSV
          </Button>
          <Button variant="outlined" startIcon={<UploadFileIcon />} onClick={() => jsonInputRef.current?.click()} disabled={isImporting}>
            Import JSON
          </Button>
          <Button variant="outlined" startIcon={<UploadFileIcon />} onClick={() => csvInputRef.current?.click()} disabled={isImporting || isImportingCsv}>
            Import CSV
          </Button>
          <Button variant="outlined" startIcon={<PrintIcon />} onClick={() => handlePrint('filtered')} disabled={isPrinting}>
            Imprimer filtres
          </Button>
          <Button variant="outlined" startIcon={<PrintIcon />} onClick={() => handlePrint('all')} disabled={isPrinting}>
            Imprimer tout
          </Button>
          <Button
            variant="outlined"
            color="error"
            startIcon={<DeleteIcon />}
            onClick={() => setConfirmDeleteAllAndroidsOpen(true)}
            disabled={androids.length === 0 || isDeletingAllAndroids}
          >
            Tout supprimer
          </Button>
          <Button component={RouterLink} to="/android/create" variant="contained">
            Nouvelle application
          </Button>
          <input ref={jsonInputRef} type="file" accept="application/json,.json" hidden onChange={handleImportJson} />
          <input ref={csvInputRef} type="file" accept="text/csv,.csv,text/plain" hidden onChange={handleImportCsvFile} />
        </Stack>
      </Stack>

      <Stack direction={{ xs: 'column', md: 'row' }} spacing={1.5}>
        <TextField label="Filtre nom" value={nameFilter} onChange={(event) => setNameFilter(event.target.value)} fullWidth />
        <TextField label="Filtre package" value={packageNameFilter} onChange={(event) => setPackageNameFilter(event.target.value)} fullWidth />
        <TextField label="Filtre categorie" value={categoryFilter} onChange={(event) => setCategoryFilter(event.target.value)} fullWidth />
        <TextField label="Filtre description" value={descriptionFilter} onChange={(event) => setDescriptionFilter(event.target.value)} fullWidth />
      </Stack>

      {importError && <Alert severity="error">{importError}</Alert>}
      {importDuplicates.length > 0 && (
        <Alert severity="warning">
          {importDuplicates.length} package{importDuplicates.length > 1 ? 's' : ''} en doublon ignore{importDuplicates.length > 1 ? 's' : ''} : {importDuplicates.join(', ')}
        </Alert>
      )}

      {filteredAndroids.length === 0 && (
        <Alert severity="info">
          Aucune application Android trouvee.
        </Alert>
      )}

      {isDesktop ? (
        <TableContainer component={Paper} variant="outlined">
          <Table>
            <TableHead>
              <TableRow>
                <TableCell>Nom</TableCell>
                <TableCell>Package</TableCell>
                <TableCell>Categories</TableCell>
                <TableCell>Description</TableCell>
                <TableCell>Play Store</TableCell>
                <TableCell align="right">Actions</TableCell>
              </TableRow>
            </TableHead>
            <TableBody>
              {filteredAndroids.map((android) => {
                const playStoreUrl = buildGooglePlayStoreUrl(android.packageName);

                return (
                  <TableRow key={android.id} hover>
                    <TableCell>
                      <Typography sx={{ fontWeight: 600 }}>{android.name}</Typography>
                    </TableCell>
                    <TableCell>{android.packageName || '-'}</TableCell>
                    <TableCell>
                      <Stack direction="row" spacing={0.5} useFlexGap sx={{ flexWrap: 'wrap' }}>
                        {(android.category ?? []).length > 0 ? android.category?.map((category) => (
                          <Chip key={category} label={category} size="small" variant="outlined" />
                        )) : '-'}
                      </Stack>
                    </TableCell>
                    <TableCell>{android.description || '-'}</TableCell>
                    <TableCell>
                      {playStoreUrl ? (
                        <Button
                          component="a"
                          href={playStoreUrl}
                          target="_blank"
                          rel="noopener noreferrer"
                          size="small"
                          variant="outlined"
                          endIcon={<OpenInNewIcon fontSize="small" />}
                        >
                          Ouvrir
                        </Button>
                      ) : '-'}
                    </TableCell>
                    <TableCell align="right">
                      <IconButton component={RouterLink} to={`/android/${android.id}`} aria-label="Voir l'application Android">
                        <VisibilityOutlinedIcon fontSize="small" />
                      </IconButton>
                      <IconButton component={RouterLink} to={`/android/${android.id}/edit`} aria-label="Modifier l'application Android">
                        <EditOutlinedIcon fontSize="small" />
                      </IconButton>
                      <IconButton aria-label="Supprimer l'application Android" color="error" onClick={() => setAndroidToDelete(android)}>
                        <DeleteIcon fontSize="small" />
                      </IconButton>
                    </TableCell>
                  </TableRow>
                );
              })}
            </TableBody>
          </Table>
        </TableContainer>
      ) : (
        <Stack spacing={2}>
          {filteredAndroids.map((android) => {
            const playStoreUrl = buildGooglePlayStoreUrl(android.packageName);

            return (
              <Card key={android.id} variant="outlined">
                <CardContent>
                  <Stack spacing={1}>
                    <Typography variant="h6">{android.name}</Typography>
                    <Typography variant="body2" color="text.secondary">
                      {android.packageName || 'Package inconnu'}
                    </Typography>
                    <Stack direction="row" spacing={0.5} useFlexGap sx={{ flexWrap: 'wrap' }}>
                      {(android.category ?? []).length > 0 ? android.category?.map((category) => (
                        <Chip key={category} label={category} size="small" variant="outlined" />
                      )) : <Chip label="Aucune categorie" size="small" variant="outlined" />}
                    </Stack>
                    <Typography variant="body2" color="text.secondary">
                      {android.description || 'Aucune description'}
                    </Typography>
                  </Stack>
                </CardContent>
                <CardActions sx={{ px: 2, pb: 2, pt: 0, gap: 1, flexWrap: 'wrap' }}>
                  <Button component={RouterLink} to={`/android/${android.id}`} size="small" variant="outlined">
                    Voir
                  </Button>
                  <Button component={RouterLink} to={`/android/${android.id}/edit`} size="small" variant="outlined">
                    Modifier
                  </Button>
                  {playStoreUrl && (
                    <Button
                      component="a"
                      size="small"
                      variant="outlined"
                      href={playStoreUrl}
                      target="_blank"
                      rel="noopener noreferrer"
                      endIcon={<OpenInNewIcon fontSize="small" />}
                    >
                      Play Store
                    </Button>
                  )}
                  <Button size="small" color="error" variant="outlined" onClick={() => setAndroidToDelete(android)}>
                    Supprimer
                  </Button>
                </CardActions>
              </Card>
            );
          })}
        </Stack>
      )}

      <Dialog open={Boolean(androidToDelete)} onClose={() => setAndroidToDelete(null)}>
        <DialogTitle>Supprimer l'application Android</DialogTitle>
        <DialogContent>
          <DialogContentText>
            Voulez-vous vraiment supprimer {androidToDelete?.name || 'cette application Android'} ?
          </DialogContentText>
        </DialogContent>
        <DialogActions>
          <Button onClick={() => setAndroidToDelete(null)} disabled={isDeleting}>Annuler</Button>
          <Button color="error" onClick={handleDelete} disabled={isDeleting}>Supprimer</Button>
        </DialogActions>
      </Dialog>

      <Dialog open={confirmDeleteAllAndroidsOpen} onClose={() => setConfirmDeleteAllAndroidsOpen(false)}>
        <DialogTitle>Supprimer toutes les applications Android</DialogTitle>
        <DialogContent>
          <DialogContentText>
            Cette action va supprimer toutes les applications Android ({androids.length}). Voulez-vous continuer ?
          </DialogContentText>
        </DialogContent>
        <DialogActions>
          <Button onClick={() => setConfirmDeleteAllAndroidsOpen(false)} disabled={isDeletingAllAndroids}>Annuler</Button>
          <Button color="error" onClick={handleDeleteAllAndroids} disabled={isDeletingAllAndroids}>Tout supprimer</Button>
        </DialogActions>
      </Dialog>
    </Stack>
  );
};