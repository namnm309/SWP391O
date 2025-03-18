import React from "react";
import { ListItem, ListItemIcon, ListItemText, Tooltip } from "@mui/material";

interface SidebarSubItemProps {
  open: boolean;
  text: string;
  icon: JSX.Element;
  onClick: () => void;
}

const SidebarSubItem: React.FC<SidebarSubItemProps> = ({ open, text, icon, onClick }) => {
  return (
    <Tooltip title={open ? "" : text} placement="right" arrow>
      <ListItem
        onClick={onClick}
        sx={{
          pl: open ? 4 : 1,
          py: 1,
          minHeight: 40,
          justifyContent: open ? "initial" : "center",
          cursor: "pointer"
        }}
      >
        <ListItemIcon
          sx={{
            minWidth: 0,
            mr: open ? 2 : "auto",
            justifyContent: "center",
          }}
        >
          {icon}
        </ListItemIcon>
        <ListItemText primary={text} sx={{ opacity: open ? 1 : 0 }} />
      </ListItem>
    </Tooltip>
  );
};

export default SidebarSubItem;
