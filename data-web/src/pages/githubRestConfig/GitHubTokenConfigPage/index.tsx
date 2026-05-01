import { useMemo, useState, type FC } from 'react';
import Alert from '@mui/material/Alert';
import Box from '@mui/material/Box';
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
import useMediaQuery from '@mui/material/useMediaQuery';
import { useTheme } from '@mui/material/styles';
import {
  useCreateGitHubRestConfigMutation,
  useLazyGetGitHubRestConfigByIdentifierQuery,
  useListRequiredGitHubTokenPermissionsMutation,
  type GitHubTokenPermission,
} from '../../../services/githubRestConfigApi';
import type { GitHubTokenConfigPageProps } from './GitHubTokenConfigPage.types';

const knownOperations = [
  'repository.read',
  'repository.write',
  'issues.read',
  'issues.write',
  'pull-requests.read',
  'pull-requests.write',
  'actions.read',
  'actions.write',
  'webhooks.read',
  'webhooks.write',
];

const parseOperations = (value: string): string[] => {
  const separators = /[\n,;]+/;

  return value
    .split(separators)
    .map((item) => item.trim())
    .filter(Boolean);
};

export const GitHubTokenConfigPage: FC<GitHubTokenConfigPageProps> = () => {
  const theme = useTheme();
  const isDesktop = useMediaQuery(theme.breakpoints.up('md'));

  const [identifier, setIdentifier] = useState('');
  const [token, setToken] = useState('');
  const [comment, setComment] = useState('');
  const [lookupIdentifier, setLookupIdentifier] = useState('');
  const [operationsInput, setOperationsInput] = useState('repository.read\nissues.write');

  const [createGitHubRestConfig, createState] = useCreateGitHubRestConfigMutation();
  const [loadConfig, lookupState] = useLazyGetGitHubRestConfigByIdentifierQuery();
  const [resolvePermissions, permissionState] = useListRequiredGitHubTokenPermissionsMutation();

  const requiredPermissions = permissionState.data?.requiredPermissions ?? [];
  const unknownOperations = permissionState.data?.unknownOperations ?? [];

  const helperText = useMemo(
    () => `Formats acceptes: sauts de ligne, virgules ou point-virgules. Exemple: ${knownOperations[0]}, ${knownOperations[1]}`,
    [],
  );

  const handleSave = async () => {
    const payloadIdentifier = identifier.trim();
    const payloadToken = token.trim();

    if (!payloadIdentifier || !payloadToken) {
      return;
    }

    const created = await createGitHubRestConfig({
      gitHubRestConfigCreationForm: {
        identifier: payloadIdentifier,
        token: payloadToken,
        comment: comment.trim() || undefined,
      },
    }).unwrap();

    setLookupIdentifier(created.identifier ?? payloadIdentifier);
    setToken('');
  };

  const handleLookup = async () => {
    const value = lookupIdentifier.trim();
    if (!value) {
      return;
    }

    await loadConfig({ identifier: value }).unwrap();
  };

  const handleResolvePermissions = async () => {
    const operations = parseOperations(operationsInput);
    if (operations.length === 0) {
      return;
    }

    await resolvePermissions({
      gitHubTokenPermissionRequest: { operations },
    }).unwrap();
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

  return (
    <Stack spacing={3} sx={{ p: { xs: 2, md: 3 }, maxWidth: 980, mx: 'auto' }}>
      <Box>
        <Typography variant="h4" component="h1">
          Configuration token GitHub REST
        </Typography>
        <Typography variant="body2" color="text.secondary">
          Enregistrez un token par identifiant et calculez les droits requis selon les operations appelees.
        </Typography>
      </Box>

      <Paper variant="outlined" sx={{ p: { xs: 2, md: 2.5 } }}>
        <Stack spacing={2}>
          <Typography variant="h6">Enregistrer une configuration</Typography>
          <TextField
            label="Identifiant"
            value={identifier}
            onChange={(event) => setIdentifier(event.target.value)}
            placeholder="integration-ci"
            fullWidth
          />
          <TextField
            label="Token"
            value={token}
            onChange={(event) => setToken(event.target.value)}
            type="password"
            placeholder="github_pat_xxx"
            fullWidth
          />
          <TextField
            label="Commentaire"
            value={comment}
            onChange={(event) => setComment(event.target.value)}
            placeholder="Usage fonctionnel du token"
            multiline
            minRows={2}
            fullWidth
          />
          <Stack direction={{ xs: 'column', sm: 'row' }} spacing={1.2}>
            <Button variant="contained" onClick={handleSave} disabled={createState.isLoading}>
              Enregistrer
            </Button>
          </Stack>
          {createState.isSuccess && (
            <Alert severity="success">
              Configuration enregistree. Token stocke avec apercu: {createState.data?.tokenPreview}
            </Alert>
          )}
          {createState.isError && (
            <Alert severity="error">
              Echec de l'enregistrement de la configuration token.
            </Alert>
          )}
        </Stack>
      </Paper>

      <Paper variant="outlined" sx={{ p: { xs: 2, md: 2.5 } }}>
        <Stack spacing={2}>
          <Typography variant="h6">Consulter une configuration</Typography>
          <Stack direction={{ xs: 'column', sm: 'row' }} spacing={1.2}>
            <TextField
              label="Identifiant"
              value={lookupIdentifier}
              onChange={(event) => setLookupIdentifier(event.target.value)}
              placeholder="integration-ci"
              fullWidth
            />
            <Button variant="outlined" onClick={handleLookup} disabled={lookupState.isFetching}>
              Rechercher
            </Button>
          </Stack>
          {lookupState.isError && (
            <Alert severity="error">Configuration introuvable pour cet identifiant.</Alert>
          )}
          {lookupState.data && (
            <Paper variant="outlined" sx={{ p: 1.5 }}>
              <Stack spacing={0.7}>
                <Typography variant="subtitle2">{lookupState.data.identifier}</Typography>
                <Typography variant="body2" color="text.secondary">
                  Token: {lookupState.data.tokenPreview}
                </Typography>
                <Typography variant="body2" color="text.secondary">
                  Commentaire: {lookupState.data.comment || 'Aucun'}
                </Typography>
              </Stack>
            </Paper>
          )}
        </Stack>
      </Paper>

      <Paper variant="outlined" sx={{ p: { xs: 2, md: 2.5 } }}>
        <Stack spacing={2}>
          <Typography variant="h6">Droits GitHub requis pour le token</Typography>
          <Typography variant="body2" color="text.secondary">
            Renseignez les operations REST appelees, puis obtenez les permissions minimales a appliquer au PAT fine-grained.
          </Typography>
          <TextField
            label="Operations"
            value={operationsInput}
            onChange={(event) => setOperationsInput(event.target.value)}
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
                onClick={() => {
                  const current = new Set(parseOperations(operationsInput));
                  if (current.has(operation)) {
                    current.delete(operation);
                  } else {
                    current.add(operation);
                  }
                  setOperationsInput(Array.from(current).join('\n'));
                }}
              />
            ))}
          </Stack>
          <Button variant="contained" onClick={handleResolvePermissions} disabled={permissionState.isLoading}>
            Calculer les droits
          </Button>

          {permissionState.isError && (
            <Alert severity="error">Impossible de calculer les droits avec les operations saisies.</Alert>
          )}

          {permissionState.data && (
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
    </Stack>
  );
};
