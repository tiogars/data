import { emptySplitApi as api } from "./emptyApi";
export const addTagTypes = ["url-manager"] as const;
const injectedRtkApi = api
    .enhanceEndpoints({
        addTagTypes,
    })
    .injectEndpoints({
        endpoints: (build) => ({
            getState: build.query<GetStateApiResponse, GetStateApiArg>({
                query: () => ({ url: `/url-manager/state` }),
                providesTags: ["url-manager"],
            }),
            updateState: build.mutation<
                UpdateStateApiResponse,
                UpdateStateApiArg
            >({
                query: (queryArg) => ({
                    url: `/url-manager/state`,
                    method: "PUT",
                    body: queryArg.urlManagerState,
                }),
                invalidatesTags: ["url-manager"],
            }),
            importState: build.mutation<
                ImportStateApiResponse,
                ImportStateApiArg
            >({
                query: (queryArg) => ({
                    url: `/url-manager/import`,
                    method: "POST",
                    body: queryArg.urlManagerState,
                }),
                invalidatesTags: ["url-manager"],
            }),
            exportState: build.query<ExportStateApiResponse, ExportStateApiArg>(
                {
                    query: () => ({ url: `/url-manager/export` }),
                    providesTags: ["url-manager"],
                },
            ),
        }),
        overrideExisting: false,
    });
export { injectedRtkApi as urlManagerApi };
export type GetStateApiResponse = /** status 200 OK */ UrlManagerState;
export type GetStateApiArg = void;
export type UpdateStateApiResponse = /** status 200 OK */ UrlManagerState;
export type UpdateStateApiArg = {
    urlManagerState: UrlManagerState;
};
export type ImportStateApiResponse = /** status 200 OK */ UrlManagerState;
export type ImportStateApiArg = {
    urlManagerState: UrlManagerState;
};
export type ExportStateApiResponse = /** status 200 OK */ UrlManagerState;
export type ExportStateApiArg = void;
export type ManagedUrl = {
    /** Identifiant unique du lien. */
    id?: string;
    /** Libelle du lien. */
    label?: string;
    /** URL cible. */
    url?: string;
    /** Liste des tags associes. */
    tags?: string[];
    /** Description optionnelle. */
    description?: string;
};
export type UrlCardConfig = {
    /** Identifiant unique de la carte. */
    id?: string;
    /** Titre de la carte affichee sur l'accueil. */
    title?: string;
    /** Tags utilises pour filtrer les liens. */
    tags?: string[];
    /** Mode de filtre: any (au moins un tag) ou all (tous les tags). */
    matchMode?: string;
};
export type UrlManagerState = {
    /** Liste des URLs gerees. */
    urls?: ManagedUrl[];
    /** Liste des cartes d'affichage pour la page d'accueil. */
    cards?: UrlCardConfig[];
};
export const {
    useGetStateQuery,
    useUpdateStateMutation,
    useImportStateMutation,
    useExportStateQuery,
} = injectedRtkApi;
