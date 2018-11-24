import React, { Component } from 'react';
import { Segment, Button, Table, Input, Icon } from 'semantic-ui-react'
import TableRow from './row-table';
import { Cookies } from "react-cookie";
import ReactToExcel from "react-html-table-to-excel";

const cookies = new Cookies();
export default class Direction extends Component {
    state = { list: [], loadingTable: false, isDisable: false, statusNoResult: "" };


    componentDidMount() {
        var comp = [];
        this.setState({ loadingTable: true });
        var statusNotFound = "";
        var param = {
            "userId": cookies.get("u_id"),
            "userToken": cookies.get("u_token"),
            "websiteId": cookies.get("u_w_id"),
            "pageOptionId": cookies.get("u_option"),
        }

        fetch("/api/redirectiontest/lastest", {
            method: 'POST',
            headers: {
                'Accept': 'application/json',
                'Content-Type': 'application/json'
            },
            body: JSON.stringify(param)
        }).then(response => response.json()).then((data) => {
            comp = data.redirectiontestReport.map((item, index) => {
                return (<TableRow key={index} webAddress={item.url} redirectUrl={item.driectToUrl} type={item.type} httpcode={item.httpCode} />);
            });
            if (comp.length === 0) {
                statusNotFound = "This page haven't test yet, please try to test";
            }
            this.setState({ statusNoResult: statusNotFound });
            this.setState({ list: comp });
            this.setState({ loadingTable: false });
        });


    }
    _doLinkRedirection() {
        this.setState({ loadingTable: true, isDisable: true });
        var comp = [];
        var statusNotFound = "";
        var param = {
            "userId": cookies.get("u_id"),
            "userToken": cookies.get("u_token"),
            "websiteId": cookies.get("u_w_id"),
            "pageOptionId": cookies.get("u_option"),
        }
        fetch("/api/redirectiontest", {
            method: 'POST',
            headers: {
                'Accept': 'application/json',
                'Content-Type': 'application/json'
            },
            body: JSON.stringify(param)
        }).then(response => response.json()).then((data) => {
            comp = data.redirectiontestReport.map((item, index) => {
                return (<TableRow key={index} webAddress={item.url} redirectUrl={item.driectToUrl} type={item.type} httpcode={item.httpCode} />);
            });
            if (comp.length === 0) {
                statusNotFound = "No Redirection Found";
            }
            this.setState({ statusNoResult: statusNotFound });
            this.setState({ list: comp });
            this.setState({ loadingTable: false });
        });
    }
    render() {
        return (

            <Segment.Group horizontal style={{ margin: 0 }}>

                <Segment basic loading={this.state.loadingTable} >
                    <Button icon primary labelPosition='right' disabled={this.state.isDisable} onClick={() => this._doLinkRedirection()}>
                        Check
                       <Icon name='right arrow' />
                    </Button>
                    <div style={{ marginBottom: '10px', float: 'right' }}>


                        <ReactToExcel
                            className="btn1"
                            table="table-to-xls"
                            filename="directions_test_file"
                            sheet="sheet 1"
                            buttonText={<Button color="green" ><Icon name="print" />Export</Button>}
                        />
                    </div>
                    <div style={{ float: 'right', marginBottom: '10px' }}>
                        <Input icon='search' placeholder='Search...' />
                    </div>
                    
                    <Table singleLine unstackable id="table-to-xls">
                        <Table.Header>
                            <Table.Row>
                                <Table.HeaderCell>Web Address</Table.HeaderCell>
                                <Table.HeaderCell>Directs to</Table.HeaderCell>
                                <Table.HeaderCell>Type</Table.HeaderCell>
                                <Table.HeaderCell>Code</Table.HeaderCell>
                            </Table.Row>
                        </Table.Header>
                        <Table.Body>
                            {this.state.list.length === 0 ? <Table.Row><Table.Cell>{this.state.statusNoResult}</Table.Cell></Table.Row> : this.state.list}

                        </Table.Body>
                    </Table>


                </Segment>
                {/* <Segment basic>
                            
                        </Segment> */}
            </Segment.Group>
        );
    }
}