import Link from '@mui/material/Link';
import { Link as RouterLink } from 'react-router-dom';
import { useListMaisonsQuery } from '../services/maisonApi';
import { WebsiteLink } from './WebsiteLink';

type MaisonReferenceProps = {
  maisonId?: string;
  maisonName?: string;
  emptyLabel?: string;
  showWebsite?: boolean;
  websiteLabel?: string;
};

export const MaisonReference = ({
  maisonId,
  maisonName,
  emptyLabel = '-',
  showWebsite = false,
  websiteLabel = 'Site web',
}: MaisonReferenceProps) => {
  const { data } = useListMaisonsQuery(undefined, { refetchOnMountOrArgChange: true });
  const maison = (data?.items ?? []).find((item) => item.id === maisonId);
  const displayName = maison?.name ?? maisonName ?? maisonId;

  if (!displayName) {
    return <>{emptyLabel}</>;
  }

  const detailLink = maisonId ? (
    <Link component={RouterLink} to={`/maison/${maisonId}`}>
      {displayName}
    </Link>
  ) : (
    <>{displayName}</>
  );

  if (!showWebsite || !maison?.website) {
    return detailLink;
  }

  return (
    <>
      {detailLink}
      {' · '}
      <WebsiteLink href={maison.website}>{websiteLabel}</WebsiteLink>
    </>
  );
};