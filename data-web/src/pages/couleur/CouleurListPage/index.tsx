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
import { type Couleur, useImportCouleursMutation, useSearchCouleursQuery } from '../../../services/couleurApi';
import { couleurApi, useDeleteCouleurMutation } from '../../../services/couleurApi';
import type { CouleurListPageProps } from './CouleurListPage.types';

type CouleurRow = Couleur & { id: string };
const couleurColumns: CrudListColumn<CouleurRow>[] = [
  {
    key: 'name',
    header: 'Nom',
    render: (item) => <Typography sx={{ fontWeight: 600 }}>{item.name || '-'}</Typography>,
  },
];
const toRows = (items: Couleur[] | undefined): CouleurRow[] => (items ?? []).filter((item): item is CouleurRow => Boolean(item.id));
const exportName = () => { const d = new Date(); return `couleur-export-${d.getFullYear()}${String(d.getMonth()+1).padStart(2,'0')}${String(d.getDate()).padStart(2,'0')}.json`; };

export const CouleurListPage: FC<CouleurListPageProps> = () => {
  const {
    searchInput,
    setSearchInput,
    page,
    pageSize,
    queryArgs,
    handlePageChange,
    handlePageSizeChange,
  } = usePaginatedSearch();
  const { data, isLoading, error, refetch } = useSearchCouleursQuery(queryArgs, { refetchOnMountOrArgChange: true });
  const [deleteItem, { isLoading: isDeleting }] = useDeleteCouleurMutation();
  const [importItems, { isLoading: isImporting }] = useImportCouleursMutation();
  const [exportItemsTrigger, { isFetching: isExporting }] = couleurApi.useLazyExportCouleursQuery();
  const importJsonInputRef = useRef<HTMLInputElement | null>(null);
  const [itemToDelete, setItemToDelete] = useState<CouleurRow | null>(null);
  const [importDialogOpen, setImportDialogOpen] = useState(false);
  const [importText, setImportText] = useState('');
  const [importError, setImportError] = useState<string | null>(null);
  const [importSummary, setImportSummary] = useState<{ addedCount: number; notAddedCount: number; alreadyExistsCount: number; invalidCount: number } | null>(null);
  const items = useMemo(() => toRows(data?.items), [data?.items]);
  const handleDelete = async () => { if (!itemToDelete) return; await deleteItem({ id: itemToDelete.id }).unwrap(); setItemToDelete(null); await refetch(); };
  const handleExport = async () => { const payload = await exportItemsTrigger().unwrap(); const blob = new Blob([JSON.stringify(payload, null, 2)], { type: 'application/json' }); const href = URL.createObjectURL(blob); const a = document.createElement('a'); a.href = href; a.download = exportName(); a.click(); URL.revokeObjectURL(href); };
  const handleImportText = async () => { if (!importText.trim()) { setImportError('Veuillez coller au moins une valeur (une ligne par element).'); return; } try { setImportError(null); const r = await importItems({ couleurImportForm: { text: importText } }).unwrap(); setImportSummary({ addedCount: r.addedCount ?? 0, notAddedCount: r.notAddedCount ?? 0, alreadyExistsCount: r.alreadyExistsCount ?? 0, invalidCount: r.invalidCount ?? 0 }); setImportDialogOpen(false); setImportText(''); await refetch(); } catch { setImportError("Impossible d'importer les couleurs. Verifiez le texte saisi."); } };
  const handleImportJson = async (event: ChangeEvent<HTMLInputElement>) => { const file = event.target.files?.[0]; if (!file) return; try { setImportError(null); const raw = await file.text(); const parsed = JSON.parse(raw) as { items?: Couleur[] } | Couleur[]; const importedItems = Array.isArray(parsed) ? parsed : Array.isArray(parsed.items) ? parsed.items : null; if (!importedItems) { setImportError("Le JSON doit contenir une liste d'elements (tableau ou champ items)."); return; } const r = await importItems({ couleurImportForm: { items: importedItems } }).unwrap(); setImportSummary({ addedCount: r.addedCount ?? r.importedCount ?? 0, notAddedCount: r.notAddedCount ?? 0, alreadyExistsCount: r.alreadyExistsCount ?? r.duplicateNames?.length ?? 0, invalidCount: r.invalidCount ?? 0 }); await refetch(); } catch { setImportError('Le fichier JSON est invalide ou incompatible.'); } finally { event.target.value = ''; } };
  if (isLoading) return <div>Chargement...</div>;
  if (error) return <div>Erreur lors du chargement des couleurs</div>;
  return <Stack spacing={3} sx={{ p: { xs: 2, md: 3 } }}><Stack direction={{ xs: 'column', sm: 'row' }} spacing={1.5} useFlexGap sx={{ alignItems: { sm: 'center' }, justifyContent: 'space-between' }}><Box><Typography variant="h4" component="h1">Couleur</Typography><Typography variant="body2" color="text.secondary">Gere les couleurs avec import texte ligne par ligne et export JSON.</Typography></Box><Stack direction="row" spacing={1} useFlexGap sx={{ flexWrap: 'wrap' }}><Chip label={`${data?.count ?? 0} ${(data?.count ?? 0) > 1 ? 'couleurs' : 'couleur'}`} color="primary" variant="outlined" /><Button variant="outlined" startIcon={<DownloadIcon />} onClick={handleExport} disabled={isExporting}>Export JSON</Button><Button variant="outlined" startIcon={<UploadFileIcon />} onClick={() => { setImportError(null); importJsonInputRef.current?.click(); }} disabled={isImporting}>Import JSON</Button><Button variant="outlined" startIcon={<UploadFileIcon />} onClick={() => { setImportError(null); setImportDialogOpen(true); }} disabled={isImporting}>Import texte</Button><Button component={RouterLink} to="/couleur/create" variant="contained">Nouvelle couleur</Button><input ref={importJsonInputRef} type="file" accept="application/json,.json" hidden onChange={handleImportJson} /></Stack></Stack><TextField fullWidth label="Recherche" placeholder="Rechercher par nom" value={searchInput} onChange={(event) => setSearchInput(event.target.value)} />{importError && <Alert severity="error">{importError}</Alert>}{importSummary && <Alert severity={importSummary.notAddedCount > 0 ? 'warning' : 'success'}>Import termine : {importSummary.addedCount} couleur{importSummary.addedCount > 1 ? 's' : ''} ajoute{importSummary.addedCount > 1 ? 's' : ''}.</Alert>}{items.length === 0 && <Alert severity="info">Aucune couleur configuree.</Alert>}<ResponsiveCrudList items={items} getRowKey={(item) => item.id} columns={couleurColumns} getDetailPath={(item) => `/couleur/${item.id}`} getEditPath={(item) => `/couleur/${item.id}/edit`} onDelete={setItemToDelete} actionLabels={{ view: 'Voir', edit: 'Modifier', remove: 'Supprimer' }} renderCardTitle={(item) => item.name || '-'} pagination={{ count: data?.count ?? 0, page, pageSize, onPageChange: handlePageChange, onPageSizeChange: handlePageSizeChange }} /><Dialog open={Boolean(itemToDelete)} onClose={() => setItemToDelete(null)}><DialogTitle>Supprimer couleur</DialogTitle><DialogContent><DialogContentText>Voulez-vous vraiment supprimer {itemToDelete?.name || 'cet element'} ?</DialogContentText></DialogContent><DialogActions><Button onClick={() => setItemToDelete(null)} disabled={isDeleting}>Annuler</Button><Button color="error" onClick={handleDelete} disabled={isDeleting}>Supprimer</Button></DialogActions></Dialog><Dialog open={importDialogOpen} onClose={() => { if (!isImporting) setImportDialogOpen(false); }} fullWidth maxWidth="sm"><DialogTitle>Importer des couleurs</DialogTitle><DialogContent><DialogContentText sx={{ mb: 2 }}>Collez un texte ou chaque ligne non vide correspond a un element.</DialogContentText><TextField autoFocus fullWidth multiline minRows={8} label="Valeurs (une ligne par element)" value={importText} onChange={(event) => setImportText(event.target.value)} disabled={isImporting} /></DialogContent><DialogActions><Button onClick={() => setImportDialogOpen(false)} disabled={isImporting}>Annuler</Button><Button onClick={handleImportText} disabled={isImporting} variant="contained">{isImporting ? 'Import en cours...' : 'Importer'}</Button></DialogActions></Dialog></Stack>;
};
