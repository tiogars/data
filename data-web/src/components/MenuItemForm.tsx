import Box from '@mui/material/Box';
import MenuItem from '@mui/material/MenuItem';
import Stack from '@mui/material/Stack';
import TextField from '@mui/material/TextField';
import Typography from '@mui/material/Typography';
import { Controller, useFormContext } from 'react-hook-form';
import { menuItemIconOptions, renderMenuItemIcon } from '../features/menuItem/iconRegistry';
import { useListMenuItemsQuery } from '../services/menuItemApi';

export type MenuItemFormValues = {
  label: string;
  path: string;
  icon: string;
  displayOrder: number;
  parentId?: string;
};

type MenuItemFormProps = {
  disabled?: boolean;
};

const MenuItemForm = ({ disabled = false }: MenuItemFormProps) => {
  const {
    register,
    control,
    watch,
    formState: { errors },
  } = useFormContext<MenuItemFormValues>();

  const selectedIcon = watch('icon');
  const { data: menuItems } = useListMenuItemsQuery(undefined, { refetchOnMountOrArgChange: true });
  
  // Récupérer tous les menus potentiels (racine et enfants)
  const allMenuOptions = menuItems?.items
    ?.filter((item) => Boolean(item.label) && Boolean(item.id))
    .flatMap((item) => [
      { id: item.id, label: item.label, level: 0 },
      ...(item.children?.map((child) => ({ 
        id: child.id, 
        label: `  → ${child.label}`, 
        level: 1 
      })) ?? [])
    ]) ?? [];

  return (
    <Stack spacing={2.5}>
      <TextField
        label="Libelle"
        fullWidth
        disabled={disabled}
        error={Boolean(errors.label)}
        helperText={errors.label?.message ?? 'Nom affiche dans le menu.'}
        {...register('label', {
          required: 'Le libelle est obligatoire.',
          validate: (value) => value.trim().length > 0 || 'Le libelle est obligatoire.',
        })}
      />
      <TextField
        label="Chemin"
        fullWidth
        disabled={disabled}
        error={Boolean(errors.path)}
        helperText={errors.path?.message ?? "Doit commencer par '/' (ex: /section)."}
        {...register('path', {
          required: 'Le chemin est obligatoire.',
          validate: (value) => value.trim().startsWith('/') || "Le chemin doit commencer par '/'.",
        })}
      />
      <Controller
        control={control}
        name="icon"
        rules={{ required: "L'icone est obligatoire." }}
        render={({ field }) => (
          <TextField
            {...field}
            select
            label="Icone"
            fullWidth
            disabled={disabled}
            error={Boolean(errors.icon)}
            helperText={errors.icon?.message ?? 'Icone affichee dans la sidebar.'}
          >
            {menuItemIconOptions.map((option) => (
              <MenuItem key={option.value} value={option.value}>
                <Box sx={{ display: 'flex', alignItems: 'center', gap: 1 }}>
                  {renderMenuItemIcon(option.value)}
                  <span>{option.label}</span>
                </Box>
              </MenuItem>
            ))}
          </TextField>
        )}
      />
      <Box sx={{ display: 'flex', alignItems: 'center', gap: 1.5 }}>
        {renderMenuItemIcon(selectedIcon, 'medium')}
        <Typography variant="body2" color="text.secondary">
          Apercu de l'icone selectionnee
        </Typography>
      </Box>
      <Controller
        control={control}
        name="parentId"
        render={({ field }) => (
          <TextField
            {...field}
            select
            label="Menu parent (optionnel)"
            fullWidth
            disabled={disabled}
            error={Boolean(errors.parentId)}
            helperText={errors.parentId?.message ?? 'Laissez vide pour creer un menu de premier niveau.'}
          >
            <MenuItem value="">
              <em>Aucun (menu racine)</em>
            </MenuItem>
            {allMenuOptions.map((option) => (
              <MenuItem key={option.id} value={option.id}>
                {option.label}
              </MenuItem>
            ))}
          </TextField>
        )}
      />
      <TextField
        label="Ordre d'affichage"
        type="number"
        fullWidth
        disabled={disabled}
        error={Boolean(errors.displayOrder)}
        helperText={errors.displayOrder?.message ?? 'Les plus petits nombres apparaissent en premier.'}
        {...register('displayOrder', {
          required: "L'ordre d'affichage est obligatoire.",
          setValueAs: (value) => Number(value),
          validate: (value) => Number.isFinite(value) || "L'ordre d'affichage doit etre un nombre.",
        })}
      />
    </Stack>
  );
};

export default MenuItemForm;
