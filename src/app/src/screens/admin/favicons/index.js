import React, { Component } from 'react';
import 'semantic-ui-css/semantic.min.css';
import {Segment, Button, Table, Image, Icon, Input, Label } from 'semantic-ui-react'

import image from '../../../assets/image.jpg';

import icon from '../../../assets/icon.png';







class faviconScreen extends Component {

    render() {
        return (
            <div style={{ height: 'auto', marginTop: '20px' }}>
                <Segment.Group>
                    <Segment><h3>Favicons test</h3></Segment>
                    <Segment.Group horizontal >
                        <Segment style={{ margin: 'auto', textAlign: 'center', padding: 0, size: 'mini' }}>
                            <Icon className="star" size='huge' color='violet' /></Segment>
                        <Segment>
                            <p style={{ fontSize: 24 }}>1 <br /> Different Favicons</p>
                        </Segment>

                        <Segment style={{ margin: 'auto', textAlign: 'center', padding: 0 }}>
                            <Icon className="check" size='huge' color='green' />
                        </Segment>
                        <Segment style={{ paddingLeft: '10px' }}>
                            <p style={{ fontSize: 24 }}>0 <br />
                                Pages missing Favicons</p>
                        </Segment >

                        {/* <Segment basic style={{ textAlign: 'center', margin: 'auto' }} >
                            <div ><strong >Broken Links </strong><Icon name='question circle' size='large'></Icon></div>
                            <Image src={FaviconsLink} size='medium' style={{ margin: 'auto' }} />
                        </Segment> */}

                    </Segment.Group>
                    <Segment.Group horizontal>

                        <Segment basic>
                            <h4>All Favicons</h4>
                            <div style={{ marginBottom: '60px', marginRight: '10px' }}>
                                <Button floated='right' ><Icon name="print" />Export</Button>

                                <Input icon='search' placeholder='Search...' style={{ float: 'right' }} />
                            </div>
                            <Table singleLine textAlign='center' style={{ tableLayout: 'auto' }}>
                                <Table.Header >
                                    <Table.Row>
                                        <Table.HeaderCell>Favicon</Table.HeaderCell>
                                        <Table.HeaderCell>URL</Table.HeaderCell>
                                        <Table.HeaderCell>Width</Table.HeaderCell>
                                        <Table.HeaderCell>Pages</Table.HeaderCell>

                                    </Table.Row>
                                </Table.Header>
                                <Table.Body>
                                    <Table.Row>
                                        <Table.Cell style={{ width: '100px', whiteSpace: 'normal', wordBreak: 'break-all' }}><Image src={icon} size='tiny' style={{ margin: 'auto' }} /></Table.Cell>
                                        <Table.Cell style={{ width: '100px', whiteSpace: 'normal', wordBreak: 'break-all' }}><a href='https://react.semantic-ui.com/Contact'>www.react.semantic-ui.com/Contact</a></Table.Cell>
                                        <Table.Cell style={{ width: '100px', whiteSpace: 'normal', wordBreak: 'break-all' }}>0</Table.Cell>
                                        <Table.Cell style={{ width: '100px', whiteSpace: 'normal', wordBreak: 'break-all' }}> <Label style={{ fontSize: '13px' }} >5</Label></Table.Cell>


                                    </Table.Row>



                                </Table.Body>
                            </Table>

                        </Segment>


                        <Segment basic>
                            <h4>Pages With Favicons</h4>

                            <div style={{ marginBottom: '60px', marginRight: '20px' }}>
                                <Button floated='right' ><Icon name="print" />Export</Button>

                                <Input icon='search' placeholder='Search...' style={{ float: 'right' }} />
                            </div>
                            <div style={{ display: 'inline-flex', textAlign: 'center' }}>
                                <div style={{ marginLeft: '80px', marginTop: '10px' }}>
                                    <div style={{ marginBottom: '10px', fontSize: '20px' }}>Browse Top Albums, MP3 Songs, Latest Playlists on Gaana.</div>
                                    <div><Image src={image} size='medium' /></div>

                                </div>
                                <div style={{ marginLeft: '80px', marginTop: '10px' }}>
                                    <div style={{ marginBottom: '10px', fontSize: '20px' }}>Browse Top Albums, MP3 Songs, Latest Playlists on Gaana.</div>
                                    <div><Image src={image} size='medium' /></div>

                                </div>

                                <div style={{ marginLeft: '80px', marginTop: '10px' }}>
                                    <div style={{ marginBottom: '10px', fontSize: '20px' }}>Browse Top Albums, MP3 Songs, Latest Playlists on Gaana.</div>
                                    <div><Image src={image} size='medium' /></div>

                                </div>
                            </div>


                        </Segment>






                    </Segment.Group>
                </Segment.Group>
            </div>

        );
    }



}

export default faviconScreen;