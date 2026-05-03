import { useMemo, useRef, useState, type ChangeEvent, type FC } from 'react';
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
import Typography from '@mui/material/Typography';
import useMediaQuery from '@mui/material/useMediaQuery';
import { useTheme } from '@mui/material/styles';
import DeleteIcon from '@mui/icons-material/Delete';
import EditOutlinedIcon from '@mui/icons-material/EditOutlined';
import LaunchIcon from '@mui/icons-material/Launch';
import DownloadIcon from '@mui/icons-material/Download';
import UploadFileIcon from '@mui/icons-material/UploadFile';
import ManagedUrlForm, {
  emptyManagedUrlFormValues,
  toManagedUrlFormValues,
  toManagedUrlPayload,
  type ManagedUrlFormValues,
} from '../../../components/UrlManager/ManagedUrlForm';
import {
  addManagedUrl,
  createExportFileName,
  deleteManagedUrl,
  normalizeImportedState,
  updateManagedUrl,
} from '../../../features/urlManager/storage';
import { useUrlManagerData } from '../../../features/urlManager/useUrlManagerData';
import type { ManagedUrl } from '../../../features/urlManager/types';
import {
  useImportUrlManagerStateMutation,
  useLazyExportUrlManagerStateQuery,
  useUpdateUrlManagerStateMutation,
} from '../../../services/urlManagerApi';

