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
        // brand
        "./src/services/brandApi.ts": {
            exportName: "brandApi",
            filterEndpoints: [
                "getBrand",
                "updateBrand",
                "deleteBrand",
                "listBrands",
                "createBrand",
                "deleteAllBrands",
                "exportBrands",
                "importBrands",
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
                "createBrick",
                "deleteAllBricks",
                "importBricks",
                "exportBricks",
                "getExternalLinkById",
                "updateExternalLink",
                "deleteExternalLinkById",
                "listExternalLinks",
                "createExternalLink",
                "deleteAllExternalLinks",
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
                "listGitHubRepositories",
                "createGitHubRepository",
                "deleteAllGitHubRepositories",
            ],
        },
        // JpaEntities
        "./src/services/serverInfoApi.ts": {
            filterEndpoints: [/jpaEntities/i],
        },
        // menuItem
        "./src/services/menuItemApi.ts": {
            exportName: "menuItemApi",
            filterEndpoints: [
                "getMenuItemById",
                "updateMenuItem",
                "deleteMenuItemById",
                "listMenuItems",
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
              "createModel",
              "deleteAllModels",
              "importModels",
              "exportModels",
              "printModels",
              "getModelAiText",
              "deleteModelById",
              "getModelById"
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
                "createSection",
                "deleteAllSections",
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
                "createFooterLink",
                "deleteAllFooterLinks",
            ],
        },
    },
    hooks: true,
    tag: true,
};

export default config;
