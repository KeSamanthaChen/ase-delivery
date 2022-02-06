import { Button, message } from 'antd';
import axios from 'axios';
import { useDispatch } from 'react-redux';
import { setUserRole, setUserIdAndUsername, setLoginStatus } from './loginSlice';
import { api_url } from '../Common/utils';

export default function LoginButton(props) {
    const dispatch = useDispatch();

    // let user;
    let submit = function () {
        let base = Buffer.from(`${props.username}:${props.password}`).toString('base64');
        const hide = message.loading('Logging in...');
        axios.get(`${api_url}/auth/csrf`, {withCredentials: true})
            .then(response => {
                if (response.status === 200) {
                    return axios({
                        method: 'POST',
                        url: `${api_url}/auth`,
                        withCredentials: true,
                        headers: {
                            'Authorization': `Basic ${base}`,
                        }
                    });
                }
        })
        .then(response => {
            if (response.status === 200) {
                return axios({
                    method: 'GET',
                    withCredentials: true,
                    url: `${api_url}/auth/current-session`,
                    headers: {
                        'Authorization': `Basic ${base}`,
                    }
                });
            }
        }).then(response => {
            dispatch(setUserRole({userRole: response.data['role'].toLowerCase()}));
            return axios({
                method: 'GET',
                withCredentials: true,
                url: `${api_url}/delivery/users/${props.username}`
                });
        }).then(response => {
            if (response.status === 200) {
                dispatch(setUserIdAndUsername({uid: response.data['id'], uname: response.data['username']}));
                dispatch(setLoginStatus({loginStatus: true}));
            };
            hide();
        }).catch(error => {
            message.error('Wrong credentials or bad Internet connection');
            console.log(error);
            hide();
        })
    };

    return (
        <div>
            <Button type="primary"
                style={{ margin: '10px' }}
                onClick={submit}>
                Log In
            </Button>
        </div>
    )
}

