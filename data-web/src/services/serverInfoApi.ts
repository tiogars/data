import { emptySplitApi as api } from './emptyApi';

const injectedRtkApi = api.injectEndpoints({
  endpoints: (build) => ({
    getJavaVersionInfo: build.query<JavaVersionInfo, void>({
      query: () => ({ url: '/server-info/java-version' }),
    }),
    getJpaEntitiesInfo: build.query<JpaEntityClassInfoListResponse, void>({
      query: () => ({ url: '/server-info/jpa-entities' }),
    }),
  }),
  overrideExisting: false,
});

export { injectedRtkApi as serverInfoApi };

export type JavaVersionInfo = {
  javaVersion?: string;
  runtimeVersion?: string;
  vmName?: string;
  vmVendor?: string;
  osName?: string;
};

export type JpaEntityClassInfoListResponse = {
  items?: JpaEntityClassInfo[];
  count?: number;
};

export type JpaEntityClassInfo = {
  className?: string;
  simpleClassName?: string;
  entityName?: string;
  tableName?: string;
  tableSchema?: string;
  tableCatalog?: string;
  attributes?: JpaEntityAttributeInfo[];
};

export type JpaEntityAttributeInfo = {
  name?: string;
  type?: string;
  id?: boolean;
  generated?: boolean;
  generationStrategy?: string;
  column?: JpaColumnInfo;
  manyToOne?: JpaManyToOneInfo;
  joinColumn?: JpaJoinColumnInfo;
  version?: boolean;
  lob?: boolean;
  transientField?: boolean;
};

export type JpaColumnInfo = {
  name?: string;
  nullable?: boolean;
  updatable?: boolean;
  insertable?: boolean;
  unique?: boolean;
  length?: number;
  precision?: number;
  scale?: number;
  columnDefinition?: string;
};

export type JpaManyToOneInfo = {
  fetch?: string;
  optional?: boolean;
  cascade?: string[];
};

export type JpaJoinColumnInfo = {
  name?: string;
  referencedColumnName?: string;
  nullable?: boolean;
  updatable?: boolean;
  insertable?: boolean;
  unique?: boolean;
};

export const { useGetJavaVersionInfoQuery, useGetJpaEntitiesInfoQuery } = injectedRtkApi;
