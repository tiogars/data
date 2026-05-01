import type { FC } from 'react';
import { Link as RouterLink } from 'react-router-dom';
import Box from '@mui/material/Box';
import Button from '@mui/material/Button';
import Divider from '@mui/material/Divider';
import Link from '@mui/material/Link';
import Paper from '@mui/material/Paper';
import Stack from '@mui/material/Stack';
import Typography from '@mui/material/Typography';
import { renderFooterLinkIcon } from '../../../features/footerLink/iconRegistry';
import { useGetFooterLinkByIdQuery } from '../../../services/footerLinkApi';
import type { FooterLinkDetailPageProps } from './FooterLinkDetailPage.types';

export const FooterLinkDetailPage: FC<FooterLinkDetailPageProps> = ({ id }) => {
  const { data, isLoading, error } = useGetFooterLinkByIdQuery({ id });

  if (isLoading) return <div>Chargement...</div>;
  if (error) return <div>Erreur lors du chargement du lien footer</div>;
  if (!data) return <div>Lien footer introuvable</div>;

  return (
    <Paper variant="outlined" sx={{ p: { xs: 2.5, md: 3 }, maxWidth: 760, mx: 'auto', mt: 3 }}>
      <Stack spacing={2}>
        <Stack direction={{ xs: 'column', sm: 'row' }} spacing={1.5} sx={{ justifyContent: 'space-between', alignItems: { sm: 'center' } }}>
          <Typography variant="h4" component="h1">
            {data.label || 'Lien footer'}
          </Typography>
          <Button component={RouterLink} to={`/footer-link/${id}/edit`} variant="contained">
            Modifier
          </Button>
        </Stack>
        <Divider />
        <Box>
          <Typography variant="overline" color="text.secondary">Icône</Typography>
          <Box sx={{ mt: 0.5 }}>{renderFooterLinkIcon(data.icon, 'medium')}</Box>
        </Box>
        <Box>
          <Typography variant="overline" color="text.secondary">URL</Typography>
          <Typography>
            <Link href={data.url} target="_blank" rel="noopener">
              {data.url}
            </Link>
          </Typography>
        </Box>
        <Box>
          <Typography variant="overline" color="text.secondary">Ordre d'affichage</Typography>
          <Typography>{data.displayOrder ?? 0}</Typography>
        </Box>
        <Box>
          <Typography variant="overline" color="text.secondary">Identifiant</Typography>
          <Typography color="text.secondary">{data.id}</Typography>
        </Box>
      </Stack>
    </Paper>
  );
};