import { createContext, useContext } from "react";

export type ThemeMode = "light" | "dark" | "system";
interface ThemeContextProps {
	mode: ThemeMode;
	setMode: (mode: ThemeMode) => void;
}
export const ThemeModeContext = createContext<ThemeContextProps>({ mode: "system", setMode: () => {} });

export const useThemeMode = () => useContext(ThemeModeContext);