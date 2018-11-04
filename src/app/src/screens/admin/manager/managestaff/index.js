import React, { Component } from 'react';
import {Segment , Button, Icon, Input, Table, TableCell} from 'semantic-ui-react'
import TableRow from '../../javascripterror/row-table';
// import { Cookies } from "react-cookie";

// const cookies = new Cookies();
export default class managestaffscreen extends Component {

    render() {
        return (
            <Segment.Group>
                <Segment basic>
                <div style={{marginBottom : '30px'}}>
                                

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
                               <Table.Row>
                                   <TableCell>SE1111</TableCell>
                                   <TableCell>Name: John<br/>Username: staff1 </TableCell>
                                   <TableCell>WWW.ABC.COM</TableCell>
                                   <TableCell><Button> Edit </Button><Button primary> New Assign</Button></TableCell>
                               </Table.Row>
                               <Table.Row>
                                   <TableCell>SE1112</TableCell>
                                   <TableCell>Name: Henry<br/>Username: staff2</TableCell>
                                   <TableCell>WWW.ABC.COM<br/> WWW.ACB.COM</TableCell>
                                   <TableCell><Button> Edit </Button><Button primary> New Assign</Button></TableCell>
                               </Table.Row>
                            </Table.Body>
                        </Table>
                    </Segment>

                </Segment.Group>
            </Segment.Group>
        );
    }
}