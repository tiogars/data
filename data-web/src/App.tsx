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
import { AuthBaseUrlPage } from "./pages/authConfig/AuthBaseUrlPage";
import { JavaVersionPage } from "./pages/serverInfo/JavaVersionPage";
import { JpaEntitiesPage } from "./pages/serverInfo/JpaEntitiesPage";
import { MenuItemListPage } from "./pages/menuItem/MenuItemListPage";
import { MenuItemCreatePage } from "./pages/menuItem/MenuItemCreatePage";
import { MenuItemDetailPage } from "./pages/menuItem/MenuItemDetailPage";
import { MenuItemEditPage } from "./pages/menuItem/MenuItemEditPage";
import Header from "./components/Header";
import Footer from "./components/Footer";
import ApiErrorSnackbar from "./components/ApiErrorSnackbar";
import "./App.css";
import { useEffect, useMemo, useState } from "react";
import { ThemeProvider, createTheme, CssBaseline } from "@mui/material";
import { Provider } from "react-redux";
import { store } from "./store";
import { OidcAuthProvider } from "./auth/OidcAuthProvider";
import { OidcSigninCallbackPage } from "./pages/auth/OidcSigninCallbackPage";
import { OidcSignoutCallbackPage } from "./pages/auth/OidcSignoutCallbackPage";
import { OidcUserAccountPage } from "./pages/auth/OidcUserAccountPage";
import { UrlManagerPage } from "./pages/urlManager/UrlManagerPage";
import { UrlCardsConfigPage } from "./pages/urlManager/UrlCardsConfigPage";
import { GtinListPage } from "./pages/gtin/GtinListPage";
import { GtinCreatePage } from "./pages/gtin/GtinCreatePage";
import { GtinDetailPage } from "./pages/gtin/GtinDetailPage";
import { GtinEditPage } from "./pages/gtin/GtinEditPage";
import { BrickListPage } from "./pages/brick/BrickListPage";
import { BrickDetailPage } from "./pages/brick/BrickDetailPage";

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
            <OidcAuthProvider>
                <ThemeModeContext.Provider value={providerValue}>
                    <ThemeProvider theme={theme}>
                        <CssBaseline />
                        <ApiErrorSnackbar />
                        <BrowserRouter basename={import.meta.env.BASE_URL}>
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
                                            <Route path="/menu-item" element={<MenuItemListPage />} />
                                            <Route path="/menu-item/create" element={<MenuItemCreatePage />} />
                                            <Route path="/menu-item/:id" element={<MenuItemDetailPageWrapper />} />
                                            <Route path="/menu-item/:id/edit" element={<MenuItemEditPageWrapper />} />
                                            <Route path="/gateway-config" element={<GatewayBaseUrlPage />} />
                                            <Route path="/auth-config" element={<AuthBaseUrlPage />} />
                                            <Route path="/auth/callback" element={<OidcSigninCallbackPage />} />
                                            <Route path="/auth/logout-callback" element={<OidcSignoutCallbackPage />} />
                                            <Route path="/auth/account" element={<OidcUserAccountPage />} />
                                            <Route path="/url-manager" element={<UrlManagerPage />} />
                                            <Route path="/url-cards" element={<UrlCardsConfigPage />} />
                                            <Route path="/gtin" element={<GtinListPage />} />
                                            <Route path="/gtin/create" element={<GtinCreatePage />} />
                                            <Route path="/gtin/:id" element={<GtinDetailPageWrapper />} />
                                            <Route path="/gtin/:id/edit" element={<GtinEditPageWrapper />} />
                                            <Route path="/brick" element={<BrickListPage />} />
                                            <Route path="/brick/:id" element={<BrickDetailPageWrapper />} />
                                            <Route path="/server-info/java-version" element={<JavaVersionPage />} />
                                            <Route path="/server-info/jpa-entities" element={<JpaEntitiesPage />} />
                                        </Routes>
                                    </main>
                                    <Footer />
                                </div>
                            </div>
                        </BrowserRouter>
                    </ThemeProvider>
                </ThemeModeContext.Provider>
            </OidcAuthProvider>
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

const MenuItemDetailPageWrapper = () => {
    const { id } = useParams();
    if (!id) return <div>Entree de menu introuvable</div>;
    return <MenuItemDetailPage id={id} />;
};

const MenuItemEditPageWrapper = () => {
    const { id } = useParams();
    if (!id) return <div>Entree de menu introuvable</div>;
    return <MenuItemEditPage id={id} />;
};

const GtinDetailPageWrapper = () => {
    const { id } = useParams();
    if (!id) return <div>GTIN introuvable</div>;
    return <GtinDetailPage id={id} />;
};

const BrickDetailPageWrapper = () => {
    const { id } = useParams();
    if (!id) return <div>Brick introuvable</div>;
    return <BrickDetailPage id={id} />;
};

const GtinEditPageWrapper = () => {
    const { id } = useParams();
    if (!id) return <div>GTIN introuvable</div>;
    return <GtinEditPage id={id} />;
};

export default App;
