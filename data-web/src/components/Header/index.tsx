import { AppBar, Box, Toolbar, Typography, Container, IconButton } from '@mui/material';
import MenuIcon from '@mui/icons-material/Menu';
import CustomBreadcrumbs from '../Breadcrumbs';
import ThemeModeSelector from '../ThemeModeSelector';
import type { FC } from 'react';

interface HeaderProps {
    onMenuClick: () => void;
}

const Header: FC<HeaderProps> = ({ onMenuClick }) => {
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
                    <ThemeModeSelector />
                </Toolbar>
                <Box sx={{ px: 2 }}>
                    <CustomBreadcrumbs />
                </Box>
            </Container>
        </AppBar>
    );
};

export default Header;
