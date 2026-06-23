import IconButton from '@mui/material/IconButton';
import InputAdornment from '@mui/material/InputAdornment';
import Stack from '@mui/material/Stack';
import TextField from '@mui/material/TextField';
import OpenInNewIcon from '@mui/icons-material/OpenInNew';
import { useFormContext } from 'react-hook-form';
import { normalizeWebsiteUrl } from './WebsiteLink';

export type MaisonFormValues = {
  name: string;
  website: string;
};

type MaisonFormProps = { disabled?: boolean };

const MaisonForm = ({ disabled = false }: MaisonFormProps) => {
  const { register, watch, formState: { errors } } = useFormContext<MaisonFormValues>();
  const websiteValue = watch('website');
  const websiteHref = normalizeWebsiteUrl(websiteValue);

  return (
    <Stack spacing={2.5}>
      <TextField
        label="Nom"
        fullWidth
        disabled={disabled}
        error={Boolean(errors.name)}
        helperText={errors.name?.message ?? 'Nom de la maison.'}
        {...register('name', {
          required: 'Le nom est obligatoire.',
          validate: (value) => value.trim().length > 0 || 'Le nom est obligatoire.',
        })}
      />
      <TextField
        label="Site web"
        fullWidth
        disabled={disabled}
        error={Boolean(errors.website)}
        helperText={errors.website?.message ?? 'URL optionnelle de la maison.'}
        slotProps={{
          input: {
            endAdornment: websiteHref ? (
              <InputAdornment position="end">
                <IconButton
                  component="a"
                  href={websiteHref}
                  target="_blank"
                  rel="noopener noreferrer"
                  aria-label="Ouvrir le site web"
                  edge="end"
                  disabled={disabled}
                >
                  <OpenInNewIcon fontSize="small" />
                </IconButton>
              </InputAdornment>
            ) : undefined,
          },
        }}
        {...register('website', {
          validate: (value) => {
            if (!value.trim()) return true;
            try { new URL(value); return true; } catch { return 'Veuillez saisir une URL valide.'; }
          },
        })}
      />
    </Stack>
  );
};

export default MaisonForm;
