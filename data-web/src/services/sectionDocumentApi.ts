import { emptySplitApi as api } from "./emptyApi";

export const addTagTypes = ["section-document"] as const;

const injectedRtkApi = api
  .enhanceEndpoints({ addTagTypes })
  .injectEndpoints({
    endpoints: (build) => ({
      listSectionDocuments: build.query<ListSectionDocumentsApiResponse, ListSectionDocumentsApiArg>({
        query: () => ({ url: "/section-document/list" }),
        providesTags: ["section-document"],
      }),
      createSectionDocument: build.mutation<CreateSectionDocumentApiResponse, CreateSectionDocumentApiArg>({
        query: (queryArg) => ({
          url: "/section-document",
          method: "POST",
          body: queryArg.sectionDocument,
        }),
        invalidatesTags: ["section-document"],
      }),
      updateSectionDocument: build.mutation<UpdateSectionDocumentApiResponse, UpdateSectionDocumentApiArg>({
        query: (queryArg) => ({
          url: `/section-document/${queryArg.id}`,
          method: "PUT",
          body: queryArg.sectionDocument,
        }),
        invalidatesTags: ["section-document"],
      }),
      deleteSectionDocument: build.mutation<DeleteSectionDocumentApiResponse, DeleteSectionDocumentApiArg>({
        query: (queryArg) => ({
          url: `/section-document/${queryArg.id}`,
          method: "DELETE",
        }),
        invalidatesTags: ["section-document"],
      }),
    }),
    overrideExisting: false,
  });

export { injectedRtkApi as sectionDocumentApi };

export type SectionDocument = {
  id?: string;
  name?: string;
  storagePath?: string;
};

export type SectionDocumentListResponse = {
  items?: SectionDocument[];
  count?: number;
};

export type ListSectionDocumentsApiResponse = SectionDocumentListResponse;
export type ListSectionDocumentsApiArg = void;

export type CreateSectionDocumentApiResponse = SectionDocument;
export type CreateSectionDocumentApiArg = {
  sectionDocument: SectionDocument;
};

export type UpdateSectionDocumentApiResponse = SectionDocument;
export type UpdateSectionDocumentApiArg = {
  id: string;
  sectionDocument: SectionDocument;
};

export type DeleteSectionDocumentApiResponse = unknown;
export type DeleteSectionDocumentApiArg = {
  id: string;
};

export const {
  useListSectionDocumentsQuery,
  useCreateSectionDocumentMutation,
  useUpdateSectionDocumentMutation,
  useDeleteSectionDocumentMutation,
} = injectedRtkApi;
