import React, { Component } from 'react';
import 'semantic-ui-css/semantic.min.css';
import {Segment, Button, Image, Icon, Input,  } from 'semantic-ui-react'

import image from '../../../assets/mobile.png';









class mobileLayoutScreen extends Component {

    render() {
        return (
            <div style={{ height: 'auto', marginTop: '20px' }}>
                <Segment.Group>
                    <Segment><h3>Mobile Layout test</h3></Segment>
                    <Segment.Group horizontal >

                        <Segment style={{ margin: 'auto', textAlign: 'center', padding: 0 }}>
                            <Icon className="check" size='huge' color='green' />
                        </Segment>
                        <Segment style={{ paddingLeft: '10px' }}>
                            <p style={{ fontSize: 24 }}>0 <br />
                                Pages not designed for mobile</p>
                        </Segment >

                        <Segment style={{ margin: 'auto', textAlign: 'center', padding: 0, size: 'mini', }}>
                            <Icon className="mobile alternate" size='huge' color='yellow' /></Segment>
                        <Segment>
                            <p style={{ fontSize: 24 }}>3 <br /> Mobile pages with issues</p>
                        </Segment>

                        <Segment style={{ margin: 'auto', textAlign: 'center', padding: 0, size: 'mini' }}>
                            <Icon className="mobile alternate" size='huge' color='blue' /></Segment>
                        <Segment>
                            <p style={{ fontSize: 24 }}>17 <br /> Mobile Optimized Pages</p>
                        </Segment>

                        

                        

                    </Segment.Group>
                    <Segment.Group horizontal>

                        


                        <Segment basic>
                            <h4>Pages With Layout Issues</h4>

                            <div style={{ marginBottom: '60px', marginRight: '180px' }}>
                                <Button floated='right' ><Icon name="print" />Export</Button>

                                <Input icon='search' placeholder='Search...' style={{ float: 'right' }} />
                            </div>
                            <div style={{ display: 'inline-flex', textAlign: 'center' }}>
                                <div style={{ marginLeft: '80px', marginTop: '10px' }}>
                                    <div style={{ marginBottom: '10px', fontSize: '20px' }}>Browse Top Albums, MP3 Songs</div>
                                    <div><Image src={image} size='medium' /></div>

                                </div>
                                <div style={{ marginLeft: '80px', marginTop: '10px' }}>
                                    <div style={{ marginBottom: '10px', fontSize: '20px' }}>Browse Top Albums, MP3 Songs</div>
                                    <div><Image src={image} size='medium' /></div>

                                </div>

                                <div style={{ marginLeft: '80px', marginTop: '10px' }}>
                                    <div style={{ marginBottom: '10px', fontSize: '20px' }}>Browse Top Albums, MP3 Songs</div>
                                    <div><Image src={image} size='medium' /></div>

                                </div>

                                <div style={{ marginLeft: '80px', marginTop: '10px' }}>
                                    <div style={{ marginBottom: '10px', fontSize: '20px' }}>Browse Top Albums, MP3 Songs</div>
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

export default mobileLayoutScreen;