import { emptySplitApi as api } from './emptyApi';

const injectedRtkApi = api.injectEndpoints({
  endpoints: (build) => ({
    getModelById: build.query<Model, { id: string }>({
      query: (queryArg) => ({ url: `/model/${queryArg.id}` }),
    }),
    getModelAiText: build.query<ModelAiTextResponse, { id: string }>({
      query: (queryArg) => ({ url: `/model/${queryArg.id}/ai-text` }),
    }),
    updateModel: build.mutation<Model, { id: string; model: Model }>({
      query: (queryArg) => ({
        url: `/model/${queryArg.id}`,
        method: 'PUT',
        body: queryArg.model,
      }),
    }),
    deleteModelById: build.mutation<unknown, { id: string }>({
      query: (queryArg) => ({
        url: `/model/${queryArg.id}`,
        method: 'DELETE',
      }),
    }),
    listModels: build.query<ModelListResponse, void>({
      query: () => ({ url: '/model' }),
    }),
    createModel: build.mutation<Model, { modelCreationForm: ModelCreationForm }>({
      query: (queryArg) => ({
        url: '/model',
        method: 'POST',
        body: queryArg.modelCreationForm,
      }),
    }),
    deleteAllModels: build.mutation<unknown, void>({
      query: () => ({ url: '/model', method: 'DELETE' }),
    }),
    exportModels: build.query<ModelListResponse, void>({
      query: () => ({ url: '/model/export' }),
    }),
    importModels: build.mutation<ModelImportResult, { modelImportForm: ModelImportForm }>({
      query: (queryArg) => ({
        url: '/model/import',
        method: 'POST',
        body: queryArg.modelImportForm,
      }),
    }),
    printModels: build.query<ModelPrintResponse, { mode?: 'filtered' | 'all'; name?: string; description?: string }>({
      query: (queryArg) => ({
        url: '/model/print',
        params: {
          mode: queryArg.mode,
          name: queryArg.name,
          description: queryArg.description,
        },
      }),
    }),
  }),
  overrideExisting: false,
});

export { injectedRtkApi as modelApi };

export type Model = {
  id?: string;
  name?: string;
  description?: string;
  modelAttributes?: ModelAttribute[];
};

export type ModelAttribute = {
  id?: string;
  name?: string;
  description?: string;
};

export type ModelListResponse = {
  items?: Model[];
  count?: number;
};

export type ModelCreationForm = {
  name?: string;
  description?: string;
  modelAttributes?: ModelAttribute[];
};

export type ModelImportForm = {
  items?: Model[];
};

export type ModelImportResult = {
  imported?: Model[];
  importedCount?: number;
  duplicateNames?: string[];
  skippedCount?: number;
};

export type ModelPrintResponse = {
  items?: Model[];
  count?: number;
  generatedAt?: string;
  total?: number;
};

export type ModelAiTextResponse = {
  modelId?: string;
  text?: string;
};

export const {
  useGetModelByIdQuery,
  useLazyGetModelAiTextQuery,
  useUpdateModelMutation,
  useDeleteModelByIdMutation,
  useListModelsQuery,
  useCreateModelMutation,
  useDeleteAllModelsMutation,
  useLazyExportModelsQuery,
  useImportModelsMutation,
  useLazyPrintModelsQuery,
} = injectedRtkApi;
