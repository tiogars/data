import { emptySplitApi as api } from "./emptyApi";
export const addTagTypes = ["vin"] as const;
const injectedRtkApi = api
    .enhanceEndpoints({
        addTagTypes,
    })
    .injectEndpoints({
        endpoints: (build) => ({
            getVin: build.query<GetVinApiResponse, GetVinApiArg>({
                query: (queryArg) => ({ url: `/vin/${queryArg.id}` }),
                providesTags: ["vin"],
            }),
            updateVin: build.mutation<UpdateVinApiResponse, UpdateVinApiArg>({
                query: (queryArg) => ({
                    url: `/vin/${queryArg.id}`,
                    method: "PUT",
                    body: queryArg.vinCreationForm,
                }),
                invalidatesTags: ["vin"],
            }),
            deleteVin: build.mutation<DeleteVinApiResponse, DeleteVinApiArg>({
                query: (queryArg) => ({
                    url: `/vin/${queryArg.id}`,
                    method: "DELETE",
                }),
                invalidatesTags: ["vin"],
            }),
            createVin: build.mutation<CreateVinApiResponse, CreateVinApiArg>({
                query: (queryArg) => ({
                    url: `/vin`,
                    method: "POST",
                    body: queryArg.vinCreationForm,
                }),
                invalidatesTags: ["vin"],
            }),
            deleteAllVins: build.mutation<
                DeleteAllVinsApiResponse,
                DeleteAllVinsApiArg
            >({
                query: () => ({ url: `/vin`, method: "DELETE" }),
                invalidatesTags: ["vin"],
            }),
            importVins: build.mutation<ImportVinsApiResponse, ImportVinsApiArg>(
                {
                    query: (queryArg) => ({
                        url: `/vin/import`,
                        method: "POST",
                        body: queryArg.vinImportForm,
                    }),
                    invalidatesTags: ["vin"],
                },
            ),
            searchVins: build.query<SearchVinsApiResponse, SearchVinsApiArg>({
                query: (queryArg) => ({
                    url: `/vin/search`,
                    params: {
                        page: queryArg.page,
                        size: queryArg.size,
                        q: queryArg.q,
                        appellationId: queryArg.appellationId,
                        couleurId: queryArg.couleurId,
                        annee: queryArg.annee,
                    },
                }),
                providesTags: ["vin"],
            }),
            printVins: build.query<PrintVinsApiResponse, PrintVinsApiArg>({
                query: (queryArg) => ({
                    url: `/vin/print`,
                    params: {
                        mode: queryArg.mode,
                        annee: queryArg.annee,
                        region: queryArg.region,
                    },
                }),
                providesTags: ["vin"],
            }),
            listVins: build.query<ListVinsApiResponse, ListVinsApiArg>({
                query: () => ({ url: `/vin/list` }),
                providesTags: ["vin"],
            }),
            exportVins: build.query<ExportVinsApiResponse, ExportVinsApiArg>({
                query: () => ({ url: `/vin/export` }),
                providesTags: ["vin"],
            }),
        }),
        overrideExisting: false,
    });
