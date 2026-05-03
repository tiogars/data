import Stack from '@mui/material/Stack';
import TextField from '@mui/material/TextField';
import type { ChangeEvent } from 'react';
import type { ManagedUrl } from '../../features/urlManager/types';
import { parseTagsInput } from '../../features/urlManager/storage';

export type ManagedUrlFormValues = {
  label: string;
  url: string;
  tagsInput: string;
  description: string;
};

export const emptyManagedUrlFormValues: ManagedUrlFormValues = {
  label: '',
  url: '',
  tagsInput: '',
  description: '',
};

export function toManagedUrlPayload(values: ManagedUrlFormValues): Omit<ManagedUrl, 'id'> {
  return {
    label: values.label.trim(),
    url: values.url.trim(),
    tags: parseTagsInput(values.tagsInput),
    description: values.description.trim() || undefined,
  };
}

export function toManagedUrlFormValues(item: ManagedUrl): ManagedUrlFormValues {
  return {
    label: item.label,
    url: item.url,
    tagsInput: item.tags.join(', '),
    description: item.description ?? '',
  };
}

type ManagedUrlFormProps = {
  values: ManagedUrlFormValues;
  disabled?: boolean;
  onChange: (next: ManagedUrlFormValues) => void;
};

const ManagedUrlForm = ({ values, disabled = false, onChange }: ManagedUrlFormProps) => {
  const handleField = (field: keyof ManagedUrlFormValues) => (event: ChangeEvent<HTMLInputElement>) => {
    onChange({ ...values, [field]: event.target.value });
  };

  return (
    <Stack spacing={2}>
      <TextField
        label="Libelle"
        value={values.label}
        onChange={handleField('label')}
        disabled={disabled}
        required
        fullWidth
      />
      <TextField
        label="URL"
        value={values.url}
        onChange={handleField('url')}
        disabled={disabled}
        required
        fullWidth
        placeholder="https://..."
      />
      <TextField
        label="Tags"
        value={values.tagsInput}
        onChange={handleField('tagsInput')}
        disabled={disabled}
        fullWidth
        helperText="Separer les tags avec des virgules (ex: devops, java, sprint-12)."
      />
      <TextField
        label="Description"
        value={values.description}
        onChange={handleField('description')}
        disabled={disabled}
        fullWidth
        multiline
        minRows={2}
      />
    </Stack>
  );
};

export default ManagedUrlForm;
