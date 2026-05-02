import Alert from '@mui/material/Alert';
import Box from '@mui/material/Box';
import Button from '@mui/material/Button';
import CircularProgress from '@mui/material/CircularProgress';
import Paper from '@mui/material/Paper';
import Stack from '@mui/material/Stack';
import Table from '@mui/material/Table';
import TableBody from '@mui/material/TableBody';
import TableCell from '@mui/material/TableCell';
import TableContainer from '@mui/material/TableContainer';
import TableRow from '@mui/material/TableRow';
import Typography from '@mui/material/Typography';
import useMediaQuery from '@mui/material/useMediaQuery';
import { useTheme } from '@mui/material/styles';
import { useMemo } from 'react';
import { useGetJavaVersionInfoQuery } from '../../../services/serverInfoApi';

export const JavaVersionPage = () => {
  const theme = useTheme();
  const isDesktop = useMediaQuery(theme.breakpoints.up('md'));

  const { data, isLoading, isFetching, isError, refetch } = useGetJavaVersionInfoQuery();

  const rows = useMemo(
    () => [
      { label: 'Version Java', value: data?.javaVersion ?? '-' },
      { label: 'Version runtime', value: data?.runtimeVersion ?? '-' },
      { label: 'Nom JVM', value: data?.vmName ?? '-' },
      { label: 'Fournisseur JVM', value: data?.vmVendor ?? '-' },
      { label: 'Systeme hote', value: data?.osName ?? '-' },
    ],
    [data],
  );

  return (
    <Stack spacing={3} sx={{ p: { xs: 2, md: 3 }, maxWidth: 900, mx: 'auto' }}>
      <Box>
        <Typography variant="h4" component="h1">
          Version Java du serveur
        </Typography>
        <Typography variant="body2" color="text.secondary">
          Cette page expose les informations runtime Java actuellement utilisees par le backend.
        </Typography>
      </Box>

      <Stack direction={{ xs: 'column', sm: 'row' }} spacing={1.2}>
        <Button variant="outlined" onClick={() => refetch()} disabled={isFetching}>
          Rafraichir
        </Button>
      </Stack>

      {isLoading && (
        <Paper variant="outlined" sx={{ p: 3 }}>
          <Stack direction="row" spacing={1.2} sx={{ alignItems: 'center' }}>
            <CircularProgress size={20} />
            <Typography>Chargement des informations serveur...</Typography>
          </Stack>
        </Paper>
      )}

      {isError && (
        <Alert severity="error">
          Impossible de recuperer la version Java du serveur. Verifiez que le backend est demarre.
        </Alert>
      )}

      {!isLoading && !isError && (
        isDesktop ? (
          <TableContainer component={Paper} variant="outlined">
            <Table size="small" aria-label="informations java serveur">
              <TableBody>
                {rows.map((row) => (
                  <TableRow key={row.label}>
                    <TableCell sx={{ width: '35%', fontWeight: 600 }}>{row.label}</TableCell>
                    <TableCell>{row.value}</TableCell>
                  </TableRow>
                ))}
              </TableBody>
            </Table>
          </TableContainer>
        ) : (
          <Stack spacing={1.2}>
            {rows.map((row) => (
              <Paper key={row.label} variant="outlined" sx={{ p: 1.5 }}>
                <Typography variant="subtitle2">{row.label}</Typography>
                <Typography variant="body2" color="text.secondary">
                  {row.value}
                </Typography>
              </Paper>
            ))}
          </Stack>
        )
      )}
    </Stack>
  );
};
