import Box from '@mui/material/Box';
import MenuItem from '@mui/material/MenuItem';
import Stack from '@mui/material/Stack';
import TextField from '@mui/material/TextField';
import Typography from '@mui/material/Typography';
import { Controller, useFormContext } from 'react-hook-form';
import { footerLinkIconOptions, renderFooterLinkIcon } from '../features/footerLink/iconRegistry';

export type FooterLinkFormValues = {
  label: string;
  url: string;
  icon: string;
  displayOrder: number;
};

type FooterLinkFormProps = {
  disabled?: boolean;
};

const FooterLinkForm = ({ disabled = false }: FooterLinkFormProps) => {
  const {
    register,
    control,
    watch,
    formState: { errors },
  } = useFormContext<FooterLinkFormValues>();

  const selectedIcon = watch('icon');

  return (
    <Stack spacing={2.5}>
      <TextField
        label="Libellé"
        fullWidth
        disabled={disabled}
        error={Boolean(errors.label)}
        helperText={errors.label?.message ?? 'Nom affiché dans le footer.'}
        {...register('label', {
          required: 'Le libellé est obligatoire.',
          validate: (value) => value.trim().length > 0 || 'Le libellé est obligatoire.',
        })}
      />
      <TextField
        label="URL"
        type="url"
        fullWidth
        disabled={disabled}
        error={Boolean(errors.url)}
        helperText={errors.url?.message ?? 'Adresse absolue du lien.'}
        {...register('url', {
          required: 'L\'URL est obligatoire.',
          pattern: {
            value: /^https?:\/\/.+/i,
            message: 'L\'URL doit commencer par http:// ou https://.',
          },
        })}
      />
      <Controller
        control={control}
        name="icon"
        rules={{ required: 'L\'icône est obligatoire.' }}
        render={({ field }) => (
          <TextField
            {...field}
            select
            label="Icône"
            fullWidth
            disabled={disabled}
            error={Boolean(errors.icon)}
            helperText={errors.icon?.message ?? 'Icône affichée dans le footer et les listes.'}
          >
            {footerLinkIconOptions.map((option) => (
              <MenuItem key={option.value} value={option.value}>
                <Box sx={{ display: 'flex', alignItems: 'center', gap: 1 }}>
                  {renderFooterLinkIcon(option.value)}
                  <span>{option.label}</span>
                </Box>
              </MenuItem>
            ))}
          </TextField>
        )}
      />
      <Box sx={{ display: 'flex', alignItems: 'center', gap: 1.5 }}>
        {renderFooterLinkIcon(selectedIcon, 'medium')}
        <Typography variant="body2" color="text.secondary">
          Aperçu de l'icône sélectionnée
        </Typography>
      </Box>
      <TextField
        label="Ordre d'affichage"
        type="number"
        fullWidth
        disabled={disabled}
        error={Boolean(errors.displayOrder)}
        helperText={errors.displayOrder?.message ?? 'Les plus petits nombres apparaissent en premier.'}
        {...register('displayOrder', {
          required: 'L\'ordre d\'affichage est obligatoire.',
          setValueAs: (value) => Number(value),
          validate: (value) => Number.isFinite(value) || 'L\'ordre d\'affichage doit être un nombre.',
        })}
      />
    </Stack>
  );
};

export default FooterLinkForm;