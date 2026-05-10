import { Link, useLocation } from "react-router-dom";
import { useState, useMemo } from "react";
import Drawer from "@mui/material/Drawer";
import Alert from "@mui/material/Alert";
import Tooltip from "@mui/material/Tooltip";
import { SimpleTreeView } from "@mui/x-tree-view/SimpleTreeView";
import { TreeItem, treeItemClasses } from "@mui/x-tree-view/TreeItem";
import SettingsEthernetIcon from "@mui/icons-material/SettingsEthernet";
import SecurityIcon from "@mui/icons-material/Security";
import AccountTreeIcon from "@mui/icons-material/AccountTree";
import ManageAccountsIcon from "@mui/icons-material/ManageAccounts";
import LinkIcon from "@mui/icons-material/Link";
import DashboardCustomizeIcon from "@mui/icons-material/DashboardCustomize";
import SchemaIcon from "@mui/icons-material/Schema";
import PublicIcon from "@mui/icons-material/Public";
import ToysIcon from "@mui/icons-material/Toys";
import StorageIcon from "@mui/icons-material/Storage";
import BuildIcon from "@mui/icons-material/Build";
import PaletteIcon from "@mui/icons-material/Palette";
import Box from "@mui/material/Box";
import { renderMenuItemIcon } from "../../features/menuItem/iconRegistry";
import { useListMenuItemsQuery } from "../../services/menuItemApi";
import { useOidcAuth } from "../../auth/OidcAuthProvider";

import type { FC, ReactNode } from "react";

type TreeItemData = {
  id: string;
  label: string;
  icon: ReactNode;
  path?: string;
  children?: TreeItemData[];
};

const MENU_GROUPS: TreeItemData[] = [
  {
    id: "business",
    label: "Données métier",
    icon: <StorageIcon />,
    children: [
      { id: "brick", label: "Bricks", icon: <ToysIcon />, path: "/brick" },
      { id: "model", label: "Modeles", icon: <SchemaIcon />, path: "/model" },
      { id: "continent", label: "Continents", icon: <PublicIcon />, path: "/continent" },
      { id: "url-manager", label: "Gestion URLs", icon: <LinkIcon />, path: "/url-manager" },
    ],
  },
  {
    id: "interface",
    label: "Interface",
    icon: <PaletteIcon />,
    children: [
      { id: "url-cards", label: "Cartes accueil", icon: <DashboardCustomizeIcon />, path: "/url-cards" },
    ],
  },
  {
    id: "system",
    label: "Configuration",
    icon: <BuildIcon />,
    children: [
      { id: "gateway-config", label: "Gateway API", icon: <SettingsEthernetIcon />, path: "/gateway-config" },
      { id: "auth-config", label: "Authentification", icon: <SecurityIcon />, path: "/auth-config" },
      { id: "jpa-entities", label: "Entites JPA", icon: <AccountTreeIcon />, path: "/server-info/jpa-entities" },
    ],
  },
  {
    id: "account",
    label: "Compte",
    icon: <ManageAccountsIcon />,
    children: [
      { id: "my-account", label: "Mon compte", icon: <ManageAccountsIcon />, path: "/auth/account" },
    ],
  },
];

function isItemSelected(pathname: string, itemPath?: string) {
  if (!itemPath) return false;
  return pathname === itemPath || pathname.startsWith(`${itemPath}/`);
}


interface SidebarProps {
  open: boolean;
  onClose: () => void;
}

const DRAWER_EXPANDED_WIDTH = 220;
const DRAWER_COLLAPSED_WIDTH = 64;

