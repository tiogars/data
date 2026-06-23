import type { ReactNode } from 'react';
import Link from '@mui/material/Link';

export const normalizeWebsiteUrl = (value: string | undefined) => {
  const trimmedValue = value?.trim();
  if (!trimmedValue) return '';

  return trimmedValue;
};

type WebsiteLinkProps = {
  href: string | undefined;
  children?: ReactNode;
};

export const WebsiteLink = ({ href, children }: WebsiteLinkProps) => {
  const normalizedHref = normalizeWebsiteUrl(href);

  if (!normalizedHref) {
    return <>{children ?? 'Aucun site web'}</>;
  }

  return (
    <Link href={normalizedHref} target="_blank" rel="noopener noreferrer">
      {children ?? normalizedHref}
    </Link>
  );
};