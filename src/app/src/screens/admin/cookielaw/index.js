import React, { Component } from 'react';
import {Segment, Button, SegmentGroup, Icon, Table, Label, Input} from 'semantic-ui-react'

export default class CookieLaw extends Component {

    render() {
        return (
            <div>

                <SegmentGroup vertical='true'>
                    <Segment><h3>Cookie used by this website</h3></Segment>
                    <Segment.Group horizontal>
                        <Segment basic>
                            <Segment.Group horizontal>
                                <Segment style={{ margin: 'auto', textAlign: 'center', padding: 0 }}>
                                    <Icon className="warning sign" size='huge' color='yellow' />
                                </Segment>
                                <Segment style={{ paddingLeft: '10px' }}>
                                    <p style={{ fontSize: 24 }}>2,154 <br />
                                        Pages setting cookie</p>
                                </Segment >
                                <Segment style={{ margin: 'auto', textAlign: 'center', padding: 0 }}>
                                    <Icon className="file outline" size='huge' color='teal' /></Segment>
                                <Segment>
                                    <p style={{ fontSize: 24 }}>0<br /> Pages not explaining they use cookie</p>
                                </Segment>

                            </Segment.Group>
                            <Button style={{ marginRight: '10px' }} floated='right'><Icon name="print" />Export</Button>
                            <div style={{ marginBottom: '10px' }}>
                                <Input icon='search' placeholder='Search...' />
                            </div>
                            <Table singleLine style={{ fontSize: '14px' }}>
                                <Table.Header>
                                    <Table.Row>
                                        <Table.HeaderCell>Technology</Table.HeaderCell>
                                        <Table.HeaderCell>Description</Table.HeaderCell>
                                        <Table.HeaderCell>Cookie used</Table.HeaderCell>
                                        <Table.HeaderCell>Pages</Table.HeaderCell>
                                        <Table.HeaderCell>%</Table.HeaderCell>
                                    </Table.Row>
                                </Table.Header>
                                <Table.Body>
                                    <Table.Row>
                                        <Table.Cell style={{ width: '100px', whiteSpace: 'normal', wordBreak: 'break-all' }}>unkown</Table.Cell>
                                        <Table.Cell style={{ width: '300px', whiteSpace: 'normal', wordBreak: 'break-all' }}>The purpose of these cookie   is unknown</Table.Cell>
                                        <Table.Cell style={{ width: '500px', whiteSpace: 'normal', wordBreak: 'break-all' }}><Label circular color='yellow'>_gid</Label><Label circular color='yellow'>sessionId</Label>
                                        <Label circular color='yellow'>visitorId</Label> <Label circular color='yellow'>nssr</Label> <Label circular color='yellow'>__stripe_sid</Label><Label circular color='yellow'>__stripe_mind</Label><Label circular color='yellow'>pxrc</Label>
                                        <Label circular color='yellow'>nnls</Label> <Label circular color='yellow'>didts</Label> <Label circular color='yellow'>NID</Label>
                                        </Table.Cell>
                                        <Table.Cell style={{ width: '100px', whiteSpace: 'normal', wordBreak: 'break-all' }}> 2,154  </Table.Cell>
                                        <Table.Cell style={{ width: '100px', whiteSpace: 'normal', wordBreak: 'break-all' }}>100%</Table.Cell>
                                    </Table.Row>
                                    <Table.Row>
                                        <Table.Cell style={{ width: '100px', whiteSpace: 'normal', wordBreak: 'break-all' }}>Google Analytics</Table.Cell>
                                        <Table.Cell style={{ width: '100px', whiteSpace: 'normal', wordBreak: 'break-all' }}>The world most popular analytics tool.</Table.Cell>
                                        <Table.Cell style={{ width: '100px', whiteSpace: 'normal', wordBreak: 'break-all' }}><Label circular color='yellow'>_gat</Label><Label circular color='yellow'>ga</Label>
                                        <Label circular color='yellow'>__utmb</Label> <Label circular color='yellow'>__utmc</Label> <Label circular color='yellow'>_utmz</Label><Label circular color='grey'>+2</Label>
                                        </Table.Cell>
                                        <Table.Cell style={{ width: '100px', whiteSpace: 'normal', wordBreak: 'break-all' }}> 2,154  </Table.Cell>
                                        <Table.Cell style={{ width: '50px', whiteSpace: 'normal', wordBreak: 'break-all' }}>100%</Table.Cell>
                                    </Table.Row>

                                </Table.Body>
                            </Table>
                        </Segment>
                        
                    </Segment.Group>
                </SegmentGroup>
            </div>
        );
    }
}