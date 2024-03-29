import React, { useState } from 'react';
import { useDispatch } from 'react-redux';
import { Input, Modal, Row, Col } from 'antd';
import { addBox } from './boxSlice';

export default function AddNewBoxPage(props) {
    const [name, setName] = useState('')
    const [address, setAddress] = useState('')
    const [id, setID] = useState('')

    const onNameChanged = e => setName(e.target.value)
    const onAddressChanged = e => setAddress(e.target.value)
    const onIDChanged = e => setID(e.target.value)

    const dispatch = useDispatch()

    return (
        <Modal title={"Create "+props.activeTabName} visible={props.visible}
        onOk={() => {
            if (address && id && name) {
                dispatch(
                    addBox({
                        id,
                        name,
                        address,
                    })
                );
            }
            props.handleOk(props.handleOk);
        }}
        onCancel={props.handleCancel}
        >
            <Row gutter={8}>
                <Col span={5}>
                    <p>Name:</p>
                </Col>
                <Col span={15}>
                    <Input
                    value={name}
                    onChange={onNameChanged}
                    placeholder="Enter box name" />
                </Col>
            </Row>
            <Row gutter={8}>
                <Col span={5}>
                    <p>Address:</p>
                </Col>
                <Col span={15}>
                    <Input
                    value={address}
                    onChange={onAddressChanged}
                    placeholder="Enter Street Address" />
                </Col>
            </Row>
            <Row gutter={8}>
                <Col span={5}>
                    <p>ID:</p>
                </Col>
                <Col span={15}>
                    <Input
                    value={id}
                    onChange={onIDChanged}
                    placeholder="Enter Raspberry Pi ID" />
                </Col>
            </Row>
        </Modal>
    )
}