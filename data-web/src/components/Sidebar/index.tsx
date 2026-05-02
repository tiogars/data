import { Link, useLocation } from "react-router-dom";
import Drawer from "@mui/material/Drawer";
import List from "@mui/material/List";
import ListItem from "@mui/material/ListItem";
import ListItemButton from "@mui/material/ListItemButton";
import ListItemIcon from "@mui/material/ListItemIcon";
import ListItemText from "@mui/material/ListItemText";
import Tooltip from "@mui/material/Tooltip";
import InboxIcon from "@mui/icons-material/Inbox";
import LinkIcon from "@mui/icons-material/Link";
import GitHubIcon from "@mui/icons-material/GitHub";
import KeyIcon from "@mui/icons-material/Key";
import MemoryIcon from "@mui/icons-material/Memory";

const menuItems = [
  { to: "/section", label: "Sections", icon: <InboxIcon /> },
  { to: "/footer-link", label: "Liens footer", icon: <LinkIcon /> },
  { to: "/github-repository", label: "Repositories GitHub", icon: <GitHubIcon /> },
  { to: "/github-token-config", label: "Token GitHub REST", icon: <KeyIcon /> },
  { to: "/server-info/java-version", label: "Version Java serveur", icon: <MemoryIcon /> },
];


import type { FC } from "react";


interface SidebarProps {
  open: boolean;
  onClose: () => void;
}

const DRAWER_EXPANDED_WIDTH = 220;
const DRAWER_COLLAPSED_WIDTH = 64;

const Sidebar: FC<SidebarProps> = ({ open, onClose }) => {
  const location = useLocation();
  const drawerWidth = open ? DRAWER_EXPANDED_WIDTH : DRAWER_COLLAPSED_WIDTH;

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
                selected={location.pathname === item.to}
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
