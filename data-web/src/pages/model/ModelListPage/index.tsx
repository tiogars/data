import { useMemo, useRef, useState, type ChangeEvent, type FC } from 'react';
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
import DownloadIcon from '@mui/icons-material/Download';
import PrintIcon from '@mui/icons-material/Print';
import UploadFileIcon from '@mui/icons-material/UploadFile';
import { ResponsiveCrudList, type CrudListColumn } from '../../../components/ResponsiveCrudList';
import {
  type Model,
  useImportModelsMutation,
  useSearchModelsQuery,
} from '../../../services/modelApi';
import { modelApi, useDeleteModelMutation } from '../../../services/modelApi';
import type { ModelListPageProps } from './ModelListPage.types';

type ModelRow = Model & { id: string };

function toModelRows(items: Model[] | undefined): ModelRow[] {
  return (items ?? []).filter((item): item is ModelRow => Boolean(item.id));
}

const modelColumns: CrudListColumn<ModelRow>[] = [
  {
    key: 'name',
    header: 'Nom',
    render: (model) => <Typography sx={{ fontWeight: 600 }}>{model.name}</Typography>,
  },
  {
    key: 'description',
    header: 'Description',
    render: (model) => model.description || '-',
  },
];

function createExportFileName() {
  const date = new Date();
  const yyyy = date.getFullYear();
  const mm = String(date.getMonth() + 1).padStart(2, '0');
  const dd = String(date.getDate()).padStart(2, '0');
  return `model-export-${yyyy}${mm}${dd}.json`;
}

function escapeHtml(value: string): string {
  return value
    .replaceAll('&', '&amp;')
    .replaceAll('<', '&lt;')
    .replaceAll('>', '&gt;')
    .replaceAll('"', '&quot;')
    .replaceAll("'", '&#39;');
}

