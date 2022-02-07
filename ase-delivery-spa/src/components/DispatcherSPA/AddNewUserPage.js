import React, { useState } from 'react';
import { useDispatch } from 'react-redux';
import { Input, Modal, Row, Col, } from 'antd';
import { UserOutlined } from '@ant-design/icons';
import { addUser } from './UsersSlice';

export default function AddNewUserPage(props) {
    const [username, setUsername] = useState('')
    const [email, setEmail] = useState('')
    const [password, setPassword] = useState('')

    const onUsernameChanged = e => setUsername(e.target.value)
    const onEmailChanged = e => setEmail(e.target.value)
    const onPasswordChanged = e => setPassword(e.target.value)

    const dispatch = useDispatch()

    return (
        <Modal 
            title={`${props.actionType} ${props.activeTabName}`} 
            visible={props.visible}
            onOk={() => {
                if (username && email && password) {
                    dispatch(
                        addUser({
                            user: {
                                username,
                                email,
                                password,
                                role: props.activeTabName.charAt(0).toUpperCase() + props.activeTabName.slice(1),
                            },
                            role: props.activeTabName,
                        })
                    );
                }
                props.handleOk(props.handleOk);
            }} 
            onCancel={props.handleCancel} 
        >
            <Row gutter={8}>
                <Col span={5}>
                    <p>Username:</p>
                </Col>
                <Col span={15}>
                    <Input
                    placeholder="Enter username"
                    value={username}
                    onChange={onUsernameChanged}
                    prefix={<UserOutlined className="site-form-item-icon" />}
                    />
                </Col>
            </Row>
            <Row gutter={8}>
                <Col span={5}>
                    <p>Email:</p>
                </Col>
                <Col span={15}>
                    <Input 
                    placeholder="Email Address" 
                    value={email}
                    onChange={onEmailChanged}
                    />
                </Col>
            </Row>
            <Row gutter={8}>
                <Col span={5}>
                    <p>Password:</p>
                </Col>
                <Col span={15}>
                    <Input 
                    placeholder="Password" 
                    value={password}
                    onChange={onPasswordChanged}
                    />
                </Col>
            </Row>
        </Modal>
    )    
}