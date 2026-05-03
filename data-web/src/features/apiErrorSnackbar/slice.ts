import { createSlice, type PayloadAction } from '@reduxjs/toolkit';

interface ApiErrorSnackbarState {
  open: boolean;
  message: string;
}

const initialState: ApiErrorSnackbarState = {
  open: false,
  message: '',
};

const apiErrorSnackbarSlice = createSlice({
  name: 'apiErrorSnackbar',
  initialState,
  reducers: {
    showApiErrorSnackbar: (state, action: PayloadAction<string>) => {
      state.open = true;
      state.message = action.payload;
    },
    hideApiErrorSnackbar: (state) => {
      state.open = false;
    },
  },
});

export const { showApiErrorSnackbar, hideApiErrorSnackbar } = apiErrorSnackbarSlice.actions;
export const apiErrorSnackbarReducer = apiErrorSnackbarSlice.reducer;
