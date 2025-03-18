import React, { useState } from "react";
import { Box, TextField, Button, Typography } from "@mui/material";
import axios from "../../utils/axiosConfig";

const ResendCodePage: React.FC = () => {
  const [email, setEmail] = useState("");

  const handleResend = async () => {
    try {
      await axios.post("/users/resend", { email });
      alert("Mã xác thực mới đã được gửi!");
    } catch (error) {
      console.error(error);
      alert("Resend code failed");
    }
  };

  return (
    <Box sx={{ maxWidth: 400, mx: "auto", mt: 5 }}>
      <Typography variant="h5" mb={2}>Gửi lại mã xác thực</Typography>
      <TextField
        label="Email"
        fullWidth
        sx={{ mb: 2 }}
        value={email}
        onChange={(e) => setEmail(e.target.value)}
      />
      <Button variant="contained" onClick={handleResend}>Gửi lại mã</Button>
    </Box>
  );
};

export default ResendCodePage;
