import { createSlice } from '@reduxjs/toolkit';

export const dispatcherSlice = createSlice({
    name: 'dispatcher',
    initialState: {
        currentTab: 'customer',
    },
    reducers: {
        setCurrentTab: (state, action) => {
            state.currentTab = action.payload;
        }
    }
})

export const { setCurrentTab } = dispatcherSlice.actions;

export default dispatcherSlice.reducer;
