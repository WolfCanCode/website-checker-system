import React, { Component } from 'react';
import {Segment, Button, SegmentGroup, Table, Input, Icon } from 'semantic-ui-react'
export default class Pages extends Component {

    render() {
        return (
            <div >
                <SegmentGroup vertical='true'>
                    <Segment><h3>Pages Test</h3></Segment>
                    <Segment.Group horizontal>
                        <Segment basic>
                            <div style={{ float: 'right', marginBottom: '10px' }}>
                                <Input icon='search' placeholder='Search...' />
                            </div>


                            <Button style={{ marginRight: '10px' }} floated='right'><Icon name="print" />Export</Button>

                            <Table singleLine>
                                <Table.Header>
                                    <Table.Row>
                                        <Table.HeaderCell>Title</Table.HeaderCell>
                                        <Table.HeaderCell>Web Address</Table.HeaderCell>
                                        <Table.HeaderCell>Canonical URL</Table.HeaderCell>
                                        <Table.HeaderCell>HTTP</Table.HeaderCell>
                                        <Table.HeaderCell>Last checked</Table.HeaderCell>
                                    </Table.Row>
                                </Table.Header>
                                <Table.Body>
                                    <Table.Row>
                                        <Table.Cell>Block - Nền tảng học trực tuyến</Table.Cell>
                                        <Table.Cell>https://block.vn </Table.Cell>
                                        <Table.Cell>https://block.vn</Table.Cell>
                                        <Table.Cell><Button>200</Button></Table.Cell>
                                        <Table.Cell> 1 day ago</Table.Cell>
                                    </Table.Row>

                                </Table.Body>
                            </Table>


                        </Segment>
                        {/* <Segment basic>
                            
                        </Segment> */}
                    </Segment.Group>
                </SegmentGroup>
            </div>
        );
    }
}