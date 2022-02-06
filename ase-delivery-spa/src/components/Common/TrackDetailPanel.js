import React from 'react';
import { Steps, Empty, Image, Row, Col, Divider } from 'antd'
import packetIcon from '../../resources/packet.png';
import axios from 'axios';
import './common.less';
import { api_url, getXSRFToken, status_codes } from './utils';

class TrackDetailPanel extends React.Component {
    constructor(props) {
        super(props);
        this.state = {
            status: null,
            tracks: null,
            assignedBox: null,
            trackingCode: null,
        };
    }

    getData() {
        axios({
            method: 'GET',
            withCredentials: true,
            url: `${api_url}/delivery/deliveries/${this.props.trackingCode}`,
            headers: {
                'X-XSRF-TOKEN': getXSRFToken(),
            }
        }).then(response => {
            if (response.data) {
                let trackData = {
                    status: 0,
                    tracks: [],
                };
                let tracks = response.data['statuses'].sort((a, b) => status_codes[a.status] - status_codes[b.status]);
                trackData.status = status_codes[tracks[tracks.length-1].status];
                for (let i in tracks) {
                    trackData.tracks.push({
                        code: status_codes[tracks[i]['status']],
                        dateTime: tracks[i]['date']
                    })
                }
                this.setState({
                    status: trackData.status,
                    tracks: trackData.tracks,
                    assignedBox: {
                        boxId: response.data['targetBox']['id'],
                        name: response.data['targetBox']['name'],
                    },
                    trackingCode: this.props.trackingCode
                })
            }
            else {
                this.setState({
                    status: null
                })
            }
        })
    }

    componentDidMount() {
        if (this.props.trackingCode !== this.state.trackingCode) 
            this.getData();
    }

    componentDidUpdate() {
        if (this.props.trackingCode !== this.state.trackingCode) 
            this.getData();
    }

    getDesc(i) {
        let desc;
        if (i <= this.state.status) {
            desc = this.state.tracks[i].dateTime;
        } else {
            desc = '-';
        }
        return desc;
    }

    render() {
        const { Step } = Steps;
        return <div>
            {this.state.status === null && <Empty description="Track details not available yet" />}
            {this.state.status !== null &&
                <div>
                    <Row>
                        <Col><Image src={packetIcon} preview={false} width="70px" /></Col>
                        <Col style={{ marginLeft: "10px" }}>
                            <h3 style={{ margin: "10px 0px 0px 0px" }}>Pickup station of #{this.props.trackingCode.toUpperCase()}</h3>
                            <h2 style={{ margin: "0px", lineHeight: "90%" }}>{this.state.assignedBox.name}</h2>
                        </Col>
                    </Row>
                    <Divider style={{ margin: "0px 0px 15px 0px" }} />
                    <Steps direction="vertical" current={this.state.status} style={{ width: "89%", margin: "auto" }}>
                        <Step title="Arrived at the central deposit" description={this.getDesc(0)}></Step>
                        <Step title="Picked up by the deliverer" description={this.getDesc(1)}></Step>
                        <Step title="Arrived at the pick-up box" description={this.getDesc(2)}></Step>
                        <Step title="Picked up by customer" description={this.getDesc(3)}></Step>
                    </Steps>
                </div>
            }
        </div>;
    }
}

export default TrackDetailPanel;