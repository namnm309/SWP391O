import React from "react";
import ListItem from "@mui/material/ListItem";
import ListItemIcon from '@mui/material/ListItemIcon';
import ListItemText from '@mui/material/ListItemText';
import Tooltip from "@mui/material/Tooltip";

interface SidebarItemProps {
  open: boolean;
  text: string;
  icon: JSX.Element;
  onClick: () => void;
  subIcon?: JSX.Element;
}

const SidebarItem: React.FC<SidebarItemProps> = ({ open, text, icon, onClick, subIcon }) => {
  return (
    <Tooltip title={open ? "" : text} placement="right" arrow>
      <ListItem
        onClick={onClick}
        sx={{
          px: open ? 2 : 1,
          py: 1,
          minHeight: 48,
          justifyContent: open ? "initial" : "center",
          cursor: "pointer"
        }}
      >
        <ListItemIcon
          sx={{
            minWidth: 0,
            mr: open ? 2 : 0,
            justifyContent: "center",
          }}
        >
          {icon}
        </ListItemIcon>
        <ListItemText 
          primary={text} 
          sx={{ 
            opacity: open ? 1 : 0, 
            display: open ? "flex" : "none"  
          }} 
        />
        {subIcon && open && (
          <div style={{ marginLeft: "auto" }}>
            {subIcon}
          </div>
        )}
      </ListItem>
    </Tooltip>
  );
};

export default SidebarItem;
