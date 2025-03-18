import React, { useEffect } from "react";
import { useForm } from "react-hook-form";
import { Dialog, DialogTitle, DialogContent, TextField, Button } from "@mui/material";

import { Vaccine } from "../../models/vaccine";

interface VaccineFormProps {
  open: boolean;
  defaultValues?: Vaccine;
  onClose: () => void;
  onSubmit: (data: Vaccine) => void;
}

const VaccineForm: React.FC<VaccineFormProps> = ({
  open,
  defaultValues,
  onClose,
  onSubmit,
}) => {
  const { register, handleSubmit, reset } = useForm<Vaccine>({
    defaultValues: {
      vaccineName: "",
      price: 0,
      description: "",
      vaccineAge: "",
      dateOfManufacture: "",
      vaccineExpiryDate: "",
    },
  });

  useEffect(() => {
    if (defaultValues) {
      reset(defaultValues);
    }
  }, [defaultValues, reset]);

  const submitHandler = (data: Vaccine) => {
    onSubmit(data);
  };

  return (
    <Dialog open={open} onClose={onClose} fullWidth>
      <DialogTitle>{defaultValues ? "Cập nhật" : "Tạo mới"} Vaccine</DialogTitle>
      <DialogContent>
        <form onSubmit={handleSubmit(submitHandler)}>
          <TextField
            label="Tên Vaccine"
            fullWidth
            margin="normal"
            {...register("vaccineName", { required: true })}
          />

          <TextField
            label="Giá (VNĐ)"
            type="number"
            fullWidth
            margin="normal"
            {...register("price", { valueAsNumber: true })}
          />

          <TextField
            label="Mô tả"
            fullWidth
            margin="normal"
            multiline
            rows={3}
            {...register("description")}
          />

          <TextField
            label="Độ tuổi"
            fullWidth
            margin="normal"
            {...register("vaccineAge")}
          />

          <TextField
            label="Ngày sản xuất (YYYY-MM-DD)"
            type="date"
            fullWidth
            margin="normal"
            slotProps={{ inputLabel: { shrink: true } }}
            {...register("dateOfManufacture")}
          />

          <TextField
            label="Hạn sử dụng"
            fullWidth
            margin="normal"
            placeholder="24 tháng"
            {...register("vaccineExpiryDate")}
          />

          <Button variant="contained" type="submit" sx={{ mt: 2 }}>
            {defaultValues ? "Cập nhật" : "Tạo mới"}
          </Button>
          <Button variant="outlined" onClick={onClose} sx={{ mt: 2, ml: 2 }}>
            Hủy
          </Button>
        </form>
      </DialogContent>
    </Dialog>
  );
};

export default VaccineForm;
