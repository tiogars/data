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
import UploadFileIcon from '@mui/icons-material/UploadFile';
import { MaisonReference } from '../../../components/MaisonReference';
import { ResponsiveCrudList, type CrudListColumn } from '../../../components/ResponsiveCrudList';
import { usePaginatedSearch } from '../../../hooks/usePaginatedSearch';
import { type VinNom, useImportVinNomsMutation, useSearchVinNomsQuery } from '../../../services/vinNomApi';
import { vinNomApi, useDeleteVinNomMutation } from '../../../services/vinNomApi';
import type { VinNomListPageProps } from './VinNomListPage.types';

type VinNomRow = VinNom & { id: string };
const toVinNomRows = (items: VinNom[] | undefined): VinNomRow[] => (items ?? []).filter((item): item is VinNomRow => Boolean(item.id));
const createExportFileName = () => { const d = new Date(); return `vin-nom-export-${d.getFullYear()}${String(d.getMonth() + 1).padStart(2, '0')}${String(d.getDate()).padStart(2, '0')}.json`; };
const vinNomColumns: CrudListColumn<VinNomRow>[] = [
  {
    key: 'name',
    header: 'Nom',
    render: (vinNom) => <Typography sx={{ fontWeight: 600 }}>{vinNom.name || '-'}</Typography>,
  },
  {
    key: 'maison',
    header: 'Maison',
    render: (vinNom) => <MaisonReference maisonId={vinNom.maisonId} maisonName={vinNom.maisonName} showWebsite />,
  },
];

