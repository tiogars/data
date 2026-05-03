import { AppBar, Box, Toolbar, Typography, Container, IconButton, Button, Chip } from '@mui/material';
import MenuIcon from '@mui/icons-material/Menu';
import CustomBreadcrumbs from '../Breadcrumbs';
import ThemeModeSelector from '../ThemeModeSelector';
import type { FC } from 'react';
import { Link as RouterLink } from 'react-router-dom';
import { useOidcAuth } from '../../auth/OidcAuthProvider';

interface HeaderProps {
    onMenuClick: () => void;
}

const Header: FC<HeaderProps> = ({ onMenuClick }) => {
    const { user, isAuthenticated, isLoading, login, logout } = useOidcAuth();
    const preferredUsername = typeof user?.profile?.preferred_username === 'string' ? user.profile.preferred_username : null;

    const handleLogin = () => {
        void login();
    };

    const handleLogout = () => {
        void logout();
    };

    return (
        <AppBar position="static" color="default" elevation={1} sx={{ mb: 2 }}>
            <Container maxWidth={false} disableGutters>
                <Toolbar sx={{ display: 'flex', alignItems: 'center', justifyContent: 'space-between' }}>
                    <Box sx={{ display: 'flex', alignItems: 'center', gap: 1 }}>
                        <IconButton edge="start" color="inherit" aria-label="menu" onClick={onMenuClick} sx={{ mr: 1 }}>
                            <MenuIcon />
                        </IconButton>
                        <Typography variant="h5">Data Web</Typography>
                    </Box>
                    <Box sx={{ display: 'flex', alignItems: 'center', gap: 1 }}>
                        {isAuthenticated && preferredUsername && (
                            <Chip
                                size="small"
                                color="success"
                                label={preferredUsername}
                                clickable
                                component={RouterLink}
                                to="/auth/account"
                            />
                        )}
                        <Button
                            variant={isAuthenticated ? 'outlined' : 'contained'}
                            size="small"
                            onClick={isAuthenticated ? handleLogout : handleLogin}
                            disabled={isLoading}
                        >
                            {isAuthenticated ? 'Deconnexion' : 'Connexion'}
                        </Button>
                        <ThemeModeSelector />
                    </Box>
                </Toolbar>
                <Box sx={{ px: 2 }}>
                    <CustomBreadcrumbs />
                </Box>
            </Container>
        </AppBar>
    );
};

export default Header;
