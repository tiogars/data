import InboxIcon from '@mui/icons-material/Inbox';
import LinkIcon from '@mui/icons-material/Link';
import GitHubIcon from '@mui/icons-material/GitHub';
import KeyIcon from '@mui/icons-material/Key';
import MemoryIcon from '@mui/icons-material/Memory';
import MenuIcon from '@mui/icons-material/Menu';
import SettingsEthernetIcon from '@mui/icons-material/SettingsEthernet';
import SecurityIcon from '@mui/icons-material/Security';
import RouteIcon from '@mui/icons-material/Route';
import type { SvgIconProps } from '@mui/material/SvgIcon';

const iconComponents = {
  inbox: InboxIcon,
  link: LinkIcon,
  github: GitHubIcon,
  key: KeyIcon,
  memory: MemoryIcon,
  menu: MenuIcon,
  gateway: SettingsEthernetIcon,
  auth: SecurityIcon,
} as const;

export const menuItemIconOptions = [
  { value: 'inbox', label: 'Sections' },
  { value: 'link', label: 'Liens' },
  { value: 'github', label: 'GitHub' },
  { value: 'key', label: 'Cle' },
  { value: 'memory', label: 'Memoire' },
  { value: 'menu', label: 'Menu' },
] as const;

export function renderMenuItemIcon(icon: string | undefined, fontSize: SvgIconProps['fontSize'] = 'small') {
  const IconComponent = icon ? iconComponents[icon as keyof typeof iconComponents] : undefined;
  const ResolvedIcon = IconComponent ?? RouteIcon;
  return <ResolvedIcon fontSize={fontSize} />;
}
