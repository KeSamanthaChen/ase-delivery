import { Tag } from 'antd';

export const status_codes = {
    "ORDERED": 0,
    "DELIVERING": 1,
    "DELIVERED": 2,
    "COMPLETE": 3
};

export const codes_status = {
    0: "ORDERED",
    1: "DELIVERING",
    2: "DELIVERED",
    3: "COMPLETE"
}

export { api_url } from '../../api';

export function getXSRFToken() {
    return document.cookie.split('; ')
    .find(row => row.startsWith('XSRF-TOKEN='))
    .split('=')[1];
    
}

export function getTag(status) {
    switch (status) {
        case 0:
            return <Tag color="volcano">Central Deposit</Tag>;
        case 1:
            return <Tag color="blue">Delivering</Tag>;
        case 2:
            return <Tag color="cyan">Delivered</Tag>;
        case 3:
            return <Tag color="green">Picked Up</Tag>;
        default:
            return <Tag color="red">Error</Tag>;
    }
}