const Sidebar: FC<SidebarProps> = ({ open, onClose }) => {
  const location = useLocation();
  const { isAuthenticated } = useOidcAuth();
  const { data, isError } = useListMenuItemsQuery(undefined, {
    skip: !isAuthenticated,
    refetchOnMountOrArgChange: true,
    refetchOnFocus: true,
  });

  const [expandedNodeIds, setExpandedNodeIds] = useState<string[]>(
    ["business", "interface"]
  );

  const drawerWidth = open ? DRAWER_EXPANDED_WIDTH : DRAWER_COLLAPSED_WIDTH;

  // Convertir les items dynamiques de la BD en structure TreeItemData
  const dynamicItems: TreeItemData[] = useMemo(() => {
    if (!data?.items) return [];

    // Créer une map pour un accès rapide
    const itemMap = new Map<string, TreeItemData>();
    
    data.items.forEach((item) => {
      if (!item.id || !item.label) return;
      itemMap.set(item.id, {
        id: item.id,
        label: item.label,
        icon: renderMenuItemIcon(item.icon),
        path: item.path || undefined,
        children: [],
      });
    });

    // Construire la hiérarchie
    data.items.forEach((item) => {
      if (!item.id || !item.parentId) return;
      const parent = itemMap.get(item.parentId);
      const child = itemMap.get(item.id);
      if (parent && child) {
        parent.children = parent.children || [];
        parent.children.push(child);
      }
    });

    // Retourner les items racine (sans parentId)
    return Array.from(itemMap.values()).filter(
      (item) => !data.items?.find((i) => i.id === item.id)?.parentId
    );
  }, [data?.items]);

  // Fusionner les items statiques et dynamiques
  const allTreeItems: TreeItemData[] = [...MENU_GROUPS, ...dynamicItems];

  const renderTreeItemContent = (item: TreeItemData) => {
    const isSelected = item.path ? isItemSelected(location.pathname, item.path) : false;
    
    if (!item.path) {
      // Item de groupe - afficher juste l'étiquette avec icône
      return (
        <Box sx={{ display: "flex", alignItems: "center", gap: 1 }}>
          {item.icon}
          {open && (
            <span style={{ fontSize: "0.875rem", fontWeight: 500 }}>
              {item.label}
            </span>
          )}
        </Box>
      );
    }

    // Item avec route - afficher comme lien
    return (
      <Tooltip
        title={item.label}
        placement="right"
        disableHoverListener={open}
        disableFocusListener={open}
        disableTouchListener={open}
      >
        <Link
          to={item.path}
          style={{
            textDecoration: "none",
            color: "inherit",
            display: "flex",
            alignItems: "center",
            gap: open ? 8 : 0,
            width: "100%",
            backgroundColor: isSelected ? "rgba(33, 150, 243, 0.08)" : "transparent",
            borderRadius: 4,
            padding: "4px 8px",
          }}
        >
          {item.icon}
          {open && (
            <span style={{ fontSize: "0.875rem", whiteSpace: "nowrap" }}>
              {item.label}
            </span>
          )}
        </Link>
      </Tooltip>
    );
  };

  const renderTreeItem = (item: TreeItemData): React.ReactNode => {
    const hasChildren = (item.children ?? []).length > 0;

    return (
      <TreeItem
        key={item.id}
        itemId={item.id}
        label={renderTreeItemContent(item)}
        sx={{
          minHeight: 48,
          [`& .${treeItemClasses.content}`]: {
            padding: 0,
            margin: open ? "4px 0" : "0px",
          },
        }}
      >
        {hasChildren ? (item.children ?? []).map((child) => renderTreeItem(child)) : null}
      </TreeItem>
    );
  };

  return (
    <Drawer
      variant="permanent"
      open
      onClose={onClose}
      anchor="left"
      sx={{
        width: drawerWidth,
        flexShrink: 0,
        "& .MuiDrawer-paper": {
          width: drawerWidth,
          boxSizing: "border-box",
          overflowX: "hidden",
          transition: (theme) =>
            theme.transitions.create("width", {
              easing: theme.transitions.easing.sharp,
              duration: theme.transitions.duration.standard,
            }),
        },
      }}
    >
      {isError && (
        <Alert severity="warning" sx={{ m: 1 }}>
          Menu dynamique indisponible, seules les entrees systeme sont affichees.
        </Alert>
      )}
      <SimpleTreeView
        expandedItems={expandedNodeIds}
        onExpandedItemsChange={(_event, nodeIds) =>
          setExpandedNodeIds(nodeIds)
        }
        sx={{
          width: "100%",
          overflowX: "hidden",
          p: open ? 0.5 : 0.25,
          "& .MuiTreeItem-root": {
            margin: 0,
          },
          "& .MuiTreeItem-group": {
            marginLeft: open ? 12 : 4,
          },
          "& .MuiTreeItem-iconContainer": {
            minWidth: 32,
            display: "flex",
            justifyContent: "center",
            mr: open ? 1 : 0,
          },
          "& .MuiTreeItem-label": {
            py: 0,
          },
        }}
      >
        {allTreeItems.map((item) => renderTreeItem(item))}
      </SimpleTreeView>
    </Drawer>
  );
};

export default Sidebar;
