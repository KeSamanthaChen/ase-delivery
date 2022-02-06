import React from 'react';
import { Input} from 'antd';
import { UserOutlined, KeyOutlined } from '@ant-design/icons';
import LoginButton from './LoginButton';

class LoginPanel extends React.Component {
    constructor(props) {
        super(props);
        this.state = {
            username: null,
            password: null,
        };
    }

    onUsernameChanged(e) {
        this.setState({
            username: e.target.value,
        });
    }

    onPasswordChanged(e) {
        this.setState({
            password: e.target.value,
        });
    }

    render() {

        return (
            <div id='loginPanel'>
                <Input placeholder="Enter your username"
                    style={{ margin: '10px', maxWidth: '500px' }}
                    prefix={<UserOutlined />}
                    onChange={e => { this.onUsernameChanged(e) }} />
                <br />
                <Input.Password placeholder="Enter your password"
                    style={{ margin: '10px', maxWidth: '500px' }}
                    prefix={<KeyOutlined />}
                    onChange={e => { this.onPasswordChanged(e) }} />
                <br />
                <LoginButton username={this.state.username}
                            password={this.state.password}
                            />
            </div>
        )
    }
}

export default LoginPanel;