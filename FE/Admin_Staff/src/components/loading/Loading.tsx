import React from "react";

import "./Loading.scss";
import { Backdrop, CircularProgress } from "@mui/material";
import { useStore } from "../../store";

const Loading: React.FC = () => {
  const isLoading = useStore((state) => state.loading.isLoading);

  return (
    <Backdrop
      sx={(theme) => ({ color: "#FE8282", zIndex: 9999999 })}
      open={isLoading}
    >
      <CircularProgress color="inherit" />
    </Backdrop>
  );
};

export default Loading;
