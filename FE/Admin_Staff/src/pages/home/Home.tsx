import React, { useEffect, useState } from "react";
import { Box } from "@mui/material";
import { styled } from "@mui/system";
import { UserRole } from "../../models/user";
import { menuItemsByRole } from "../../config/menuConfig";

import Header from "../../components/header/Header";
import Footer from "../../components/footer/Footer";
import Sidebar from "../../components/sidebar/Sidebar";
import UserManagement from "../../components/management/UsersManagement";
import FeedbackList from "../../components/feedback/FeedbackList";
import BookingList from "../../components/booking/BookingList";
import Dashboard from "../../components/dashboard/Dashboard";
import VaccineList from "../../components/vaccine/VaccineList";
import PatientList from "../../components/patient/PatientList";
import StaffManagement from "../../components/management/StaffManagement";
import { useStore } from "../../store";
import { useNavigate } from "react-router-dom";

const MainScreen = styled(Box)({
  flexGrow: 1,
  padding: "1rem",
  background: "#fff",
  margin: "1rem 1rem 1rem 0",
  borderRadius: "1rem",
  height: "auto",
  overflow: "auto",
});

const DashboardLayout: React.FC = () => {
  const navigate = useNavigate();
  const user = useStore((state) => state.profile.user);

  const [activeScreen, setActiveScreen] = useState<string>("");
  const [sidebarOpen, setSidebarOpen] = useState<boolean>(true);

  useEffect(() => {
    if (!user) {
      navigate("/");
    } else {
      const role: UserRole = user.role;
      const defaultScreen = menuItemsByRole[role][0].key;
      setActiveScreen(defaultScreen);
    }
  }, [user, navigate]);

  if (!user) {
    return null;
  }

  const role: UserRole = user.role;

  const toggleSidebar = () => {
    setSidebarOpen(!sidebarOpen);
  };

  const handleSelect = (key: string) => {
    setActiveScreen(key);
  };

  const renderCurrentPage = () => {
    switch (activeScreen) {
      case "dashboard":
        return <Dashboard />;
      case UserRole.CUSTOMER:
        return <PatientList patientType={UserRole.CUSTOMER} />;
      case "children":
        return <PatientList patientType={UserRole.CHILD} />;
      case "bookings":
        return <BookingList />;
      case "vaccines":
        return <VaccineList />;
      case "feedback":
        return <FeedbackList />;
      case "users-management":
        return <UserManagement />;
      case "staff-management":
        return <StaffManagement />;
      // case "roles-management":
      //   return <RolesManagement />;
      // case "daily-orders":
      //   return <DailyOrdersReport />;
      // case "top-vaccine":
      //   return <TopVaccineReport />;
      // case "revenue":
      //   return <RevenueReport />;
      // case "vaccinated-age":
      //   return <VaccinatedAgeStats />;
      // case "notifications":
      //   return <NotificationsPage />;
      default:
        return <Dashboard />;
    }
  };

  return (
    <>
      <Header />

      <Box sx={{ display: "flex", height: "calc(100vh - 74px)" }}>
        <Sidebar
          open={sidebarOpen}
          onToggle={toggleSidebar}
          onSelect={handleSelect}
          role={role}
        />
        <MainScreen>{renderCurrentPage()}</MainScreen>
      </Box>

      {/* <Footer /> */}
    </>
  );
};

export default DashboardLayout;
