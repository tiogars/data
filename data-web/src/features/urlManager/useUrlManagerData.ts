import { useGetUrlManagerStateQuery } from '../../services/urlManagerApi';

export function useUrlManagerData() {
  const {
    data,
    isLoading,
    isFetching,
    refetch,
  } = useGetUrlManagerStateQuery(undefined, {
    refetchOnMountOrArgChange: true,
    refetchOnFocus: true,
  });

  return {
    urls: data?.urls ?? [],
    cards: data?.cards ?? [],
    isLoading: isLoading || isFetching,
    refresh: refetch,
  };
}
