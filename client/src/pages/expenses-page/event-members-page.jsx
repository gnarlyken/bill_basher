import React from 'react';
import {
  Box,
  Container,
  CssBaseline,
  Typography,
  List,
  ListItem,
  ListItemText,
  ListItemButton,
  IconButton,
} from '@mui/material';
import NavBar from '../../components/layout/navbar/navbar';
import LogoComponent from '../../components/logo/logo-component';
import { useParams } from 'react-router-dom';
import ApiService from '../../services/api-service';
import ArrowBackIcon from '@mui/icons-material/ArrowBack';
import { useNavigate } from 'react-router-dom';

const EventMembersPage = () => {
  const navigate = useNavigate();
  const { eventName, eventId } = useParams();
  const [users, setUsers] = React.useState([]);

  React.useEffect(() => {
    (async () => {
      const users = await ApiService.getUsersPerEvent(eventId);
      setUsers(users.data);
    })();
    // eslint-disable-next-line react-hooks/exhaustive-deps
  }, []);

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
          <List
            sx={{
              display: 'flex',
              flexDirection: 'column',
              alignItems: 'center',
              width: '100%',
            }}
          >
            {users.map((user, index) => (
              <ListItem
                key={index}
                disablePadding
                sx={{
                  my: 0.5,
                  bgcolor: user.isActive ? 'secondary.main' : 'text.disabled',
                  width: '100%',
                  borderRadius: 2,
                }}
              >
                <ListItemButton
                  component="a"
                  href={`#${user.username}`}
                  sx={{ my: 0, py: 0 }}
                >
                  <ListItemText
                    primary={
                      <Typography variant="body2">{user.username}</Typography>
                    }
                  />
                </ListItemButton>
              </ListItem>
            ))}
          </List>
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

export default EventMembersPage;
