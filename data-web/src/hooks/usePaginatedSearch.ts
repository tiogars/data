import { useCallback, useEffect, useMemo, useState, type ChangeEvent } from 'react';

type UsePaginatedSearchOptions = {
  initialPageSize?: number;
  debounceMs?: number;
};

/**
 * Gere la recherche debouncee et la pagination communes aux pages de liste.
 */
export function usePaginatedSearch({
  initialPageSize = 10,
  debounceMs = 300,
}: UsePaginatedSearchOptions = {}) {
  const [searchInput, setSearchInput] = useState('');
  const [searchQuery, setSearchQuery] = useState('');
  const [page, setPage] = useState(0);
  const [pageSize, setPageSize] = useState(initialPageSize);

  useEffect(() => {
    const timeout = setTimeout(() => {
      setSearchQuery(searchInput.trim());
      setPage(0);
    }, debounceMs);

    return () => clearTimeout(timeout);
  }, [searchInput, debounceMs]);

  const queryArgs = useMemo(
    () => ({
      page,
      size: pageSize,
      q: searchQuery || undefined,
    }),
    [page, pageSize, searchQuery]
  );

  const handlePageChange = useCallback((_event: unknown, nextPage: number) => {
    setPage(nextPage);
  }, []);

  const handlePageSizeChange = useCallback((event: ChangeEvent<HTMLInputElement | HTMLTextAreaElement>) => {
    setPageSize(Number(event.target.value));
    setPage(0);
  }, []);

  return {
    searchInput,
    setSearchInput,
    searchQuery,
    page,
    pageSize,
    queryArgs,
    handlePageChange,
    handlePageSizeChange,
  };
}
