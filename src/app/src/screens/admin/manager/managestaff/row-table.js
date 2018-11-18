import React, { Component } from 'react';
import { Table, Button, Modal, Input, Form } from 'semantic-ui-react'
import { Cookies } from "react-cookie";

const cookies = new Cookies();


class TableRow extends Component {
    state = {
        modalEdit: false,
        valWeb: null,
        staffName: this.props.nameStaff,
        staffEmail: this.props.emailStaff,
        staffPassword: "",
        oldName: this.props.nameStaff,
        oldEmail: this.props.emailStaff,
        isDisabled: true
    }

    _onchangeName(event) {
        this.setState({ staffName: event.target.value }, () => this._checkEditBtn());
    }

    _onchangeEmail(event) {
        this.setState({ staffEmail: event.target.value }, () => this._checkEditBtn());
    }

    _onchangePassword(event) {
        this.setState({ staffPassword: event.target.value }, () => this._checkEditBtn());
    }

    _checkEditBtn() {
        var result = false;
        var password = this.state.staffPassword;
        if (this.state.staffName === "" || this.state.staffEmail === "") {
            result = true;
        }
        if (this.state.staffName === this.state.oldName && this.state.staffEmail === this.state.oldEmail) {
            if (password.length < 8) {
                result = true;
            }
        }
        this.setState({ isDisabled: result });
    }

    _doEditStaff() {
        this.setState({ isDisabled: true });
        var param = {
            "managerId": cookies.get("u_id"), "managerToken": cookies.get("u_token"), staff: {
                "id": this.props.idStaff, "name": this.state.staffName, "email": this.state.staffEmail, "password": this.state.staffPassword
            }
        };

        fetch("/api/user/editStaff", {
            method: 'POST',
            headers: {
                'Accept': 'application/json',
                'Content-Type': 'application/json'
            },
            body: JSON.stringify(param)
        }).then(response => response.json()).then(async (data) => {
            if (data.action === "SUCCESS") {
                this.setState({ isDisabled: true, modalEdit: false });
                this.props.onRefresh();
            } else {
                alert("Something went wrong!!!");
            }
        });
    }

    _doDeleteStaff() {
        this.setState({ isDisabled: true });
        var param = {
            "managerId": cookies.get("u_id"), "managerToken": cookies.get("u_token"), staff: {
                "id": this.props.idStaff
            }
        };

        fetch("/api/user/deleteStaff", {
            method: 'POST',
            headers: {
                'Accept': 'application/json',
                'Content-Type': 'application/json'
            },
            body: JSON.stringify(param)
        }).then(response => response.json()).then(async (data) => {
            if (data.action === "SUCCESS") {
                this.setState({ modalDelete: false });
                this.props.onRefresh();
            } else {
                alert("Something went wrong!!!");
            }
        });
    }

    render() {

        return (<Table.Row>
            {/* Assign */}
            {/* <Modal open={this.state.modalAsign} >
                <Modal.Header>Assign New Website</Modal.Header>
                <Modal.Content >
                    <p >Staff Name</p>
                </Modal.Content>
                <Input type="text" style={{ marginLeft: '20px', width: '90%' }} defaultValue={this.props.nameStaff}></Input>
                <Modal.Content>
                    <p>Staff email</p>
                </Modal.Content>
                <Input type="text" style={{ marginLeft: '20px', marginBottom: '20px', width: '90%' }} defaultValue={this.props.emailStaff}></Input>
                <Modal.Content>
                    <p>Website email</p>
                </Modal.Content>
                <Dropdown style={{ marginLeft: '20px', marginBottom: '20px', width: '40%' }} options={this.props.websites} selection onChange={(event, data) => this._onChaneWebsite(event, data)} defaultValue={this.props.websites[0]} value={this.state.valWeb} />
                <Modal.Actions>
                    <Button onClick={() => this.setState({ modalAsign: false })}>Cancel</Button>
                    <Button color="blue" content='Assign' />
                </Modal.Actions>
            </Modal> */}

            {/* Delete */}
            <Modal
                open={this.state.modalDelete}
            >
                <Modal.Header>Delete Your Staff</Modal.Header>
                <Modal.Content>
                    <p>Are you sure to staff this website ?</p>
                </Modal.Content>
                <Modal.Actions>
                    <Button onClick={() => this.setState({ modalDelete: false })}>No</Button>
                    <Button color="blue" content='Yes' onClick={() => this._doDeleteStaff()} />
                </Modal.Actions>
            </Modal>

            {/* Edit */}
            <Modal open={this.state.modalEdit} >
                <Modal.Header>Edit Staff</Modal.Header>
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
                            <label>Staff password (Leave blank if no change)</label>
                            <Input type="password" onChange={(event) => this._onchangePassword(event)} value={this.state.staffPassword}></Input>
                        </Form.Field>

                    </Form>
                </Modal.Content>
                <Modal.Content>
                    <p>Website Asigned</p>
                    {this.props.webStaff.map((item, index) => {
                        return (<div key={index}>
                            {item.url}
                        </div>);
                    })}
                </Modal.Content>

                <Modal.Actions>
                    <Button onClick={() => this.setState({ modalEdit: false })}> Cancel</Button>
                    <Button content='Done' color='blue' disabled={this.state.isDisabled} onClick={() => this._doEditStaff()} />
                </Modal.Actions>
            </Modal>

            {/* Row */}
            <Table.Cell>{this.props.No + 1}</Table.Cell>
            <Table.Cell >Name: {this.props.nameStaff}<br /> Username: {this.props.emailStaff}</Table.Cell>
            <Table.Cell>
                {this.props.webStaff.map((item, index) => {
                    return (<div key={index}>
                        {item.url}
                    </div>);
                })}
            </Table.Cell>
            <Table.Cell>
                <Button color="orange" onClick={() => this.setState({ modalEdit: true })} > Edit </Button>
                <Button negative onClick={() => this.setState({ modalDelete: true })}> Delete</Button>
            </Table.Cell>
        </Table.Row >
        );
    }
}
export default TableRow