import React, { Component } from 'react';
import {Segment , Button, Icon, Input, Table, TableCell} from 'semantic-ui-react'
import ModalExampleTopAligned from'./modal';
 import { Cookies } from "react-cookie";
 import TableRow from "./row-table";

const cookies = new Cookies();
export default class managestaffscreen extends Component {
    state = { list: [], loadingTable: false, isDisable: false };

  
    componentDidMount() {
        var comp = [];
        this.setState({ loadingTable: true });
        var param = { "id": cookies.get("u_id"), "userToken": cookies.get("u_token"), "websiteId": cookies.get("u_w_id") };
console.log(cookies.get("u_id"));
        fetch("/api/user/getstaff", {
            method: 'POST',
            headers: {
                'Accept': 'application/json',
                'Content-Type': 'application/json'
            },
            body: JSON.stringify(param)
        }).then(response => response.json()).then((data) => {
            console.log(data.liststaff[0].website)
            comp = data.liststaff.map((item, index) => {
                    return (<TableRow key={index} idStaff={item.id} nameStaff={item.name} emailStaff={item.email} webStaff={item.website} passwordStaff={item.password}/>);
            }); 
            this.setState({ list: comp });
        });
    }
    render() {
        return (
            <Segment.Group>
               
                <Segment basic>
                <div style={{marginBottom : '30px'}}>
                {/* <ModalExampleTopAligned/> */}

                                <Input icon='search' placeholder='Search...' style={{ float: 'right' }} />
                            </div>
                </Segment>
                <Segment.Group horizontal style={{ maxHeight: '63vh', overflow: "auto"}}>
                    <Segment basic>
                        <Table singleLine>
                            <Table.Header>
                                <Table.Row>
                                    <Table.HeaderCell>ID</Table.HeaderCell>
                                    <Table.HeaderCell>Infor</Table.HeaderCell>
                                    <Table.HeaderCell>Assign for Website</Table.HeaderCell>
                                    <Table.HeaderCell>Action</Table.HeaderCell>
                                    {/* <Table.HeaderCell>Last checked</Table.HeaderCell> */}
                                </Table.Row>
                            </Table.Header>
                            <Table.Body>
                                {this.state.list}
                            </Table.Body>
                        </Table>
                    </Segment>

                </Segment.Group>
            </Segment.Group>
        );
    }
}