import React, { Component } from 'react';
import { Table, Button, Input, Modal, Transition } from 'semantic-ui-react'
import { Cookies } from "react-cookie";

const cookies = new Cookies();

export default class TableRow extends Component {
    state = { open: false, open1: false }
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
        fetch("/api/sitemap/makeVer", {
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

    render() {
        const { open, size } = this.state
        const { open1, closeOnEscape, closeOnDimmerClick } = this.state
        return (<Table.Row>
            {/* Delete */}
            <Modal
                open={open1}
                closeOnEscape={closeOnEscape}
                closeOnDimmerClick={closeOnDimmerClick}
                onClose={this.close1}
            >
                <Modal.Header>Delete Your Website</Modal.Header>
                <Modal.Content>
                    <p>Are you sure to delete this website ?</p>
                </Modal.Content>
                <Modal.Actions>
                    <Button onClick={this.close1}>No</Button>
                    <Button color="blue" content='Yes' />
                </Modal.Actions>
            </Modal>

            {/* Edit */}
            <Transition duration={200} divided size='huge' verticalAlign='middle' visible={open}>
                <Modal size={size} open={open} onClose={this.close}>
                    <Modal.Header>Edit Website</Modal.Header>
                    <Modal.Content >
                        <p >Website Name</p>
                    </Modal.Content>
                    <Input type="text" style={{ marginLeft: '20px', width: '90%' }} defaultValue={this.props.name}></Input>
                    <Modal.Content>
                        <p>Website URL</p>
                    </Modal.Content>
                    <Input type="text" style={{ marginLeft: '20px', marginBottom: '20px', width: '90%' }} defaultValue={this.props.url}></Input>
                    <Modal.Actions>
                        <Button onClick={this.close}>Cancel</Button>
                        <Button content='Done' color='blue' />
                    </Modal.Actions>
                </Modal>
            </Transition>

            {/* <Table.Cell style={{ width: '100px', whiteSpace: 'normal', wordBreak: 'break-all' }}><a href={this.props.urlPage}>{this.props.urlPage.split("www.")[1]}</a></Table.Cell>
            <Table.Cell style={{ width: '100px', whiteSpace: 'normal', wordBreak: 'break-all' }}><a href={this.props.urlLink}>{this.props.urlLink.split("www.")[1]}</a></Table.Cell> */}
            <Table.Cell style={{ width: '100px', whiteSpace: 'normal', wordBreak: 'break-all' }}><a >{this.props.id}</a></Table.Cell>
            <Table.Cell style={{ width: '100px', whiteSpace: 'normal', wordBreak: 'break-all' }}><a >{this.props.name}</a></Table.Cell>
            <Table.Cell style={{ width: '100px', whiteSpace: 'normal', wordBreak: 'break-all' }}><a >{this.props.url}</a></Table.Cell>
            <Table.Cell style={{ width: '100px', whiteSpace: 'normal', wordBreak: 'break-all' }}><a >{this.props.version}</a></Table.Cell>
            <Table.Cell style={{ width: '100px', whiteSpace: 'normal', wordBreak: 'break-all' }}><a >{this.props.time}</a></Table.Cell>
            <Table.Cell style={{ width: '100px', whiteSpace: 'normal', wordBreak: 'break-all' }}><Button onClick={() => this._makeNewver(this.props.id)} color="blue">Make</Button></Table.Cell>
            <Table.Cell style={{ width: '100px', whiteSpace: 'normal', wordBreak: 'break-all' }}><Button onClick={this.show('mini')} > Edit </Button><Button onClick={this.closeConfigShow(false, true)}> Delete</Button></Table.Cell>
            {/* <Table.Cell style={{ width: '100px', whiteSpace: 'normal', wordBreak: 'break-all' }}>{this.props.httpcode}</Table.Cell> */}
        </Table.Row >
        );
    }
}