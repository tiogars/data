import type { ChangeEvent, ReactNode } from 'react';
import { Link as RouterLink } from 'react-router-dom';
import Button from '@mui/material/Button';
import Card from '@mui/material/Card';
import CardActions from '@mui/material/CardActions';
import CardContent from '@mui/material/CardContent';
import IconButton from '@mui/material/IconButton';
import Paper from '@mui/material/Paper';
import Stack from '@mui/material/Stack';
import Table from '@mui/material/Table';
import TableBody from '@mui/material/TableBody';
import TableCell from '@mui/material/TableCell';
import TableContainer from '@mui/material/TableContainer';
import TableHead from '@mui/material/TableHead';
import TablePagination from '@mui/material/TablePagination';
import TableRow from '@mui/material/TableRow';
import Typography from '@mui/material/Typography';
import useMediaQuery from '@mui/material/useMediaQuery';
import { useTheme } from '@mui/material/styles';
import DeleteIcon from '@mui/icons-material/Delete';
import EditOutlinedIcon from '@mui/icons-material/EditOutlined';
import VisibilityOutlinedIcon from '@mui/icons-material/VisibilityOutlined';

export type CrudListColumn<TItem> = {
  key: string;
  header: ReactNode;
  render: (item: TItem) => ReactNode;
};

export type CrudListPagination = {
  count: number;
  page: number;
  pageSize: number;
  onPageChange: (event: unknown, nextPage: number) => void;
  onPageSizeChange: (event: ChangeEvent<HTMLInputElement | HTMLTextAreaElement>) => void;
  rowsPerPageOptions?: number[];
};

export type ResponsiveCrudListProps<TItem> = {
  items: TItem[];
  getRowKey: (item: TItem) => string;
  columns: CrudListColumn<TItem>[];
  getDetailPath: (item: TItem) => string;
  getEditPath: (item: TItem) => string;
  onDelete: (item: TItem) => void;
  actionLabels: { view: string; edit: string; remove: string };
  renderCardTitle: (item: TItem) => ReactNode;
  renderCardContent?: (item: TItem) => ReactNode;
  pagination: CrudListPagination;
};

/**
 * Rendu structurel commun d'une liste CRUD : table sur desktop, cartes sur mobile.
 */
export const ResponsiveCrudList = <TItem,>({
  items,
  getRowKey,
  columns,
  getDetailPath,
  getEditPath,
  onDelete,
  actionLabels,
  renderCardTitle,
  renderCardContent,
  pagination,
}: ResponsiveCrudListProps<TItem>) => {
  const theme = useTheme();
  const isDesktop = useMediaQuery(theme.breakpoints.up('md'));

  const paginationNode = (
    <TablePagination
      component="div"
      count={pagination.count}
      page={pagination.page}
      onPageChange={pagination.onPageChange}
      rowsPerPage={pagination.pageSize}
      onRowsPerPageChange={pagination.onPageSizeChange}
      rowsPerPageOptions={pagination.rowsPerPageOptions ?? [10, 20, 50]}
    />
  );

  if (isDesktop) {
    return (
      <TableContainer component={Paper} variant="outlined">
        <Table>
          <TableHead>
            <TableRow>
              {columns.map((column) => (
                <TableCell key={column.key}>{column.header}</TableCell>
              ))}
              <TableCell align="right">Actions</TableCell>
            </TableRow>
          </TableHead>
          <TableBody>
            {items.map((item) => (
              <TableRow key={getRowKey(item)} hover>
                {columns.map((column) => (
                  <TableCell key={column.key}>{column.render(item)}</TableCell>
                ))}
                <TableCell align="right">
                  <IconButton component={RouterLink} to={getDetailPath(item)} aria-label={actionLabels.view}>
                    <VisibilityOutlinedIcon fontSize="small" />
                  </IconButton>
                  <IconButton component={RouterLink} to={getEditPath(item)} aria-label={actionLabels.edit}>
                    <EditOutlinedIcon fontSize="small" />
                  </IconButton>
                  <IconButton aria-label={actionLabels.remove} color="error" onClick={() => onDelete(item)}>
                    <DeleteIcon fontSize="small" />
                  </IconButton>
                </TableCell>
              </TableRow>
            ))}
          </TableBody>
        </Table>
        {paginationNode}
      </TableContainer>
    );
  }

  return (
    <>
      <Stack spacing={2}>
        {items.map((item) => (
          <Card key={getRowKey(item)} variant="outlined">
            <CardContent>
              <Stack spacing={1}>
                <Typography variant="h6">{renderCardTitle(item)}</Typography>
                {renderCardContent?.(item)}
              </Stack>
            </CardContent>
            <CardActions sx={{ px: 2, pb: 2, pt: 0, gap: 1, flexWrap: 'wrap' }}>
              <Button component={RouterLink} to={getDetailPath(item)} size="small" variant="outlined">
                Voir
              </Button>
              <Button component={RouterLink} to={getEditPath(item)} size="small" variant="outlined">
                Modifier
              </Button>
              <Button size="small" color="error" variant="outlined" onClick={() => onDelete(item)}>
                Supprimer
              </Button>
            </CardActions>
          </Card>
        ))}
      </Stack>
      {paginationNode}
    </>
  );
};
