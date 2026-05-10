import { footerLinkApi, type FooterLink } from './footerLinkApi';
import { gtinApi, type Gtin } from './gtinApi';
import { brandApi, type Brand } from './brandApi';
import { modelApi, type Model } from './modelApi';
import { githubRepositoryApi, type GitHubRepository } from './githubRepositoryApi';
import { menuItemApi, type MenuItem } from './menuItemApi';
import { sectionApi, type Section } from './sectionApi';
import { brickApi, type Brick, type ExternalLink } from './brickApi';

const SECTION_TAG = 'Section' as const;
const FOOTER_LINK_TAG = 'FooterLink' as const;
const GITHUB_REPOSITORY_TAG = 'GitHubRepository' as const;
const MENU_ITEM_TAG = 'MenuItem' as const;
const GTIN_TAG = 'Gtin' as const;
const BRAND_TAG = 'Brand' as const;
const MODEL_TAG = 'Model' as const;
const BRICK_TAG = 'Brick' as const;
const EXTERNAL_LINK_TAG = 'ExternalLink' as const;
const LIST_TAG_ID = 'LIST';

function collectSectionIds(sections: Section[] | undefined): string[] {
  return (sections ?? []).flatMap((section) => {
    const currentSectionIds = section.id ? [section.id] : [];

    return currentSectionIds.concat(collectSectionIds(section.children));
  });
}

function collectFooterLinkIds(items: FooterLink[] | undefined): string[] {
  return (items ?? []).flatMap((item) => (item.id ? [item.id] : []));
}

function collectGitHubRepositoryIds(items: GitHubRepository[] | undefined): string[] {
  return (items ?? []).flatMap((item) => (item.id ? [item.id] : []));
}

function collectMenuItemIds(items: MenuItem[] | undefined): string[] {
  return (items ?? []).flatMap((item) => (item.id ? [item.id] : []));
}

function collectGtinIds(items: Gtin[] | undefined): string[] {
  return (items ?? []).flatMap((item) => (item.id ? [item.id] : []));
}

function collectBrandIds(items: Brand[] | undefined): string[] {
  return (items ?? []).flatMap((item) => (item.id ? [item.id] : []));
}

function collectModelIds(items: Model[] | undefined): string[] {
  return (items ?? []).flatMap((item) => (item.id ? [item.id] : []));
}

function collectBrickIds(items: Brick[] | undefined): string[] {
  return (items ?? []).flatMap((item) => (item.id ? [item.id] : []));
}

function collectExternalLinkIds(items: ExternalLink[] | undefined): string[] {
  return (items ?? []).flatMap((item) => (item.id ? [item.id] : []));
}

sectionApi.enhanceEndpoints({
  addTagTypes: [SECTION_TAG],
  endpoints: {
    getSectionById: {
      providesTags: (_result, _error, queryArg) => [{ type: SECTION_TAG, id: queryArg.id }],
    },
    listSections: {
      providesTags: (result) => [
        { type: SECTION_TAG, id: LIST_TAG_ID },
        ...collectSectionIds(result?.items).map((id) => ({ type: SECTION_TAG, id })),
      ],
    },
    updateSection: {
      invalidatesTags: (_result, _error, queryArg) => [
        { type: SECTION_TAG, id: queryArg.id },
        { type: SECTION_TAG, id: LIST_TAG_ID },
      ],
    },
    createSection: {
      invalidatesTags: [{ type: SECTION_TAG, id: LIST_TAG_ID }],
    },
    deleteSectionById: {
      invalidatesTags: (_result, _error, queryArg) => [
        { type: SECTION_TAG, id: queryArg.id },
        { type: SECTION_TAG, id: LIST_TAG_ID },
      ],
    },
    deleteAllSections: {
      invalidatesTags: [SECTION_TAG],
    },
  },
});

footerLinkApi.enhanceEndpoints({
  addTagTypes: [FOOTER_LINK_TAG],
  endpoints: {
    getFooterLinkById: {
      providesTags: (_result, _error, queryArg) => [{ type: FOOTER_LINK_TAG, id: queryArg.id }],
    },
    listFooterLinks: {
      providesTags: (result) => [
        { type: FOOTER_LINK_TAG, id: LIST_TAG_ID },
        ...collectFooterLinkIds(result?.items).map((id) => ({ type: FOOTER_LINK_TAG, id })),
      ],
    },
    updateFooterLink: {
      invalidatesTags: (_result, _error, queryArg) => [
        { type: FOOTER_LINK_TAG, id: queryArg.id },
        { type: FOOTER_LINK_TAG, id: LIST_TAG_ID },
      ],
    },
    createFooterLink: {
      invalidatesTags: [{ type: FOOTER_LINK_TAG, id: LIST_TAG_ID }],
    },
    deleteFooterLinkById: {
      invalidatesTags: (_result, _error, queryArg) => [
        { type: FOOTER_LINK_TAG, id: queryArg.id },
        { type: FOOTER_LINK_TAG, id: LIST_TAG_ID },
      ],
    },
    deleteAllFooterLinks: {
      invalidatesTags: [FOOTER_LINK_TAG],
    },
  },
});

