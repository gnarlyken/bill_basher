import React from 'react';
import { Link, useNavigate } from 'react-router-dom';
// import { useSelector } from 'react-redux';
import { Box, Button, Container, CssBaseline, Typography } from '@mui/material';
import EventsListComponent from '../events/event-list/events-list-component';
import NavBar from '../../../components/layout/navbar/navbar';
import LogoComponent from '../../../components/logo/logo-component';

const EventsList = () => {
  const navigate = useNavigate();

  const onClick = () => {
    navigate('/createevent');
  };

  return (
    <>
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
          <LogoComponent />
          <Typography
            variant="h4"
            component="h1"
            sx={{ color: 'primary.dark' }}
          >
            My Events
          </Typography>
          <Box display="flex" justifyContent="flex-end" sx={{ width: '100%' }}>
            <Button
              variant="contained"
              sx={{ mt: 3, mb: 2, width: '50%', ml: 2 }}
              onClick={onClick}
            >
              Create new Event
            </Button>
          </Box>
          <Box sx={{ width: '100%', height: '100%' }}>
            <EventsListComponent />
          </Box>
        </Box>

        <NavBar />
      </Container>
    </>
  );
};

export default EventsList;
