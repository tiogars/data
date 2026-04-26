/** @type {import('@rtk-query/codegen-openapi').ConfigFile} */
module.exports = {
  schemaFile: 'openapi-section.json',
  apiFile: './src/emptyApi.ts',
  apiImport: 'emptySplitApi',
  outputFile: './src/services/sectionApi.ts',
  exportName: 'sectionApi',
  hooks: true,
  tag: 'section',
};
