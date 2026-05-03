import { configureStore } from '@reduxjs/toolkit';
import './services/cacheInvalidation';
import { apiErrorSnackbarReducer } from './features/apiErrorSnackbar/slice';
import { rtkQueryErrorSnackbarMiddleware } from './middleware/rtkQueryErrorSnackbarMiddleware';
import { emptySplitApi } from './services/emptyApi';

export const store = configureStore({
  reducer: {
    apiErrorSnackbar: apiErrorSnackbarReducer,
    [emptySplitApi.reducerPath]: emptySplitApi.reducer,
  },
  middleware: (getDefaultMiddleware) =>
    getDefaultMiddleware().concat(rtkQueryErrorSnackbarMiddleware, emptySplitApi.middleware),
});

export type RootState = ReturnType<typeof store.getState>;
export type AppDispatch = typeof store.dispatch;
