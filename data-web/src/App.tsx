import { BrowserRouter, Navigate, Route, Routes, useParams } from "react-router-dom";
import HomePage from "./pages/HomePage";
import Sidebar from "./components/Sidebar";
import { SectionCreatePage } from "./pages/section/SectionCreatePage";
import { SectionDetailPage } from "./pages/section/SectionDetailPage";
import { SectionEditPage } from "./pages/section/SectionEditPage";
import { SectionDocsSettingsPage } from "./pages/section/SectionDocsSettingsPage";
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
import { WingetListPage } from "./pages/winget/WingetListPage";
import { WingetCreatePage } from "./pages/winget/WingetCreatePage";
import { WingetDetailPage } from "./pages/winget/WingetDetailPage";
import { WingetEditPage } from "./pages/winget/WingetEditPage";
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
import { AppellationListPage } from "./pages/appellation/AppellationListPage";
import { AppellationCreatePage } from "./pages/appellation/AppellationCreatePage";
import { AppellationDetailPage } from "./pages/appellation/AppellationDetailPage";
import { AppellationEditPage } from "./pages/appellation/AppellationEditPage";
import { CouleurListPage } from "./pages/couleur/CouleurListPage";
import { CouleurCreatePage } from "./pages/couleur/CouleurCreatePage";
import { CouleurDetailPage } from "./pages/couleur/CouleurDetailPage";
import { CouleurEditPage } from "./pages/couleur/CouleurEditPage";
import { CepageListPage } from "./pages/cepage/CepageListPage";
import { CepageCreatePage } from "./pages/cepage/CepageCreatePage";
import { CepageDetailPage } from "./pages/cepage/CepageDetailPage";
import { CepageEditPage } from "./pages/cepage/CepageEditPage";
import { CirconstanceListPage } from "./pages/circonstance/CirconstanceListPage";
import { CirconstanceCreatePage } from "./pages/circonstance/CirconstanceCreatePage";
import { CirconstanceDetailPage } from "./pages/circonstance/CirconstanceDetailPage";
import { CirconstanceEditPage } from "./pages/circonstance/CirconstanceEditPage";
import { TypeVinListPage } from "./pages/typeVin/TypeVinListPage";
import { TypeVinCreatePage } from "./pages/typeVin/TypeVinCreatePage";
import { TypeVinDetailPage } from "./pages/typeVin/TypeVinDetailPage";
import { TypeVinEditPage } from "./pages/typeVin/TypeVinEditPage";
import { VinTagListPage } from "./pages/vinTag/VinTagListPage";
import { VinTagCreatePage } from "./pages/vinTag/VinTagCreatePage";
import { VinTagDetailPage } from "./pages/vinTag/VinTagDetailPage";
import { VinTagEditPage } from "./pages/vinTag/VinTagEditPage";
import { MaisonListPage } from "./pages/maison/MaisonListPage";
import { MaisonCreatePage } from "./pages/maison/MaisonCreatePage";
import { MaisonDetailPage } from "./pages/maison/MaisonDetailPage";
import { MaisonEditPage } from "./pages/maison/MaisonEditPage";
import { ContenantListPage } from "./pages/contenant/ContenantListPage";
import { ContenantCreatePage } from "./pages/contenant/ContenantCreatePage";
import { ContenantDetailPage } from "./pages/contenant/ContenantDetailPage";
import { ContenantEditPage } from "./pages/contenant/ContenantEditPage";
import { VinNomListPage } from "./pages/vinNom/VinNomListPage";
import { VinNomCreatePage } from "./pages/vinNom/VinNomCreatePage";
import { VinNomDetailPage } from "./pages/vinNom/VinNomDetailPage";
import { VinNomEditPage } from "./pages/vinNom/VinNomEditPage";
import { VinListPage } from "./pages/vin/VinListPage";
import { VinCreatePage } from "./pages/vin/VinCreatePage";
import { VinDetailPage } from "./pages/vin/VinDetailPage";
import { VinEditPage } from "./pages/vin/VinEditPage";

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
                                            <Route path="/section/settings/docs" element={<SectionDocsSettingsPage />} />
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
                                            <Route path="/winget" element={<Navigate to="/winget/list" replace />} />
                                            <Route path="/winget/list" element={<WingetListPage />} />
                                            <Route path="/winget/create" element={<WingetCreatePage />} />
                                            <Route path="/winget/:id" element={<WingetDetailPageWrapper />} />
                                            <Route path="/winget/:id/edit" element={<WingetEditPageWrapper />} />
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
                                            <Route path="/appellation" element={<Navigate to="/appellation/list" replace />} />
                                            <Route path="/appellation/list" element={<AppellationListPage />} />
                                            <Route path="/appellation/create" element={<AppellationCreatePage />} />
                                            <Route path="/appellation/:id" element={<AppellationDetailPageWrapper />} />
                                            <Route path="/appellation/:id/edit" element={<AppellationEditPageWrapper />} />
                                            <Route path="/couleur" element={<Navigate to="/couleur/list" replace />} />
                                            <Route path="/couleur/list" element={<CouleurListPage />} />
                                            <Route path="/couleur/create" element={<CouleurCreatePage />} />
                                            <Route path="/couleur/:id" element={<CouleurDetailPageWrapper />} />
                                            <Route path="/couleur/:id/edit" element={<CouleurEditPageWrapper />} />
                                            <Route path="/cepage" element={<Navigate to="/cepage/list" replace />} />
                                            <Route path="/cepage/list" element={<CepageListPage />} />
                                            <Route path="/cepage/create" element={<CepageCreatePage />} />
                                            <Route path="/cepage/:id" element={<CepageDetailPageWrapper />} />
                                            <Route path="/cepage/:id/edit" element={<CepageEditPageWrapper />} />
                                            <Route path="/circonstance" element={<Navigate to="/circonstance/list" replace />} />
                                            <Route path="/circonstance/list" element={<CirconstanceListPage />} />
                                            <Route path="/circonstance/create" element={<CirconstanceCreatePage />} />
                                            <Route path="/circonstance/:id" element={<CirconstanceDetailPageWrapper />} />
                                            <Route path="/circonstance/:id/edit" element={<CirconstanceEditPageWrapper />} />
                                            <Route path="/type-vin" element={<Navigate to="/type-vin/list" replace />} />
                                            <Route path="/type-vin/list" element={<TypeVinListPage />} />
                                            <Route path="/type-vin/create" element={<TypeVinCreatePage />} />
                                            <Route path="/type-vin/:id" element={<TypeVinDetailPageWrapper />} />
                                            <Route path="/type-vin/:id/edit" element={<TypeVinEditPageWrapper />} />
                                            <Route path="/vin-tag" element={<Navigate to="/vin-tag/list" replace />} />
                                            <Route path="/vin-tag/list" element={<VinTagListPage />} />
                                            <Route path="/vin-tag/create" element={<VinTagCreatePage />} />
                                            <Route path="/vin-tag/:id" element={<VinTagDetailPageWrapper />} />
                                            <Route path="/vin-tag/:id/edit" element={<VinTagEditPageWrapper />} />
                                            <Route path="/maison" element={<Navigate to="/maison/list" replace />} />
                                            <Route path="/maison/list" element={<MaisonListPage />} />
                                            <Route path="/maison/create" element={<MaisonCreatePage />} />
                                            <Route path="/maison/:id" element={<MaisonDetailPageWrapper />} />
                                            <Route path="/maison/:id/edit" element={<MaisonEditPageWrapper />} />
                                            <Route path="/contenant" element={<Navigate to="/contenant/list" replace />} />
                                            <Route path="/contenant/list" element={<ContenantListPage />} />
                                            <Route path="/contenant/create" element={<ContenantCreatePage />} />
                                            <Route path="/contenant/:id" element={<ContenantDetailPageWrapper />} />
                                            <Route path="/contenant/:id/edit" element={<ContenantEditPageWrapper />} />
                                            <Route path="/vin-nom" element={<Navigate to="/vin-nom/list" replace />} />
                                            <Route path="/vin-nom/list" element={<VinNomListPage />} />
                                            <Route path="/vin-nom/create" element={<VinNomCreatePage />} />
                                            <Route path="/vin-nom/:id" element={<VinNomDetailPageWrapper />} />
                                            <Route path="/vin-nom/:id/edit" element={<VinNomEditPageWrapper />} />
                                            <Route path="/vin" element={<Navigate to="/vin/list" replace />} />
                                            <Route path="/vin/list" element={<VinListPage />} />
                                            <Route path="/vin/create" element={<VinCreatePage />} />
                                            <Route path="/vin/:id" element={<VinDetailPageWrapper />} />
                                            <Route path="/vin/:id/edit" element={<VinEditPageWrapper />} />
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

