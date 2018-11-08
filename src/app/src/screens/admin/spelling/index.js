import React, { Component } from 'react';
import 'semantic-ui-css/semantic.min.css';
import { Segment, Button, Table, Icon, Input, Label } from 'semantic-ui-react'
// import { Cookies } from "react-cookie";

// const cookies = new Cookies();

class spellingScreen extends Component {

    render() {
        return (
            <div>
                <Segment.Group>
                    <Segment.Group horizontal>

                        <Segment basic>



                            <Segment.Group horizontal >
                                <Segment style={{ margin: 'auto', textAlign: 'center', padding: 0 }}>
                                    <Icon className="warning sign" size='huge' color='red' />
                                </Segment>
                                <Segment style={{ paddingLeft: '10px' }}>
                                    <p style={{ fontSize: 24 }}>4 <br />
                                        Unrecognigzed spellings</p>
                                </Segment >
                                <Segment style={{ margin: 'auto', textAlign: 'center', padding: 0 }}>
                                    <Icon className="file outline" size='huge' color='grey' /></Segment>
                                <Segment>
                                    <p style={{ fontSize: 24 }}>2 <br /> Affected Pages</p>
                                </Segment>
                            </Segment.Group>
                            <div style={{ marginBottom: '60px', marginRight: '20px' }}>
                                <Button floated='right' ><Icon name="print" />Export</Button>

                                <Input icon='search' placeholder='Search...' style={{ float: 'right' }} />
                            </div>
                            <Table singleLine unstackable textAlign='center' style={{ tableLayout: 'auto' }}>
                                <Table.Header >
                                    <Table.Row>
                                        <Table.HeaderCell>Word</Table.HeaderCell>
                                        <Table.HeaderCell>Suggesttons</Table.HeaderCell>
                                        <Table.HeaderCell>Found in</Table.HeaderCell>
                                        <Table.HeaderCell>Pages</Table.HeaderCell>
                                        <Table.HeaderCell>Action</Table.HeaderCell>

                                    </Table.Row>
                                </Table.Header>
                                <Table.Body>
                                    <Table.Row>
                                        <Table.Cell style={{ width: '100px', whiteSpace: 'normal', wordBreak: 'break-all' }}>suberd</Table.Cell>
                                        <Table.Cell style={{ width: '100px', whiteSpace: 'normal', wordBreak: 'break-all' }}>suburd superd subduer</Table.Cell>
                                        <Table.Cell style={{ width: '100px', whiteSpace: 'normal', wordBreak: 'break-all' }}>Text</Table.Cell>
                                        <Table.Cell style={{ width: '100px', whiteSpace: 'normal', wordBreak: 'break-all' }}> <Label style={{ fontSize: '13px' }} >1</Label></Table.Cell>
                                        <Table.Cell style={{ width: '100px', whiteSpace: 'normal', wordBreak: 'break-all' }}><Button color='blue'>Learn Spelling</Button> </Table.Cell>

                                    </Table.Row>
                                    <Table.Row>
                                        <Table.Cell style={{ width: '100px', whiteSpace: 'normal', wordBreak: 'break-all' }}>suberd</Table.Cell>
                                        <Table.Cell style={{ width: '100px', whiteSpace: 'normal', wordBreak: 'break-all' }}>suburd superd subduer</Table.Cell>
                                        <Table.Cell style={{ width: '100px', whiteSpace: 'normal', wordBreak: 'break-all' }}>Text</Table.Cell>
                                        <Table.Cell style={{ width: '100px', whiteSpace: 'normal', wordBreak: 'break-all' }}> <Label style={{ fontSize: '13px' }} >1</Label></Table.Cell>
                                        <Table.Cell style={{ width: '100px', whiteSpace: 'normal', wordBreak: 'break-all' }}><Button color='blue'>Learn Spelling</Button> </Table.Cell>

                                    </Table.Row>
                                    <Table.Row>
                                        <Table.Cell style={{ width: '100px', whiteSpace: 'normal', wordBreak: 'break-all' }}>suberd</Table.Cell>
                                        <Table.Cell style={{ width: '100px', whiteSpace: 'normal', wordBreak: 'break-all' }}>suburd superd subduer</Table.Cell>
                                        <Table.Cell style={{ width: '100px', whiteSpace: 'normal', wordBreak: 'break-all' }}>Text</Table.Cell>
                                        <Table.Cell style={{ width: '100px', whiteSpace: 'normal', wordBreak: 'break-all' }}> <Label style={{ fontSize: '13px' }} >1</Label></Table.Cell>
                                        <Table.Cell style={{ width: '100px', whiteSpace: 'normal', wordBreak: 'break-all' }}><Button color='blue'>Learn Spelling</Button> </Table.Cell>

                                    </Table.Row>
                                    <Table.Row>
                                        <Table.Cell style={{ width: '100px', whiteSpace: 'normal', wordBreak: 'break-all' }}>suberd</Table.Cell>
                                        <Table.Cell style={{ width: '100px', whiteSpace: 'normal', wordBreak: 'break-all' }}>suburd superd subduer</Table.Cell>
                                        <Table.Cell style={{ width: '100px', whiteSpace: 'normal', wordBreak: 'break-all' }}>Text</Table.Cell>
                                        <Table.Cell style={{ width: '100px', whiteSpace: 'normal', wordBreak: 'break-all' }}> <Label style={{ fontSize: '13px' }} >1</Label></Table.Cell>
                                        <Table.Cell style={{ width: '100px', whiteSpace: 'normal', wordBreak: 'break-all' }}><Button color='blue'>Learn Spelling</Button> </Table.Cell>

                                    </Table.Row>




                                </Table.Body>
                            </Table>

                        </Segment>

                    </Segment.Group>
                </Segment.Group>
            </div>

        );
    }



}

export default spellingScreen;