import React from 'react';
import { connect } from 'react-redux';
import { Modal, Row, Col, Select, message, Spin, Space, Steps } from 'antd';
import { LoadingOutlined, FormOutlined, ExportOutlined, DownSquareOutlined, CheckSquareOutlined } from '@ant-design/icons';
// import { UserOutlined } from '@ant-design/icons';
import { updateDelivery, addDelivery } from './deliverySlice';
import { api_url, codes_status } from '../Common/utils';
import axios from 'axios';

class EditDeliveryPage extends React.Component {

    constructor(props) {
        super(props);
        this.state = {
            trackingCode: this.props.defaultData.trackingCode,
            customerUsername: this.props.defaultData.customer,
            delivererUsername: this.props.defaultData.deliverer,
            boxName: this.props.defaultData.targetBox?.name,
            statuses: this.props.defaultData.statuses,
            status: this.props.defaultData.status,
            deliverers: null,
            customers: null,
            boxes: null,
            visible: this.props.visible,
            curCustomer: null,
            curDeliverer: null,
            curDeliverers: null,
            curBox: this.props.defaultData.targetBox?.id,
            curStatus: this.props.defaultData.status,
            isEditMode: this.props.isEditMode,
            curBoxes: null,    // available box list of the curCustomer
        };
    }

    componentDidMount() {
        this.getUserList();
    }

    getUserList() {
        let customer = this.state.customerUsername;
        axios({
            method: 'GET',
            url: `${api_url}/delivery/users?role=Deliverer`,
            withCredentials: true
        }).then(response => {
            this.setState({ deliverers: response.data });
            return axios({
                method: 'GET',
                url: `${api_url}/delivery/users?role=Customer`,
                withCredentials: true
            });
        }).then(response => {
            this.setState({ customers: response.data });
            return axios({
                method: 'GET',
                url: `${api_url}/delivery/users/${this.state.customerUsername}`,
                withCredentials: true
            }).then(response => {
                this.setState({ curCustomer: response.data['id'] });
                return axios({
                    method: 'GET',
                    url: `${api_url}/delivery/users/${this.state.delivererUsername}`,
                    withCredentials: true
                });
            }).then(response => {
                this.setState({ curDeliverer: response.data['id'] });
                return axios({
                    method: 'GET',
                    url: `${api_url}/delivery/boxes`,
                    withCredentials: true
                });
            }).then(response => {
                let curBoxes = response.data.filter(function (box) {
                    if (box.state === "AVAILABLE") return true;
                    if ((box.state === "FILLED" || box.state === "ASSIGNED") && box.customerName === customer)
                        return true;
                    return false;
                });
                this.setState({ boxes: response.data, curBoxes: curBoxes });
            }).catch(error => {
                console.log(error);
                message.error("Something went wrong...");
            })
        })
    }

    onCustomerChange(newCustomer) {
        let customerName = this.state.customers.filter(function (customer) {
            return customer.id === newCustomer;
        })[0]['username'];
        let curBoxes = this.state.boxes.filter(function (box) {
            if (box.state === "AVAILABLE") return true;
            if ((box.state === "ASSIGNED") && box.customerName === customerName)
                return true;
            return false;
        });
        this.setState({ curCustomer: newCustomer, curBoxes: curBoxes });
    }

    onDelivererChange(newDeliverer) {
        this.setState({ curDeliverer: newDeliverer });
    }

    onBoxChange(newBox) {
        let box = this.state.boxes.filter(function(box) {
            return box.id === newBox;
        })[0];
        let newDeliverers = this.state.deliverers;
        if (box.state === "ASSIGNED") {
            let delivererName = box?.delivererName;
            if (delivererName) {
                newDeliverers = this.state.deliverers.filter(function(deliverer) {
                    return deliverer.username === delivererName;
                });
            }
        }
        this.setState({ curBox: newBox, curDeliverers: newDeliverers, curDeliverer: newDeliverers[0].id });
    }

    changeStatus(newValue) {
        this.setState({ curStatus: newValue });
    }

    async onCreateConfirm() {
        let newDelivery = {
            customer: {
                id: this.state.curCustomer,
            },
            deliverer: {
                id: this.state.curDeliverer,
            },
            targetBox: {
                id: this.state.curBox,
            }
        };
        await this.props.addDelivery(newDelivery);
        this.props.handleCancel();
    }

    async onUpdateConfirm() {
        let newStatuses = Object.assign([], this.state.statuses);
        if (this.state.curStatus < this.state.status) {
            newStatuses = newStatuses.slice(0, this.state.curStatus + 1);
        }
        if (this.state.curStatus > this.state.status) {
            for (let i = this.state.status + 1; i <= this.state.curStatus; i++) {
                let status = {
                    status: codes_status[i],
                    date: new Date().toISOString()
                };
                newStatuses.push(status);
            }
        }
        let newDelivery = {
            trackingCode: this.state.trackingCode,
            customer: {
                id: this.state.curCustomer,
            },
            deliverer: {
                id: this.state.curDeliverer,
            },
            targetBox: {
                id: this.state.curBox,
            },
            statuses: newStatuses,
        };
        await this.props.updateDelivery(newDelivery);
        this.props.handleCancel();
    }

