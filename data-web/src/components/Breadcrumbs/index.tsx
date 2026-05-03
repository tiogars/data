import Breadcrumbs from "@mui/material/Breadcrumbs";
import Link from "@mui/material/Link";
import Typography from "@mui/material/Typography";
import { useLocation, Link as RouterLink } from "react-router-dom";

const breadcrumbNameMap: Record<string, string> = {
    "/section": "Sections",
    "/section/create": "Créer",
    "/section/:id": "Détail",
    "/section/:id/edit": "Modifier",
    "/menu-item": "Menu gauche",
    "/menu-item/create": "Créer",
    "/gateway-config": "Configuration gateway",
};

function getBreadcrumbs(pathname: string) {
    const pathnames = pathname.split("/").filter(Boolean);
    const breadcrumbs = [
        <Link underline="hover" color="inherit" component={RouterLink} to="/" key="home">
            Accueil
        </Link>,
    ];
    pathnames.forEach((value, index) => {
        const to = `/${pathnames.slice(0, index + 1).join("/")}`;
        let label = breadcrumbNameMap[to];
        // Gestion dynamique pour /section/:id et /section/:id/edit
        if (!label && /^\/section\/\d+$/.test(to)) label = "Détail";
        if (!label && /^\/section\/\d+\/edit$/.test(to)) label = "Modifier";
        if (!label && /^\/menu-item\/.+/.test(to) && !to.endsWith('/edit')) label = "Détail";
        if (!label && /^\/menu-item\/.+\/edit$/.test(to)) label = "Modifier";
        if (!label) label = value;
        const isLast = index === pathnames.length - 1;
        if (isLast) {
            breadcrumbs.push(
                <Typography color="text.primary" key={to}>
                    {label}
                </Typography>
            );
        } else {
            breadcrumbs.push(
                <Link underline="hover" color="inherit" component={RouterLink} to={to} key={to}>
                    {label}
                </Link>
            );
        }
    });
    return breadcrumbs;
}

const CustomBreadcrumbs = () => {
    const location = useLocation();
    return (
        <Breadcrumbs aria-label="breadcrumb" sx={{ mb: 2 }}>
            {getBreadcrumbs(location.pathname)}
        </Breadcrumbs>
    );
};

export default CustomBreadcrumbs;
