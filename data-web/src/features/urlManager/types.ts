export type ManagedUrl = {
  id: string;
  label: string;
  url: string;
  tags: string[];
  description?: string;
};

export type UrlCardMatchMode = 'any' | 'all';

export type UrlCardConfig = {
  id: string;
  title: string;
  tags: string[];
  matchMode: UrlCardMatchMode;
};

export type UrlManagerState = {
  urls: ManagedUrl[];
  cards: UrlCardConfig[];
};
