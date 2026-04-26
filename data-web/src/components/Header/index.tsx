import { AppBar, Box, Toolbar, Typography, Container } from '@mui/material';
import CustomBreadcrumbs from '../Breadcrumbs';
import ThemeModeSelector from '../ThemeModeSelector';

const Header = () => {

    return (
        <AppBar position="static" color="default" elevation={1} sx={{ mb: 2 }}>
            <Container maxWidth={false} disableGutters>
                <Toolbar sx={{ display: 'flex', alignItems: 'center', justifyContent: 'space-between' }}>
                    <Typography variant="h5">Data Web</Typography>
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
