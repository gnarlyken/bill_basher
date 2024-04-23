import React from 'react';
import { Box, Button, Container, CssBaseline } from '@mui/material';
import NavBar from '../navbar/navbar';
import Img from './img';

const Wrapper = () => {
  return (
    <Container component="main" maxWidth="xs" sx={{ width: '100%' }}>
      <CssBaseline />
      <Box
        sx={{
          marginTop: 8,
          display: 'flex',
          flexDirection: 'column',
          alignItems: 'center',
          height: '70vh',
        }}
      >
        <Img
          src="../../assests/BB_logo.png"
          alt="Bill Basher Logo"
          sx={{
            aspectRatio: '1.42',
            width: 1 / 2,
            marginLeft: 'auto',
            marginRight: 'auto',
          }}
        />
        <NavBar />
      </Box>
    </Container>
  );
};

export default Wrapper;
