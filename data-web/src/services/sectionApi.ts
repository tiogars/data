import { emptySplitApi as api } from "./emptyApi";
export const addTagTypes = ["section"] as const;
const injectedRtkApi = api
    .enhanceEndpoints({
        addTagTypes,
    })
    .injectEndpoints({
        endpoints: (build) => ({
            getSectionById: build.query<
                GetSectionByIdApiResponse,
                GetSectionByIdApiArg
            >({
                query: (queryArg) => ({ url: `/section/${queryArg.id}` }),
                providesTags: ["section"],
            }),
            updateSection: build.mutation<
                UpdateSectionApiResponse,
                UpdateSectionApiArg
            >({
                query: (queryArg) => ({
                    url: `/section/${queryArg.id}`,
                    method: "PUT",
                    body: queryArg.section,
                }),
                invalidatesTags: ["section"],
            }),
            deleteSectionById: build.mutation<
                DeleteSectionByIdApiResponse,
                DeleteSectionByIdApiArg
            >({
                query: (queryArg) => ({
                    url: `/section/${queryArg.id}`,
                    method: "DELETE",
                }),
                invalidatesTags: ["section"],
            }),
            listSections: build.query<
                ListSectionsApiResponse,
                ListSectionsApiArg
            >({
                query: () => ({ url: `/section` }),
                providesTags: ["section"],
            }),
            createSection: build.mutation<
                CreateSectionApiResponse,
                CreateSectionApiArg
            >({
                query: (queryArg) => ({
                    url: `/section`,
                    method: "POST",
                    body: queryArg.sectionCreationForm,
                }),
                invalidatesTags: ["section"],
            }),
            deleteAllSections: build.mutation<
                DeleteAllSectionsApiResponse,
                DeleteAllSectionsApiArg
            >({
                query: () => ({ url: `/section`, method: "DELETE" }),
                invalidatesTags: ["section"],
            }),
        }),
        overrideExisting: false,
    });
export { injectedRtkApi as sectionApi };
export type GetSectionByIdApiResponse = /** status 200 OK */ Section;
export type GetSectionByIdApiArg = {
    id: string;
};
export type UpdateSectionApiResponse = /** status 200 OK */ Section;
export type UpdateSectionApiArg = {
    id: string;
    section: Section;
};
export type DeleteSectionByIdApiResponse = unknown;
export type DeleteSectionByIdApiArg = {
    id: string;
};
export type ListSectionsApiResponse = /** status 200 OK */ SectionListResponse;
export type ListSectionsApiArg = void;
export type CreateSectionApiResponse = /** status 200 OK */ Section;
export type CreateSectionApiArg = {
    sectionCreationForm: SectionCreationForm;
};
export type DeleteAllSectionsApiResponse = unknown;
export type DeleteAllSectionsApiArg = void;
export type Section = {
    /** L'identifiant unique de la section. */
    id?: string;
    /** Le nom de la section. */
    name?: string;
    /** La description de la section. */
    description?: string;
    /** L'identifiant du parent direct de la section. */
    parentId?: string;
    /** Les sous-sections rattachées à cette section. */
    children?: Section[];
};
export type SectionListResponse = {
    items?: Section[];
    count?: number;
};
export type SectionCreationForm = {
    /** Le nom de la section. */
    name?: string;
    /** La description de la section. */
    description?: string;
    /** L'identifiant de la section parente. */
    parentId?: string;
};
export const {
    useGetSectionByIdQuery,
    useUpdateSectionMutation,
    useDeleteSectionByIdMutation,
    useListSectionsQuery,
    useCreateSectionMutation,
    useDeleteAllSectionsMutation,
} = injectedRtkApi;
