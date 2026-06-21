import { BrowserRouter, Navigate, Route, Routes, useParams } from "react-router-dom";
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
import { GitHubRepositorySearchPage } from "./pages/githubRepository/GitHubRepositoryListPage";
import { GitHubTokenConfigSearchPage } from "./pages/githubRestConfig/GitHubTokenConfigPage";
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
import { AndroidListPage } from "./pages/android/AndroidListPage";
import { AndroidCreatePage } from "./pages/android/AndroidCreatePage";
import { AndroidDetailPage } from "./pages/android/AndroidDetailPage";
import { AndroidEditPage } from "./pages/android/AndroidEditPage";
import { AndroidReleaseListPage } from "./pages/android/AndroidReleaseListPage";
import { BrandListPage } from "./pages/brand/BrandListPage";
import { BrandCreatePage } from "./pages/brand/BrandCreatePage";
import { BrandDetailPage } from "./pages/brand/BrandDetailPage";
import { BrandEditPage } from "./pages/brand/BrandEditPage";
import { ModelListPage } from "./pages/model/ModelListPage";
import { ModelCreatePage } from "./pages/model/ModelCreatePage";
import { ModelDetailPage } from "./pages/model/ModelDetailPage";
import { ModelEditPage } from "./pages/model/ModelEditPage";
import { ContinentListPage } from "./pages/continent/ContinentListPage";
import { ContinentCreatePage } from "./pages/continent/ContinentCreatePage";
import { ContinentDetailPage } from "./pages/continent/ContinentDetailPage";
import { ContinentEditPage } from "./pages/continent/ContinentEditPage";
import { BrickListPage } from "./pages/brick/BrickListPage";
import { BrickDetailPage } from "./pages/brick/BrickDetailPage";
import { BrickExternalLinksSettingsPage } from "./pages/brick/BrickExternalLinksSettingsPage";
import { UserAccountListPage } from "./pages/userAccount/UserAccountListPage";
import { UserAccountCreatePage } from "./pages/userAccount/UserAccountCreatePage";
import { UserAccountDetailPage } from "./pages/userAccount/UserAccountDetailPage";
import { UserAccountEditPage } from "./pages/userAccount/UserAccountEditPage";
import { CarDashboardPage } from "./pages/car/CarDashboardPage";
import { CarListPage } from "./pages/car/CarListPage";
import { CarCreatePage } from "./pages/car/CarCreatePage";
import { CarEditPage } from "./pages/car/CarEditPage";
import { CarMileageTablePage } from "./pages/carMileage/CarMileageTablePage";
import { CarMileageFormPage } from "./pages/carMileage/CarMileageFormPage";
import { IconGalleryPage } from "./pages/icon/IconGalleryPage";

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
                                            <Route path="/section" element={<Navigate to="/section/list" replace />} />
                                            <Route path="/section/list" element={<SectionListPage />} />
                                            <Route path="/section/create" element={<SectionCreatePage />} />
                                            <Route path="/section/:id" element={<SectionDetailPageWrapper />} />
                                            <Route path="/section/:id/edit" element={<SectionEditPageWrapper />} />
                                            <Route path="/footer-link" element={<Navigate to="/footer-link/list" replace />} />
                                            <Route path="/footer-link/list" element={<FooterLinkListPage />} />
                                            <Route path="/footer-link/create" element={<FooterLinkCreatePage />} />
                                            <Route path="/footer-link/:id" element={<FooterLinkDetailPageWrapper />} />
                                            <Route path="/footer-link/:id/edit" element={<FooterLinkEditPageWrapper />} />
                                            <Route path="/github-repository" element={<Navigate to="/github-repository/search" replace />} />
                                            <Route path="/github-repository/search" element={<GitHubRepositorySearchPage />} />
                                            <Route path="/github-repository/create" element={<GitHubRepositoryCreatePage />} />
                                            <Route path="/github-repository/:id" element={<GitHubRepositoryDetailPageWrapper />} />
                                            <Route path="/github-repository/:id/edit" element={<GitHubRepositoryEditPageWrapper />} />
                                            <Route path="/github-token-config" element={<Navigate to="/github-token-config/search" replace />} />
                                            <Route path="/github-token-config/search" element={<GitHubTokenConfigSearchPage />} />
                                            <Route path="/menu-item" element={<Navigate to="/menu-item/list" replace />} />
                                            <Route path="/menu-item/list" element={<MenuItemListPage />} />
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
                                            <Route path="/gtin" element={<Navigate to="/gtin/list" replace />} />
                                            <Route path="/gtin/list" element={<GtinListPage />} />
                                            <Route path="/gtin/create" element={<GtinCreatePage />} />
                                            <Route path="/gtin/:id" element={<GtinDetailPageWrapper />} />
                                            <Route path="/gtin/:id/edit" element={<GtinEditPageWrapper />} />
                                            <Route path="/android" element={<Navigate to="/android/list" replace />} />
                                            <Route path="/android/list" element={<AndroidListPage />} />
                                            <Route path="/android/releases" element={<AndroidReleaseListPage />} />
                                            <Route path="/android/create" element={<AndroidCreatePage />} />
                                            <Route path="/android/:id" element={<AndroidDetailPageWrapper />} />
                                            <Route path="/android/:id/edit" element={<AndroidEditPageWrapper />} />
                                            <Route path="/brand" element={<Navigate to="/brand/list" replace />} />
                                            <Route path="/brand/list" element={<BrandListPage />} />
                                            <Route path="/brand/create" element={<BrandCreatePage />} />
                                            <Route path="/brand/:id" element={<BrandDetailPageWrapper />} />
                                            <Route path="/brand/:id/edit" element={<BrandEditPageWrapper />} />
                                            <Route path="/model" element={<Navigate to="/model/list" replace />} />
                                            <Route path="/model/list" element={<ModelListPage />} />
                                            <Route path="/model/create" element={<ModelCreatePage />} />
                                            <Route path="/model/:id" element={<ModelDetailPageWrapper />} />
                                            <Route path="/model/:id/edit" element={<ModelEditPageWrapper />} />
                                            <Route path="/continent" element={<Navigate to="/continent/list" replace />} />
                                            <Route path="/continent/list" element={<ContinentListPage />} />
                                            <Route path="/continent/create" element={<ContinentCreatePage />} />
                                            <Route path="/continent/:id" element={<ContinentDetailPageWrapper />} />
                                            <Route path="/continent/:id/edit" element={<ContinentEditPageWrapper />} />
                                            <Route path="/brick" element={<Navigate to="/brick/list" replace />} />
                                            <Route path="/brick/list" element={<BrickListPage />} />
                                            <Route path="/brick/settings/external-links" element={<BrickExternalLinksSettingsPage />} />
                                            <Route path="/brick/:id" element={<BrickDetailPageWrapper />} />
                                            <Route path="/user-account" element={<Navigate to="/user-account/list" replace />} />
                                            <Route path="/user-account/list" element={<UserAccountListPage />} />
                                            <Route path="/user-account/create" element={<UserAccountCreatePage />} />
                                            <Route path="/user-account/:id" element={<UserAccountDetailPageWrapper />} />
                                            <Route path="/user-account/:id/edit" element={<UserAccountEditPageWrapper />} />
                                            <Route path="/car" element={<Navigate to="/car/list" replace />} />
                                            <Route path="/car/dashboard" element={<CarDashboardPage />} />
                                            <Route path="/car/list" element={<CarListPage />} />
                                            <Route path="/car/create" element={<CarCreatePage />} />
                                            <Route path="/car/:id/edit" element={<CarEditPageWrapper />} />
                                            <Route path="/car-mileage" element={<Navigate to="/car-mileage/table" replace />} />
                                            <Route path="/car-mileage/table" element={<CarMileageTablePage />} />
                                            <Route path="/car-mileage/form" element={<CarMileageFormPage />} />
                                            <Route path="/server-info/java-version" element={<JavaVersionPage />} />
                                            <Route path="/server-info/jpa-entities" element={<JpaEntitiesPage />} />
                                            <Route path="/icon-gallery" element={<IconGalleryPage />} />
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

