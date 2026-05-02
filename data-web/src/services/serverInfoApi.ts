import { emptySplitApi as api } from './emptyApi';

const injectedRtkApi = api.injectEndpoints({
  endpoints: (build) => ({
    getJavaVersionInfo: build.query<GetJavaVersionInfoApiResponse, GetJavaVersionInfoApiArg>({
      query: () => ({ url: '/server-info/java-version' }),
    }),
  }),
  overrideExisting: false,
});

export { injectedRtkApi as serverInfoApi };

export type GetJavaVersionInfoApiResponse = JavaVersionInfo;
export type GetJavaVersionInfoApiArg = void;

export type JavaVersionInfo = {
  javaVersion?: string;
  runtimeVersion?: string;
  vmName?: string;
  vmVendor?: string;
  osName?: string;
};

export const { useGetJavaVersionInfoQuery } = injectedRtkApi;
