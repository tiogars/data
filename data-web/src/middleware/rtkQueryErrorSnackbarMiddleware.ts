import { isRejectedWithValue, type Middleware } from '@reduxjs/toolkit';
import { showApiErrorSnackbar } from '../features/apiErrorSnackbar/slice';

const DEFAULT_MESSAGE = "Une erreur est survenue lors d'un appel API.";

const isObject = (value: unknown): value is Record<string, unknown> =>
  typeof value === 'object' && value !== null;

const getPayloadMessage = (payload: unknown): string | null => {
  if (!isObject(payload)) {
    return null;
  }

  const payloadData = payload.data;
  if (typeof payloadData === 'string') {
    return payloadData;
  }

  if (isObject(payloadData) && typeof payloadData.message === 'string') {
    return payloadData.message;
  }

  if (typeof payload.error === 'string') {
    return payload.error;
  }

  return null;
};

const getStatusLabel = (payload: unknown): string | null => {
  if (!isObject(payload) || payload.status === undefined) {
    return null;
  }

  if (typeof payload.status === 'number') {
    return `HTTP ${payload.status}`;
  }

  if (typeof payload.status === 'string') {
    return payload.status;
  }

  return null;
};

const toSnackbarMessage = (payload: unknown): string => {
  const message = getPayloadMessage(payload);
  const status = getStatusLabel(payload);

  if (message && status) {
    return `${status} - ${message}`;
  }

  if (message) {
    return message;
  }

  if (status) {
    return `${status} - ${DEFAULT_MESSAGE}`;
  }

  return DEFAULT_MESSAGE;
};

export const rtkQueryErrorSnackbarMiddleware: Middleware = ({ dispatch }) => (next) => (action) => {
  if (isRejectedWithValue(action)) {
    dispatch(showApiErrorSnackbar(toSnackbarMessage(action.payload)));
  }

  return next(action);
};
