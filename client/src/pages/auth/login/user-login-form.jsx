import React from 'react';
import { Link, useNavigate } from 'react-router-dom';
import { useDispatch } from 'react-redux';
import {
  Avatar,
  Alert,
  Box,
  Button,
  Container,
  CssBaseline,
  Grid,
  Typography,
} from '@mui/material';
import LockIcon from '@mui/icons-material/Lock';
import TextFieldComponent from '../../../components/text-field/text-field-component';
import ApiService from '../../../services/api-service';
import { addUser } from '../../../redux-toolkit/reducers/user/user.reducer';
import LogoComponent from '../../../components/logo/logo-component';

const UserLoginForm = () => {
  const navigate = useNavigate();
  const dispatch = useDispatch();
  const [error, setError] = React.useState('');

  const loginUser = async (userLoginData) => {
    try {
      const res = await ApiService.login(userLoginData);
      if (res.status === 200) {
        localStorage.setItem('userSecret', res.data.token);
        dispatch(addUser(res.data));
        navigate('/events');
      }
    } catch (error) {
      if (error.response.status === 401) {
        setError(error.response.data);
      }
    }
  };

  const handleSubmit = (e) => {
    e.preventDefault();
    const formData = new FormData(e.currentTarget);

    const usernameOrEmail = formData.get('username');
    const password = formData.get('password');

    const userLoginData = {
      usernameOrEmail,
      password,
    };

    loginUser(userLoginData);
  };

  return (
    <Container component="main" maxWidth="xs" bgcolor="0087ac">
      <CssBaseline />
      <Box
        sx={{
          marginTop: 8,
          display: 'flex',
          flexDirection: 'column',
          alignItems: 'center',
        }}
      >
        <LogoComponent />
        <Avatar sx={{ m: 1, bgcolor: 'primary.main' }}>
          <LockIcon />
        </Avatar>
        <Typography component="h1" variant="h5">
          Login
        </Typography>
        <Box component="form" noValidate onSubmit={handleSubmit} sx={{ mt: 3 }}>
          <Grid container spacing={2}>
            <TextFieldComponent
              defaultValue={''}
              required={true}
              label={'Username'}
              name={'username'}
              type={'text'}
            />
            <TextFieldComponent
              defaultValue={''}
              required={true}
              label={'Password'}
              name={'password'}
              type={'password'}
            />
          </Grid>
          <Button
            type="submit"
            fullWidth
            variant="contained"
            sx={{ mt: 3, mb: 2 }}
          >
            Login
          </Button>
          <Grid container justifyContent="flex-end">
            <Grid item>
              <Link to="/register" style={{ color: '#000000' }}>
                <Typography variant="body1">
                  New to Bill Basher? Register here
                </Typography>
              </Link>
            </Grid>
          </Grid>
          {error && (
            <Alert severity="error" variant="filled">
              {error}
            </Alert>
          )}
        </Box>
      </Box>
    </Container>
  );
};

export default UserLoginForm;
