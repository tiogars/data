/** @type {import('@rtk-query/codegen-openapi').ConfigFile} */
const config = {
  schemaFile: 'openapi-section.json',
  apiFile: './src/services/emptyApi.ts',
  apiImport: 'emptySplitApi',
  hooks: true,
  outputFiles: {
    './src/services/sectionApi.ts': {
      exportName: 'sectionApi',
      filterEndpoints: [
        'getSectionById',
        'updateSection',
        'deleteSectionById',
        'listSections',
        'createSection',
        'deleteAllSections',
      ],
    },
    './src/services/footerLinkApi.ts': {
      exportName: 'footerLinkApi',
      filterEndpoints: [
        'getFooterLinkById',
        'updateFooterLink',
        'deleteFooterLinkById',
        'listFooterLinks',
        'createFooterLink',
        'deleteAllFooterLinks',
      ],
    },
  },
};

module.exports = config;