"use client"

import type React from "react"
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
  TextField,
  InputAdornment,
  IconButton,
  Pagination,
  Button,
  Dialog,
  DialogTitle,
  DialogContent,
  DialogActions,
  FormControl,
  InputLabel,
  Select,
  MenuItem,
} from "@mui/material"
import SearchIcon from "@mui/icons-material/Search"
import AddIcon from "@mui/icons-material/Add"
import EditIcon from "@mui/icons-material/Edit"
import DeleteIcon from "@mui/icons-material/Delete"

interface Booking {
  id: number
  patientName: string
  date: string
  time: string
  status: "Scheduled" | "Completed" | "Cancelled"
}

const initialBookings: Booking[] = [
  { id: 1, patientName: "John Doe", date: "2023-05-15", time: "09:00", status: "Scheduled" },
  { id: 2, patientName: "Jane Smith", date: "2023-05-16", time: "10:30", status: "Completed" },
  { id: 3, patientName: "Alice Johnson", date: "2023-05-17", time: "14:00", status: "Cancelled" },
  { id: 4, patientName: "Bob Williams", date: "2023-05-18", time: "11:15", status: "Scheduled" },
  { id: 5, patientName: "Emma Brown", date: "2023-05-19", time: "15:45", status: "Scheduled" },
]

