import type { FC } from 'react';
import Alert from '@mui/material/Alert';
import Button from '@mui/material/Button';
import Chip from '@mui/material/Chip';
import Divider from '@mui/material/Divider';
import Paper from '@mui/material/Paper';
import Stack from '@mui/material/Stack';
import Table from '@mui/material/Table';
import TableBody from '@mui/material/TableBody';
import TableCell from '@mui/material/TableCell';
import TableContainer from '@mui/material/TableContainer';
import TableHead from '@mui/material/TableHead';
import TableRow from '@mui/material/TableRow';
import TextField from '@mui/material/TextField';
import Typography from '@mui/material/Typography';
import type { GitHubTokenPermission } from '../../../services/githubRestConfigApi';

type GitHubTokenPermissionsSectionProps = {
  isDesktop: boolean;
  knownOperations: string[];
  operationsInput: string;
  onOperationsInputChange: (value: string) => void;
  onToggleOperation: (operation: string) => void;
  onResolvePermissions: () => Promise<void>;
  helperText: string;
  isResolving: boolean;
  resolveError: boolean;
  hasPermissionData: boolean;
  requiredPermissions: GitHubTokenPermission[];
  unknownOperations: string[];
};

const renderPermissionListMobile = (items: GitHubTokenPermission[]) => (
  <Stack spacing={1.2}>
    {items.map((permission, index) => (
      <Paper key={`${permission.permission}-${permission.access}-${index}`} variant="outlined" sx={{ p: 1.5 }}>
        <Stack spacing={0.7}>
          <Typography variant="subtitle2">{permission.permission}</Typography>
          <Chip
            size="small"
            label={(permission.access ?? 'read').toUpperCase()}
            color={permission.access === 'write' ? 'warning' : 'default'}
            sx={{ width: 'fit-content' }}
          />
          <Typography variant="body2" color="text.secondary">
            {permission.reason}
          </Typography>
        </Stack>
      </Paper>
    ))}
  </Stack>
);

export const GitHubTokenPermissionsSection: FC<GitHubTokenPermissionsSectionProps> = ({
  isDesktop,
  knownOperations,
  operationsInput,
  onOperationsInputChange,
  onToggleOperation,
  onResolvePermissions,
  helperText,
  isResolving,
  resolveError,
  hasPermissionData,
  requiredPermissions,
  unknownOperations,
}) => (
  <Paper variant="outlined" sx={{ p: { xs: 2, md: 2.5 } }}>
    <Stack spacing={2}>
      <Typography variant="h6">Droits GitHub requis pour le token</Typography>
      <Typography variant="body2" color="text.secondary">
        Renseignez les operations REST appelees, puis obtenez les permissions minimales a appliquer au PAT fine-grained.
      </Typography>
      <TextField
        label="Operations"
        value={operationsInput}
        onChange={(event) => onOperationsInputChange(event.target.value)}
        multiline
        minRows={4}
        helperText={helperText}
        fullWidth
      />
      <Stack direction="row" spacing={1.2} sx={{ flexWrap: 'wrap' }}>
        {knownOperations.map((operation) => (
          <Chip
            key={operation}
            label={operation}
            size="small"
            onClick={() => onToggleOperation(operation)}
          />
        ))}
      </Stack>
      <Button variant="contained" onClick={onResolvePermissions} disabled={isResolving}>
        Calculer les droits
      </Button>

      {resolveError && (
        <Alert severity="error">Impossible de calculer les droits avec les operations saisies.</Alert>
      )}

      {hasPermissionData && (
        <Stack spacing={1.5}>
          <Divider />
          <Typography variant="subtitle1">Permissions a configurer</Typography>

          {requiredPermissions.length === 0 && (
            <Alert severity="info">Aucune permission n'a ete calculee.</Alert>
          )}

          {requiredPermissions.length > 0 && (
            isDesktop ? (
              <TableContainer component={Paper} variant="outlined">
                <Table size="small" aria-label="permissions github requises">
                  <TableHead>
                    <TableRow>
                      <TableCell>Permission</TableCell>
                      <TableCell>Acces</TableCell>
                      <TableCell>Raison</TableCell>
                    </TableRow>
                  </TableHead>
                  <TableBody>
                    {requiredPermissions.map((permission, index) => (
                      <TableRow key={`${permission.permission}-${permission.access}-${index}`}>
                        <TableCell>{permission.permission}</TableCell>
                        <TableCell>
                          <Chip
                            size="small"
                            label={(permission.access ?? 'read').toUpperCase()}
                            color={permission.access === 'write' ? 'warning' : 'default'}
                          />
                        </TableCell>
                        <TableCell>{permission.reason}</TableCell>
                      </TableRow>
                    ))}
                  </TableBody>
                </Table>
              </TableContainer>
            ) : renderPermissionListMobile(requiredPermissions)
          )}

          {unknownOperations.length > 0 && (
            <Alert severity="warning">
              Operations inconnues: {unknownOperations.join(', ')}
            </Alert>
          )}
        </Stack>
      )}
    </Stack>
  </Paper>
);
