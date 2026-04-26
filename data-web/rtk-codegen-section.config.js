/** @type {import('@rtk-query/codegen-openapi').ConfigFile} */
const config = {
  schemaFile: 'openapi-section.json',
  apiFile: './src/emptyApi.ts',
  apiImport: 'emptySplitApi',
  outputFile: './src/services/sectionApi.ts',
  exportName: 'sectionApi',
  hooks: true,
  tag: 'section',
};

module.exports = config;
