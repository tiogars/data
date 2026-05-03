import type { ManagedUrl, UrlCardConfig, UrlManagerState } from './types';

function normalizeTag(tag: string): string {
  return tag.trim().toLowerCase();
}

function normalizeTags(tags: string[] | undefined): string[] {
  const unique = new Set((tags ?? []).map(normalizeTag).filter(Boolean));
  return Array.from(unique).sort((a, b) => a.localeCompare(b));
}

export function parseTagsInput(value: string): string[] {
  return normalizeTags(value.split(','));
}

export function getAllKnownTags(urls: ManagedUrl[]): string[] {
  const tags = new Set<string>();

  urls.forEach((item) => {
    item.tags.forEach((tag) => {
      tags.add(normalizeTag(tag));
    });
  });

  return Array.from(tags).sort((a, b) => a.localeCompare(b));
}

export function addManagedUrl(urls: ManagedUrl[], input: Omit<ManagedUrl, 'id'>): ManagedUrl[] {
  const item: ManagedUrl = {
    id: crypto.randomUUID(),
    label: input.label.trim(),
    url: input.url.trim(),
    tags: normalizeTags(input.tags),
    description: input.description?.trim() || undefined,
  };

  return [...urls, item].sort((a, b) => a.label.localeCompare(b.label));
}

export function updateManagedUrl(urls: ManagedUrl[], id: string, input: Omit<ManagedUrl, 'id'>): ManagedUrl[] {
  const next = urls.map((item) => {
    if (item.id !== id) {
      return item;
    }

    return {
      id,
      label: input.label.trim(),
      url: input.url.trim(),
      tags: normalizeTags(input.tags),
      description: input.description?.trim() || undefined,
    };
  });

  return next.sort((a, b) => a.label.localeCompare(b.label));
}

export function deleteManagedUrl(urls: ManagedUrl[], id: string): ManagedUrl[] {
  return urls.filter((item) => item.id !== id);
}

export function addUrlCard(cards: UrlCardConfig[], input: Omit<UrlCardConfig, 'id'>): UrlCardConfig[] {
  const item: UrlCardConfig = {
    id: crypto.randomUUID(),
    title: input.title.trim(),
    tags: normalizeTags(input.tags),
    matchMode: input.matchMode,
  };

  return [...cards, item].sort((a, b) => a.title.localeCompare(b.title));
}

export function updateUrlCard(cards: UrlCardConfig[], id: string, input: Omit<UrlCardConfig, 'id'>): UrlCardConfig[] {
  const next = cards.map((item) => {
    if (item.id !== id) {
      return item;
    }

    return {
      id,
      title: input.title.trim(),
      tags: normalizeTags(input.tags),
      matchMode: input.matchMode,
    };
  });

  return next.sort((a, b) => a.title.localeCompare(b.title));
}

export function deleteUrlCard(cards: UrlCardConfig[], id: string): UrlCardConfig[] {
  return cards.filter((item) => item.id !== id);
}

export function normalizeImportedState(raw: unknown): UrlManagerState {
  const unsafe = raw as Partial<UrlManagerState> | null;

  const urls = (unsafe?.urls ?? [])
    .filter(Boolean)
    .map((item) => {
      const url = item as Partial<ManagedUrl>;
      return {
        id: url.id ?? crypto.randomUUID(),
        label: (url.label ?? '').trim(),
        url: (url.url ?? '').trim(),
        tags: normalizeTags(url.tags),
        description: (url.description ?? '').trim() || undefined,
      };
    })
    .filter((item) => item.label.length > 0 && item.url.length > 0)
    .sort((a, b) => a.label.localeCompare(b.label));

  const cards = (unsafe?.cards ?? [])
    .filter(Boolean)
    .map((item) => {
      const card = item as Partial<UrlCardConfig>;
      const matchMode: UrlCardConfig['matchMode'] = card.matchMode === 'all' ? 'all' : 'any';
      return {
        id: card.id ?? crypto.randomUUID(),
        title: (card.title ?? '').trim(),
        tags: normalizeTags(card.tags),
        matchMode,
      };
    })
    .filter((item) => item.title.length > 0 && item.tags.length > 0)
    .sort((a, b) => a.title.localeCompare(b.title));

  return { urls, cards };
}

export function createExportFileName(now = new Date()): string {
  const yyyy = String(now.getFullYear());
  const mm = String(now.getMonth() + 1).padStart(2, '0');
  const dd = String(now.getDate()).padStart(2, '0');
  const hh = String(now.getHours()).padStart(2, '0');
  const min = String(now.getMinutes()).padStart(2, '0');

  return `url-manager-export-${yyyy}${mm}${dd}-${hh}${min}.json`;
}
