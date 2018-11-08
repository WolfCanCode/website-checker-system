import React, { Component } from 'react';
import {Segment, Button, SegmentGroup, Icon, Table, Label, Input} from 'semantic-ui-react'
// import { Cookies } from "react-cookie";

// const cookies = new Cookies();
export default class Grammar extends Component {

    render() {
        return (
            <div>
                <SegmentGroup vertical='true'>
                    <Segment.Group horizontal>
                        <Segment basic>
                            <Segment.Group horizontal>
                                <Segment style={{ margin: 'auto', textAlign: 'center', padding: 0 }}>
                                    <Icon className="warning sign" size='huge' color='red' />
                                </Segment>
                                <Segment style={{ paddingLeft: '10px' }}>
                                    <p style={{ fontSize: 24 }}>4 <br />
                                        Potential grammar error</p>
                                </Segment >
                                <Segment style={{ margin: 'auto', textAlign: 'center', padding: 0 }}>
                                    <Icon className="file outline" size='huge' color='grey' /></Segment>
                                <Segment>
                                    <p style={{ fontSize: 24 }}>2 <br /> Affected Pages</p>
                                </Segment>

                            </Segment.Group>
                            <Button style={{ marginRight: '10px' }} floated='right'><Icon name="print" />Export</Button>
                            <div style={{ marginBottom: '10px' }}>
                                <Input icon='search' placeholder='Search...' />
                            </div>
                            <Table unstackable singleLine style={{ fontSize: '14px' }}>
                                <Table.Header>
                                    <Table.Row>
                                        <Table.HeaderCell></Table.HeaderCell>
                                        <Table.HeaderCell>Excerpt</Table.HeaderCell>
                                        <Table.HeaderCell></Table.HeaderCell>
                                        <Table.HeaderCell>Issue</Table.HeaderCell>
                                        <Table.HeaderCell></Table.HeaderCell>
                                    </Table.Row>
                                </Table.Header>
                                <Table.Body>
                                    <Table.Row>
                                        <Table.Cell><Icon circular inverted color='blue' name="search" /></Table.Cell>
                                        <Table.Cell style={{ width: '200px', whiteSpace: 'normal', wordBreak: 'break-all' }}>..s to build website/apps and they <mark>as follow</mark>,...</Table.Cell>
                                        <Table.Cell> <Icon name='plus square outline' /></Table.Cell>
                                        <Table.Cell style={{ width: '50px', whiteSpace: 'normal', wordBreak: 'break-all' }}> <Label style={{ fontSize: '14px' }} horizontal>
                                            Did you mean "as follows"?
                                            </Label></Table.Cell>
                                        <Table.Cell><Button floated='right' color='blue'>Learn Grammar</Button></Table.Cell>
                                    </Table.Row>
                                    <Table.Row>
                                        <Table.Cell><Icon circular inverted color='blue' name="search" /></Table.Cell>
                                        <Table.Cell style={{ width: '100px', whiteSpace: 'normal', wordBreak: 'break-all' }}>..s to build website/apps and they <mark>as follow</mark>,...</Table.Cell>
                                        <Table.Cell> <Icon name='plus square outline' /></Table.Cell>
                                        <Table.Cell> <Label style={{ fontSize: '14px' }} horizontal>
                                            Did you mean "as follows"?
                                        </Label></Table.Cell>
                                        <Table.Cell><Button floated='right' color='blue'>Learn Grammar</Button></Table.Cell>
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