import React from 'react';
import { useNavigate } from 'react-router-dom';
import {
  Box,
  Button,
  Container,
  IconButton,
  CssBaseline,
  TextField,
  Typography,
} from '@mui/material';
import ExpenseListComponent from './expense-list-component';
import NavBar from '../../components/layout/navbar/navbar';
import LogoComponent from '../../components/logo/logo-component';
import { useParams } from 'react-router-dom';
import ApiService from '../../services/api-service';
import ArrowBackIcon from '@mui/icons-material/ArrowBack';
import EditIcon from '@mui/icons-material/Edit';
import SaveIcon from '@mui/icons-material/Save';

const ExpenseListPage = () => {
  const navigate = useNavigate();
  const { eventId } = useParams();
  const [eventName, setEventName] = React.useState('');
  const [editedEventName, setEditedEventName] = React.useState('');
  const [creatorData, setCreatorData] = React.useState('');
  const [isEditing, setIsEditing] = React.useState(false);

  React.useEffect(() => {
    (async () => {
      const eventData = await ApiService.getEventById(eventId);
      setCreatorData(eventData.data.userId);
      setEventName(eventData.data.eventName);
    })();
    // eslint-disable-next-line react-hooks/exhaustive-deps
  }, []);

  const onFinishEvent = async () => {
    await ApiService.finishEvent(
      creatorData,
      eventId,
      !editedEventName ? eventName : editedEventName,
    );
    navigate('/events');
  };

  const handleEdit = () => {
    setEditedEventName(!editedEventName ? eventName : editedEventName);
    setIsEditing(true);
  };

  const handleSave = async () => {
    setIsEditing(false);
    await ApiService.updateEvent(creatorData, eventId, editedEventName);
  };

  const handleCancel = () => {
    setIsEditing(false);
    setEditedEventName('');
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
          <Box sx={{ display: 'flex', alignItems: 'center' }}>
            {!isEditing ? (
              <>
                <Typography
                  variant="h4"
                  component="h1"
                  sx={{ color: 'primary.dark', marginRight: '8px' }}
                >
                  {!editedEventName ? eventName : editedEventName}
                </Typography>
                <IconButton
                  aria-label="edit"
                  sx={{ bgcolor: 'secondary.main' }}
                  onClick={handleEdit}
                >
                  <EditIcon sx={{ color: 'primary.main' }} />
                </IconButton>
              </>
            ) : (
              <>
                <TextField
                  sx={{ backgroundColor: 'secondary.main', marginRight: '8px' }}
                  value={editedEventName}
                  onChange={(e) => setEditedEventName(e.target.value)}
                  fullWidth
                  label="Event Name"
                  variant="filled"
                />
                <IconButton
                  aria-label="save"
                  sx={{ bgcolor: 'secondary.main' }}
                  onClick={handleSave}
                >
                  <SaveIcon sx={{ color: 'primary.main' }} />
                </IconButton>
                <IconButton
                  aria-label="cancel"
                  sx={{ bgcolor: 'secondary.main' }}
                  onClick={handleCancel}
                >
                  <ArrowBackIcon sx={{ color: 'primary.main' }} />
                </IconButton>
              </>
            )}
          </Box>
          <Box display="flex" justifyContent="flex-end" sx={{ width: '100%' }}>
            <Button
              variant="contained"
              sx={{ mt: 3, mb: 2, width: '50%' }}
              onClick={() => navigate(`/eventmembers/${eventName}/${eventId}`)}
            >
              View Members
            </Button>
            <Button
              variant="contained"
              sx={{ mt: 3, mb: 2, width: '50%', ml: 2 }}
              onClick={() => navigate(`/createexpense/${eventName}/${eventId}`)}
            >
              Add new Expense
            </Button>
            <Button
              variant="contained"
              sx={{ mt: 3, mb: 2, width: '50%', ml: 2 }}
              onClick={onFinishEvent}
            >
              Finish Event
            </Button>
            <Button
              variant="contained"
              sx={{ mt: 3, mb: 2, width: '50%', ml: 2 }}
              onClick={() => navigate(`/balance/${eventName}/${eventId}`)}
            >
              View Balance
            </Button>
          </Box>

          <Box sx={{ width: '100%', height: '100%' }}>
            <ExpenseListComponent eventId={eventId} />
          </Box>
        </Box>
        <IconButton
          aria-label="back"
          sx={{ bgcolor: 'secondary.main' }}
          onClick={() => navigate(`/events`)}
        >
          <ArrowBackIcon sx={{ color: 'primary.main' }} />
        </IconButton>
        <NavBar />
      </Container>
    </>
  );
};

export default ExpenseListPage;
