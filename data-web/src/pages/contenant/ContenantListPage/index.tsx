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
import { ResponsiveCrudList, type CrudListColumn } from '../../../components/ResponsiveCrudList';
import { usePaginatedSearch } from '../../../hooks/usePaginatedSearch';
import { type Contenant, useImportContenantsMutation, useSearchContenantsQuery } from '../../../services/contenantApi';
import { contenantApi, useDeleteContenantMutation } from '../../../services/contenantApi';
import type { ContenantListPageProps } from './ContenantListPage.types';

type ContenantRow = Contenant & { id: string };
const toContenantRows = (items: Contenant[] | undefined): ContenantRow[] => (items ?? []).filter((item): item is ContenantRow => Boolean(item.id));
const createExportFileName = () => { const d = new Date(); return `contenant-export-${d.getFullYear()}${String(d.getMonth() + 1).padStart(2, '0')}${String(d.getDate()).padStart(2, '0')}.json`; };
const contenantColumns: CrudListColumn<ContenantRow>[] = [
  {
    key: 'name',
    header: 'Nom',
    render: (c) => <Typography sx={{ fontWeight: 600 }}>{c.name || '-'}</Typography>,
  },
  {
    key: 'volumeCl',
    header: 'Volume (cl)',
    render: (c) => c.volumeCl ?? '-',
  },
];

