import React, { useState } from "react";
import {
  Typography,
  Box,
  Paper,
  Table,
  TableHead,
  TableRow,
  TableCell,
  TableBody,
  TablePagination,
  IconButton,
  Button,
  Tooltip,
} from "@mui/material";
import { Edit, Delete } from "@mui/icons-material";
import { Vaccine } from "../../models/vaccine";
import VaccineForm from "./VaccineForm";
import { Validate } from "../../utils/validate";

const initialVaccines: Vaccine[] = [
  {
    id: 1,
    vaccineName: "Vaccine A",
    price: 100000,
    description: "Dùng cho trẻ 3-6 tháng",
    vaccineAge: "3-6 tháng",
    dateOfManufacture: "2025-01-01",
    vaccineExpiryDate: "24 tháng",
  },
  {
    id: 2,
    vaccineName: "Vaccine B",
    price: 200000,
    description: "Dành cho người lớn",
    vaccineAge: "18+",
    dateOfManufacture: "2024-12-01",
    vaccineExpiryDate: "12 tháng",
  },
];

const VaccineManagement: React.FC = () => {
  const [vaccines, setVaccines] = useState<Vaccine[]>(initialVaccines);

  const [page, setPage] = useState(0);
  const [rowsPerPage, setRowsPerPage] = useState(10);

  const [openForm, setOpenForm] = useState(false);
  const [editingVaccine, setEditingVaccine] = useState<Vaccine | undefined>(
    undefined
  );

  const handleCreateNew = () => {
    setEditingVaccine(undefined);
    setOpenForm(true);
  };

  const handleEdit = (vaccine: Vaccine) => {
    setEditingVaccine(vaccine);
    setOpenForm(true);
  };

  const handleDelete = (id?: number) => {
    if (!id) return;
    const confirm = window.confirm("Bạn có chắc chắn muốn xoá vaccine này?");
    if (!confirm) return;
    setVaccines((prev) => prev.filter((v) => v.id !== id));
  };

  const handleSubmitForm = (data: Vaccine) => {
    if (data.id) {
      setVaccines((prev) =>
        prev.map((v) => (v.id === data.id ? data : v))
      );
    } else {
      const newId = vaccines.length
        ? vaccines[vaccines.length - 1].id! + 1
        : 1;
      setVaccines([...vaccines, { ...data, id: newId }]);
    }
    setOpenForm(false);
  };

  const handleChangePage = (event: unknown, newPage: number) => {
    setPage(newPage);
  };
  const handleChangeRowsPerPage = (
    event: React.ChangeEvent<HTMLInputElement>
  ) => {
    setRowsPerPage(+event.target.value);
    setPage(0);
  };

  const startIndex = page * rowsPerPage;
  const endIndex = startIndex + rowsPerPage;
  const currentData = vaccines.slice(startIndex, endIndex);

  return (
    <div>
      <Box sx={{ display: "flex", justifyContent: "space-between", mb: 2 }}>
        <Typography variant="h4">Quản lý Vaccine</Typography>
        <Button variant="contained" onClick={handleCreateNew}>
          Tạo mới
        </Button>
      </Box>

      <Paper>
        <Table>
          <TableHead>
            <TableRow>
              <TableCell>ID</TableCell>
              <TableCell>Vaccine</TableCell>
              <TableCell>Giá (VNĐ)</TableCell>
              <TableCell>Hạn sử dụng</TableCell>
              <TableCell align="center">Actions</TableCell>
            </TableRow>
          </TableHead>
          <TableBody>
            {currentData.map((v) => (
              <TableRow key={v.id}>
                <TableCell>{v.id}</TableCell>
                <TableCell>{v.vaccineName}</TableCell>
                <TableCell>{Validate.formatPrice(v.price)}</TableCell>
                <TableCell>{v.vaccineExpiryDate}</TableCell>
                <TableCell align="center">
                  <Tooltip title="Edit">
                    <IconButton onClick={() => handleEdit(v)}>
                      <Edit />
                    </IconButton>
                  </Tooltip>
                  <Tooltip title="Delete">
                    <IconButton color="error" onClick={() => handleDelete(v.id)}>
                      <Delete />
                    </IconButton>
                  </Tooltip>
                </TableCell>
              </TableRow>
            ))}
            {!currentData.length && (
              <TableRow>
                <TableCell colSpan={5} align="center">
                  Không có dữ liệu
                </TableCell>
              </TableRow>
            )}
          </TableBody>
        </Table>

        <TablePagination
          component="div"
          count={vaccines.length}
          page={page}
          onPageChange={handleChangePage}
          rowsPerPage={rowsPerPage}
          onRowsPerPageChange={handleChangeRowsPerPage}
        />
      </Paper>

      {/* Dialog form create/update */}
      <VaccineForm
        open={openForm}
        defaultValues={editingVaccine}
        onClose={() => setOpenForm(false)}
        onSubmit={handleSubmitForm}
      />
    </div>
  );
};

export default VaccineManagement;
