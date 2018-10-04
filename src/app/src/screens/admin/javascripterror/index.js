import React, { Component } from 'react';
import {Segment, Button, SegmentGroup, Table, Input, Icon, Label } from 'semantic-ui-react'
export default class Direction extends Component {

  render() {
    return (
      <div >


        <SegmentGroup vertical='true'>
          <Segment>
            <h3>Javascript Error Test</h3></Segment>


          <Segment.Group horizontal>

            <Segment basic >
              <Button style={{ marginRight: '10px' }} floated='right'><Icon name="print" />Export</Button>
              <div style={{ marginBottom: '10px' }}>
                <Input icon='search' placeholder='Search...' />
              </div>
              <Table singleLine style={{ fontSize: '14px' }}>
                <Table.Header>
                  <Table.Row>
                    <Table.HeaderCell></Table.HeaderCell>
                    <Table.HeaderCell>Error   Message</Table.HeaderCell>
                    <Table.HeaderCell>Type</Table.HeaderCell>
                    <Table.HeaderCell>Pages</Table.HeaderCell>
                    <Table.HeaderCell></Table.HeaderCell>
                  </Table.Row>
                </Table.Header>
                <Table.Body>
                  <Table.Row>
                    <Table.Cell><Icon circular inverted color='blue' name="search" /></Table.Cell>
                    <Table.Cell>Data</Table.Cell>
                    <Table.Cell> <Label style={{ fontSize: '14px' }} horizontal>
                      Log
      </Label></Table.Cell>
                    <Table.Cell> <Label style={{ fontSize: '14px' }} horizontal>
                      10
      </Label></Table.Cell>
                    <Table.Cell><Button floated='right'>Ignore</Button></Table.Cell>
                  </Table.Row>
                  <Table.Row>
                    <Table.Cell><Icon circular inverted color='blue' name="search" /></Table.Cell>
                    <Table.Cell>123</Table.Cell>
                    <Table.Cell> <Label style={{ fontSize: '14px' }} horizontal>
                      Log
      </Label></Table.Cell>
                    <Table.Cell> <Label style={{ fontSize: '14px' }} horizontal>
                      2
      </Label></Table.Cell>
                    <Table.Cell><Button floated='right'>Ignore</Button></Table.Cell>
                  </Table.Row>
                  <Table.Row>
                    <Table.Cell><Icon circular inverted color='blue' name="search" /></Table.Cell>
                    <Table.Cell>[Facebook Pixel] - Duplicate Pixel ID: 1541902979357787.</Table.Cell>
                    <Table.Cell> <Label style={{ fontSize: '14px' }} color='yellow' horizontal>
                      Warning
      </Label></Table.Cell>
                    <Table.Cell> <Label style={{ fontSize: '14px' }} horizontal>
                      2
      </Label></Table.Cell>
                    <Table.Cell><Button floated='right'>Ignore</Button></Table.Cell>
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