import React, { Component } from 'react';
import { Button, Table, Icon, Modal, Transition } from 'semantic-ui-react';

import { Cookies } from "react-cookie";
import SuggestRow from "./suggest-row";

const cookies = new Cookies();
export default class TableRow extends Component {

    state = {
        wrongWord: this.props.word, excerpt: this.props.excerpt, page: this.props.page,
        showSuggestDialog: false, listSuggestion: []
    }

    _getSuggestion(wrongWord) {
        var comp = [];
        // this.props.loadingTable(true);
        // var param = { "userId": cookies.get("u_id"), "userToken": cookies.get("u_token"), "websiteId": id };
        var param = {
            "userId": cookies.get("u_id"),
            "userToken": cookies.get("u_token"),
            "websiteId": cookies.get("u_w_id"),
            "pageOptionId": cookies.get("u_option"),
            "wrongWord": wrongWord,
        };
        fetch("/api/spelling/getSuggestion", {
            method: 'POST',
            headers: {
                'Accept': 'application/json',
                'Content-Type': 'application/json'
            },
            body: JSON.stringify(param)
        }).then(response => response.json()).then((data) => {

            comp = data.suggestObjectList.map((item, index) => {

                return (<SuggestRow no={index} word={item.suggestWord} type={item.type} definition={item.definition} />);

            });

            this.setState({ showSuggestDialog: true, listSuggestion: comp })

        });
    }
    render() {
        return (
            <Table.Row>
                <Table.Cell style={{ width: '10px', whiteSpace: 'normal', wordBreak: 'break-all' }}>{this.props.no + 1} </Table.Cell>
                <Table.Cell style={{ width: '200px', whiteSpace: 'normal', wordBreak: 'break-all' }}>{this.state.wrongWord}</Table.Cell>
                <Table.Cell style={{ width: '200px', whiteSpace: 'normal', wordBreak: 'break-all' }}>{this.state.excerpt}</Table.Cell>
                <Table.Cell style={{ width: '250px', whiteSpace: 'normal', wordBreak: 'break-all' }}>{this.state.page}</Table.Cell>
                <Table.Cell style={{ width: '300px', whiteSpace: 'normal', wordBreak: 'break-all' }}>
                    <Table.Cell > <Button primary> Ignore </Button> </Table.Cell>
                    <Table.Cell > <Button primary onClick={() => this._getSuggestion(this.state.wrongWord)}  > <Icon name='eye' />View suggestions</Button> </Table.Cell>
                </Table.Cell>

                <Transition duration={600} divided verticalAlign='middle' visible={this.state.showSuggestDialog}>
                    <Modal open={this.state.showSuggestDialog} style={{ width: '400', height: 'auto', background: 'white' }}>
                        <Modal.Header style={{ textAlign: 'left', fontSize: 25, fontWeight: 'bold', color: 'black' }}>
                            Suggestion for word: {this.state.wrongWord}
                        </Modal.Header>
                        <Modal.Content >
                            <Table singleLine unstackable>

                                <Table.Header>
                                    <Table.Row>
                                        <Table.HeaderCell>No.</Table.HeaderCell>
                                        <Table.HeaderCell>Word</Table.HeaderCell>
                                        <Table.HeaderCell>Type</Table.HeaderCell>
                                        <Table.HeaderCell>Definition</Table.HeaderCell>
                                    </Table.Row>
                                </Table.Header>
                                <Table.Body>
                                    {this.state.listSuggestion}
                                </Table.Body>
                            </Table>
                        </Modal.Content>
                        <Modal.Actions >
                            <Button onClick={() => this.setState({ showSuggestDialog: false })}> Cancel</Button>
                        </Modal.Actions>
                    </Modal>
                </Transition>
            </Table.Row>


        );
    }
}