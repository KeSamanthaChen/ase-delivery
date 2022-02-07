import { Layout, Menu, Input, Space, Button, Table, Spin } from 'antd';
import { LoadingOutlined, ReloadOutlined } from '@ant-design/icons';
import React from 'react';
import { connect } from 'react-redux';
import { setCurrentTab } from './dispatcherSlice';
import { getUsers, deleteUser } from './UsersSlice';
import { getDeliveries, deleteDelivery } from './deliverySlice';
import { getBoxes, deleteBox } from './boxSlice';
import AddNewUserPage from './AddNewUserPage';
import AddNewBoxPage from './AddNewBoxPage';
import EditUserPage from './EditUserPage';
import EditDeliveryPage from './EditDeliveryPage';
import EditBoxPage from './EditBoxPage';
import { getTag } from '../Common/utils';

const { Header, Content, Sider } = Layout;
const { Search } = Input;

const TAB_LIST = [
    {
        key: 'customer',
        tab: 'Customer',
    },
    {
        key: 'deliverer',
        tab: 'Deliverer',
    },
    {
        key: 'dispatcher',
        tab: 'Dispatcher',
    },
    {
        key: 'delivery',
        tab: 'Delivery',
    },
    {
        key: 'box',
        tab: 'Box',
    },
];

class DispatcherSPA extends React.Component {
    constructor(props) {
        super(props);
        this.state = {
            modalAction: null,
            modalData: null,
        };
    }

    onRefreshClicked() {
        this.refresh(this.props.currentTab);
    }


    componentDidMount() {
        this.refresh();
    }

    refresh(tab) {
        switch (tab) {
            case 'delivery':
                this.props.getDeliveries();
                break;
            case 'customer':
            case 'deliverer':
            case 'dispatcher':
                this.props.getUsers();
                break;
            case 'box':
                this.props.getBoxes();
                break;
            default:
                this.props.getUsers();
                this.props.getDeliveries();
                this.props.getBoxes();
                break;
        }
    }

    /**
     * @param modalAction 'create'|'edit'|null, where null means no modal will be shown
     */
    showModal = (modalAction, modalData) => {
        this.setState({ modalAction, modalData });
    };

    handleOk = () => {
        this.setState({ modalAction: null });
        this.refresh(this.props.currentTab);
    };

    handleCancel = (actionType, modalType) => {
        this.setState({ modalAction: null });
    };

    onSearch = (value)=>  {
        this.setState({
            searchValue: value,
        });
    }

