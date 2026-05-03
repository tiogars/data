import MenuItem from '@mui/material/MenuItem';
import Stack from '@mui/material/Stack';
import TextField from '@mui/material/TextField';
import type { ChangeEvent } from 'react';
import type { UrlCardConfig } from '../../features/urlManager/types';
import { parseTagsInput } from '../../features/urlManager/storage';

export type UrlCardConfigFormValues = {
  title: string;
  tagsInput: string;
  matchMode: 'any' | 'all';
};

export const emptyUrlCardConfigFormValues: UrlCardConfigFormValues = {
  title: '',
  tagsInput: '',
  matchMode: 'any',
};

export function toUrlCardPayload(values: UrlCardConfigFormValues): Omit<UrlCardConfig, 'id'> {
  return {
    title: values.title.trim(),
    tags: parseTagsInput(values.tagsInput),
    matchMode: values.matchMode,
  };
}

export function toUrlCardFormValues(item: UrlCardConfig): UrlCardConfigFormValues {
  return {
    title: item.title,
    tagsInput: item.tags.join(', '),
    matchMode: item.matchMode,
  };
}

type UrlCardConfigFormProps = {
  values: UrlCardConfigFormValues;
  disabled?: boolean;
  onChange: (next: UrlCardConfigFormValues) => void;
};

const UrlCardConfigForm = ({ values, disabled = false, onChange }: UrlCardConfigFormProps) => {
  const handleField = (field: keyof UrlCardConfigFormValues) => (event: ChangeEvent<HTMLInputElement>) => {
    onChange({ ...values, [field]: event.target.value });
  };

  return (
    <Stack spacing={2}>
      <TextField
        label="Titre de la carte"
        value={values.title}
        onChange={handleField('title')}
        disabled={disabled}
        required
        fullWidth
      />
      <TextField
        label="Tags"
        value={values.tagsInput}
        onChange={handleField('tagsInput')}
        disabled={disabled}
        required
        fullWidth
        helperText="Separer les tags avec des virgules."
      />
      <TextField
        select
        label="Mode de filtre"
        value={values.matchMode}
        onChange={handleField('matchMode')}
        disabled={disabled}
        fullWidth
      >
        <MenuItem value="any">Au moins un tag (par tag)</MenuItem>
        <MenuItem value="all">Tous les tags (reunion de tags)</MenuItem>
      </TextField>
    </Stack>
  );
};

export default UrlCardConfigForm;
