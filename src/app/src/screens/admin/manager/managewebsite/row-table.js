import React, { Component } from 'react';
import { Table, Button, Input, Modal, Transition, Dropdown } from 'semantic-ui-react'
import { Cookies } from "react-cookie";

const cookies = new Cookies();

export default class TableRow extends Component {
    state = {
        open: false, open1: false, oldWebName: this.props.name, webName: this.props.name, isDisable: true, editLoading: false, options: [], userAssign: [], defValue: [], loadingDelete: false
    }
    constructor(props) {
        super(props);
        this._makeNewver = this._makeNewver.bind(this);
    }
    show = size => () => this.setState({ size, open: true })
    close1 = () => this.setState({ open1: false })

    closeConfigShow = (closeOnEscape, closeOnDimmerClick) => () => {
        this.setState({ closeOnEscape, closeOnDimmerClick, open1: true })
    }
    close = () => this.setState({ open: false })

    _makeNewver(id) {
        this.props.loadingTable(true);
        var param = { "userId": cookies.get("u_id"), "userToken": cookies.get("u_token"), "websiteId": id };
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
                this.props.refreshTable(this.state.version);
            } else {
                alert("Thất bại");
            }
        });
    }

    _editWebsite() {
        this.setState({ editLoading: true, isDisable: false });
        var param = {
            "managerId": cookies.get("u_id"), "managerToken": cookies.get("u_token"), website: {
                "id": this.props.id, "name": this.state.webName
            }
        };
        fetch("http://localhost:8080/api/manager/editWebsite", {
            method: 'POST',
            headers: {
                'Accept': 'application/json',
                'Content-Type': 'application/json'
            },
            body: JSON.stringify(param)
        }).then(response => response.json()).then((data) => {
            if (data.action === "SUCCESS") {
                this.setState({ editLoading: false });
                this.setState({ open: false });
                this.props.refreshTable();
            } if (data.action === "DUPLICATE ERROR") {
                alert("This website is existed");
            }
        });
    }

    _deleteWebsite() {
        this.setState({ loadingDelete: true });
        var param = {
            "managerId": cookies.get("u_id"), "managerToken": cookies.get("u_token"), website: {
                "id": this.props.id
            }
        };
        fetch("http://localhost:8080/api/manager/deleteWebsite", {
            method: 'POST',
            headers: {
                'Accept': 'application/json',
                'Content-Type': 'application/json'
            },
            body: JSON.stringify(param)
        }).then(response => response.json()).then((data) => {
            if (data.action === "SUCCESS") {
                this.setState({ open1: false, close1: true, loadingDelete: true });
                this.props.refreshTable();
            }
        });
    }

    _assignModal() {
        var param = {
            "managerId": cookies.get("u_id"), "managerToken": cookies.get("u_token"), website: {
                "id": this.props.id
            }
        };
        fetch("http://localhost:8080/api/manager/defaultAssign", {
            method: 'POST',
            headers: {
                'Accept': 'application/json',
                'Content-Type': 'application/json'
            },
            body: JSON.stringify(param)
        }).then(response => response.json()).then((data) => {
            var op = data.staffs.map((item, index) => {
                return { key: item.name, value: item.id, text: item.email };
            })
            var def = data.defStaffs.map((item, index) => {
                return item.id;
            });
            // var defAss = [];
            // for (let i = 0; i < op.length; i++) {
            //     for (let j = 0; j < def.length; j++) {
            //         if (op[i].value === def[j].value) {
            //             defAss.push(op[i]);
            //         }
            //     }
            // }
            this.setState({ options: op, assignModal: true, userAssign: def });
        });
    }

    _changeAssign(event, data) {
        this.setState({
            userAssign: data.value
        }, () => this._checkAssignBtn());
    }

    _checkAssignBtn() {
        if (this.state.userAssign.length === 0) {
            this.setState({ isDisable: true });
        } else {
            this.setState({ isDisable: false });
        }
    }

    _doAssignUser() {
        var param = {
            "managerId": cookies.get("u_id"), "managerToken": cookies.get("u_token"), website: {
                "id": this.props.id
            }, listStaffId: this.state.userAssign
        };
        fetch("http://localhost:8080/api/manager/assignWebsite", {
            method: 'POST',
            headers: {
                'Accept': 'application/json',
                'Content-Type': 'application/json'
            },
            body: JSON.stringify(param)
        }).then(response => response.json()).then((data) => {
            if (data.action === "SUCCESS") {
                this.setState({ assignModal: false });
            }
        });
    }

    _onchangeName(event) {
        this.setState({ webName: event.target.value }, () => this._checkAddBtn());
    }

    _checkAddBtn() {
        var result = false;
        if (this.state.webName === "" || this.state.webName === this.state.oldWebName) {
            result = true;
        }
        this.setState({ isDisable: result });
    }
    render() {
        const { open, size } = this.state;
        const { open1, closeOnEscape, closeOnDimmerClick } = this.state;
        const renderLabel = label => ({
            color: 'blue',
            content: `Staff: ${label.key} - ${label.text}`,
            icon: 'check',
        })
        return (<Table.Row>
            {/* Delete */}
            <Transition duration={600} divided size='huge' verticalAlign='middle' visible={open1}>
                <Modal
                    open={open1}
                    closeOnEscape={closeOnEscape}
                    closeOnDimmerClick={closeOnDimmerClick}
                    onClose={this.close1}
                    dimmer="blurring"
                >
                    <Modal.Header>Delete Your Website</Modal.Header>
                    <Modal.Content>
                        <p>Are you sure to delete this website ?</p>
                    </Modal.Content>
                    <Modal.Actions>
                        <Button onClick={this.close1}>No</Button>
                        <Button color="blue" content='Yes' onClick={() => this._deleteWebsite()} loading={this.state.loadingDelete}
                        />
                    </Modal.Actions>
                </Modal>
            </Transition>
            {/* Edit */}
            <Transition duration={600} divided size='huge' verticalAlign='middle' visible={open}>
                <Modal
                    size={size}
                    open={open}
                    dimmer="blurring" >
                    <Modal.Header>Edit Website</Modal.Header>
                    <Modal.Content >
                        <p >Website Name</p>
                    </Modal.Content>
                    <Input type="text" style={{ marginLeft: '20px', width: '90%' }} onChange={(event) => this._onchangeName(event)} value={this.state.webName}></Input>
                    <Modal.Content>
                        <p>Website URL</p>
                    </Modal.Content>
                    <Input type="text" style={{ marginLeft: '20px', marginBottom: '20px', width: '90%' }} defaultValue={this.props.url} disabled={true}></Input>
                    <Modal.Actions>
                        <Button onClick={this.close}>Cancel</Button>
                        <Button content='Done' color='blue' disabled={this.state.isDisable} onClick={() => this._editWebsite()} />
                    </Modal.Actions>
                </Modal>
            </Transition>

            {/* Assign */}
            <Transition duration={600} divided size='huge' verticalAlign='middle' visible={this.state.assignModal}>
                <Modal
                    size={"large"}
                    open={this.state.assignModal}
                    dimmer="blurring">
                    <Modal.Header>Assign Website</Modal.Header>
                    <Modal.Content >
                        <p >Staffs:</p>
                    </Modal.Content>
                    <Dropdown
                        multiple
                        selection
                        fluid
                        options={this.state.options}
                        placeholder='Choose an option'
                        renderLabel={renderLabel}
                        onChange={(event, data) => this._changeAssign(event, data)}
                        value={this.state.userAssign}
                    />                    <Modal.Actions>
                        <Button onClick={() => this.setState({ assignModal: false })}>Cancel</Button>
                        <Button content='Done' color='blue' disabled={this.state.isDisable} onClick={() => this._doAssignUser()} />
                    </Modal.Actions>
                </Modal>
            </Transition>

            <Table.Cell ><a >{this.props.id}</a></Table.Cell>
            <Table.Cell ><a >{this.props.name}</a></Table.Cell>
            <Table.Cell ><a >{this.props.url}</a></Table.Cell>
            <Table.Cell ><a >{this.props.version}</a></Table.Cell>
            <Table.Cell ><a >{this.props.time}</a></Table.Cell>
            <Table.Cell ><Button onClick={() => this._makeNewver(this.props.id)} color="green">Make</Button></Table.Cell>
            <Table.Cell >
                <Button primary onClick={() => this._assignModal()} > Assign </Button>
                <Button color="orange" onClick={this.show('mini')} > Edit </Button>
                <Button negative onClick={this.closeConfigShow(false, true)}> Delete</Button></Table.Cell>
        </Table.Row >
        );
    }
}