export const UrlManagerPage: FC = () => {
  const theme = useTheme();
  const isDesktop = useMediaQuery(theme.breakpoints.up('md'));
  const { urls, cards, isLoading, refresh } = useUrlManagerData();
  const [updateState, { isLoading: isSaving }] = useUpdateUrlManagerStateMutation();
  const [importState, { isLoading: isImporting }] = useImportUrlManagerStateMutation();
  const [exportStateTrigger, { isFetching: isExporting }] = useLazyExportUrlManagerStateQuery();
  const [isFormOpen, setIsFormOpen] = useState(false);
  const [editing, setEditing] = useState<ManagedUrl | null>(null);
  const [deleting, setDeleting] = useState<ManagedUrl | null>(null);
  const [importError, setImportError] = useState<string | null>(null);
  const [formValues, setFormValues] = useState<ManagedUrlFormValues>(emptyManagedUrlFormValues);
  const fileInputRef = useRef<HTMLInputElement | null>(null);

  const formTitle = editing ? 'Modifier une URL' : 'Ajouter une URL';

  const hasInvalidForm = useMemo(() => {
    const payload = toManagedUrlPayload(formValues);
    return payload.label.length === 0 || payload.url.length === 0;
  }, [formValues]);

  const openCreate = () => {
    setEditing(null);
    setFormValues(emptyManagedUrlFormValues);
    setIsFormOpen(true);
  };

  const openEdit = (item: ManagedUrl) => {
    setEditing(item);
    setFormValues(toManagedUrlFormValues(item));
    setIsFormOpen(true);
  };

  const closeForm = () => {
    setIsFormOpen(false);
    setEditing(null);
    setFormValues(emptyManagedUrlFormValues);
  };

  const persistState = async (nextUrls: ManagedUrl[]) => {
    await updateState({
      urlManagerState: {
        urls: nextUrls,
        cards,
      },
    }).unwrap();
    await refresh();
  };

  const submitForm = async () => {
    const payload = toManagedUrlPayload(formValues);

    if (payload.label.length === 0 || payload.url.length === 0) {
      return;
    }

    const nextUrls = editing
      ? updateManagedUrl(urls, editing.id, payload)
      : addManagedUrl(urls, payload);

    await persistState(nextUrls);

    closeForm();
  };

  const confirmDelete = async () => {
    if (!deleting) return;

    const nextUrls = deleteManagedUrl(urls, deleting.id);
    await persistState(nextUrls);
    setDeleting(null);
  };

  const handleExport = async () => {
    const payload = await exportStateTrigger().unwrap();
    const blob = new Blob([JSON.stringify(payload, null, 2)], { type: 'application/json' });
    const href = URL.createObjectURL(blob);
    const anchor = document.createElement('a');
    anchor.href = href;
    anchor.download = createExportFileName();
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
      const raw = await file.text();
      const parsed = JSON.parse(raw);
      const normalized = normalizeImportedState(parsed);

      await importState({ urlManagerState: normalized }).unwrap();
      await refresh();
    } catch {
      setImportError('Le fichier JSON est invalide ou incompatible.');
    } finally {
      event.target.value = '';
    }
  };

  if (isLoading) {
    return <div>Chargement...</div>;
  }

  return (
    <Stack spacing={3} sx={{ p: { xs: 2, md: 3 } }}>
      <Stack direction={{ xs: 'column', sm: 'row' }} spacing={1.5} sx={{ alignItems: { sm: 'center' }, justifyContent: 'space-between' }}>
        <Box>
          <Typography variant="h4" component="h1">
            Gestionnaire d'URLs
          </Typography>
          <Typography variant="body2" color="text.secondary">
            Creez des liens et classez-les avec des tags pour les reutiliser sur la page d'accueil.
          </Typography>
        </Box>
        <Stack direction="row" spacing={1} useFlexGap sx={{ flexWrap: 'wrap' }}>
          <Chip label={`${urls.length} URL${urls.length > 1 ? 's' : ''}`} color="primary" variant="outlined" />
          <Button variant="outlined" startIcon={<DownloadIcon />} onClick={handleExport} disabled={isExporting}>
            Export JSON
          </Button>
          <Button variant="outlined" startIcon={<UploadFileIcon />} onClick={() => fileInputRef.current?.click()} disabled={isImporting}>
            Import JSON
          </Button>
          <Button variant="contained" onClick={openCreate}>
            Nouvelle URL
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

      {importError && <Alert severity="error">{importError}</Alert>}

      {urls.length === 0 && (
        <Alert severity="info">
          Aucun lien configure. Ajoutez une URL puis assignez des tags pour l'affichage sur l'accueil.
        </Alert>
      )}

      {isDesktop ? (
        <TableContainer component={Paper} variant="outlined">
          <Table>
            <TableHead>
              <TableRow>
                <TableCell>Libelle</TableCell>
                <TableCell>URL</TableCell>
                <TableCell>Tags</TableCell>
                <TableCell align="right">Actions</TableCell>
              </TableRow>
            </TableHead>
            <TableBody>
              {urls.map((item) => (
                <TableRow key={item.id} hover>
                  <TableCell>
                    <Typography sx={{ fontWeight: 600 }}>{item.label}</Typography>
                    {item.description && (
                      <Typography variant="body2" color="text.secondary">
                        {item.description}
                      </Typography>
                    )}
                  </TableCell>
                  <TableCell>
                    <Typography
                      component="a"
                      href={item.url}
                      target="_blank"
                      rel="noreferrer"
                      sx={{ textDecoration: 'none' }}
                    >
                      {item.url}
                    </Typography>
                  </TableCell>
                  <TableCell>
                    <Stack direction="row" spacing={0.75} useFlexGap sx={{ flexWrap: 'wrap' }}>
                      {item.tags.map((tag) => (
                        <Chip key={tag} size="small" label={tag} variant="outlined" />
                      ))}
                    </Stack>
                  </TableCell>
                  <TableCell align="right">
                    <IconButton aria-label="Ouvrir" component="a" href={item.url} target="_blank" rel="noreferrer">
                      <LaunchIcon fontSize="small" />
                    </IconButton>
                    <IconButton aria-label="Modifier" onClick={() => openEdit(item)}>
                      <EditOutlinedIcon fontSize="small" />
                    </IconButton>
                    <IconButton aria-label="Supprimer" color="error" onClick={() => setDeleting(item)}>
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
          {urls.map((item) => (
            <Card key={item.id} variant="outlined">
              <CardContent>
                <Stack spacing={1.25}>
                  <Typography variant="h6">{item.label}</Typography>
                  <Typography component="a" href={item.url} target="_blank" rel="noreferrer" sx={{ wordBreak: 'break-word' }}>
                    {item.url}
                  </Typography>
                  {item.description && (
                    <Typography variant="body2" color="text.secondary">
                      {item.description}
                    </Typography>
                  )}
                  <Stack direction="row" spacing={0.75} useFlexGap sx={{ flexWrap: 'wrap' }}>
                    {item.tags.map((tag) => (
                      <Chip key={tag} size="small" label={tag} variant="outlined" />
                    ))}
                  </Stack>
                </Stack>
              </CardContent>
              <CardActions sx={{ px: 2, pb: 2, pt: 0, gap: 1, flexWrap: 'wrap' }}>
                <Button size="small" variant="outlined" component="a" href={item.url} target="_blank" rel="noreferrer">
                  Ouvrir
                </Button>
                <Button size="small" variant="outlined" onClick={() => openEdit(item)}>
                  Modifier
                </Button>
                <Button size="small" variant="outlined" color="error" onClick={() => setDeleting(item)}>
                  Supprimer
                </Button>
              </CardActions>
            </Card>
          ))}
        </Stack>
      )}

      <Dialog open={isFormOpen} onClose={closeForm} fullWidth maxWidth="sm">
        <DialogTitle>{formTitle}</DialogTitle>
        <DialogContent>
          <ManagedUrlForm values={formValues} onChange={setFormValues} disabled={isSaving} />
        </DialogContent>
        <DialogActions>
          <Button onClick={closeForm} disabled={isSaving}>Annuler</Button>
          <Button variant="contained" onClick={submitForm} disabled={hasInvalidForm || isSaving}>
            {editing ? 'Enregistrer' : 'Ajouter'}
          </Button>
        </DialogActions>
      </Dialog>

      <Dialog open={Boolean(deleting)} onClose={() => setDeleting(null)}>
        <DialogTitle>Supprimer l'URL</DialogTitle>
        <DialogContent>
          <Typography>
            Voulez-vous supprimer {deleting?.label ?? 'cette URL'} ?
          </Typography>
        </DialogContent>
        <DialogActions>
          <Button onClick={() => setDeleting(null)} disabled={isSaving}>Annuler</Button>
          <Button color="error" onClick={confirmDelete} disabled={isSaving}>Supprimer</Button>
        </DialogActions>
      </Dialog>
    </Stack>
  );
};
