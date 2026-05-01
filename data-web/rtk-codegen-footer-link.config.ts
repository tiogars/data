import type { ConfigFile } from '@rtk-query/codegen-openapi';

const config: ConfigFile = {
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

export default config;