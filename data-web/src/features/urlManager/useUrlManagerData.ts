import { useMemo } from 'react';
import { useGetStateQuery } from '../../services/urlManagerApi';
import { normalizeImportedState } from './storage';

export function useUrlManagerData() {
  const {
    data,
    isLoading,
    isFetching,
    refetch,
  } = useGetStateQuery(undefined, {
    refetchOnMountOrArgChange: true,
    refetchOnFocus: true,
  });

  const state = useMemo(() => normalizeImportedState(data ?? {}), [data]);

  return {
    urls: state.urls,
    cards: state.cards,
    isLoading: isLoading || isFetching,
    refresh: refetch,
  };
}
