
import ReactIcon from '@mui/icons-material/AutoAwesome'; // Remplacer par une icône React personnalisée si besoin
import CodeIcon from '@mui/icons-material/Code';
import FlashOnIcon from '@mui/icons-material/FlashOn';
import GitHubIcon from '@mui/icons-material/GitHub';
import IntegrationInstructionsIcon from '@mui/icons-material/IntegrationInstructions';
import StorageIcon from '@mui/icons-material/Storage';
import { Box, IconButton, Link, Typography } from '@mui/material';

const stackLinks = [
    {
        label: 'React',
        url: 'https://react.dev/',
        icon: <ReactIcon fontSize="small" />,
    },
    {
        label: 'MUI',
        url: 'https://mui.com/',
        icon: <IntegrationInstructionsIcon fontSize="small" />,
    },
    {
        label: 'Redux Toolkit',
        url: 'https://redux-toolkit.js.org/',
        icon: <FlashOnIcon fontSize="small" />,
    },
    {
        label: 'Vite',
        url: 'https://vitejs.dev/',
        icon: <FlashOnIcon fontSize="small" />,
    },
    {
        label: 'TypeScript',
        url: 'https://www.typescriptlang.org/',
        icon: <CodeIcon fontSize="small" />,
    },
    {
        label: 'Java',
        url: 'https://adoptium.net/',
        icon: <IntegrationInstructionsIcon fontSize="small" />,
    },
    {
        label: 'Spring Boot',
        url: 'https://spring.io/projects/spring-boot',
        icon: <StorageIcon fontSize="small" />,
    },
    {
        label: 'GitHub',
        url: 'https://github.com/tiogars/data',
        icon: <GitHubIcon fontSize="small" />,
    },
];

const Footer = () => {
    return (
        <Box component="footer" sx={{ py: 2, px: 2, textAlign: 'center', bgcolor: 'background.paper', borderTop: 1, borderColor: 'divider' }}>
            <Typography variant="body2" color="text.secondary" sx={{ mb: 1 }}>
                Stack technique&nbsp;:
                {stackLinks.map((item, idx) => (
                    <IconButton
                        key={item.label}
                        component={Link}
                        href={item.url}
                        target="_blank"
                        rel="noopener"
                        aria-label={item.label}
                        sx={{ mx: 0.5 }}
                        size="small"
                    >
                        {item.icon}
                    </IconButton>
                ))}
            </Typography>
            <Typography variant="caption" color="text.disabled">
                © {new Date().getFullYear()} tiogars / Projet Data
            </Typography>
        </Box>
    );
};

export default Footer;
