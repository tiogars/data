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
import EditOutlinedIcon from '@mui/icons-material/EditOutlined';
import UploadFileIcon from '@mui/icons-material/UploadFile';
import VisibilityOutlinedIcon from '@mui/icons-material/VisibilityOutlined';
import { MaisonReference } from '../../../components/MaisonReference';
import { useListAppellationsQuery } from '../../../services/appellationApi';
import { useListCouleursQuery } from '../../../services/couleurApi';
import { type Vin, useImportVinsMutation, useSearchVinsQuery } from '../../../services/vinApi';
import { vinApi, useDeleteVinMutation } from '../../../services/vinApi';
import type { VinListPageProps } from './VinListPage.types';

type VinRow = Vin & { id: string };
const toVinRows = (items: Vin[] | undefined): VinRow[] => (items ?? []).filter((item): item is VinRow => Boolean(item.id));
const createExportFileName = () => { const d = new Date(); return `vin-export-${d.getFullYear()}${String(d.getMonth() + 1).padStart(2, '0')}${String(d.getDate()).padStart(2, '0')}.json`; };
const formatMaisonVinNom = (vin: Pick<VinRow, 'maisonName' | 'vinNomName'>) => vin.maisonName && vin.vinNomName ? `${vin.maisonName} / ${vin.vinNomName}` : vin.maisonName || vin.vinNomName || '-';
const renderMaisonVinNom = (vin: Pick<VinRow, 'maisonId' | 'maisonName' | 'vinNomName'>) => {
  if (!vin.maisonId && !vin.maisonName) {
    return vin.vinNomName || '-';
  }

  return (
    <Stack spacing={0.5}>
      <Typography component="div">
        <MaisonReference maisonId={vin.maisonId} maisonName={vin.maisonName} showWebsite />
      </Typography>
      {vin.vinNomName ? <Typography variant="body2" color="text.secondary">{vin.vinNomName}</Typography> : null}
    </Stack>
  );
};

