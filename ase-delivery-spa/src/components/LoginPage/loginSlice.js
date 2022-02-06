import { createSlice } from '@reduxjs/toolkit';

export const loginSlice = createSlice({
    name: 'login',
    initialState: {
        userRole: null,
        uid: null,
        uname: null,
        isLoggedIn: false,
    },
    reducers: {
        setUser: (state, action) => {
            state.userRole = action.payload.userRole;
            state.uid = action.payload.uid;
            state.uname = action.payload.uname;
        },
        setUserRole: (state, action) => {
            state.userRole = action.payload.userRole;
        },
        setUserIdAndUsername: (state, action) => {
            state.uid = action.payload.uid;
            state.uname = action.payload.uname;
        },
        setLoginStatus: (state, action) => {
            state.isLoggedIn = action.payload.loginStatus;
        }
    }
})

export const { setUser, setUserRole, setUserIdAndUsername, setLoginStatus } = loginSlice.actions;

export default loginSlice.reducer;