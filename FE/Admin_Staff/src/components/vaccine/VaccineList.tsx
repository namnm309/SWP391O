import React, { useState } from "react";
import {
  Box,
  Typography,
  Paper,
  Button,
  Dialog,
  DialogTitle,
  DialogContent,
  DialogActions,
  TextField,
  FormControl,
  InputLabel,
  Select,
  MenuItem,
  Checkbox,
  OutlinedInput,
  ListItemText,
} from "@mui/material";
import { DataGrid, GridColDef } from "@mui/x-data-grid";
import { useForm, Controller } from "react-hook-form";
import EditIcon from "@mui/icons-material/Edit";
import DeleteIcon from "@mui/icons-material/Delete";
import IconButton from "@mui/material/IconButton";

interface Vaccine {
  id: number;
  name: string;
  description: string;
  price: number;
  service: "single" | "combo" | "modify";
}

const initialVaccines: Vaccine[] = [
  { id: 1, name: "Flu Shot", description: "Annual influenza vaccine", price: 25, service: "single" },
  { id: 2, name: "COVID-19 Vaccine", description: "Coronavirus vaccine", price: 0, service: "single" },
  {
    id: 3,
    name: "Travel Vaccine Package",
    description: "Combo of vaccines for international travel",
    price: 150,
    service: "combo",
  },
  {
    id: 4,
    name: "Childhood Vaccine Series",
    description: "Set of essential childhood vaccines",
    price: 200,
    service: "combo",
  },
  { id: 5, name: "Booster Shots", description: "Modify existing vaccination schedule", price: 50, service: "modify" },
];

const VaccineList: React.FC = () => {
  const [vaccines, setVaccines] = useState<Vaccine[]>(initialVaccines);
  const [dialogOpen, setDialogOpen] = useState(false);
  const [currentVaccine, setCurrentVaccine] = useState<Vaccine | null>(null);
  const [filterServices, setFilterServices] = useState<string[]>([]);
  const [searchQuery, setSearchQuery] = useState("");
  const { control, handleSubmit, reset } = useForm<Vaccine>();

  const handleOpenDialog = (vaccine?: Vaccine) => {
    setCurrentVaccine(vaccine || null);
    reset(vaccine || { id: 0, name: "", description: "", price: 0, service: "single" });
    setDialogOpen(true);
  };

  const handleCloseDialog = () => {
    setDialogOpen(false);
    setCurrentVaccine(null);
  };

  const onSubmit = (data: Vaccine) => {
    if (currentVaccine) {
      setVaccines(vaccines.map((v) => (v.id === currentVaccine.id ? { ...data, id: currentVaccine.id } : v)));
    } else {
      setVaccines([...vaccines, { ...data, id: vaccines.length + 1 }]);
    }
    handleCloseDialog();
  };

  const handleDeleteVaccine = (id: number) => {
    setVaccines(vaccines.filter((v) => v.id !== id));
  };

  const filteredVaccines = vaccines.filter(
    (v) =>
      (filterServices.length === 0 || filterServices.includes(v.service)) &&
      v.name.toLowerCase().includes(searchQuery.toLowerCase())
  );

  const columns: GridColDef[] = [
    { field: "id", headerName: "ID", width: 70 },
    { field: "name", headerName: "Name", width: 200 },
    { field: "description", headerName: "Description", flex: 1 },
    { field: "price", headerName: "Price ($)", width: 120, type: "number", sortable: true },
    { field: "service", headerName: "Service", width: 150 },
    {
      field: "actions",
      headerName: "Actions",
      width: 150,
      renderCell: (params) => (
        <>
          <IconButton color="primary" onClick={() => handleOpenDialog(params.row)}>
            <EditIcon />
          </IconButton>
          <IconButton color="error" onClick={() => handleDeleteVaccine(params.row.id)}>
            <DeleteIcon />
          </IconButton>
        </>
      ),
    },
  ];

  return (
    <Box sx={{ flexGrow: 1, p: 3 }}>
      <Typography variant="h4" component="h2" gutterBottom>
        Vaccine List
      </Typography>
      <Box sx={{ display: "flex", justifyContent: "space-between", mb: 3, gap: 4 }}>
        <TextField
          label="Search"
          variant="outlined"
          value={searchQuery}
          onChange={(e) => setSearchQuery(e.target.value)}
          sx={{ flex: 1 }}
        />
        <FormControl sx={{ minWidth: 200 }}>
          <InputLabel>Filter Service</InputLabel>
          <Select
            multiple
            value={filterServices}
            onChange={(e) => setFilterServices(e.target.value as string[])}
            input={<OutlinedInput label="Filter Service" />}
            renderValue={(selected) => selected.join(", ")}
          >
            {["single", "combo", "modify"].map((service) => (
              <MenuItem key={service} value={service}>
                <Checkbox checked={filterServices.includes(service)} />
                <ListItemText primary={service.charAt(0).toUpperCase() + service.slice(1)} />
              </MenuItem>
            ))}
          </Select>
        </FormControl>
        <Button variant="contained" color="primary" onClick={() => handleOpenDialog()}>
          Add Vaccine
        </Button>
      </Box>
      <Paper>
        <DataGrid rows={filteredVaccines} columns={columns} autoHeight disableRowSelectionOnClick />
      </Paper>
    </Box>
  );
};

export default VaccineList;
