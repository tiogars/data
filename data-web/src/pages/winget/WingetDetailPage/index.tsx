import type { FC } from 'react';
import { Link as RouterLink } from 'react-router-dom';
import Box from '@mui/material/Box';
import Button from '@mui/material/Button';
import Chip from '@mui/material/Chip';
import Divider from '@mui/material/Divider';
import Paper from '@mui/material/Paper';
import Stack from '@mui/material/Stack';
import Typography from '@mui/material/Typography';
import ContentCopyIcon from '@mui/icons-material/ContentCopy';
import { useGetWingetQuery } from '../../../services/wingetApi';
import type { WingetDetailPageProps } from './WingetDetailPage.types';

export const WingetDetailPage: FC<WingetDetailPageProps> = ({ id }) => {
  const { data, isLoading, error } = useGetWingetQuery({ id });

  if (isLoading) return <div>Chargement...</div>;
  if (error) return <div>Erreur lors du chargement de l'application Winget</div>;
  if (!data) return <div>Application Winget introuvable</div>;

  return (
    <Paper variant="outlined" sx={{ p: { xs: 2.5, md: 3 }, maxWidth: 760, mx: 'auto', mt: 3 }}>
      <Stack spacing={2}>
        <Stack direction={{ xs: 'column', sm: 'row' }} spacing={1.5} sx={{ justifyContent: 'space-between', alignItems: { sm: 'center' } }}>
          <Typography variant="h4" component="h1">
            {data.name || 'Application Winget'}
          </Typography>
          <Button component={RouterLink} to={`/winget/${id}/edit`} variant="contained">
            Modifier
          </Button>
        </Stack>
        <Divider />
        <Box>
          <Typography variant="overline" color="text.secondary">Nom</Typography>
          <Typography>{data.name}</Typography>
        </Box>
        <Box>
          <Typography variant="overline" color="text.secondary">Description</Typography>
          <Typography>{data.description || 'Aucune description'}</Typography>
        </Box>
        <Box>
          <Typography variant="overline" color="text.secondary">Winget ID</Typography>
          <Typography>{data.wingetId || '-'}</Typography>
        </Box>
        <Box>
          <Typography variant="overline" color="text.secondary">Commande</Typography>
          <Stack direction={{ xs: 'column', sm: 'row' }} spacing={1} sx={{ mt: 0.5 }}>
            <Typography sx={{ wordBreak: 'break-word' }}>{data.installCommand || '-'}</Typography>
            {data.installCommand && navigator?.clipboard && (
              <Button
                size="small"
                variant="outlined"
                startIcon={<ContentCopyIcon fontSize="small" />}
                onClick={() => void navigator.clipboard.writeText(data.installCommand ?? '')}
              >
                Copier
              </Button>
            )}
          </Stack>
        </Box>
        <Box>
          <Typography variant="overline" color="text.secondary">Tags</Typography>
          <Stack direction="row" spacing={0.5} useFlexGap sx={{ flexWrap: 'wrap', mt: 0.5 }}>
            {(data.tags ?? []).length > 0 ? data.tags?.map((tag) => (
              <Chip key={tag} label={tag} size="small" variant="outlined" />
            )) : <Typography>Aucun tag</Typography>}
          </Stack>
        </Box>
      </Stack>
    </Paper>
  );
};
