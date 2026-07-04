import { emptySplitApi as api } from "./emptyApi";
export const addTagTypes = ["section-docs-settings"] as const;
const injectedRtkApi = api
    .enhanceEndpoints({
        addTagTypes,
    })
    .injectEndpoints({
        endpoints: (build) => ({
            getSectionDocsSettingsState: build.query<
                GetSectionDocsSettingsStateApiResponse,
                GetSectionDocsSettingsStateApiArg
            >({
                query: () => ({ url: `/section-docs-settings/state` }),
                providesTags: ["section-docs-settings"],
            }),
            updateSectionDocsSettingsState: build.mutation<
                UpdateSectionDocsSettingsStateApiResponse,
                UpdateSectionDocsSettingsStateApiArg
            >({
                query: (queryArg) => ({
                    url: `/section-docs-settings/state`,
                    method: "PUT",
                    body: queryArg.sectionDocsSettingsState,
                }),
                invalidatesTags: ["section-docs-settings"],
            }),
        }),
        overrideExisting: false,
    });
export { injectedRtkApi as sectionDocsSettingsApi };
export type GetSectionDocsSettingsStateApiResponse =
    /** status 200 OK */ SectionDocsSettingsState;
export type GetSectionDocsSettingsStateApiArg = void;
export type UpdateSectionDocsSettingsStateApiResponse =
    /** status 200 OK */ SectionDocsSettingsState;
export type UpdateSectionDocsSettingsStateApiArg = {
    sectionDocsSettingsState: SectionDocsSettingsState;
};
export type SectionDocsSetting = {
    /** Identifiant unique du paramétrage de section. */
    id?: string;
    /** Identifiant de la section racine concernée. */
    sectionId?: string;
    /** Chemin relatif sous volumes/docs pour cette section racine. */
    storagePath?: string;
};
export type SectionDocsSettingsState = {
    /** Liste des paramétrages de chemin documentaire par section racine. */
    items?: SectionDocsSetting[];
};
export const {
    useGetSectionDocsSettingsStateQuery,
    useUpdateSectionDocsSettingsStateMutation,
} = injectedRtkApi;
