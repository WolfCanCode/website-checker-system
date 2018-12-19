import React, { Component } from 'react';
import 'semantic-ui-css/semantic.min.css';
import { Segment, Button, Table, Icon, Dropdown } from 'semantic-ui-react'
import { Cookies } from "react-cookie";
import ReactToExcel from "react-html-table-to-excel";
import TableRow from './row-table';

const cookies = new Cookies();

class ServerBehaviorScreen extends Component {
    state = { done: 0, list: [], loadingTable: false, isDisable: false, listReportId: [], isDoneTest: false, dateOption: [], dateValue: null };
    componentDidMount() {
        var comp = [];
        this.setState({ loadingTable: true });
        var param = {
            "userId": cookies.get("u_id"),
            "userToken": cookies.get("u_token"),
            "websiteId": cookies.get("u_w_id"),
            "pageOptionId": cookies.get("u_option"),
        }

        fetch("/api/svbehavior/getHistoryList", {
            method: 'POST',
            headers: {
                'Accept': 'application/json',
                'Content-Type': 'application/json'
            },
            body: JSON.stringify(param)
        }).then(response => response.json()).then((res) => {
            if (res.action === "SUCCESS") {
                if (res.data.length !== 0) {
                    var dateOption = res.data.map((item, index) => {
                        return { key: index, value: item, text: new Date(item).toLocaleString() };
                    })
                    console.log(dateOption);
                    this.setState({ dateOption: dateOption, dateValue: dateOption[0] })
                }
            }
        });

        fetch("/api/svbehavior/lastest", {
            method: 'POST',
            headers: {
                'Accept': 'application/json',
                'Content-Type': 'application/json'
            },
            body: JSON.stringify(param)
        }).then(response => response.json()).then((data) => {
            if (data.serverBehaviorReport !== null) {
                comp = data.serverBehaviorReport.map((item, index) => {
                    return (<TableRow key={index} url={item.url} isPageSSL={item.pageSSL} isRedirectHTTPS={item.redirectHTTPS} isRedirectWWW={item.redirectWWW} />);
                });
            }
            this.setState({ list: comp, loadingTable: false });
        });

    }

    _changeDate(event, data) {
        var comp = [];
        this.setState({ dateValue: data.value, loadingTable: true });
        fetch("/api/svbehavior/getHistoryReport", {
            method: 'POST',
            headers: {
                'Accept': 'application/json',
                'Content-Type': 'application/json'
            },
            body: JSON.stringify({ reportTime: data.value })
        }).then(response => response.json()).then((res) => {
            comp = res.data.map((item, index) => {
                return (<TableRow key={index} url={item.url} isPageSSL={item.pageSSL} isRedirectHTTPS={item.redirectHTTPS} isRedirectWWW={item.redirectWWW} />);
            });

            this.setState({ list: comp, loadingTable: false });
        });

    }

    _clickUpdateListDate() {
        var param = {
            "userId": cookies.get("u_id"),
            "userToken": cookies.get("u_token"),
            "websiteId": cookies.get("u_w_id"),
            "pageOptionId": cookies.get("u_option"),
        }

        fetch("/api/svbehavior/getHistoryList", {
            method: 'POST',
            headers: {
                'Accept': 'application/json',
                'Content-Type': 'application/json'
            },
            body: JSON.stringify(param)
        }).then(response => response.json()).then((res) => {
            if (res.action === "SUCCESS") {
                if (res.data.length !== 0) {
                    var dateOption = res.data.map((item, index) => {
                        return { key: index, value: item, text: new Date(item).toLocaleString() };
                    })
                    console.log(dateOption);
                    this.setState({ dateOption: dateOption })
                }
            }
        });

    }

