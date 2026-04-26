import { BrowserRouter, Route, Routes, useParams, Navigate } from "react-router-dom";
import HomePage from "./pages/HomePage";
import Sidebar from "./components/Sidebar";
import { SectionCreatePage } from "./pages/section/SectionCreatePage";
import { SectionDetailPage } from "./pages/section/SectionDetailPage";
import { SectionEditPage } from "./pages/section/SectionEditPage";
import { SectionListPage } from "./pages/section/SectionListPage";
import Header from "./components/Header";
import "./App.css";
import { useMemo, useState } from "react";
import { ThemeProvider, createTheme, CssBaseline } from "@mui/material";
import { Provider } from "react-redux";
import { store } from "./store";

import { ThemeModeContext } from "./themeModeHook";



export type ThemeMode = "light" | "dark" | "system";
// Voir themeModeHook.ts pour le contexte

const getSystemTheme = () =>
    globalThis.matchMedia?.("(prefers-color-scheme: dark)").matches ? "dark" : "light";

const App = () => {
    const [mode, setMode] = useState<ThemeMode>(() => {
        const saved = localStorage.getItem("theme-mode");
        return (saved as ThemeMode) || "system";
    });

    const effectiveMode = mode === "system" ? getSystemTheme() : mode;

    const theme = useMemo(
        () =>
            createTheme({
                palette: {
                    mode: effectiveMode,
                },
            }),
        [effectiveMode]
    );

    // Sauvegarde le mode dans le localStorage
    const handleSetMode = (newMode: ThemeMode) => {
        setMode(newMode);
        localStorage.setItem("theme-mode", newMode);
    };

    const providerValue = useMemo(() => ({ mode, setMode: handleSetMode }), [mode]);

    return (
        <Provider store={store}>
            <ThemeModeContext.Provider value={providerValue}>
                <ThemeProvider theme={theme}>
                    <CssBaseline />
                    <BrowserRouter>
                        <div className="app-layout">
                            <Sidebar />
                            <main className="app-main">
                                <Header />
                                <Routes>
                                    <Route path="/" element={<HomePage />} />
                                    <Route path="/section" element={<SectionListPage />} />
                                    <Route path="/section/create" element={<SectionCreatePage />} />
                                    <Route path="/section/:id" element={<SectionDetailPageWrapper />} />
                                    <Route path="/section/:id/edit" element={<SectionEditPageWrapper />} />
                                </Routes>
                            </main>
                        </div>
                    </BrowserRouter>
                </ThemeProvider>
            </ThemeModeContext.Provider>
        </Provider>
    );
};


// Wrappers pour injecter l'id depuis l'URL dans les pages detail/edit
const SectionDetailPageWrapper = () => {
    const { id } = useParams();
    if (!id) return <div>Section introuvable</div>;
    return <SectionDetailPage id={id} />;
};
const SectionEditPageWrapper = () => {
    const { id } = useParams();
    if (!id) return <div>Section introuvable</div>;
    return <SectionEditPage id={id} />;
};

export default App;
