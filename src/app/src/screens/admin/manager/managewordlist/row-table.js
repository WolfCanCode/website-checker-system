import React, { Component } from 'react';
import { Table, Button, Input, Modal, Transition, Dropdown } from 'semantic-ui-react'
import { Cookies } from "react-cookie";

const cookies = new Cookies();

export default class TableRow extends Component {
    state = {
        open: false, open1: false, word: this.props.word,type: this.props.type, isDisable: true, editLoading: false, loadingDelete: false, topicList:[], topicSelected:[]
    }

    
    show = size => () => this.setState({ size, open: true })
    close1 = () => this.setState({ open1: false })

    closeConfigShow = (closeOnEscape, closeOnDimmerClick) => () => {
        this.setState({ closeOnEscape, closeOnDimmerClick, open1: true })
    }
    close = () => this.setState({ open: false })



    _editWarningWord() {
        this.setState({ editLoading: true, isDisable: false });
        var param = {
            "managerId": cookies.get("u_id"), "managerToken": cookies.get("u_token"), warningWord: {
                "id": this.props.id, "word": this.state.word, "topic": this.state.type
            }
        };
        fetch("/api/word/editWarningWord", {
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
                this.setState({isDisable : false})
                this.props.refreshTable();
            } if (data.action === "DUPLICATE ERROR") {
                alert("This word is existed");
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
            type: {
                id:typeSelected.value,
                typeName: typeSelected.text
            }
        },() => this._checkEditBtn());
        
    }
    
    componentDidMount() {
        
        this._getTopicList();
        console.log("dsd" + this.props.type);
        
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
                    return { key:item.id, text:item.typeName, value:item.id};
                });
                console.log(list);
                
                this.setState({ topicList: list, isLoading: false});

            }
        });


    }

    _deleteWarmingWord() {
        this.setState({ loadingDelete: true });
        var param = {
            "managerId": cookies.get("u_id"), "managerToken": cookies.get("u_token"), warningWord: {
                "id": this.props.id
            }
        };
        fetch("/api/word/deleteWarningWord", {
            method: 'POST',
            headers: {
                'Accept': 'application/json',
                'Content-Type': 'application/json'
            },
            body: JSON.stringify(param)
        }).then(response => response.json()).then((data) => {
            if (data.action === "SUCCESS") {
                this.setState({ open1: false, close1: true, loadingDelete: false });
                this.props.refreshTable();
            }
        });
    }




    _onchangeWord(event) {
        this.setState({ word: event.target.value }, () => this._checkEditBtn());
    }
   

    _checkEditBtn() {
        var result = false;
        console.log(this.state.word);
        console.log(this.state.oldWord);
        
        if (this.state.word === ""  ) {
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
                        <Button color="blue" content='Yes' onClick={() => this._deleteWarmingWord()} loading={this.state.loadingDelete}
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
                    <Modal.Content >
                        <p >Type</p>
                    </Modal.Content>
                    {/* <Input type="text" style={{ marginLeft: '20px', width: '90%', marginBottom: '20px' }} onChange={(event) => this._onchangeType(event)} value={this.state.type}></Input> */}
                    <Dropdown style={{ marginLeft: '20px', width: '90%', marginBottom: '20px' }} placeholder='' fluid  selection value={this.state.type.id} options={this.state.topicList}  onChange={(event, data) => this._onchangeType(event, data)}/>

                    <Modal.Actions>
                        <Button onClick={this.close}>Cancel</Button>
                        <Button content='Done' color='blue' disabled={this.state.isDisable} onClick={() => this._editWarningWord()} />
                    </Modal.Actions>
                </Modal>
            </Transition>




            <Table.Cell ><a >{this.props.no + 1}</a></Table.Cell>
            <Table.Cell ><a >{this.props.word}</a></Table.Cell>
            <Table.Cell ><a >{this.props.modifiedTime.split("T")[0]}</a></Table.Cell>
            <Table.Cell ><a >{this.props.type.typeName}</a></Table.Cell>
            <Table.Cell ><Button onClick={this.show('mini')} color="orange"> Edit </Button><Button onClick={this.closeConfigShow(false, true)}negative> Delete</Button></Table.Cell>

        </Table.Row >
        );
    }
}