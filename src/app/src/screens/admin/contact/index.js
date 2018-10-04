import React, { Component } from 'react';
import {Segment, Button, SegmentGroup, Table, Icon } from 'semantic-ui-react'
export default class Contact extends Component {

  render() {
    return (
      <div >


        <SegmentGroup vertical='true'>
          <Segment><h3>Contact Test</h3></Segment>

          <Segment.Group horizontal>
            <Segment basic>
              <Segment.Group horizontal  >
                <Segment style={{ margin: 'auto', textAlign: 'center', padding: 0 }}>
                  <Icon name='phone square' size='huge' />
                </Segment>
                <Segment>
                  <p style={{ fontSize: 24 }}>2 <br />
                    Phone Numbers</p>
                </Segment>
                <Segment style={{ margin: 'auto', textAlign: 'center', padding: 0 }}>
                  <Icon name="mail" size='huge' />
                </Segment>
                <Segment>
                  <p style={{ fontSize: 24 }}>2 <br /> Emails</p>
                </Segment>
              </Segment.Group>
              <Button style={{ marginBottom: '10px' }} floated='right'><Icon name="print" />Export</Button>
              <Table singleLine>
                <Table.Header>
                  <Table.Row>
                    <Table.HeaderCell>Phone</Table.HeaderCell>
                    <Table.HeaderCell>Page affected</Table.HeaderCell>
                    <Table.HeaderCell>Action</Table.HeaderCell>

                  </Table.Row>
                </Table.Header>
                <Table.Body>
                  <Table.Row>
                    <Table.Cell>028 66 567 768</Table.Cell>
                    <Table.Cell style={{ fontSize: "24px" }}> 3 <Icon name="clone" /></Table.Cell>
                    <Table.Cell><Button>Edit</Button></Table.Cell>

                  </Table.Row>
                  <Table.Row>
                    <Table.Cell>028 66 787 768</Table.Cell>
                    <Table.Cell style={{ fontSize: "24px" }}> 2  <Icon name="clone" /></Table.Cell>
                    <Table.Cell><Button>Edit</Button></Table.Cell>
                  </Table.Row>
                </Table.Body>
              </Table>
              <Table singleLine>
                <Table.Header>
                  <Table.Row>
                    <Table.HeaderCell>Email</Table.HeaderCell>
                    <Table.HeaderCell>Page affected</Table.HeaderCell>
                    <Table.HeaderCell>Action</Table.HeaderCell>
                  </Table.Row>
                </Table.Header>
                <Table.Body>
                  <Table.Row>
                    <Table.Cell>hotro@block.vn</Table.Cell>
                    <Table.Cell style={{ fontSize: "24px" }}> 3 <Icon name="clone" /></Table.Cell>
                    <Table.Cell><Button>Edit</Button></Table.Cell>

                  </Table.Row>
                  <Table.Row>
                    <Table.Cell>hotro@block.vn</Table.Cell>
                    <Table.Cell style={{ fontSize: "24px" }}> 2 <Icon name="clone" /></Table.Cell>
                    <Table.Cell><Button>Edit</Button></Table.Cell>
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