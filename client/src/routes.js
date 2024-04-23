import { useRoutes } from 'react-router-dom';
import UserLoginPage from './pages/auth/user-login-page';
import UserRegistrationPage from './pages/auth/register/user-registration-page';
import EventsPage from './pages/events/events-page';
import CreateEvent from './pages/events-page/events/create-event';
import UserAccountPage from './pages/user/user-account-page';
import ExpenseListPage from './pages/expenses-page/expense-list-page';
import CreateExpense from './pages/expenses-page/create-expense';
import EventMembersPage from './pages/expenses-page/event-members-page';
import Unauthorized from './unauthorized/unauthorized';
import UpdateExpense from './pages/expenses-page/update-expense';
import BalancePage from './pages/events-page/balances/balance-page';

export const AppRouter = () => {
  const elements = useRoutes([
    {
      path: '/',
      element: <UserLoginPage />,
    },
    {
      path: '/register',
      element: <UserRegistrationPage />,
    },
    {
      path: '/events',
      element: <EventsPage />,
    },
    {
      path: '/createevent',
      element: <CreateEvent />,
    },
    {
      path: '/useraccount',
      element: <UserAccountPage />,
    },
    {
      path: '/event/:eventId',
      element: <ExpenseListPage />,
    },
    {
      path: '/createexpense/:eventName/:eventId',
      element: <CreateExpense />,
    },
    {
      path: '/eventmembers/:eventName/:eventId',
      element: <EventMembersPage />,
    },
    {
      path: '/unauthorized',
      element: <Unauthorized />,
    },
    {
      path: '/updateexpense/:eventId/:expenseId',
      element: <UpdateExpense />,
    },
    {
      path: '/balance/:eventName/:eventId',
      element: <BalancePage />,
    },
  ]);

  return elements;
};
