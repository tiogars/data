import { emptySplitApi as api } from "./emptyApi";
export const addTagTypes = ["server-info"] as const;
const injectedRtkApi = api
    .enhanceEndpoints({
        addTagTypes,
    })
    .injectEndpoints({
        endpoints: (build) => ({
            listJpaEntities: build.query<
                ListJpaEntitiesApiResponse,
                ListJpaEntitiesApiArg
            >({
                query: () => ({ url: `/server-info/jpa-entities` }),
                providesTags: ["server-info"],
            }),
            getJavaVersion: build.query<
                GetJavaVersionApiResponse,
                GetJavaVersionApiArg
            >({
                query: () => ({ url: `/server-info/java-version` }),
                providesTags: ["server-info"],
            }),
        }),
        overrideExisting: false,
    });
export { injectedRtkApi as enhancedApi };
export type ListJpaEntitiesApiResponse =
    /** status 200 OK */ JpaEntityClassInfoListResponse;
export type ListJpaEntitiesApiArg = void;
export type GetJavaVersionApiResponse = /** status 200 OK */ JavaVersionInfo;
export type GetJavaVersionApiArg = void;
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
export type JpaEntityClassInfo = {
    className?: string;
    simpleClassName?: string;
    entityName?: string;
    tableName?: string;
    tableSchema?: string;
    tableCatalog?: string;
    attributes?: JpaEntityAttributeInfo[];
};
export type JpaEntityClassInfoListResponse = {
    items?: JpaEntityClassInfo[];
    count?: number;
};
export type JavaVersionInfo = {
    /** Version Java utilisee par la JVM du serveur. */
    javaVersion?: string;
    /** Version runtime Java complete. */
    runtimeVersion?: string;
    /** Nom de la JVM. */
    vmName?: string;
    /** Fournisseur de la JVM. */
    vmVendor?: string;
    /** Nom du systeme d'exploitation hebergeant le serveur. */
    osName?: string;
};
export const { useListJpaEntitiesQuery, useGetJavaVersionQuery } =
    injectedRtkApi;
