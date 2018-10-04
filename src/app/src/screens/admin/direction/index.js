import React, { Component } from 'react';
import {Segment, Button, SegmentGroup, Table, Input, Icon } from 'semantic-ui-react'
export default class Direction extends Component {

    render() {
        return (
            <div >
                <SegmentGroup vertical='true'>
                    <Segment>
                        <h3>Redirection Test</h3></Segment>
                    <Segment.Group horizontal>
                        <Segment basic >
                            <div style={{ float: 'right', marginBottom: '10px' }}>
                                <Input icon='search' placeholder='Search...' />
                            </div>
                            <Button style={{ marginRight: '10px' }} floated='right'><Icon name="print" />Export</Button>
                            <Table singleLine>
                                <Table.Header>
                                    <Table.Row>
                                        <Table.HeaderCell>Web Address</Table.HeaderCell>
                                        <Table.HeaderCell>Directs to</Table.HeaderCell>
                                        <Table.HeaderCell>Type</Table.HeaderCell>
                                        <Table.HeaderCell>Type</Table.HeaderCell>
                                    </Table.Row>
                                </Table.Header>
                                <Table.Body>
                                    <Table.Row>
                                        <Table.Cell>https://www.block.vn/</Table.Cell>
                                        <Table.Cell style={{ fontSize: "24px" }}>https://block.vn </Table.Cell>
                                        <Table.Cell>Permanent</Table.Cell>
                                        <Table.Cell><Button>200</Button></Table.Cell>
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