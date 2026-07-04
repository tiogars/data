import type { ConfigFile } from "@rtk-query/codegen-openapi";

// see https://redux-toolkit.js.org/rtk-query/usage/code-generation#usage
// pnpm add -D @rtk-query/codegen-openapi tsx ts-node
// npx tsx node_modules/@rtk-query/codegen-openapi/lib/bin/cli.js openapi-config.ts
// npx @rtk-query/codegen-openapi openapi-config.ts
// Encountered a TypeScript configfile, but neither esbuild-runner nor ts-node are installed.
const config: ConfigFile = {
    schemaFile: "openapi-section.json",
    apiFile: "./src/services/emptyApi.ts",
    apiImport: "emptySplitApi",
    outputFiles: {
        // android
        "./src/services/androidApi.ts": {
            exportName: "androidApi",
            filterEndpoints: [
                "getAndroid",
                "updateAndroid",
                "deleteAndroid",
                "listAndroids",
                "searchAndroids",
                "createAndroid",
                "deleteAllAndroids",
                "importAndroids",
                "importAndroidsCsv",
                "exportAndroids",
                "printAndroids",
            ],
        },
        // brand
        "./src/services/brandApi.ts": {
            exportName: "brandApi",
            filterEndpoints: [
                "getBrand",
                "updateBrand",
                "deleteBrand",
                "listBrands",
                "searchBrands",
                "createBrand",
                "deleteAllBrands",
                "exportBrands",
                "importBrands",
            ],
        },
        // car
        "./src/services/carApi.ts": {
            exportName: "carApi",
            filterEndpoints: [
                "getCar",
                "updateCar",
                "deleteCar",
                "listCars",
                "searchCars",
                "createCar",
                "deleteAllCars",
                "exportCars",
                "importCars",
                "exportCarsCsv",
                "importCarsCsv",
            ],
        },
        // carMileage
        "./src/services/carMileageApi.ts": {
            exportName: "carMileageApi",
            filterEndpoints: [
                "getCarMileage",
                "updateCarMileage",
                "deleteCarMileage",
                "searchCarMileages",
                "createCarMileage",
                "chartCarMileages",
                "exportCarMileages",
                "importCarMileages",
                "exportCarMileagesCsv",
                "importCarMileagesCsv",
            ],
        },
        // brick
        "./src/services/brickApi.ts": {
            exportName: "brickApi",
            filterEndpoints: [
                "getBrickById",
                "updateBrick",
                "deleteBrickById",
                "listBricks",
                "searchBricks",
                "createBrick",
                "deleteAllBricks",
                "importBricks",
                "exportBricks",
                "getExternalLinkById",
                "updateExternalLink",
                "deleteExternalLinkById",
                "listExternalLinks",
                "createExternalLink",
            ],
        },
        // continent
        "./src/services/continentApi.ts": {
            exportName: "continentApi",
            filterEndpoints: [
                "getContinent",
                "updateContinent",
                "deleteContinent",
                "listContinents",
                "searchContinents",
                "createContinent",
            ],
        },
        // githubRepository
        "./src/services/githubRepositoryApi.ts": {
            exportName: "githubRepositoryApi",
            filterEndpoints: [
                "getGitHubRepositoryById",
                "updateGitHubRepository",
                "deleteGitHubRepositoryById",
                "searchGitHubRepositories",
                "createGitHubRepository",
                "deleteAllGitHubRepositories",
            ],
        },
        // githubRepositorySync
        "./src/services/githubRepositorySyncApi.ts": {
            exportName: "githubRepositorySyncApi",
            filterEndpoints: [
                "syncGitHubRepository",
                "syncAllGitHubRepositories",
            ],
        },
        // githubRestConfig
        "./src/services/githubRestConfigApi.ts": {
            exportName: "githubRestConfigApi",
            filterEndpoints: [
                "searchGitHubRestConfigs",
                "create",
                "getByIdentifier",
                "updateByIdentifier",
                "deleteByIdentifier",
                "listRequiredPermissions",
            ],
        },
        // gtin
        "./src/services/gtinApi.ts": {
            exportName: "gtinApi",
            filterEndpoints: [
                "getGtin",
                "updateGtin",
                "deleteGtin",
                "listGtins",
                "searchGtins",
                "createGtin",
                "deleteAllGtins",
                "exportGtins",
                "importGtins",
                "exportGtinsCsv",
                "importGtinsCsv",
            ],
        },
        // JpaEntities
        "./src/services/serverInfoApi.ts": {
            filterEndpoints: [/jpaEntities/i, /javaVersion/i],
        },
        // menuItem
        "./src/services/menuItemApi.ts": {
            exportName: "menuItemApi",
            filterEndpoints: [
                "getMenuItemById",
                "updateMenuItem",
                "deleteMenuItemById",
                "listMenuItems",
                "searchMenuItems",
                "createMenuItem",
                "deleteAllMenuItems",
            ],
        },
        // model
        "./src/services/modelApi.ts": {
            exportName: "modelApi",
            filterEndpoints: [
              "getModel",
              "updateModel",
              "deleteModel",
              "listModels",
              "searchModels",
              "createModel",
              "deleteAllModels",
              "importModels",
              "exportModels",
              "printModels",
              "getModelAiText"
            ],
        },
        // urlManager
        "./src/services/urlManagerApi.ts": {
            exportName: "urlManagerApi",
            filterEndpoints: [
                "getState",
                "updateState",
                "importState",
                "exportState",
            ],
        },
        // section
        "./src/services/sectionApi.ts": {
            exportName: "sectionApi",
            filterEndpoints: [
                "getSectionById",
                "updateSection",
                "deleteSectionById",
                "listSections",
                "searchSections",
                "createSection",
                "deleteAllSections",
            ],
        },
        // sectionDocsSettings
        "./src/services/sectionDocsSettingsApi.ts": {
            exportName: "sectionDocsSettingsApi",
            filterEndpoints: [
                "getSectionDocsSettingsState",
                "updateSectionDocsSettingsState",
            ],
        },
        // footerLink
        "./src/services/footerLinkApi.ts": {
            exportName: "footerLinkApi",
            filterEndpoints: [
                "getFooterLinkById",
                "updateFooterLink",
                "deleteFooterLinkById",
                "listFooterLinks",
                "searchFooterLinks",
                "createFooterLink",
                "deleteAllFooterLinks",
            ],
        },
        // vin
        "./src/services/vinApi.ts": {
            exportName: "vinApi",
            filterEndpoints: [
                "getVin",
                "updateVin",
                "deleteVin",
                "listVins",
                "searchVins",
                "createVin",
                "deleteAllVins",
                "exportVins",
                "importVins",
                "printVins",
            ],
        },
    },
    hooks: true,
    tag: true,
};

export default config;
