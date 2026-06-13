import { useEffect, useMemo, useState, type FC } from 'react';
import Alert from '@mui/material/Alert';
import Box from '@mui/material/Box';
import Button from '@mui/material/Button';
import Card from '@mui/material/Card';
import CardActions from '@mui/material/CardActions';
import CardContent from '@mui/material/CardContent';
import Chip from '@mui/material/Chip';
import CircularProgress from '@mui/material/CircularProgress';
import Paper from '@mui/material/Paper';
import Stack from '@mui/material/Stack';
import Table from '@mui/material/Table';
import TableBody from '@mui/material/TableBody';
import TableCell from '@mui/material/TableCell';
import TableContainer from '@mui/material/TableContainer';
import TableHead from '@mui/material/TableHead';
import TableRow from '@mui/material/TableRow';
import Typography from '@mui/material/Typography';
import useMediaQuery from '@mui/material/useMediaQuery';
import { useTheme } from '@mui/material/styles';
import DownloadIcon from '@mui/icons-material/Download';
import RefreshIcon from '@mui/icons-material/Refresh';

type AndroidRelease = {
  versionName: string;
  buildNumber: number;
  fileName: string;
  downloadUrl: string;
  sha256?: string;
  sizeBytes?: number;
  createdAt?: string;
};

type AndroidReleaseManifest = {
  generatedAt: string | null;
  latest: AndroidRelease | null;
  releases: AndroidRelease[];
};

const DEFAULT_MANIFEST: AndroidReleaseManifest = {
  generatedAt: null,
  latest: null,
  releases: [],
};

const dateFormatter = new Intl.DateTimeFormat('fr-FR', {
  dateStyle: 'medium',
  timeStyle: 'short',
});

function isAndroidRelease(value: unknown): value is AndroidRelease {
  if (!value || typeof value !== 'object') {
    return false;
  }

  const candidate = value as Partial<AndroidRelease>;
  return typeof candidate.versionName === 'string'
    && typeof candidate.buildNumber === 'number'
    && typeof candidate.fileName === 'string'
    && typeof candidate.downloadUrl === 'string';
}

function withBaseUrl(path: string): string {
  const baseUrl = import.meta.env.BASE_URL || '/';
  const normalizedBase = baseUrl.endsWith('/') ? baseUrl : `${baseUrl}/`;
  const normalizedPath = path.startsWith('/') ? path.slice(1) : path;
  return `${normalizedBase}${normalizedPath}`;
}

function formatDate(value?: string | null): string {
  if (!value) {
    return '-';
  }

  const parsed = new Date(value);
  if (Number.isNaN(parsed.getTime())) {
    return '-';
  }

  return dateFormatter.format(parsed);
}

function formatSize(value?: number): string {
  if (!value || value < 0) {
    return '-';
  }

  const kb = value / 1024;
  if (kb < 1024) {
    return `${kb.toFixed(1)} Ko`;
  }

  return `${(kb / 1024).toFixed(2)} Mo`;
}

function sanitizeManifest(raw: unknown): AndroidReleaseManifest {
  if (!raw || typeof raw !== 'object') {
    return DEFAULT_MANIFEST;
  }

  const candidate = raw as Partial<AndroidReleaseManifest>;
  const releases = Array.isArray(candidate.releases)
    ? candidate.releases.filter((item): item is AndroidRelease => isAndroidRelease(item))
    : [];

  return {
    generatedAt: typeof candidate.generatedAt === 'string' ? candidate.generatedAt : null,
    latest: isAndroidRelease(candidate.latest) ? candidate.latest : null,
    releases,
  };
}