export const VinListPage: FC<VinListPageProps> = () => {
  const theme = useTheme();
  const isDesktop = useMediaQuery(theme.breakpoints.up('md'));
  const [searchInput, setSearchInput] = useState('');
  const [searchQuery, setSearchQuery] = useState('');
  const [page, setPage] = useState(0);
  const [pageSize, setPageSize] = useState(10);
  const [appellationId, setAppellationId] = useState('');
  const [couleurId, setCouleurId] = useState('');
  const [annee, setAnnee] = useState('');
  const queryArgs = useMemo(() => ({ page, size: pageSize, q: searchQuery || undefined, appellationId: appellationId || undefined, couleurId: couleurId || undefined, annee: annee ? Number(annee) : undefined }), [annee, appellationId, couleurId, page, pageSize, searchQuery]);
  const { data, isLoading, error, refetch } = useSearchVinsQuery(queryArgs, { refetchOnMountOrArgChange: true });
  const [deleteVin, { isLoading: isDeleting }] = useDeleteVinMutation();
  const [importVins, { isLoading: isImporting }] = useImportVinsMutation();
  const [exportVinsTrigger, { isFetching: isExporting }] = vinApi.useLazyExportVinsQuery();
  const { data: appellationsData } = useListAppellationsQuery(undefined, { refetchOnMountOrArgChange: true });
  const { data: couleursData } = useListCouleursQuery(undefined, { refetchOnMountOrArgChange: true });
  const appellations = useMemo(() => (appellationsData?.items ?? []).filter((item): item is { id: string; name?: string } => Boolean(item.id)), [appellationsData?.items]);
  const couleurs = useMemo(() => (couleursData?.items ?? []).filter((item): item is { id: string; name?: string } => Boolean(item.id)), [couleursData?.items]);
  const importJsonInputRef = useRef<HTMLInputElement | null>(null);
  const [vinToDelete, setVinToDelete] = useState<VinRow | null>(null);
  const [importDialogOpen, setImportDialogOpen] = useState(false);
  const [importText, setImportText] = useState('');
  const [importError, setImportError] = useState<string | null>(null);
  const [importSummary, setImportSummary] = useState<{ addedCount: number; notAddedCount: number; alreadyExistsCount: number; invalidCount: number } | null>(null);
  const vins = useMemo(() => toVinRows(data?.items), [data?.items]);

  useEffect(() => {
    const timeout = setTimeout(() => {
      setSearchQuery(searchInput.trim());
      setPage(0);
    }, 300);

    return () => clearTimeout(timeout);
  }, [searchInput]);

  useEffect(() => {
    setPage(0);
  }, [appellationId, couleurId, annee]);

  const handleDelete = async () => {
    if (!vinToDelete) return;
    await deleteVin({ id: vinToDelete.id }).unwrap();
    setVinToDelete(null);
    await refetch();
  };

  const handleExport = async () => {
    const payload = await exportVinsTrigger().unwrap();
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
      setImportError('Veuillez coller un JSON de vins.');
      return;
    }

    try {
      setImportError(null);
      const parsed = JSON.parse(importText) as { items?: Vin[] } | Vin[];
      const items = Array.isArray(parsed) ? parsed : Array.isArray(parsed.items) ? parsed.items : null;

      if (!items) {
        setImportError("Le JSON doit contenir une liste d'elements (tableau ou champ items).");
        return;
      }

      const result = await importVins({ vinImportForm: { items } }).unwrap();
      setImportSummary({
        addedCount: result.addedCount ?? result.importedCount ?? 0,
        notAddedCount: result.notAddedCount ?? 0,
        alreadyExistsCount: result.alreadyExistsCount ?? 0,
        invalidCount: result.invalidCount ?? 0,
      });
      setImportDialogOpen(false);
      setImportText('');
      await refetch();
    } catch {
      setImportError('Le JSON saisi est invalide ou incompatible.');
    }
  };

  const handleImportJson = async (event: ChangeEvent<HTMLInputElement>) => {
    const file = event.target.files?.[0];
    if (!file) return;

    try {
      setImportError(null);
      const raw = await file.text();
      const parsed = JSON.parse(raw) as { items?: Vin[] } | Vin[];
      const items = Array.isArray(parsed) ? parsed : Array.isArray(parsed.items) ? parsed.items : null;

      if (!items) {
        setImportError("Le JSON doit contenir une liste d'elements (tableau ou champ items).");
        return;
      }

      const result = await importVins({ vinImportForm: { items } }).unwrap();
      setImportSummary({
        addedCount: result.addedCount ?? result.importedCount ?? 0,
        notAddedCount: result.notAddedCount ?? 0,
        alreadyExistsCount: result.alreadyExistsCount ?? 0,
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
  if (error) return <div>Erreur lors du chargement des vins</div>;

  return (
    <Stack spacing={3} sx={{ p: { xs: 2, md: 3 } }}>
      <Stack direction={{ xs: 'column', sm: 'row' }} spacing={1.5} useFlexGap sx={{ alignItems: { sm: 'center' }, justifyContent: 'space-between' }}>
        <Box>
          <Typography variant="h4" component="h1">Vin</Typography>
          <Typography variant="body2" color="text.secondary">Gere les vins avec filtres, import JSON et export JSON.</Typography>
        </Box>
        <Stack direction="row" spacing={1} useFlexGap sx={{ flexWrap: 'wrap' }}>
          <Chip label={`${data?.count ?? 0} ${(data?.count ?? 0) > 1 ? 'vins' : 'vin'}`} color="primary" variant="outlined" />
          <Button variant="outlined" startIcon={<DownloadIcon />} onClick={handleExport} disabled={isExporting}>Export JSON</Button>
          <Button variant="outlined" startIcon={<UploadFileIcon />} onClick={() => { setImportError(null); importJsonInputRef.current?.click(); }} disabled={isImporting}>Import JSON</Button>
          <Button variant="outlined" startIcon={<UploadFileIcon />} onClick={() => { setImportError(null); setImportDialogOpen(true); }} disabled={isImporting}>Coller JSON</Button>
          <Button component={RouterLink} to="/vin/create" variant="contained">Nouveau vin</Button>
          <input ref={importJsonInputRef} type="file" accept="application/json,.json" hidden onChange={handleImportJson} />
        </Stack>
      </Stack>
      <Stack direction={{ xs: 'column', md: 'row' }} spacing={1.5}>
        <TextField fullWidth label="Recherche" placeholder="Rechercher par region, commune ou commentaire" value={searchInput} onChange={(event) => setSearchInput(event.target.value)} />
        <TextField select label="Appellation" value={appellationId} onChange={(event) => setAppellationId(event.target.value)} sx={{ minWidth: { md: 220 } }}>
          <MenuItem value=""><em>Toutes</em></MenuItem>
          {appellations.map((item) => <MenuItem key={item.id} value={item.id}>{item.name || item.id}</MenuItem>)}
        </TextField>
        <TextField select label="Couleur" value={couleurId} onChange={(event) => setCouleurId(event.target.value)} sx={{ minWidth: { md: 200 } }}>
          <MenuItem value=""><em>Toutes</em></MenuItem>
          {couleurs.map((item) => <MenuItem key={item.id} value={item.id}>{item.name || item.id}</MenuItem>)}
        </TextField>
        <TextField label="Annee" type="number" value={annee} onChange={(event) => setAnnee(event.target.value)} sx={{ minWidth: { md: 160 } }} />
      </Stack>
      {importError && <Alert severity="error">{importError}</Alert>}
      {importSummary && <Alert severity={importSummary.notAddedCount > 0 ? 'warning' : 'success'}>Import termine : {importSummary.addedCount} vin{importSummary.addedCount > 1 ? 's' : ''} ajoute{importSummary.addedCount > 1 ? 's' : ''}.</Alert>}
      {vins.length === 0 && <Alert severity="info">Aucun vin configure.</Alert>}
      {isDesktop ? (
        <TableContainer component={Paper} variant="outlined">
          <Table>
            <TableHead>
              <TableRow>
                <TableCell>Annee</TableCell>
                <TableCell>Appellation</TableCell>
                <TableCell>Couleur</TableCell>
                <TableCell>Maison / Nom du vin</TableCell>
                <TableCell>Region</TableCell>
                <TableCell align="right">Actions</TableCell>
              </TableRow>
            </TableHead>
            <TableBody>
              {vins.map((vin) => (
                <TableRow key={vin.id} hover>
                  <TableCell>{vin.annee ?? '-'}</TableCell>
                  <TableCell>{vin.appellationName || '-'}</TableCell>
                  <TableCell>{vin.couleurName || '-'}</TableCell>
                  <TableCell>{renderMaisonVinNom(vin)}</TableCell>
                  <TableCell>{vin.region || '-'}</TableCell>
                  <TableCell align="right">
                    <IconButton component={RouterLink} to={`/vin/${vin.id}`} aria-label="Voir">
                      <VisibilityOutlinedIcon fontSize="small" />
                    </IconButton>
                    <IconButton component={RouterLink} to={`/vin/${vin.id}/edit`} aria-label="Modifier">
                      <EditOutlinedIcon fontSize="small" />
                    </IconButton>
                    <IconButton aria-label="Supprimer" color="error" onClick={() => setVinToDelete(vin)}>
                      <DeleteIcon fontSize="small" />
                    </IconButton>
                  </TableCell>
                </TableRow>
              ))}
            </TableBody>
          </Table>
          <TablePagination component="div" count={data?.count ?? 0} page={page} onPageChange={(_e, n) => setPage(n)} rowsPerPage={pageSize} onRowsPerPageChange={(e) => { const n = Number(e.target.value); setPageSize(n); setPage(0); }} rowsPerPageOptions={[10, 20, 50]} />
        </TableContainer>
      ) : (
        <Stack spacing={2}>
          {vins.map((vin) => (
            <Card key={vin.id} variant="outlined">
              <CardContent>
                <Stack spacing={1}>
                  <Typography variant="h6">{vin.vinNomName || vin.maisonName || '-'}</Typography>
                  <Typography variant="body2" color="text.secondary">
                    <MaisonReference maisonId={vin.maisonId} maisonName={vin.maisonName} showWebsite />
                  </Typography>
                  <Typography variant="body2" color="text.secondary">{vin.appellationName || '-'} • {vin.couleurName || '-'} • {vin.annee ?? '-'}</Typography>
                  <Typography variant="body2" color="text.secondary">Region : {vin.region || '-'}</Typography>
                </Stack>
              </CardContent>
              <CardActions sx={{ px: 2, pb: 2, pt: 0, gap: 1, flexWrap: 'wrap' }}>
                <Button component={RouterLink} to={`/vin/${vin.id}`} size="small" variant="outlined">Voir</Button>
                <Button component={RouterLink} to={`/vin/${vin.id}/edit`} size="small" variant="outlined">Modifier</Button>
                <Button size="small" color="error" variant="outlined" onClick={() => setVinToDelete(vin)}>Supprimer</Button>
              </CardActions>
            </Card>
          ))}
        </Stack>
      )}
      {!isDesktop && <TablePagination component="div" count={data?.count ?? 0} page={page} onPageChange={(_e, n) => setPage(n)} rowsPerPage={pageSize} onRowsPerPageChange={(e) => { const n = Number(e.target.value); setPageSize(n); setPage(0); }} rowsPerPageOptions={[10, 20, 50]} />}
      <Dialog open={Boolean(vinToDelete)} onClose={() => setVinToDelete(null)}>
        <DialogTitle>Supprimer le vin</DialogTitle>
        <DialogContent>
          <DialogContentText>Voulez-vous vraiment supprimer {formatMaisonVinNom(vinToDelete ?? { maisonName: undefined, vinNomName: undefined } as VinRow)} ?</DialogContentText>
        </DialogContent>
        <DialogActions>
          <Button onClick={() => setVinToDelete(null)} disabled={isDeleting}>Annuler</Button>
          <Button color="error" onClick={handleDelete} disabled={isDeleting}>Supprimer</Button>
        </DialogActions>
      </Dialog>
      <Dialog open={importDialogOpen} onClose={() => { if (!isImporting) setImportDialogOpen(false); }} fullWidth maxWidth="sm">
        <DialogTitle>Coller un JSON de vins</DialogTitle>
        <DialogContent>
          <DialogContentText sx={{ mb: 2 }}>Collez un tableau JSON de vins ou un objet contenant un champ items.</DialogContentText>
          <TextField autoFocus fullWidth multiline minRows={8} label="JSON des vins" value={importText} onChange={(event) => setImportText(event.target.value)} disabled={isImporting} />
        </DialogContent>
        <DialogActions>
          <Button onClick={() => setImportDialogOpen(false)} disabled={isImporting}>Annuler</Button>
          <Button onClick={handleImportText} disabled={isImporting} variant="contained">{isImporting ? 'Import en cours...' : 'Importer'}</Button>
        </DialogActions>
      </Dialog>
    </Stack>
  );
};
