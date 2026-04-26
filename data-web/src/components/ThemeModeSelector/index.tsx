import { FormControl, InputLabel, MenuItem, Select } from '@mui/material';
import type { SelectChangeEvent } from '@mui/material/Select';
import type { ThemeMode } from '../../App';
import { useThemeMode } from '../../themeModeHook';

const ThemeModeSelector = () => {
    const { mode, setMode } = useThemeMode();

    const handleChange = (event: SelectChangeEvent<ThemeMode>) => {
        setMode(event.target.value as ThemeMode);
    };

    return (
        <FormControl size="small" variant="outlined">
            <InputLabel id="theme-mode-label" style={{ color: 'inherit' }}>Thème</InputLabel>
            <Select
                labelId="theme-mode-label"
                id="theme-mode-select"
                value={mode}
                label="Thème"
                onChange={handleChange}
                style={{ minWidth: 120, color: 'inherit' }}
            >
                <MenuItem value="light">Clair</MenuItem>
                <MenuItem value="dark">Sombre</MenuItem>
                <MenuItem value="system">Système</MenuItem>
            </Select>
        </FormControl>
    );
};

export default ThemeModeSelector;
