import type { FC } from 'react';
import { useState } from 'react';
import Alert from '@mui/material/Alert';
import Box from '@mui/material/Box';
import Button from '@mui/material/Button';
import CircularProgress from '@mui/material/CircularProgress';
import Paper from '@mui/material/Paper';
import Stack from '@mui/material/Stack';
import Tab from '@mui/material/Tab';
import TextField from '@mui/material/TextField';
import Tabs from '@mui/material/Tabs';
import Typography from '@mui/material/Typography';
import { useForm } from 'react-hook-form';
import { useNavigate } from 'react-router-dom';
import WingetForm, { type WingetFormValues } from '../../../components/WingetForm';
import { parseTagsText } from '../../../components/WingetForm.helpers';
import { CrudFormPageShell } from '../../../components/CrudFormPageShell';
import { useCreateWingetMutation } from '../../../services/wingetApi';
import { useImportWingetsMutation } from '../../../services/wingetImportApi';
import type { WingetCreatePageProps } from './WingetCreatePage.types';

const defaultValues: WingetFormValues = {
  name: '',
  description: '',
  wingetId: '',
  installCommand: '',
  tagsText: '',
};

export const WingetCreatePage: FC<WingetCreatePageProps> = ({ onCreated }) => {
  const navigate = useNavigate();
  const [activeTab, setActiveTab] = useState(0);
  const [bulkWingetIdsText, setBulkWingetIdsText] = useState('');
  const [createWinget, { isLoading, error, isSuccess }] = useCreateWingetMutation();
  const [importWingets, { isLoading: isImporting, data: importResult, error: importError }] = useImportWingetsMutation();
  const methods = useForm<WingetFormValues>({ defaultValues });
  const { reset } = methods;

  const handleBulkImport = async () => {
    await importWingets({ wingetImportForm: { wingetIdsText: bulkWingetIdsText } }).unwrap();
    setBulkWingetIdsText('');
  };

  const onSubmit = async (values: WingetFormValues) => {
    const created = await createWinget({
      wingetCreationForm: {
        name: values.name,
        description: values.description,
        wingetId: values.wingetId,
        installCommand: values.installCommand,
        tags: parseTagsText(values.tagsText),
      },
    }).unwrap();

    reset(defaultValues);

    if (created.id) {
      await onCreated?.(created.id);
      navigate(`/winget/${created.id}`);
    }
  };

  return (
    <Stack spacing={2.5}>
      <Box sx={{ maxWidth: 640, mx: 'auto', width: '100%', mt: 3 }}>
        <Paper sx={{ p: { xs: 1.5, md: 2 } }}>
          <Tabs
            value={activeTab}
            onChange={(_, newValue: number) => setActiveTab(newValue)}
            variant="fullWidth"
            aria-label="Onglets creation et import Winget"
          >
            <Tab label="Creation" />
            <Tab label="Import en masse" />
          </Tabs>
        </Paper>
      </Box>

      {activeTab === 0 ? (
        <CrudFormPageShell
          methods={methods}
          title="Creer une application Winget"
          subtitle="Ajoutez le nom, l'identifiant Winget, la commande d'installation et les tags."
          submitLabel="Creer"
          onSubmit={onSubmit}
          isSubmitting={isLoading}
          showSuccess={isSuccess}
          successMessage="Application Winget creee avec succes."
          showError={Boolean(error)}
          errorMessage="Erreur lors de la creation de l'application Winget."
        >
          <WingetForm disabled={isLoading} />
        </CrudFormPageShell>
      ) : null}

      {activeTab === 1 ? (
        <Box sx={{ maxWidth: 640, mx: 'auto', width: '100%' }}>
          <Paper sx={{ p: { xs: 2.5, md: 3 } }}>
            <Stack spacing={2}>
              <Box>
                <Typography variant="h5" component="h2">
                  Import Winget en masse
                </Typography>
                <Typography variant="body2" color="text.secondary">
                  Un wingetId par ligne. Le serveur derive automatiquement le nom avec le texte apres le premier point.
                </Typography>
              </Box>
              <TextField
                label="Winget IDs"
                fullWidth
                multiline
                minRows={6}
                value={bulkWingetIdsText}
                onChange={(event) => setBulkWingetIdsText(event.target.value)}
                disabled={isImporting}
                helperText="Exemple: Microsoft.VisualStudioCode"
              />
              <Button variant="contained" onClick={() => void handleBulkImport()} disabled={isImporting || bulkWingetIdsText.trim().length === 0}>
                {isImporting ? 'Import en cours...' : 'Importer'}
              </Button>
              {isImporting ? <CircularProgress size={24} /> : null}
              {importResult ? (
                <Alert severity="success">
                  Import termine: {importResult.createdCount ?? 0} crees, {importResult.skippedCount ?? 0} ignores.
                </Alert>
              ) : null}
              {importResult && (importResult.skippedWingetIds?.length ?? 0) > 0 ? (
                <Alert severity="warning">
                  IDs ignores: {importResult.skippedWingetIds?.join(', ')}
                </Alert>
              ) : null}
              {importError ? <Alert severity="error">Erreur lors de l'import Winget en masse.</Alert> : null}
            </Stack>
          </Paper>
        </Box>
      ) : null}
    </Stack>
  );
};
