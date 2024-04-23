import './App.css';
import { BrowserRouter, Routes, Route } from 'react-router-dom';
import UserLoginPage from './pages/auth/login/user-login-page';
import UserRegistrationPage from './pages/auth/register/user-registration-page';
import { CssBaseline, ThemeProvider } from '@mui/material';
import theme from './theme/theme';
import EventsPage from './pages/events-page/events-page';
import CreateEvent from './pages/events-page/events/create-event';
import UserAccountPage from './pages/user/user-account-page';
import ExpenseListPage from './pages/expenses-page/expense-list-page';
import CreateExpense from './pages/expenses-page/create-expense';
import EventMembersPage from './pages/expenses-page/event-members-page';
import UpdateExpense from './pages/expenses-page/update-expense';
import BalancePage from './pages/events-page/balances/balance-page';

function App() {
  return (
    <ThemeProvider theme={theme}>
      <CssBaseline>
        <BrowserRouter>
          <Routes>
            <Route path="/" element={<UserLoginPage />} />
            <Route path="/register" element={<UserRegistrationPage />} />
            <Route path="/events" element={<EventsPage />} />
            <Route path="/createevent" element={<CreateEvent />} />
            <Route path="/useraccount" element={<UserAccountPage />} />
            <Route path="/event/:eventId" element={<ExpenseListPage />} />
            <Route
              path="/createexpense/:eventName/:eventId"
              element={<CreateExpense />}
            />
            <Route
              path="/eventmembers/:eventName/:eventId"
              element={<EventMembersPage />}
            />
            <Route
              path="/updateexpense/:eventId/:expenseId"
              element={<UpdateExpense />}
            />
            <Route
              path="/balance/:eventName/:eventId"
              element={<BalancePage />}
            />
          </Routes>
        </BrowserRouter>
      </CssBaseline>
    </ThemeProvider>
  );
}

export default App;
