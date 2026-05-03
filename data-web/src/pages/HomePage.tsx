import { Link as RouterLink } from 'react-router-dom';
import Alert from '@mui/material/Alert';
import Box from '@mui/material/Box';
import Button from '@mui/material/Button';
import Card from '@mui/material/Card';
import CardActions from '@mui/material/CardActions';
import CardContent from '@mui/material/CardContent';
import Chip from '@mui/material/Chip';
import Grid from '@mui/material/Grid';
import Stack from '@mui/material/Stack';
import Typography from '@mui/material/Typography';
import OpenInNewIcon from '@mui/icons-material/OpenInNew';
import { useMemo } from 'react';
import { useUrlManagerData } from '../features/urlManager/useUrlManagerData';
import type { ManagedUrl, UrlCardConfig } from '../features/urlManager/types';

function matchesCard(url: ManagedUrl, card: UrlCardConfig): boolean {
  if (card.matchMode === 'all') {
    return card.tags.every((tag) => url.tags.includes(tag));
  }

  return card.tags.some((tag) => url.tags.includes(tag));
}

const HomePage = () => {
  const { urls, cards, isLoading } = useUrlManagerData();

  const cardsWithLinks = useMemo(() => {
    return cards.map((card) => {
      const links = urls.filter((item) => matchesCard(item, card));

      return {
        ...card,
        links,
      };
    });
  }, [cards, urls]);

  if (isLoading) {
    return <div>Chargement...</div>;
  }

  return (
    <Stack spacing={3} sx={{ p: { xs: 2, md: 3 } }}>
      <Box>
        <Typography variant="h4" component="h1">Bienvenue sur Data Web</Typography>
        <Typography variant="body1" color="text.secondary">
          Les cartes ci-dessous sont configurees depuis Gestion URLs et Cartes accueil.
        </Typography>
      </Box>

      <Stack direction={{ xs: 'column', sm: 'row' }} spacing={1.5}>
        <Button component={RouterLink} to="/url-manager" variant="contained">Gerer les URLs</Button>
        <Button component={RouterLink} to="/url-cards" variant="outlined">Configurer les cartes</Button>
      </Stack>

      {cards.length === 0 && (
        <Alert severity="info">
          Aucune carte configuree. Ajoutez des URLs taguees puis creez des cartes sur la page Cartes accueil.
        </Alert>
      )}

      <Grid container spacing={2}>
        {cardsWithLinks.map((card) => (
          <Grid key={card.id} size={{ xs: 12, md: 6, lg: 4 }}>
            <Card variant="outlined" sx={{ height: '100%' }}>
              <CardContent>
                <Stack spacing={1.25}>
                  <Typography variant="h6">{card.title}</Typography>
                  <Typography variant="body2" color="text.secondary">
                    {card.matchMode === 'any' ? 'Affichage par tag' : 'Affichage par reunion de tags'}
                  </Typography>
                  <Stack direction="row" spacing={0.75} useFlexGap sx={{ flexWrap: 'wrap' }}>
                    {card.tags.map((tag) => (
                      <Chip key={tag} size="small" label={tag} variant="outlined" />
                    ))}
                  </Stack>

                  {card.links.length === 0 ? (
                    <Alert severity="warning">Aucun lien ne correspond a ce filtre.</Alert>
                  ) : (
                    <Stack spacing={1}>
                      {card.links.map((item) => (
                        <Box key={item.id}>
                          <Typography sx={{ fontWeight: 600 }}>{item.label}</Typography>
                          <Typography variant="body2" color="text.secondary" sx={{ wordBreak: 'break-word' }}>
                            {item.description || item.url}
                          </Typography>
                          <Button
                            size="small"
                            href={item.url}
                            target="_blank"
                            rel="noreferrer"
                            endIcon={<OpenInNewIcon fontSize="small" />}
                            sx={{ mt: 0.5, px: 0 }}
                          >
                            Ouvrir
                          </Button>
                        </Box>
                      ))}
                    </Stack>
                  )}
                </Stack>
              </CardContent>
              <CardActions sx={{ px: 2, pb: 2, pt: 0 }}>
                <Chip size="small" label={`${card.links.length} lien${card.links.length > 1 ? 's' : ''}`} color={card.links.length > 0 ? 'success' : 'default'} variant="outlined" />
              </CardActions>
            </Card>
          </Grid>
        ))}
      </Grid>
    </Stack>
  );
};

export default HomePage;
