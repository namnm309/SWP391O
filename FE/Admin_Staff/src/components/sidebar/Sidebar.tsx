import React, { useState } from "react";
import { Drawer, List, Collapse, IconButton, Box, styled, Typography } from "@mui/material";
import ChevronLeftIcon from "@mui/icons-material/ChevronLeft";
import ChevronRightIcon from "@mui/icons-material/ChevronRight";
import ExpandLess from "@mui/icons-material/ExpandLess";
import ExpandMore from "@mui/icons-material/ExpandMore";
import { menuItemsByRole } from "../../config/menuConfig";
import SidebarItem from "./SidebarItem";
import SidebarSubItem from "./SidebarSubItem";
import { UserRole } from "../../models/user";

const drawerWidth = 240;
const collapsedDrawerWidth = 80;

const BoxCustom = styled(Box)({
  height: "100%",
  margin: "1rem",
  borderRadius: "1rem",
  display: "flex",
  flexDirection: "column",
  overflow: "hidden",
  background: "#fff"
});

const DrawerHeader = styled("div")(({ theme }) => ({
  display: "flex",
  alignItems: "center",
  padding: theme.spacing(0, 1),
  ...theme.mixins.toolbar,
}));

interface SidebarProps {
  role?: UserRole;
  open: boolean;
  onToggle: () => void;
  onSelect: (key: string) => void;
}

const Sidebar = ({ onSelect, role = UserRole.CUSTOMER, open, onToggle }: SidebarProps) => {
  const menuItems = menuItemsByRole[role];

  const [menuOpen, setMenuOpen] = useState<{ [key: string]: boolean }>({
    patients: false,
    management: false,
  });

  const toggleMenu = (menuKey: string) => {
    setMenuOpen((prev) => ({
      ...prev,
      [menuKey]: !prev[menuKey],
    }));
  };

  return (
    <Drawer
      variant="permanent"
      sx={{
        width: open ? drawerWidth : collapsedDrawerWidth,
        flexShrink: 0,
        "& .MuiDrawer-paper": {
          width: open ? drawerWidth : collapsedDrawerWidth,
          height: "100%",
          position: "unset",
          boxSizing: "border-box",
          background: "transparent",
          overflowX: "hidden",
          borderRight: "none",
          transition: (theme) =>
            theme.transitions.create("width", {
              easing: theme.transitions.easing.sharp,
              duration: theme.transitions.duration.enteringScreen,
            }),
        },
      }}
    >
      <BoxCustom>
        <DrawerHeader
          title="Menu"
          sx={{ justifyContent: open ? "space-between" : "center" }}
        >
          {open && (
            <Box sx={{ ml: 2, display: "flex", alignItems: "start", cursor: "default" }}>
              Menu
            </Box>
          )}
          <IconButton onClick={onToggle}>
            {open ? <ChevronLeftIcon /> : <ChevronRightIcon />}
          </IconButton>
        </DrawerHeader>
        <List sx={{ flexGrow: 1, overflow: "auto" }}>
          {menuItems.map((item) => (
            <React.Fragment key={item.key}>
              <SidebarItem
                open={open}
                text={item.label}
                icon={item.icon}
                subIcon={
                  item.subItems
                    ? menuOpen[item.key]
                      ? <ExpandLess />
                      : <ExpandMore />
                    : undefined
                }
                onClick={() => {
                  if (item.subItems) {
                    toggleMenu(item.key);
                  } else {
                    onSelect(item.key);
                  }
                }}
              />
              {item.subItems && (
                <Collapse in={open && menuOpen[item.key]} timeout="auto" unmountOnExit>
                  {item.subItems.map((subItem) => (
                    <SidebarSubItem
                      key={subItem.key}
                      open={open}
                      text={subItem.label}
                      icon={subItem.icon}
                      onClick={() => onSelect(subItem.key)}
                    />
                  ))}
                </Collapse>
              )}
            </React.Fragment>
          ))}
        </List>
      </BoxCustom>
    </Drawer>
  );
};

export default Sidebar;
