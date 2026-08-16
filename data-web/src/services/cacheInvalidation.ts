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
import { createCrudCacheConfig } from './crudCache';

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

/** Les sections sont hierarchiques : les enfants doivent aussi porter un tag. */
function collectSectionIds(sections: Section[] | undefined): string[] {
  return (sections ?? []).flatMap((section) => {
    const currentSectionIds = section.id ? [section.id] : [];

    return currentSectionIds.concat(collectSectionIds(section.children));
  });
}

const sectionCache = createCrudCacheConfig(SECTION_TAG, { collect: collectSectionIds });
const footerLinkCache = createCrudCacheConfig(FOOTER_LINK_TAG);
const githubRepositoryCache = createCrudCacheConfig(GITHUB_REPOSITORY_TAG);
const menuItemCache = createCrudCacheConfig(MENU_ITEM_TAG);
const gtinCache = createCrudCacheConfig(GTIN_TAG);
const brandCache = createCrudCacheConfig(BRAND_TAG);
const modelCache = createCrudCacheConfig(MODEL_TAG);
const continentCache = createCrudCacheConfig(CONTINENT_TAG);
const appellationCache = createCrudCacheConfig(APPELLATION_TAG);
const couleurCache = createCrudCacheConfig(COULEUR_TAG);
const cepageCache = createCrudCacheConfig(CEPAGE_TAG);
const circonstanceCache = createCrudCacheConfig(CIRCONSTANCE_TAG);
const contenantCache = createCrudCacheConfig(CONTENANT_TAG);
const typeVinCache = createCrudCacheConfig(TYPE_VIN_TAG);
const maisonCache = createCrudCacheConfig(MAISON_TAG);
const vinNomCache = createCrudCacheConfig(VIN_NOM_TAG);
const vinTagCache = createCrudCacheConfig(VIN_TAG_ENTITY);
const vinCache = createCrudCacheConfig(VIN_TAG);
const brickCache = createCrudCacheConfig(BRICK_TAG);
const externalLinkCache = createCrudCacheConfig(EXTERNAL_LINK_TAG);

sectionApi.enhanceEndpoints({
  addTagTypes: [SECTION_TAG],
  endpoints: {
    getSectionById: sectionCache.detail,
    listSections: sectionCache.list,
    searchSections: sectionCache.list,
    updateSection: sectionCache.update,
    createSection: sectionCache.create,
    deleteSectionById: sectionCache.remove,
    deleteAllSections: sectionCache.removeAll,
  },
});

footerLinkApi.enhanceEndpoints({
  addTagTypes: [FOOTER_LINK_TAG],
  endpoints: {
    getFooterLinkById: footerLinkCache.detail,
    listFooterLinks: footerLinkCache.list,
    searchFooterLinks: footerLinkCache.list,
    updateFooterLink: footerLinkCache.update,
    createFooterLink: footerLinkCache.create,
    deleteFooterLinkById: footerLinkCache.remove,
    deleteAllFooterLinks: footerLinkCache.removeAll,
  },
});

githubRepositoryApi.enhanceEndpoints({
  addTagTypes: [GITHUB_REPOSITORY_TAG],
  endpoints: {
    getGitHubRepositoryById: githubRepositoryCache.detail,
    searchGitHubRepositories: githubRepositoryCache.list,
    updateGitHubRepository: githubRepositoryCache.update,
    createGitHubRepository: githubRepositoryCache.create,
    deleteGitHubRepositoryById: githubRepositoryCache.remove,
    deleteAllGitHubRepositories: githubRepositoryCache.removeAll,
  },
});

menuItemApi.enhanceEndpoints({
  addTagTypes: [MENU_ITEM_TAG],
  endpoints: {
    getMenuItemById: menuItemCache.detail,
    listMenuItems: menuItemCache.list,
    searchMenuItems: menuItemCache.list,
    updateMenuItem: menuItemCache.update,
    createMenuItem: menuItemCache.create,
    deleteMenuItemById: menuItemCache.remove,
    deleteAllMenuItems: menuItemCache.removeAll,
  },
});

gtinApi.enhanceEndpoints({
  addTagTypes: [GTIN_TAG],
  endpoints: {
    getGtin: gtinCache.detail,
    listGtins: gtinCache.list,
    searchGtins: gtinCache.list,
    updateGtin: gtinCache.update,
    createGtin: gtinCache.create,
    deleteGtin: gtinCache.remove,
    deleteAllGtins: gtinCache.removeAll,
    importGtins: gtinCache.importAll,
    importGtinsCsv: gtinCache.importAll,
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
    getModel: modelCache.detail,
    listModels: modelCache.list,
    searchModels: modelCache.list,
    updateModel: modelCache.update,
    createModel: modelCache.create,
    deleteModel: modelCache.remove,
    deleteAllModels: modelCache.removeAll,
    importModels: modelCache.importAll,
  },
});

continentApi.enhanceEndpoints({
  addTagTypes: [CONTINENT_TAG],
  endpoints: {
    getContinent: continentCache.detail,
    listContinents: continentCache.list,
    searchContinents: continentCache.list,
    updateContinent: continentCache.update,
    createContinent: continentCache.create,
    deleteContinent: continentCache.remove,
  },
});

appellationApi.enhanceEndpoints({
  addTagTypes: [APPELLATION_TAG],
  endpoints: {
    getAppellation: appellationCache.detail,
    listAppellations: appellationCache.list,
    searchAppellations: appellationCache.list,
    updateAppellation: appellationCache.update,
    createAppellation: appellationCache.create,
    deleteAppellation: appellationCache.remove,
    deleteAllAppellations: appellationCache.removeAll,
    importAppellations: appellationCache.importAll,
    importAppellationsCsv: appellationCache.importAll,
  },
});

