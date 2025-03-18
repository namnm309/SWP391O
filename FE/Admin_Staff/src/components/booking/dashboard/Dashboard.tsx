import type React from "react"
import {
  Box,
  Card,
  CardContent,
  Typography,
  Table,
  TableBody,
  TableCell,
  TableContainer,
  TableHead,
  TableRow,
  Paper,
  Button,
} from "@mui/material"
import Grid from "@mui/material/Grid2"
import PeopleIcon from "@mui/icons-material/People"
import EventIcon from "@mui/icons-material/Event"
import VaccinesIcon from "@mui/icons-material/Vaccines"

const StatCard: React.FC<{ title: string; value: string; icon: React.ReactNode }> = ({ title, value, icon }) => (
  <Card sx={{ height: "100%" }}>
    <CardContent sx={{ display: "flex", flexDirection: "column", height: "100%" }}>
      <Box sx={{ display: "flex", justifyContent: "space-between", mb: 2 }}>
        <Typography variant="h6" component="div">
          {title}
        </Typography>
        <Box sx={{ color: "primary.main" }}>{icon}</Box>
      </Box>
      <Typography variant="h4" component="div" sx={{ mt: "auto" }}>
        {value}
      </Typography>
    </CardContent>
  </Card>
)

const recentPatients = [
  { name: "John Doe", appointmentDate: "2023-05-15", status: "Scheduled" },
  { name: "Jane Smith", appointmentDate: "2023-05-16", status: "Completed" },
  { name: "Alice Johnson", appointmentDate: "2023-05-17", status: "Cancelled" },
  { name: "Bob Williams", appointmentDate: "2023-05-18", status: "Scheduled" },
]

const Dashboard: React.FC = () => {
  return (
    <Box sx={{ flexGrow: 1, p: 3 }}>
      <Typography variant="h4" component="h2" gutterBottom>
        Dashboard Overview
      </Typography>
      <Grid container spacing={3}>
        <Grid size={{xs:12, sm:4}}>
          <StatCard title="Total Patients" value="1,234" icon={<PeopleIcon fontSize="large" />} />
        </Grid>
        <Grid size={{xs:12, sm:4}}>
          <StatCard title="Upcoming Appointments" value="42" icon={<EventIcon fontSize="large" />} />
        </Grid>
        <Grid size={{xs:12, sm:4}}>
          <StatCard title="Vaccines in Stock" value="500" icon={<VaccinesIcon fontSize="large" />} />
        </Grid>
        <Grid size={12}>
          <Card>
            <CardContent>
              <Typography variant="h5" component="div" gutterBottom>
                Recent Patient Activity
              </Typography>
              <TableContainer component={Paper} elevation={0}>
                <Table>
                  <TableHead>
                    <TableRow>
                      <TableCell>Name</TableCell>
                      <TableCell>Appointment Date</TableCell>
                      <TableCell>Status</TableCell>
                      <TableCell>Actions</TableCell>
                    </TableRow>
                  </TableHead>
                  <TableBody>
                    {recentPatients.map((patient) => (
                      <TableRow key={patient.name}>
                        <TableCell>{patient.name}</TableCell>
                        <TableCell>{patient.appointmentDate}</TableCell>
                        <TableCell>{patient.status}</TableCell>
                        <TableCell>
                          <Button variant="outlined" size="small">
                            View Details
                          </Button>
                        </TableCell>
                      </TableRow>
                    ))}
                  </TableBody>
                </Table>
              </TableContainer>
            </CardContent>
          </Card>
        </Grid>
      </Grid>
    </Box>
  )
}

export default Dashboard