const AndroidDetailPageWrapper = () => {
    const { id } = useParams();
    if (!id) return <div>Application Android introuvable</div>;
    return <AndroidDetailPage id={id} />;
};

const AndroidEditPageWrapper = () => {
    const { id } = useParams();
    if (!id) return <div>Application Android introuvable</div>;
    return <AndroidEditPage id={id} />;
};

const BrandDetailPageWrapper = () => {
    const { id } = useParams();
    if (!id) return <div>Marque introuvable</div>;
    return <BrandDetailPage id={id} />;
};

const BrandEditPageWrapper = () => {
    const { id } = useParams();
    if (!id) return <div>Marque introuvable</div>;
    return <BrandEditPage id={id} />;
};

const ModelDetailPageWrapper = () => {
    const { id } = useParams();
    if (!id) return <div>Modele introuvable</div>;
    return <ModelDetailPage id={id} />;
};

const ModelEditPageWrapper = () => {
    const { id } = useParams();
    if (!id) return <div>Modele introuvable</div>;
    return <ModelEditPage id={id} />;
};

const ContinentDetailPageWrapper = () => {
    const { id } = useParams();
    if (!id) return <div>Continent introuvable</div>;
    return <ContinentDetailPage id={id} />;
};

const ContinentEditPageWrapper = () => {
    const { id } = useParams();
    if (!id) return <div>Continent introuvable</div>;
    return <ContinentEditPage id={id} />;
};

const CarEditPageWrapper = () => {
    const { id } = useParams();
    if (!id) return <div>Voiture introuvable</div>;
    return <CarEditPage id={id} />;
};

const UserAccountDetailPageWrapper = () => {
    const { id } = useParams();
    if (!id) return <div>Compte utilisateur introuvable</div>;
    return <UserAccountDetailPage id={id} />;
};

const UserAccountEditPageWrapper = () => {
    const { id } = useParams();
    if (!id) return <div>Compte utilisateur introuvable</div>;
    return <UserAccountEditPage id={id} />;
};

export default App;
