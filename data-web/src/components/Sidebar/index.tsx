import { Link, useLocation } from "react-router-dom";
import { useEffect, useMemo, useState } from "react";
import Drawer from "@mui/material/Drawer";
import useMediaQuery from "@mui/material/useMediaQuery";
import { useTheme } from "@mui/material/styles";
import Alert from "@mui/material/Alert";
import Tooltip from "@mui/material/Tooltip";
import { SimpleTreeView } from "@mui/x-tree-view/SimpleTreeView";
import { TreeItem, treeItemClasses } from "@mui/x-tree-view/TreeItem";
import Box from "@mui/material/Box";
import { renderMenuItemIcon } from "../../features/menuItem/iconRegistry";
import { useListMenuItemsQuery } from "../../services/menuItemApi";
import { useOidcAuth } from "../../auth/OidcAuthProvider";

import type { FC, ReactNode } from "react";
import type { MenuItem } from "../../services/menuItemApi";

type TreeItemData = {
  id: string;
  label: string;
  icon: ReactNode;
  path?: string;
  children?: TreeItemData[];
};

const canonicalMenuPathMap: Record<string, string> = {
  "/section": "/section/list",
  "/footer-link": "/footer-link/list",
  "/menu-item": "/menu-item/list",
  "/gtin": "/gtin/list",
  "/android": "/android/list",
  "/brand": "/brand/list",
  "/model": "/model/list",
  "/car": "/car/list",
  "/car-mileage": "/car-mileage/table",
  "/continent": "/continent/list",
  "/brick": "/brick/list",
  "/github-repository": "/github-repository/search",
  "/github-token-config": "/github-token-config/search",
};

function normalizeMenuPath(path?: string): string | undefined {
  if (!path) {
    return undefined;
  }

  const trimmed = path.trim();
  if (!trimmed) {
    return undefined;
  }

  if (trimmed !== "/" && trimmed.endsWith("/")) {
    const withoutTrailingSlash = trimmed.slice(0, -1);
    return canonicalMenuPathMap[withoutTrailingSlash] ?? withoutTrailingSlash;
  }

  return canonicalMenuPathMap[trimmed] ?? trimmed;
}

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
  const theme = useTheme();
  const isMobile = useMediaQuery(theme.breakpoints.down("md"));
  const location = useLocation();
  const { isAuthenticated } = useOidcAuth();
  const { data, isError, isLoading } = useListMenuItemsQuery(undefined, {
    skip: !isAuthenticated,
    refetchOnMountOrArgChange: true,
    refetchOnFocus: true,
  });

  const [expandedNodeIds, setExpandedNodeIds] = useState<string[]>([]);

  const drawerWidth = open ? DRAWER_EXPANDED_WIDTH : DRAWER_COLLAPSED_WIDTH;

  const dynamicItems: TreeItemData[] = useMemo(() => {
    if (!data?.items) return [];

    const toTreeItemData = (item: MenuItem): TreeItemData | null => {
      if (!item.id || !item.label) {
        return null;
      }

      const children = (item.children ?? [])
        .map(toTreeItemData)
        .filter((child): child is TreeItemData => child !== null);

      return {
        id: item.id,
        label: item.label,
        icon: renderMenuItemIcon(item.icon),
        path: normalizeMenuPath(item.path),
        children,
      };
    };

    return data.items
      .map(toTreeItemData)
      .filter((item): item is TreeItemData => item !== null);
  }, [data?.items]);

  useEffect(() => {
    if (dynamicItems.length === 0) {
      return;
    }

    setExpandedNodeIds((previousIds) => {
      if (previousIds.length > 0) {
        return previousIds;
      }
      return dynamicItems
        .filter((item) => (item.children ?? []).length > 0)
        .map((item) => item.id);
    });
  }, [dynamicItems]);

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
          onClick={isMobile ? onClose : undefined}
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

  const renderTreeItem = (item: TreeItemData): ReactNode => {
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
      variant={isMobile ? "temporary" : "permanent"}
      open={isMobile ? open : true}
      onClose={onClose}
      anchor="left"
      sx={{
        width: isMobile ? 0 : drawerWidth,
        flexShrink: 0,
        "& .MuiDrawer-paper": {
          width: isMobile ? DRAWER_EXPANDED_WIDTH : drawerWidth,
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
          Menu dynamique indisponible, le menu lateral ne peut pas etre affiche.
        </Alert>
      )}
      {!isLoading && !isError && dynamicItems.length === 0 && (
        <Alert severity="info" sx={{ m: 1 }}>
          Aucune entree de menu disponible. Verifiez la configuration /menu-item.
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
        {dynamicItems.map((item) => renderTreeItem(item))}
      </SimpleTreeView>
    </Drawer>
  );
};

export default Sidebar;