    render() {
        const { Option } = Select;

        const delivererOptions = [];
        const customerOptions = [];
        const boxOptions = [];

        const antIcon = <LoadingOutlined style={{ fontSize: 30 }} spin />;

        if (this.state.curDeliverers) {
            for (let i in this.state.curDeliverers) {
                let curDeliverer = this.state.curDeliverers[i];
                delivererOptions.push(<Option key={curDeliverer['id']}>{curDeliverer['username']}</Option>);
            }
        }

        for (let i in this.state.customers) {
            let curCustomer = this.state.customers[i];
            customerOptions.push(<Option key={curCustomer['id']}>{curCustomer['username']}</Option>);
        }

        for (let i in this.state.curBoxes) {
            let curBox = this.state.curBoxes[i];
            boxOptions.push(<Option key={curBox['id']}>{curBox['name']}</Option>);
        }

        const ableToRender = this.state.curCustomer !== null && this.state.curDeliverer !== null
            && this.state.customers !== null && this.state.deliverers !== null
            && this.state.boxes !== null;


        const { Step } = Steps;

        return <Modal visible={this.props.visible}
            onOk={this.state.isEditMode ? this.onUpdateConfirm.bind(this) : this.onCreateConfirm.bind(this)}
            onCancel={this.props.handleCancel}
            optionFilterProp="children"
            title="Edit Delivery"
            filterOption={(input, option) => option.chilren.toLowerCase().indexOf(input.toLowerCase()) >= 0}
        >
            {ableToRender &&
                <div style={{ width: "100%", padding: "8 8 16 8px" }}>
                    <Space direction="vertical" size={16} style={{ width: "100%" }}>
                        {this.state.isEditMode && <Row>
                            <Col span="24">
                                <Steps size="small" current={this.state.status}>
                                    <Step title="Ordered" icon={<FormOutlined />} />
                                    <Step title="Delivering" icon={<ExportOutlined />} />
                                    <Step title="Delivered" icon={<DownSquareOutlined />} />
                                    <Step title="Completed" icon={<CheckSquareOutlined />} />
                                </Steps>
                            </Col>
                        </Row>}
                        <Row gutter={[16, 32]}>
                            <Col span={5} >
                                Customer
                            </Col>
                            <Col span={12}>
                                <Select style={{ width: "100%" }}
                                    showSearch
                                    value={this.state.curCustomer}
                                    optionFilterProp="children"
                                    onChange={(value) => { this.onCustomerChange(value) }}
                                    filterOption={(input, option) => option.chilren.toLowerCase().indexOf(input.toLowerCase()) >= 0}>
                                    {customerOptions}
                                </Select>
                            </Col>
                        </Row>
                        <Row gutter={[16, 32]}>
                            <Col span="5">
                                Target Box
                            </Col>
                            <Col span="12">
                                <Select style={{ width: "100%" }}
                                    showSearch
                                    value={this.state.curBox}
                                    optionFilterProp="children"
                                    onChange={(value) => { this.onBoxChange(value) }}
                                    filterOption={(input, option) => option.chilren.toLowerCase().indexOf(input.toLowerCase()) >= 0}>
                                    {boxOptions}
                                </Select>
                            </Col>
                        </Row>
                        <Row gutter={[16, 32]}>
                            <Col span="5">
                                Deliverer
                            </Col>
                            <Col span="12">
                                <Select style={{ width: "100%" }}
                                    showSearch
                                    disabled={this.state.isEditMode}
                                    value={this.state.isEditMode ? this.state.delivererUsername :this.state.curDeliverer}
                                    optionFilterProp="children"
                                    onChange={(value) => { this.onDelivererChange(value) }}
                                    filterOption={(input, option) => option.chilren.toLowerCase().indexOf(input.toLowerCase()) >= 0}>
                                    {delivererOptions}
                                </Select>
                            </Col>
                        </Row>
                        {/* {this.state.isEditMode &&
                            <Row gutter={[16, 32]}>
                                <Col span="5">
                                    Status
                                </Col>
                                <Col span={12}>
                                    <Radio.Group value={this.state.curStatus}>
                                        <Radio value={0} onChange={() => { this.changeStatus(0) }}>Ordered</Radio>
                                        <Radio value={1} onChange={() => { this.changeStatus(1) }}>Delivering</Radio>
                                        <Radio value={2} onChange={() => { this.changeStatus(2) }}>Delivered</Radio>
                                        <Radio value={3} onChange={() => { this.changeStatus(3) }}>Completed</Radio>
                                    </Radio.Group>
                                </Col>
                            </Row>
                        } */}
                    </Space>
                </div>
            }
            {!ableToRender &&
                <div style={{ width: '100%', height: '100%', textAlign: 'center' }}>
                    <Spin indicator={antIcon} size="large" style={{ marginTop: 24, marginBottom: 24 }} />
                </div>
            }
        </Modal>
    }
}

const mapStateToProps = state => ({
    deliveries: state.delivery.deliveries,
});

const mapDispatchToProps = (dispatch) => {
    return {
        updateDelivery: (delivery) => dispatch(updateDelivery(delivery)),
        addDelivery: (delivery) => dispatch(addDelivery(delivery)),
    }
};

export default connect(mapStateToProps, mapDispatchToProps)(EditDeliveryPage);