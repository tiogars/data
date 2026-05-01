/** @type {import('@rtk-query/codegen-openapi').ConfigFile} */
const config = {
  schemaFile: 'openapi-section.json',
  apiFile: './src/services/emptyApi.ts',
  apiImport: 'emptySplitApi',
  outputFile: './src/services/sectionApi.ts',
  exportName: 'sectionApi',
  hooks: true,
};

module.exports = config;