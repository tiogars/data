import InboxIcon from '@mui/icons-material/Inbox';
import LinkIcon from '@mui/icons-material/Link';
import GitHubIcon from '@mui/icons-material/GitHub';
import KeyIcon from '@mui/icons-material/Key';
import MemoryIcon from '@mui/icons-material/Memory';
import MenuIcon from '@mui/icons-material/Menu';
import AndroidIcon from '@mui/icons-material/Android';
import SettingsEthernetIcon from '@mui/icons-material/SettingsEthernet';
import SecurityIcon from '@mui/icons-material/Security';
import RouteIcon from '@mui/icons-material/Route';
import type { SvgIconProps } from '@mui/material/SvgIcon';
import SettingsIcon from '@mui/icons-material/Settings';
import DirectionsCarIcon from '@mui/icons-material/DirectionsCar';
import ShowChartIcon from '@mui/icons-material/ShowChart';
import AddCircleIcon from '@mui/icons-material/AddCircle';
import TableViewIcon from '@mui/icons-material/TableView';
import EditNoteIcon from '@mui/icons-material/EditNote';

export const menuItemIconComponents = {
  inbox: InboxIcon,
  link: LinkIcon,
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
  table_view: TableViewIcon,
  edit_note: EditNoteIcon,
} as const;

export const menuItemIconOptions = [
  { value: 'inbox', label: 'Sections' },
  { value: 'link', label: 'Liens' },
  { value: 'github', label: 'GitHub' },
  { value: 'key', label: 'Cle' },
  { value: 'memory', label: 'Memoire' },
  { value: 'menu', label: 'Menu' },
  { value: 'android', label: 'Android' },
  { value: 'settings', label: 'Paramètres' },
] as const;

export function renderMenuItemIcon(icon: string | undefined, fontSize: SvgIconProps['fontSize'] = 'small') {
  const IconComponent = icon ? menuItemIconComponents[icon as keyof typeof menuItemIconComponents] : undefined;
  const ResolvedIcon = IconComponent ?? RouteIcon;
  return <ResolvedIcon fontSize={fontSize} />;
}
