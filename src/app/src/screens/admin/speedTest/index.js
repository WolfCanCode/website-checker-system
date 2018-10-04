import React, { Component } from 'react';
import 'semantic-ui-css/semantic.min.css';
import {Segment, Button, Table, Image, Icon, Input } from 'semantic-ui-react'
import SpeedTest from '../../../assets/desktopSpeed.png';






class speedTestScreen extends Component {

    render() {
        return (
            <div style={{ height: 'auto', marginTop: '20px' }}>
                <Segment.Group>
                    <Segment><h3>Desktop Speed Test</h3></Segment>
                    <Segment.Group horizontal>

                        <Segment basic>



                            <Segment.Group horizontal >
                                
                                <Segment style={{ paddingLeft: '10px' }}>
                                    <p style={{ fontSize: 24 }}>5.1s <br />
                                        Page interactive time</p>
                                </Segment >
                                
                                <Segment>
                                    <p style={{ fontSize: 24 }}>6.2s <br /> Page load time</p>
                                </Segment>
                                <Segment>
                                    <p style={{ fontSize: 24 }}>2.5 MB <br /> Average page size</p>
                                </Segment>
                            </Segment.Group>
                            <div style={{marginBottom : '60px', marginRight : '20px'}}>
                                <Button floated='right' ><Icon name="print" />Export</Button>

                                <Input icon='search' placeholder='Search...' style={{ float: 'right' }} />
                            </div>
                            <Table singleLine textAlign='center' style={{ tableLayout: 'auto' }}>
                                <Table.Header >
                                    <Table.Row>
                                        <Table.HeaderCell>Page</Table.HeaderCell>
                                        <Table.HeaderCell>Interactive time</Table.HeaderCell>
                                        <Table.HeaderCell>Load time</Table.HeaderCell>
                                        <Table.HeaderCell>Size</Table.HeaderCell>

                                    </Table.Row>
                                </Table.Header>
                                <Table.Body>
                                    <Table.Row>
                                        <Table.Cell style={{ width: '100px', whiteSpace: 'normal', wordBreak: 'break-all' }}><a href='https://react.semantic-ui.com'>www.react.semantic-ui.com</a></Table.Cell>
                                        <Table.Cell style={{ width: '100px', whiteSpace: 'normal', wordBreak: 'break-all' }}>5.6s</Table.Cell>
                                        <Table.Cell  style={{ width: '100px', whiteSpace: 'normal', wordBreak: 'break-all' }}>7.3s</Table.Cell>
                                        <Table.Cell  style={{ width: '100px', whiteSpace: 'normal', wordBreak: 'break-all' }}>5.3 MB</Table.Cell>
                                        

                                    </Table.Row>

                                    <Table.Row>
                                        <Table.Cell style={{ width: '100px', whiteSpace: 'normal', wordBreak: 'break-all' }}><a href='https://react.semantic-ui.com'>www.react.semantic-ui.com</a></Table.Cell>
                                        <Table.Cell style={{ width: '100px', whiteSpace: 'normal', wordBreak: 'break-all' }}>5.6s</Table.Cell>
                                        <Table.Cell  style={{ width: '100px', whiteSpace: 'normal', wordBreak: 'break-all' }}>7.3s</Table.Cell>
                                        <Table.Cell  style={{ width: '100px', whiteSpace: 'normal', wordBreak: 'break-all' }}>5.3 MB</Table.Cell>
                                        

                                    </Table.Row>

                                    <Table.Row>
                                        <Table.Cell style={{ width: '100px', whiteSpace: 'normal', wordBreak: 'break-all' }}><a href='https://react.semantic-ui.com'>www.react.semantic-ui.com</a></Table.Cell>
                                        <Table.Cell style={{ width: '100px', whiteSpace: 'normal', wordBreak: 'break-all' }}>5.6s</Table.Cell>
                                        <Table.Cell  style={{ width: '100px', whiteSpace: 'normal', wordBreak: 'break-all' }}>7.3s</Table.Cell>
                                        <Table.Cell  style={{ width: '100px', whiteSpace: 'normal', wordBreak: 'break-all' }}>5.3 MB</Table.Cell>
                                        

                                    </Table.Row>

                                    <Table.Row>
                                        <Table.Cell style={{ width: '100px', whiteSpace: 'normal', wordBreak: 'break-all' }}><a href='https://react.semantic-ui.com'>www.react.semantic-ui.com</a></Table.Cell>
                                        <Table.Cell style={{ width: '100px', whiteSpace: 'normal', wordBreak: 'break-all' }}>5.6s</Table.Cell>
                                        <Table.Cell  style={{ width: '100px', whiteSpace: 'normal', wordBreak: 'break-all' }}>7.3s</Table.Cell>
                                        <Table.Cell  style={{ width: '100px', whiteSpace: 'normal', wordBreak: 'break-all' }}>5.3 MB</Table.Cell>
                                        

                                    </Table.Row>
                                   
                                    

                                </Table.Body>
                            </Table>

                        </Segment>



                        <Segment basic style={{ textAlign: 'center', margin: 'auto' }} >
                            <div ><strong >Desktop Speed </strong><Icon name='question circle' size='large'></Icon></div>
                            <Image src={SpeedTest} size='medium' style={{ margin: 'auto' }} />
                        </Segment>


                    </Segment.Group>
                </Segment.Group>
            </div>

        );
    }



}

export default speedTestScreen;