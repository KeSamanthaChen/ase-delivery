import React, { useState } from 'react';
import { useDispatch } from 'react-redux';
import { Input, Modal, Row, Col, Select } from 'antd';
import { UserOutlined } from '@ant-design/icons';
import { updateUser } from './UsersSlice';

const { Option } = Select;

export default function EditUserPage(props) {
    const [username] = useState(props.defaultData.username);
    const [email, setEmail] = useState(props.defaultData.email);
    const [rfidToken, setRFIDToken] = useState(props.defaultData.rfidToken);
    const [role, setRole] = useState(props.activeTabName);
    const dispatch = useDispatch();

    const onEmailChanged = e => setEmail(e.target.value);
    const onRFIDChanged = e => setRFIDToken(e.target.value);
    const onRoleChanged = val => setRole(val);

    return (
        <Modal 
            title={`${props.actionType} ${props.activeTabName}`}
            visible={props.visible}
            onOk={() => {
                if (username && email && rfidToken) {
                    props.handleOk();
                    dispatch(
                        updateUser({
                            user: {
                                username,
                                email,
                                rfidToken,
                                role: role.charAt(0).toUpperCase() + role.slice(1),
                            },
                            role,
                        })
                    );
                }
            }} 
            onCancel={props.handleCancel} 
        >
            <Row gutter={8}>
                <Col span={5}>
                    <p>Username:</p>
                </Col>
                <Col span={15}>
                    <Input
                    disabled
                    value={username}
                    defaultValue={props.defaultData.name}
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
                    defaultValue={props.defaultData.email}
                    placeholder="Email Address" 
                    value={email}
                    onChange={onEmailChanged}
                    />
                </Col>
            </Row>
            <Row gutter={8}>
                <Col span={5}>
                    <p>RFID:</p>
                </Col>
                <Col span={15}>
                    <Input 
                    defaultValue={props.defaultData.RFID}
                    placeholder="RFID" 
                    value={rfidToken}
                    onChange={onRFIDChanged}
                    />
                </Col>
            </Row>
            <Row gutter={8}>
                <Col span={5}>
                    <p>Role:</p>
                </Col>
                <Col span={15}>
                <Select defaultValue={role} value={role} onChange={onRoleChanged}>
                    <Option value="customer">Customer</Option>
                    <Option value="deliverer">Deliverer</Option>
                    <Option value="dispatcher">Dispatcher</Option>
                </Select>
                </Col>
            </Row>
        </Modal>
    )    
}