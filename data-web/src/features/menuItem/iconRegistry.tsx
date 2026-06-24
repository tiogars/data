import AddCircleIcon from "@mui/icons-material/AddCircle";
import AndroidIcon from "@mui/icons-material/Android";
import DesktopWindowsIcon from "@mui/icons-material/DesktopWindows";
import DirectionsCarIcon from "@mui/icons-material/DirectionsCar";
import EditNoteIcon from "@mui/icons-material/EditNote";
import GamesIcon from "@mui/icons-material/Games";
import GitHubIcon from "@mui/icons-material/GitHub";
import InboxIcon from "@mui/icons-material/Inbox";
import KeyIcon from "@mui/icons-material/Key";
import LinkIcon from "@mui/icons-material/Link";
import MemoryIcon from "@mui/icons-material/Memory";
import MenuIcon from "@mui/icons-material/Menu";
import RouteIcon from "@mui/icons-material/Route";
import SecurityIcon from "@mui/icons-material/Security";
import SettingsIcon from "@mui/icons-material/Settings";
import SettingsEthernetIcon from "@mui/icons-material/SettingsEthernet";
import ShowChartIcon from "@mui/icons-material/ShowChart";
import SmartToyIcon from '@mui/icons-material/SmartToy';
import TableViewIcon from "@mui/icons-material/TableView";
import type { SvgIconProps } from "@mui/material/SvgIcon";

export const menuItemIconComponents = {
    inbox: InboxIcon,
    link: LinkIcon,
    games: GamesIcon,
    github: GitHubIcon,
    key: KeyIcon,
    memory: MemoryIcon,
    menu: MenuIcon,
    android: AndroidIcon,
    gateway: SettingsEthernetIcon,
    auth: SecurityIcon,
    settings: SettingsIcon,
    directions_car: DirectionsCarIcon,
    monitoring: ShowChartIcon,
    add_circle: AddCircleIcon,
    smart_toy: SmartToyIcon,
    table_view: TableViewIcon,
    edit_note: EditNoteIcon,
    desktop_windows: DesktopWindowsIcon,
} as const;

export const menuItemIconOptions = [
    { value: "inbox", label: "Sections" },
    { value: "link", label: "Liens" },
    { value: "games", label: "Jeux" },
    { value: "github", label: "GitHub" },
    { value: "key", label: "Cle" },
    { value: "memory", label: "Memoire" },
    { value: "menu", label: "Menu" },
    { value: "android", label: "Android" },
    { value: "desktop_windows", label: "Windows" },
    { value: "smart_toy", label: "Smart Toy" },
    { value: "settings", label: "Paramètres" },
] as const;

export function renderMenuItemIcon(
    icon: string | undefined,
    fontSize: SvgIconProps["fontSize"] = "small",
) {
    const IconComponent = icon
        ? menuItemIconComponents[icon as keyof typeof menuItemIconComponents]
        : undefined;
    const ResolvedIcon = IconComponent ?? RouteIcon;
    return <ResolvedIcon fontSize={fontSize} />;
}