export { injectedRtkApi as vinApi };
export type GetVinApiResponse = /** status 200 OK */ Vin;
export type GetVinApiArg = {
    id: string;
};
export type UpdateVinApiResponse = /** status 200 OK */ Vin;
export type UpdateVinApiArg = {
    id: string;
    vinCreationForm: VinCreationForm;
};
export type DeleteVinApiResponse = unknown;
export type DeleteVinApiArg = {
    id: string;
};
export type CreateVinApiResponse = /** status 200 OK */ Vin;
export type CreateVinApiArg = {
    vinCreationForm: VinCreationForm;
};
export type DeleteAllVinsApiResponse = unknown;
export type DeleteAllVinsApiArg = void;
export type ImportVinsApiResponse = /** status 200 OK */ VinImportResult;
export type ImportVinsApiArg = {
    vinImportForm: VinImportForm;
};
export type SearchVinsApiResponse = /** status 200 OK */ VinSearchResponse;
export type SearchVinsApiArg = {
    /** Index de page (commence a 0). */
    page?: number;
    /** Nombre d'elements par page. */
    size?: number;
    /** Texte libre de recherche. */
    q?: string;
    /** Identifiant d'appellation. */
    appellationId?: string;
    /** Identifiant de couleur. */
    couleurId?: string;
    /** Annee exacte. */
    annee?: number;
};
export type PrintVinsApiResponse = /** status 200 OK */ VinPrintResponse;
export type PrintVinsApiArg = {
    mode?: string;
    annee?: number;
    region?: string;
};
export type ListVinsApiResponse = /** status 200 OK */ VinListResponse;
export type ListVinsApiArg = void;
export type ExportVinsApiResponse = /** status 200 OK */ VinListResponse;
export type ExportVinsApiArg = void;
export type VinCepageEntry = {
    /** Identifiant du cepage. */
    cepageId?: string;
    /** Nom du cepage. */
    cepageName?: string;
    /** Pourcentage du cepage dans l'assemblage. */
    pourcentage?: number;
};
export type Vin = {
    /** Identifiant unique du vin. */
    id?: string;
    /** Identifiant de l'appellation. */
    appellationId?: string;
    /** Nom de l'appellation. */
    appellationName?: string;
    /** Identifiant de la couleur. */
    couleurId?: string;
    /** Nom de la couleur. */
    couleurName?: string;
    /** Identifiant du type de vin. */
    typeVinId?: string;
    /** Nom du type de vin. */
    typeVinName?: string;
    /** Identifiant de la maison. */
    maisonId?: string;
    /** Nom de la maison. */
    maisonName?: string;
    /** Identifiant du nom du vin. */
    vinNomId?: string;
    /** Nom du vin. */
    vinNomName?: string;
    /** Identifiant du contenant. */
    contenantId?: string;
    /** Nom du contenant. */
    contenantName?: string;
    /** Annee du vin. */
    annee?: number;
    /** Mois de degorgement du vin mousseux. */
    degorgementMois?: number;
    /** Annee de degorgement du vin mousseux. */
    degorgementAnnee?: number;
    /** Dosage en grammes par litre. */
    dosageGrammesParLitre?: number;
    /** Commune du vin. */
    commune?: string;
    /** Region du vin. */
    region?: string;
    /** Commentaires de degustation. */
    commentaires?: string;
    /** Accords mets et vins. */
    accordsMetsVins?: string;
    /** Liste des cepages du vin. */
    cepages?: VinCepageEntry[];
    /** Liste des identifiants de circonstances. */
    circonstances?: string[];
    /** Liste des libelles de circonstances. */
    circonstanceNames?: string[];
    /** Liste des identifiants de tags. */
    tags?: string[];
    /** Liste des libelles de tags. */
    tagNames?: string[];
    /** Date de creation ISO-8601. */
    createdAt?: string;
    /** Date de mise a jour ISO-8601. */
    updatedAt?: string;
};
export type VinCepageEntryForm = {
    /** Identifiant du cepage. */
    cepageId?: string;
    /** Pourcentage du cepage dans l'assemblage. */
    pourcentage?: number;
};
export type VinCreationForm = {
    /** Identifiant de l'appellation. */
    appellationId?: string;
    /** Identifiant de la couleur. */
    couleurId?: string;
    /** Identifiant du type de vin. */
    typeVinId?: string;
    /** Identifiant de la maison. */
    maisonId?: string;
    /** Identifiant du nom du vin. */
    vinNomId?: string;
    /** Identifiant du contenant. */
    contenantId?: string;
    /** Millesime du vin. */
    annee?: number;
    /** Mois de degorgement du vin mousseux. */
    degorgementMois?: number;
    /** Annee de degorgement du vin mousseux. */
    degorgementAnnee?: number;
    /** Dosage en grammes par litre. */
    dosageGrammesParLitre?: number;
    /** Commune du vin. */
    commune?: string;
    /** Region du vin. */
    region?: string;
    /** Commentaires de degustation. */
    commentaires?: string;
    /** Accords mets et vins. */
    accordsMetsVins?: string;
    /** Liste des cepages composant le vin. */
    cepages?: VinCepageEntryForm[];
    /** Liste des identifiants de circonstances. */
    circonstanceIds?: string[];
    /** Liste des identifiants de tags. */
    tagIds?: string[];
};
export type VinImportResult = {
    /** Liste des vins importes. */
    imported?: Vin[];
    /** Nombre de vins importes (champ historique). */
    importedCount?: number;
    /** Nombre de vins non ajoutes (champ historique). */
    notAddedCount?: number;
    /** Nombre de vins ajoutes. */
    addedCount?: number;
    /** Nombre de vins deja presents et ignores. */
    alreadyExistsCount?: number;
    /** Nombre de lignes invalides ignorees. */
    invalidCount?: number;
};
export type VinImportForm = {
    /** Liste JSON des vins a importer. */
    items?: Vin[];
};
export type VinSearchResponse = {
    items?: Vin[];
    count?: number;
    page?: number;
    size?: number;
    query?: string;
};
export type VinPrintResponse = {
    items?: Vin[];
    count?: number;
    generatedAt?: string;
    total?: number;
};
export type VinListResponse = {
    items?: Vin[];
    count?: number;
};
export const {
    useGetVinQuery,
    useUpdateVinMutation,
    useDeleteVinMutation,
    useCreateVinMutation,
    useDeleteAllVinsMutation,
    useImportVinsMutation,
    useSearchVinsQuery,
    usePrintVinsQuery,
    useListVinsQuery,
    useExportVinsQuery,
} = injectedRtkApi;
