import { emptySplitApi as api } from "./emptyApi";
export const addTagTypes = ["car"] as const;
const injectedRtkApi = api
    .enhanceEndpoints({
        addTagTypes,
    })
    .injectEndpoints({
        endpoints: (build) => ({
            getCar: build.query<GetCarApiResponse, GetCarApiArg>({
                query: (queryArg) => ({ url: `/car/${queryArg.id}` }),
                providesTags: ["car"],
            }),
            updateCar: build.mutation<UpdateCarApiResponse, UpdateCarApiArg>({
                query: (queryArg) => ({
                    url: `/car/${queryArg.id}`,
                    method: "PUT",
                    body: queryArg.car,
                }),
                invalidatesTags: ["car"],
            }),
            deleteCar: build.mutation<DeleteCarApiResponse, DeleteCarApiArg>({
                query: (queryArg) => ({
                    url: `/car/${queryArg.id}`,
                    method: "DELETE",
                }),
                invalidatesTags: ["car"],
            }),
            createCar: build.mutation<CreateCarApiResponse, CreateCarApiArg>({
                query: (queryArg) => ({
                    url: `/car`,
                    method: "POST",
                    body: queryArg.carCreationForm,
                }),
                invalidatesTags: ["car"],
            }),
            deleteAllCars: build.mutation<
                DeleteAllCarsApiResponse,
                DeleteAllCarsApiArg
            >({
                query: () => ({ url: `/car`, method: "DELETE" }),
                invalidatesTags: ["car"],
            }),
            importCars: build.mutation<ImportCarsApiResponse, ImportCarsApiArg>(
                {
                    query: (queryArg) => ({
                        url: `/car/import`,
                        method: "POST",
                        body: queryArg.carImportForm,
                    }),
                    invalidatesTags: ["car"],
                },
            ),
            importCarsCsv: build.mutation<
                ImportCarsCsvApiResponse,
                ImportCarsCsvApiArg
            >({
                query: (queryArg) => ({
                    url: `/car/import/csv`,
                    method: "POST",
                    body: queryArg.body,
                }),
                invalidatesTags: ["car"],
            }),
            searchCars: build.query<SearchCarsApiResponse, SearchCarsApiArg>({
                query: (queryArg) => ({
                    url: `/car/search`,
                    params: {
                        page: queryArg.page,
                        size: queryArg.size,
                        q: queryArg.q,
                    },
                }),
                providesTags: ["car"],
            }),
            listCars: build.query<ListCarsApiResponse, ListCarsApiArg>({
                query: () => ({ url: `/car/list` }),
                providesTags: ["car"],
            }),
            exportCars: build.query<ExportCarsApiResponse, ExportCarsApiArg>({
                query: () => ({ url: `/car/export` }),
                providesTags: ["car"],
            }),
            exportCarsCsv: build.query<
                ExportCarsCsvApiResponse,
                ExportCarsCsvApiArg
            >({
                query: () => ({ url: `/car/export/csv` }),
                providesTags: ["car"],
            }),
        }),
        overrideExisting: false,
    });
export { injectedRtkApi as carApi };
export type GetCarApiResponse = /** status 200 OK */ Car;
export type GetCarApiArg = {
    id: string;
};
export type UpdateCarApiResponse = /** status 200 OK */ Car;
export type UpdateCarApiArg = {
    id: string;
    car: Car;
};
export type DeleteCarApiResponse = unknown;
export type DeleteCarApiArg = {
    id: string;
};
export type CreateCarApiResponse = /** status 200 OK */ Car;
export type CreateCarApiArg = {
    carCreationForm: CarCreationForm;
};
export type DeleteAllCarsApiResponse = unknown;
export type DeleteAllCarsApiArg = void;
export type ImportCarsApiResponse = /** status 200 OK */ CarImportResult;
export type ImportCarsApiArg = {
    carImportForm: CarImportForm;
};
export type ImportCarsCsvApiResponse = /** status 200 OK */ CarImportResult;
export type ImportCarsCsvApiArg = {
    body: string;
};
export type SearchCarsApiResponse = /** status 200 OK */ CarSearchResponse;
export type SearchCarsApiArg = {
    /** Index de page (commence a 0). */
    page?: number;
    /** Nombre d'elements par page. */
    size?: number;
    /** Texte libre de recherche (nom, numero d'immatriculation et description). */
    q?: string;
};
export type ListCarsApiResponse = /** status 200 OK */ CarListResponse;
export type ListCarsApiArg = void;
export type ExportCarsApiResponse = /** status 200 OK */ CarListResponse;
export type ExportCarsApiArg = void;
export type ExportCarsCsvApiResponse = /** status 200 OK */ string;
export type ExportCarsCsvApiArg = void;
export type Car = {
    /** L'identifiant unique de la voiture. */
    id?: string;
    /** Le nom de la voiture. */
    name?: string;
    /** Le numéro d'immatriculation de la voiture. */
    vehicleRegistrationPlate?: string;
    /** La description optionnelle de la voiture. */
    description?: string;
    /** Date de derniere mise a jour de l'element. */
    updatedAt?: string;
};
export type CarCreationForm = {
    /** Le nom de la voiture. */
    name?: string;
    /** Le numéro d'immatriculation de la voiture. */
    vehicleRegistrationPlate?: string;
    /** La description optionnelle de la voiture. */
    description?: string;
};
export type CarImportResult = {
    /** Liste des voitures ajoutees pendant cet import. */
    imported?: Car[];
    /** Nombre de voitures ajoutees. */
    addedCount?: number;
    /** Nombre total de voitures non ajoutees. */
    notAddedCount?: number;
    /** Nombre de voitures non ajoutees car deja presentes. */
    alreadyExistsCount?: number;
    /** Nombre de lignes non ajoutees a cause d'une erreur de validation ou de persistence. */
    invalidCount?: number;
};
export type CarImportForm = {
    /** Liste des voitures a importer. */
    items?: Car[];
};
export type CarSearchResponse = {
    items?: Car[];
    count?: number;
    page?: number;
    size?: number;
    query?: string;
};
export type CarListResponse = {
    items?: Car[];
    count?: number;
};
export const {
    useGetCarQuery,
    useUpdateCarMutation,
    useDeleteCarMutation,
    useCreateCarMutation,
    useDeleteAllCarsMutation,
    useImportCarsMutation,
    useImportCarsCsvMutation,
    useSearchCarsQuery,
    useListCarsQuery,
    useExportCarsQuery,
    useExportCarsCsvQuery,
} = injectedRtkApi;
