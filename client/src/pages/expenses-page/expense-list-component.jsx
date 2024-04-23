import React from 'react';
import {
  List,
  ListItem,
  ListItemText,
  ListItemButton,
  Typography,
} from '@mui/material';
import ApiService from '../../services/api-service';
import PropTypes from 'prop-types';
import EditIcon from '@mui/icons-material/Edit';
import DeleteForeverIcon from '@mui/icons-material/DeleteForever';
import { useNavigate } from 'react-router-dom';

const ExpenseListComponent = ({ eventId }) => {
  const navigate = useNavigate();
  const [expenseList, setEventsList] = React.useState([]);

  React.useEffect(() => {
    (async () => {
      const expenseList = await ApiService.getExpensesPerEvent(eventId);
      setEventsList(expenseList.data);
    })();
    // eslint-disable-next-line react-hooks/exhaustive-deps
  }, []);

  const handleDelete = async (expenseId) => {
    await ApiService.deleteExpenseById(expenseId);
    const updatedExpenseList = await ApiService.getExpensesPerEvent(eventId);
    setEventsList(updatedExpenseList.data);
  };

  return (
    <>
      {expenseList.length === 0 && (
        <Typography variant="h5" component="h2">
          Please create new expense
        </Typography>
      )}
      {expenseList && (
        <List
          sx={{
            display: 'flex',
            flexDirection: 'column',
            alignItems: 'center',
          }}
        >
          {expenseList.map((expense, index) => (
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
              <ListItemButton
                component="a"
                href={`#${expense.expenseReason}`}
                sx={{ my: 0, py: 0 }}
              >
                <ListItemText
                  primary={
                    <Typography variant="body2">
                      {expense.expenseReason +
                        ' Paid by ' +
                        expense.userId.username +
                        ' Total: ' +
                        expense.amountSpent +
                        '€'}
                    </Typography>
                  }
                />
              </ListItemButton>
              <DeleteForeverIcon
                sx={{ color: 'primary.main' }}
                onClick={() => handleDelete(expense.expenseId)}
              />
              <EditIcon
                sx={{ color: 'primary.main' }}
                onClick={() =>
                  navigate(`/updateexpense/${eventId}/${expense.expenseId}`)
                }
              />
            </ListItem>
          ))}
        </List>
      )}
    </>
  );
};

ExpenseListComponent.propTypes = {
  defaultValue: PropTypes.string,
  eventId: PropTypes.string.isRequired,
};

export default ExpenseListComponent;
