import React, { Component } from 'react';
import 'semantic-ui-css/semantic.min.css';
import { Segment, Button, Table, Image, Icon, Input, Label } from 'semantic-ui-react'
import BrokenPages from '../../../assets/brokenPage.png';






class brokenPagesScreen extends Component {

    render() {
        return (
            <div style={{ height: 'auto', marginTop: '20px' }}>

                <Segment.Group>
                    <Segment><h3>Broken Pages test</h3></Segment>
                    <Segment.Group horizontal>

                        <Segment basic>



                            <Segment.Group horizontal >

                                <Segment style={{ margin: 'auto', textAlign: 'center', padding: 0 }}>
                                    <Icon className="warning sign" size='huge' color='red' />
                                </Segment>
                                <Segment style={{ paddingLeft: '10px' }}>
                                    <p style={{ fontSize: 24 }}>10<br />
                                        Missing pages</p>
                                </Segment >
                                <Segment style={{ margin: 'auto', textAlign: 'center', padding: 0 }}>
                                    <Icon className="check" size='huge' color='green' />
                                </Segment>
                                <Segment style={{ paddingLeft: '10px' }}>
                                    <p style={{ fontSize: 24 }}>0 <br />
                                        Empty pages</p>
                                </Segment >
                                <Segment style={{ margin: 'auto', textAlign: 'center', padding: 0 }}>
                                    <Icon className="warning sign" size='huge' color='black' /></Segment>
                                <Segment>
                                    <p style={{ fontSize: 24 }}>2 <br /> Error pages</p>
                                </Segment>
                            </Segment.Group>
                            <div style={{ marginBottom: '60px', marginRight: '20px' }}>
                                <Button floated='right' ><Icon name="print" />Export</Button>

                                <Input icon='search' placeholder='Search...' style={{ float: 'right' }} />
                            </div>
                            <Table singleLine textAlign='center' style={{ tableLayout: 'auto' }}>
                                <Table.Header >
                                    <Table.Row>
                                        <Table.HeaderCell>Page</Table.HeaderCell>
                                        <Table.HeaderCell>Status</Table.HeaderCell>
                                        <Table.HeaderCell>HTTP Code</Table.HeaderCell>


                                    </Table.Row>
                                </Table.Header>
                                <Table.Body>
                                    <Table.Row>
                                        <Table.Cell style={{ width: '100px', whiteSpace: 'normal', wordBreak: 'break-all' }}><a href='https://react.semantic-ui.com'>www.react.semantic-ui.com</a></Table.Cell>
                                        <Table.Cell style={{ width: '100px', whiteSpace: 'normal', wordBreak: 'break-all' }}> <Label style={{ fontSize: '13px' }} color='red' horizontal>Missing Page</Label></Table.Cell>
                                        <Table.Cell style={{ width: '100px', whiteSpace: 'normal', wordBreak: 'break-all' }}> <Label style={{ fontSize: '13px' }} >404</Label></Table.Cell>
                                    </Table.Row>
                                    <Table.Row>
                                        <Table.Cell style={{ width: '100px', whiteSpace: 'normal', wordBreak: 'break-all' }}><a href='https://react.semantic-ui.com'>www.react.semantic-ui.com</a></Table.Cell>
                                        <Table.Cell style={{ width: '100px', whiteSpace: 'normal', wordBreak: 'break-all' }}> <Label style={{ fontSize: '13px' }} color='red' horizontal>Missing Page</Label></Table.Cell>
                                        <Table.Cell style={{ width: '100px', whiteSpace: 'normal', wordBreak: 'break-all' }}> <Label style={{ fontSize: '13px' }} >404</Label></Table.Cell>
                                    </Table.Row>
                                    <Table.Row>
                                        <Table.Cell style={{ width: '100px', whiteSpace: 'normal', wordBreak: 'break-all' }}><a href='https://react.semantic-ui.com'>www.react.semantic-ui.com</a></Table.Cell>
                                        <Table.Cell style={{ width: '100px', whiteSpace: 'normal', wordBreak: 'break-all' }}> <Label style={{ fontSize: '13px' }} color='red' horizontal>Missing Page</Label></Table.Cell>
                                        <Table.Cell style={{ width: '100px', whiteSpace: 'normal', wordBreak: 'break-all' }}> <Label style={{ fontSize: '13px' }} >404</Label></Table.Cell>
                                    </Table.Row>
                                    <Table.Row>
                                        <Table.Cell style={{ width: '100px', whiteSpace: 'normal', wordBreak: 'break-all' }}><a href='https://react.semantic-ui.com'>www.react.semantic-ui.com</a></Table.Cell>
                                        <Table.Cell style={{ width: '100px', whiteSpace: 'normal', wordBreak: 'break-all' }}> <Label style={{ fontSize: '13px' }} color='red' horizontal>Missing Page</Label></Table.Cell>
                                        <Table.Cell style={{ width: '100px', whiteSpace: 'normal', wordBreak: 'break-all' }}> <Label style={{ fontSize: '13px' }} >404</Label></Table.Cell>
                                    </Table.Row>




                                </Table.Body>
                            </Table>

                        </Segment>



                        <Segment basic style={{ textAlign: 'center', margin: 'auto' }} >
                            <div ><strong >Broken Pages </strong><Icon name='question circle' size='large'></Icon></div>
                            <Image src={BrokenPages} size='medium' style={{ margin: 'auto' }} />
                        </Segment>


                    </Segment.Group>
                </Segment.Group>
            </div>

        );
    }



}

export default brokenPagesScreen;