import React from 'react';
import {
  List,
  ListItem,
  ListItemText,
  ListItemButton,
  Typography,
} from '@mui/material';
import ApiService from '../../../../services/api-service';
import { useSelector } from 'react-redux';
import { useNavigate } from 'react-router-dom';
import DeleteForeverIcon from '@mui/icons-material/DeleteForever';

const EventsListComponent = () => {
  const navigate = useNavigate();
  const user = useSelector((state) => state.user.user);
  const userId = user.userId;
  const [eventsList, setEventsList] = React.useState([]);

  React.useEffect(() => {
    (async () => {
      const eventsList = await ApiService.getUserEvents(userId);
      setEventsList(eventsList.data);
    })();
    // eslint-disable-next-line react-hooks/exhaustive-deps
  }, []);

  const handleDelete = async (eventId) => {
    await ApiService.deleteEventById(eventId);
    const updatedEventsList = await ApiService.getUserEvents(userId);
    setEventsList(updatedEventsList.data);
  };

  return (
    <>
      {eventsList.length === 0 && (
        <Typography variant="h5" component="h2">
          You do not have any events yet
        </Typography>
      )}
      {eventsList && (
        <List
          sx={{
            display: 'flex',
            flexDirection: 'column',
            alignItems: 'center',
          }}
        >
          {eventsList.map((event, index) => (
            <ListItem
              key={index}
              disablePadding
              sx={{
                my: 0.5,
                bgcolor: event.eventActive ? 'secondary.main' : 'text.disabled',
                width: '100%',
                borderRadius: 2,
              }}
            >
              <ListItemButton
                component="a"
                href={`#${event.eventName}`}
                sx={{ my: 0, py: 0 }}
                onClick={() => navigate(`/event/${event.eventId}`)}
              >
                <ListItemText
                  primary={
                    <Typography variant="body2">{event.eventName}</Typography>
                  }
                />
              </ListItemButton>
              {!event.eventActive && (
                <DeleteForeverIcon
                  sx={{ color: 'secondary.main' }}
                  onClick={() => handleDelete(event.eventId)}
                />
              )}
            </ListItem>
          ))}
        </List>
      )}
    </>
  );
};

export default EventsListComponent;
