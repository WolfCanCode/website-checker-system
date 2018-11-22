import React, { Component } from 'react';
import { Segment, Button, SegmentGroup, Input, Table, Modal, Form, Dropdown } from 'semantic-ui-react'

import TableRow from './row-table';
// import logo1 from './images/mobile.png';

import { Cookies } from "react-cookie";

const cookies = new Cookies();

export default class managewordlist extends Component {


    state = { addModal: false, isLoading: false, listWord: null, word: "", type : "", isDisable: true, addLoading: false, topicList:[], topicSelected:[] }





    componentDidMount() {
        this._refreshTable();
        this._getTopicList();
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
                console.log(data);
                
                var list = data.warningWordList.map((item, index) => {
                    return (<TableRow key={index} no = {index} id={item.id} word={item.word} type={item.topic} modifiedTime={item.modifiedTime} loadingTable={(isLoading) => this._loadingTable(isLoading)} refreshTable={() => this._refreshTable()} />);
                });
                this.setState({ listWord: list, isLoading: false });

            }
        });
        
    }

    _getTopicList(){
        fetch("/api/word/getTopicList", {
            method: 'POST',
            headers: {
                'Accept': 'application/json',
                'Content-Type': 'application/json'
            },
            body: JSON.stringify({ "managerId": cookies.get("u_id"), "managerToken": cookies.get("u_token") })
           
        }).then(response => response.json()).then((data) => {
            if (data.action === "SUCCESS") {
                
                
                var list = data.topicList.map((item, index) => {
                    return {key:item.id, text:item.typeName, value:item.id};
                });
                console.log(list);
                
                this.setState({ topicList: list, isLoading: false });

            }
        });


    }

    _onchangeType(event, data) {
        console.log(data);
      var typeSelected=  this.state.topicList.find(item=>{          
            return item.value === data.value
        });
        console.log(typeSelected);
        
        this.setState({
            topicSelected: typeSelected
        },() => this._checkAddBtn());
        
    }

    _onchangeWord(event) {
        this.setState({ word: event.target.value }, () => this._checkAddBtn());
    }

    



    _checkAddBtn() {
        var result = false;
        if (this.state.word === "" || this.state.topicSelected.length ===0) {
            result = true;
        }
        this.setState({ isDisable: result });
    }

    _addWarningWord() {
        this.setState({ addLoading: true, isDisable: false });
        var param = {
            "managerId": cookies.get("u_id"), "managerToken": cookies.get("u_token")
            , warningWord: {
                "word": this.state.word, "topic": {id:this.state.topicSelected.value, typeName: this.state.topicSelected.text}
            }
        };
        fetch("/api/word/addWarningWord", {
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
                this.setState({topicSelected:[]}); 
                this.setState({word:""});
                this.setState({typeSelected:[]});
                this._refreshTable();

            } if (data.action === "DUPLICATE ERROR") {
                alert("This word is existed");
                this.setState({ addLoading: false });
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
                                            <Input type="text" placeholder="Word" onChange={(event) => this._onchangeWord(event)} ></Input>
                                        </Form.Field>
                                        <Form.Field>
                                            <label>Type</label>
                                            <Dropdown placeholder='' fluid  selection options={this.state.topicList} onChange={(event, data) => this._onchangeType(event, data)}/>
                                            {/* <Input type="text" placeholder="Type" onChange={(event) => this._onchangeType(event)} value={this.state.type}></Input> */}
                                        </Form.Field>

                                    </Form>
                                    
                                </Modal.Content>
                                <Modal.Actions>
                                    <Button onClick={() => this.setState({ addModal: false })}>Cancel</Button>
                                    <Button content='Done' color='blue' loading={this.state.addLoading} disabled={this.state.isDisable} onClick={() => this._addWarningWord()} />
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
                                        <Table.HeaderCell>Type</Table.HeaderCell>
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