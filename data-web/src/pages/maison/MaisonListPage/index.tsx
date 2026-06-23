import { useEffect, useMemo, useRef, useState, type ChangeEvent, type FC } from 'react';
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
import DownloadIcon from '@mui/icons-material/Download';
import EditOutlinedIcon from '@mui/icons-material/EditOutlined';
import UploadFileIcon from '@mui/icons-material/UploadFile';
import VisibilityOutlinedIcon from '@mui/icons-material/VisibilityOutlined';
import { WebsiteLink } from '../../../components/WebsiteLink';
import { type Maison, useImportMaisonsMutation, useSearchMaisonsQuery } from '../../../services/maisonApi';
import { maisonApi, useDeleteMaisonMutation } from '../../../services/maisonApi';
import type { MaisonListPageProps } from './MaisonListPage.types';

type MaisonRow = Maison & { id: string };

const toMaisonRows = (items: Maison[] | undefined): MaisonRow[] => (items ?? []).filter((item): item is MaisonRow => Boolean(item.id));

const createExportFileName = () => {
  const date = new Date();
  const yyyy = date.getFullYear();
  const mm = String(date.getMonth() + 1).padStart(2, '0');
  const dd = String(date.getDate()).padStart(2, '0');
  return `maison-export-${yyyy}${mm}${dd}.json`;
};

