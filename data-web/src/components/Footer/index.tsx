
import { Box, CircularProgress, IconButton, Link, Typography } from '@mui/material';
import { renderFooterLinkIcon } from '../../features/footerLink/iconRegistry';
import { type FooterLink, useListFooterLinksQuery } from '../../services/footerLinkApi';
import { useOidcAuth } from '../../auth/OidcAuthProvider';

type FooterProps = {
    items?: FooterLink[];
};

type FooterContentProps = {
    items: FooterLink[];
    isLoading?: boolean;
    hasError?: boolean;
};

const FooterContent = ({ items, isLoading = false, hasError = false }: FooterContentProps) => {
    return (
        <Box component="footer" sx={{ py: 2, px: 2, textAlign: 'center', bgcolor: 'background.paper', borderTop: 1, borderColor: 'divider' }}>
            <Typography variant="body2" color="text.secondary" sx={{ mb: 1 }}>
                Stack technique&nbsp;:
                {isLoading && <CircularProgress size={16} sx={{ ml: 1, verticalAlign: 'middle' }} />}
                {!isLoading && items.map((item) => (
                    <IconButton
                        key={item.id ?? `${item.label}-${item.url}`}
                        component={Link}
                        href={item.url}
                        target="_blank"
                        rel="noopener"
                        aria-label={item.label ?? item.url ?? 'Lien footer'}
                        sx={{ mx: 0.5 }}
                        size="small"
                    >
                        {renderFooterLinkIcon(item.icon)}
                    </IconButton>
                ))}
            </Typography>
            {hasError && (
                <Typography variant="caption" color="error.main" sx={{ display: 'block', mb: 1 }}>
                    Impossible de charger les liens du footer.
                </Typography>
            )}
            <Typography variant="caption" color="text.disabled">
                © {new Date().getFullYear()} tiogars / Projet Data
            </Typography>
        </Box>
    );
};

const FooterWithApi = () => {
    const { isAuthenticated } = useOidcAuth();
    const { data, isLoading, error } = useListFooterLinksQuery(undefined, {
        skip: !isAuthenticated,
        refetchOnMountOrArgChange: true,
    });

    return <FooterContent items={data?.items ?? []} isLoading={isLoading} hasError={Boolean(error)} />;
};

const Footer = ({ items }: FooterProps) => {
    if (items) {
        return <FooterContent items={items} />;
    }

    return <FooterWithApi />;
};

export default Footer;
