import { AppBar, Box, Toolbar, Typography, Container, IconButton, Button, Chip } from '@mui/material';
import MenuBookOutlinedIcon from '@mui/icons-material/MenuBookOutlined';
import BugReportOutlinedIcon from '@mui/icons-material/BugReportOutlined';
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
                        <IconButton
                            aria-label="Documentation"
                            color="inherit"
                            href="https://docs.data.tiogars.fr"
                            rel="noopener noreferrer"
                            target="_blank"
                        >
                            <MenuBookOutlinedIcon />
                        </IconButton>
                        <IconButton
                            aria-label="Signaler un problème"
                            color="inherit"
                            href="https://github.com/tiogars/data/issues/new"
                            rel="noopener noreferrer"
                            target="_blank"
                        >
                            <BugReportOutlinedIcon />
                        </IconButton>
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
