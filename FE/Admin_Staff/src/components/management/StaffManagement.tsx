import { useState } from "react"
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
} from "@mui/material"
import AddIcon from "@mui/icons-material/Add"
import EditIcon from "@mui/icons-material/Edit"
import DeleteIcon from "@mui/icons-material/Delete"

interface Staff {
  id: number
  name: string
  position: string
  email: string
  phone: string
}

const initialStaff: Staff[] = [
  { id: 1, name: "Dr. John Smith", position: "Doctor", email: "john.smith@example.com", phone: "123-456-7890" },
  { id: 2, name: "Nurse Jane Doe", position: "Nurse", email: "jane.doe@example.com", phone: "987-654-3210" },
  {
    id: 3,
    name: "Admin Bob Johnson",
    position: "Administrator",
    email: "bob.johnson@example.com",
    phone: "456-789-0123",
  },
]

const StaffManagement = () => {
  const [staff, setStaff] = useState<Staff[]>(initialStaff)
  const [dialogOpen, setDialogOpen] = useState(false)
  const [currentStaff, setCurrentStaff] = useState<Staff | null>(null)
  const [dialogMode, setDialogMode] = useState<"add" | "edit">("add")

  const handleOpenDialog = (mode: "add" | "edit", staffMember?: Staff) => {
    setDialogMode(mode)
    setCurrentStaff(staffMember || { id: 0, name: "", position: "", email: "", phone: "" })
    setDialogOpen(true)
  }

  const handleCloseDialog = () => {
    setDialogOpen(false)
    setCurrentStaff(null)
  }

  const handleSaveStaff = () => {
    if (currentStaff) {
      if (dialogMode === "add") {
        setStaff([...staff, { ...currentStaff, id: staff.length + 1 }])
      } else {
        setStaff(staff.map((s) => (s.id === currentStaff.id ? currentStaff : s)))
      }
    }
    handleCloseDialog()
  }

  const handleDeleteStaff = (id: number) => {
    setStaff(staff.filter((s) => s.id !== id))
  }

  return (
    <Box sx={{ flexGrow: 1, p: 3 }}>
      <Typography variant="h4" component="h2" gutterBottom>
        Staff Management
      </Typography>
      <Button
        variant="contained"
        color="primary"
        startIcon={<AddIcon />}
        onClick={() => handleOpenDialog("add")}
        sx={{ mb: 2 }}
      >
        Add Staff
      </Button>
      <TableContainer component={Paper}>
        <Table>
          <TableHead>
            <TableRow>
              <TableCell>Name</TableCell>
              <TableCell>Position</TableCell>
              <TableCell>Email</TableCell>
              <TableCell>Phone</TableCell>
              <TableCell>Actions</TableCell>
            </TableRow>
          </TableHead>
          <TableBody>
            {staff.map((s) => (
              <TableRow key={s.id}>
                <TableCell>{s.name}</TableCell>
                <TableCell>{s.position}</TableCell>
                <TableCell>{s.email}</TableCell>
                <TableCell>{s.phone}</TableCell>
                <TableCell>
                  <IconButton color="primary" onClick={() => handleOpenDialog("edit", s)}>
                    <EditIcon />
                  </IconButton>
                  <IconButton color="error" onClick={() => handleDeleteStaff(s.id)}>
                    <DeleteIcon />
                  </IconButton>
                </TableCell>
              </TableRow>
            ))}
          </TableBody>
        </Table>
      </TableContainer>

      <Dialog open={dialogOpen} onClose={handleCloseDialog}>
        <DialogTitle>{dialogMode === "add" ? "Add Staff" : "Edit Staff"}</DialogTitle>
        <DialogContent>
          <TextField
            autoFocus
            margin="dense"
            label="Name"
            type="text"
            fullWidth
            value={currentStaff?.name || ""}
            onChange={(e) => setCurrentStaff({ ...currentStaff!, name: e.target.value })}
          />
          <TextField
            margin="dense"
            label="Position"
            type="text"
            fullWidth
            value={currentStaff?.position || ""}
            onChange={(e) => setCurrentStaff({ ...currentStaff!, position: e.target.value })}
          />
          <TextField
            margin="dense"
            label="Email"
            type="email"
            fullWidth
            value={currentStaff?.email || ""}
            onChange={(e) => setCurrentStaff({ ...currentStaff!, email: e.target.value })}
          />
          <TextField
            margin="dense"
            label="Phone"
            type="tel"
            fullWidth
            value={currentStaff?.phone || ""}
            onChange={(e) => setCurrentStaff({ ...currentStaff!, phone: e.target.value })}
          />
        </DialogContent>
        <DialogActions>
          <Button onClick={handleCloseDialog}>Cancel</Button>
          <Button onClick={handleSaveStaff} color="primary">
            Save
          </Button>
        </DialogActions>
      </Dialog>
    </Box>
  )
}

export default StaffManagement

