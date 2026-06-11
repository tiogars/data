import { emptySplitApi as api } from "./emptyApi";
export const addTagTypes = ["footer-link"] as const;
const injectedRtkApi = api
    .enhanceEndpoints({
        addTagTypes,
    })
    .injectEndpoints({
        endpoints: (build) => ({
            getFooterLinkById: build.query<
                GetFooterLinkByIdApiResponse,
                GetFooterLinkByIdApiArg
            >({
                query: (queryArg) => ({ url: `/footer-link/${queryArg.id}` }),
                providesTags: ["footer-link"],
            }),
            updateFooterLink: build.mutation<
                UpdateFooterLinkApiResponse,
                UpdateFooterLinkApiArg
            >({
                query: (queryArg) => ({
                    url: `/footer-link/${queryArg.id}`,
                    method: "PUT",
                    body: queryArg.footerLink,
                }),
                invalidatesTags: ["footer-link"],
            }),
            deleteFooterLinkById: build.mutation<
                DeleteFooterLinkByIdApiResponse,
                DeleteFooterLinkByIdApiArg
            >({
                query: (queryArg) => ({
                    url: `/footer-link/${queryArg.id}`,
                    method: "DELETE",
                }),
                invalidatesTags: ["footer-link"],
            }),
            createFooterLink: build.mutation<
                CreateFooterLinkApiResponse,
                CreateFooterLinkApiArg
            >({
                query: (queryArg) => ({
                    url: `/footer-link`,
                    method: "POST",
                    body: queryArg.footerLinkCreationForm,
                }),
                invalidatesTags: ["footer-link"],
            }),
            deleteAllFooterLinks: build.mutation<
                DeleteAllFooterLinksApiResponse,
                DeleteAllFooterLinksApiArg
            >({
                query: () => ({ url: `/footer-link`, method: "DELETE" }),
                invalidatesTags: ["footer-link"],
            }),
            listFooterLinks: build.query<
                ListFooterLinksApiResponse,
                ListFooterLinksApiArg
            >({
                query: () => ({ url: `/footer-link/list` }),
                providesTags: ["footer-link"],
            }),
        }),
        overrideExisting: false,
    });
export { injectedRtkApi as footerLinkApi };
export type GetFooterLinkByIdApiResponse = /** status 200 OK */ FooterLink;
export type GetFooterLinkByIdApiArg = {
    id: string;
};
export type UpdateFooterLinkApiResponse = /** status 200 OK */ FooterLink;
export type UpdateFooterLinkApiArg = {
    id: string;
    footerLink: FooterLink;
};
export type DeleteFooterLinkByIdApiResponse = unknown;
export type DeleteFooterLinkByIdApiArg = {
    id: string;
};
export type CreateFooterLinkApiResponse = /** status 200 OK */ FooterLink;
export type CreateFooterLinkApiArg = {
    footerLinkCreationForm: FooterLinkCreationForm;
};
export type DeleteAllFooterLinksApiResponse = unknown;
export type DeleteAllFooterLinksApiArg = void;
export type ListFooterLinksApiResponse =
    /** status 200 OK */ FooterLinkListResponse;
export type ListFooterLinksApiArg = void;
export type FooterLink = {
    /** L'identifiant unique du lien de footer. */
    id?: string;
    /** Le libellé affiché pour le lien. */
    label?: string;
    /** L'URL cible du lien. */
    url?: string;
    /** La clé d'icône utilisée par l'application web. */
    icon?: string;
    /** L'ordre d'affichage du lien dans le footer. */
    displayOrder?: number;
};
export type FooterLinkCreationForm = {
    /** Le libellé affiché pour le lien. */
    label?: string;
    /** L'URL cible du lien. */
    url?: string;
    /** La clé d'icône utilisée par l'application web. */
    icon?: string;
    /** L'ordre d'affichage du lien dans le footer. */
    displayOrder?: number;
};
export type FooterLinkListResponse = {
    items?: FooterLink[];
    count?: number;
};
export const {
    useGetFooterLinkByIdQuery,
    useUpdateFooterLinkMutation,
    useDeleteFooterLinkByIdMutation,
    useCreateFooterLinkMutation,
    useDeleteAllFooterLinksMutation,
    useListFooterLinksQuery,
} = injectedRtkApi;