couleurApi.enhanceEndpoints({
  addTagTypes: [COULEUR_TAG],
  endpoints: {
    getCouleur: couleurCache.detail,
    listCouleurs: couleurCache.list,
    searchCouleurs: couleurCache.list,
    updateCouleur: couleurCache.update,
    createCouleur: couleurCache.create,
    deleteCouleur: couleurCache.remove,
    deleteAllCouleurs: couleurCache.removeAll,
    importCouleurs: couleurCache.importAll,
    importCouleursCsv: couleurCache.importAll,
  },
});

cepageApi.enhanceEndpoints({
  addTagTypes: [CEPAGE_TAG],
  endpoints: {
    getCepage: cepageCache.detail,
    listCepages: cepageCache.list,
    searchCepages: cepageCache.list,
    updateCepage: cepageCache.update,
    createCepage: cepageCache.create,
    deleteCepage: cepageCache.remove,
    deleteAllCepages: cepageCache.removeAll,
    importCepages: cepageCache.importAll,
    importCepagesCsv: cepageCache.importAll,
  },
});

circonstanceApi.enhanceEndpoints({
  addTagTypes: [CIRCONSTANCE_TAG],
  endpoints: {
    getCirconstance: circonstanceCache.detail,
    listCirconstances: circonstanceCache.list,
    searchCirconstances: circonstanceCache.list,
    updateCirconstance: circonstanceCache.update,
    createCirconstance: circonstanceCache.create,
    deleteCirconstance: circonstanceCache.remove,
    deleteAllCirconstances: circonstanceCache.removeAll,
    importCirconstances: circonstanceCache.importAll,
    importCirconstancesCsv: circonstanceCache.importAll,
  },
});

contenantApi.enhanceEndpoints({
  addTagTypes: [CONTENANT_TAG],
  endpoints: {
    getContenant: contenantCache.detail,
    listContenants: contenantCache.list,
    searchContenants: contenantCache.list,
    updateContenant: contenantCache.update,
    createContenant: contenantCache.create,
    deleteContenant: contenantCache.remove,
    deleteAllContenants: contenantCache.removeAll,
    importContenants: contenantCache.importAll,
    importContenantsCsv: contenantCache.importAll,
  },
});

typeVinApi.enhanceEndpoints({
  addTagTypes: [TYPE_VIN_TAG],
  endpoints: {
    getTypeVin: typeVinCache.detail,
    listTypeVins: typeVinCache.list,
    searchTypeVins: typeVinCache.list,
    updateTypeVin: typeVinCache.update,
    createTypeVin: typeVinCache.create,
    deleteTypeVin: typeVinCache.remove,
    deleteAllTypeVins: typeVinCache.removeAll,
    importTypeVins: typeVinCache.importAll,
    importTypeVinsCsv: typeVinCache.importAll,
  },
});

maisonApi.enhanceEndpoints({
  addTagTypes: [MAISON_TAG],
  endpoints: {
    getMaison: maisonCache.detail,
    listMaisons: maisonCache.list,
    searchMaisons: maisonCache.list,
    updateMaison: maisonCache.update,
    createMaison: maisonCache.create,
    deleteMaison: maisonCache.remove,
    deleteAllMaisons: maisonCache.removeAll,
    importMaisons: maisonCache.importAll,
    importMaisonsCsv: maisonCache.importAll,
  },
});

vinNomApi.enhanceEndpoints({
  addTagTypes: [VIN_NOM_TAG],
  endpoints: {
    getVinNom: vinNomCache.detail,
    listVinNoms: vinNomCache.list,
    searchVinNoms: vinNomCache.list,
    updateVinNom: vinNomCache.update,
    createVinNom: vinNomCache.create,
    deleteVinNom: vinNomCache.remove,
    deleteAllVinNoms: vinNomCache.removeAll,
    importVinNoms: vinNomCache.importAll,
    importVinNomsCsv: vinNomCache.importAll,
  },
});

vinTagApi.enhanceEndpoints({
  addTagTypes: [VIN_TAG_ENTITY],
  endpoints: {
    getVinTag: vinTagCache.detail,
    listVinTags: vinTagCache.list,
    searchVinTags: vinTagCache.list,
    updateVinTag: vinTagCache.update,
    createVinTag: vinTagCache.create,
    deleteVinTag: vinTagCache.remove,
    deleteAllVinTags: vinTagCache.removeAll,
    importVinTags: vinTagCache.importAll,
    importVinTagsCsv: vinTagCache.importAll,
  },
});

vinApi.enhanceEndpoints({
  addTagTypes: [VIN_TAG],
  endpoints: {
    getVin: vinCache.detail,
    listVins: vinCache.list,
    searchVins: vinCache.list,
    updateVin: vinCache.update,
    createVin: vinCache.create,
    deleteVin: vinCache.remove,
    deleteAllVins: vinCache.removeAll,
    importVins: vinCache.importAll,
  },
});

brickApi.enhanceEndpoints({
  addTagTypes: [BRICK_TAG, EXTERNAL_LINK_TAG],
  endpoints: {
    getBrickById: brickCache.detail,
    listBricks: brickCache.list,
    searchBricks: brickCache.list,
    updateBrick: brickCache.update,
    createBrick: brickCache.create,
    deleteBrickById: brickCache.remove,
    deleteAllBricks: brickCache.removeAll,
    importBricks: {
      invalidatesTags: [BRICK_TAG, EXTERNAL_LINK_TAG],
    },
    getExternalLinkById: externalLinkCache.detail,
    listExternalLinks: externalLinkCache.list,
    createExternalLink: externalLinkCache.create,
    updateExternalLink: externalLinkCache.update,
    deleteExternalLinkById: externalLinkCache.remove,
  },
});