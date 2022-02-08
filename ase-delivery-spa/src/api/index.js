import axios from 'axios';

export const api_url = 'http://ec2-3-120-178-127.eu-central-1.compute.amazonaws.com:10789/api';
// export const api_url = process.env.REACT_APP_API_BASE_URL;
// export const api_url = "http://ase-delivery-gateway-service:10789/api";

// TODO: change this to the real API instance
const apiInstance = axios.create({
  baseURL: api_url,
  withCredentials: true,
});

export default apiInstance;