    // make a loop for menu items
    render() {
        if (this.props.getUsersLoading) {
            const antIcon = <LoadingOutlined style={{ fontSize: 30 }} spin />;
            return <div style={{width: '100%', height: '100%', textAlign: 'center'}}>
                <Spin indicator={antIcon} size="large" style={{marginTop: 64}}/>
            </div>;
        }

        const menuList = TAB_LIST.map(({key, tab}) =>
            <Menu.Item key={key}
                onClick={() => { this.props.setCurrentTab(key); }}
            >
                {tab}
            </Menu.Item>
        );

        const userColumns = [
            {
                title: 'Name',
                dataIndex: 'username',
                key: 'username',
            },
            {
                title: 'Email',
                dataIndex: 'email',
                key: 'email',
            },
            {
                title: 'RFID',
                dataIndex: 'rfidToken',
                key: 'rfidToken',
            },
            {
                title: 'Action',
                key: 'action',
                render: (text, record) => (
                    <Space size="middle">
                        <Button onClick={() => {
                            this.showModal('edit', record);
                        }}>Edit</Button>
                        <Button danger onClick={() => {
                            this.props.deleteUser(record, this.props.currentTab);
                        }}>Delete</Button>
                    </Space>
                ),
            },
        ];

        const deliveryColumns = [
            {
                title: 'Tracking Code',
                dataIndex: 'trackingCode',
                key: 'trackingCode',
            },
            {
                title: 'Customer',
                dataIndex: 'customer',
                key: 'customer',
            },
            {
                title: 'Deliverer',
                dataIndex: 'deliverer',
                key: 'deliverer',
            },
            {
                title: 'Target Box',
                dataIndex: 'targetBox',
                key: 'targetBox',
                render: (box) => <span>{box && box.name}</span>
            },
            {
                title: 'Status',
                dataIndex: 'status',
                key: 'status',
                render: (text) => getTag(text),
            },
            {
                title: 'Action',
                key: 'action',
                render: (text, record) => (
                    <Space size="middle">
                        <Button onClick={() => {
                            this.showModal('edit', record);
                        }}>Edit</Button>
                        {/* only allow deletion for newly ordered deliveries */}
                        <Button danger disabled={record.status !== 0} onClick={() => {
                            this.props.deleteDelivery(record.trackingCode, this.refreshDeliveries.bind(this));
                        }}>Delete</Button>
                    </Space>
                ),
            },
        ];

        const boxColumns = [
            {
                title: 'ID',
                dataIndex: 'id',
                key: 'id',
            },
            {
                title: 'Name',
                dataIndex: 'name',
                key: 'name',
            },
            {
                title: 'Address',
                dataIndex: 'address',
                key: 'address',
            },
            {
                title: 'State',
                key: 'state',
                render: (text, record) => (
                    <Space size="middle">
                        {record.state}
                    </Space>
                ),
            },
            {
                title: 'Customer Name',
                dataIndex: 'customerName',
                key: 'customerName',
            },
            {
                title: 'Deliverer Name',
                dataIndex: 'delivererName',
                key: 'delivererName',
            },
            {
                title: 'Action',
                key: 'action',
                render: (text, record) => (
                    <Space size="middle">
                        <Button onClick={() => {
                            this.showModal('edit', record);
                        }}>Edit</Button>
                        <Button danger onClick={() => {
                            this.props.deleteBox(record.id);
                        }}>Delete</Button>
                    </Space>
                ),
            },
        ];

        let modalComponent;
        let isEditMode;
        if (this.state.modalAction !== null) {
            switch (this.props.currentTab) {
                case 'customer':
                case 'deliverer':
                case 'dispatcher':
                    modalComponent = this.state.modalAction === 'create' ? AddNewUserPage : EditUserPage;
                    break;
                case 'delivery':
                    modalComponent = EditDeliveryPage;
                    isEditMode = this.state.modalAction === 'create' ? false : true;
                    break;
                case 'box':
                    modalComponent = this.state.modalAction === 'create' ? AddNewBoxPage : EditBoxPage;
                    break;
                default:
                    console.log('error');
                    break;
            }
        }
        const modal = modalComponent && React.createElement(modalComponent, {
            actionType: this.state.modalAction,
            activeTabName: this.props.currentTab,
            visible: true,
            handleOk: this.handleOk,
            handleCancel: this.handleCancel,
            isEditMode: isEditMode,
            defaultData: this.state.modalData,
        });

        let searchedCustomers;
        let searchedDeliverers;
        let searchedDispatchers;
        let searchedDeliveries;
        let searchedBoxes;

        if (this.state.searchValue) {
            const simpleSearch = obj => Object.values(obj).some(val => String(val).includes(this.state.searchValue));
            switch (this.props.currentTab) {
                case 'customer':
                    searchedCustomers = this.props.customers.filter(simpleSearch);
                    break;
                case 'deliverer':
                    searchedDeliverers = this.props.deliverers.filter(simpleSearch);
                    break;
                case 'dispatcher':
                    searchedDispatchers = this.props.dispatchers.filter(simpleSearch);
                    break;
                case 'delivery':
                    searchedDeliveries = this.props.deliveries.filter(simpleSearch);
                    break;
                case 'box':
                    searchedBoxes = this.props.boxes.filter(simpleSearch);
                    break;
                default:
                    console.log('error');
                    break;
            }
        }

        return <div>
            <Layout>
                <Sider
                    style={{
                        overflow: 'auto',
                        height: '100vh',
                        position: 'fixed',
                        zIndex: 2,
                        left: 0,
                        textAlign: 'center',
                    }}
                >
                    <div className="logo" />
                    <Menu mode="inline" defaultSelectedKeys={[this.props.currentTab]} style={{marginTop: 64}}>
                        {menuList}
                    </Menu>
                </Sider>
                <Layout className="site-layout" style={{ marginLeft: 200 }}>
                    <Header style={{ zIndex: 1, width: '100%', lineHeight: 'normal', display: 'flex', alignItems: 'center', justifyContent: 'space-evenly' }}>
                        <div className="logo" />
                        <Space direction="vertical">
                            <Search placeholder={"input search " + this.props.currentTab} onSearch={this.onSearch} style={{ width: 400 }} />
                        </Space>
                        <Button key="1" style={{ verticalAlign: 'top' }} onClick={() => this.showModal('create', this.props.currentTab)}>{"Add " + this.props.currentTab}</Button>
                        <Button onClick={this.onRefreshClicked.bind(this)}><ReloadOutlined /></Button>
                    </Header>
                    <Content style={{ margin: '24px 16px 0', overflow: 'initial' }}>
                        {this.props.currentTab === 'customer' && <Table rowKey='username' columns={userColumns} dataSource={searchedCustomers || this.props.customers} />}
                        {this.props.currentTab === 'deliverer' && <Table rowKey='username' columns={userColumns} dataSource={searchedDeliverers || this.props.deliverers} />}
                        {this.props.currentTab === 'dispatcher' && <Table rowKey='username' columns={userColumns} dataSource={searchedDispatchers || this.props.dispatchers} />}
                        {this.props.currentTab === 'delivery' && 
                            <Table rowKey='trackingCode' 
                                columns={deliveryColumns} 
                                dataSource={searchedDeliveries || this.props.deliveries} 
                                pagination={{ position: ['bottomCenter'], pageSize: 7 }}
                        />}
                        {this.props.currentTab === 'box' && <Table rowKey='id' columns={boxColumns} dataSource={searchedBoxes || this.props.boxes} />}
                    </Content>
                </Layout>
            </Layout>
            {modal}
        </div>;
    }
}

//convert Redux's reducer function to react components' props (for class components to use)
const mapDispatchToProps = (dispatch) => {
    return {
        setCurrentTab: (args) => dispatch(setCurrentTab(args)),
        getUsers: () => dispatch(getUsers()),
        getDeliveries: () => dispatch(getDeliveries()),
        getBoxes: () => dispatch(getBoxes()),
        deleteUser: (user, role) => dispatch(deleteUser({user, role})),
        deleteDelivery: trackingCode => dispatch(deleteDelivery(trackingCode)),
        deleteBox: id => dispatch(deleteBox(id)),
    }
};

//convert Redux's state to react components' props (for class components to use)
const mapStateToProps = state => ({
    currentTab: state.dispatcher.currentTab,
    customers: state.users.customers,
    deliverers: state.users.deliverers,
    dispatchers: state.users.dispatchers,
    deliveries: state.delivery.deliveries,
    boxes: state.box.boxes,
    getUsersLoading: state.users.getUsersLoading,
});


export default connect(mapStateToProps, mapDispatchToProps)(DispatcherSPA);