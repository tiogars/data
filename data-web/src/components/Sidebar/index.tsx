import { Link, useLocation } from "react-router-dom";
import { useMemo, useState } from "react";
import Drawer from "@mui/material/Drawer";
import useMediaQuery from "@mui/material/useMediaQuery";
import { useTheme } from "@mui/material/styles";
import Alert from "@mui/material/Alert";
import Tooltip from "@mui/material/Tooltip";
import Box from "@mui/material/Box";
import List from "@mui/material/List";
import ListItem from "@mui/material/ListItem";
import ListItemButton from "@mui/material/ListItemButton";
import ListItemIcon from "@mui/material/ListItemIcon";
import ListItemText from "@mui/material/ListItemText";
import Collapse from "@mui/material/Collapse";
import ExpandLess from "@mui/icons-material/ExpandLess";
import ExpandMore from "@mui/icons-material/ExpandMore";
import { renderMenuItemIcon } from "../../features/menuItem/iconRegistry";
import { useListMenuItemsQuery } from "../../services/menuItemApi";
import { useOidcAuth } from "../../auth/OidcAuthProvider";

import type { FC, ReactNode } from "react";
import type { MenuItem } from "../../services/menuItemApi";

type SidebarItemData = {
  id: string;
  label: string;
  icon: ReactNode;
  path?: string;
  children?: SidebarItemData[];
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
  "/user-account": "/user-account/list",
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

function findExpandedGroupIdsForPath(
  items: SidebarItemData[],
  pathname: string,
  ancestorGroupIds: string[] = [],
): string[] | null {
  for (const item of items) {
    const hasChildren = (item.children ?? []).length > 0;
    const nextAncestorGroupIds = hasChildren
      ? [...ancestorGroupIds, item.id]
      : ancestorGroupIds;

    if (item.path && isItemSelected(pathname, item.path)) {
      return hasChildren ? nextAncestorGroupIds : ancestorGroupIds;
    }

    if (!hasChildren) {
      continue;
    }

    const childExpandedGroupIds = findExpandedGroupIdsForPath(
      item.children ?? [],
      pathname,
      nextAncestorGroupIds,
    );

    if (childExpandedGroupIds) {
      return childExpandedGroupIds;
    }
  }

  return null;
}

function findGroupBranchIds(
  items: SidebarItemData[],
  itemId: string,
  ancestorGroupIds: string[] = [],
): string[] | null {
  for (const item of items) {
    const hasChildren = (item.children ?? []).length > 0;
    const nextAncestorGroupIds = hasChildren
      ? [...ancestorGroupIds, item.id]
      : ancestorGroupIds;

    if (hasChildren && item.id === itemId) {
      return nextAncestorGroupIds;
    }

    if (!hasChildren) {
      continue;
    }

    const childBranchIds = findGroupBranchIds(
      item.children ?? [],
      itemId,
      nextAncestorGroupIds,
    );

    if (childBranchIds) {
      return childBranchIds;
    }
  }

  return null;
}

function branchContainsSelectedItem(item: SidebarItemData, pathname: string): boolean {
  if (item.path && isItemSelected(pathname, item.path)) {
    return true;
  }

  return (item.children ?? []).some((child) =>
    branchContainsSelectedItem(child, pathname),
  );
}

function resolveExpandedGroupIds(
  expandedGroupState: { pathname: string; ids: string[] },
  pathname: string,
  selectedExpandedGroupIds: string[] | null,
) {
  if (expandedGroupState.pathname !== pathname) {
    return selectedExpandedGroupIds ?? expandedGroupState.ids;
  }

  if (expandedGroupState.ids.length > 0 || selectedExpandedGroupIds === null) {
    return expandedGroupState.ids;
  }

  return selectedExpandedGroupIds;
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

  const [expandedGroupState, setExpandedGroupState] = useState<{
    pathname: string;
    ids: string[];
  }>({
    pathname: location.pathname,
    ids: [],
  });

  const drawerWidth = open ? DRAWER_EXPANDED_WIDTH : DRAWER_COLLAPSED_WIDTH;

  const dynamicItems: SidebarItemData[] = useMemo(() => {
    if (!data?.items) return [];

    const toSidebarItemData = (item: MenuItem): SidebarItemData | null => {
      if (!item.id || !item.label) {
        return null;
      }

      const children = (item.children ?? [])
        .map(toSidebarItemData)
        .filter((child): child is SidebarItemData => child !== null);

      return {
        id: item.id,
        label: item.label,
        icon: renderMenuItemIcon(item.icon),
        path: normalizeMenuPath(item.path),
        children,
      };
    };

    return data.items
      .map(toSidebarItemData)
      .filter((item): item is SidebarItemData => item !== null);
  }, [data?.items]);

  const selectedExpandedGroupIds = useMemo(
    () => findExpandedGroupIdsForPath(dynamicItems, location.pathname),
    [dynamicItems, location.pathname],
  );

  const expandedGroupIds = resolveExpandedGroupIds(
    expandedGroupState,
    location.pathname,
    selectedExpandedGroupIds,
  );

  const handleGroupToggle = (itemId: string) => {
    const expandedIndex = expandedGroupIds.indexOf(itemId);
    const nextIds =
      expandedIndex >= 0
        ? expandedGroupIds.slice(0, expandedIndex)
        : findGroupBranchIds(dynamicItems, itemId) ?? expandedGroupIds;

    setExpandedGroupState({
      pathname: location.pathname,
      ids: nextIds,
    });
  };

  const handleLeafClick = (itemPath: string | undefined, ancestorGroupIds: string[]) => {
    setExpandedGroupState({
      pathname: itemPath ?? location.pathname,
      ids: ancestorGroupIds,
    });

    if (isMobile) {
      onClose();
    }
  };

  const renderLeafItem = (
    item: SidebarItemData,
    depth: number,
    ancestorGroupIds: string[],
  ) => {
    const isSelected = item.path ? isItemSelected(location.pathname, item.path) : false;

    return (
      <ListItem key={item.id} disablePadding sx={{ display: "block" }}>
        <Tooltip
          title={item.label}
          placement="right"
          disableHoverListener={open}
          disableFocusListener={open}
          disableTouchListener={open}
        >
          <ListItemButton
            component={item.path ? Link : "button"}
            to={item.path}
            onClick={() => handleLeafClick(item.path, ancestorGroupIds)}
            selected={isSelected}
            sx={{
              minHeight: 48,
              borderRadius: 1,
              mx: open ? 0.5 : 0.25,
              my: 0.25,
              pl: open ? 2 + depth * 2 : 1.25,
              pr: open ? 1.5 : 1.25,
              justifyContent: open ? "initial" : "center",
            }}
          >
            <ListItemIcon
              sx={{
                minWidth: 0,
                mr: open ? 1.5 : 0,
                justifyContent: "center",
              }}
            >
              {item.icon}
            </ListItemIcon>
            {open && (
              <ListItemText
                primary={item.label}
                sx={{
                  "& .MuiListItemText-primary": {
                    fontSize: "0.875rem",
                    whiteSpace: "nowrap",
                    overflow: "hidden",
                    textOverflow: "ellipsis",
                  },
                }}
              />
            )}
          </ListItemButton>
        </Tooltip>
      </ListItem>
    );
  };

  const renderGroupItem = (
    item: SidebarItemData,
    depth: number,
    ancestorGroupIds: string[],
  ) => {
    const isExpanded = expandedGroupIds.includes(item.id);
    const isSelectedBranch = branchContainsSelectedItem(item, location.pathname);
    const nextAncestorGroupIds = [...ancestorGroupIds, item.id];

    return (
      <ListItem key={item.id} disablePadding sx={{ display: "block" }}>
        <Tooltip
          title={item.label}
          placement="right"
          disableHoverListener={open}
          disableFocusListener={open}
          disableTouchListener={open}
        >
          <ListItemButton
            onClick={() => handleGroupToggle(item.id)}
            selected={isSelectedBranch}
            sx={{
              minHeight: 48,
              borderRadius: 1,
              mx: open ? 0.5 : 0.25,
              my: 0.25,
              pl: open ? 1.5 + depth * 2 : 1.25,
              pr: open ? 1.5 : 1.25,
              justifyContent: open ? "initial" : "center",
            }}
          >
            <ListItemIcon
              sx={{
                minWidth: 0,
                mr: open ? 1.5 : 0,
                justifyContent: "center",
              }}
            >
              {item.icon}
            </ListItemIcon>
            {open && (
              <>
                <ListItemText
                  primary={item.label}
                  sx={{
                    "& .MuiListItemText-primary": {
                      fontSize: "0.875rem",
                      fontWeight: 500,
                      whiteSpace: "nowrap",
                      overflow: "hidden",
                      textOverflow: "ellipsis",
                    },
                  }}
                />
                {isExpanded ? <ExpandLess /> : <ExpandMore />}
              </>
            )}
          </ListItemButton>
        </Tooltip>
        <Collapse in={isExpanded} timeout="auto" unmountOnExit>
          <List disablePadding>
            {renderMenuItems(item.children ?? [], depth + 1, nextAncestorGroupIds)}
          </List>
        </Collapse>
      </ListItem>
    );
  };

  const renderMenuItems = (
    items: SidebarItemData[],
    depth = 0,
    ancestorGroupIds: string[] = [],
  ): ReactNode =>
    items.map((item) => {
      const hasChildren = (item.children ?? []).length > 0;
      return hasChildren
        ? renderGroupItem(item, depth, ancestorGroupIds)
        : renderLeafItem(item, depth, ancestorGroupIds);
    });

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
      <Box sx={{ width: "100%", overflowX: "hidden", p: open ? 0.5 : 0.25 }}>
        <List disablePadding>{renderMenuItems(dynamicItems)}</List>
      </Box>
    </Drawer>
  );
};

export default Sidebar;
