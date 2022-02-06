import React, { useState } from 'react';
import { useDispatch } from 'react-redux';
import { Input, Modal, Row, Col } from 'antd';
import { updateBox } from './boxSlice';

export default function EditBoxPage(props) {
    const [id, setID] = useState(props.defaultData.id)
    const [name, setName] = useState(props.defaultData.name)
    const [state, setState] = useState(props.defaultData.state)
    const [customerName, setCustomerName] = useState(props.defaultData.customerName)
    const [delivererName, setDelivererName] = useState(props.defaultData.delivererName)

    const onIDChanged = e => setID(e.target.value)
    const onNameChanged = e => setName(e.target.value)
    const onStateChanged = e => setState(e.target.value)
    const onCustomerNameChanged = e => setCustomerName(e.target.value)
    const onDelivererNameChanged = e => setDelivererName(e.target.value)

    const dispatch = useDispatch()

    return (
        <Modal title="Edit box" visible={props.visible}
        onOk={() => {
            if (id) {
                dispatch(
                    updateBox({
                        id,
                        name,
                        state,
                        address: props.defaultData.address,
                        customerName,
                        delivererName,
                    })
                );
            }
            props.handleOk(props.handleOk);
        }}
        onCancel={props.handleCancel}
        >
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
                    <p>State:</p>
                </Col>
                <Col span={15}>
                    <Input
                    value={state}
                    onChange={onStateChanged}
                    placeholder="Enter state" />
                </Col>
            </Row>
            <Row gutter={8}>
                <Col span={5}>
                    <p>Customer name:</p>
                </Col>
                <Col span={15}>
                    <Input
                    value={customerName}
                    onChange={onCustomerNameChanged}
                    placeholder="Enter customer name" />
                </Col>
            </Row>
            <Row gutter={8}>
                <Col span={5}>
                    <p>Deliverer name:</p>
                </Col>
                <Col span={15}>
                    <Input
                    value={delivererName}
                    onChange={onDelivererNameChanged}
                    placeholder="Enter deliverer name" />
                </Col>
            </Row>
        </Modal>
    )
}