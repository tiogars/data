import { emptySplitApi as api } from "./emptyApi";
export const addTagTypes = ["car-mileage"] as const;
const injectedRtkApi = api
    .enhanceEndpoints({
        addTagTypes,
    })
    .injectEndpoints({
        endpoints: (build) => ({
            getCarMileage: build.query<
                GetCarMileageApiResponse,
                GetCarMileageApiArg
            >({
                query: (queryArg) => ({ url: `/car-mileage/${queryArg.id}` }),
                providesTags: ["car-mileage"],
            }),
            updateCarMileage: build.mutation<
                UpdateCarMileageApiResponse,
                UpdateCarMileageApiArg
            >({
                query: (queryArg) => ({
                    url: `/car-mileage/${queryArg.id}`,
                    method: "PUT",
                    body: queryArg.carMileage,
                }),
                invalidatesTags: ["car-mileage"],
            }),
            deleteCarMileage: build.mutation<
                DeleteCarMileageApiResponse,
                DeleteCarMileageApiArg
            >({
                query: (queryArg) => ({
                    url: `/car-mileage/${queryArg.id}`,
                    method: "DELETE",
                }),
                invalidatesTags: ["car-mileage"],
            }),
            createCarMileage: build.mutation<
                CreateCarMileageApiResponse,
                CreateCarMileageApiArg
            >({
                query: (queryArg) => ({
                    url: `/car-mileage`,
                    method: "POST",
                    body: queryArg.carMileageCreationForm,
                }),
                invalidatesTags: ["car-mileage"],
            }),
            importCarMileages: build.mutation<
                ImportCarMileagesApiResponse,
                ImportCarMileagesApiArg
            >({
                query: (queryArg) => ({
                    url: `/car-mileage/import`,
                    method: "POST",
                    body: queryArg.carMileageImportForm,
                }),
                invalidatesTags: ["car-mileage"],
            }),
            importCarMileagesCsv: build.mutation<
                ImportCarMileagesCsvApiResponse,
                ImportCarMileagesCsvApiArg
            >({
                query: (queryArg) => ({
                    url: `/car-mileage/import/csv`,
                    method: "POST",
                    body: queryArg.body,
                }),
                invalidatesTags: ["car-mileage"],
            }),
            searchCarMileages: build.query<
                SearchCarMileagesApiResponse,
                SearchCarMileagesApiArg
            >({
                query: (queryArg) => ({
                    url: `/car-mileage/search`,
                    params: {
                        carId: queryArg.carId,
                        page: queryArg.page,
                        size: queryArg.size,
                    },
                }),
                providesTags: ["car-mileage"],
            }),
            exportCarMileages: build.query<
                ExportCarMileagesApiResponse,
                ExportCarMileagesApiArg
            >({
                query: () => ({ url: `/car-mileage/export` }),
                providesTags: ["car-mileage"],
            }),
            exportCarMileagesCsv: build.query<
                ExportCarMileagesCsvApiResponse,
                ExportCarMileagesCsvApiArg
            >({
                query: () => ({ url: `/car-mileage/export/csv` }),
                providesTags: ["car-mileage"],
            }),
            chartCarMileages: build.query<
                ChartCarMileagesApiResponse,
                ChartCarMileagesApiArg
            >({
                query: (queryArg) => ({
                    url: `/car-mileage/chart`,
                    params: {
                        carId: queryArg.carId,
                    },
                }),
                providesTags: ["car-mileage"],
            }),
        }),
        overrideExisting: false,
    });
export { injectedRtkApi as carMileageApi };
export type GetCarMileageApiResponse = /** status 200 OK */ CarMileage;
export type GetCarMileageApiArg = {
    id: string;
};
export type UpdateCarMileageApiResponse = /** status 200 OK */ CarMileage;
export type UpdateCarMileageApiArg = {
    id: string;
    carMileage: CarMileage;
};
export type DeleteCarMileageApiResponse = unknown;
export type DeleteCarMileageApiArg = {
    id: string;
};
export type CreateCarMileageApiResponse = /** status 200 OK */ CarMileage;
export type CreateCarMileageApiArg = {
    carMileageCreationForm: CarMileageCreationForm;
};
export type ImportCarMileagesApiResponse =
    /** status 200 OK */ CarMileageImportResult;
