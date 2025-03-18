"use client"
import React, { useState, useEffect, useCallback } from "react"
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
  Avatar,
} from "@mui/material"
import SearchIcon from "@mui/icons-material/Search"
import AddIcon from "@mui/icons-material/Add"
import EditIcon from "@mui/icons-material/Edit"
import DeleteIcon from "@mui/icons-material/Delete"
import PatientDialog from "./PatientDialog"
import { useStore } from "../../store"
import { useShallow } from 'zustand/react/shallow'
import { Feedback, VaccinationHistory } from "../../models/vaccine"
import { Patient, UserRole } from "../../models/user"

interface PatientListProps {
  patientType: UserRole.CUSTOMER | UserRole.CHILD
}

const initialVaccinationHistory: VaccinationHistory[] = [
  { id: 1, patientId: 1, vaccineName: "Flu Shot", date: "2023-01-15" },
  { id: 2, patientId: 1, vaccineName: "COVID-19", date: "2023-02-20" },
  { id: 3, patientId: 2, vaccineName: "Tetanus", date: "2023-03-10" },
]

const initialFeedback: Feedback[] = [
  { id: 1, patientId: 1, date: "2023-04-01", comment: "Great service!", rating: 5 },
  { id: 2, patientId: 2, date: "2023-04-15", comment: "Quick and efficient.", rating: 4 },
]

const PatientList: React.FC<PatientListProps> = ({ patientType }) => {
  const { fetchAllUsers, fetchAllChildren } = useStore(
    useShallow((state) => ({
      fetchAllUsers: state.fetchAllUsers,
      fetchAllChildren: state.fetchAllChildren,
    }))
  )

  const [patients, setPatients] = useState<Patient[]>([])
  const [searchTerm, setSearchTerm] = useState("")
  const [page, setPage] = useState(1)
  const [dialogOpen, setDialogOpen] = useState(false)
  const [currentPatient, setCurrentPatient] = useState<Patient | null>(null)
  const [dialogMode, setDialogMode] = useState<"add" | "edit" | "delete">("add")

  const patientsPerPage = 5

  const fetchPatientsData = useCallback(async () => {
    try {
      let data: Patient[] = []
      if (patientType === UserRole.CUSTOMER) {
        data = await fetchAllUsers()
      } else {
        data = await fetchAllChildren()
      }
      setPatients(data || [])
    } catch (error) {
      console.error("Error fetching patients:", error)
    }
  }, [patientType, fetchAllUsers, fetchAllChildren])

  useEffect(() => {
    fetchPatientsData()
  }, [patientType, fetchPatientsData])

  const filteredPatients = patients.filter((patient) => {
    const matchesSearch =
      patient.username.toLowerCase().includes(searchTerm.toLowerCase()) ||
      patient.fullName.toLowerCase().includes(searchTerm.toLowerCase()) ||
      patient.phone.includes(searchTerm)
    return matchesSearch
  })

  const paginatedPatients = filteredPatients.slice(
    (page - 1) * patientsPerPage,
    page * patientsPerPage
  )

  const handleOpenDialog = (mode: "add" | "edit" | "delete", patient?: Patient) => {
    setDialogMode(mode)
    setCurrentPatient(patient || null)
    setDialogOpen(true)
  }

  const handleCloseDialog = () => {
    setDialogOpen(false)
    setCurrentPatient(null)
  }

  const onSubmit = (data: Patient) => {
    console.log("Submitted data:", data)
    fetchPatientsData()
    handleCloseDialog()
  }

  const handleDeletePatient = () => {
    console.log("Deleting patient:", currentPatient)
    fetchPatientsData()
    handleCloseDialog()
  }

  return (
    <Box sx={{ flexGrow: 1, p: 3 }}>
      <Typography variant="h4" component="h2" gutterBottom>
        {patientType === UserRole.CUSTOMER ? "Parent List" : "Children List"}
      </Typography>
      <Box sx={{ display: "flex", justifyContent: "space-between", mb: 3 }}>
        <TextField
          variant="outlined"
          placeholder="Search patients..."
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
          sx={{ width: "70%" }}
        />
        <Button
          variant="contained"
          color="primary"
          startIcon={<AddIcon />}
          onClick={() => handleOpenDialog("add")}
        >
          Add {patientType === UserRole.CUSTOMER ? "Parent" : "Child"}
        </Button>
      </Box>
      <TableContainer component={Paper}>
        <Table>
          <TableHead>
            <TableRow>
              <TableCell>Avatar</TableCell>
              <TableCell>Full Name</TableCell>
              <TableCell>Date of Birth</TableCell>
              <TableCell>Phone</TableCell>
              <TableCell>Vaccine Status</TableCell>
              <TableCell>Actions</TableCell>
            </TableRow>
          </TableHead>
          <TableBody>
            {paginatedPatients.map((patient) => (
              <TableRow key={patient.id}>
                <TableCell>
                  <Avatar src={patient.avatar} alt={patient.fullName} />
                </TableCell>
                <TableCell>{patient.fullName}</TableCell>
                <TableCell>{patient.dateOfBirth}</TableCell>
                <TableCell>{patient.phone}</TableCell>
                <TableCell>{patient.vaccineStatus}</TableCell>
                <TableCell>
                  <IconButton color="primary" onClick={() => handleOpenDialog("edit", patient)}>
                    <EditIcon />
                  </IconButton>
                  <IconButton color="error" onClick={() => handleOpenDialog("delete", patient)}>
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
          count={Math.ceil(filteredPatients.length / patientsPerPage)}
          page={page}
          onChange={(event, value) => setPage(value)}
          color="primary"
        />
      </Box>
      <PatientDialog
        open={dialogOpen}
        mode={dialogMode}
        patient={currentPatient}
        patientType={patientType}
        vaccinationHistory={initialVaccinationHistory}
        feedback={initialFeedback}
        onClose={handleCloseDialog}
        onSubmit={onSubmit}
        onDelete={handleDeletePatient}
      />
    </Box>
  )
}

export default PatientList
