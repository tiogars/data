import { footerLinkApi } from './footerLinkApi';
import { gtinApi } from './gtinApi';
import { brandApi } from './brandApi';
import { modelApi } from './modelApi';
import { continentApi } from './continentApi';
import { appellationApi } from './appellationApi';
import { couleurApi } from './couleurApi';
import { cepageApi } from './cepageApi';
import { circonstanceApi } from './circonstanceApi';
import { contenantApi } from './contenantApi';
import { typeVinApi } from './typeVinApi';
import { maisonApi } from './maisonApi';
import { vinNomApi } from './vinNomApi';
import { vinTagApi } from './vinTagApi';
import { vinApi } from './vinApi';
import { githubRepositoryApi } from './githubRepositoryApi';
import { menuItemApi } from './menuItemApi';
import { sectionApi, type Section } from './sectionApi';
import { brickApi } from './brickApi';
import { collectIds, createCrudCacheConfig, createEntityTags, LIST_TAG_ID } from './crudCache';

const SECTION_TAG = 'Section' as const;
const FOOTER_LINK_TAG = 'FooterLink' as const;
const GITHUB_REPOSITORY_TAG = 'GitHubRepository' as const;
const MENU_ITEM_TAG = 'MenuItem' as const;
const GTIN_TAG = 'Gtin' as const;
const BRAND_TAG = 'Brand' as const;
const MODEL_TAG = 'Model' as const;
const CONTINENT_TAG = 'Continent' as const;
const APPELLATION_TAG = 'Appellation' as const;
const COULEUR_TAG = 'Couleur' as const;
const CEPAGE_TAG = 'Cepage' as const;
const CIRCONSTANCE_TAG = 'Circonstance' as const;
const CONTENANT_TAG = 'Contenant' as const;
const TYPE_VIN_TAG = 'TypeVin' as const;
const MAISON_TAG = 'Maison' as const;
const VIN_NOM_TAG = 'VinNom' as const;
const VIN_TAG_ENTITY = 'VinTag' as const;
const VIN_TAG = 'Vin' as const;
const BRICK_TAG = 'Brick' as const;
const EXTERNAL_LINK_TAG = 'ExternalLink' as const;

function collectSectionIds(sections: Section[] | undefined): string[] {
  return (sections ?? []).flatMap((section) => {
    const currentSectionIds = section.id ? [section.id] : [];

    return currentSectionIds.concat(collectSectionIds(section.children));
  });
}

