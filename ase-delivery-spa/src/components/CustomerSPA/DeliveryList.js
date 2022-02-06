import React from 'react';
import { List, Avatar, Empty, Button, Modal } from 'antd';
import TrackDetailPanel from '../Common/TrackDetailPanel';
import packetIcon from '../../resources/packet.png';


class DeliveryList extends React.Component {
    constructor(props) {
        super(props);
        this.state = ({
            category: props.category,
            isModalVisible: false,
            currentlyViewing: null,
        });
    }

    onTrackClicked(trackingCode) {
        this.setState({
            isModalVisible: true,
            currentlyViewing: trackingCode
        });
    }

    hideModal() {
        this.setState({
            isModalVisible: false
        });
    }

    render() {
        return <div style={{ textAlign: "start", maxWidth: "500px", margin: "auto" }}>
            {!this.props.deliveries && <Empty description="No deliveries yet" />}
            {this.props.deliveries && <List itemLayout="horizontal"
                dataSource={this.props.deliveries}
                renderItem={
                    item => (
                        <List.Item
                            actions={[<Button onClick={_ => { this.onTrackClicked(item.tracking_code) }}>track</Button>]}>
                            <List.Item.Meta
                                avatar={<Avatar src={packetIcon} />}
                                title={`No. ${item.tracking_code.toUpperCase()}`}
                                description={`created on: ${item.created_date}`}
                            />
                        </List.Item>
                    )
                } />}
            <Modal title="Track Details" visible={this.state.isModalVisible}
                footer={
                    <div>
                        <Button type="primary" onClick={() => { this.setState({ isModalVisible: false }) }}>Back</Button>
                    </div>}
                onCancel={() => { this.setState({ isModalVisible: false }) }}
                style={{ paddingTop: "10px" }}>
                <TrackDetailPanel trackingCode={this.state.currentlyViewing} />
            </Modal>
        </div>;
    }
}

export default DeliveryList;