githubRepositoryApi.enhanceEndpoints({
  addTagTypes: [GITHUB_REPOSITORY_TAG],
  endpoints: {
    getGitHubRepositoryById: {
      providesTags: (_result, _error, queryArg) => [{ type: GITHUB_REPOSITORY_TAG, id: queryArg.id }],
    },
    listGitHubRepositories: {
      providesTags: (result) => [
        { type: GITHUB_REPOSITORY_TAG, id: LIST_TAG_ID },
        ...collectGitHubRepositoryIds(result?.items).map((id) => ({ type: GITHUB_REPOSITORY_TAG, id })),
      ],
    },
    updateGitHubRepository: {
      invalidatesTags: (_result, _error, queryArg) => [
        { type: GITHUB_REPOSITORY_TAG, id: queryArg.id },
        { type: GITHUB_REPOSITORY_TAG, id: LIST_TAG_ID },
      ],
    },
    createGitHubRepository: {
      invalidatesTags: [{ type: GITHUB_REPOSITORY_TAG, id: LIST_TAG_ID }],
    },
    deleteGitHubRepositoryById: {
      invalidatesTags: (_result, _error, queryArg) => [
        { type: GITHUB_REPOSITORY_TAG, id: queryArg.id },
        { type: GITHUB_REPOSITORY_TAG, id: LIST_TAG_ID },
      ],
    },
    deleteAllGitHubRepositories: {
      invalidatesTags: [GITHUB_REPOSITORY_TAG],
    },
  },
});

menuItemApi.enhanceEndpoints({
  addTagTypes: [MENU_ITEM_TAG],
  endpoints: {
    getMenuItemById: {
      providesTags: (_result, _error, queryArg) => [{ type: MENU_ITEM_TAG, id: queryArg.id }],
    },
    listMenuItems: {
      providesTags: (result) => [
        { type: MENU_ITEM_TAG, id: LIST_TAG_ID },
        ...collectMenuItemIds(result?.items).map((id) => ({ type: MENU_ITEM_TAG, id })),
      ],
    },
    updateMenuItem: {
      invalidatesTags: (_result, _error, queryArg) => [
        { type: MENU_ITEM_TAG, id: queryArg.id },
        { type: MENU_ITEM_TAG, id: LIST_TAG_ID },
      ],
    },
    createMenuItem: {
      invalidatesTags: [{ type: MENU_ITEM_TAG, id: LIST_TAG_ID }],
    },
    deleteMenuItemById: {
      invalidatesTags: (_result, _error, queryArg) => [
        { type: MENU_ITEM_TAG, id: queryArg.id },
        { type: MENU_ITEM_TAG, id: LIST_TAG_ID },
      ],
    },
    deleteAllMenuItems: {
      invalidatesTags: [MENU_ITEM_TAG],
    },
  },
});

gtinApi.enhanceEndpoints({
  addTagTypes: [GTIN_TAG],
  endpoints: {
    getGtinById: {
      providesTags: (_result, _error, queryArg) => [{ type: GTIN_TAG, id: queryArg.id }],
    },
    listGtins: {
      providesTags: (result) => [
        { type: GTIN_TAG, id: LIST_TAG_ID },
        ...collectGtinIds(result?.items).map((id) => ({ type: GTIN_TAG, id })),
      ],
    },
    updateGtin: {
      invalidatesTags: (_result, _error, queryArg) => [
        { type: GTIN_TAG, id: queryArg.id },
        { type: GTIN_TAG, id: LIST_TAG_ID },
      ],
    },
    createGtin: {
      invalidatesTags: [{ type: GTIN_TAG, id: LIST_TAG_ID }],
    },
    deleteGtinById: {
      invalidatesTags: (_result, _error, queryArg) => [
        { type: GTIN_TAG, id: queryArg.id },
        { type: GTIN_TAG, id: LIST_TAG_ID },
      ],
    },
    deleteAllGtins: {
      invalidatesTags: [GTIN_TAG],
    },
    importGtins: {
      invalidatesTags: [GTIN_TAG],
    },
  },
});

