import axios from 'axios';

export const api_url = 'http://localhost:10789/api';

// TODO: change this to the real API instance
const apiInstance = axios.create({
  baseURL: api_url,
  withCredentials: true,
});

export default apiInstance;
