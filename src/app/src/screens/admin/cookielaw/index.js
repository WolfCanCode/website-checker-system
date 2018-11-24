import React, { Component } from 'react';
import { Segment, Button, Icon, Table, Input } from 'semantic-ui-react'
import TableRow from './row-table';
import { Cookies } from "react-cookie";
import ReactToExcel from "react-html-table-to-excel";



const cookies = new Cookies();
export default class CookieLaw extends Component {

    state = { list: [], loadingTable: false, isDisable: false };


    componentDidMount() {
        var comp = [];
        this.setState({ loadingTable: true });
        var param = {
            "userId": cookies.get("u_id"),
            "userToken": cookies.get("u_token"),
            "websiteId": cookies.get("u_w_id"),
            "pageOptionId": cookies.get("u_option"),
        }
        fetch("/api/cookie/lastest", {
            method: 'POST',
            headers: {
                'Accept': 'application/json',
                'Content-Type': 'application/json'
            },
            body: JSON.stringify(param)
        }).then(response => response.json()).then((data) => {
            comp = data.cookieReport.map((item, index) => {
                return (<TableRow key={index} cookieName={item.cookieName} category={item.category} party={item.party} description={item.description} />);
            });
            this.setState({ list: comp });
            this.setState({ loadingTable: false });
        });


    }

    _doCookies() {
        var comp = [];
        this.setState({ loadingTable: true, isDisable: true });
        var param = {
            "userId": cookies.get("u_id"),
            "userToken": cookies.get("u_token"),
            "websiteId": cookies.get("u_w_id"),
            "pageOptionId": cookies.get("u_option"),
        }

        fetch("/api/cookie", {
            method: 'POST',
            headers: {
                'Accept': 'application/json',
                'Content-Type': 'application/json'
            },
            body: JSON.stringify(param)
        }).then(response => response.json()).then((data) => {
            comp = data.cookieReport.map((item, index) => {
                return (<TableRow key={index} cookieName={item.cookieName} category={item.category} party={item.party} description={item.description} />);
            });
            this.setState({ list: comp });
            this.setState({ loadingTable: false, isDisable: false });
        });

    }





    render() {
        return (

            <Segment.Group horizontal style={{ margin: 0 }}>

                <Segment basic loading={this.state.loadingTable} >
                    <Button icon  primary labelPosition='right' disabled={this.state.isDisable} onClick={() => this._doCookies()}>
                        Check
                       <Icon name='right arrow' />
                    </Button>
                    <div style={{ marginBottom: '10px', float: 'right', color : 'green' }}>


                        <ReactToExcel
                            className="btn1"
                            table="table-to-xls"
                            filename="cookie_test_file"
                            sheet="sheet 1"
                            buttonText={<Button color="green"><Icon name="print" />Export</Button>}
                        />
                    </div>

                    <div style={{ marginBottom: '10px', float: 'right' }}>
                        <Input icon='search' placeholder='Search...' />
                    </div>
                    <Table singleLine unstackable style={{ fontSize: '16px', }} id="table-to-xls">
                        <Table.Header textAlign='center'>
                            <Table.Row>
                                <Table.HeaderCell>Cookie Name</Table.HeaderCell>
                                <Table.HeaderCell>Category</Table.HeaderCell>
                                <Table.HeaderCell>Party</Table.HeaderCell>
                                <Table.HeaderCell>Description</Table.HeaderCell>

                            </Table.Row>
                        </Table.Header>
                        <Table.Body>
                            {this.state.list.length === 0 ? <Table.Row><Table.Cell>This page haven't test yet, please try to test</Table.Cell></Table.Row> : this.state.list}
                            {/* <Table.Row>
                                        <Table.Cell style={{ width: '100px', whiteSpace: 'normal', wordBreak: 'break-all' }}>Google Analytics</Table.Cell>
                                        <Table.Cell style={{ width: '100px', whiteSpace: 'normal', wordBreak: 'break-all' }}>The world most popular analytics tool.</Table.Cell>
                                        <Table.Cell style={{ width: '100px', whiteSpace: 'normal', wordBreak: 'break-all' }}><Label circular color='yellow'>_gat</Label><Label circular color='yellow'>ga</Label>
                                        <Label circular color='yellow'>__utmb</Label> <Label circular color='yellow'>__utmc</Label> <Label circular color='yellow'>_utmz</Label><Label circular color='grey'>+2</Label>
                                        </Table.Cell>
                                        <Table.Cell style={{ width: '100px', whiteSpace: 'normal', wordBreak: 'break-all' }}> 2,154  </Table.Cell>
                                        <Table.Cell style={{ width: '50px', whiteSpace: 'normal', wordBreak: 'break-all' }}>100%</Table.Cell>
                                    </Table.Row> */}

                        </Table.Body>
                    </Table>
                </Segment>


            </Segment.Group>

        );
    }
}