export const MaisonListPage: FC<MaisonListPageProps> = () => {
  const theme = useTheme();
  const isDesktop = useMediaQuery(theme.breakpoints.up('md'));
  const [searchInput, setSearchInput] = useState('');
  const [searchQuery, setSearchQuery] = useState('');
  const [page, setPage] = useState(0);
  const [pageSize, setPageSize] = useState(10);
  const queryArgs = useMemo(() => ({ page, size: pageSize, q: searchQuery || undefined }), [page, pageSize, searchQuery]);
  const { data, isLoading, error, refetch } = useSearchMaisonsQuery(queryArgs, { refetchOnMountOrArgChange: true });
  const [deleteMaison, { isLoading: isDeleting }] = useDeleteMaisonMutation();
  const [importMaisons, { isLoading: isImporting }] = useImportMaisonsMutation();
  const [exportMaisonsTrigger, { isFetching: isExporting }] = maisonApi.useLazyExportMaisonsQuery();
  const importJsonInputRef = useRef<HTMLInputElement | null>(null);
  const [maisonToDelete, setMaisonToDelete] = useState<MaisonRow | null>(null);
  const [importDialogOpen, setImportDialogOpen] = useState(false);
  const [importText, setImportText] = useState('');
  const [importError, setImportError] = useState<string | null>(null);
  const [importSummary, setImportSummary] = useState<{ addedCount: number; notAddedCount: number; alreadyExistsCount: number; invalidCount: number } | null>(null);
  const maisons = useMemo(() => toMaisonRows(data?.items), [data?.items]);

  useEffect(() => {
    const timeout = setTimeout(() => {
      setSearchQuery(searchInput.trim());
      setPage(0);
    }, 300);
    return () => clearTimeout(timeout);
  }, [searchInput]);

  const handleDelete = async () => {
    if (!maisonToDelete) return;
    await deleteMaison({ id: maisonToDelete.id }).unwrap();
    setMaisonToDelete(null);
    await refetch();
  };

  const handleExport = async () => {
    const payload = await exportMaisonsTrigger().unwrap();
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
      const result = await importMaisons({ maisonImportForm: { text: importText } }).unwrap();
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
      setImportError("Impossible d'importer les maisons. Verifiez le texte saisi.");
    }
  };

  const handleImportJson = async (event: ChangeEvent<HTMLInputElement>) => {
    const file = event.target.files?.[0];
    if (!file) return;
    try {
      setImportError(null);
      const raw = await file.text();
      const parsed = JSON.parse(raw) as { items?: Maison[] } | Maison[];
      const items = Array.isArray(parsed) ? parsed : Array.isArray(parsed.items) ? parsed.items : null;
      if (!items) {
        setImportError("Le JSON doit contenir une liste d'elements (tableau ou champ items).");
        return;
      }
      const result = await importMaisons({ maisonImportForm: { items } }).unwrap();
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
  if (error) return <div>Erreur lors du chargement des maisons</div>;

  return (
    <Stack spacing={3} sx={{ p: { xs: 2, md: 3 } }}>
      <Stack direction={{ xs: 'column', sm: 'row' }} spacing={1.5} useFlexGap sx={{ alignItems: { sm: 'center' }, justifyContent: 'space-between' }}>
        <Box>
          <Typography variant="h4" component="h1">Maison</Typography>
          <Typography variant="body2" color="text.secondary">Gere les maisons avec import texte ligne par ligne et export JSON.</Typography>
        </Box>
        <Stack direction="row" spacing={1} useFlexGap sx={{ flexWrap: 'wrap' }}>
          <Chip label={`${data?.count ?? 0} ${(data?.count ?? 0) > 1 ? 'maisons' : 'maison'}`} color="primary" variant="outlined" />
          <Button variant="outlined" startIcon={<DownloadIcon />} onClick={handleExport} disabled={isExporting}>Export JSON</Button>
          <Button variant="outlined" startIcon={<UploadFileIcon />} onClick={() => { setImportError(null); importJsonInputRef.current?.click(); }} disabled={isImporting}>Import JSON</Button>
          <Button variant="outlined" startIcon={<UploadFileIcon />} onClick={() => { setImportError(null); setImportDialogOpen(true); }} disabled={isImporting}>Import texte</Button>
          <Button component={RouterLink} to="/maison/create" variant="contained">Nouvelle maison</Button>
          <input ref={importJsonInputRef} type="file" accept="application/json,.json" hidden onChange={handleImportJson} />
        </Stack>
      </Stack>
      <TextField fullWidth label="Recherche" placeholder="Rechercher par nom ou site web" value={searchInput} onChange={(event) => setSearchInput(event.target.value)} />
      {importError && <Alert severity="error">{importError}</Alert>}
      {importSummary && <Alert severity={importSummary.notAddedCount > 0 ? 'warning' : 'success'}>Import termine : {importSummary.addedCount} maison{importSummary.addedCount > 1 ? 's' : ''} ajoutee{importSummary.addedCount > 1 ? 's' : ''}.</Alert>}
      {maisons.length === 0 && <Alert severity="info">Aucune maison configuree.</Alert>}
      {isDesktop ? (
        <TableContainer component={Paper} variant="outlined">
          <Table>
            <TableHead><TableRow><TableCell>Nom</TableCell><TableCell>Site web</TableCell><TableCell align="right">Actions</TableCell></TableRow></TableHead>
            <TableBody>{maisons.map((maison) => <TableRow key={maison.id} hover><TableCell><Typography sx={{ fontWeight: 600 }}>{maison.name || '-'}</Typography></TableCell><TableCell><WebsiteLink href={maison.website}>{maison.website || '-'}</WebsiteLink></TableCell><TableCell align="right"><IconButton component={RouterLink} to={`/maison/${maison.id}`} aria-label="Voir"><VisibilityOutlinedIcon fontSize="small" /></IconButton><IconButton component={RouterLink} to={`/maison/${maison.id}/edit`} aria-label="Modifier"><EditOutlinedIcon fontSize="small" /></IconButton><IconButton aria-label="Supprimer" color="error" onClick={() => setMaisonToDelete(maison)}><DeleteIcon fontSize="small" /></IconButton></TableCell></TableRow>)}</TableBody>
          </Table>
          <TablePagination component="div" count={data?.count ?? 0} page={page} onPageChange={(_e, n) => setPage(n)} rowsPerPage={pageSize} onRowsPerPageChange={(e) => { const n = Number(e.target.value); setPageSize(n); setPage(0); }} rowsPerPageOptions={[10, 20, 50]} />
        </TableContainer>
      ) : (
        <Stack spacing={2}>{maisons.map((maison) => <Card key={maison.id} variant="outlined"><CardContent><Stack spacing={1}><Typography variant="h6">{maison.name || '-'}</Typography><Typography variant="body2" color="text.secondary"><WebsiteLink href={maison.website}>{maison.website || 'Aucun site web'}</WebsiteLink></Typography></Stack></CardContent><CardActions sx={{ px: 2, pb: 2, pt: 0, gap: 1, flexWrap: 'wrap' }}><Button component={RouterLink} to={`/maison/${maison.id}`} size="small" variant="outlined">Voir</Button><Button component={RouterLink} to={`/maison/${maison.id}/edit`} size="small" variant="outlined">Modifier</Button><Button size="small" color="error" variant="outlined" onClick={() => setMaisonToDelete(maison)}>Supprimer</Button></CardActions></Card>)}</Stack>
      )}
      {!isDesktop && <TablePagination component="div" count={data?.count ?? 0} page={page} onPageChange={(_e, n) => setPage(n)} rowsPerPage={pageSize} onRowsPerPageChange={(e) => { const n = Number(e.target.value); setPageSize(n); setPage(0); }} rowsPerPageOptions={[10, 20, 50]} />}
      <Dialog open={Boolean(maisonToDelete)} onClose={() => setMaisonToDelete(null)}><DialogTitle>Supprimer la maison</DialogTitle><DialogContent><DialogContentText>Voulez-vous vraiment supprimer {maisonToDelete?.name || 'cette maison'} ?</DialogContentText></DialogContent><DialogActions><Button onClick={() => setMaisonToDelete(null)} disabled={isDeleting}>Annuler</Button><Button color="error" onClick={handleDelete} disabled={isDeleting}>Supprimer</Button></DialogActions></Dialog>
      <Dialog open={importDialogOpen} onClose={() => { if (!isImporting) setImportDialogOpen(false); }} fullWidth maxWidth="sm"><DialogTitle>Importer des maisons</DialogTitle><DialogContent><DialogContentText sx={{ mb: 2 }}>Collez un texte ou chaque ligne non vide correspond a une maison.</DialogContentText><TextField autoFocus fullWidth multiline minRows={8} label="Maisons (une ligne par element)" value={importText} onChange={(event) => setImportText(event.target.value)} disabled={isImporting} /></DialogContent><DialogActions><Button onClick={() => setImportDialogOpen(false)} disabled={isImporting}>Annuler</Button><Button onClick={handleImportText} disabled={isImporting} variant="contained">{isImporting ? 'Import en cours...' : 'Importer'}</Button></DialogActions></Dialog>
    </Stack>
  );
};
