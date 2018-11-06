import React, { Component } from 'react';
import { Segment, Button, SegmentGroup, Input, Table, TableCell, Header, Icon, Modal } from 'semantic-ui-react'

import TableRow from './row-table';
import logo1 from './images/mobile.png';

import { Cookies } from "react-cookie";

const cookies = new Cookies();

export default class managewebsitescreen extends Component {
    

    state = { open: false }

    show = size => () => this.setState({ size, open: true })
    close = () => this.setState({ open: false })


    state = { listWeb: null };


    componentDidMount() {

        fetch("/api/website/all", {
            method: 'POST',
            headers: {
                'Accept': 'application/json',
                'Content-Type': 'application/json'
            },
            body: JSON.stringify({ "id": cookies.get("u_id") })
        }).then(response => response.json()).then((data) => {
            if (data.action === "SUCCESS") {
                var list = data.website.map((item, index) => {
                    return (<TableRow key={index} id={item.id} name={item.name} url={item.url} />);
                    // { key: index, value: item.id, text: item.url };

                });
                this.setState({ listWeb: list });

            }
        });


    }

    render() {
        const { open, size } = this.state
        return (
            <div>

                <SegmentGroup vertical='true'>

                    <Segment basic>
                        <div style={{ marginBottom: '30px' }}>

                            <Button style={{ float: 'right' }} onClick={this.show('mini')}> Add </Button>
                            <Modal size={size} open={open} onClose={this.close}>
                                <Modal.Header>Add Website</Modal.Header>
                                <Modal.Content >
                                    <p >Website Name</p>
                                </Modal.Content>
                                <Input type="text" style={{marginLeft : '20px', width : '90%'}} required></Input>
                                <Modal.Content>
                                    <p>Website URL</p>
                                </Modal.Content>
                                <Input type="text" style={{marginLeft : '20px', marginBottom : '20px', width : '90%'}}></Input>
                                <Modal.Actions>
                                    <Button onClick={this.close}>Cancel</Button>
                                    <Button content='Done' color='blue' />
                                </Modal.Actions>
                            </Modal>

                        </div>
                    </Segment>
                    <Segment.Group horizontal style={{ maxHeight: '63vh', overflow: "auto" }}>
                        <Segment basic>
                            <Table singleLine>
                                <Table.Header>
                                    <Table.Row>
                                        <Table.HeaderCell>ID</Table.HeaderCell>
                                        <Table.HeaderCell>Name</Table.HeaderCell>
                                        <Table.HeaderCell>URL</Table.HeaderCell>
                                        <Table.HeaderCell>Action</Table.HeaderCell>

                                    </Table.Row>
                                </Table.Header>
                                <Table.Body>
                                    {/* <Table.Row>
                                   <TableCell>1</TableCell>
                                   <TableCell>Genk </TableCell>
                                   <TableCell>http://genk.vn/</TableCell>
                                   <TableCell><Button> Edit </Button><Button> Delete</Button></TableCell>
                               </Table.Row>

                               <Table.Row>
                                   <TableCell>2</TableCell>
                                   <TableCell>Gamek </TableCell>
                                   <TableCell>http://gamek.vn/</TableCell>
                                   <TableCell><Button> Edit </Button><Button> Delete</Button></TableCell>
                               </Table.Row> */}
                                    {this.state.listWeb}

                                </Table.Body>
                            </Table>
                        </Segment>

                    </Segment.Group>

                    {/* <div>


                        <div class='ui medium images'>

                            <img src={require('./images/' + 'mobile.png')} class='ui image' />
                            <img src={logo1} class='ui image' />
                            <img src={logo1} class='ui image' />


                        </div>
                    </div> */}

                </SegmentGroup>
            </div>
        );
    }
}