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
import EditOutlinedIcon from '@mui/icons-material/EditOutlined';
import VisibilityOutlinedIcon from '@mui/icons-material/VisibilityOutlined';
import DownloadIcon from '@mui/icons-material/Download';
import UploadFileIcon from '@mui/icons-material/UploadFile';
import {
  type Brand,
  useImportBrandsMutation,
  useListBrandsQuery,
} from '../../../services/brandApi';
import { brandApi, useDeleteBrandMutation } from '../../../services/brandApi';
import type { BrandListPageProps } from './BrandListPage.types';

type BrandRow = Brand & { id: string };

function toBrandRows(items: Brand[] | undefined): BrandRow[] {
  return (items ?? []).filter((item): item is BrandRow => Boolean(item.id));
}

function createExportFileName() {
  const date = new Date();
  const yyyy = date.getFullYear();
  const mm = String(date.getMonth() + 1).padStart(2, '0');
  const dd = String(date.getDate()).padStart(2, '0');
  return `brand-export-${yyyy}${mm}${dd}.json`;
}

export const BrandListPage: FC<BrandListPageProps> = () => {
  const theme = useTheme();
  const isDesktop = useMediaQuery(theme.breakpoints.up('md'));
  const { data, isLoading, error, refetch } = useListBrandsQuery(undefined, { refetchOnMountOrArgChange: true });
  const [deleteBrandById, { isLoading: isDeleting }] = useDeleteBrandMutation();
  const [importBrands, { isLoading: isImporting }] = useImportBrandsMutation();
  const [exportBrandsTrigger, { isFetching: isExporting }] = brandApi.useLazyExportBrandsQuery();
  const importJsonInputRef = useRef<HTMLInputElement | null>(null);
  const [brandToDelete, setBrandToDelete] = useState<BrandRow | null>(null);
  const [importDialogOpen, setImportDialogOpen] = useState(false);
  const [importText, setImportText] = useState('');
  const [importError, setImportError] = useState<string | null>(null);
  const [importSummary, setImportSummary] = useState<{
    addedCount: number;
    notAddedCount: number;
    alreadyExistsCount: number;
    invalidCount: number;
  } | null>(null);

  const brands = useMemo(() => toBrandRows(data?.items), [data?.items]);

  const handleDelete = async () => {
    if (!brandToDelete) return;

    await deleteBrandById({ id: brandToDelete.id }).unwrap();
    setBrandToDelete(null);
    await refetch();
  };

  const handleExport = async () => {
    const payload = await exportBrandsTrigger().unwrap();
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
      setImportError('Veuillez coller au moins une marque (une ligne par marque).');
      return;
    }

    try {
      setImportError(null);
      const result = await importBrands({
        brandImportForm: {
          text: importText,
        },
      }).unwrap();

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
      setImportError("Impossible d'importer les marques. Verifiez le texte saisi.");
    }
  };

  const handleImportJson = async (event: ChangeEvent<HTMLInputElement>) => {
    const file = event.target.files?.[0];
    if (!file) {
      return;
    }

    try {
      setImportError(null);

      const raw = await file.text();
      const parsed = JSON.parse(raw) as { items?: Brand[] } | Brand[];
      const items = Array.isArray(parsed)
        ? parsed
        : Array.isArray(parsed.items)
          ? parsed.items
          : null;

      if (!items) {
        setImportError('Le JSON doit contenir une liste de marques (tableau ou champ items).');
        return;
      }

      const result = await importBrands({
        brandImportForm: {
          items,
        },
      }).unwrap();

      setImportSummary({
        addedCount: result.addedCount ?? result.importedCount ?? 0,
        notAddedCount: result.notAddedCount ?? result.skippedCount ?? 0,
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
  if (error) return <div>Erreur lors du chargement des marques</div>;

  return (
    <Stack spacing={3} sx={{ p: { xs: 2, md: 3 } }}>
      <Stack direction={{ xs: 'column', sm: 'row' }} spacing={1.5} useFlexGap sx={{ alignItems: { sm: 'center' }, justifyContent: 'space-between' }}>
        <Box>
          <Typography variant="h4" component="h1">
            Marque
          </Typography>
          <Typography variant="body2" color="text.secondary">
            Gere les marques avec import texte ligne par ligne et export JSON.
          </Typography>
        </Box>
        <Stack direction="row" spacing={1} useFlexGap sx={{ flexWrap: 'wrap' }}>
          <Chip label={`${data?.count ?? 0} marque${(data?.count ?? 0) > 1 ? 's' : ''}`} color="primary" variant="outlined" />
          <Button variant="outlined" startIcon={<DownloadIcon />} onClick={handleExport} disabled={isExporting}>
            Export JSON
          </Button>
          <Button
            variant="outlined"
            startIcon={<UploadFileIcon />}
            onClick={() => {
              setImportError(null);
              importJsonInputRef.current?.click();
            }}
            disabled={isImporting}
          >
            Import JSON
          </Button>
          <Button
            variant="outlined"
            startIcon={<UploadFileIcon />}
            onClick={() => {
              setImportError(null);
              setImportDialogOpen(true);
            }}
            disabled={isImporting}
          >
            Import texte
          </Button>
          <Button component={RouterLink} to="/brand/create" variant="contained">
            Nouvelle marque
          </Button>
          <input
            ref={importJsonInputRef}
            type="file"
            accept="application/json,.json"
            hidden
            onChange={handleImportJson}
          />
        </Stack>
      </Stack>

      {importError && <Alert severity="error">{importError}</Alert>}
      {importSummary && (
        <Alert severity={importSummary.notAddedCount > 0 ? 'warning' : 'success'}>
          Import termine : {importSummary.addedCount} marque{importSummary.addedCount > 1 ? 's' : ''} ajoutee{importSummary.addedCount > 1 ? 's' : ''}, {importSummary.notAddedCount} non ajoutee{importSummary.notAddedCount > 1 ? 's' : ''}.
          {importSummary.notAddedCount > 0 && (
            <> Details : {importSummary.alreadyExistsCount} deja presente{importSummary.alreadyExistsCount > 1 ? 's' : ''}, {importSummary.invalidCount} invalide{importSummary.invalidCount > 1 ? 's' : ''}.</>
          )}
        </Alert>
      )}

      {brands.length === 0 && (
        <Alert severity="info">
          Aucune marque configuree.
        </Alert>
      )}

      {isDesktop ? (
        <TableContainer component={Paper} variant="outlined">
          <Table>
            <TableHead>
              <TableRow>
                <TableCell>Nom</TableCell>
                <TableCell>Description</TableCell>
                <TableCell align="right">Actions</TableCell>
              </TableRow>
            </TableHead>
            <TableBody>
              {brands.map((brand) => (
                <TableRow key={brand.id} hover>
                  <TableCell>
                    <Typography sx={{ fontWeight: 600 }}>{brand.name}</Typography>
                  </TableCell>
                  <TableCell>{brand.description || '-'}</TableCell>
                  <TableCell align="right">
                    <IconButton component={RouterLink} to={`/brand/${brand.id}`} aria-label="Voir la marque">
                      <VisibilityOutlinedIcon fontSize="small" />
                    </IconButton>
                    <IconButton component={RouterLink} to={`/brand/${brand.id}/edit`} aria-label="Modifier la marque">
                      <EditOutlinedIcon fontSize="small" />
                    </IconButton>
                    <IconButton aria-label="Supprimer la marque" color="error" onClick={() => setBrandToDelete(brand)}>
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
          {brands.map((brand) => (
            <Card key={brand.id} variant="outlined">
              <CardContent>
                <Stack spacing={1}>
                  <Typography variant="h6">{brand.name}</Typography>
                  <Typography variant="body2" color="text.secondary">
                    {brand.description || 'Aucune description'}
                  </Typography>
                </Stack>
              </CardContent>
              <CardActions sx={{ px: 2, pb: 2, pt: 0, gap: 1, flexWrap: 'wrap' }}>
                <Button component={RouterLink} to={`/brand/${brand.id}`} size="small" variant="outlined">
                  Voir
                </Button>
                <Button component={RouterLink} to={`/brand/${brand.id}/edit`} size="small" variant="outlined">
                  Modifier
                </Button>
                <Button size="small" color="error" variant="outlined" onClick={() => setBrandToDelete(brand)}>
                  Supprimer
                </Button>
              </CardActions>
            </Card>
          ))}
        </Stack>
      )}

      <Dialog open={Boolean(brandToDelete)} onClose={() => setBrandToDelete(null)}>
        <DialogTitle>Supprimer la marque</DialogTitle>
        <DialogContent>
          <DialogContentText>
            Voulez-vous vraiment supprimer {brandToDelete?.name || 'cette marque'} ?
          </DialogContentText>
        </DialogContent>
        <DialogActions>
          <Button onClick={() => setBrandToDelete(null)} disabled={isDeleting}>Annuler</Button>
          <Button color="error" onClick={handleDelete} disabled={isDeleting}>Supprimer</Button>
        </DialogActions>
      </Dialog>

      <Dialog
        open={importDialogOpen}
        onClose={() => {
          if (!isImporting) {
            setImportDialogOpen(false);
          }
        }}
        fullWidth
        maxWidth="sm"
      >
        <DialogTitle>Importer des marques</DialogTitle>
        <DialogContent>
          <DialogContentText sx={{ mb: 2 }}>
            Collez un texte ou chaque ligne non vide correspond a une marque.
          </DialogContentText>
          <TextField
            autoFocus
            fullWidth
            multiline
            minRows={8}
            label="Marques (une ligne par marque)"
            value={importText}
            onChange={(event) => setImportText(event.target.value)}
            disabled={isImporting}
          />
        </DialogContent>
        <DialogActions>
          <Button onClick={() => setImportDialogOpen(false)} disabled={isImporting}>Annuler</Button>
          <Button onClick={handleImportText} disabled={isImporting} variant="contained">
            {isImporting ? 'Import en cours...' : 'Importer'}
          </Button>
        </DialogActions>
      </Dialog>
    </Stack>
  );
};
