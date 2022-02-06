import { createSlice, createAsyncThunk } from '@reduxjs/toolkit';
import { api_url, status_codes, getXSRFToken } from '../Common/utils';
import { message } from 'antd';
import axios from 'axios';

export const getDeliveries = createAsyncThunk(
    'delivery/getDeliveries',
    async () => {
        const response = await axios({
            method: 'GET',
            url: `${api_url}/delivery/deliveries`,
            withCredentials: true
        });

        let data = [];

        for (let item in response.data) {
            data.push({
                trackingCode: response.data[item]['trackingCode'],
                customer: response.data[item]['customer']['username'],
                deliverer: response.data[item]['deliverer']['username'],
                targetBox: response.data[item]['targetBox'],
                statuses: response.data[item]['statuses'],
                status: Math.max.apply(Math, response.data[item]['statuses'].map(function (o) { return status_codes[o.status]; })),
            })
        }
        return data;
    }
)

export const addDelivery = createAsyncThunk('delivery/addDelivery',
    async (delivery) => {
        const response = await axios({
            method: 'POST',
            url: `${api_url}/delivery/deliveries`,
            withCredentials: true,
            headers: {
                'X-XSRF-TOKEN': getXSRFToken(),
                'Content-Type': 'application/json',
            },
            data: delivery
        });
        return response.data;
    });

export const updateDelivery = createAsyncThunk('delivery/updateDelivery',
    async (delivery) => {
        const response = await axios({
            method: 'PUT',
            url: `${api_url}/delivery/deliveries/${delivery.trackingCode}`,
            withCredentials: true,
            headers: {
                'X-XSRF-TOKEN': getXSRFToken(),
                'Content-Type': 'application/json',
            },
            data: delivery
        });
        return response.data;
    });

export const deleteDelivery = createAsyncThunk('delivery/deleteDelivery',
    async (trackingCode) => {
        const response = await axios({
            method: 'DELETE',
            url: `${api_url}/delivery/deliveries/${trackingCode}`,
            withCredentials: true,
            headers: {
                'X-XSRF-TOKEN': getXSRFToken(),
            }
        });
        return { status: response.status, code: trackingCode };
    });

const initialState = {
    deliveries: [],
};

const deliverySlice = createSlice({
    name: 'delivery',
    initialState,
    reducers: {},
    extraReducers: {
        [getDeliveries.fulfilled]: (state, { payload }) => {
            state.deliveries = payload;
        },
        [addDelivery.fulfilled]: (state, { payload }) => {
                state.deliveries.push({
                    trackingCode: payload.trackingCode,
                    customer: payload.customer.username,
                    deliverer: payload.deliverer.username,
                    targetBox: payload.targetBox,
                    statuses: payload.statuses,
                    status: Math.max.apply(Math, payload['statuses'].map(function (o) { return status_codes[o.status]; })),
                });
                message.success('Delivery added');
        },
        [updateDelivery.fulfilled]: (state, { payload }) => {
            const deliveryIndex = state.deliveries.findIndex(delivery => delivery.trackingCode === payload.trackingCode);
            state.deliveries[deliveryIndex] = {
                trackingCode: payload.trackingCode,
                customer: payload.customer.username,
                deliverer: payload.deliverer.username,
                targetBox: payload.targetBox,
                statuses: payload.statuses,
                status: Math.max.apply(Math, payload['statuses'].map(function (o) { return status_codes[o.status]; }))
            };
        },
        [deleteDelivery.fulfilled]: (state, { payload }) => {
            // console.log(state);
            if (payload.status === 200) {
                message.success('Delivery deleted');
                const deliveryIndex = state.deliveries.findIndex(delivery => delivery.trackingCode === payload.code);
                // console.log(deliveryIndex, state.deliveries);
                state.deliveries.splice(deliveryIndex, 1);
            } else {
                message.error('Deletion failed');
            }
        },
    },
});

export default deliverySlice.reducer;
