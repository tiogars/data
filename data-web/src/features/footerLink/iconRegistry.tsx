import ReactIcon from '@mui/icons-material/AutoAwesome';
import CodeIcon from '@mui/icons-material/Code';
import FlashOnIcon from '@mui/icons-material/FlashOn';
import GitHubIcon from '@mui/icons-material/GitHub';
import IntegrationInstructionsIcon from '@mui/icons-material/IntegrationInstructions';
import LinkIcon from '@mui/icons-material/Link';
import StorageIcon from '@mui/icons-material/Storage';
import type { SvgIconProps } from '@mui/material/SvgIcon';

const iconComponents = {
  react: ReactIcon,
  mui: IntegrationInstructionsIcon,
  redux: FlashOnIcon,
  vite: FlashOnIcon,
  typescript: CodeIcon,
  java: IntegrationInstructionsIcon,
  spring: StorageIcon,
  github: GitHubIcon,
} as const;

export const footerLinkIconOptions = [
  { value: 'react', label: 'React' },
  { value: 'mui', label: 'MUI' },
  { value: 'redux', label: 'Redux Toolkit' },
  { value: 'vite', label: 'Vite' },
  { value: 'typescript', label: 'TypeScript' },
  { value: 'java', label: 'Java' },
  { value: 'spring', label: 'Spring Boot' },
  { value: 'github', label: 'GitHub' },
] as const;

export function renderFooterLinkIcon(icon: string | undefined, fontSize: SvgIconProps['fontSize'] = 'small') {
  const IconComponent = icon ? iconComponents[icon as keyof typeof iconComponents] : undefined;
  const ResolvedIcon = IconComponent ?? LinkIcon;
  return <ResolvedIcon fontSize={fontSize} />;
}