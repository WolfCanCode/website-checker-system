import React, { Component } from 'react';
import 'semantic-ui-css/semantic.min.css';
import { Segment, Statistic, Icon, Button, Card } from 'semantic-ui-react'
import { Cookies } from "react-cookie";

const cookies = new Cookies();



class ConfigScreen extends Component {

    state = { isLoading: false, isDisable: true, time: null, version: 0 };


    componentDidMount() {
        this._refreshVer();
    }

    _refreshVer() {
        this.setState({ isLoading: false });
        var param = { "userId": cookies.get("u_id"), "userToken": cookies.get("u_token"), "websiteId": cookies.get("u_w_id") };
        fetch("http://localhost:8080/api/sitemap/getVer", {
            method: 'POST',
            headers: {
                'Accept': 'application/json',
                'Content-Type': 'application/json'
            },
            body: JSON.stringify(param)
        }).then(response => response.json()).then((data) => {
            if (data.action === "SUCCESS") {
                this.setState({ version: data.version, time: data.time, isLoading: false });
            } else if (data.action === "INCORRECT") {
                console.info("Something went wrong!!")
            } else if (data.action === "PERMISSION ERROR") {
                alert("You don't have permission to do this");
            }
        });
    }

    _getNewVer() {
        this.setState({ isLoading: true });
        var param = { "userId": cookies.get("u_id"), "userToken": cookies.get("u_token"), "websiteId": cookies.get("u_w_id") };
        fetch("http://localhost:8080/api/sitemap/makeVer", {
            method: 'POST',
            headers: {
                'Accept': 'application/json',
                'Content-Type': 'application/json'
            },
            body: JSON.stringify(param)
        }).then(response => response.json()).then((data) => {
            if (data.action === "SUCCESS") {
                this.setState({ version: data.version, time: data.time, isLoading: false });
            } else {
                alert("Thất bại");
            }
        });
    }


    render() {
        return (
            <Segment.Group>
                <Segment>
                    <Card.Group>
                        <Card style={{ textAlign: 'center' }} >
                            <Card.Content>
                                <Statistic size="huge">
                                    <Statistic.Label>SITEMAP VERSION
                            <font style={{ color: 'gray', fontStyle: 'italic', fontSize: '12px' }}>
                                            {this.state.time !== 0 && this.state.time !== null ? <p>(Last updated {this.state.time})</p> : ''}
                                        </font>
                                    </Statistic.Label>
                                    <Statistic.Value>{this.state.version}</Statistic.Value>
                                    <Icon circular inverted name='redo' size="small" style={{ margin: "auto" }} loading={this.state.isLoading} onClick={() => this._refreshVer()} />
                                </Statistic>

                                <Button animated color='green' loading={this.state.isLoading} disabled={this.state.isLoading} onClick={() => this._getNewVer()}>
                                    <Button.Content visible>Get new version</Button.Content>
                                    <Button.Content hidden>
                                        <Icon name='download' />
                                    </Button.Content>
                                </Button>
                            </Card.Content>
                        </Card>
                    </Card.Group>
                </Segment>
            </Segment.Group>

        );
    }



}

export default ConfigScreen;