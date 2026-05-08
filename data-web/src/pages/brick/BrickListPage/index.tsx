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
import DialogContentText from '@mui/material/DialogContentText';
import DialogTitle from '@mui/material/DialogTitle';
import Divider from '@mui/material/Divider';
import IconButton from '@mui/material/IconButton';
import Paper from '@mui/material/Paper';
import Stack from '@mui/material/Stack';
import Switch from '@mui/material/Switch';
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
import LinkIcon from '@mui/icons-material/Link';
import PictureAsPdfIcon from '@mui/icons-material/PictureAsPdf';
import UploadFileIcon from '@mui/icons-material/UploadFile';
import AddIcon from '@mui/icons-material/Add';
import { getAccessToken } from '../../../services/oidcAuth';
import { getGatewayBaseUrl } from '../../../services/emptyApi';
import {
  type Brick,
  type BrickImportForm,
  type ExternalLink,
  useCreateBrickMutation,
  useCreateExternalLinkMutation,
  useDeleteAllBricksMutation,
  useDeleteBrickByIdMutation,
  useDeleteExternalLinkByIdMutation,
  useImportBricksMutation,
  useLazyExportBricksQuery,
  useListBricksQuery,
  useListExternalLinksQuery,
  useUpdateBrickMutation,
  useUpdateExternalLinkMutation,
} from '../../../services/brickApi';

type BrickFormValues = {
  number: string;
  title: string;
  tagsText: string;
  imageBase64: string;
};

type ExternalLinkFormValues = {
  name: string;
  url: string;
  enabled: boolean;
};

const emptyBrickFormValues: BrickFormValues = {
  number: '',
  title: '',
  tagsText: '',
  imageBase64: '',
};

const emptyExternalLinkFormValues: ExternalLinkFormValues = {
  name: '',
  url: '',
  enabled: true,
};

function normalizeTagsFromInput(value: string): string[] {
  const unique = new Set(
    value
      .split(',')
      .map((item) => item.trim().toLowerCase())
      .filter(Boolean)
  );
  return Array.from(unique);
}

function toBrickFormValues(item: Brick): BrickFormValues {
  return {
    number: item.number ?? '',
    title: item.title ?? '',
    tagsText: (item.tags ?? []).join(', '),
    imageBase64: item.imageBase64 ?? '',
  };
}

function toExternalLinkFormValues(item: ExternalLink): ExternalLinkFormValues {
  return {
    name: item.name ?? '',
    url: item.url ?? '',
    enabled: Boolean(item.enabled),
  };
}

function buildExternalUrl(template: string, brickNumber: string): string {
  const encoded = encodeURIComponent(brickNumber);
  if (template.includes('{brick_number}')) {
    return template.replaceAll('{brick_number}', encoded);
  }
  return `${template}${encoded}`;
}

async function fileToDataUrl(file: File): Promise<string> {
  return new Promise((resolve, reject) => {
    const reader = new FileReader();
    reader.onload = () => {
      if (typeof reader.result === 'string') {
        resolve(reader.result);
        return;
      }
      reject(new Error('Format image non supporte.'));
    };
    reader.onerror = () => reject(new Error('Impossible de lire le fichier image.'));
    reader.readAsDataURL(file);
  });
}

function canSubmitBrickForm(values: BrickFormValues): boolean {
  return values.number.trim().length > 0 && values.title.trim().length > 0;
}

function canSubmitExternalLinkForm(values: ExternalLinkFormValues): boolean {
  return values.name.trim().length > 0 && values.url.trim().length > 0;
}

