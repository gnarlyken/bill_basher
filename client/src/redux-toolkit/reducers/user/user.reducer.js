import { createSlice } from '@reduxjs/toolkit';

const initialState = {
  token: '',
  user: null,
};

const userSlice = createSlice({
  name: 'user',
  initialState,
  reducers: {
    addUser: (state, action) => {
      const { token, user } = action.payload;
      state.token = token;
      state.user = user;
    },
    clearUser: (state) => {
      state.token = '';
      state.user = null;
    },
  },
});

export const { addUser, clearUser } = userSlice.actions;
export default userSlice.reducer;
