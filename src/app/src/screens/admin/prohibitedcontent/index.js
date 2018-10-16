import React, { Component } from 'react';
import { Segment, Button, SegmentGroup, Icon, Image, Table, Label, Input} from 'semantic-ui-react'
import ProhibitedFile from '../../../assets/prohibited.PNG';
export default class ProhibitedContent extends Component {

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
                                    <p style={{ fontSize: 24 }}>2 <br />
                                        Pieces of prohibited content</p>
                                </Segment >
                                <Segment style={{ margin: 'auto', textAlign: 'center', padding: 0 }}>
                                    <Icon className="file outline" size='huge' color='grey' /></Segment>
                                <Segment>
                                    <p style={{ fontSize: 24 }}>15 <br /> Affected Pages</p>
                                </Segment>

                            </Segment.Group>
                            <Button style={{ marginRight: '10px' }} floated='right'><Icon name="print" />Export</Button>
                            <div style={{ marginBottom: '10px' }}>
                                <Input icon='search' placeholder='Search...' />
                            </div>
                            <Table singleLine style={{ fontSize: '14px' }}>
                                <Table.Header>
                                    <Table.Row>
                                        <Table.HeaderCell></Table.HeaderCell>
                                        <Table.HeaderCell>Text</Table.HeaderCell>
                                        <Table.HeaderCell>Type</Table.HeaderCell>
                                        <Table.HeaderCell>Pages</Table.HeaderCell>
                                        <Table.HeaderCell></Table.HeaderCell>
                                    </Table.Row>
                                </Table.Header>
                                <Table.Body>
                                    <Table.Row>
                                        <Table.Cell><Icon circular inverted color='blue' name="search" /></Table.Cell>
                                        <Table.Cell style={{ width: '200px', whiteSpace: 'normal', wordBreak: 'break-all' }}>Wine</Table.Cell>
                                        <Table.Cell>Custom</Table.Cell>
                                        <Table.Cell style={{ width: '50px', whiteSpace: 'normal', wordBreak: 'break-all' }}>   <Label circular color='grey'>
        2
      </Label></Table.Cell>
                                        <Table.Cell><Button floated='right' >Ignore</Button></Table.Cell>
                                    </Table.Row>
                                    <Table.Row>
                                        <Table.Cell><Icon circular inverted color='blue' name="search" /></Table.Cell>
                                        <Table.Cell style={{ width: '200px', whiteSpace: 'normal', wordBreak: 'break-all' }}>Crappy</Table.Cell>
                                        <Table.Cell>Potentially offensive</Table.Cell>
                                        <Table.Cell style={{ width: '50px', whiteSpace: 'normal', wordBreak: 'break-all' }}>   <Label circular color='grey'>
        13
      </Label></Table.Cell>
                                        <Table.Cell><Button floated='right' >Ignore</Button></Table.Cell>
                                    </Table.Row>

                                </Table.Body>
                            </Table>
                        </Segment>
                        <Segment basic style={{ textAlign: 'center', margin: 'auto' }} >
                            <div ><strong >Prohibited Content </strong><Icon name='question circle' size='large'></Icon></div>
                            <Image src={ProhibitedFile} size='medium' style={{ margin: 'auto' }} />
                        </Segment>
                    </Segment.Group>
                </SegmentGroup>
            </div>
        );
    }
}