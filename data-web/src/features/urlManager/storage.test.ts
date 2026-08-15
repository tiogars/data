import { afterEach, beforeEach, describe, expect, it, vi } from 'vitest';
import {
  addManagedUrl,
  addUrlCard,
  createExportFileName,
  deleteManagedUrl,
  deleteUrlCard,
  getAllKnownTags,
  normalizeImportedState,
  parseTagsInput,
  updateManagedUrl,
  updateUrlCard,
} from './storage';
import type { ManagedUrl, UrlCardConfig } from './types';

const managedUrls: ManagedUrl[] = [
  {
    id: 'url-1',
    label: 'Zulu',
    url: 'https://zulu.example',
    tags: ['work', 'tools'],
  },
];

const urlCards: UrlCardConfig[] = [
  { id: 'card-1', title: 'Work', tags: ['work'], matchMode: 'any' },
];

describe('url manager storage', () => {
  beforeEach(() => {
    vi.stubGlobal('crypto', { randomUUID: vi.fn(() => 'generated-id') });
  });

  afterEach(() => {
    vi.unstubAllGlobals();
  });

  it('shouldNormalizeDeduplicateAndSortTagsWhenParsingInput', () => {
    expect(parseTagsInput(' Tools,work, tools, ,PERSONAL ')).toEqual(['personal', 'tools', 'work']);
  });

  it('shouldCollectNormalizedUniqueTagsWhenUrlsContainDuplicates', () => {
    expect(
      getAllKnownTags([
        ...managedUrls,
        { id: 'url-2', label: 'Alpha', url: 'https://alpha.example', tags: [' Work ', 'personal'] },
      ]),
    ).toEqual(['personal', 'tools', 'work']);
  });

  it('shouldNormalizeAndSortUrlsWhenAddingAndUpdating', () => {
    const added = addManagedUrl(managedUrls, {
      label: ' Alpha ',
      url: ' https://alpha.example ',
      tags: [' Work ', 'work'],
      description: ' Useful ',
    });

    expect(added).toEqual([
      {
        id: 'generated-id',
        label: 'Alpha',
        url: 'https://alpha.example',
        tags: ['work'],
        description: 'Useful',
      },
      managedUrls[0],
    ]);

    expect(
      updateManagedUrl(added, 'url-1', {
        label: ' Beta ',
        url: ' https://beta.example ',
        tags: [],
        description: ' ',
      }),
    ).toEqual([
      added[0],
      { id: 'url-1', label: 'Beta', url: 'https://beta.example', tags: [], description: undefined },
    ]);
  });

  it('shouldPreserveUrlsWhenUpdatingUnknownIdAndDeleteOnlyMatchingUrl', () => {
    expect(updateManagedUrl(managedUrls, 'missing', managedUrls[0])).toEqual(managedUrls);
    expect(deleteManagedUrl(managedUrls, 'missing')).toEqual(managedUrls);
    expect(deleteManagedUrl(managedUrls, 'url-1')).toEqual([]);
  });

  it('shouldNormalizeSortUpdateAndDeleteCards', () => {
    const added = addUrlCard(urlCards, {
      title: ' Personal ',
      tags: [' Home ', 'home'],
      matchMode: 'all',
    });

    expect(added[0]).toEqual({ id: 'generated-id', title: 'Personal', tags: ['home'], matchMode: 'all' });
    expect(
      updateUrlCard(added, 'card-1', { title: ' Archive ', tags: [' Old '], matchMode: 'any' })[0],
    ).toEqual({ id: 'card-1', title: 'Archive', tags: ['old'], matchMode: 'any' });
    expect(updateUrlCard(urlCards, 'missing', urlCards[0])).toEqual(urlCards);
    expect(deleteUrlCard(added, 'generated-id')).toEqual(urlCards);
  });

  it('shouldReturnEmptyStateWhenImportedValueIsNull', () => {
    expect(normalizeImportedState(null)).toEqual({ urls: [], cards: [] });
  });

  it('shouldNormalizeImportedStateAndDiscardIncompleteEntries', () => {
    expect(
      normalizeImportedState({
        urls: [
          null,
          { id: 'kept-url', label: ' Zulu ', url: ' https://zulu.example ', tags: [' Work '] },
          { label: 'Alpha', url: 'https://alpha.example', tags: [' Personal '], description: ' Info ' },
          { label: '', url: 'https://invalid.example' },
        ],
        cards: [
          null,
          { id: 'kept-card', title: ' Work ', tags: [' Tools '], matchMode: 'all' },
          { title: 'Default mode', tags: [' Work '], matchMode: 'invalid' },
          { title: 'No tags', tags: [] },
        ],
      }),
    ).toEqual({
      urls: [
        {
          id: 'generated-id',
          label: 'Alpha',
          url: 'https://alpha.example',
          tags: ['personal'],
          description: 'Info',
        },
        {
          id: 'kept-url',
          label: 'Zulu',
          url: 'https://zulu.example',
          tags: ['work'],
          description: undefined,
        },
      ],
      cards: [
        { id: 'generated-id', title: 'Default mode', tags: ['work'], matchMode: 'any' },
        { id: 'kept-card', title: 'Work', tags: ['tools'], matchMode: 'all' },
      ],
    });
  });

  it('shouldCreateDatedExportFileNameWhenDateIsProvided', () => {
    expect(createExportFileName(new Date(2026, 7, 5, 9, 7))).toBe('url-manager-export-20260805-0907.json');
  });
});