"use client"
import React, { useState, useEffect } from "react"
import {
  Box,
  Typography,
  Dialog,
  DialogTitle,
  DialogContent,
  DialogActions,
  Tabs,
  Tab,
  TextField,
  FormControl,
  InputLabel,
  Select,
  MenuItem,
  Paper,
  Table,
  TableContainer,
  TableHead,
  TableRow,
  TableCell,
  TableBody,
  Button,
  OutlinedInput,
} from "@mui/material"
import { Controller, useForm } from "react-hook-form"
import { Patient, UserRole } from "../../models/user"
import { Feedback, VaccinationHistory } from "../../models/vaccine"

interface PatientDialogProps {
  open: boolean
  mode: "add" | "edit" | "delete"
  patient: Patient | null
  patientType: UserRole.CUSTOMER | UserRole.CHILD
  vaccinationHistory: VaccinationHistory[]
  feedback: Feedback[]
  onClose: () => void
  onSubmit: (data: Patient) => void
  onDelete: () => void
}

const PatientDialog: React.FC<PatientDialogProps> = ({
  open,
  mode,
  patient,
  patientType,
  vaccinationHistory,
  feedback,
  onClose,
  onSubmit,
  onDelete,
}) => {
  const { control, handleSubmit, reset } = useForm<Patient>()
  const [dialogTab, setDialogTab] = useState(0)

  useEffect(() => {
    reset(
      patient || {
        id: 0,
        username: "",
        fullName: "",
        dateOfBirth: "",
        phone: "",
        role: patientType === UserRole.CUSTOMER ? UserRole.CUSTOMER : UserRole.CHILD,
        avatar: "/placeholder.svg",
        vaccineStatus: "Not Vaccinated",
      }
    )
    setDialogTab(0)
  }, [patient, patientType, reset, open])

  return (
    <Dialog open={open} onClose={onClose} maxWidth="md" fullWidth>
      <DialogTitle>
        {mode === "add"
          ? `Add ${patientType === UserRole.CUSTOMER ? "Parent" : "Child"}`
          : mode === "edit"
          ? "Edit Patient"
          : "Delete Patient"}
      </DialogTitle>
      <DialogContent>
        {mode === "delete" ? (
          <Typography>Are you sure you want to delete this patient?</Typography>
        ) : (
          <>
            {/* Section 1: Patient Info */}
            <Box sx={{ mb: 3 }}>
              <Typography variant="h6">Patient Info</Typography>
              <Box
                component="form"
                sx={{ display: "flex", flexDirection: "column", gap: 2, mt: 1 }}
              >
                <Controller
                  name="username"
                  control={control}
                  defaultValue=""
                  rules={{ required: "Username is required" }}
                  render={({ field, fieldState: { error } }) => (
                    <TextField {...field} label="Username" error={!!error} helperText={error?.message} />
                  )}
                />
                <Controller
                  name="fullName"
                  control={control}
                  defaultValue=""
                  rules={{ required: "Full Name is required" }}
                  render={({ field, fieldState: { error } }) => (
                    <TextField {...field} label="Full Name" error={!!error} helperText={error?.message} />
                  )}
                />
                <Controller
                  name="dateOfBirth"
                  control={control}
                  defaultValue=""
                  rules={{ required: "Date of Birth is required" }}
                  render={({ field, fieldState: { error } }) => (
                    <TextField
                      {...field}
                      label="Date of Birth"
                      type="date"
                      slotProps={{ inputLabel: { shrink: true } }}
                      error={!!error}
                      helperText={error?.message}
                    />
                  )}
                />
                <Controller
                  name="phone"
                  control={control}
                  defaultValue=""
                  render={({ field }) => <TextField {...field} label="Phone" />}
                />
                <Controller
                  name="role"
                  control={control}
                  defaultValue={patientType === UserRole.CUSTOMER ? UserRole.CUSTOMER : UserRole.CHILD}
                  render={({ field }) => (
                    <FormControl>
                      <InputLabel>Role</InputLabel>
                      <Select 
                        {...field}
                        input={<OutlinedInput label="Role" />}
                      >
                        {patientType === UserRole.CUSTOMER ? (
                          <MenuItem value="parent">Parent</MenuItem>
                        ) : (
                          <MenuItem value="child">Child</MenuItem>
                        )}
                      </Select>
                    </FormControl>
                  )}
                />
                <Controller
                  name="vaccineStatus"
                  control={control}
                  defaultValue="Not Vaccinated"
                  render={({ field }) => (
                    <FormControl>
                      <InputLabel>Vaccine Status</InputLabel>
                      <Select 
                        {...field}
                        input={<OutlinedInput label="Vaccine Status" />}
                      >
                        <MenuItem value="Not Vaccinated">Not Vaccinated</MenuItem>
                        <MenuItem value="Partially Vaccinated">Partially Vaccinated</MenuItem>
                        <MenuItem value="Fully Vaccinated">Fully Vaccinated</MenuItem>
                      </Select>
                    </FormControl>
                  )}
                />
              </Box>
            </Box>

            {/* Section 2: Relationship Details */}
            {patient?.role === UserRole.CUSTOMER ? (
              <Box sx={{ mb: 3 }}>
                <Typography variant="h6">Children</Typography>
                <TableContainer component={Paper}>
                  <Table>
                    <TableHead>
                      <TableRow>
                        <TableCell>Full Name</TableCell>
                        <TableCell>Date of Birth</TableCell>
                        <TableCell>Vaccine Status</TableCell>
                      </TableRow>
                    </TableHead>
                    <TableBody>
                      {patient.children?.map((child) => (
                        <TableRow key={child.id}>
                          <TableCell>{child.fullName}</TableCell>
                          <TableCell>{child.dateOfBirth}</TableCell>
                          <TableCell>{child.vaccineStatus}</TableCell>
                        </TableRow>
                      ))}
                    </TableBody>
                  </Table>
                </TableContainer>
              </Box>
            ) : patient?.role === UserRole.CHILD && patient.parent ? (
              <Box sx={{ mb: 3 }}>
                <Typography variant="h6">Parent</Typography>
                <TableContainer component={Paper}>
                  <Table>
                    <TableHead>
                      <TableRow>
                        <TableCell>Full Name</TableCell>
                        <TableCell>Date of Birth</TableCell>
                        <TableCell>Phone</TableCell>
                      </TableRow>
                    </TableHead>
                    <TableBody>
                      <TableRow>
                        <TableCell>{patient.parent.fullName}</TableCell>
                        <TableCell>{patient.parent.dateOfBirth}</TableCell>
                        <TableCell>{patient.parent.phone}</TableCell>
                      </TableRow>
                    </TableBody>
                  </Table>
                </TableContainer>
              </Box>
            ) : null}

            {/* Section 3: Tabs for Additional Data */}
            <Box sx={{ mt: 2 }}>
              <Tabs value={dialogTab} onChange={(e, newValue) => setDialogTab(newValue)}>
                <Tab label="Vaccine History" />
                <Tab label="Feedback" />
              </Tabs>
              {dialogTab === 0 && (
                <TableContainer component={Paper} sx={{ mt: 1 }}>
                  <Table>
                    <TableHead>
                      <TableRow>
                        <TableCell>Vaccine Name</TableCell>
                        <TableCell>Date</TableCell>
                      </TableRow>
                    </TableHead>
                    <TableBody>
                      {vaccinationHistory
                        .filter((vh) => vh.patientId === patient?.id)
                        .map((vh) => (
                          <TableRow key={vh.id}>
                            <TableCell>{vh.vaccineName}</TableCell>
                            <TableCell>{vh.date}</TableCell>
                          </TableRow>
                        ))}
                    </TableBody>
                  </Table>
                </TableContainer>
              )}
              {dialogTab === 1 && (
                <TableContainer component={Paper} sx={{ mt: 1 }}>
                  <Table>
                    <TableHead>
                      <TableRow>
                        <TableCell>Date</TableCell>
                        <TableCell>Comment</TableCell>
                        <TableCell>Rating</TableCell>
                      </TableRow>
                    </TableHead>
                    <TableBody>
                      {feedback
                        .filter((f) => f.patientId === patient?.id)
                        .map((f) => (
                          <TableRow key={f.id}>
                            <TableCell>{f.date}</TableCell>
                            <TableCell>{f.comment}</TableCell>
                            <TableCell>{f.rating}</TableCell>
                          </TableRow>
                        ))}
                    </TableBody>
                  </Table>
                </TableContainer>
              )}
            </Box>
          </>
        )}
      </DialogContent>
      <DialogActions>
        <Button onClick={onClose}>Cancel</Button>
        {mode === "delete" ? (
          <Button onClick={onDelete} color="error">
            Delete
          </Button>
        ) : (
          <Button onClick={handleSubmit(onSubmit)} color="primary">
            Save
          </Button>
        )}
      </DialogActions>
    </Dialog>
  )
}

export default PatientDialog
