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
    /** La date et l'heure du releve. */
    readingAt?: string;
    /** Le kilometrage releve en kilometres. */
    odometerKm?: number;
    /** Le volume de carburant ajoute en litres. */
    fuelVolumeLiters?: number;
    /** Indique si le plein complet a ete fait. */
    fullTank?: boolean;
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
export type CarMileageSearchResponse = {
    items?: CarMileage[];
    count?: number;
    page?: number;
    size?: number;
    carId?: string;
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
    useSearchCarMileagesQuery,
    useChartCarMileagesQuery,
} = injectedRtkApi;
