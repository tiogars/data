import IconButton from '@mui/material/IconButton';
import Paper from '@mui/material/Paper';
import Stack from '@mui/material/Stack';
import TextField from '@mui/material/TextField';
import Typography from '@mui/material/Typography';
import Button from '@mui/material/Button';
import DeleteIcon from '@mui/icons-material/Delete';
import AddIcon from '@mui/icons-material/Add';
import { useFieldArray, useFormContext } from 'react-hook-form';

export type ModelAttributeFormValues = {
  name: string;
  description: string;
};

export type ModelFormValues = {
  name: string;
  description: string;
  modelAttributes: ModelAttributeFormValues[];
};

type ModelFormProps = {
  disabled?: boolean;
};

const ModelForm = ({ disabled = false }: ModelFormProps) => {
  const {
    register,
    control,
    formState: { errors },
  } = useFormContext<ModelFormValues>();

  const { fields, append, remove } = useFieldArray({
    control,
    name: 'modelAttributes',
  });

  return (
    <Stack spacing={2.5}>
      <TextField
        label="Nom du modele"
        fullWidth
        disabled={disabled}
        error={Boolean(errors.name)}
        helperText={errors.name?.message ?? 'Nom unique du modele (texte libre).'}
        {...register('name', {
          required: 'Le nom du modele est obligatoire.',
          validate: (value) => value.trim().length > 0 || 'Le nom du modele est obligatoire.',
        })}
      />
      <TextField
        label="Description"
        fullWidth
        multiline
        minRows={3}
        disabled={disabled}
        error={Boolean(errors.description)}
        helperText={errors.description?.message ?? 'Description optionnelle du modele.'}
        {...register('description')}
      />

      <Stack spacing={1.5}>
        <Stack direction="row" sx={{ justifyContent: 'space-between', alignItems: 'center' }}>
          <Typography variant="h6">Attributs du modele</Typography>
          <Button
            variant="outlined"
            size="small"
            startIcon={<AddIcon />}
            disabled={disabled}
            onClick={() => append({ name: '', description: '' })}
          >
            Ajouter un attribut
          </Button>
        </Stack>

        {fields.length === 0 && (
          <Typography variant="body2" color="text.secondary">
            Aucun attribut configure.
          </Typography>
        )}

        {fields.map((field, index) => (
          <Paper key={field.id} variant="outlined" sx={{ p: 1.5 }}>
            <Stack spacing={1.5}>
              <Stack direction="row" sx={{ justifyContent: 'space-between', alignItems: 'center' }}>
                <Typography variant="subtitle2">Attribut {index + 1}</Typography>
                <IconButton
                  aria-label="Supprimer l'attribut"
                  color="error"
                  disabled={disabled}
                  onClick={() => remove(index)}
                >
                  <DeleteIcon fontSize="small" />
                </IconButton>
              </Stack>
              <TextField
                label="Nom de l'attribut"
                fullWidth
                disabled={disabled}
                error={Boolean(errors.modelAttributes?.[index]?.name)}
                helperText={errors.modelAttributes?.[index]?.name?.message ?? 'Nom de l\'attribut.'}
                {...register(`modelAttributes.${index}.name` as const, {
                  required: 'Le nom de l\'attribut est obligatoire.',
                  validate: (value) => value.trim().length > 0 || 'Le nom de l\'attribut est obligatoire.',
                })}
              />
              <TextField
                label="Description de l'attribut"
                fullWidth
                multiline
                minRows={2}
                disabled={disabled}
                error={Boolean(errors.modelAttributes?.[index]?.description)}
                helperText={errors.modelAttributes?.[index]?.description?.message ?? 'Description optionnelle de l\'attribut.'}
                {...register(`modelAttributes.${index}.description` as const)}
              />
            </Stack>
          </Paper>
        ))}
      </Stack>
    </Stack>
  );
};

export default ModelForm;
