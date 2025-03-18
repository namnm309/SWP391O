import React, { useMemo } from "react";
import { BrowserRouter, useRoutes, Navigate } from "react-router-dom";
import { ThemeProvider } from "@mui/material";
import theme from "./theme";
import Home from "./pages/home/Home";
import AuthPage from "./pages/auth/AuthPage";
import "./App.css";
import ProtectedRoute from "./utils/ProtectedRoute";
import Loading from "./components/loading/Loading";
import { useStore } from "./store";
import { LocalizationProvider } from "@mui/x-date-pickers";
import { AdapterDayjs } from "@mui/x-date-pickers/AdapterDayjs";

const AppRoutes = () => {
  const user = useStore((store) => store.profile.user);
  const role = useMemo(() => user?.role, [user]);
  const isAuthenticated = !!user?.token;

  const routes = [
    {
      path: "/",
      element: isAuthenticated ? <Navigate to="/dashboard" /> : <AuthPage />,
    },
    {
      path: "/dashboard",
      element: (
        <ProtectedRoute
          isAllowed={isAuthenticated && (role === "ROLE_STAFF" || role === "ROLE_ADMIN")}
          redirectPath="/"
        >
          <Home />
        </ProtectedRoute>
      ),
    },
  ];

  return useRoutes(routes);
};

function App() {
  return (
    <ThemeProvider theme={theme}>
      <LocalizationProvider dateAdapter={AdapterDayjs}>
        <BrowserRouter>
          <AppRoutes />
          <Loading />
        </BrowserRouter>
      </LocalizationProvider>
    </ThemeProvider>
  );
}

export default App;
