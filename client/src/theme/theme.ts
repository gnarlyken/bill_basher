import { createTheme } from '@mui/material';

const theme = createTheme({
  palette: {
    primary: {
      main: '#f78a93',
      // light: '#d0d175',
      dark: '#fe4455',
    },
    secondary: {
      main: '#9ce0db',
      // light: '#d0d175',
      dark: '#19e0cb',
    },
    background: {
      default: '#76a4b1',
    },
  },
  // typography: {
  //   allVariants: {
  //     color: '#ffffff',
  //   },
  // },
  zIndex: {
    appBar: 1250,
  },
});

export default theme;
