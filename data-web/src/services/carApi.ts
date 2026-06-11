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
export type SearchCarsApiResponse = /** status 200 OK */ CarSearchResponse;
export type SearchCarsApiArg = {
    /** Index de page (commence a 0). */
    page?: number;
    /** Nombre d'elements par page. */
    size?: number;
    /** Texte libre de recherche (nom et description). */
    q?: string;
};
export type ListCarsApiResponse = /** status 200 OK */ CarListResponse;
export type ListCarsApiArg = void;
export type Car = {
    /** L'identifiant unique de la voiture. */
    id?: string;
    /** Le nom de la voiture. */
    name?: string;
    /** La description optionnelle de la voiture. */
    description?: string;
};
export type CarCreationForm = {
    /** Le nom de la voiture. */
    name?: string;
    /** La description optionnelle de la voiture. */
    description?: string;
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
    useSearchCarsQuery,
    useListCarsQuery,
} = injectedRtkApi;
