import { createSlice, createAsyncThunk } from '@reduxjs/toolkit';
import api from '../../api';

export const getUsers = createAsyncThunk('users/getUsers', async () => {
    const [ customerResult, delivererResult, dispatcherResult ] = await Promise.all([
        api.get('/delivery/users?role=Customer'),
        api.get('/delivery/users?role=Deliverer'),
        api.get('/delivery/users?role=Dispatcher'),
    ]);
    return {
        customers: customerResult.data,
        deliverers: delivererResult.data,
        dispatchers: dispatcherResult.data,
    };
});

export const updateUser = createAsyncThunk('users/updateUser', async ({user, role}) => {
    const updatedUser = (await api.put('/delivery/users', user)).data;
    return { user: updatedUser, role };
});

export const addUser = createAsyncThunk('users/addUser', async ({user, role}) => {
    const newUser = (await api.post('/delivery/users', user)).data;
    return { user: newUser, role };
});

export const deleteUser = createAsyncThunk('users/deleteUser', async ({user, role}) => {
    await api.delete(`/delivery/user/${user.username}`);
    return { user, role };
});

const initialState = {
    customers: [],
    deliverers: [],
    dispatchers: [],
    getUsersLoading: false,
    updateUserLoading: false,
    addUserLoading: false,
};

const usersSlice = createSlice({
    name: 'users',
    initialState,
    reducers: {},
    extraReducers: {
        [getUsers.pending]: (state) => {
            state.getUsersLoading = true;
        },
        [getUsers.fulfilled]: (state, { payload }) => {
            state.getUsersLoading = false;
            state.customers = payload.customers;
            state.deliverers = payload.deliverers;
            state.dispatchers = payload.dispatchers;
        },
        [getUsers.rejected]: (state) => {
            state.getUsersLoading = false;
        },
        [updateUser.pending]: (state) => {
            state.updateUserLoading = true;
        },
        [updateUser.fulfilled]: (state, { payload }) => {
            state.updateUserLoading = false;
            const userIndex = state[payload.role + 's'].findIndex(user => user.username === payload.user.username);
            if (userIndex >= 0) {
                state[payload.role + 's'][userIndex] = payload.user;
            }
        },
        [updateUser.rejected]: (state) => {
            state.updateUserLoading = false;
        },
        [addUser.pending]: (state) => {
            state.addUserLoading = true;
        },
        [addUser.fulfilled]: (state, { payload }) => {
            state.addUserLoading = false;
            state[payload.role + 's'].push(payload.user);
        },
        [addUser.rejected]: (state) => {
            state.addUserLoading = false;
        },
        [deleteUser.fulfilled]: (state, { payload }) => {
            const userIndex = state[payload.role + 's'].findIndex(user => user.username === payload.user.username);
            if (userIndex >= 0) {
                state[payload.role + 's'].splice(userIndex, 1);
            }
        },
    },
});

export default usersSlice.reducer;
