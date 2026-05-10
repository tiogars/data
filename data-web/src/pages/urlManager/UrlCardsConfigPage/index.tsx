import { useMemo, useState, type FC } from 'react';
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
import Stack from '@mui/material/Stack';
import Typography from '@mui/material/Typography';
import UrlCardConfigForm, {
  emptyUrlCardConfigFormValues,
  toUrlCardFormValues,
  toUrlCardPayload,
  type UrlCardConfigFormValues,
} from '../../../components/UrlManager/UrlCardConfigForm';
import { addUrlCard, deleteUrlCard, getAllKnownTags, updateUrlCard } from '../../../features/urlManager/storage';
import { useUrlManagerData } from '../../../features/urlManager/useUrlManagerData';
import type { UrlCardConfig } from '../../../features/urlManager/types';
import { useUpdateStateMutation } from '../../../services/urlManagerApi';

export const UrlCardsConfigPage: FC = () => {
  const { urls, cards, isLoading, refresh } = useUrlManagerData();
  const [updateState, { isLoading: isSaving }] = useUpdateStateMutation();
  const [isFormOpen, setIsFormOpen] = useState(false);
  const [editing, setEditing] = useState<UrlCardConfig | null>(null);
  const [deleting, setDeleting] = useState<UrlCardConfig | null>(null);
  const [formValues, setFormValues] = useState<UrlCardConfigFormValues>(emptyUrlCardConfigFormValues);

  const knownTags = useMemo(() => getAllKnownTags(urls), [urls]);

  const hasInvalidForm = useMemo(() => {
    const payload = toUrlCardPayload(formValues);
    return payload.title.length === 0 || payload.tags.length === 0;
  }, [formValues]);

  const openCreate = () => {
    setEditing(null);
    setFormValues(emptyUrlCardConfigFormValues);
    setIsFormOpen(true);
  };

  const openEdit = (item: UrlCardConfig) => {
    setEditing(item);
    setFormValues(toUrlCardFormValues(item));
    setIsFormOpen(true);
  };

  const closeForm = () => {
    setIsFormOpen(false);
    setEditing(null);
    setFormValues(emptyUrlCardConfigFormValues);
  };

  const persistState = async (nextCards: UrlCardConfig[]) => {
    await updateState({
      urlManagerState: {
        urls,
        cards: nextCards,
      },
    }).unwrap();
    await refresh();
  };

  const submitForm = async () => {
    const payload = toUrlCardPayload(formValues);

    if (payload.title.length === 0 || payload.tags.length === 0) {
      return;
    }

    const nextCards = editing
      ? updateUrlCard(cards, editing.id, payload)
      : addUrlCard(cards, payload);

    await persistState(nextCards);
    closeForm();
  };

  const confirmDelete = async () => {
    if (!deleting) return;

    const nextCards = deleteUrlCard(cards, deleting.id);
    await persistState(nextCards);
    setDeleting(null);
  };

  if (isLoading) {
    return <div>Chargement...</div>;
  }

  return (
    <Stack spacing={3} sx={{ p: { xs: 2, md: 3 } }}>
      <Stack direction={{ xs: 'column', sm: 'row' }} spacing={1.5} sx={{ alignItems: { sm: 'center' }, justifyContent: 'space-between' }}>
        <Box>
          <Typography variant="h4" component="h1">
            Cartes de liens accueil
          </Typography>
          <Typography variant="body2" color="text.secondary">
            Parametrez les cartes affichees sur l'accueil par tag unique ou reunion de tags.
          </Typography>
        </Box>
        <Stack direction="row" spacing={1}>
          <Chip label={`${cards.length} carte${cards.length > 1 ? 's' : ''}`} color="primary" variant="outlined" />
          <Button variant="contained" onClick={openCreate}>Nouvelle carte</Button>
        </Stack>
      </Stack>

      {knownTags.length === 0 && (
        <Alert severity="warning">
          Aucun tag disponible. Commencez par creer des URLs taguees dans le gestionnaire d'URLs.
        </Alert>
      )}

      {cards.length === 0 && (
        <Alert severity="info">
          Aucune carte configuree. Les cartes apparaissant sur la page d'accueil seront definies ici.
        </Alert>
      )}

      <Stack spacing={2}>
        {cards.map((card) => (
          <Card key={card.id} variant="outlined">
            <CardContent>
              <Stack spacing={1}>
                <Typography variant="h6">{card.title}</Typography>
                <Typography variant="body2" color="text.secondary">
                  {card.matchMode === 'any' ? 'Filtre: au moins un tag' : 'Filtre: tous les tags'}
                </Typography>
                <Stack direction="row" spacing={0.75} useFlexGap sx={{ flexWrap: 'wrap' }}>
                  {card.tags.map((tag) => (
                    <Chip key={tag} size="small" label={tag} variant="outlined" />
                  ))}
                </Stack>
              </Stack>
            </CardContent>
            <CardActions sx={{ px: 2, pb: 2, pt: 0, gap: 1, flexWrap: 'wrap' }}>
              <Button size="small" variant="outlined" onClick={() => openEdit(card)}>Modifier</Button>
              <Button size="small" variant="outlined" color="error" onClick={() => setDeleting(card)}>Supprimer</Button>
            </CardActions>
          </Card>
        ))}
      </Stack>

      {knownTags.length > 0 && (
        <Box>
          <Typography variant="overline" color="text.secondary">Tags disponibles</Typography>
          <Stack direction="row" spacing={0.75} useFlexGap sx={{ flexWrap: 'wrap', mt: 1 }}>
            {knownTags.map((tag) => (
              <Chip key={tag} size="small" label={tag} />
            ))}
          </Stack>
        </Box>
      )}

      <Dialog open={isFormOpen} onClose={closeForm} fullWidth maxWidth="sm">
        <DialogTitle>{editing ? 'Modifier une carte' : 'Ajouter une carte'}</DialogTitle>
        <DialogContent>
          <UrlCardConfigForm values={formValues} onChange={setFormValues} disabled={isSaving} />
        </DialogContent>
        <DialogActions>
          <Button onClick={closeForm} disabled={isSaving}>Annuler</Button>
          <Button variant="contained" onClick={submitForm} disabled={hasInvalidForm || isSaving}>
            {editing ? 'Enregistrer' : 'Ajouter'}
          </Button>
        </DialogActions>
      </Dialog>

      <Dialog open={Boolean(deleting)} onClose={() => setDeleting(null)}>
        <DialogTitle>Supprimer la carte</DialogTitle>
        <DialogContent>
          <Typography>
            Voulez-vous supprimer la carte {deleting?.title ?? ''} ?
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