export const BrickListPage: FC = () => {
  const theme = useTheme();
  const isDesktop = useMediaQuery(theme.breakpoints.up('md'));
  const { data: bricksData, isLoading: isBricksLoading, error: bricksError, refetch: refetchBricks } = useListBricksQuery(undefined, { refetchOnMountOrArgChange: true });
  const { data: externalLinksData, isLoading: isLinksLoading, error: linksError, refetch: refetchLinks } = useListExternalLinksQuery(undefined, { refetchOnMountOrArgChange: true });
  const [createBrick, { isLoading: isCreatingBrick }] = useCreateBrickMutation();
  const [updateBrick, { isLoading: isUpdatingBrick }] = useUpdateBrickMutation();
  const [deleteBrickById, { isLoading: isDeletingBrick }] = useDeleteBrickByIdMutation();
  const [deleteAllBricks, { isLoading: isDeletingAllBricks }] = useDeleteAllBricksMutation();
  const [createExternalLink, { isLoading: isCreatingLink }] = useCreateExternalLinkMutation();
  const [updateExternalLink, { isLoading: isUpdatingLink }] = useUpdateExternalLinkMutation();
  const [deleteExternalLinkById, { isLoading: isDeletingLink }] = useDeleteExternalLinkByIdMutation();
  const [importBricks, { isLoading: isImporting }] = useImportBricksMutation();
  const [exportBricksTrigger, { isFetching: isExporting }] = useLazyExportBricksQuery();

  const [brickDialogOpen, setBrickDialogOpen] = useState(false);
  const [brickToEdit, setBrickToEdit] = useState<Brick | null>(null);
  const [brickToDelete, setBrickToDelete] = useState<Brick | null>(null);
  const [confirmDeleteAllBricksOpen, setConfirmDeleteAllBricksOpen] = useState(false);
  const [brickFormValues, setBrickFormValues] = useState<BrickFormValues>(emptyBrickFormValues);

  const [externalLinkDialogOpen, setExternalLinkDialogOpen] = useState(false);
  const [externalLinkToEdit, setExternalLinkToEdit] = useState<ExternalLink | null>(null);
  const [externalLinkToDelete, setExternalLinkToDelete] = useState<ExternalLink | null>(null);
  const [externalLinkFormValues, setExternalLinkFormValues] = useState<ExternalLinkFormValues>(emptyExternalLinkFormValues);

  const [importError, setImportError] = useState<string | null>(null);
  const fileInputRef = useRef<HTMLInputElement | null>(null);
  const imageFileInputRef = useRef<HTMLInputElement | null>(null);

  const bricks = useMemo(() => (bricksData?.items ?? []).filter((item): item is Brick & { id: string } => Boolean(item.id)), [bricksData?.items]);
  const externalLinks = useMemo(
    () => (externalLinksData?.items ?? []).filter((item): item is ExternalLink & { id: string } => Boolean(item.id)),
    [externalLinksData?.items]
  );

  const enabledExternalLinks = useMemo(() => externalLinks.filter((item) => item.enabled), [externalLinks]);
  const isBusy = isCreatingBrick || isUpdatingBrick || isDeletingBrick || isDeletingAllBricks || isCreatingLink || isUpdatingLink || isDeletingLink;

  const openCreateBrickDialog = () => {
    setBrickToEdit(null);
    setBrickFormValues(emptyBrickFormValues);
    setBrickDialogOpen(true);
  };

  const openEditBrickDialog = (brick: Brick) => {
    setBrickToEdit(brick);
    setBrickFormValues(toBrickFormValues(brick));
    setBrickDialogOpen(true);
  };

  const closeBrickDialog = () => {
    setBrickDialogOpen(false);
    setBrickToEdit(null);
    setBrickFormValues(emptyBrickFormValues);
  };

  const submitBrick = async () => {
    if (!canSubmitBrickForm(brickFormValues)) return;

    const payload = {
      number: brickFormValues.number.trim(),
      title: brickFormValues.title.trim(),
      tags: normalizeTagsFromInput(brickFormValues.tagsText),
      imageBase64: brickFormValues.imageBase64.trim() || undefined,
    };

    if (brickToEdit?.id) {
      await updateBrick({
        id: brickToEdit.id,
        brick: {
          ...brickToEdit,
          ...payload,
        },
      }).unwrap();
    } else {
      await createBrick({ brickCreationForm: payload }).unwrap();
    }

    await refetchBricks();
    closeBrickDialog();
  };

  const confirmBrickDelete = async () => {
    if (!brickToDelete?.id) return;
    await deleteBrickById({ id: brickToDelete.id }).unwrap();
    await refetchBricks();
    setBrickToDelete(null);
  };

  const confirmDeleteAllBricks = async () => {
    await deleteAllBricks().unwrap();
    await refetchBricks();
    setConfirmDeleteAllBricksOpen(false);
  };

  const openCreateExternalLinkDialog = () => {
    setExternalLinkToEdit(null);
    setExternalLinkFormValues(emptyExternalLinkFormValues);
    setExternalLinkDialogOpen(true);
  };

  const openEditExternalLinkDialog = (externalLink: ExternalLink) => {
    setExternalLinkToEdit(externalLink);
    setExternalLinkFormValues(toExternalLinkFormValues(externalLink));
    setExternalLinkDialogOpen(true);
  };

  const closeExternalLinkDialog = () => {
    setExternalLinkDialogOpen(false);
    setExternalLinkToEdit(null);
    setExternalLinkFormValues(emptyExternalLinkFormValues);
  };

  const submitExternalLink = async () => {
    if (!canSubmitExternalLinkForm(externalLinkFormValues)) return;

    const payload = {
      name: externalLinkFormValues.name.trim(),
      url: externalLinkFormValues.url.trim(),
      enabled: externalLinkFormValues.enabled,
    };

    if (externalLinkToEdit?.id) {
      await updateExternalLink({
        id: externalLinkToEdit.id,
        externalLink: {
          ...externalLinkToEdit,
          ...payload,
        },
      }).unwrap();
    } else {
      await createExternalLink({ externalLinkCreationForm: payload }).unwrap();
    }

    await refetchLinks();
    closeExternalLinkDialog();
  };

  const confirmExternalLinkDelete = async () => {
    if (!externalLinkToDelete?.id) return;
    await deleteExternalLinkById({ id: externalLinkToDelete.id }).unwrap();
    await refetchLinks();
    setExternalLinkToDelete(null);
  };

  const handleExportJson = async () => {
    const payload = await exportBricksTrigger().unwrap();
    const blob = new Blob([JSON.stringify(payload, null, 2)], { type: 'application/json' });
    const href = URL.createObjectURL(blob);
    const anchor = document.createElement('a');
    const date = new Date();
    const yyyy = date.getFullYear();
    const mm = String(date.getMonth() + 1).padStart(2, '0');
    const dd = String(date.getDate()).padStart(2, '0');
    anchor.href = href;
    anchor.download = `bricks-export-${yyyy}${mm}${dd}.json`;
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
      const parsed = JSON.parse(raw) as BrickImportForm;

      await importBricks({
        brickImportForm: {
          bricks: Array.isArray(parsed.bricks) ? parsed.bricks : [],
          tags: Array.isArray(parsed.tags) ? parsed.tags : [],
          externalLinks: Array.isArray(parsed.externalLinks) ? parsed.externalLinks : [],
        },
      }).unwrap();

      await Promise.all([refetchBricks(), refetchLinks()]);
    } catch {
      setImportError('Le fichier JSON est invalide ou incompatible.');
    } finally {
      event.target.value = '';
    }
  };

  const handleImageUpload = async (event: ChangeEvent<HTMLInputElement>) => {
    const file = event.target.files?.[0];
    if (!file) return;

    try {
      const dataUrl = await fileToDataUrl(file);
      setBrickFormValues((prev) => ({ ...prev, imageBase64: dataUrl }));
    } finally {
      event.target.value = '';
    }
  };

  const handleExportPdf = async () => {
    const accessToken = await getAccessToken();
    const response = await fetch(`${getGatewayBaseUrl()}/brick/pdf`, {
      headers: accessToken ? { Authorization: `Bearer ${accessToken}` } : {},
    });
    if (!response.ok) return;
    const blob = await response.blob();
    const url = URL.createObjectURL(blob);
    const date = new Date();
    const yyyy = date.getFullYear();
    const mm = String(date.getMonth() + 1).padStart(2, '0');
    const dd = String(date.getDate()).padStart(2, '0');
    const a = document.createElement('a');
    a.href = url;
    a.download = `bricks-${yyyy}${mm}${dd}.pdf`;
    a.click();
    URL.revokeObjectURL(url);
  };

  if (isBricksLoading || isLinksLoading) {
    return <div>Chargement...</div>;
  }

  if (bricksError || linksError) {
    return <div>Erreur lors du chargement des donnees brick.</div>;
  }

  return (
    <Stack spacing={3} sx={{ p: { xs: 2, md: 3 } }}>
      <Stack direction={{ xs: 'column', sm: 'row' }} spacing={1.5} useFlexGap sx={{ alignItems: { sm: 'center' }, justifyContent: 'space-between' }}>
        <Box>
          <Typography variant="h4" component="h1">
            Bricks
          </Typography>
          <Typography variant="body2" color="text.secondary">
            Gere ta collection de bricks, les liens externes et les imports/exports.
          </Typography>
        </Box>
        <Stack direction="row" spacing={1} useFlexGap sx={{ flexWrap: 'wrap' }}>
          <Chip label={`${bricks.length} brick${bricks.length > 1 ? 's' : ''}`} color="primary" variant="outlined" />
          <Button variant="outlined" startIcon={<DownloadIcon />} onClick={handleExportJson} disabled={isExporting}>
            Export JSON
          </Button>
          <Button variant="outlined" startIcon={<UploadFileIcon />} onClick={() => fileInputRef.current?.click()} disabled={isImporting}>
            Import JSON
          </Button>
          <Button variant="outlined" startIcon={<PictureAsPdfIcon />} onClick={() => void handleExportPdf()}>
            Export PDF
          </Button>
          <Button
            variant="outlined"
            color="error"
            startIcon={<DeleteIcon />}
            onClick={() => setConfirmDeleteAllBricksOpen(true)}
            disabled={bricks.length === 0 || isDeletingAllBricks}
          >
            Tout supprimer
          </Button>
          <Button variant="contained" startIcon={<AddIcon />} onClick={openCreateBrickDialog}>
            Nouvelle brick
          </Button>
          <input ref={fileInputRef} type="file" accept="application/json" hidden onChange={handleImportFile} />
        </Stack>
      </Stack>

      {importError && <Alert severity="error">{importError}</Alert>}

      {bricks.length === 0 && (
        <Alert severity="info">
          Aucune brick configuree.
        </Alert>
      )}

      {isDesktop ? (
        <TableContainer component={Paper} variant="outlined">
          <Table>
            <TableHead>
              <TableRow>
                <TableCell>Image</TableCell>
                <TableCell>Numero</TableCell>
                <TableCell>Titre</TableCell>
                <TableCell>Tags</TableCell>
                <TableCell>Liens web</TableCell>
                <TableCell align="right">Actions</TableCell>
              </TableRow>
            </TableHead>
            <TableBody>
              {bricks.map((brick) => (
                <TableRow key={brick.id} hover>
                  <TableCell>
                    {brick.imageBase64 ? (
                      <Box
                        component="img"
                        src={brick.imageBase64}
                        alt={brick.title ?? 'brick'}
                        sx={{ width: 64, height: 64, objectFit: 'cover', borderRadius: 1, border: '1px solid', borderColor: 'divider' }}
                      />
                    ) : (
                      <Typography variant="body2" color="text.secondary">Sans image</Typography>
                    )}
                  </TableCell>
                  <TableCell>
                    <Typography sx={{ fontWeight: 600 }}>{brick.number}</Typography>
                  </TableCell>
                  <TableCell>{brick.title}</TableCell>
                  <TableCell>
                    <Stack direction="row" spacing={0.75} useFlexGap sx={{ flexWrap: 'wrap' }}>
                      {(brick.tags ?? []).map((tag) => (
                        <Chip key={`${brick.id}-${tag}`} size="small" label={tag} variant="outlined" />
                      ))}
                    </Stack>
                  </TableCell>
                  <TableCell>
                    <Stack direction="row" spacing={0.75} useFlexGap sx={{ flexWrap: 'wrap' }}>
                      {enabledExternalLinks.map((link) => (
                        <Button
                          key={`${brick.id}-${link.id}`}
                          size="small"
                          variant="outlined"
                          component="a"
                          target="_blank"
                          rel="noreferrer"
                          href={buildExternalUrl(link.url ?? '', brick.number ?? '')}
                          startIcon={<LinkIcon />}
                        >
                          {link.name}
                        </Button>
                      ))}
                    </Stack>
                  </TableCell>
                  <TableCell align="right">
                    <IconButton aria-label="Modifier" onClick={() => openEditBrickDialog(brick)}>
                      <EditOutlinedIcon fontSize="small" />
                    </IconButton>
                    <IconButton aria-label="Supprimer" color="error" onClick={() => setBrickToDelete(brick)}>
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
          {bricks.map((brick) => (
            <Card key={brick.id} variant="outlined">
              <CardContent>
                <Stack spacing={1.25}>
                  {brick.imageBase64 && (
                    <Box
                      component="img"
                      src={brick.imageBase64}
                      alt={brick.title ?? 'brick'}
                      sx={{ width: '100%', maxHeight: 220, objectFit: 'contain', borderRadius: 1, border: '1px solid', borderColor: 'divider' }}
                    />
                  )}
                  <Typography variant="h6">{brick.number} - {brick.title}</Typography>
                  <Stack direction="row" spacing={0.75} useFlexGap sx={{ flexWrap: 'wrap' }}>
                    {(brick.tags ?? []).map((tag) => (
                      <Chip key={`${brick.id}-${tag}`} size="small" label={tag} variant="outlined" />
                    ))}
                  </Stack>
                  <Stack direction="row" spacing={0.75} useFlexGap sx={{ flexWrap: 'wrap' }}>
                    {enabledExternalLinks.map((link) => (
                      <Button
                        key={`${brick.id}-${link.id}`}
                        size="small"
                        variant="outlined"
                        component="a"
                        target="_blank"
                        rel="noreferrer"
                        href={buildExternalUrl(link.url ?? '', brick.number ?? '')}
                      >
                        {link.name}
                      </Button>
                    ))}
                  </Stack>
                </Stack>
              </CardContent>
              <CardActions sx={{ px: 2, pb: 2, pt: 0, gap: 1, flexWrap: 'wrap' }}>
                <Button size="small" variant="outlined" onClick={() => openEditBrickDialog(brick)}>
                  Modifier
                </Button>
                <Button size="small" variant="outlined" color="error" onClick={() => setBrickToDelete(brick)}>
                  Supprimer
                </Button>
              </CardActions>
            </Card>
          ))}
        </Stack>
      )}

      <Divider />

      <Stack spacing={1.5}>
        <Stack direction={{ xs: 'column', sm: 'row' }} spacing={1.5} sx={{ alignItems: { sm: 'center' }, justifyContent: 'space-between' }}>
          <Box>
            <Typography variant="h5" component="h2">External links</Typography>
            <Typography variant="body2" color="text.secondary">
              Gere la liste globale des liens web utilises pour chaque brick.
            </Typography>
          </Box>
          <Button variant="contained" startIcon={<AddIcon />} onClick={openCreateExternalLinkDialog}>
            Nouveau lien
          </Button>
        </Stack>

        {externalLinks.length === 0 && <Alert severity="info">Aucun lien externe configure.</Alert>}

        <Stack spacing={1}>
          {externalLinks.map((link) => (
            <Card key={link.id} variant="outlined">
              <CardContent>
                <Stack direction={{ xs: 'column', md: 'row' }} spacing={1.5} sx={{ justifyContent: 'space-between' }}>
                  <Box>
                    <Typography variant="subtitle1" sx={{ fontWeight: 600 }}>{link.name}</Typography>
                    <Typography variant="body2" sx={{ wordBreak: 'break-word' }} color="text.secondary">
                      {link.url}
                    </Typography>
                  </Box>
                  <Stack direction="row" spacing={1} useFlexGap sx={{ alignItems: 'center', flexWrap: 'wrap' }}>
                    <Chip label={link.enabled ? 'Actif' : 'Inactif'} color={link.enabled ? 'success' : 'default'} size="small" />
                    <Button size="small" variant="outlined" onClick={() => openEditExternalLinkDialog(link)}>Modifier</Button>
                    <Button size="small" variant="outlined" color="error" onClick={() => setExternalLinkToDelete(link)}>Supprimer</Button>
                  </Stack>
                </Stack>
              </CardContent>
            </Card>
          ))}
        </Stack>
      </Stack>

      <Dialog open={brickDialogOpen} onClose={closeBrickDialog} fullWidth maxWidth="md">
        <DialogTitle>{brickToEdit ? 'Modifier la brick' : 'Nouvelle brick'}</DialogTitle>
        <DialogContent>
          <Stack spacing={2} sx={{ mt: 0.5 }}>
            <TextField
              label="Numero"
              value={brickFormValues.number}
              onChange={(event) => setBrickFormValues((prev) => ({ ...prev, number: event.target.value }))}
              required
              fullWidth
            />
            <TextField
              label="Titre"
              value={brickFormValues.title}
              onChange={(event) => setBrickFormValues((prev) => ({ ...prev, title: event.target.value }))}
              required
              fullWidth
            />
            <TextField
              label="Tags"
              helperText="Separes par des virgules"
              value={brickFormValues.tagsText}
              onChange={(event) => setBrickFormValues((prev) => ({ ...prev, tagsText: event.target.value }))}
              fullWidth
            />
            <TextField
              label="Image base64"
              helperText="Data URL: data:image/...;base64,..."
              value={brickFormValues.imageBase64}
              onChange={(event) => setBrickFormValues((prev) => ({ ...prev, imageBase64: event.target.value }))}
              multiline
              minRows={3}
              fullWidth
            />
            <Stack direction="row" spacing={1} useFlexGap sx={{ flexWrap: 'wrap' }}>
              <Button variant="outlined" onClick={() => imageFileInputRef.current?.click()}>
                Charger une image
              </Button>
              {brickFormValues.imageBase64 && (
                <Button variant="outlined" color="warning" onClick={() => setBrickFormValues((prev) => ({ ...prev, imageBase64: '' }))}>
                  Retirer l'image
                </Button>
              )}
              <input ref={imageFileInputRef} type="file" accept="image/*" hidden onChange={handleImageUpload} />
            </Stack>
            {brickFormValues.imageBase64 && (
              <Box
                component="img"
                src={brickFormValues.imageBase64}
                alt="Preview"
                sx={{ width: '100%', maxHeight: 280, objectFit: 'contain', borderRadius: 1, border: '1px solid', borderColor: 'divider' }}
              />
            )}
          </Stack>
        </DialogContent>
        <DialogActions>
          <Button onClick={closeBrickDialog} disabled={isBusy}>Annuler</Button>
          <Button variant="contained" onClick={submitBrick} disabled={!canSubmitBrickForm(brickFormValues) || isBusy}>
            {brickToEdit ? 'Enregistrer' : 'Ajouter'}
          </Button>
        </DialogActions>
      </Dialog>

      <Dialog open={Boolean(brickToDelete)} onClose={() => setBrickToDelete(null)}>
        <DialogTitle>Supprimer la brick</DialogTitle>
        <DialogContent>
          <DialogContentText>
            Voulez-vous supprimer {brickToDelete?.number ?? 'cette brick'} ?
          </DialogContentText>
        </DialogContent>
        <DialogActions>
          <Button onClick={() => setBrickToDelete(null)} disabled={isDeletingBrick}>Annuler</Button>
          <Button color="error" onClick={confirmBrickDelete} disabled={isDeletingBrick}>Supprimer</Button>
        </DialogActions>
      </Dialog>

      <Dialog open={confirmDeleteAllBricksOpen} onClose={() => setConfirmDeleteAllBricksOpen(false)}>
        <DialogTitle>Supprimer toutes les bricks</DialogTitle>
        <DialogContent>
          <DialogContentText>
            Cette action va supprimer toutes les bricks ({bricks.length}). Voulez-vous continuer ?
          </DialogContentText>
        </DialogContent>
        <DialogActions>
          <Button onClick={() => setConfirmDeleteAllBricksOpen(false)} disabled={isDeletingAllBricks}>Annuler</Button>
          <Button color="error" onClick={confirmDeleteAllBricks} disabled={isDeletingAllBricks}>Tout supprimer</Button>
        </DialogActions>
      </Dialog>

      <Dialog open={externalLinkDialogOpen} onClose={closeExternalLinkDialog} fullWidth maxWidth="sm">
        <DialogTitle>{externalLinkToEdit ? 'Modifier le lien externe' : 'Nouveau lien externe'}</DialogTitle>
        <DialogContent>
          <Stack spacing={2} sx={{ mt: 0.5 }}>
            <TextField
              label="Nom"
              value={externalLinkFormValues.name}
              onChange={(event) => setExternalLinkFormValues((prev) => ({ ...prev, name: event.target.value }))}
              required
              fullWidth
            />
            <TextField
              label="URL template"
              helperText="Utilise {brick_number} ou un suffixe pour injecter le numero"
              value={externalLinkFormValues.url}
              onChange={(event) => setExternalLinkFormValues((prev) => ({ ...prev, url: event.target.value }))}
              required
              fullWidth
            />
            <Stack direction="row" spacing={1} sx={{ alignItems: 'center' }}>
              <Switch
                checked={externalLinkFormValues.enabled}
                onChange={(event) => setExternalLinkFormValues((prev) => ({ ...prev, enabled: event.target.checked }))}
              />
              <Typography variant="body2">Lien actif</Typography>
            </Stack>
          </Stack>
        </DialogContent>
        <DialogActions>
          <Button onClick={closeExternalLinkDialog} disabled={isBusy}>Annuler</Button>
          <Button variant="contained" onClick={submitExternalLink} disabled={!canSubmitExternalLinkForm(externalLinkFormValues) || isBusy}>
            {externalLinkToEdit ? 'Enregistrer' : 'Ajouter'}
          </Button>
        </DialogActions>
      </Dialog>

      <Dialog open={Boolean(externalLinkToDelete)} onClose={() => setExternalLinkToDelete(null)}>
        <DialogTitle>Supprimer le lien externe</DialogTitle>
        <DialogContent>
          <DialogContentText>
            Voulez-vous supprimer {externalLinkToDelete?.name ?? 'ce lien'} ?
          </DialogContentText>
        </DialogContent>
        <DialogActions>
          <Button onClick={() => setExternalLinkToDelete(null)} disabled={isDeletingLink}>Annuler</Button>
          <Button color="error" onClick={confirmExternalLinkDelete} disabled={isDeletingLink}>Supprimer</Button>
        </DialogActions>
      </Dialog>
    </Stack>
  );
};