const brandCache = createCrudCacheConfig(BRAND_TAG);

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
        ...collectIds(result?.items).map((id) => ({ type: FOOTER_LINK_TAG, id })),
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
    searchGitHubRepositories: {
      providesTags: (result) => [
        { type: GITHUB_REPOSITORY_TAG, id: LIST_TAG_ID },
        ...collectIds(result?.items).map((id) => ({ type: GITHUB_REPOSITORY_TAG, id })),
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
        ...collectIds(result?.items).map((id) => ({ type: MENU_ITEM_TAG, id })),
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
    getGtin: {
      providesTags: (_result, _error, queryArg) => [{ type: GTIN_TAG, id: queryArg.id }],
    },
    listGtins: {
      providesTags: (result) => [
        { type: GTIN_TAG, id: LIST_TAG_ID },
        ...collectIds(result?.items).map((id) => ({ type: GTIN_TAG, id })),
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
    deleteGtin: {
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
    getBrand: brandCache.detail,
    listBrands: brandCache.list,
    searchBrands: brandCache.list,
    updateBrand: brandCache.update,
    createBrand: brandCache.create,
    deleteBrand: brandCache.remove,
    deleteAllBrands: brandCache.removeAll,
    importBrands: brandCache.importAll,
  },
});

modelApi.enhanceEndpoints({
  addTagTypes: [MODEL_TAG],
  endpoints: {
    getModel: {
      providesTags: (_result, _error, queryArg) => [{ type: MODEL_TAG, id: queryArg.id }],
    },
    listModels: {
      providesTags: (result) => [
        { type: MODEL_TAG, id: LIST_TAG_ID },
        ...collectIds(result?.items).map((id) => ({ type: MODEL_TAG, id })),
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
    deleteModel: {
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

continentApi.enhanceEndpoints({
  addTagTypes: [CONTINENT_TAG],
  endpoints: {
    getContinent: {
      providesTags: (_result, _error, queryArg) => [{ type: CONTINENT_TAG, id: queryArg.id }],
    },
    listContinents: {
      providesTags: (result) => [
        { type: CONTINENT_TAG, id: LIST_TAG_ID },
        ...collectIds(result?.items).map((id) => ({ type: CONTINENT_TAG, id })),
      ],
    },
    updateContinent: {
      invalidatesTags: (_result, _error, queryArg) => [
        { type: CONTINENT_TAG, id: queryArg.id },
        { type: CONTINENT_TAG, id: LIST_TAG_ID },
      ],
    },
    createContinent: {
      invalidatesTags: [{ type: CONTINENT_TAG, id: LIST_TAG_ID }],
    },
    deleteContinent: {
      invalidatesTags: (_result, _error, queryArg) => [
        { type: CONTINENT_TAG, id: queryArg.id },
        { type: CONTINENT_TAG, id: LIST_TAG_ID },
      ],
    },
  },
});

appellationApi.enhanceEndpoints({ addTagTypes: [APPELLATION_TAG], endpoints: { getAppellation: { providesTags: (_r,_e,q) => [{ type: APPELLATION_TAG, id: q.id }] }, listAppellations: { providesTags: (r) => createEntityTags(APPELLATION_TAG, collectIds(r?.items)) }, searchAppellations: { providesTags: (r) => createEntityTags(APPELLATION_TAG, collectIds(r?.items)) }, updateAppellation: { invalidatesTags: (_r,_e,q) => [{ type: APPELLATION_TAG, id: q.id }, { type: APPELLATION_TAG, id: LIST_TAG_ID }] }, createAppellation: { invalidatesTags: [{ type: APPELLATION_TAG, id: LIST_TAG_ID }] }, deleteAppellation: { invalidatesTags: (_r,_e,q) => [{ type: APPELLATION_TAG, id: q.id }, { type: APPELLATION_TAG, id: LIST_TAG_ID }] }, deleteAllAppellations: { invalidatesTags: [APPELLATION_TAG] }, importAppellations: { invalidatesTags: [APPELLATION_TAG] } } });
couleurApi.enhanceEndpoints({ addTagTypes: [COULEUR_TAG], endpoints: { getCouleur: { providesTags: (_r,_e,q) => [{ type: COULEUR_TAG, id: q.id }] }, listCouleurs: { providesTags: (r) => createEntityTags(COULEUR_TAG, collectIds(r?.items)) }, searchCouleurs: { providesTags: (r) => createEntityTags(COULEUR_TAG, collectIds(r?.items)) }, updateCouleur: { invalidatesTags: (_r,_e,q) => [{ type: COULEUR_TAG, id: q.id }, { type: COULEUR_TAG, id: LIST_TAG_ID }] }, createCouleur: { invalidatesTags: [{ type: COULEUR_TAG, id: LIST_TAG_ID }] }, deleteCouleur: { invalidatesTags: (_r,_e,q) => [{ type: COULEUR_TAG, id: q.id }, { type: COULEUR_TAG, id: LIST_TAG_ID }] }, deleteAllCouleurs: { invalidatesTags: [COULEUR_TAG] }, importCouleurs: { invalidatesTags: [COULEUR_TAG] } } });
cepageApi.enhanceEndpoints({ addTagTypes: [CEPAGE_TAG], endpoints: { getCepage: { providesTags: (_r,_e,q) => [{ type: CEPAGE_TAG, id: q.id }] }, listCepages: { providesTags: (r) => createEntityTags(CEPAGE_TAG, collectIds(r?.items)) }, searchCepages: { providesTags: (r) => createEntityTags(CEPAGE_TAG, collectIds(r?.items)) }, updateCepage: { invalidatesTags: (_r,_e,q) => [{ type: CEPAGE_TAG, id: q.id }, { type: CEPAGE_TAG, id: LIST_TAG_ID }] }, createCepage: { invalidatesTags: [{ type: CEPAGE_TAG, id: LIST_TAG_ID }] }, deleteCepage: { invalidatesTags: (_r,_e,q) => [{ type: CEPAGE_TAG, id: q.id }, { type: CEPAGE_TAG, id: LIST_TAG_ID }] }, deleteAllCepages: { invalidatesTags: [CEPAGE_TAG] }, importCepages: { invalidatesTags: [CEPAGE_TAG] } } });
circonstanceApi.enhanceEndpoints({ addTagTypes: [CIRCONSTANCE_TAG], endpoints: { getCirconstance: { providesTags: (_r,_e,q) => [{ type: CIRCONSTANCE_TAG, id: q.id }] }, listCirconstances: { providesTags: (r) => createEntityTags(CIRCONSTANCE_TAG, collectIds(r?.items)) }, searchCirconstances: { providesTags: (r) => createEntityTags(CIRCONSTANCE_TAG, collectIds(r?.items)) }, updateCirconstance: { invalidatesTags: (_r,_e,q) => [{ type: CIRCONSTANCE_TAG, id: q.id }, { type: CIRCONSTANCE_TAG, id: LIST_TAG_ID }] }, createCirconstance: { invalidatesTags: [{ type: CIRCONSTANCE_TAG, id: LIST_TAG_ID }] }, deleteCirconstance: { invalidatesTags: (_r,_e,q) => [{ type: CIRCONSTANCE_TAG, id: q.id }, { type: CIRCONSTANCE_TAG, id: LIST_TAG_ID }] }, deleteAllCirconstances: { invalidatesTags: [CIRCONSTANCE_TAG] }, importCirconstances: { invalidatesTags: [CIRCONSTANCE_TAG] } } });
contenantApi.enhanceEndpoints({ addTagTypes: [CONTENANT_TAG], endpoints: { getContenant: { providesTags: (_r,_e,q) => [{ type: CONTENANT_TAG, id: q.id }] }, listContenants: { providesTags: (r) => createEntityTags(CONTENANT_TAG, collectIds(r?.items)) }, searchContenants: { providesTags: (r) => createEntityTags(CONTENANT_TAG, collectIds(r?.items)) }, updateContenant: { invalidatesTags: (_r,_e,q) => [{ type: CONTENANT_TAG, id: q.id }, { type: CONTENANT_TAG, id: LIST_TAG_ID }] }, createContenant: { invalidatesTags: [{ type: CONTENANT_TAG, id: LIST_TAG_ID }] }, deleteContenant: { invalidatesTags: (_r,_e,q) => [{ type: CONTENANT_TAG, id: q.id }, { type: CONTENANT_TAG, id: LIST_TAG_ID }] }, deleteAllContenants: { invalidatesTags: [CONTENANT_TAG] }, importContenants: { invalidatesTags: [CONTENANT_TAG] } } });
typeVinApi.enhanceEndpoints({ addTagTypes: [TYPE_VIN_TAG], endpoints: { getTypeVin: { providesTags: (_r,_e,q) => [{ type: TYPE_VIN_TAG, id: q.id }] }, listTypeVins: { providesTags: (r) => createEntityTags(TYPE_VIN_TAG, collectIds(r?.items)) }, searchTypeVins: { providesTags: (r) => createEntityTags(TYPE_VIN_TAG, collectIds(r?.items)) }, updateTypeVin: { invalidatesTags: (_r,_e,q) => [{ type: TYPE_VIN_TAG, id: q.id }, { type: TYPE_VIN_TAG, id: LIST_TAG_ID }] }, createTypeVin: { invalidatesTags: [{ type: TYPE_VIN_TAG, id: LIST_TAG_ID }] }, deleteTypeVin: { invalidatesTags: (_r,_e,q) => [{ type: TYPE_VIN_TAG, id: q.id }, { type: TYPE_VIN_TAG, id: LIST_TAG_ID }] }, deleteAllTypeVins: { invalidatesTags: [TYPE_VIN_TAG] }, importTypeVins: { invalidatesTags: [TYPE_VIN_TAG] } } });
maisonApi.enhanceEndpoints({ addTagTypes: [MAISON_TAG], endpoints: { getMaison: { providesTags: (_r,_e,q) => [{ type: MAISON_TAG, id: q.id }] }, listMaisons: { providesTags: (r) => createEntityTags(MAISON_TAG, collectIds(r?.items)) }, searchMaisons: { providesTags: (r) => createEntityTags(MAISON_TAG, collectIds(r?.items)) }, updateMaison: { invalidatesTags: (_r,_e,q) => [{ type: MAISON_TAG, id: q.id }, { type: MAISON_TAG, id: LIST_TAG_ID }] }, createMaison: { invalidatesTags: [{ type: MAISON_TAG, id: LIST_TAG_ID }] }, deleteMaison: { invalidatesTags: (_r,_e,q) => [{ type: MAISON_TAG, id: q.id }, { type: MAISON_TAG, id: LIST_TAG_ID }] }, deleteAllMaisons: { invalidatesTags: [MAISON_TAG] }, importMaisons: { invalidatesTags: [MAISON_TAG] } } });
vinNomApi.enhanceEndpoints({ addTagTypes: [VIN_NOM_TAG], endpoints: { getVinNom: { providesTags: (_r,_e,q) => [{ type: VIN_NOM_TAG, id: q.id }] }, listVinNoms: { providesTags: (r) => createEntityTags(VIN_NOM_TAG, collectIds(r?.items)) }, searchVinNoms: { providesTags: (r) => createEntityTags(VIN_NOM_TAG, collectIds(r?.items)) }, updateVinNom: { invalidatesTags: (_r,_e,q) => [{ type: VIN_NOM_TAG, id: q.id }, { type: VIN_NOM_TAG, id: LIST_TAG_ID }] }, createVinNom: { invalidatesTags: [{ type: VIN_NOM_TAG, id: LIST_TAG_ID }] }, deleteVinNom: { invalidatesTags: (_r,_e,q) => [{ type: VIN_NOM_TAG, id: q.id }, { type: VIN_NOM_TAG, id: LIST_TAG_ID }] }, deleteAllVinNoms: { invalidatesTags: [VIN_NOM_TAG] }, importVinNoms: { invalidatesTags: [VIN_NOM_TAG] } } });
vinTagApi.enhanceEndpoints({ addTagTypes: [VIN_TAG_ENTITY], endpoints: { getVinTag: { providesTags: (_r,_e,q) => [{ type: VIN_TAG_ENTITY, id: q.id }] }, listVinTags: { providesTags: (r) => createEntityTags(VIN_TAG_ENTITY, collectIds(r?.items)) }, searchVinTags: { providesTags: (r) => createEntityTags(VIN_TAG_ENTITY, collectIds(r?.items)) }, updateVinTag: { invalidatesTags: (_r,_e,q) => [{ type: VIN_TAG_ENTITY, id: q.id }, { type: VIN_TAG_ENTITY, id: LIST_TAG_ID }] }, createVinTag: { invalidatesTags: [{ type: VIN_TAG_ENTITY, id: LIST_TAG_ID }] }, deleteVinTag: { invalidatesTags: (_r,_e,q) => [{ type: VIN_TAG_ENTITY, id: q.id }, { type: VIN_TAG_ENTITY, id: LIST_TAG_ID }] }, deleteAllVinTags: { invalidatesTags: [VIN_TAG_ENTITY] }, importVinTags: { invalidatesTags: [VIN_TAG_ENTITY] } } });
vinApi.enhanceEndpoints({ addTagTypes: [VIN_TAG], endpoints: { getVin: { providesTags: (_r,_e,q) => [{ type: VIN_TAG, id: q.id }] }, listVins: { providesTags: (r) => createEntityTags(VIN_TAG, collectIds(r?.items)) }, searchVins: { providesTags: (r) => createEntityTags(VIN_TAG, collectIds(r?.items)) }, updateVin: { invalidatesTags: (_r,_e,q) => [{ type: VIN_TAG, id: q.id }, { type: VIN_TAG, id: LIST_TAG_ID }] }, createVin: { invalidatesTags: [{ type: VIN_TAG, id: LIST_TAG_ID }] }, deleteVin: { invalidatesTags: (_r,_e,q) => [{ type: VIN_TAG, id: q.id }, { type: VIN_TAG, id: LIST_TAG_ID }] }, deleteAllVins: { invalidatesTags: [VIN_TAG] }, importVins: { invalidatesTags: [VIN_TAG] } } });

brickApi.enhanceEndpoints({
  addTagTypes: [BRICK_TAG, EXTERNAL_LINK_TAG],
  endpoints: {
    getBrickById: {
      providesTags: (_result, _error, queryArg) => [{ type: BRICK_TAG, id: queryArg.id }],
    },
    listBricks: {
      providesTags: (result) => [
        { type: BRICK_TAG, id: LIST_TAG_ID },
        ...collectIds(result?.items).map((id) => ({ type: BRICK_TAG, id })),
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
        ...collectIds(result?.items).map((id) => ({ type: EXTERNAL_LINK_TAG, id })),
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