import { useState, type FC } from 'react';
import { Link as RouterLink, useNavigate } from 'react-router-dom';
import Box from '@mui/material/Box';
import Button from '@mui/material/Button';
import Chip from '@mui/material/Chip';
import Dialog from '@mui/material/Dialog';
import DialogActions from '@mui/material/DialogActions';
import DialogContent from '@mui/material/DialogContent';
import DialogContentText from '@mui/material/DialogContentText';
import DialogTitle from '@mui/material/DialogTitle';
import Divider from '@mui/material/Divider';
import IconButton from '@mui/material/IconButton';
import Paper from '@mui/material/Paper';
import Snackbar from '@mui/material/Snackbar';
import Stack from '@mui/material/Stack';
import Tooltip from '@mui/material/Tooltip';
import Typography from '@mui/material/Typography';
import DeleteIcon from '@mui/icons-material/Delete';
import EditOutlinedIcon from '@mui/icons-material/EditOutlined';
import ShareIcon from '@mui/icons-material/Share';
import { useGetBrickByIdQuery, useDeleteBrickByIdMutation } from '../../../services/brickApi';
import type { BrickDetailPageProps } from './BrickDetailPage.types';

export const BrickDetailPage: FC<BrickDetailPageProps> = ({ id }) => {
  const navigate = useNavigate();
  const { data, isLoading, error } = useGetBrickByIdQuery({ id });
  const [deleteBrickById, { isLoading: isDeleting }] = useDeleteBrickByIdMutation();

  const [confirmDeleteOpen, setConfirmDeleteOpen] = useState(false);
  const [snackbarMessage, setSnackbarMessage] = useState<string | null>(null);

  const handleDelete = async () => {
    await deleteBrickById({ id }).unwrap();
    setConfirmDeleteOpen(false);
    void navigate('/brick');
  };

  const handleShare = async () => {
    const url = window.location.href;
    if (navigator.share) {
      try {
        await navigator.share({ title: data?.title ?? 'Brick', url });
      } catch {
        // annulé par l'utilisateur
      }
    } else {
      await navigator.clipboard.writeText(url);
      setSnackbarMessage('URL copiée dans le presse-papiers');
    }
  };

  if (isLoading) return <div>Chargement...</div>;
  if (error) return <div>Erreur lors du chargement de la brick</div>;
  if (!data) return <div>Brick introuvable</div>;

  return (
    <Box sx={{ p: { xs: 2, md: 3 }, maxWidth: 860, mx: 'auto' }}>
      <Stack spacing={3}>
        {/* En-tête */}
        <Stack
          direction={{ xs: 'column', sm: 'row' }}
          spacing={1.5}
          sx={{ justifyContent: 'space-between', alignItems: { sm: 'center' } }}
        >
          <Box>
            <Typography variant="h4" component="h1">
              {data.number}
            </Typography>
            <Typography variant="h6" color="text.secondary">
              {data.title}
            </Typography>
          </Box>

          <Stack direction="row" spacing={1}>
            <Tooltip title="Partager">
              <IconButton onClick={() => void handleShare()} aria-label="Partager">
                <ShareIcon />
              </IconButton>
            </Tooltip>
            <Button
              component={RouterLink}
              to={`/brick/${id}/edit`}
              variant="outlined"
              startIcon={<EditOutlinedIcon />}
            >
              Modifier
            </Button>
            <Button
              variant="outlined"
              color="error"
              startIcon={<DeleteIcon />}
              onClick={() => setConfirmDeleteOpen(true)}
            >
              Supprimer
            </Button>
          </Stack>
        </Stack>

        <Divider />

        {/* Image mise en valeur */}
        {data.imageBase64 ? (
          <Paper
            variant="outlined"
            sx={{
              overflow: 'hidden',
              borderRadius: 2,
              display: 'flex',
              justifyContent: 'center',
              alignItems: 'center',
              bgcolor: 'background.default',
              p: 1,
            }}
          >
            <Box
              component="img"
              src={data.imageBase64}
              alt={data.title ?? 'brick'}
              sx={{
                maxWidth: '100%',
                maxHeight: { xs: 320, md: 480 },
                objectFit: 'contain',
                borderRadius: 1,
              }}
            />
          </Paper>
        ) : (
          <Paper
            variant="outlined"
            sx={{
              height: 160,
              display: 'flex',
              alignItems: 'center',
              justifyContent: 'center',
              borderRadius: 2,
            }}
          >
            <Typography color="text.secondary">Aucune image</Typography>
          </Paper>
        )}

        {/* Détails */}
        <Stack spacing={2}>
          {(data.tags ?? []).length > 0 && (
            <Box>
              <Typography variant="overline" color="text.secondary">Tags</Typography>
              <Stack direction="row" spacing={0.75} useFlexGap sx={{ flexWrap: 'wrap', mt: 0.5 }}>
                {(data.tags ?? []).map((tag) => (
                  <Chip key={tag} label={tag} size="small" variant="outlined" />
                ))}
              </Stack>
            </Box>
          )}

          {data.createdAt && (
            <Box>
              <Typography variant="overline" color="text.secondary">Créé le</Typography>
              <Typography>{new Date(data.createdAt).toLocaleDateString('fr-FR', { dateStyle: 'long' })}</Typography>
            </Box>
          )}

          {data.updatedAt && (
            <Box>
              <Typography variant="overline" color="text.secondary">Modifié le</Typography>
              <Typography>{new Date(data.updatedAt).toLocaleDateString('fr-FR', { dateStyle: 'long' })}</Typography>
            </Box>
          )}

          <Box>
            <Typography variant="overline" color="text.secondary">Identifiant</Typography>
            <Typography color="text.secondary" sx={{ fontFamily: 'monospace', fontSize: '0.85rem' }}>{data.id}</Typography>
          </Box>
        </Stack>

        <Box>
          <Button component={RouterLink} to="/brick" variant="text">
            ← Retour à la liste
          </Button>
        </Box>
      </Stack>

      {/* Dialogue confirmation suppression */}
      <Dialog open={confirmDeleteOpen} onClose={() => setConfirmDeleteOpen(false)}>
        <DialogTitle>Supprimer la brick</DialogTitle>
        <DialogContent>
          <DialogContentText>
            Voulez-vous supprimer la brick <strong>{data.number}</strong> ? Cette action est irréversible.
          </DialogContentText>
        </DialogContent>
        <DialogActions>
          <Button onClick={() => setConfirmDeleteOpen(false)} disabled={isDeleting}>
            Annuler
          </Button>
          <Button color="error" onClick={() => void handleDelete()} disabled={isDeleting}>
            Supprimer
          </Button>
        </DialogActions>
      </Dialog>

      <Snackbar
        open={Boolean(snackbarMessage)}
        autoHideDuration={3000}
        onClose={() => setSnackbarMessage(null)}
        message={snackbarMessage}
      />
    </Box>
  );
};
