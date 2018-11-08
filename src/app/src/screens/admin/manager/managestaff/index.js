import React, { Component } from 'react';
import { Segment, Input, Table, Modal, Button, Form, Transition } from 'semantic-ui-react'

import { Cookies } from "react-cookie";
import TableRow from "./row-table";

const cookies = new Cookies();
export default class managestaffscreen extends Component {
    state = { list: [], listComp: [], loadingTable: false, addModal: false, staffName: "", staffEmail: "", staffPassword: "", isDisable: true };


    componentDidMount() {
        this._doMappingRefresh();
    }

    _doMappingRefresh() {
        this.setState({ loadingTable: true });
        var comp = [];
        var param = { "managerId": cookies.get("u_id"), "managerToken": cookies.get("u_token") };
        fetch("/api/user/getStaff", {
            method: 'POST',
            headers: {
                'Accept': 'application/json',
                'Content-Type': 'application/json'
            },
            body: JSON.stringify(param)
        }).then(response => response.json()).then((data) => {
            comp = data.listStaff.map((item, index) => {
                return (<TableRow key={index} No={index} idStaff={item.id} nameStaff={item.name} emailStaff={item.email} webStaff={item.website} item={item} onRefresh={() => this._doMappingRefresh()} />);
            });
            this.setState({ list: data.listStaff, listComp: comp, loadingTable: false });
        });
    }


    _openAddModal() {
        this.setState()
    }

    _onchangeName(event) {
        this.setState({ staffName: event.target.value }, () => this._checkAddBtn());
    }

    _onchangeEmail(event) {
        this.setState({ staffEmail: event.target.value }, () => this._checkAddBtn());
    }

    _onchangePassword(event) {
        this.setState({ staffPassword: event.target.value }, () => this._checkAddBtn());
    }

    _checkAddBtn() {
        var result = false;
        if (this.state.staffName === "" || this.state.staffEmail === "" || this.state.staffPassword === "") {
            result = true;
        }
        if (this.state.staffPassword.length < 8) {
            result = true;
        }
        this.setState({ isDisable: result });
    }

    _addUser() {
        var param = {
            "managerId": cookies.get("u_id"), "managerToken": cookies.get("u_token"), staff: {
                "name": this.state.staffName, "email": this.state.staffEmail, "password": this.state.staffPassword
            }
        };
        fetch("/api/user/addStaff", {
            method: 'POST',
            headers: {
                'Accept': 'application/json',
                'Content-Type': 'application/json'
            },
            body: JSON.stringify(param)
        }).then(response => response.json()).then((data) => {
            if (data.action === "SUCCESS") {
                this.setState({ addModal: false });
                this._doMappingRefresh();
            }
        });
    }

    render() {
        return (
            <Segment.Group>
                {/* Add modal */}
                <Transition duration={600} divided size='huge' verticalAlign='middle' visible={this.state.addModal}>
                    <Modal open={this.state.addModal} size="mini" >
                        <Modal.Header>Add Staff</Modal.Header>
                        <Modal.Content >
                            <Form>
                                <Form.Field>
                                    <label>Staff Name</label>
                                    <Input type="text" placeholder="Staff Name" onChange={(event) => this._onchangeName(event)} value={this.state.staffName}></Input>
                                </Form.Field>
                                <Form.Field>
                                    <label>Staff UserName</label>
                                    <Input type="text" placeholder="Staff UserName" onChange={(event) => this._onchangeEmail(event)} value={this.state.staffEmail}></Input>
                                </Form.Field>
                                <Form.Field>
                                    <label>Staff password</label>
                                    <Input type="password" placeholder="Staff password" onChange={(event) => this._onchangePassword(event)} value={this.state.staffPassword}></Input>
                                </Form.Field>

                            </Form>
                        </Modal.Content>
                        <Modal.Actions>
                            <Button onClick={() => this.setState({ addModal: false })}> Cancel</Button>
                            <Button content='Done' color='blue' disabled={this.state.isDisable} onClick={() => this._addUser()} />
                        </Modal.Actions>
                    </Modal>
                </Transition>
                {/* body */}
                <Segment basic >
                    <div style={{ marginBottom: '30px' }}>
                        <Input icon='search' placeholder='Search...' style={{ float: 'right' }} />
                        <Button style={{ float: 'right' }} onClick={() => this.setState({ addModal: true })}> Add </Button>
                    </div>
                </Segment>
                <Segment.Group horizontal style={{ maxHeight: '63vh', overflow: "auto" }}>
                    <Segment basic loading={this.state.loadingTable}>
                        <Table singleLine unstackable>
                            <Table.Header>
                                <Table.Row>
                                    <Table.HeaderCell>No</Table.HeaderCell>
                                    <Table.HeaderCell>Information</Table.HeaderCell>
                                    <Table.HeaderCell>Assign for Website</Table.HeaderCell>
                                    <Table.HeaderCell>Action</Table.HeaderCell>
                                </Table.Row>
                            </Table.Header>
                            <Table.Body>
                                {this.state.listComp}
                            </Table.Body>
                        </Table>
                    </Segment>

                </Segment.Group>
            </Segment.Group>
        );
    }
}