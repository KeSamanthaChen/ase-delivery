import React from 'react';
import { Card } from 'antd';
import LoginPanel from './LoginPanel';


class LoginPage extends React.Component {
    onActiveTabKeyChanged(key) {
        this.setState({activeTabKey: key});
    }
    
    render() {
    
        const cardStyle = { 
            width: '90%', 
            borderRadius: '12px', 
            margin: 'auto',
            marginTop: '15px',
            textAlign: 'center',
            boxShadow: "0px 0px 8px rgba(208, 216, 243, 0.6)",
         };

        return (
            <div id="loginPage">
                <Card style={cardStyle}
                    title="Log In">                        <LoginPanel />
                </Card>
            </div>
        )
    }
}

export default LoginPage;