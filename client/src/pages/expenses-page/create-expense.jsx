import { useNavigate } from 'react-router-dom';
// import { useSelector } from 'react-redux';
import {
  Box,
  Button,
  Container,
  CssBaseline,
  IconButton,
  Grid,
  Typography,
} from '@mui/material';
import NavBar from '../../components/layout/navbar/navbar';
import LogoComponent from '../../components/logo/logo-component';
import { useParams } from 'react-router-dom';
import { useSelector } from 'react-redux';
import ApiService from '../../services/api-service';
import TextFieldComponent from '../../components/text-field/text-field-component';
import ArrowBackIcon from '@mui/icons-material/ArrowBack';

const CreateExpense = () => {
  const navigate = useNavigate();
  const { eventName, eventId } = useParams();
  const user = useSelector((state) => state.user.user);
  const userId = user.userId;

  const createExpense = async (data) => {
    try {
      await ApiService.createExpense(data);
      navigate(`/event/${eventId}`);
    } catch (error) {
      if (error) {
        console.log(error);
      }
    }
  };

  const handleSubmit = (e) => {
    e.preventDefault();
    const formData = new FormData(e.currentTarget);

    const expenseName = formData.get('expenseName');
    const amountSpent = formData.get('amountSpent');

    const expenseCreationData = {
      eventId,
      userId,
      expenseName,
      amountSpent,
    };
    createExpense(expenseCreationData);
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
            {eventName}
          </Typography>
          <Box
            component="form"
            noValidate
            onSubmit={handleSubmit}
            sx={{
              mt: 3,
              width: '100%',
              display: 'flex',
              flexDirection: 'column',
              justifyContent: 'space-between',
            }}
          >
            <Grid container spacing={2}>
              <Grid item xs={6}>
                <TextFieldComponent
                  defaultValue={''}
                  required={true}
                  label={'Expense reason'}
                  name={'expenseName'}
                  type={'text'}
                />
              </Grid>
              <Grid item xs={6}>
                <TextFieldComponent
                  defaultValue={''}
                  required={true}
                  label={'Amount spent'}
                  name={'amountSpent'}
                  type={'text'}
                />
              </Grid>
            </Grid>
            <Button
              type="submit"
              fullWidth
              variant="contained"
              sx={{ mt: 3, mb: 2 }}
            >
              Add Expense
            </Button>
          </Box>
        </Box>
        <IconButton
          aria-label="back"
          sx={{ bgcolor: 'secondary.main' }}
          onClick={() => navigate(`/event/${eventId}`)}
        >
          <ArrowBackIcon sx={{ color: 'primary.main' }} />
        </IconButton>
        <NavBar />
      </Container>
    </>
  );
};

export default CreateExpense;
