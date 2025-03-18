import { createTheme } from "@mui/material/styles";

const theme = createTheme({
  palette: {
    primary: {
      main: "#078DEE",
      light: "#68CDF9",
      dark: "#0351AB",
    },
    secondary: {
      main: "#8E33FF",
      light: "#C684FF",
      dark: "#5119B7",
    },
    info: {
      main: "#00B8D9",
      light: "#61F3F3",
      dark: "#00B8D9",
    },
    success: {
      main: "#22C55E",
      light: "#77ED8B",
      dark: "#118D57",
    },
    warning: {
      main: "#FFAB00",
      light: "#FFD666",
      dark: "#B76E00",
    },
    error: {
      main: "#FF5630",
      light: "#FFAC82",
      dark: "#B71D18",
    }
  },
  components: {
    MuiButton: {
      styleOverrides: {
        root: {
          textTransform: "none",
          fontWeight: "bold",
        },
      },
    },
    MuiTab: {
      styleOverrides: {
        root: {
          textTransform: "none",
          fontWeight: "bold",
        },
      },
    },
  },
});

export default theme;