export const AndroidReleaseListPage: FC = () => {
  const theme = useTheme();
  const isDesktop = useMediaQuery(theme.breakpoints.up('md'));
  const [manifest, setManifest] = useState<AndroidReleaseManifest>(DEFAULT_MANIFEST);
  const [isLoading, setIsLoading] = useState(true);
  const [errorMessage, setErrorMessage] = useState<string | null>(null);

  const loadManifest = async () => {
    try {
      setErrorMessage(null);
      setIsLoading(true);
      const response = await fetch(withBaseUrl('downloads/android/manifest.json'), {
        cache: 'no-store',
      });

      if (!response.ok) {
        throw new Error(`HTTP ${response.status}`);
      }

      const payload = await response.json() as unknown;
      const normalized = sanitizeManifest(payload);
      const sortedReleases = [...normalized.releases].sort((a, b) => b.buildNumber - a.buildNumber);

      setManifest({
        ...normalized,
        releases: sortedReleases,
      });
    } catch {
      setErrorMessage("Impossible de charger la liste des versions Android. Verifiez que le manifest est present dans data-web/public/downloads/android/manifest.json.");
    } finally {
      setIsLoading(false);
    }
  };

  useEffect(() => {
    void loadManifest();
  }, []);

  const totalReleases = manifest.releases.length;
  const latestBuildLabel = useMemo(() => {
    if (!manifest.latest) {
      return 'Aucune release';
    }

    return `${manifest.latest.versionName}+${manifest.latest.buildNumber}`;
  }, [manifest.latest]);

  return (
    <Stack spacing={3} sx={{ p: { xs: 2, md: 3 } }}>
      <Stack direction={{ xs: 'column', sm: 'row' }} spacing={1.5} useFlexGap sx={{ alignItems: { sm: 'center' }, justifyContent: 'space-between' }}>
        <Box>
          <Typography variant="h4" component="h1">
            Releases Android
          </Typography>
          <Typography variant="body2" color="text.secondary">
            Telechargez les APK installables produits par le conteneur de build Android.
          </Typography>
        </Box>
        <Stack direction="row" spacing={1} useFlexGap sx={{ flexWrap: 'wrap' }}>
          <Chip label={`${totalReleases} version${totalReleases > 1 ? 's' : ''}`} color="primary" variant="outlined" />
          <Chip label={`Derniere: ${latestBuildLabel}`} variant="outlined" />
          <Button variant="outlined" startIcon={<RefreshIcon />} onClick={() => void loadManifest()} disabled={isLoading}>
            Rafraichir
          </Button>
        </Stack>
      </Stack>

      <Alert severity="info">
        Le conteneur de build depose les APK dans data-web/public/downloads/android puis met a jour automatiquement ce catalogue.
      </Alert>

      {isLoading && (
        <Paper variant="outlined" sx={{ p: 3 }}>
          <Stack direction="row" spacing={1.2} sx={{ alignItems: 'center' }}>
            <CircularProgress size={20} />
            <Typography>Chargement des releases Android...</Typography>
          </Stack>
        </Paper>
      )}

      {errorMessage && (
        <Alert severity="error">{errorMessage}</Alert>
      )}

      {!isLoading && !errorMessage && manifest.releases.length === 0 && (
        <Alert severity="warning">
          Aucune version disponible. Lancez d'abord le build Android Docker pour generer un APK.
        </Alert>
      )}

      {!isLoading && !errorMessage && manifest.releases.length > 0 && (isDesktop ? (
        <TableContainer component={Paper} variant="outlined">
          <Table size="small">
            <TableHead>
              <TableRow>
                <TableCell>Version</TableCell>
                <TableCell>Fichier</TableCell>
                <TableCell>Taille</TableCell>
                <TableCell>Date</TableCell>
                <TableCell align="right">Action</TableCell>
              </TableRow>
            </TableHead>
            <TableBody>
              {manifest.releases.map((release) => (
                <TableRow key={release.fileName} hover>
                  <TableCell>
                    <Typography sx={{ fontWeight: 600 }}>
                      {release.versionName}+{release.buildNumber}
                    </Typography>
                    {manifest.latest?.fileName === release.fileName && (
                      <Chip size="small" label="Latest" color="success" variant="outlined" sx={{ mt: 0.5 }} />
                    )}
                  </TableCell>
                  <TableCell>{release.fileName}</TableCell>
                  <TableCell>{formatSize(release.sizeBytes)}</TableCell>
                  <TableCell>{formatDate(release.createdAt)}</TableCell>
                  <TableCell align="right">
                    <Button
                      size="small"
                      variant="contained"
                      href={withBaseUrl(release.downloadUrl)}
                      startIcon={<DownloadIcon />}
                    >
                      Telecharger
                    </Button>
                  </TableCell>
                </TableRow>
              ))}
            </TableBody>
          </Table>
        </TableContainer>
      ) : (
        <Stack spacing={2}>
          {manifest.releases.map((release) => (
            <Card key={release.fileName} variant="outlined">
              <CardContent>
                <Stack spacing={1.1}>
                  <Typography variant="h6">
                    {release.versionName}+{release.buildNumber}
                  </Typography>
                  <Typography variant="body2" color="text.secondary">
                    {release.fileName}
                  </Typography>
                  <Stack direction="row" spacing={1}>
                    <Chip size="small" label={`Taille: ${formatSize(release.sizeBytes)}`} variant="outlined" />
                    <Chip size="small" label={`Date: ${formatDate(release.createdAt)}`} variant="outlined" />
                  </Stack>
                </Stack>
              </CardContent>
              <CardActions sx={{ px: 2, pb: 2, pt: 0 }}>
                <Button
                  size="small"
                  variant="contained"
                  href={withBaseUrl(release.downloadUrl)}
                  startIcon={<DownloadIcon />}
                >
                  Telecharger
                </Button>
              </CardActions>
            </Card>
          ))}
        </Stack>
      ))}

      <Typography variant="caption" color="text.secondary">
        Manifest genere le: {formatDate(manifest.generatedAt)}
      </Typography>
    </Stack>
  );
};
