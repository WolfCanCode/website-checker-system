import React, { Component } from 'react';
import { Segment, Button, SegmentGroup, Input, Table, Modal, Form } from 'semantic-ui-react'

import TableRow from './row-table';
// import logo1 from './images/mobile.png';

import { Cookies } from "react-cookie";

const cookies = new Cookies();

export default class managewordlist extends Component {


    state = { addModal: false, isLoading: false, listWord: null, word: "", isDisable: true, addLoading: false }





    componentDidMount() {
        this._refreshTable();
    }

    _loadingTable(isLoading) {
        this.setState({ isLoading: isLoading })
    }

    _refreshTable() {
        fetch("/api/word/manage", {
            method: 'POST',
            headers: {
                'Accept': 'application/json',
                'Content-Type': 'application/json'
            },
            body: JSON.stringify({ "managerId": cookies.get("u_id"), "managerToken": cookies.get("u_token") })
        }).then(response => response.json()).then((data) => {
            if (data.action === "SUCCESS") {
                var list = data.wordList.map((item, index) => {
                    return (<TableRow key={index} id={item.id} word={item.word} createdTime={item.createdTime} loadingTable={(isLoading) => this._loadingTable(isLoading)} refreshTable={() => this._refreshTable()} />);
                });
                this.setState({ listWord: list, isLoading: false });

            }
        });

    }

    _onchangeWord(event) {
        this.setState({ word: event.target.value }, () => this._checkAddBtn());
    }



    _checkAddBtn() {
        var result = false;
        if (this.state.word === "") {
            result = true;
        }
        this.setState({ isDisable: result });
    }

    _addWord() {
        this.setState({ addLoading: true, isDisable: false });
        var param = {
            "managerId": cookies.get("u_id"), "managerToken": cookies.get("u_token")
            , word: {
                "word": this.state.word
            }
        };
        fetch("/api/word/addWord", {
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
                this._refreshTable();

            } if (data.action === "DUPLICATE ERROR") {
                alert("This word is existed");
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
                            <Modal open={this.state.addModal}>
                                <Modal.Header>Add Word</Modal.Header>
                                <Modal.Content>
                                    <Form>
                                        <Form.Field>
                                            <label>Word</label>
                                            <Input type="text" placeholder="Word" onChange={(event) => this._onchangeWord(event)} value={this.state.word}></Input>
                                        </Form.Field>

                                    </Form>
                                </Modal.Content>
                                <Modal.Actions>
                                    <Button onClick={() => this.setState({ addModal: false })}>Cancel</Button>
                                    <Button content='Done' color='blue' loading={this.state.addLoading} disabled={this.state.isDisable} onClick={() => this._addWord()} />
                                </Modal.Actions>
                            </Modal>

                        </div>
                    </Segment>
                    <Segment.Group horizontal style={{ maxHeight: '63vh', overflow: "auto" }}>
                        <Segment basic loading={this.state.isLoading}>
                            <Table singleLine unstackable>
                                <Table.Header>
                                    <Table.Row>
                                        <Table.HeaderCell>No</Table.HeaderCell>
                                        <Table.HeaderCell>Word</Table.HeaderCell>
                                        <Table.HeaderCell>Lastest updated</Table.HeaderCell>
                                        <Table.HeaderCell>Action</Table.HeaderCell>

                                    </Table.Row>
                                </Table.Header>
                                <Table.Body>
                                    {this.state.listWord}
                                </Table.Body>
                            </Table>
                        </Segment>

                    </Segment.Group>


                </SegmentGroup>
            </div>
        );
    }
}