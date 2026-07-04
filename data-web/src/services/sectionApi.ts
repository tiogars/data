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
            searchSections: build.query<
                SearchSectionsApiResponse,
                SearchSectionsApiArg
            >({
                query: (queryArg) => ({
                    url: `/section/search`,
                    params: {
                        page: queryArg.page,
                        size: queryArg.size,
                        q: queryArg.q,
                        documentId: queryArg.documentId,
                    },
                }),
                providesTags: ["section"],
            }),
            listSections: build.query<
                ListSectionsApiResponse,
                ListSectionsApiArg
            >({
                query: (queryArg) => ({
                    url: `/section/list`,
                    params: {
                        documentId: queryArg.documentId,
                    },
                }),
                providesTags: ["section"],
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
export type CreateSectionApiResponse = /** status 200 OK */ Section;
export type CreateSectionApiArg = {
    sectionCreationForm: SectionCreationForm;
};
export type DeleteAllSectionsApiResponse = unknown;
export type DeleteAllSectionsApiArg = void;
export type SearchSectionsApiResponse =
    /** status 200 OK */ SectionSearchResponse;
export type SearchSectionsApiArg = {
    /** Index de page (commence a 0). */
    page?: number;
    /** Nombre d'elements par page. */
    size?: number;
    /** Texte libre de recherche (nom et description). */
    q?: string;
    /** Identifiant du document pour filtrer les sections. */
    documentId?: string;
};
export type ListSectionsApiResponse = /** status 200 OK */ SectionListResponse;
export type ListSectionsApiArg = {
    /** Identifiant du document pour filtrer les sections. */
    documentId?: string;
};
export type Section = {
    /** L'identifiant unique de la section. */
    id?: string;
    /** Le nom de la section. */
    name?: string;
    /** La description de la section. */
    description?: string;
    /** L'ordre d'affichage de la section parmi ses sections soeurs. */
    displayOrder?: number;
    /** L'identifiant du parent direct de la section. */
    parentId?: string;
    /** L'identifiant du document auquel la section appartient. */
    documentId?: string;
    /** Les sous-sections rattachées à cette section. */
    children?: Section[];
};
export type SectionCreationForm = {
    /** Le nom de la section. */
    name?: string;
    /** La description de la section. */
    description?: string;
    /** L'ordre d'affichage de la section parmi ses sections soeurs. */
    displayOrder?: number;
    /** L'identifiant de la section parente. */
    parentId?: string;
    /** L'identifiant du document auquel rattacher la section. */
    documentId?: string;
};
export type SectionSearchResponse = {
    items?: Section[];
    count?: number;
    page?: number;
    size?: number;
    query?: string;
};
export type SectionListResponse = {
    items?: Section[];
    count?: number;
};
export const {
    useGetSectionByIdQuery,
    useUpdateSectionMutation,
    useDeleteSectionByIdMutation,
    useCreateSectionMutation,
    useDeleteAllSectionsMutation,
    useSearchSectionsQuery,
    useListSectionsQuery,
} = injectedRtkApi;
