import { Link, useLocation } from "react-router-dom";
import { useState } from "react";
import Drawer from "@mui/material/Drawer";
import Alert from "@mui/material/Alert";
import List from "@mui/material/List";
import ListItem from "@mui/material/ListItem";
import ListItemButton from "@mui/material/ListItemButton";
import ListItemIcon from "@mui/material/ListItemIcon";
import ListItemText from "@mui/material/ListItemText";
import Tooltip from "@mui/material/Tooltip";
import Collapse from "@mui/material/Collapse";
import ExpandLessIcon from "@mui/icons-material/ExpandLess";
import ExpandMoreIcon from "@mui/icons-material/ExpandMore";
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
import { renderMenuItemIcon } from "../../features/menuItem/iconRegistry";
import { useListMenuItemsQuery } from "../../services/menuItemApi";
import { useOidcAuth } from "../../auth/OidcAuthProvider";

import type { FC, ReactNode } from "react";

type SidebarMenuItem = {
  to: string;
  label: string;
  icon: ReactNode;
};

type SidebarMenuGroup = {
  id: string;
  label: string;
  icon: ReactNode;
  items: SidebarMenuItem[];
};

const MENU_GROUPS: SidebarMenuGroup[] = [
  {
    id: "business",
    label: "Données métier",
    icon: <StorageIcon />,
    items: [
      { to: "/brick", label: "Bricks", icon: <ToysIcon /> },
      { to: "/model", label: "Modeles", icon: <SchemaIcon /> },
      { to: "/continent", label: "Continents", icon: <PublicIcon /> },
      { to: "/url-manager", label: "Gestion URLs", icon: <LinkIcon /> },
    ],
  },
  {
    id: "interface",
    label: "Interface",
    icon: <PaletteIcon />,
    items: [
      { to: "/url-cards", label: "Cartes accueil", icon: <DashboardCustomizeIcon /> },
    ],
  },
  {
    id: "system",
    label: "Configuration",
    icon: <BuildIcon />,
    items: [
      { to: "/gateway-config", label: "Gateway API", icon: <SettingsEthernetIcon /> },
      { to: "/auth-config", label: "Authentification", icon: <SecurityIcon /> },
      { to: "/server-info/jpa-entities", label: "Entites JPA", icon: <AccountTreeIcon /> },
    ],
  },
  {
    id: "account",
    label: "Compte",
    icon: <ManageAccountsIcon />,
    items: [
      { to: "/auth/account", label: "Mon compte", icon: <ManageAccountsIcon /> },
    ],
  },
];

function isItemSelected(pathname: string, itemPath: string) {
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

  const [expandedGroups, setExpandedGroups] = useState<Set<string>>(
    new Set(["business", "interface"])
  );

  const toggleGroup = (groupId: string) => {
    setExpandedGroups((prev) => {
      const next = new Set(prev);
      if (next.has(groupId)) {
        next.delete(groupId);
      } else {
        next.add(groupId);
      }
      return next;
    });
  };

  const drawerWidth = open ? DRAWER_EXPANDED_WIDTH : DRAWER_COLLAPSED_WIDTH;

  // Convertir les menus de la BD en groupes dynamiques
  const dynamicGroups: SidebarMenuGroup[] =
    data?.items
      ?.filter((item) => Boolean(item.label) && !item.parentId)
      .map((group) => ({
        id: group.id || "",
        label: group.label || "",
        icon: renderMenuItemIcon(group.icon),
        items: (group.children || [])
          .filter((child) => Boolean(child.path) && Boolean(child.label))
          .map((child) => ({
            to: child.path as string,
            label: child.label as string,
            icon: renderMenuItemIcon(child.icon),
          })),
      })) ?? [];

  // Combiner les groupes statiques et dynamiques
  const allGroups: SidebarMenuGroup[] = [
    ...MENU_GROUPS,
    ...dynamicGroups,
  ];

  const renderMenuItemContent = (item: SidebarMenuItem) => (
    <Tooltip
      title={item.label}
      placement="right"
      disableHoverListener={open}
      disableFocusListener={open}
      disableTouchListener={open}
    >
      <ListItemButton
        component={Link}
        to={item.to}
        selected={isItemSelected(location.pathname, item.to)}
        sx={{
          minHeight: 48,
          justifyContent: open ? "initial" : "center",
          px: 2.5,
          pl: open ? 4 : 2.5,
        }}
      >
        <ListItemIcon
          sx={{
            minWidth: 0,
            mr: open ? 2 : "auto",
            justifyContent: "center",
          }}
        >
          {item.icon}
        </ListItemIcon>
        <ListItemText
          primary={item.label}
          sx={{
            opacity: open ? 1 : 0,
            whiteSpace: "nowrap",
          }}
        />
      </ListItemButton>
    </Tooltip>
  );

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
      <List sx={{ width: "100%" }}>
        {allGroups.map((group) => (
          <div key={group.id}>
            {open ? (
              <>
                <ListItemButton
                  onClick={() => toggleGroup(group.id)}
                  sx={{
                    minHeight: 48,
                    px: 2.5,
                  }}
                >
                  <ListItemIcon
                    sx={{
                      minWidth: 0,
                      mr: 2,
                      justifyContent: "center",
                    }}
                  >
                    {group.icon}
                  </ListItemIcon>
                  <ListItemText
                    primary={group.label}
                    slotProps={{
                      primary: {
                        sx: { fontSize: "0.875rem", fontWeight: 500 },
                      }
                    }}
                  />
                  {expandedGroups.has(group.id) ? (
                    <ExpandLessIcon />
                  ) : (
                    <ExpandMoreIcon />
                  )}
                </ListItemButton>
                <Collapse in={expandedGroups.has(group.id)} timeout="auto">
                  <List component="div" disablePadding>
                    {group.items.map((item) => (
                      <ListItem key={item.to} disablePadding>
                        {renderMenuItemContent(item)}
                      </ListItem>
                    ))}
                  </List>
                </Collapse>
              </>
            ) : (
              <>
                {group.items.map((item) => (
                  <ListItem key={item.to} disablePadding>
                    {renderMenuItemContent(item)}
                  </ListItem>
                ))}
              </>
            )}
          </div>
        ))}
      </List>
    </Drawer>
  );
};

export default Sidebar;
