import type { ConfigFile } from '@rtk-query/codegen-openapi';

const config: ConfigFile = {
  schemaFile: 'openapi-section.json',
  apiFile: './src/emptyApi.ts',
  apiImport: 'emptySplitApi',
  outputFile: './src/services/sectionApi.ts',
  exportName: 'sectionApi',
  hooks: true,
};

export default config;
