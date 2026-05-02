import { BrowserRouter, Route, Routes, useParams } from "react-router-dom";
import HomePage from "./pages/HomePage";
import Sidebar from "./components/Sidebar";
import { SectionCreatePage } from "./pages/section/SectionCreatePage";
import { SectionDetailPage } from "./pages/section/SectionDetailPage";
import { SectionEditPage } from "./pages/section/SectionEditPage";
import { SectionListPage } from "./pages/section/SectionListPage";
import { FooterLinkCreatePage } from "./pages/footerLink/FooterLinkCreatePage";
import { FooterLinkDetailPage } from "./pages/footerLink/FooterLinkDetailPage";
import { FooterLinkEditPage } from "./pages/footerLink/FooterLinkEditPage";
import { FooterLinkListPage } from "./pages/footerLink/FooterLinkListPage";
import { GitHubRepositoryCreatePage } from "./pages/githubRepository/GitHubRepositoryCreatePage";
import { GitHubRepositoryDetailPage } from "./pages/githubRepository/GitHubRepositoryDetailPage";
import { GitHubRepositoryEditPage } from "./pages/githubRepository/GitHubRepositoryEditPage";
import { GitHubRepositoryListPage } from "./pages/githubRepository/GitHubRepositoryListPage";
import { GitHubTokenConfigPage } from "./pages/githubRestConfig/GitHubTokenConfigPage";
import { GatewayBaseUrlPage } from "./pages/gatewayConfig/GatewayBaseUrlPage";
import { JavaVersionPage } from "./pages/serverInfo/JavaVersionPage";
import Header from "./components/Header";
import Footer from "./components/Footer";
import "./App.css";
import { useEffect, useMemo, useState } from "react";
import { ThemeProvider, createTheme, CssBaseline } from "@mui/material";
import { Provider } from "react-redux";
import { store } from "./store";

import { ThemeModeContext } from "./themeModeHook";



export type ThemeMode = "light" | "dark" | "system";
// Voir themeModeHook.ts pour le contexte

const DRAWER_STATE_STORAGE_KEY = "sidebar-open";

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

    const [drawerOpen, setDrawerOpen] = useState(() => {
        const saved = localStorage.getItem(DRAWER_STATE_STORAGE_KEY);
        if (saved === null) return true;
        return saved === "true";
    });

    useEffect(() => {
        localStorage.setItem(DRAWER_STATE_STORAGE_KEY, String(drawerOpen));
    }, [drawerOpen]);

    return (
        <Provider store={store}>
            <ThemeModeContext.Provider value={providerValue}>
                <ThemeProvider theme={theme}>
                    <CssBaseline />
                    <BrowserRouter>
                        <div className="app-layout">
                            <Sidebar open={drawerOpen} onClose={() => setDrawerOpen(false)} />
                            <div className="app-content">
                                <main className="app-main">
                                    <Header onMenuClick={() => setDrawerOpen((v) => !v)} />
                                    <Routes>
                                        <Route path="/" element={<HomePage />} />
                                        <Route path="/section" element={<SectionListPage />} />
                                        <Route path="/section/create" element={<SectionCreatePage />} />
                                        <Route path="/section/:id" element={<SectionDetailPageWrapper />} />
                                        <Route path="/section/:id/edit" element={<SectionEditPageWrapper />} />
                                        <Route path="/footer-link" element={<FooterLinkListPage />} />
                                        <Route path="/footer-link/create" element={<FooterLinkCreatePage />} />
                                        <Route path="/footer-link/:id" element={<FooterLinkDetailPageWrapper />} />
                                        <Route path="/footer-link/:id/edit" element={<FooterLinkEditPageWrapper />} />
                                        <Route path="/github-repository" element={<GitHubRepositoryListPage />} />
                                        <Route path="/github-repository/create" element={<GitHubRepositoryCreatePage />} />
                                        <Route path="/github-repository/:id" element={<GitHubRepositoryDetailPageWrapper />} />
                                        <Route path="/github-repository/:id/edit" element={<GitHubRepositoryEditPageWrapper />} />
                                        <Route path="/github-token-config" element={<GitHubTokenConfigPage />} />
                                        <Route path="/gateway-config" element={<GatewayBaseUrlPage />} />
                                        <Route path="/server-info/java-version" element={<JavaVersionPage />} />
                                    </Routes>
                                </main>
                                <Footer />
                            </div>
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

const FooterLinkDetailPageWrapper = () => {
    const { id } = useParams();
    if (!id) return <div>Lien footer introuvable</div>;
    return <FooterLinkDetailPage id={id} />;
};

const FooterLinkEditPageWrapper = () => {
    const { id } = useParams();
    if (!id) return <div>Lien footer introuvable</div>;
    return <FooterLinkEditPage id={id} />;
};

const GitHubRepositoryDetailPageWrapper = () => {
    const { id } = useParams();
    if (!id) return <div>Repository GitHub introuvable</div>;
    return <GitHubRepositoryDetailPage id={id} />;
};

const GitHubRepositoryEditPageWrapper = () => {
    const { id } = useParams();
    if (!id) return <div>Repository GitHub introuvable</div>;
    return <GitHubRepositoryEditPage id={id} />;
};

export default App;
