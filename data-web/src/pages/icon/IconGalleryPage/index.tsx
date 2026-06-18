import { useMemo, useState } from 'react';
import Box from '@mui/material/Box';
import Button from '@mui/material/Button';
import Chip from '@mui/material/Chip';
import Grid from '@mui/material/Grid';
import InputAdornment from '@mui/material/InputAdornment';
import Paper from '@mui/material/Paper';
import Stack from '@mui/material/Stack';
import TextField from '@mui/material/TextField';
import Tooltip from '@mui/material/Tooltip';
import Typography from '@mui/material/Typography';
import LaunchIcon from '@mui/icons-material/Launch';
import SearchIcon from '@mui/icons-material/Search';
import { menuItemIconComponents } from '../../../features/menuItem/iconRegistry';
import { footerLinkIconComponents } from '../../../features/footerLink/iconRegistry';

const MUI_ICONS_SEARCH_URL = 'https://mui.com/material-ui/material-icons/#search-material-icons';

type IconEntry = {
  key: string;
  context: 'menuItem' | 'footerLink';
  label: string;
};

const menuItemEntries: IconEntry[] = Object.keys(menuItemIconComponents).map((key) => ({
  key,
  context: 'menuItem',
  label: key,
}));

const footerLinkEntries: IconEntry[] = Object.keys(footerLinkIconComponents).map((key) => ({
  key,
  context: 'footerLink',
  label: key,
}));

const menuItemKeys = new Set(menuItemEntries.map((e) => e.key));
const footerLinkKeys = new Set(footerLinkEntries.map((e) => e.key));

const allIconEntries: IconEntry[] = [
  ...menuItemEntries,
  ...footerLinkEntries.filter((e) => !menuItemKeys.has(e.key)),
];

const contextLabels: Record<string, string> = {
  menuItem: 'Menu',
  footerLink: 'Footer',
};

const contextColors: Record<string, 'primary' | 'secondary'> = {
  menuItem: 'primary',
  footerLink: 'secondary',
};

export const IconGalleryPage = () => {
  const [search, setSearch] = useState('');

  const filtered = useMemo(() => {
    const q = search.trim().toLowerCase();
    if (!q) return allIconEntries;
    return allIconEntries.filter((e) => e.key.toLowerCase().includes(q));
  }, [search]);

  return (
    <Stack spacing={3} sx={{ p: { xs: 2, md: 3 }, maxWidth: 1200, mx: 'auto' }}>
      <Box>
        <Typography variant="h4" component="h1">
          Icones disponibles
        </Typography>
        <Typography variant="body2" color="text.secondary">
          Visualisez toutes les icones enregistrees dans l'application. Utilisez la recherche pour affiner le mot cle.
        </Typography>
      </Box>

      <Stack direction={{ xs: 'column', sm: 'row' }} spacing={1.5} sx={{ alignItems: { sm: 'center' } }}>
        <TextField
          size="small"
          placeholder="Rechercher une icone..."
          value={search}
          onChange={(e) => setSearch(e.target.value)}
          sx={{ maxWidth: 360 }}
          slotProps={{
            input: {
              startAdornment: (
                <InputAdornment position="start">
                  <SearchIcon fontSize="small" />
                </InputAdornment>
              ),
            },
          }}
        />
        <Chip label={`${filtered.length} icone${filtered.length > 1 ? 's' : ''}`} variant="outlined" />
        <Button
          variant="outlined"
          size="small"
          endIcon={<LaunchIcon fontSize="small" />}
          href={MUI_ICONS_SEARCH_URL}
          target="_blank"
          rel="noopener noreferrer"
        >
          Rechercher sur MUI
        </Button>
      </Stack>

      {filtered.length === 0 && (
        <Typography color="text.secondary">
          Aucune icone ne correspond a la recherche.
        </Typography>
      )}

      <Grid container spacing={2}>
        {filtered.map((entry) => {
          const allContexts = [
            ...(menuItemKeys.has(entry.key) ? ['menuItem'] : []),
            ...(footerLinkKeys.has(entry.key) ? ['footerLink'] : []),
          ];
          const IconComponent =
            menuItemIconComponents[entry.key as keyof typeof menuItemIconComponents] ??
            footerLinkIconComponents[entry.key as keyof typeof footerLinkIconComponents];

          return (
            <Grid key={entry.key} size={{ xs: 6, sm: 4, md: 3, lg: 2 }}>
              <Tooltip title={entry.key} placement="top">
                <Paper
                  variant="outlined"
                  sx={{
                    p: 1.5,
                    display: 'flex',
                    flexDirection: 'column',
                    alignItems: 'center',
                    gap: 1,
                    cursor: 'default',
                    '&:hover': { bgcolor: 'action.hover' },
                    minHeight: 100,
                    justifyContent: 'center',
                  }}
                >
                  <IconComponent sx={{ fontSize: 32 }} />
                  <Typography
                    variant="caption"
                    sx={{
                      wordBreak: 'break-all',
                      textAlign: 'center',
                      lineHeight: 1.3,
                    }}
                  >
                    {entry.key}
                  </Typography>
                  <Stack direction="row" spacing={0.5} useFlexGap sx={{ flexWrap: 'wrap', justifyContent: 'center' }}>
                    {allContexts.map((ctx) => (
                      <Chip
                        key={ctx}
                        label={contextLabels[ctx]}
                        size="small"
                        color={contextColors[ctx]}
                        variant="outlined"
                        sx={{ fontSize: 10, height: 18, '& .MuiChip-label': { px: 0.75 } }}
                      />
                    ))}
                  </Stack>
                </Paper>
              </Tooltip>
            </Grid>
          );
        })}
      </Grid>
    </Stack>
  );
};
