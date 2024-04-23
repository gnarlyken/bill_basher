/* eslint-disable react/prop-types */
import React from 'react';
import {
  List,
  ListItem,
  ListItemText,
  ListItemButton,
  Checkbox,
  Typography,
} from '@mui/material';

const EventMembersListComponent = ({ onCheckedItemsChange, peopleList }) => {
  const [checkedItems, setCheckedItems] = React.useState({});

  const handleToggle = (person) => {
    setCheckedItems((prev) => ({
      ...prev,
      [person.username]: !prev[person.username],
    }));
    onCheckedItemsChange({
      ...checkedItems,
      [person.username]: !checkedItems[person.username],
    });
  };

  return (
    <List
      sx={{
        display: 'flex',
        flexDirection: 'column',
        alignItems: 'center',
      }}
    >
      {peopleList.map((person, index) => (
        <ListItem
          key={index}
          disablePadding
          sx={{
            my: 0.5,
            bgcolor: 'secondary.main',
            width: '100%',
            borderRadius: 2,
          }}
        >
          <ListItemButton component="div" sx={{ my: 0, py: 0 }}>
            <Checkbox
              sx={{ m: 0, p: 0 }}
              checked={checkedItems[person.username] || false}
              onChange={() => handleToggle(person)}
            />
            <ListItemText
              primary={
                <Typography variant="body2">{person.username}</Typography>
              }
            />
          </ListItemButton>
        </ListItem>
      ))}
    </List>
  );
};

export default EventMembersListComponent;
