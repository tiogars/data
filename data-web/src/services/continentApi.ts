import { emptySplitApi as api } from "./emptyApi";
const injectedRtkApi = api.injectEndpoints({
    endpoints: (build) => ({
        getContinent: build.query<GetContinentApiResponse, GetContinentApiArg>({
            query: (queryArg) => ({ url: `/continent/${queryArg.id}` }),
        }),
        updateContinent: build.mutation<
            UpdateContinentApiResponse,
            UpdateContinentApiArg
        >({
            query: (queryArg) => ({
                url: `/continent/${queryArg.id}`,
                method: "PUT",
                body: queryArg.continentUpdateForm,
            }),
        }),
        deleteContinent: build.mutation<
            DeleteContinentApiResponse,
            DeleteContinentApiArg
        >({
            query: (queryArg) => ({
                url: `/continent/${queryArg.id}`,
                method: "DELETE",
            }),
        }),
        listContinents: build.query<
            ListContinentsApiResponse,
            ListContinentsApiArg
        >({
            query: () => ({ url: `/continent` }),
        }),
        createContinent: build.mutation<
            CreateContinentApiResponse,
            CreateContinentApiArg
        >({
            query: (queryArg) => ({
                url: `/continent`,
                method: "POST",
                body: queryArg.continentCreationForm,
            }),
        }),
    }),
    overrideExisting: false,
});
export { injectedRtkApi as continentApi };
export type GetContinentApiResponse = /** status 200 OK */ Continent;
export type GetContinentApiArg = {
    id: string;
};
export type UpdateContinentApiResponse = /** status 200 OK */ Continent;
export type UpdateContinentApiArg = {
    id: string;
    continentUpdateForm: ContinentUpdateForm;
};
export type DeleteContinentApiResponse = unknown;
export type DeleteContinentApiArg = {
    id: string;
};
export type ListContinentsApiResponse =
    /** status 200 OK */ ContinentListResponse;
export type ListContinentsApiArg = void;
export type CreateContinentApiResponse = /** status 200 OK */ Continent;
export type CreateContinentApiArg = {
    continentCreationForm: ContinentCreationForm;
};
export type Continent = {
    /** L'identifiant unique du continent. */
    id?: string;
    /** Le code du continent. */
    code?: string;
    /** Le nom du continent. */
    name?: string;
};
export type ContinentUpdateForm = {
    /** L'identifiant unique du continent. */
    id?: string;
    /** Le code du continent. */
    code?: string;
    /** Le nom du continent. */
    name?: string;
};
export type ContinentListResponse = {
    items?: Continent[];
    count?: number;
};
export type ContinentCreationForm = {
    /** Le code du continent. */
    code?: string;
    /** Le nom du continent. */
    name?: string;
};
export const {
    useGetContinentQuery,
    useUpdateContinentMutation,
    useDeleteContinentMutation,
    useListContinentsQuery,
    useCreateContinentMutation,
} = injectedRtkApi;
