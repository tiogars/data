import { useState, type FC } from 'react';
import { Link as RouterLink } from 'react-router-dom';
import Alert from '@mui/material/Alert';
import Box from '@mui/material/Box';
import Button from '@mui/material/Button';
import Divider from '@mui/material/Divider';
import List from '@mui/material/List';
import ListItem from '@mui/material/ListItem';
import ListItemText from '@mui/material/ListItemText';
import Paper from '@mui/material/Paper';
import Stack from '@mui/material/Stack';
import Typography from '@mui/material/Typography';
import SmartToyIcon from '@mui/icons-material/SmartToy';
import ContentCopyIcon from '@mui/icons-material/ContentCopy';
import { useGetModelByIdQuery, useLazyGetModelAiTextQuery } from '../../../services/modelApi';
import type { ModelDetailPageProps } from './ModelDetailPage.types';

export const ModelDetailPage: FC<ModelDetailPageProps> = ({ id }) => {
  const { data, isLoading, error } = useGetModelByIdQuery({ id });
  const [getModelAiText, { isFetching: isGenerating }] = useLazyGetModelAiTextQuery();
  const [aiText, setAiText] = useState<string | null>(null);
  const [copySuccess, setCopySuccess] = useState<string | null>(null);
  const [copyError, setCopyError] = useState<string | null>(null);

  const handleGenerateAiText = async () => {
    setCopySuccess(null);
    setCopyError(null);

    try {
      const payload = await getModelAiText({ id }).unwrap();
      setAiText(payload.text ?? null);

      if (payload.text && payload.text.length > 0) {
        await navigator.clipboard.writeText(payload.text);
        setCopySuccess('Texte IA genere et copie dans le presse-papiers.');
      } else {
        setCopyError('Le service a retourne un texte vide.');
      }
    } catch {
      setCopyError('Erreur lors de la generation du texte IA.');
    }
  };

  const handleCopyAiText = async () => {
    if (!aiText) {
      setCopyError('Aucun texte IA a copier.');
      return;
    }

    try {
      await navigator.clipboard.writeText(aiText);
      setCopySuccess('Texte IA copie dans le presse-papiers.');
      setCopyError(null);
    } catch {
      setCopyError('Impossible de copier le texte IA.');
    }
  };

  if (isLoading) return <div>Chargement...</div>;
  if (error) return <div>Erreur lors du chargement du modele</div>;
  if (!data) return <div>Modele introuvable</div>;

  return (
    <Paper variant="outlined" sx={{ p: { xs: 2.5, md: 3 }, maxWidth: 760, mx: 'auto', mt: 3 }}>
      <Stack spacing={2}>
        <Stack direction={{ xs: 'column', sm: 'row' }} spacing={1.5} sx={{ justifyContent: 'space-between', alignItems: { sm: 'center' } }}>
          <Typography variant="h4" component="h1">
            {data.name || 'Modele'}
          </Typography>
          <Stack direction={{ xs: 'column', sm: 'row' }} spacing={1}>
            <Button
              variant="outlined"
              startIcon={<SmartToyIcon />}
              onClick={handleGenerateAiText}
              disabled={isGenerating}
            >
              Convertir en texte IA
            </Button>
            <Button component={RouterLink} to={`/model/${id}/edit`} variant="contained">
              Modifier
            </Button>
          </Stack>
        </Stack>
        <Divider />
        {copySuccess && <Alert severity="success">{copySuccess}</Alert>}
        {copyError && <Alert severity="error">{copyError}</Alert>}
        <Box>
          <Typography variant="overline" color="text.secondary">Nom</Typography>
          <Typography>{data.name}</Typography>
        </Box>
        <Box>
          <Typography variant="overline" color="text.secondary">Description</Typography>
          <Typography>{data.description || 'Aucune description'}</Typography>
        </Box>
        <Box>
          <Typography variant="overline" color="text.secondary">Attributs</Typography>
          {(!data.modelAttributes || data.modelAttributes.length === 0) ? (
            <Typography>Aucun attribut</Typography>
          ) : (
            <List disablePadding>
              {data.modelAttributes.map((attribute, index) => (
                <ListItem key={attribute.id ?? `${attribute.name ?? 'attr'}-${index}`} disableGutters>
                  <ListItemText
                    primary={attribute.name || 'Attribut'}
                    secondary={attribute.description || 'Aucune description'}
                  />
                </ListItem>
              ))}
            </List>
          )}
        </Box>
        <Box>
          <Typography variant="overline" color="text.secondary">Identifiant</Typography>
          <Typography color="text.secondary">{data.id}</Typography>
        </Box>
        {aiText && (
          <Box>
            <Stack direction={{ xs: 'column', sm: 'row' }} spacing={1} sx={{ justifyContent: 'space-between', alignItems: { sm: 'center' } }}>
              <Typography variant="overline" color="text.secondary">Texte IA</Typography>
              <Button size="small" variant="outlined" startIcon={<ContentCopyIcon />} onClick={handleCopyAiText}>
                Copier
              </Button>
            </Stack>
            <Paper variant="outlined" sx={{ p: 1.5, mt: 0.5, backgroundColor: 'background.default' }}>
              <Typography component="pre" sx={{ m: 0, whiteSpace: 'pre-wrap', fontFamily: 'monospace', fontSize: 13 }}>
                {aiText}
              </Typography>
            </Paper>
          </Box>
        )}
      </Stack>
    </Paper>
  );
};