function printHtml(rows: ModelRow[], title: string, generatedAt?: string, total?: number) {
  const bodyRows = rows
    .map(
      (item) => `<tr><td>${escapeHtml(item.name ?? '')}</td><td>${escapeHtml(item.description ?? '')}</td><td>${escapeHtml(item.id)}</td></tr>`
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
    th, td { border: 1px solid #ccc; padding: 8px; text-align: left; }
    th { background: #f5f5f5; }
  </style>
</head>
<body>
  <h1>${escapeHtml(title)}</h1>
  <div class="meta">Genere le: ${escapeHtml(generatedAt ?? '-')} | Elements imprimes: ${rows.length} | Total: ${total ?? rows.length}</div>
  <table>
    <thead>
      <tr><th>Nom</th><th>Description</th><th>Identifiant</th></tr>
    </thead>
    <tbody>${bodyRows}</tbody>
  </table>
</body>
</html>`;
}

export const ModelListPage: FC<ModelListPageProps> = () => {
  const [page, setPage] = useState(0);
  const [pageSize, setPageSize] = useState(10);
  const [deleteModelById, { isLoading: isDeleting }] = useDeleteModelMutation();
  const [importModels, { isLoading: isImporting }] = useImportModelsMutation();
  const [exportModelsTrigger, { isFetching: isExporting }] = modelApi.useLazyExportModelsQuery();
  const [printModelsTrigger, { isFetching: isPrinting }] = modelApi.useLazyPrintModelsQuery();
  const [modelToDelete, setModelToDelete] = useState<ModelRow | null>(null);
  const [importError, setImportError] = useState<string | null>(null);
  const [importDuplicates, setImportDuplicates] = useState<string[]>([]);
  const [nameFilter, setNameFilter] = useState('');
  const [descriptionFilter, setDescriptionFilter] = useState('');
  const searchQuery = useMemo(() => [nameFilter, descriptionFilter].map((value) => value.trim()).filter(Boolean).join(' '), [nameFilter, descriptionFilter]);
  const queryArgs = useMemo(() => ({
    page,
    size: pageSize,
    q: searchQuery || undefined,
  }), [page, pageSize, searchQuery]);
  const { data, isLoading, error, refetch } = useSearchModelsQuery(queryArgs, { refetchOnMountOrArgChange: true });
  const fileInputRef = useRef<HTMLInputElement | null>(null);

  const models = useMemo(() => toModelRows(data?.items), [data?.items]);
  const filteredModels = models;

  const handleDelete = async () => {
    if (!modelToDelete) return;

    await deleteModelById({ id: modelToDelete.id }).unwrap();
    setModelToDelete(null);
    await refetch();
  };

  const handleExport = async () => {
    const payload = await exportModelsTrigger().unwrap();
    const blob = new Blob([JSON.stringify(payload, null, 2)], { type: 'application/json' });
    const href = URL.createObjectURL(blob);
    const anchor = document.createElement('a');
    anchor.href = href;
    anchor.download = createExportFileName();
    anchor.click();
    URL.revokeObjectURL(href);
  };

  const handlePrint = async (mode: 'filtered' | 'all') => {
    const payload = await printModelsTrigger({
      mode,
      name: mode === 'filtered' ? nameFilter : undefined,
      description: mode === 'filtered' ? descriptionFilter : undefined,
    }).unwrap();

    const rows = toModelRows(payload.items);
    const html = printHtml(
      rows,
      mode === 'all' ? 'Impression - Tous les modeles' : 'Impression - Modeles filtres',
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

  const handleImportFile = async (event: ChangeEvent<HTMLInputElement>) => {
    const file = event.target.files?.[0];

    if (!file) {
      return;
    }

    try {
      setImportError(null);
      setImportDuplicates([]);
      const raw = await file.text();
      const parsed = JSON.parse(raw) as { items?: Model[] };

      const result = await importModels({
        modelImportForm: {
          items: Array.isArray(parsed.items) ? parsed.items : [],
        },
      }).unwrap();

      if (result.duplicateNames && result.duplicateNames.length > 0) {
        setImportDuplicates(result.duplicateNames);
      }

      await refetch();
    } catch {
      setImportError('Le fichier JSON est invalide ou incompatible.');
    } finally {
      event.target.value = '';
    }
  };

  if (isLoading) return <div>Chargement...</div>;
  if (error) return <div>Erreur lors du chargement des modeles</div>;

  return (
    <Stack spacing={3} sx={{ p: { xs: 2, md: 3 } }}>
      <Stack direction={{ xs: 'column', sm: 'row' }} spacing={1.5} useFlexGap sx={{ alignItems: { sm: 'center' }, justifyContent: 'space-between' }}>
        <Box>
          <Typography variant="h4" component="h1">
            Modeles
          </Typography>
          <Typography variant="body2" color="text.secondary">
            Gere les modeles de donnees avec import/export JSON et impression.
          </Typography>
        </Box>
        <Stack direction="row" spacing={1} useFlexGap sx={{ flexWrap: 'wrap' }}>
          <Chip label={`${filteredModels.length}/${data?.count ?? 0} modele${(data?.count ?? 0) > 1 ? 's' : ''}`} color="primary" variant="outlined" />
          <Button variant="outlined" startIcon={<DownloadIcon />} onClick={handleExport} disabled={isExporting}>
            Export JSON
          </Button>
          <Button variant="outlined" startIcon={<UploadFileIcon />} onClick={() => fileInputRef.current?.click()} disabled={isImporting}>
            Import JSON
          </Button>
          <Button variant="outlined" startIcon={<PrintIcon />} onClick={() => handlePrint('filtered')} disabled={isPrinting}>
            Imprimer filtres
          </Button>
          <Button variant="outlined" startIcon={<PrintIcon />} onClick={() => handlePrint('all')} disabled={isPrinting}>
            Imprimer tout
          </Button>
          <Button component={RouterLink} to="/model/create" variant="contained">
            Nouveau modele
          </Button>
          <input
            ref={fileInputRef}
            type="file"
            accept="application/json"
            hidden
            onChange={handleImportFile}
          />
        </Stack>
      </Stack>

      <Stack direction={{ xs: 'column', md: 'row' }} spacing={1.5}>
        <TextField
          label="Filtre nom"
          value={nameFilter}
          onChange={(event) => {
            setNameFilter(event.target.value);
            setPage(0);
          }}
          fullWidth
        />
        <TextField
          label="Filtre description"
          value={descriptionFilter}
          onChange={(event) => {
            setDescriptionFilter(event.target.value);
            setPage(0);
          }}
          fullWidth
        />
      </Stack>

      {importError && <Alert severity="error">{importError}</Alert>}
      {importDuplicates.length > 0 && (
        <Alert severity="warning">
          {importDuplicates.length} nom{importDuplicates.length > 1 ? 's' : ''} en doublon ignore{importDuplicates.length > 1 ? 's' : ''} : {importDuplicates.join(', ')}
        </Alert>
      )}

      {filteredModels.length === 0 && (
        <Alert severity="info">
          Aucun modele trouve.
        </Alert>
      )}

      <ResponsiveCrudList
        items={filteredModels}
        getRowKey={(model) => model.id}
        columns={modelColumns}
        getDetailPath={(model) => `/model/${model.id}`}
        getEditPath={(model) => `/model/${model.id}/edit`}
        onDelete={setModelToDelete}
        actionLabels={{
          view: 'Voir le modele',
          edit: 'Modifier le modele',
          remove: 'Supprimer le modele',
        }}
        renderCardTitle={(model) => model.name}
        renderCardContent={(model) => (
          <Typography variant="body2" color="text.secondary">
            {model.description || 'Aucune description'}
          </Typography>
        )}
        pagination={{
          count: data?.count ?? 0,
          page,
          pageSize,
          onPageChange: (_event, nextPage) => setPage(nextPage),
          onPageSizeChange: (event) => {
            const nextSize = Number(event.target.value);
            setPageSize(nextSize);
            setPage(0);
          },
        }}
      />

      <Dialog open={Boolean(modelToDelete)} onClose={() => setModelToDelete(null)}>
        <DialogTitle>Supprimer le modele</DialogTitle>
        <DialogContent>
          <DialogContentText>
            Voulez-vous vraiment supprimer {modelToDelete?.name || 'ce modele'} ?
          </DialogContentText>
        </DialogContent>
        <DialogActions>
          <Button onClick={() => setModelToDelete(null)} disabled={isDeleting}>Annuler</Button>
          <Button color="error" onClick={handleDelete} disabled={isDeleting}>Supprimer</Button>
        </DialogActions>
      </Dialog>
    </Stack>
  );
};
