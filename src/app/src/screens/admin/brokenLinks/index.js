import React, { Component } from 'react';
import 'semantic-ui-css/semantic.min.css';
import { Segment, Button, Table, Icon, Input } from 'semantic-ui-react'
import TableRow from './row-table';
import { Cookies } from "react-cookie";
import ReactToExcel from "react-html-table-to-excel";
import './style.css';

const cookies = new Cookies();



class brokenLinksScreen extends Component {

    state = { list: [], loadingTable: false, isDisable: false, tested: false };


    componentDidMount() {
        var comp = [];
        this.setState({ loadingTable: true });
        var param = {
            "userId": cookies.get("u_id"),
            "userToken": cookies.get("u_token"),
            "websiteId": cookies.get("u_w_id"),
            "pageOptionId": cookies.get("u_option"),
        }


        fetch("/api/brokenLink/lastest", {
            method: 'POST',
            headers: {
                'Accept': 'application/json',
                'Content-Type': 'application/json'
            },
            body: JSON.stringify(param)
        }).then(response => response.json()).then((data) => {
            comp = data.brokenLinkReport.map((item, index) => {
                return (<TableRow key={index} urlPage={item.urlPage} urlLink={item.urlLink} httpCode={item.httpCode} httpMessage={item.httpMessage} />);
            });

            this.setState({ list: comp });


            this.setState({ loadingTable: false });
        });


    }

    _doBrokenLink() {
        var comp = [];
        this.setState({ loadingTable: true, isDisable: true });
        var param = {
            "userId": cookies.get("u_id"),
            "userToken": cookies.get("u_token"),
            "websiteId": cookies.get("u_w_id"),
            "pageOptionId": cookies.get("u_option"),
        };

        fetch("/api/brokenLink", {
            method: 'POST',
            headers: {
                'Accept': 'application/json',
                'Content-Type': 'application/json'
            },
            body: JSON.stringify(param)
        }).then(response => response.json()).then((data) => {
            comp = data.brokenLinkReport.map((item, index) => {
                return (<TableRow key={index} urlPage={item.urlPage} urlLink={item.urlLink} httpCode={item.httpCode} httpMessage={item.httpMessage} />);
            });


            this.setState({ list: comp });
            if (this.state.list.length === 0) {
                this.setState({ tested: true });
            }

            this.setState({ loadingTable: false, isDisable: false });
        });

    }


    render() {
        return (
            <Segment.Group>
                <Segment basic>
                    <Button icon labelPosition='right' disabled={this.state.isDisable} onClick={() => this._doBrokenLink()}>
                        Check
                       <Icon name='right arrow' />
                    </Button>
                </Segment>
                <Segment.Group horizontal style={{ margin: 0 }}>

                    <Segment loading={this.state.loadingTable}>
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
                        <Segment basic style={{ marginBottom: '60px' }}>
                            <div style={{ float: 'right' }}>
                                <ReactToExcel
                                    className="btn1"
                                    table="table-to-xls"
                                    filename="broken_link_test_file"
                                    sheet="sheet 1"
                                    buttonText={<Button ><Icon name="print" />Export</Button>}
                                />
                            </div>

                            <Input icon='search' placeholder='Search...' style={{ float: 'right' }} />
                        </Segment>
                        <Segment style={{ maxHeight: '40vh', overflow: "auto" }}>

                            <Table singleLine unstackable textAlign='center' style={{ tableLayout: 'auto' }} id="table-to-xls">
                                <Table.Header >
                                    <Table.Row>
                                        <Table.HeaderCell>Page</Table.HeaderCell>
                                        <Table.HeaderCell>Broken link</Table.HeaderCell>
                                        <Table.HeaderCell>Issue</Table.HeaderCell>
                                        {/* <Table.HeaderCell>Action</Table.HeaderCell> */}

                                    </Table.Row>
                                </Table.Header>
                                <Table.Body>
                                    {this.state.list.length === 0 ? <Table.Row><Table.Cell>{this.state.tested ? "This page has no broken links!" : "This page haven't test yet, please try to test!"}</Table.Cell></Table.Row> : this.state.list}
                                </Table.Body>
                            </Table>
                        </Segment>
                    </Segment>




                </Segment.Group>
            </Segment.Group>

        );
    }



}

export default brokenLinksScreen;