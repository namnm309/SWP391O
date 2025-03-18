import React, { useState, useEffect } from "react";
import {
  Box,
  Typography,
  Table,
  TableBody,
  TableCell,
  TableContainer,
  TableHead,
  TableRow,
  Paper,
  Button,
  TextField,
  Dialog,
  DialogTitle,
  DialogContent,
  DialogActions,
  IconButton,
  InputAdornment,
} from "@mui/material";
import AddIcon from "@mui/icons-material/Add";
import EditIcon from "@mui/icons-material/Edit";
import DeleteIcon from "@mui/icons-material/Delete";
import SearchIcon from "@mui/icons-material/Search";
import { useStore } from "../../store";
import { Patient } from "../../models/user";

const UsersManagement: React.FC = () => {
  const [users, setUsers] = useState<Patient[]>([]);
  const [dialogOpen, setDialogOpen] = useState(false);
  const [currentUser, setCurrentUser] = useState<Patient | null>(null);
  const [dialogMode, setDialogMode] = useState<"add" | "edit">("add");
  const [searchTerm, setSearchTerm] = useState("");

  const fetchAllUsers = useStore((state) => state.fetchAllUsers);

  useEffect(() => {
    fetchAllUsers().then((data) => {
      const mappedUsers: Patient[] = data.map((p) => ({
        ...p,
        email: (p as any).email || "",
      }));
      setUsers(mappedUsers);
    });
  }, [fetchAllUsers]);

  const handleOpenDialog = (mode: "add" | "edit", user?: Patient) => {
    setDialogMode(mode);
    setCurrentUser(user || null);
    setDialogOpen(true);
  };

  const handleCloseDialog = () => {
    setDialogOpen(false);
    setCurrentUser(null);
  };

  const handleSaveUser = () => {
    if (currentUser) {
      if (dialogMode === "add") {
        setUsers([...users, { ...currentUser, id: users.length + 1 }]);
      } else {
        setUsers(users.map((u) => (u.id === currentUser.id ? currentUser : u)));
      }
    }
    handleCloseDialog();
  };

  const handleDeleteUser = (id: number) => {
    setUsers(users.filter((u) => u.id !== id));
  };

  const filteredUsers = users.filter(
    (user) =>
      user.username.toLowerCase().includes(searchTerm.toLowerCase()) ||
      user.email.toLowerCase().includes(searchTerm.toLowerCase())
  );

  return (
    <Box sx={{ flexGrow: 1, p: 3 }}>
      <Typography variant="h4" component="h2" gutterBottom>
        User Management
      </Typography>
      <Box sx={{ display: "flex", justifyContent: "space-between", mb: 3, gap: 4 }}>
        <TextField
          variant="outlined"
          placeholder="Search users..."
          value={searchTerm}
          onChange={(e) => setSearchTerm(e.target.value)}
          slotProps={{
            input: {
              startAdornment: (
                <InputAdornment position="start">
                  <SearchIcon />
                </InputAdornment>
              )
            }
          }}
          sx={{ flex: 1 }}
        />
        <Button variant="contained" color="primary" startIcon={<AddIcon />} onClick={() => handleOpenDialog("add")}>
          Add User
        </Button>
      </Box>
      <TableContainer component={Paper}>
        <Table>
          <TableHead>
            <TableRow>
              <TableCell>Username</TableCell>
              <TableCell>Full name</TableCell>
              <TableCell>Email</TableCell>
              <TableCell>Role</TableCell>
              <TableCell>Actions</TableCell>
            </TableRow>
          </TableHead>
          <TableBody>
            {filteredUsers.map((user) => (
              <TableRow key={user.id}>
                <TableCell>{user.username}</TableCell>
                <TableCell>{user.fullName}</TableCell>
                <TableCell>{user.email}</TableCell>
                <TableCell>{user.role}</TableCell>
                <TableCell>
                  <IconButton color="primary" onClick={() => handleOpenDialog("edit", user)}>
                    <EditIcon />
                  </IconButton>
                  <IconButton color="error" onClick={() => handleDeleteUser(user.id)}>
                    <DeleteIcon />
                  </IconButton>
                </TableCell>
              </TableRow>
            ))}
          </TableBody>
        </Table>
      </TableContainer>

      <Dialog open={dialogOpen} onClose={handleCloseDialog}>
        <DialogTitle>{dialogMode === "add" ? "Add User" : "Edit User"}</DialogTitle>
        <DialogContent>
          <TextField
            autoFocus
            margin="dense"
            label="Username"
            type="text"
            fullWidth
            value={currentUser?.username || ""}
            onChange={(e) => setCurrentUser({ ...currentUser!, username: e.target.value })}
          />
          <TextField
            margin="dense"
            label="Email"
            type="email"
            fullWidth
            value={currentUser?.email || ""}
            onChange={(e) => setCurrentUser({ ...currentUser!, email: e.target.value })}
          />
          <TextField
            margin="dense"
            label="Role"
            type="text"
            fullWidth
            value={currentUser?.role || ""}
          />
        </DialogContent>
        <DialogActions>
          <Button onClick={handleCloseDialog}>Cancel</Button>
          <Button onClick={handleSaveUser} color="primary">
            Save
          </Button>
        </DialogActions>
      </Dialog>
    </Box>
  );
};

export default UsersManagement;
