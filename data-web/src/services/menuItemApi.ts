import { emptySplitApi as api } from "./emptyApi";
const injectedRtkApi = api.injectEndpoints({
    endpoints: (build) => ({
        getMenuItemById: build.query<
            GetMenuItemByIdApiResponse,
            GetMenuItemByIdApiArg
        >({
            query: (queryArg) => ({ url: `/menu-item/${queryArg.id}` }),
        }),
        updateMenuItem: build.mutation<
            UpdateMenuItemApiResponse,
            UpdateMenuItemApiArg
        >({
            query: (queryArg) => ({
                url: `/menu-item/${queryArg.id}`,
                method: "PUT",
                body: queryArg.menuItem,
            }),
        }),
        deleteMenuItemById: build.mutation<
            DeleteMenuItemByIdApiResponse,
            DeleteMenuItemByIdApiArg
        >({
            query: (queryArg) => ({
                url: `/menu-item/${queryArg.id}`,
                method: "DELETE",
            }),
        }),
        listMenuItems: build.query<
            ListMenuItemsApiResponse,
            ListMenuItemsApiArg
        >({
            query: () => ({ url: `/menu-item` }),
        }),
        createMenuItem: build.mutation<
            CreateMenuItemApiResponse,
            CreateMenuItemApiArg
        >({
            query: (queryArg) => ({
                url: `/menu-item`,
                method: "POST",
                body: queryArg.menuItemCreationForm,
            }),
        }),
        deleteAllMenuItems: build.mutation<
            DeleteAllMenuItemsApiResponse,
            DeleteAllMenuItemsApiArg
        >({
            query: () => ({ url: `/menu-item`, method: "DELETE" }),
        }),
    }),
    overrideExisting: false,
});
export { injectedRtkApi as menuItemApi };
export type GetMenuItemByIdApiResponse = /** status 200 OK */ MenuItem;
export type GetMenuItemByIdApiArg = {
    id: string;
};
export type UpdateMenuItemApiResponse = /** status 200 OK */ MenuItem;
export type UpdateMenuItemApiArg = {
    id: string;
    menuItem: MenuItem;
};
export type DeleteMenuItemByIdApiResponse = unknown;
export type DeleteMenuItemByIdApiArg = {
    id: string;
};
export type ListMenuItemsApiResponse =
    /** status 200 OK */ MenuItemListResponse;
export type ListMenuItemsApiArg = void;
export type CreateMenuItemApiResponse = /** status 200 OK */ MenuItem;
export type CreateMenuItemApiArg = {
    menuItemCreationForm: MenuItemCreationForm;
};
export type DeleteAllMenuItemsApiResponse = unknown;
export type DeleteAllMenuItemsApiArg = void;
export type MenuItem = {
    /** L'identifiant unique de l'entree de menu. */
    id?: string;
    /** Le libelle affiche dans le menu. */
    label?: string;
    /** Le chemin de navigation React Router. */
    path?: string;
    /** La cle d'icone MUI utilisee par l'application web. */
    icon?: string;
    /** L'ordre d'affichage de l'entree dans le menu. */
    displayOrder?: number;
    /** Indique si l'entree a ete chargee automatiquement au premier demarrage. */
    defaultLoaded?: boolean;
    /** L'identifiant du menu parent pour la hierarchie. */
    parentId?: string;
    /** Les sous-elements de menu. */
    children?: MenuItem[];
};
export type MenuItemListResponse = {
    items?: MenuItem[];
    count?: number;
};
export type MenuItemCreationForm = {
    /** Le libelle affiche dans le menu. */
    label?: string;
    /** Le chemin de navigation React Router. */
    path?: string;
    /** La cle d'icone MUI utilisee par l'application web. */
    icon?: string;
    /** L'ordre d'affichage de l'entree dans le menu. */
    displayOrder?: number;
    /** L'identifiant du menu parent pour la hierarchie. */
    parentId?: string;
};
export const {
    useGetMenuItemByIdQuery,
    useUpdateMenuItemMutation,
    useDeleteMenuItemByIdMutation,
    useListMenuItemsQuery,
    useCreateMenuItemMutation,
    useDeleteAllMenuItemsMutation,
} = injectedRtkApi;
