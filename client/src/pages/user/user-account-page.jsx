import React from 'react';
// import { Link, useNavigate } from 'react-router-dom';
import { Box, Button, Container, CssBaseline, Typography } from '@mui/material';
import NavBar from '../../components/layout/navbar/navbar';
import { useSelector } from 'react-redux';
import LogoComponent from '../../components/logo/logo-component';
import ApiService from '../../services/api-service';
import { useNavigate } from 'react-router-dom';
import { useDispatch } from 'react-redux';
import { clearUser } from '../../redux-toolkit/reducers/user/user.reducer';

const UserAccountPage = () => {
  const navigate = useNavigate();
  const dispatch = useDispatch();
  const user = useSelector((state) => state.user.user);
  const userId = user.userId;
  const onDelete = async (userId) => {
    await ApiService.deleteUser(userId);
    navigate('/');
  };

  const logout = () => {
    localStorage.removeItem('userSecret');
    dispatch(clearUser());
    navigate('/');
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
            textAlign: 'center',
          }}
        >
          <LogoComponent />
          <Box sx={{ width: '100%', height: '100%', margin: 'auto' }}>
            <Typography variant="h5" component="h2">
              Username: {user.username}{' '}
            </Typography>
            <Typography variant="h5" component="h2">
              First name: {user.name}{' '}
            </Typography>
            <Typography variant="h5" component="h2">
              Last name: {user.surname}{' '}
            </Typography>
            <Typography variant="h5" component="h2">
              Email: {user.email}{' '}
            </Typography>
          </Box>
          <Box display="flex" justifyContent="flex-end" sx={{ width: '100%' }}>
            <Button
              variant="contained"
              sx={{ mt: 3, mb: 2, width: '100%' }}
              onClick={() => logout()}
            >
              Log out
            </Button>
            <Button
              variant="contained"
              sx={{ mt: 3, mb: 2, width: '100%', ml: 2 }}
              onClick={() => onDelete(userId)}
            >
              Delete
            </Button>
          </Box>
        </Box>

        <NavBar />
      </Container>
    </>
  );
};

export default UserAccountPage;
