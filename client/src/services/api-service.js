import axios from 'axios';

const api = axios.create({
  baseURL: 'http://localhost:8080/api/v1',
  timeout: 5000,
  headers: {
    Accept: 'application/json',
    'Content-Type': 'application/json',
  },
});

const createUser = async (data) => {
  const res = await api.post('/register', {
    username: data.username,
    name: data.name,
    surname: data.surname,
    email: data.lowerCaseEmail,
    password: data.password,
  });
  return res;
};

const deleteUser = async (userId) => {
  const data = await api.put(`/users/deactivate/${userId}`);
  return data;
};

const login = async (data) => {
  const res = await api.post('/login', {
    usernameOrEmail: data.usernameOrEmail,
    password: data.password,
  });
  return res;
};

const validateToken = async (token) => {
  const data = await api.get(`/token/validate/${token}`);
  return data;
};

const getUser = async (userId) => {
  const data = await api.get(`/users/${userId}`);
  return data;
};

const getUsers = async () => {
  const data = await api.get('/users');
  return data;
};

const getUserEvents = async (userId) => {
  const data = await api.get(`/events/by-user/${userId}`);
  return data;
};

const createEvent = async (userId, eventName) => {
  const res = await api.post('/events', {
    userId: {
      userId: userId,
    },
    eventActive: true,
    eventName: eventName,
  });
  return res;
};

const updateEvent = async (user, eventId, eventName) => {
  const res = await api.put(`/events/${eventId}`, {
    eventId: eventId,
    userId: user,
    eventActive: true,
    eventName: eventName,
  });
  return res;
};

const finishEvent = async (user, eventId, eventName) => {
  const res = await api.put(`/events/${eventId}`, {
    eventId: eventId,
    userId: user,
    eventActive: false,
    eventName: eventName,
  });
  return res;
};

const addMembersToEvent = async (userId, eventId) => {
  const res = await api.post('/user-event/add', {
    userId: {
      userId,
    },
    eventId: {
      eventId,
    },
    total: 0,
  });
  return res;
};

const getEventById = async (eventId) => {
  const data = await api.get(`/events/${eventId}`);
  return data;
};

const deleteEventById = async (eventId) => {
  const data = await api.delete(`/events/${eventId}`);
  return data;
};

const getEventBalanceById = async (eventId) => {
  const data = await api.get(`/balance/${eventId}`);
  return data;
};

const getExpensesPerEvent = async (eventId) => {
  const data = await api.get(`/expenses/event/${eventId}`);
  return data;
};

const createExpense = async (data) => {
  const res = await api.post('/expenses', {
    userId: {
      userId: data.userId,
    },
    eventId: {
      eventId: data.eventId,
    },
    expenseReason: data.expenseName,
    amountSpent: data.amountSpent,
  });
  return res;
};

const getUsersPerEvent = async (eventId) => {
  const data = await api.get(`/users/by-event/${eventId}`);
  return data;
};

const getExpenseById = async (expenseId) => {
  const data = await api.get(`/expenses/${expenseId}`);
  return data;
};

const updateExpenseById = async (data) => {
  const expenseId = data.expenseId;
  const expenseCreated = data.expenseCreated;
  const res = await api.put(`/expenses/${expenseId}`, {
    expenseId,
    eventId: data.event,
    userId: data.user,
    expenseReason: data.expenseName,
    amountSpent: data.amountSpent,
    expenseCreated,
  });
  return res;
};

const deleteExpenseById = async (expenseId) => {
  const data = await api.delete(`/expenses/remove/${expenseId}`);
  return data;
};

const ApiService = {
  createUser,
  deleteUser,
  login,
  validateToken,
  getUser,
  getUsers,

  getUserEvents,
  createEvent,
  updateEvent,
  finishEvent,
  addMembersToEvent,
  getEventById,
  deleteEventById,
  getEventBalanceById,

  getExpensesPerEvent,
  createExpense,
  getExpenseById,
  updateExpenseById,
  deleteExpenseById,
  getUsersPerEvent,
};

export default ApiService;