export const VinNomListPage: FC<VinNomListPageProps> = () => {
  const {
    searchInput,
    setSearchInput,
    page,
    pageSize,
    queryArgs,
    handlePageChange,
    handlePageSizeChange,
  } = usePaginatedSearch();
  const { data, isLoading, error, refetch } = useSearchVinNomsQuery(queryArgs, { refetchOnMountOrArgChange: true });
  const [deleteVinNom, { isLoading: isDeleting }] = useDeleteVinNomMutation();
  const [importVinNoms, { isLoading: isImporting }] = useImportVinNomsMutation();
  const [exportVinNomsTrigger, { isFetching: isExporting }] = vinNomApi.useLazyExportVinNomsQuery();
  const importJsonInputRef = useRef<HTMLInputElement | null>(null);
  const [vinNomToDelete, setVinNomToDelete] = useState<VinNomRow | null>(null);
  const [importDialogOpen, setImportDialogOpen] = useState(false);
  const [importText, setImportText] = useState('');
  const [importError, setImportError] = useState<string | null>(null);
  const [importSummary, setImportSummary] = useState<{ addedCount: number; notAddedCount: number; alreadyExistsCount: number; invalidCount: number } | null>(null);
  const vinNoms = useMemo(() => toVinNomRows(data?.items), [data?.items]);

  const handleDelete = async () => {
    if (!vinNomToDelete) return;
    await deleteVinNom({ id: vinNomToDelete.id }).unwrap();
    setVinNomToDelete(null);
    await refetch();
  };

  const handleExport = async () => {
    const payload = await exportVinNomsTrigger().unwrap();
    const blob = new Blob([JSON.stringify(payload, null, 2)], { type: 'application/json' });
    const href = URL.createObjectURL(blob);
    const anchor = document.createElement('a');
    anchor.href = href;
    anchor.download = createExportFileName();
    anchor.click();
    URL.revokeObjectURL(href);
  };

  const handleImportText = async () => {
    if (!importText.trim()) {
      setImportError('Veuillez coller au moins une valeur (une ligne par element).');
      return;
    }

    try {
      setImportError(null);
      const result = await importVinNoms({ vinNomImportForm: { text: importText } }).unwrap();
      setImportSummary({
        addedCount: result.addedCount ?? 0,
        notAddedCount: result.notAddedCount ?? 0,
        alreadyExistsCount: result.alreadyExistsCount ?? 0,
        invalidCount: result.invalidCount ?? 0,
      });
      setImportDialogOpen(false);
      setImportText('');
      await refetch();
    } catch {
      setImportError("Impossible d'importer les noms de vin. Verifiez le texte saisi.");
    }
  };

  const handleImportJson = async (event: ChangeEvent<HTMLInputElement>) => {
    const file = event.target.files?.[0];
    if (!file) return;

    try {
      setImportError(null);
      const raw = await file.text();
      const parsed = JSON.parse(raw) as { items?: VinNom[] } | VinNom[];
      const items = Array.isArray(parsed) ? parsed : Array.isArray(parsed.items) ? parsed.items : null;

      if (!items) {
        setImportError("Le JSON doit contenir une liste d'elements (tableau ou champ items).");
        return;
      }

      const result = await importVinNoms({ vinNomImportForm: { items } }).unwrap();
      setImportSummary({
        addedCount: result.addedCount ?? result.importedCount ?? 0,
        notAddedCount: result.notAddedCount ?? 0,
        alreadyExistsCount: result.alreadyExistsCount ?? result.duplicateNames?.length ?? 0,
        invalidCount: result.invalidCount ?? 0,
      });
      await refetch();
    } catch {
      setImportError('Le fichier JSON est invalide ou incompatible.');
    } finally {
      event.target.value = '';
    }
  };

  if (isLoading) return <div>Chargement...</div>;
  if (error) return <div>Erreur lors du chargement des noms de vin</div>;

  return (
    <Stack spacing={3} sx={{ p: { xs: 2, md: 3 } }}>
      <Stack direction={{ xs: 'column', sm: 'row' }} spacing={1.5} useFlexGap sx={{ alignItems: { sm: 'center' }, justifyContent: 'space-between' }}>
        <Box>
          <Typography variant="h4" component="h1">Nom de vin</Typography>
          <Typography variant="body2" color="text.secondary">Gere les noms de vin avec import texte ligne par ligne et export JSON.</Typography>
        </Box>
        <Stack direction="row" spacing={1} useFlexGap sx={{ flexWrap: 'wrap' }}>
          <Chip label={`${data?.count ?? 0} ${(data?.count ?? 0) > 1 ? 'noms de vin' : 'nom de vin'}`} color="primary" variant="outlined" />
          <Button variant="outlined" startIcon={<DownloadIcon />} onClick={handleExport} disabled={isExporting}>Export JSON</Button>
          <Button variant="outlined" startIcon={<UploadFileIcon />} onClick={() => { setImportError(null); importJsonInputRef.current?.click(); }} disabled={isImporting}>Import JSON</Button>
          <Button variant="outlined" startIcon={<UploadFileIcon />} onClick={() => { setImportError(null); setImportDialogOpen(true); }} disabled={isImporting}>Import texte</Button>
          <Button component={RouterLink} to="/vin-nom/create" variant="contained">Nouveau nom de vin</Button>
          <input ref={importJsonInputRef} type="file" accept="application/json,.json" hidden onChange={handleImportJson} />
        </Stack>
      </Stack>
      <TextField fullWidth label="Recherche" placeholder="Rechercher par nom" value={searchInput} onChange={(event) => setSearchInput(event.target.value)} />
      {importError && <Alert severity="error">{importError}</Alert>}
      {importSummary && <Alert severity={importSummary.notAddedCount > 0 ? 'warning' : 'success'}>Import termine : {importSummary.addedCount} nom{importSummary.addedCount > 1 ? 's' : ''} de vin ajoute{importSummary.addedCount > 1 ? 's' : ''}.</Alert>}
      {vinNoms.length === 0 && <Alert severity="info">Aucun nom de vin configure.</Alert>}
      <ResponsiveCrudList
        items={vinNoms}
        getRowKey={(vinNom) => vinNom.id}
        columns={vinNomColumns}
        getDetailPath={(vinNom) => `/vin-nom/${vinNom.id}`}
        getEditPath={(vinNom) => `/vin-nom/${vinNom.id}/edit`}
        onDelete={setVinNomToDelete}
        actionLabels={{ view: 'Voir', edit: 'Modifier', remove: 'Supprimer' }}
        renderCardTitle={(vinNom) => vinNom.name || '-'}
        renderCardContent={(vinNom) => (
          <Typography variant="body2" color="text.secondary">
            Maison : <MaisonReference maisonId={vinNom.maisonId} maisonName={vinNom.maisonName} showWebsite />
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
      <Dialog open={Boolean(vinNomToDelete)} onClose={() => setVinNomToDelete(null)}>
        <DialogTitle>Supprimer le nom de vin</DialogTitle>
        <DialogContent>
          <DialogContentText>Voulez-vous vraiment supprimer {vinNomToDelete?.name || 'ce nom de vin'} ?</DialogContentText>
        </DialogContent>
        <DialogActions>
          <Button onClick={() => setVinNomToDelete(null)} disabled={isDeleting}>Annuler</Button>
          <Button color="error" onClick={handleDelete} disabled={isDeleting}>Supprimer</Button>
        </DialogActions>
      </Dialog>
      <Dialog open={importDialogOpen} onClose={() => { if (!isImporting) setImportDialogOpen(false); }} fullWidth maxWidth="sm">
        <DialogTitle>Importer des noms de vin</DialogTitle>
        <DialogContent>
          <DialogContentText sx={{ mb: 2 }}>Collez un texte ou chaque ligne non vide correspond a un nom de vin.</DialogContentText>
          <TextField autoFocus fullWidth multiline minRows={8} label="Noms de vin (une ligne par element)" value={importText} onChange={(event) => setImportText(event.target.value)} disabled={isImporting} />
        </DialogContent>
        <DialogActions>
          <Button onClick={() => setImportDialogOpen(false)} disabled={isImporting}>Annuler</Button>
          <Button onClick={handleImportText} disabled={isImporting} variant="contained">{isImporting ? 'Import en cours...' : 'Importer'}</Button>
        </DialogActions>
      </Dialog>
    </Stack>
  );
};
