import { Box, Button } from '@mui/material';
import { useNavigate } from 'react-router';

const NavBar = () => {
  const navigate = useNavigate();
  return (
    <Box display="flex" justifyContent="flex-end" sx={{ width: '100%' }}>
      <Button
        variant="contained"
        sx={{ mt: 3, width: '50%', borderRadius: 0, height: 40 }}
        onClick={() => navigate('/events')}
      >
        Events
      </Button>
      <Button
        variant="contained"
        sx={{ mt: 3, width: '50%', borderRadius: 0 }}
        onClick={() => navigate('/useraccount')}
      >
        Account
      </Button>
    </Box>
  );
};

export default NavBar;