export const ContenantListPage: FC<ContenantListPageProps> = () => {
  const {
    searchInput,
    setSearchInput,
    page,
    pageSize,
    queryArgs,
    handlePageChange,
    handlePageSizeChange,
  } = usePaginatedSearch();
  const { data, isLoading, error, refetch } = useSearchContenantsQuery(queryArgs, { refetchOnMountOrArgChange: true });
  const [deleteContenant, { isLoading: isDeleting }] = useDeleteContenantMutation();
  const [importContenants, { isLoading: isImporting }] = useImportContenantsMutation();
  const [exportContenantsTrigger, { isFetching: isExporting }] = contenantApi.useLazyExportContenantsQuery();
  const importJsonInputRef = useRef<HTMLInputElement | null>(null);
  const [contenantToDelete, setContenantToDelete] = useState<ContenantRow | null>(null);
  const [importDialogOpen, setImportDialogOpen] = useState(false);
  const [importText, setImportText] = useState('');
  const [importError, setImportError] = useState<string | null>(null);
  const [importSummary, setImportSummary] = useState<{ addedCount: number; notAddedCount: number; alreadyExistsCount: number; invalidCount: number } | null>(null);
  const contenants = useMemo(() => toContenantRows(data?.items), [data?.items]);

  const handleDelete = async () => { if (!contenantToDelete) return; await deleteContenant({ id: contenantToDelete.id }).unwrap(); setContenantToDelete(null); await refetch(); };
  const handleExport = async () => { const payload = await exportContenantsTrigger().unwrap(); const blob = new Blob([JSON.stringify(payload, null, 2)], { type: 'application/json' }); const href = URL.createObjectURL(blob); const a = document.createElement('a'); a.href = href; a.download = createExportFileName(); a.click(); URL.revokeObjectURL(href); };
  const handleImportText = async () => {
    if (!importText.trim()) { setImportError('Veuillez coller au moins une valeur (une ligne par element).'); return; }
    try { setImportError(null); const r = await importContenants({ contenantImportForm: { text: importText } }).unwrap(); setImportSummary({ addedCount: r.addedCount ?? 0, notAddedCount: r.notAddedCount ?? 0, alreadyExistsCount: r.alreadyExistsCount ?? 0, invalidCount: r.invalidCount ?? 0 }); setImportDialogOpen(false); setImportText(''); await refetch(); } catch { setImportError("Impossible d'importer les contenants. Verifiez le texte saisi."); }
  };
  const handleImportJson = async (event: ChangeEvent<HTMLInputElement>) => {
    const file = event.target.files?.[0]; if (!file) return;
    try { setImportError(null); const raw = await file.text(); const parsed = JSON.parse(raw) as { items?: Contenant[] } | Contenant[]; const items = Array.isArray(parsed) ? parsed : Array.isArray(parsed.items) ? parsed.items : null; if (!items) { setImportError("Le JSON doit contenir une liste d'elements (tableau ou champ items)."); return; } const r = await importContenants({ contenantImportForm: { items } }).unwrap(); setImportSummary({ addedCount: r.addedCount ?? r.importedCount ?? 0, notAddedCount: r.notAddedCount ?? 0, alreadyExistsCount: r.alreadyExistsCount ?? r.duplicateNames?.length ?? 0, invalidCount: r.invalidCount ?? 0 }); await refetch(); } catch { setImportError('Le fichier JSON est invalide ou incompatible.'); } finally { event.target.value = ''; }
  };
  if (isLoading) return <div>Chargement...</div>;
  if (error) return <div>Erreur lors du chargement des contenants</div>;
  return <Stack spacing={3} sx={{ p: { xs: 2, md: 3 } }}><Stack direction={{ xs: 'column', sm: 'row' }} spacing={1.5} useFlexGap sx={{ alignItems: { sm: 'center' }, justifyContent: 'space-between' }}><Box><Typography variant="h4" component="h1">Contenant</Typography><Typography variant="body2" color="text.secondary">Gere les contenants avec import texte ligne par ligne et export JSON.</Typography></Box><Stack direction="row" spacing={1} useFlexGap sx={{ flexWrap: 'wrap' }}><Chip label={`${data?.count ?? 0} ${(data?.count ?? 0) > 1 ? 'contenants' : 'contenant'}`} color="primary" variant="outlined" /><Button variant="outlined" startIcon={<DownloadIcon />} onClick={handleExport} disabled={isExporting}>Export JSON</Button><Button variant="outlined" startIcon={<UploadFileIcon />} onClick={() => { setImportError(null); importJsonInputRef.current?.click(); }} disabled={isImporting}>Import JSON</Button><Button variant="outlined" startIcon={<UploadFileIcon />} onClick={() => { setImportError(null); setImportDialogOpen(true); }} disabled={isImporting}>Import texte</Button><Button component={RouterLink} to="/contenant/create" variant="contained">Nouveau contenant</Button><input ref={importJsonInputRef} type="file" accept="application/json,.json" hidden onChange={handleImportJson} /></Stack></Stack><TextField fullWidth label="Recherche" placeholder="Rechercher par nom" value={searchInput} onChange={(event) => setSearchInput(event.target.value)} />{importError && <Alert severity="error">{importError}</Alert>}{importSummary && <Alert severity={importSummary.notAddedCount > 0 ? 'warning' : 'success'}>Import termine : {importSummary.addedCount} contenant{importSummary.addedCount > 1 ? 's' : ''} ajoute{importSummary.addedCount > 1 ? 's' : ''}.</Alert>}{contenants.length === 0 && <Alert severity="info">Aucun contenant configure.</Alert>}<ResponsiveCrudList items={contenants} getRowKey={(c) => c.id} columns={contenantColumns} getDetailPath={(c) => `/contenant/${c.id}`} getEditPath={(c) => `/contenant/${c.id}/edit`} onDelete={setContenantToDelete} actionLabels={{ view: 'Voir', edit: 'Modifier', remove: 'Supprimer' }} renderCardTitle={(c) => c.name || '-'} renderCardContent={(c) => <Typography variant="body2" color="text.secondary">Volume : {c.volumeCl ?? '-'} cl</Typography>} pagination={{ count: data?.count ?? 0, page, pageSize, onPageChange: handlePageChange, onPageSizeChange: handlePageSizeChange }} /><Dialog open={Boolean(contenantToDelete)} onClose={() => setContenantToDelete(null)}><DialogTitle>Supprimer le contenant</DialogTitle><DialogContent><DialogContentText>Voulez-vous vraiment supprimer {contenantToDelete?.name || 'ce contenant'} ?</DialogContentText></DialogContent><DialogActions><Button onClick={() => setContenantToDelete(null)} disabled={isDeleting}>Annuler</Button><Button color="error" onClick={handleDelete} disabled={isDeleting}>Supprimer</Button></DialogActions></Dialog><Dialog open={importDialogOpen} onClose={() => { if (!isImporting) setImportDialogOpen(false); }} fullWidth maxWidth="sm"><DialogTitle>Importer des contenants</DialogTitle><DialogContent><DialogContentText sx={{ mb: 2 }}>Collez un texte ou chaque ligne non vide correspond a un contenant.</DialogContentText><TextField autoFocus fullWidth multiline minRows={8} label="Contenants (une ligne par element)" value={importText} onChange={(event) => setImportText(event.target.value)} disabled={isImporting} /></DialogContent><DialogActions><Button onClick={() => setImportDialogOpen(false)} disabled={isImporting}>Annuler</Button><Button onClick={handleImportText} disabled={isImporting} variant="contained">{isImporting ? 'Import en cours...' : 'Importer'}</Button></DialogActions></Dialog></Stack>;
};
