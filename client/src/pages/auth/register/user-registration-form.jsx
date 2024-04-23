import React from 'react';
import { useNavigate } from 'react-router-dom';
import ApiService from '../../../services/api-service';
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
import TextFieldComponent from '../../../components/text-field/text-field-component';
import LockIcon from '@mui/icons-material/Lock';
import LogoComponent from '../../../components/logo/logo-component';

const UserRegistrationForm = () => {
  const navigate = useNavigate();
  const [error, setError] = React.useState('');
  const [usernameError, setUsernameError] = React.useState('');
  const [nameError, setNameError] = React.useState('');
  const [surnameError, setSurnameError] = React.useState('');
  const [emailError, setEmailError] = React.useState('');
  const [passwordError, setPasswordError] = React.useState('');
  const [success, setSuccess] = React.useState('');

  const createUser = async (data) => {
    try {
      const res = await ApiService.createUser(data);
      if (res.status === 201) {
        setSuccess('User created successfully');
        navigate('/');
      }
    } catch (error) {
      if (error.response.status === 409) {
        setError(error.response.data);
      }
      if (error.response.status === 400) {
        setUsernameError(error.response.data.username);
        setNameError(error.response.data.name);
        setSurnameError(error.response.data.surname);
        setEmailError(error.response.data.email);
        setPasswordError(error.response.data.password);
      }
    }
  };

  const handleSubmit = (e) => {
    e.preventDefault();
    const formData = new FormData(e.currentTarget);

    const username = formData.get('username');
    const name = formData.get('name');
    const surname = formData.get('surname');
    const email = formData.get('email');
    const password = formData.get('password');
    const password2 = formData.get('password2');

    const lowerCaseEmail = email.toLowerCase();

    if (password !== password2) {
      setError('Passwords do not match');
    } else {
      setError('');
      const userRegistrationData = {
        username,
        name,
        surname,
        lowerCaseEmail,
        password,
      };

      createUser(userRegistrationData);
    }
  };

  return (
    <Container component="main" maxWidth="xs">
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
          Register
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
              label={'First Name'}
              name={'name'}
              type={'text'}
            />
            <TextFieldComponent
              defaultValue={''}
              required={true}
              label={'Last Name'}
              name={'surname'}
              type={'text'}
            />
            <TextFieldComponent
              defaultValue={''}
              required={true}
              label={'Email'}
              name={'email'}
              type={'text'}
            />
            <TextFieldComponent
              defaultValue={''}
              required={true}
              label={'Password'}
              name={'password'}
              type={'password'}
            />
            <TextFieldComponent
              defaultValue={''}
              required={true}
              label={'Repeat Password'}
              name={'password2'}
              type={'password'}
            />
          </Grid>
          <Button
            type="submit"
            fullWidth
            variant="contained"
            sx={{ mt: 3, mb: 2 }}
          >
            Register
          </Button>
          {error && (
            <Alert severity="error" variant="filled">
              {error}
            </Alert>
          )}
          {usernameError && (
            <Alert severity="error" variant="filled">
              {usernameError}
            </Alert>
          )}
          {nameError && (
            <Alert severity="error" variant="filled">
              {nameError}
            </Alert>
          )}
          {surnameError && (
            <Alert severity="error" variant="filled">
              {surnameError}
            </Alert>
          )}
          {emailError && (
            <Alert severity="error" variant="filled">
              {emailError}
            </Alert>
          )}
          {passwordError && (
            <Alert severity="error" variant="filled">
              {passwordError}
            </Alert>
          )}
          {success && (
            <Alert severity="success" variant="filled">
              {success}
            </Alert>
          )}
        </Box>
      </Box>
    </Container>
  );
};

export default UserRegistrationForm;
