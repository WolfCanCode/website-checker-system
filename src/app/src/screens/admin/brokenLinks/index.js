import React, { Component } from 'react';
import 'semantic-ui-css/semantic.min.css';
import { Segment, Button, Table, Image, Icon, Input } from 'semantic-ui-react'
import BrokenLinks from '../../../assets/BrokenLinks.png';






class brokenLinksScreen extends Component {

    render() {
        return (
            <div style={{ height: 'auto', marginTop: '20px' }}>

                <Segment.Group>
                    <Segment><h3>Broken Links test</h3></Segment>
                    <Segment.Group horizontal>
                        <Segment basic>
                            <Segment.Group horizontal >
                                <Segment style={{ margin: 'auto', textAlign: 'center', padding: 0 }}>
                                    <Icon className="check" size='huge' color='green' />
                                </Segment>
                                <Segment style={{ paddingLeft: '10px' }}>
                                    <p style={{ fontSize: 24 }}>0 <br />
                                        Internal broken links</p>
                                </Segment >
                                <Segment style={{ margin: 'auto', textAlign: 'center', padding: 0 }}>
                                    <Icon className="broken chain" size='huge' color='red' /></Segment>
                                <Segment>
                                    <p style={{ fontSize: 24 }}>2 <br /> External broken links</p>
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
                                        <Table.HeaderCell>Broken link</Table.HeaderCell>
                                        <Table.HeaderCell>Issue</Table.HeaderCell>
                                        <Table.HeaderCell>Action</Table.HeaderCell>

                                    </Table.Row>
                                </Table.Header>
                                <Table.Body>
                                    <Table.Row>
                                        <Table.Cell style={{ width: '100px', whiteSpace: 'normal', wordBreak: 'break-all' }}><a href='https://react.semantic-ui.com'>www.react.semantic-ui.com</a></Table.Cell>
                                        <Table.Cell style={{ width: '100px', whiteSpace: 'normal', wordBreak: 'break-all' }}><a href='https://react.semantic-ui.com/Contact'>www.react.semantic-ui.com/Contact</a></Table.Cell>
                                        <Table.Cell textAlign='left' style={{ width: '100px', whiteSpace: 'normal', wordBreak: 'break-all' }}>www.react.semantic-ui.com/Contact does not exist</Table.Cell>
                                        <Table.Cell style={{ width: '100px', whiteSpace: 'normal', wordBreak: 'break-all' }}><Button>Ignore</Button> </Table.Cell>

                                    </Table.Row>
                                    <Table.Row>
                                        <Table.Cell style={{ width: '100px', whiteSpace: 'normal', wordBreak: 'break-all' }}><a href='https://react.semantic-ui.com'>www.react.semantic-ui.com</a></Table.Cell>
                                        <Table.Cell style={{ width: '100px', whiteSpace: 'normal', wordBreak: 'break-all' }}><a href='https://react.semantic-ui.com/Contact'>www.react.semantic-ui.com/Contact</a></Table.Cell>
                                        <Table.Cell textAlign='left' style={{ width: '100px', whiteSpace: 'normal', wordBreak: 'break-all' }}>www.react.semantic-ui.com/Contact does not exist</Table.Cell>
                                        <Table.Cell style={{ width: '100px', whiteSpace: 'normal', wordBreak: 'break-all' }}><Button>Ignore</Button> </Table.Cell>

                                    </Table.Row>
                                    <Table.Row>
                                        <Table.Cell style={{ width: '100px', whiteSpace: 'normal', wordBreak: 'break-all' }}><a href='https://react.semantic-ui.com'>www.react.semantic-ui.com</a></Table.Cell>
                                        <Table.Cell style={{ width: '100px', whiteSpace: 'normal', wordBreak: 'break-all' }}><a href='https://react.semantic-ui.com/Contact'>www.react.semantic-ui.com/Contact</a></Table.Cell>
                                        <Table.Cell textAlign='left' style={{ width: '100px', whiteSpace: 'normal', wordBreak: 'break-all' }}>www.react.semantic-ui.com/Contact does not exist</Table.Cell>
                                        <Table.Cell style={{ width: '100px', whiteSpace: 'normal', wordBreak: 'break-all' }}><Button>Ignore</Button> </Table.Cell>

                                    </Table.Row>
                                    <Table.Row>
                                        <Table.Cell style={{ width: '100px', whiteSpace: 'normal', wordBreak: 'break-all' }}><a href='https://react.semantic-ui.com'>www.react.semantic-ui.com</a></Table.Cell>
                                        <Table.Cell style={{ width: '100px', whiteSpace: 'normal', wordBreak: 'break-all' }}><a href='https://react.semantic-ui.com/Contact'>www.react.semantic-ui.com/Contact</a></Table.Cell>
                                        <Table.Cell textAlign='left' style={{ width: '100px', whiteSpace: 'normal', wordBreak: 'break-all' }}>www.react.semantic-ui.com/Contact does not exist</Table.Cell>
                                        <Table.Cell style={{ width: '100px', whiteSpace: 'normal', wordBreak: 'break-all' }}><Button>Ignore</Button> </Table.Cell>

                                    </Table.Row>


                                </Table.Body>
                            </Table>

                        </Segment>



                        <Segment basic style={{ textAlign: 'center', margin: 'auto' }} >
                            <div ><strong >Broken Links </strong><Icon name='question circle' size='large'></Icon></div>
                            <Image src={BrokenLinks} size='medium' style={{ margin: 'auto' }} />
                        </Segment>


                    </Segment.Group>
                </Segment.Group>
            </div>

        );
    }



}

export default brokenLinksScreen;