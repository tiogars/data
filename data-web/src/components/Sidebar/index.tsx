import { Link, useLocation } from "react-router-dom";
import Drawer from "@mui/material/Drawer";
import Alert from "@mui/material/Alert";
import List from "@mui/material/List";
import ListItem from "@mui/material/ListItem";
import ListItemButton from "@mui/material/ListItemButton";
import ListItemIcon from "@mui/material/ListItemIcon";
import ListItemText from "@mui/material/ListItemText";
import Tooltip from "@mui/material/Tooltip";
import SettingsEthernetIcon from "@mui/icons-material/SettingsEthernet";
import SecurityIcon from "@mui/icons-material/Security";
import { renderMenuItemIcon } from "../../features/menuItem/iconRegistry";
import { useListMenuItemsQuery } from "../../services/menuItemApi";

import type { FC, ReactNode } from "react";

type SidebarMenuItem = {
  to: string;
  label: string;
  icon: ReactNode;
};

const DIRECT_MENU_ITEMS: SidebarMenuItem[] = [
  { to: "/gateway-config", label: "Gateway API", icon: <SettingsEthernetIcon /> },
  { to: "/auth-config", label: "Authentification", icon: <SecurityIcon /> },
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
  const { data, isError } = useListMenuItemsQuery();
  const drawerWidth = open ? DRAWER_EXPANDED_WIDTH : DRAWER_COLLAPSED_WIDTH;
  const dbMenuItems: SidebarMenuItem[] =
    data?.items
      ?.filter((item) => Boolean(item.path) && Boolean(item.label))
      .map((item) => ({
        to: item.path as string,
        label: item.label as string,
        icon: renderMenuItemIcon(item.icon),
      })) ?? [];

  const menuItems = [...dbMenuItems, ...DIRECT_MENU_ITEMS];

  return (
    <Drawer
      variant="permanent"
      open
      onClose={onClose}
      anchor="left"
      sx={{
        width: drawerWidth,
        flexShrink: 0,
        '& .MuiDrawer-paper': {
          width: drawerWidth,
          boxSizing: 'border-box',
          overflowX: 'hidden',
          transition: (theme) =>
            theme.transitions.create('width', {
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
      <List>
        {menuItems.map((item) => (
          <ListItem key={item.to} disablePadding>
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
                  justifyContent: open ? 'initial' : 'center',
                  px: 2.5,
                }}
              >
                <ListItemIcon
                  sx={{
                    minWidth: 0,
                    mr: open ? 2 : 'auto',
                    justifyContent: 'center',
                  }}
                >
                  {item.icon}
                </ListItemIcon>
                <ListItemText
                  primary={item.label}
                  sx={{
                    opacity: open ? 1 : 0,
                    whiteSpace: 'nowrap',
                  }}
                />
              </ListItemButton>
            </Tooltip>
          </ListItem>
        ))}
      </List>
    </Drawer>
  );
};

export default Sidebar;