const BookingList: React.FC = () => {
  const [bookings, setBookings] = useState<Booking[]>(initialBookings)
  const [searchTerm, setSearchTerm] = useState("")
  const [page, setPage] = useState(1)
  const [dialogOpen, setDialogOpen] = useState(false)
  const [currentBooking, setCurrentBooking] = useState<Booking | null>(null)
  const [dialogMode, setDialogMode] = useState<"add" | "edit" | "delete">("add")

  const bookingsPerPage = 5

  const filteredBookings = bookings.filter((booking) =>
    booking.patientName.toLowerCase().includes(searchTerm.toLowerCase()),
  )

  const paginatedBookings = filteredBookings.slice((page - 1) * bookingsPerPage, page * bookingsPerPage)

  const handleOpenDialog = (mode: "add" | "edit" | "delete", booking?: Booking) => {
    setDialogMode(mode)
    setCurrentBooking(booking || null)
    setDialogOpen(true)
  }

  const handleCloseDialog = () => {
    setDialogOpen(false)
    setCurrentBooking(null)
  }

  const handleSaveBooking = () => {
    if (currentBooking) {
      if (dialogMode === "add") {
        setBookings([...bookings, { ...currentBooking, id: bookings.length + 1 }])
      } else {
        setBookings(bookings.map((b) => (b.id === currentBooking.id ? currentBooking : b)))
      }
    }
    handleCloseDialog()
  }

  const handleDeleteBooking = () => {
    if (currentBooking) {
      setBookings(bookings.filter((b) => b.id !== currentBooking.id))
    }
    handleCloseDialog()
  }

  const handleStatusChange = (bookingId: number, newStatus: Booking["status"]) => {
    setBookings(bookings.map((booking) => (booking.id === bookingId ? { ...booking, status: newStatus } : booking)))
  }

  return (
    <Box sx={{ flexGrow: 1, p: 3 }}>
      <Typography variant="h4" component="h2" gutterBottom>
        Booking List
      </Typography>
      <Box sx={{ display: "flex", justifyContent: "space-between", mb: 3 }}>
        <TextField
          variant="outlined"
          placeholder="Search bookings..."
          value={searchTerm}
          onChange={(e) => setSearchTerm(e.target.value)}
          slotProps={{
            input: {
              startAdornment: (
                <InputAdornment position="start">
                  <SearchIcon />
                </InputAdornment>
              ),
            }
          }}
          sx={{ width: "70%" }}
        />
        <Button variant="contained" color="primary" startIcon={<AddIcon />} onClick={() => handleOpenDialog("add")}>
          Add Booking
        </Button>
      </Box>
      <TableContainer component={Paper}>
        <Table>
          <TableHead>
            <TableRow>
              <TableCell>Patient Name</TableCell>
              <TableCell>Date</TableCell>
              <TableCell>Time</TableCell>
              <TableCell>Status</TableCell>
              <TableCell>Actions</TableCell>
            </TableRow>
          </TableHead>
          <TableBody>
            {paginatedBookings.map((booking) => (
              <TableRow key={booking.id}>
                <TableCell>{booking.patientName}</TableCell>
                <TableCell>{booking.date}</TableCell>
                <TableCell>{booking.time}</TableCell>
                <TableCell>
                  <FormControl size="small">
                    <Select
                      value={booking.status}
                      onChange={(e) => handleStatusChange(booking.id, e.target.value as Booking["status"])}
                    >
                      <MenuItem value="Scheduled">Scheduled</MenuItem>
                      <MenuItem value="Completed">Completed</MenuItem>
                      <MenuItem value="Cancelled">Cancelled</MenuItem>
                    </Select>
                  </FormControl>
                </TableCell>
                <TableCell>
                  <IconButton color="primary" onClick={() => handleOpenDialog("edit", booking)}>
                    <EditIcon />
                  </IconButton>
                  <IconButton color="error" onClick={() => handleOpenDialog("delete", booking)}>
                    <DeleteIcon />
                  </IconButton>
                </TableCell>
              </TableRow>
            ))}
          </TableBody>
        </Table>
      </TableContainer>
      <Box sx={{ display: "flex", justifyContent: "center", mt: 3 }}>
        <Pagination
          count={Math.ceil(filteredBookings.length / bookingsPerPage)}
          page={page}
          onChange={(event, value) => setPage(value)}
          color="primary"
        />
      </Box>

      <Dialog open={dialogOpen} onClose={handleCloseDialog}>
        <DialogTitle>
          {dialogMode === "add" ? "Add Booking" : dialogMode === "edit" ? "Edit Booking" : "Delete Booking"}
        </DialogTitle>
        <DialogContent>
          {dialogMode === "delete" ? (
            <Typography>Are you sure you want to delete this booking?</Typography>
          ) : (
            <Box component="form" sx={{ display: "flex", flexDirection: "column", gap: 2, mt: 2 }}>
              <TextField
                label="Patient Name"
                value={currentBooking?.patientName || ""}
                onChange={(e) => setCurrentBooking({ ...currentBooking!, patientName: e.target.value })}
              />
              <TextField
                label="Date"
                type="date"
                value={currentBooking?.date || ""}
                onChange={(e) => setCurrentBooking({ ...currentBooking!, date: e.target.value })}
                slotProps={{ inputLabel: { shrink: true } }}
              />
              <TextField
                label="Time"
                type="time"
                value={currentBooking?.time || ""}
                onChange={(e) => setCurrentBooking({ ...currentBooking!, time: e.target.value })}
                slotProps={{ inputLabel: { shrink: true } }}
              />
              <FormControl>
                <InputLabel>Status</InputLabel>
                <Select
                  value={currentBooking?.status || ""}
                  onChange={(e) =>
                    setCurrentBooking({ ...currentBooking!, status: e.target.value as Booking["status"] })
                  }
                >
                  <MenuItem value="Scheduled">Scheduled</MenuItem>
                  <MenuItem value="Completed">Completed</MenuItem>
                  <MenuItem value="Cancelled">Cancelled</MenuItem>
                </Select>
              </FormControl>
            </Box>
          )}
        </DialogContent>
        <DialogActions>
          <Button onClick={handleCloseDialog}>Cancel</Button>
          {dialogMode === "delete" ? (
            <Button onClick={handleDeleteBooking} color="error">
              Delete
            </Button>
          ) : (
            <Button onClick={handleSaveBooking} color="primary">
              Save
            </Button>
          )}
        </DialogActions>
      </Dialog>
    </Box>
  )
}

export default BookingList

