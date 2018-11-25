import React, { Component } from 'react';
import { Segment, Button, Transition, SegmentGroup, Input, Table, Modal, Form } from 'semantic-ui-react'

import TableRow from './row-table';
// import logo1 from './images/mobile.png';

import { Cookies } from "react-cookie";
import Canvas from './canvas';

const cookies = new Cookies();

export default class managewebsitescreen extends Component {


    state = {
        addModal: false, isLoading: false, listWeb: null, webName: "", webUrl: "",
        isDisable: true, addLoading: false, showSitemapModal: false,
        showRefModal: false, showRefResultModal: false, currWeb: "", selectedUrl: "",
    }


    componentDidMount() {
        this._refreshTable();
    }

    _loadingTable(isLoading) {
        this.setState({ isLoading: isLoading })

    }

    _showingModal(isShow) {
        this.setState({ showSitemapModal: isShow })
    }

    _getSelectedWebName(name) {
        this.setState({ currWeb: name })
    }

    _setSelectedRectValue(url) {
        alert(url);
        if (url !== "") {
            this.setState({ selectedUrl: url, showRefModal: true });
        }

    }

    _getRefTo(url) {
        alert("URL: " + url)
        fetch("/api/sitemap/getRefTo", {
            method: 'POST',
            headers: {
                'Accept': 'application/json',
                'Content-Type': 'application/json'
            },
            body: JSON.stringify({ "managerId": cookies.get("u_id"), "managerToken": cookies.get("u_token") })
        }).then(response => response.json()).then((data) => { });

    }

    _getRefBy(url) {
        alert("URL: " + url)
    }
    _refreshTable() {
        fetch("/api/website/manage", {
            method: 'POST',
            headers: {
                'Accept': 'application/json',
                'Content-Type': 'application/json'
            },
            body: JSON.stringify({ "managerId": cookies.get("u_id"), "managerToken": cookies.get("u_token") })
        }).then(response => response.json()).then((data) => {
            if (data.action === "SUCCESS") {
                var list = data.website.map((item, index) => {
                    return (<TableRow key={index} no={index} id={item.id} name={item.name} url={item.url} version={item.version}
                        time={item.time} loadingTable={(isLoading) => this._loadingTable(isLoading)}
                        refreshTable={() => this._refreshTable()}

                        showingModal={(isShow) => this._showingModal(isShow)}
                        getSelectedWebName={(name) => this._getSelectedWebName(name)}
                        setSelectedRectValue={(url) => this._setSelectedRectValue(url)} />);
                });
                this.setState({ listWeb: list, isLoading: false });

            }
        });

    }

    _onchangeName(event) {
        this.setState({ webName: event.target.value }, () => this._checkAddBtn());
    }

    _onchangeUrl(event) {
        this.setState({ webUrl: event.target.value }, () => this._checkAddBtn());
    }

    _checkAddBtn() {
        var result = false;
        if (this.state.webName === "" || this.state.webUrl === "") {
            result = true;
        }
        this.setState({ isDisable: result });
    }

    _addWebsite() {
        this.setState({ addLoading: true, isDisable: false });
        var param = {
            "managerId": cookies.get("u_id"), "managerToken": cookies.get("u_token"), website: {
                "name": this.state.webName, "url": this.state.webUrl
            }
        };
        fetch("/api/manager/addWebsite", {
            method: 'POST',
            headers: {
                'Accept': 'application/json',
                'Content-Type': 'application/json'
            },
            body: JSON.stringify(param)
        }).then(response => response.json()).then((data) => {
            if (data.action === "SUCCESS") {
                this.setState({ addLoading: false });
                this.setState({ addModal: false });
                this.setState({ showSitemapModal: false });
                this._refreshTable();
            } if (data.action === "DUPLICATE ERROR") {
                alert("This website is existed");
            }
        });
    }