const WingetDetailPageWrapper = () => {
    const { id } = useParams();
    if (!id) return <div>Application Winget introuvable</div>;
    return <WingetDetailPage id={id} />;
};

const WingetEditPageWrapper = () => {
    const { id } = useParams();
    if (!id) return <div>Application Winget introuvable</div>;
    return <WingetEditPage id={id} />;
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


const AppellationDetailPageWrapper = () => { const { id } = useParams(); if (!id) return <div>Appellation introuvable</div>; return <AppellationDetailPage id={id} />; };
const AppellationEditPageWrapper = () => { const { id } = useParams(); if (!id) return <div>Appellation introuvable</div>; return <AppellationEditPage id={id} />; };
const CouleurDetailPageWrapper = () => { const { id } = useParams(); if (!id) return <div>Couleur introuvable</div>; return <CouleurDetailPage id={id} />; };
const CouleurEditPageWrapper = () => { const { id } = useParams(); if (!id) return <div>Couleur introuvable</div>; return <CouleurEditPage id={id} />; };
const CepageDetailPageWrapper = () => { const { id } = useParams(); if (!id) return <div>Cepage introuvable</div>; return <CepageDetailPage id={id} />; };
const CepageEditPageWrapper = () => { const { id } = useParams(); if (!id) return <div>Cepage introuvable</div>; return <CepageEditPage id={id} />; };
const CirconstanceDetailPageWrapper = () => { const { id } = useParams(); if (!id) return <div>Circonstance introuvable</div>; return <CirconstanceDetailPage id={id} />; };
const CirconstanceEditPageWrapper = () => { const { id } = useParams(); if (!id) return <div>Circonstance introuvable</div>; return <CirconstanceEditPage id={id} />; };
const TypeVinDetailPageWrapper = () => { const { id } = useParams(); if (!id) return <div>Type de vin introuvable</div>; return <TypeVinDetailPage id={id} />; };
const TypeVinEditPageWrapper = () => { const { id } = useParams(); if (!id) return <div>Type de vin introuvable</div>; return <TypeVinEditPage id={id} />; };
const VinTagDetailPageWrapper = () => { const { id } = useParams(); if (!id) return <div>Tag de vin introuvable</div>; return <VinTagDetailPage id={id} />; };
const VinTagEditPageWrapper = () => { const { id } = useParams(); if (!id) return <div>Tag de vin introuvable</div>; return <VinTagEditPage id={id} />; };
const MaisonDetailPageWrapper = () => { const { id } = useParams(); if (!id) return <div>Maison introuvable</div>; return <MaisonDetailPage id={id} />; };
const MaisonEditPageWrapper = () => { const { id } = useParams(); if (!id) return <div>Maison introuvable</div>; return <MaisonEditPage id={id} />; };
const ContenantDetailPageWrapper = () => { const { id } = useParams(); if (!id) return <div>Contenant introuvable</div>; return <ContenantDetailPage id={id} />; };
const ContenantEditPageWrapper = () => { const { id } = useParams(); if (!id) return <div>Contenant introuvable</div>; return <ContenantEditPage id={id} />; };
const VinNomDetailPageWrapper = () => { const { id } = useParams(); if (!id) return <div>Nom de vin introuvable</div>; return <VinNomDetailPage id={id} />; };
const VinNomEditPageWrapper = () => { const { id } = useParams(); if (!id) return <div>Nom de vin introuvable</div>; return <VinNomEditPage id={id} />; };
const VinDetailPageWrapper = () => { const { id } = useParams(); if (!id) return <div>Vin introuvable</div>; return <VinDetailPage id={id} />; };
const VinEditPageWrapper = () => { const { id } = useParams(); if (!id) return <div>Vin introuvable</div>; return <VinEditPage id={id} />; };

export default App;