    _doBehaviorTest() {
        this.setState({ loadingTable: true });
        this.setState({ isDisable: true });
        var comp = [];
        var param = {
            "userId": cookies.get("u_id"),
            "userToken": cookies.get("u_token"),
            "websiteId": cookies.get("u_w_id"),
            "pageOptionId": cookies.get("u_option"),
        };

        fetch("/api/svbehavior", {
            method: 'POST',
            headers: {
                'Accept': 'application/json',
                'Content-Type': 'application/json'
            },
            body: JSON.stringify(param)
        }).then(response => response.json()).then((data) => {
            var listReport = [];
            comp = data.serverBehaviorReport.map((item, index) => {
                listReport.push(item.id);
                return (<TableRow key={index} url={item.url} isPageSSL={item.pageSSL} isRedirectHTTPS={item.redirectHTTPS} isRedirectWWW={item.redirectWWW} />);
            });
            this.setState({
                list: comp, loadingTable: false, isDisable: false, listReportId: listReport, isDoneTest: true
            });
        });
    }

    _saveReport() {
        this.setState({ loadingTable: true });
        this.setState({ isDisable: true });
        var comp = [];
        var param = {
            "userId": cookies.get("u_id"),
            "userToken": cookies.get("u_token"),
            "websiteId": cookies.get("u_w_id"),
            "pageOptionId": cookies.get("u_option"),
            "listReportId": this.state.listReportId
        };

        fetch("/api/svbehavior/saveReport", {
            method: 'POST',
            headers: {
                'Accept': 'application/json',
                'Content-Type': 'application/json'
            },
            body: JSON.stringify(param)
        }).then(response => response.json()).then((data) => {
            comp = data.serverBehaviorReport.map((item, index) => {
                return (<TableRow key={index} url={item.url} isPageSSL={item.pageSSL} isRedirectHTTPS={item.redirectHTTPS} isRedirectWWW={item.redirectWWW} />);
            });
            this.setState({ list: comp, loadingTable: false, isDoneTest: false, isDisable: false });
        });
    }

    render() {
        return (
            <Segment.Group>
                <Segment style={{ border: 0, minWidth: 300 }}>
                    <Button icon primary labelPosition='right' disabled={this.state.isDisable} onClick={() => this._doBehaviorTest()}>
                        Check
                       <Icon name='right arrow' />
                    </Button>
                    {this.state.isDoneTest ? <Button icon color="green" labelPosition='right' onClick={() => this._saveReport()}>
                        Save <Icon name='check' />
                    </Button> : ""}

                    <div style={{ float: 'right' }}>
                        <Dropdown style={{ marginRight: 10, zIndex: 9999 }} placeholder='Select history' selection defaultValue={this.state.dateValue} options={this.state.dateOption} value={this.state.dateValue} onClick={() => this._clickUpdateListDate()} onChange={(event, data) => this._changeDate(event, data)} />

                        <ReactToExcel
                            className="btn1"
                            table="table-to-xls"
                            filename="server_behavior_test_file"
                            sheet="sheet 1"
                            buttonText={<Button color="green" ><Icon name="print" />Export</Button>}
                        />
                    </div>

                    {/* <Input icon='search' placeholder='Search...' style={{ float: 'right', marginRight: 5 }} /> */}
                </Segment>

                <Segment.Group horizontal >
                    <Segment basic loading={this.state.loadingTable} >


                        <Segment basic style={{ maxHeight: '61vh', overflow: "auto" }}>

                            <Table unstackable id="table-to-xls">
                                <Table.Header >
                                    <Table.Row>
                                        <Table.HeaderCell>Page</Table.HeaderCell>
                                        <Table.HeaderCell>SSL</Table.HeaderCell>
                                        <Table.HeaderCell>HTTPS redirection</Table.HeaderCell>
                                        <Table.HeaderCell>WWW redirection</Table.HeaderCell>

                                    </Table.Row>
                                </Table.Header>
                                <Table.Body>

                                    {this.state.list.length === 0 ? <Table.Row><Table.Cell>This page haven't test yet, please try to test</Table.Cell></Table.Row> : this.state.list}


                                </Table.Body>
                            </Table>

                        </Segment>
                    </Segment>

                </Segment.Group>
            </Segment.Group>

        );
    }



}

export default ServerBehaviorScreen;