    render() {
        return (
            <div>

                <SegmentGroup vertical='true'>

                    <Segment basic>
                        <div style={{ marginBottom: '30px' }}>

                            <Button style={{ float: 'right' }} onClick={() => this.setState({ addModal: true })}> Add </Button>
                            <Transition duration={600} divided size='huge' verticalAlign='middle' visible={this.state.addModal}>
                                <Modal open={this.state.addModal} closeOnEscape="true">
                                    <Modal.Header>Add Website</Modal.Header>
                                    <Modal.Content>
                                        <Form>
                                            <Form.Field>
                                                <label>Website name</label>
                                                <Input type="text" placeholder="Website Name" onChange={(event) => this._onchangeName(event)} value={this.state.webName}></Input>
                                            </Form.Field>
                                            <Form.Field>
                                                <label>Website URL</label>
                                                <Input type="text" placeholder="Website URL" onChange={(event) => this._onchangeUrl(event)} value={this.state.webUrl}></Input>
                                            </Form.Field>
                                        </Form>
                                    </Modal.Content>
                                    <Modal.Actions>
                                        <Button onClick={() => this.setState({ addModal: false })}>Cancel</Button>
                                        <Button content='Done' color='blue' loading={this.state.addLoading} disabled={this.state.isDisable} onClick={() => this._addWebsite()} />
                                    </Modal.Actions>
                                </Modal>
                            </Transition>
                        </div>
                    </Segment>
                    <Segment.Group horizontal style={{ maxHeight: '63vh', overflow: "auto" }}>
                        {/*View Sitemap*/}
                        <Transition duration={600} divided size='huge' verticalAlign='middle' visible={this.state.showSitemapModal}>
                            <Modal open={this.state.showSitemapModal} style={{ width: '100%', height: '100%', background: 'white' }}>
                                <Modal.Header style={{ textAlign: 'center', fontSize: 25, fontWeight: 'bold', color: 'black' }}>Visual Sitemap of Website: {this.state.currWeb}</Modal.Header>
                                <Modal.Content style={{ maxHeight: 'calc(85vh)', overflow: 'auto' }}>

                                    <Segment basic style={{
                                        height: 'calc(80vh)'
                                    }} loading={this.state.isLoading}>
                                        <Canvas />
                                    </Segment>
                                </Modal.Content>
                                <Modal.Actions >
                                    <Button onClick={() => this.setState({ showSitemapModal: false })}> Cancel</Button>
                                </Modal.Actions>
                            </Modal>
                        </Transition>
                        {/*End View Sitemap*/}
                        {/*Handle reference*/}
                        <Transition duration={600} divided verticalAlign='middle' visible={this.state.showRefModal}>
                            <Modal open={this.state.showRefModal}>
                                <Modal.Header style={{ textAlign: 'center', fontSize: 25, fontWeight: 'bold', color: 'black' }}>Choose your option:</Modal.Header>
                                <Modal.Content>
                                    <Form>
                                        <Form.Field>
                                            <Button onClick={() => this._getRefTo(this.state.selectedUrl)}> Reference To </Button>
                                        </Form.Field>
                                        <Form.Field>
                                            <Button onClick={() => this._getRefBy(this.state.selectedUrl)}>Referenced By </Button>
                                        </Form.Field>
                                    </Form>
                                </Modal.Content>
                                <Modal.Actions >
                                    <Button onClick={() => this.setState({ showRefModal: false })}> Cancel</Button>
                                </Modal.Actions>
                            </Modal>
                        </Transition>
                        {/*End reference*/}
                        <Segment basic loading={this.state.isLoading}>
                            <Table singleLine unstackable>
                                <Table.Header>
                                    <Table.Row>
                                        <Table.HeaderCell>No</Table.HeaderCell>
                                        <Table.HeaderCell>Name</Table.HeaderCell>
                                        <Table.HeaderCell>URL</Table.HeaderCell>
                                        <Table.HeaderCell>Version</Table.HeaderCell>
                                        <Table.HeaderCell>Lastest updated</Table.HeaderCell>
                                        <Table.HeaderCell>New version</Table.HeaderCell>
                                        <Table.HeaderCell>Action</Table.HeaderCell>
                                        <Table.HeaderCell></Table.HeaderCell>
                                    </Table.Row>
                                </Table.Header>
                                <Table.Body>
                                    {this.state.listWeb}
                                </Table.Body>
                            </Table>
                            {/* <Canvas /> */}
                        </Segment>
                    </Segment.Group>
                    {/* <div>


                        <div class='ui medium images'>

                            <img src={require('./images/' + 'mobile.png')} class='ui image' />
                            <img src={logo1} class='ui image' />
                            <img src={logo1} class='ui image' />


                        </div>
                    </div> */}

                </SegmentGroup>
            </div>
        );
    }
}