brandApi.enhanceEndpoints({
  addTagTypes: [BRAND_TAG],
  endpoints: {
    getBrandById: {
      providesTags: (_result, _error, queryArg) => [{ type: BRAND_TAG, id: queryArg.id }],
    },
    listBrands: {
      providesTags: (result) => [
        { type: BRAND_TAG, id: LIST_TAG_ID },
        ...collectBrandIds(result?.items).map((id) => ({ type: BRAND_TAG, id })),
      ],
    },
    updateBrand: {
      invalidatesTags: (_result, _error, queryArg) => [
        { type: BRAND_TAG, id: queryArg.id },
        { type: BRAND_TAG, id: LIST_TAG_ID },
      ],
    },
    createBrand: {
      invalidatesTags: [{ type: BRAND_TAG, id: LIST_TAG_ID }],
    },
    deleteBrandById: {
      invalidatesTags: (_result, _error, queryArg) => [
        { type: BRAND_TAG, id: queryArg.id },
        { type: BRAND_TAG, id: LIST_TAG_ID },
      ],
    },
    deleteAllBrands: {
      invalidatesTags: [BRAND_TAG],
    },
    importBrands: {
      invalidatesTags: [BRAND_TAG],
    },
  },
});

modelApi.enhanceEndpoints({
  addTagTypes: [MODEL_TAG],
  endpoints: {
    getModelById: {
      providesTags: (_result, _error, queryArg) => [{ type: MODEL_TAG, id: queryArg.id }],
    },
    listModels: {
      providesTags: (result) => [
        { type: MODEL_TAG, id: LIST_TAG_ID },
        ...collectModelIds(result?.items).map((id) => ({ type: MODEL_TAG, id })),
      ],
    },
    updateModel: {
      invalidatesTags: (_result, _error, queryArg) => [
        { type: MODEL_TAG, id: queryArg.id },
        { type: MODEL_TAG, id: LIST_TAG_ID },
      ],
    },
    createModel: {
      invalidatesTags: [{ type: MODEL_TAG, id: LIST_TAG_ID }],
    },
    deleteModelById: {
      invalidatesTags: (_result, _error, queryArg) => [
        { type: MODEL_TAG, id: queryArg.id },
        { type: MODEL_TAG, id: LIST_TAG_ID },
      ],
    },
    deleteAllModels: {
      invalidatesTags: [MODEL_TAG],
    },
    importModels: {
      invalidatesTags: [MODEL_TAG],
    },
  },
});

brickApi.enhanceEndpoints({
  addTagTypes: [BRICK_TAG, EXTERNAL_LINK_TAG],
  endpoints: {
    getBrickById: {
      providesTags: (_result, _error, queryArg) => [{ type: BRICK_TAG, id: queryArg.id }],
    },
    listBricks: {
      providesTags: (result) => [
        { type: BRICK_TAG, id: LIST_TAG_ID },
        ...collectBrickIds(result?.items).map((id) => ({ type: BRICK_TAG, id })),
      ],
    },
    updateBrick: {
      invalidatesTags: (_result, _error, queryArg) => [
        { type: BRICK_TAG, id: queryArg.id },
        { type: BRICK_TAG, id: LIST_TAG_ID },
      ],
    },
    createBrick: {
      invalidatesTags: [{ type: BRICK_TAG, id: LIST_TAG_ID }],
    },
    deleteBrickById: {
      invalidatesTags: (_result, _error, queryArg) => [
        { type: BRICK_TAG, id: queryArg.id },
        { type: BRICK_TAG, id: LIST_TAG_ID },
      ],
    },
    deleteAllBricks: {
      invalidatesTags: [BRICK_TAG],
    },
    importBricks: {
      invalidatesTags: [BRICK_TAG, EXTERNAL_LINK_TAG],
    },
    listExternalLinks: {
      providesTags: (result) => [
        { type: EXTERNAL_LINK_TAG, id: LIST_TAG_ID },
        ...collectExternalLinkIds(result?.items).map((id) => ({ type: EXTERNAL_LINK_TAG, id })),
      ],
    },
    getExternalLinkById: {
      providesTags: (_result, _error, queryArg) => [{ type: EXTERNAL_LINK_TAG, id: queryArg.id }],
    },
    createExternalLink: {
      invalidatesTags: [{ type: EXTERNAL_LINK_TAG, id: LIST_TAG_ID }],
    },
    updateExternalLink: {
      invalidatesTags: (_result, _error, queryArg) => [
        { type: EXTERNAL_LINK_TAG, id: queryArg.id },
        { type: EXTERNAL_LINK_TAG, id: LIST_TAG_ID },
      ],
    },
    deleteExternalLinkById: {
      invalidatesTags: (_result, _error, queryArg) => [
        { type: EXTERNAL_LINK_TAG, id: queryArg.id },
        { type: EXTERNAL_LINK_TAG, id: LIST_TAG_ID },
      ],
    },
  },
});