import { useMemo, useState, type FC } from 'react';
import { Link as RouterLink } from 'react-router-dom';
import Alert from '@mui/material/Alert';
import Box from '@mui/material/Box';
import Button from '@mui/material/Button';
import Chip from '@mui/material/Chip';
import Dialog from '@mui/material/Dialog';
import DialogActions from '@mui/material/DialogActions';
import DialogContent from '@mui/material/DialogContent';
import DialogContentText from '@mui/material/DialogContentText';
import DialogTitle from '@mui/material/DialogTitle';
import Link from '@mui/material/Link';
import Stack from '@mui/material/Stack';
import TextField from '@mui/material/TextField';
import Typography from '@mui/material/Typography';
import { ResponsiveCrudList, type CrudListColumn } from '../../../components/ResponsiveCrudList';
import { usePaginatedSearch } from '../../../hooks/usePaginatedSearch';
import { renderFooterLinkIcon } from '../../../features/footerLink/iconRegistry';
import { type FooterLink, useDeleteFooterLinkByIdMutation, useSearchFooterLinksQuery } from '../../../services/footerLinkApi';
import type { FooterLinkListPageProps } from './FooterLinkListPage.types';

type FooterLinkRow = FooterLink & { id: string };

function toFooterLinkRows(items: FooterLink[] | undefined): FooterLinkRow[] {
  return (items ?? []).filter((item): item is FooterLinkRow => Boolean(item.id));
}

const footerLinkColumns: CrudListColumn<FooterLinkRow>[] = [
  {
    key: 'icon',
    header: 'Icône',
    render: (footerLink) => <Box sx={{ width: 80 }}>{renderFooterLinkIcon(footerLink.icon)}</Box>,
  },
  {
    key: 'label',
    header: 'Libellé',
    render: (footerLink) => footerLink.label || 'Sans libellé',
  },
  {
    key: 'url',
    header: 'URL',
    render: (footerLink) => (
      <Link href={footerLink.url} target="_blank" rel="noopener">
        {footerLink.url}
      </Link>
    ),
  },
  {
    key: 'displayOrder',
    header: 'Ordre',
    render: (footerLink) => footerLink.displayOrder ?? 0,
  },
];

export const FooterLinkListPage: FC<FooterLinkListPageProps> = () => {
  const {
    searchInput,
    setSearchInput,
    page,
    pageSize,
    queryArgs,
    handlePageChange,
    handlePageSizeChange,
  } = usePaginatedSearch();
  const { data, isLoading, error, refetch } = useSearchFooterLinksQuery(queryArgs, { refetchOnMountOrArgChange: true });
  const [deleteFooterLinkById, { isLoading: isDeleting }] = useDeleteFooterLinkByIdMutation();
  const [footerLinkToDelete, setFooterLinkToDelete] = useState<FooterLinkRow | null>(null);

  const footerLinks = useMemo(() => toFooterLinkRows(data?.items), [data?.items]);

  const handleDelete = async () => {
    if (!footerLinkToDelete) return;

    await deleteFooterLinkById({ id: footerLinkToDelete.id }).unwrap();
    setFooterLinkToDelete(null);
    await refetch();
  };

  if (isLoading) return <div>Chargement...</div>;
  if (error) return <div>Erreur lors du chargement des liens footer</div>;

  return (
    <Stack spacing={3} sx={{ p: { xs: 2, md: 3 } }}>
      <Stack direction={{ xs: 'column', sm: 'row' }} spacing={1.5} useFlexGap sx={{ alignItems: { sm: 'center' }, justifyContent: 'space-between' }}>
        <Box>
          <Typography variant="h4" component="h1">
            Liens footer
          </Typography>
          <Typography variant="body2" color="text.secondary">
            Ces données alimentent directement le footer de l'application et sont initialisées côté serveur par défaut.
          </Typography>
        </Box>
        <Stack direction="row" spacing={1}>
          <Chip label={`${data?.count ?? 0} lien${(data?.count ?? 0) > 1 ? 's' : ''}`} color="primary" variant="outlined" />
          <Button component={RouterLink} to="/footer-link/create" variant="contained">
            Nouveau lien
          </Button>
        </Stack>
      </Stack>

      <TextField
        fullWidth
        label="Recherche"
        placeholder="Rechercher par libelle, URL ou icone"
        value={searchInput}
        onChange={(event) => setSearchInput(event.target.value)}
      />

      {footerLinks.length === 0 && (
        <Alert severity="info">
          Aucun lien n'est actuellement configuré pour le footer.
        </Alert>
      )}

      <ResponsiveCrudList
        items={footerLinks}
        getRowKey={(footerLink) => footerLink.id}
        columns={footerLinkColumns}
        getDetailPath={(footerLink) => `/footer-link/${footerLink.id}`}
        getEditPath={(footerLink) => `/footer-link/${footerLink.id}/edit`}
        onDelete={setFooterLinkToDelete}
        actionLabels={{
          view: 'Voir le lien',
          edit: 'Modifier le lien',
          remove: 'Supprimer le lien',
        }}
        renderCardTitle={(footerLink) => (
          <Box component="span" sx={{ display: 'inline-flex', alignItems: 'center', gap: 1.5 }}>
            {renderFooterLinkIcon(footerLink.icon, 'medium')}
            {footerLink.label || 'Sans libellé'}
          </Box>
        )}
        renderCardContent={(footerLink) => (
          <>
            <Typography variant="body2" color="text.secondary">
              Ordre {footerLink.displayOrder ?? 0}
            </Typography>
            <Link href={footerLink.url} target="_blank" rel="noopener">
              {footerLink.url}
            </Link>
          </>
        )}
        pagination={{
          count: data?.count ?? 0,
          page,
          pageSize,
          onPageChange: handlePageChange,
          onPageSizeChange: handlePageSizeChange,
        }}
      />

      <Dialog open={Boolean(footerLinkToDelete)} onClose={() => setFooterLinkToDelete(null)}>
        <DialogTitle>Supprimer le lien</DialogTitle>
        <DialogContent>
          <DialogContentText>
            Voulez-vous vraiment supprimer {footerLinkToDelete?.label || 'ce lien'} du footer ?
          </DialogContentText>
        </DialogContent>
        <DialogActions>
          <Button onClick={() => setFooterLinkToDelete(null)} disabled={isDeleting}>Annuler</Button>
          <Button color="error" onClick={handleDelete} disabled={isDeleting}>Supprimer</Button>
        </DialogActions>
      </Dialog>
    </Stack>
  );
};