export type ImportCarMileagesApiArg = {
    carMileageImportForm: CarMileageImportForm;
};
export type ImportCarMileagesCsvApiResponse =
    /** status 200 OK */ CarMileageImportResult;
export type ImportCarMileagesCsvApiArg = {
    body: string;
};
export type SearchCarMileagesApiResponse =
    /** status 200 OK */ CarMileageSearchResponse;
export type SearchCarMileagesApiArg = {
    /** Identifiant optionnel de voiture pour filtrer. */
    carId?: string;
    /** Index de page (commence a 0). */
    page?: number;
    /** Nombre d'elements par page. */
    size?: number;
};
export type ExportCarMileagesApiResponse =
    /** status 200 OK */ CarMileageListResponse;
export type ExportCarMileagesApiArg = void;
export type ExportCarMileagesCsvApiResponse = /** status 200 OK */ string;
export type ExportCarMileagesCsvApiArg = void;
export type ChartCarMileagesApiResponse =
    /** status 200 OK */ CarMileageChartResponse;
export type ChartCarMileagesApiArg = {
    /** Identifiant de la voiture. */
    carId: string;
};
export type CarMileage = {
    /** L'identifiant unique du releve. */
    id?: string;
    /** L'identifiant de la voiture. */
    carId?: string;
    /** Le nom de la voiture. */
    carName?: string;
    /** Le numéro d'immatriculation de la voiture. */
    vehicleRegistrationPlate?: string;
    /** La date et l'heure du releve. */
    readingAt?: string;
    /** Le kilometrage releve en kilometres. */
    odometerKm?: number;
    /** Le volume de carburant ajoute en litres. */
    fuelVolumeLiters?: number;
    /** Indique si le plein complet a ete fait. */
    fullTank?: boolean;
    /** Date de derniere mise a jour de l'element. */
    updatedAt?: string;
};
export type CarMileageCreationForm = {
    /** L'identifiant de la voiture. */
    carId?: string;
    /** La date et l'heure du releve. Si non renseignee, la date/heure actuelle est appliquee. */
    readingAt?: string;
    /** Le kilometrage releve en kilometres. */
    odometerKm?: number;
    /** Le volume de carburant ajoute en litres. */
    fuelVolumeLiters?: number;
    /** Indique si le plein complet a ete fait. */
    fullTank?: boolean;
};
export type CarMileageImportResult = {
    /** Liste des releves ajoutes pendant cet import. */
    imported?: CarMileage[];
    /** Nombre de releves ajoutes. */
    addedCount?: number;
    /** Nombre total de releves non ajoutes. */
    notAddedCount?: number;
    /** Nombre de releves non ajoutes car deja presents. */
    alreadyExistsCount?: number;
    /** Nombre de lignes non ajoutees a cause d'une erreur de validation ou de persistence. */
    invalidCount?: number;
};
export type CarMileageImportForm = {
    /** Liste des releves de kilometrage a importer. */
    items?: CarMileage[];
};
export type CarMileageSearchResponse = {
    items?: CarMileage[];
    count?: number;
    page?: number;
    size?: number;
    carId?: string;
};
export type CarMileageListResponse = {
    /** Liste des releves de kilometrage. */
    items?: CarMileage[];
    /** Nombre total de releves. */
    count?: number;
};
export type CarMileageChartPoint = {
    /** La date et l'heure du releve. */
    readingAt?: string;
    /** Le kilometrage releve en kilometres. */
    odometerKm?: number;
};
export type CarMileageChartResponse = {
    /** L'identifiant de la voiture. */
    carId?: string;
    /** Le nom de la voiture. */
    carName?: string;
    /** Les points du graphique ordonnes chronologiquement. */
    points?: CarMileageChartPoint[];
};
export const {
    useGetCarMileageQuery,
    useUpdateCarMileageMutation,
    useDeleteCarMileageMutation,
    useCreateCarMileageMutation,
    useImportCarMileagesMutation,
    useImportCarMileagesCsvMutation,
    useSearchCarMileagesQuery,
    useExportCarMileagesQuery,
    useExportCarMileagesCsvQuery,
    useChartCarMileagesQuery,
} = injectedRtkApi;
