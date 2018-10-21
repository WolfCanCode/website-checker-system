import React, { Component } from 'react';
import 'semantic-ui-css/semantic.min.css';
import {Segment, Button, Table, Icon, Input, Label } from 'semantic-ui-react'






class missingFilesScreen extends Component {

    render() {
        return (
            <div style={{ height: 'auto'}}>
                <Segment.Group>
                    
                    <Segment.Group horizontal>

                        <Segment basic>



                            <Segment.Group horizontal >
                                <Segment style={{ margin: 'auto', textAlign: 'center', padding: 0 }}>
                                    <Icon className="warning sign" size='huge' color='red' />
                                </Segment>
                                <Segment style={{ paddingLeft: '10px' }}>
                                    <p style={{ fontSize: 24 }}>4 <br />
                                        Missing Files</p>
                                </Segment >
                                <Segment style={{ margin: 'auto', textAlign: 'center', padding: 0 }}>
                                    <Icon className="file outline" size='huge' color='grey' /></Segment>
                                <Segment>
                                    <p style={{ fontSize: 24 }}>2 <br /> Affected Pages</p>
                                </Segment>
                            </Segment.Group>
                            {/* <Segment.Group  horizontal basic>
                                <Segment  basic style={{ float:'left', width:'70%'}}>
                                <div attached style={{ float:'left', width:'50%', marginTop:'5%'}}>
                                    <h2>Don't go missing</h2>
                                    <Divider />
                                    <p style={{color:'grey'}}>A missing file is part of a webpage that has gone missing, such as an image or css.</p>

                                   <p style={{color:'grey'}}>Missing files often cause pages to appear broken, and should be fixed.</p>

                                </div>
                                
                                
                                </Segment>
                                <Segment basic style={{ textAlign: 'center', margin: 'auto',width:'30%' } }>
                                <strong >Missing Files </strong><Icon name='question circle' size='large'></Icon>
                                <Image src={MissingFiles} size='medium' style={{ margin:'auto' }} />
                                </Segment>

                            </Segment.Group> */}
                            {/* <Segment basic style={{ textAlign: 'center', margin: 'auto' } } horizontal >
                            <div style={{ float:'left' }}></div>
                            <div style={{ float:'right', marginRight:'0' }}>
                            <div style={{ margin:'auto' }} ></div>
                          
                            </div>
                           
                        </Segment> */}
                        
                        <Segment basic>
                            <div style={{marginBottom:'50px'}}>
                                <Button  style={{ float: 'right'}}><Icon name="print" />Export</Button>

                                <Input icon='search' placeholder='Search...' style={{ float: 'right' }} />
                            </div>
                        </Segment>
                            
                            <Table singleLine textAlign='center' style={{ tableLayout: 'auto' }}>
                                <Table.Header >
                                    <Table.Row>
                                        <Table.HeaderCell>Files</Table.HeaderCell>
                                        <Table.HeaderCell>Description</Table.HeaderCell>
                                        <Table.HeaderCell>Pages</Table.HeaderCell>
                                        <Table.HeaderCell>Action</Table.HeaderCell>

                                    </Table.Row>
                                </Table.Header>
                                <Table.Body>
                                    <Table.Row>
                                        <Table.Cell style={{ width: '100px', whiteSpace: 'normal', wordBreak: 'break-all' }}><a href='https://api2.insites.com/images/missing-files1.png'>https://api2.insites.com/images/missing-files1.png</a></Table.Cell>
                                        <Table.Cell style={{ width: '100px', whiteSpace: 'normal', wordBreak: 'break-all' }}>File Not Found (404)</Table.Cell>
                                        <Table.Cell style={{ width: '100px', whiteSpace: 'normal', wordBreak: 'break-all' }}> <Label style={{fontSize:'13px'}} >1</Label></Table.Cell>                                   
                                        <Table.Cell style={{ width: '100px', whiteSpace: 'normal', wordBreak: 'break-all' }}><Button>Ignore</Button> </Table.Cell>

                                    </Table.Row>
                                    <Table.Row>
                                        <Table.Cell style={{ width: '100px', whiteSpace: 'normal', wordBreak: 'break-all' }}><a href='https://api2.insites.com/images/missing-files1.png'>https://api2.insites.com/images/missing-files1.png</a></Table.Cell>
                                        <Table.Cell style={{ width: '100px', whiteSpace: 'normal', wordBreak: 'break-all' }}>File Not Found (404)</Table.Cell>
                                        <Table.Cell style={{ width: '100px', whiteSpace: 'normal', wordBreak: 'break-all' }}> <Label style={{fontSize:'13px'}} >1</Label></Table.Cell>                                   
                                        <Table.Cell style={{ width: '100px', whiteSpace: 'normal', wordBreak: 'break-all' }}><Button>Ignore</Button> </Table.Cell>

                                    </Table.Row>
                                    <Table.Row>
                                        <Table.Cell style={{ width: '100px', whiteSpace: 'normal', wordBreak: 'break-all' }}><a href='https://api2.insites.com/images/missing-files1.png'>https://api2.insites.com/images/missing-files1.png</a></Table.Cell>
                                        <Table.Cell style={{ width: '100px', whiteSpace: 'normal', wordBreak: 'break-all' }}>File Not Found (404)</Table.Cell>
                                        <Table.Cell style={{ width: '100px', whiteSpace: 'normal', wordBreak: 'break-all' }}> <Label style={{fontSize:'13px'}} >1</Label></Table.Cell>                                   
                                        <Table.Cell style={{ width: '100px', whiteSpace: 'normal', wordBreak: 'break-all' }}><Button>Ignore</Button> </Table.Cell>

                                    </Table.Row>
                                    <Table.Row>
                                        <Table.Cell style={{ width: '100px', whiteSpace: 'normal', wordBreak: 'break-all' }}><a href='https://api2.insites.com/images/missing-files1.png'>https://api2.insites.com/images/missing-files1.png</a></Table.Cell>
                                        <Table.Cell style={{ width: '100px', whiteSpace: 'normal', wordBreak: 'break-all' }}>File Not Found (404)</Table.Cell>
                                        <Table.Cell style={{ width: '100px', whiteSpace: 'normal', wordBreak: 'break-all' }}> <Label style={{fontSize:'13px'}} >1</Label></Table.Cell>                                   
                                        <Table.Cell style={{ width: '100px', whiteSpace: 'normal', wordBreak: 'break-all' }}><Button>Ignore</Button> </Table.Cell>

                                    </Table.Row>
                                   
                                   
                                    

                                </Table.Body>
                            </Table>

                        </Segment>



                        


                    </Segment.Group>
                </Segment.Group>
            </div>

        );
    }



}

export default missingFilesScreen;