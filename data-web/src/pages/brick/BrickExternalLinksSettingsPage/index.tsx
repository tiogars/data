import { useMemo, useState, type FC } from 'react';
import Alert from '@mui/material/Alert';
import Box from '@mui/material/Box';
import Button from '@mui/material/Button';
import Card from '@mui/material/Card';
import CardContent from '@mui/material/CardContent';
import Chip from '@mui/material/Chip';
import Dialog from '@mui/material/Dialog';
import DialogActions from '@mui/material/DialogActions';
import DialogContent from '@mui/material/DialogContent';
import DialogContentText from '@mui/material/DialogContentText';
import DialogTitle from '@mui/material/DialogTitle';
import Stack from '@mui/material/Stack';
import Switch from '@mui/material/Switch';
import TextField from '@mui/material/TextField';
import Typography from '@mui/material/Typography';
import AddIcon from '@mui/icons-material/Add';
import {
  type ExternalLink,
  useCreateExternalLinkMutation,
  useDeleteExternalLinkByIdMutation,
  useListExternalLinksQuery,
  useUpdateExternalLinkMutation,
} from '../../../services/brickApi';

type ExternalLinkFormValues = {
  name: string;
  url: string;
  enabled: boolean;
};

const emptyExternalLinkFormValues: ExternalLinkFormValues = {
  name: '',
  url: '',
  enabled: true,
};

function toExternalLinkFormValues(item: ExternalLink): ExternalLinkFormValues {
  return {
    name: item.name ?? '',
    url: item.url ?? '',
    enabled: Boolean(item.enabled),
  };
}

function canSubmitExternalLinkForm(values: ExternalLinkFormValues): boolean {
  return values.name.trim().length > 0 && values.url.trim().length > 0;
}

export const BrickExternalLinksSettingsPage: FC = () => {
  const { data: externalLinksData, isLoading, error, refetch } = useListExternalLinksQuery(undefined, {
    refetchOnMountOrArgChange: true,
  });

  const [createExternalLink, { isLoading: isCreatingLink }] = useCreateExternalLinkMutation();
  const [updateExternalLink, { isLoading: isUpdatingLink }] = useUpdateExternalLinkMutation();
  const [deleteExternalLinkById, { isLoading: isDeletingLink }] = useDeleteExternalLinkByIdMutation();

  const [externalLinkDialogOpen, setExternalLinkDialogOpen] = useState(false);
  const [externalLinkToEdit, setExternalLinkToEdit] = useState<ExternalLink | null>(null);
  const [externalLinkToDelete, setExternalLinkToDelete] = useState<ExternalLink | null>(null);
  const [externalLinkFormValues, setExternalLinkFormValues] =
    useState<ExternalLinkFormValues>(emptyExternalLinkFormValues);

  const externalLinks = useMemo(
    () => (externalLinksData?.items ?? []).filter((item): item is ExternalLink & { id: string } => Boolean(item.id)),
    [externalLinksData?.items]
  );

  const isBusy = isCreatingLink || isUpdatingLink || isDeletingLink;

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

    await refetch();
    closeExternalLinkDialog();
  };

  const confirmExternalLinkDelete = async () => {
    if (!externalLinkToDelete?.id) return;
    await deleteExternalLinkById({ id: externalLinkToDelete.id }).unwrap();
    await refetch();
    setExternalLinkToDelete(null);
  };

  if (isLoading) {
    return <div>Chargement...</div>;
  }

  if (error) {
    return <div>Erreur lors du chargement des liens externes.</div>;
  }

  return (
    <Stack spacing={3} sx={{ p: { xs: 2, md: 3 } }}>
      <Stack
        direction={{ xs: 'column', sm: 'row' }}
        spacing={1.5}
        sx={{ alignItems: { sm: 'center' }, justifyContent: 'space-between' }}
      >
        <Box>
          <Typography variant="h4" component="h1">
            External links
          </Typography>
          <Typography variant="body2" color="text.secondary">
            Gere la liste globale des liens web utilises pour chaque brick.
          </Typography>
        </Box>
        <Stack direction="row" spacing={1} useFlexGap sx={{ flexWrap: 'wrap' }}>
          <Chip
            label={`${externalLinks.length} lien${externalLinks.length > 1 ? 's' : ''}`}
            color="primary"
            variant="outlined"
          />
          <Button variant="contained" startIcon={<AddIcon />} onClick={openCreateExternalLinkDialog}>
            Nouveau lien
          </Button>
        </Stack>
      </Stack>

      {externalLinks.length === 0 && <Alert severity="info">Aucun lien externe configure.</Alert>}

      <Stack spacing={1}>
        {externalLinks.map((link) => (
          <Card key={link.id} variant="outlined">
            <CardContent>
              <Stack direction={{ xs: 'column', md: 'row' }} spacing={1.5} sx={{ justifyContent: 'space-between' }}>
                <Box>
                  <Typography variant="subtitle1" sx={{ fontWeight: 600 }}>
                    {link.name}
                  </Typography>
                  <Typography variant="body2" sx={{ wordBreak: 'break-word' }} color="text.secondary">
                    {link.url}
                  </Typography>
                </Box>
                <Stack direction="row" spacing={1} useFlexGap sx={{ alignItems: 'center', flexWrap: 'wrap' }}>
                  <Chip label={link.enabled ? 'Actif' : 'Inactif'} color={link.enabled ? 'success' : 'default'} size="small" />
                  <Button size="small" variant="outlined" onClick={() => openEditExternalLinkDialog(link)}>
                    Modifier
                  </Button>
                  <Button size="small" variant="outlined" color="error" onClick={() => setExternalLinkToDelete(link)}>
                    Supprimer
                  </Button>
                </Stack>
              </Stack>
            </CardContent>
          </Card>
        ))}
      </Stack>

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
                onChange={(event) =>
                  setExternalLinkFormValues((prev) => ({
                    ...prev,
                    enabled: event.target.checked,
                  }))
                }
              />
              <Typography variant="body2">Lien actif</Typography>
            </Stack>
          </Stack>
        </DialogContent>
        <DialogActions>
          <Button onClick={closeExternalLinkDialog} disabled={isBusy}>
            Annuler
          </Button>
          <Button
            variant="contained"
            onClick={submitExternalLink}
            disabled={!canSubmitExternalLinkForm(externalLinkFormValues) || isBusy}
          >
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
          <Button onClick={() => setExternalLinkToDelete(null)} disabled={isDeletingLink}>
            Annuler
          </Button>
          <Button color="error" onClick={confirmExternalLinkDelete} disabled={isDeletingLink}>
            Supprimer
          </Button>
        </DialogActions>
      </Dialog>
    </Stack>
  );
};
