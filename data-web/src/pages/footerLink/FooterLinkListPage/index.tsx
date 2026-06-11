import { useEffect, useMemo, useState, type FC } from 'react';
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
import Link from '@mui/material/Link';
import Paper from '@mui/material/Paper';
import Stack from '@mui/material/Stack';
import Table from '@mui/material/Table';
import TableBody from '@mui/material/TableBody';
import TableCell from '@mui/material/TableCell';
import TableContainer from '@mui/material/TableContainer';
import TableHead from '@mui/material/TableHead';
import TablePagination from '@mui/material/TablePagination';
import TableRow from '@mui/material/TableRow';
import TextField from '@mui/material/TextField';
import Typography from '@mui/material/Typography';
import useMediaQuery from '@mui/material/useMediaQuery';
import { useTheme } from '@mui/material/styles';
import DeleteIcon from '@mui/icons-material/Delete';
import EditOutlinedIcon from '@mui/icons-material/EditOutlined';
import VisibilityOutlinedIcon from '@mui/icons-material/VisibilityOutlined';
import { renderFooterLinkIcon } from '../../../features/footerLink/iconRegistry';
import { type FooterLink, useDeleteFooterLinkByIdMutation, useSearchFooterLinksQuery } from '../../../services/footerLinkApi';
import type { FooterLinkListPageProps } from './FooterLinkListPage.types';

type FooterLinkRow = FooterLink & { id: string };

function toFooterLinkRows(items: FooterLink[] | undefined): FooterLinkRow[] {
  return (items ?? []).filter((item): item is FooterLinkRow => Boolean(item.id));
}

export const FooterLinkListPage: FC<FooterLinkListPageProps> = () => {
  const theme = useTheme();
  const isDesktop = useMediaQuery(theme.breakpoints.up('md'));
  const [searchInput, setSearchInput] = useState('');
  const [searchQuery, setSearchQuery] = useState('');
  const [page, setPage] = useState(0);
  const [pageSize, setPageSize] = useState(10);
  const queryArgs = useMemo(() => ({
    page,
    size: pageSize,
    q: searchQuery || undefined,
  }), [page, pageSize, searchQuery]);
  const { data, isLoading, error, refetch } = useSearchFooterLinksQuery(queryArgs, { refetchOnMountOrArgChange: true });
  const [deleteFooterLinkById, { isLoading: isDeleting }] = useDeleteFooterLinkByIdMutation();
  const [footerLinkToDelete, setFooterLinkToDelete] = useState<FooterLinkRow | null>(null);

  const footerLinks = useMemo(() => toFooterLinkRows(data?.items), [data?.items]);

  useEffect(() => {
    const timeout = setTimeout(() => {
      setSearchQuery(searchInput.trim());
      setPage(0);
    }, 300);
    return () => clearTimeout(timeout);
  }, [searchInput]);

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

      {isDesktop ? (
        <TableContainer component={Paper} variant="outlined">
          <Table>
            <TableHead>
              <TableRow>
                <TableCell>Icône</TableCell>
                <TableCell>Libellé</TableCell>
                <TableCell>URL</TableCell>
                <TableCell>Ordre</TableCell>
                <TableCell align="right">Actions</TableCell>
              </TableRow>
            </TableHead>
            <TableBody>
              {footerLinks.map((footerLink) => (
                <TableRow key={footerLink.id} hover>
                  <TableCell sx={{ width: 80 }}>{renderFooterLinkIcon(footerLink.icon)}</TableCell>
                  <TableCell>{footerLink.label || 'Sans libellé'}</TableCell>
                  <TableCell>
                    <Link href={footerLink.url} target="_blank" rel="noopener">
                      {footerLink.url}
                    </Link>
                  </TableCell>
                  <TableCell>{footerLink.displayOrder ?? 0}</TableCell>
                  <TableCell align="right">
                    <IconButton component={RouterLink} to={`/footer-link/${footerLink.id}`} aria-label="Voir le lien">
                      <VisibilityOutlinedIcon fontSize="small" />
                    </IconButton>
                    <IconButton component={RouterLink} to={`/footer-link/${footerLink.id}/edit`} aria-label="Modifier le lien">
                      <EditOutlinedIcon fontSize="small" />
                    </IconButton>
                    <IconButton aria-label="Supprimer le lien" color="error" onClick={() => setFooterLinkToDelete(footerLink)}>
                      <DeleteIcon fontSize="small" />
                    </IconButton>
                  </TableCell>
                </TableRow>
              ))}
            </TableBody>
          </Table>
          <TablePagination
            component="div"
            count={data?.count ?? 0}
            page={page}
            onPageChange={(_event, nextPage) => setPage(nextPage)}
            rowsPerPage={pageSize}
            onRowsPerPageChange={(event) => {
              const nextSize = Number(event.target.value);
              setPageSize(nextSize);
              setPage(0);
            }}
            rowsPerPageOptions={[10, 20, 50]}
          />
        </TableContainer>
      ) : (
        <Stack spacing={2}>
          {footerLinks.map((footerLink) => (
            <Card key={footerLink.id} variant="outlined">
              <CardContent>
                <Stack spacing={1.5}>
                  <Stack direction="row" spacing={1.5} sx={{ alignItems: 'center' }}>
                    {renderFooterLinkIcon(footerLink.icon, 'medium')}
                    <Box>
                      <Typography variant="h6">{footerLink.label || 'Sans libellé'}</Typography>
                      <Typography variant="body2" color="text.secondary">
                        Ordre {footerLink.displayOrder ?? 0}
                      </Typography>
                    </Box>
                  </Stack>
                  <Link href={footerLink.url} target="_blank" rel="noopener">
                    {footerLink.url}
                  </Link>
                </Stack>
              </CardContent>
              <CardActions sx={{ px: 2, pb: 2, pt: 0, gap: 1, flexWrap: 'wrap' }}>
                <Button component={RouterLink} to={`/footer-link/${footerLink.id}`} size="small" variant="outlined">
                  Voir
                </Button>
                <Button component={RouterLink} to={`/footer-link/${footerLink.id}/edit`} size="small" variant="outlined">
                  Modifier
                </Button>
                <Button size="small" color="error" variant="outlined" onClick={() => setFooterLinkToDelete(footerLink)}>
                  Supprimer
                </Button>
              </CardActions>
            </Card>
          ))}
        </Stack>
      )}

      {!isDesktop && (
        <TablePagination
          component="div"
          count={data?.count ?? 0}
          page={page}
          onPageChange={(_event, nextPage) => setPage(nextPage)}
          rowsPerPage={pageSize}
          onRowsPerPageChange={(event) => {
            const nextSize = Number(event.target.value);
            setPageSize(nextSize);
            setPage(0);
          }}
          rowsPerPageOptions={[10, 20, 50]}
        />
      )}

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