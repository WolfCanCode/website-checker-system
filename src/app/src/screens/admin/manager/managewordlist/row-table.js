import React, { Component } from 'react';
import { Table, Button, Input, Modal, Transition } from 'semantic-ui-react'
import { Cookies } from "react-cookie";

const cookies = new Cookies();

export default class TableRow extends Component {
    state = {
        open: false, open1: false, oldWord: this.props.word, word: this.props.word, isDisable: true, editLoading: false, loadingDelete: false
    }

    show = size => () => this.setState({ size, open: true })
    close1 = () => this.setState({ open1: false })

    closeConfigShow = (closeOnEscape, closeOnDimmerClick) => () => {
        this.setState({ closeOnEscape, closeOnDimmerClick, open1: true })
    }
    close = () => this.setState({ open: false })



    _editWord() {
        this.setState({ editLoading: true, isDisable: false });
        var param = {
            "managerId": cookies.get("u_id"), "managerToken": cookies.get("u_token"), word: {
                "id": this.props.id, "word": this.state.word
            }
        };
        fetch("/api/word/editWord", {
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
                alert("This word is existed");
            }
        });
    }

    _deleteWord() {
        this.setState({ loadingDelete: true });
        var param = {
            "managerId": cookies.get("u_id"), "managerToken": cookies.get("u_token"), word: {
                "id": this.props.id
            }
        };
        fetch("/api/word/deleteWord", {
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




    _onchangeWord(event) {
        this.setState({ word: event.target.value }, () => this._checkAddBtn());
    }

    _checkAddBtn() {
        var result = false;
        if (this.state.word === "" || this.state.word === this.state.oldWord) {
            result = true;
        }
        this.setState({ isDisable: result });
    }
    render() {
        const { open, size } = this.state;
        const { open1, closeOnEscape, closeOnDimmerClick } = this.state;

        return (<Table.Row>
            {/* Delete */}
            <Transition duration={600} divided size='huge' verticalAlign='middle' visible={open1}>
                <Modal
                    open={open1}
                    closeOnEscape={closeOnEscape}
                    closeOnDimmerClick={closeOnDimmerClick}
                    onClose={this.close1}
                >
                    <Modal.Header>Delete Your Word</Modal.Header>
                    <Modal.Content>
                        <p>Are you sure to delete this word ?</p>
                    </Modal.Content>
                    <Modal.Actions>
                        <Button onClick={this.close1}>No</Button>
                        <Button color="blue" content='Yes' onClick={() => this._deleteWord()} loading={this.state.loadingDelete}
                        />
                    </Modal.Actions>
                </Modal>
            </Transition>
            {/* Edit */}
            <Transition duration={600} divided size='huge' verticalAlign='middle' visible={open}>
                <Modal size={size} open={open} >
                    <Modal.Header>Edit Word</Modal.Header>
                    <Modal.Content >
                        <p >Word</p>
                    </Modal.Content>
                    <Input type="text" style={{ marginLeft: '20px', width: '90%', marginBottom: '20px' }} onChange={(event) => this._onchangeWord(event)} value={this.state.word}></Input>

                    <Modal.Actions>
                        <Button onClick={this.close}>Cancel</Button>
                        <Button content='Done' color='blue' disabled={this.state.isDisable} onClick={() => this._editWord()} />
                    </Modal.Actions>
                </Modal>
            </Transition>




            <Table.Cell ><a >{this.props.id}</a></Table.Cell>
            <Table.Cell ><a >{this.props.word}</a></Table.Cell>
            <Table.Cell ><a >{this.props.createdTime}</a></Table.Cell>
            <Table.Cell ><Button onClick={this.show('mini')} > Edit </Button><Button onClick={this.closeConfigShow(false, true)}> Delete</Button></Table.Cell>

        </Table.Row >
        );
    }
}