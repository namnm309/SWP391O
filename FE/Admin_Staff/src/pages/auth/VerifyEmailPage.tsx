import React, { useState } from "react";
import { Box, TextField, Button, Typography } from "@mui/material";
import axios from "../../utils/axiosConfig";

const VerifyEmailPage: React.FC = () => {
  const [email, setEmail] = useState("");
  const [code, setCode] = useState("");

  const handleVerify = async () => {
    try {
      await axios.post("/users/verify", { email, code });
      alert("Xác thực email thành công!");
    } catch (error) {
      console.error(error);
      alert("Verify failed");
    }
  };

  return (
    <Box sx={{ maxWidth: 400, mx: "auto", mt: 5 }}>
      <Typography variant="h5" mb={2}>Xác thực Email</Typography>
      <TextField
        label="Email"
        fullWidth
        sx={{ mb: 2 }}
        value={email}
        onChange={(e) => setEmail(e.target.value)}
      />
      <TextField
        label="Mã xác thực"
        fullWidth
        sx={{ mb: 2 }}
        value={code}
        onChange={(e) => setCode(e.target.value)}
      />
      <Button variant="contained" onClick={handleVerify}>Xác thực</Button>
    </Box>
  );
};

export default VerifyEmailPage;
