/** @type {import('@rtk-query/codegen-openapi').ConfigFile} */
const config = {
  schemaFile: 'openapi-section.json',
  apiFile: './src/services/emptyApi.ts',
  apiImport: 'emptySplitApi',
  outputFile: './src/services/footerLinkApi.ts',
  exportName: 'footerLinkApi',
  hooks: true,
  filterEndpoints: [
    'getFooterLinkById',
    'updateFooterLink',
    'deleteFooterLinkById',
    'listFooterLinks',
    'createFooterLink',
    'deleteAllFooterLinks',
  ],
};

module.exports = config;