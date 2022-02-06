import { createSlice, createAsyncThunk } from '@reduxjs/toolkit';
import api from '../../api';

export const getBoxes = createAsyncThunk('box/getBoxes', async () => {
    return (await api.get('/delivery/boxes')).data;
});

export const updateBox = createAsyncThunk('box/updateBox', async (box) => {
    return (await api.put('/delivery/boxes', box)).data;
});

export const addBox = createAsyncThunk('box/addBox', async (box) => {
    return (await api.post('/delivery/boxes', box)).data;
});

export const deleteBox = createAsyncThunk('box/deleteBox', async (id) => {
    await api.delete(`/delivery/boxes/${id}`);
    return id;
});

const initialState = {
    boxes: [],
};

const boxSlice = createSlice({
    name: 'box',
    initialState,
    reducers: {},
    extraReducers: {
        [getBoxes.fulfilled]: (state, { payload }) => {
            state.boxes = payload;
        },
        [updateBox.fulfilled]: (state, { payload }) => {
            const boxIndex = state.boxes.findIndex(box => box.id === payload.id);
            state.boxes[boxIndex] = payload;
        },
        [addBox.fulfilled]: (state, { payload }) => {
            state.boxes.push(payload);
        },
        [deleteBox.fulfilled]: (state, { payload }) => {
          const boxIndex = state.boxes.findIndex(box => box.id === payload);
          if (boxIndex >= 0) {
            state.boxes.splice(boxIndex, 1);
          }
      },
    },
});

export default boxSlice.reducer;
