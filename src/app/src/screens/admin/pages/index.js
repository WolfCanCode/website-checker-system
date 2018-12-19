import React, { Component } from 'react';
import { Segment, Button, Table, Icon, Dropdown } from 'semantic-ui-react'
import TableRow from '../pages/row-table';
import { Cookies } from "react-cookie";
import ReactToExcel from "react-html-table-to-excel";

const cookies = new Cookies();
export default class Pages extends Component {
    state = { list: [], loadingTable: false, isDisable: false, listReportId: [], isDoneTest: false, dateOption: [], dateValue: null };


    componentDidMount() {
        var comp = [];
        this.setState({ loadingTable: true });
        var param = {
            "userId": cookies.get("u_id"),
            "userToken": cookies.get("u_token"),
            "websiteId": cookies.get("u_w_id"),
            "pageOptionId": cookies.get("u_option"),
        };

        fetch("/api/pagestest/getHistoryList", {
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

        fetch("/api/pagestest/lastest", {
            method: 'POST',
            headers: {
                'Accept': 'application/json',
                'Content-Type': 'application/json'
            },
            body: JSON.stringify(param)
        }).then(response => response.json()).then((data) => {
            comp = data.pagetestReport.map((item, index) => {
                return (<TableRow key={index} url={item.url} titleWeb={item.titleWeb} canonicalUrl={item.canonicalUrl} httpCode={item.httpCode} />);
            });
            this.setState({ list: comp });
            this.setState({ loadingTable: false });
        });


    }

    _changeDate(event, data) {
        var comp = [];
        this.setState({ dateValue: data.value, loadingTable: true });
        fetch("/api/pagestest/getHistoryReport", {
            method: 'POST',
            headers: {
                'Accept': 'application/json',
                'Content-Type': 'application/json'
            },
            body: JSON.stringify({ reportTime: data.value })
        }).then(response => response.json()).then((res) => {
            comp = res.data.map((item, index) => {
                return (<TableRow key={index} url={item.url} titleWeb={item.titleWeb} canonicalUrl={item.canonicalUrl} httpCode={item.httpCode} />);
            });
            this.setState({ list: comp });
            this.setState({ loadingTable: false });
        });
    }

    _clickUpdateListDate() {
        var param = {
            "userId": cookies.get("u_id"),
            "userToken": cookies.get("u_token"),
            "websiteId": cookies.get("u_w_id"),
            "pageOptionId": cookies.get("u_option"),
        }

        fetch("/api/pagestest/getHistoryList", {
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

    _doPageTest() {
        this.setState({ loadingTable: true, isDisable: true });
        var comp = [];
        var listReport = [];
        var flagDontest = false;
        var param = {
            "userId": cookies.get("u_id"),
            "userToken": cookies.get("u_token"),
            "websiteId": cookies.get("u_w_id"),
            "pageOptionId": cookies.get("u_option"),
        }
        fetch("/api/pagestest", {
            method: 'POST',
            headers: {
                'Accept': 'application/json',
                'Content-Type': 'application/json'
            },
            body: JSON.stringify(param)
        }).then(response => response.json()).then((data) => {

            comp = data.pagetestReport.map((item, index) => {
                listReport.push(item.id);
                return (<TableRow key={index} url={item.url} titleWeb={item.titleWeb} canonicalUrl={item.canonicalUrl} httpCode={item.httpCode} />);
            });
            if (comp.length !== 0) {
                flagDontest = true
            }

            this.setState({ list: comp, isDisable: false, isDoneTest: flagDontest, loadingTable: false, listReportId: listReport });

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

        fetch("/api/pageTest/saveReport", {
            method: 'POST',
            headers: {
                'Accept': 'application/json',
                'Content-Type': 'application/json'
            },
            body: JSON.stringify(param)
        }).then(response => response.json()).then((data) => {
            comp = data.pagetestReport.map((item, index) => {
                return (<TableRow key={index} url={item.url} titleWeb={item.titleWeb} canonicalUrl={item.canonicalUrl} httpCode={item.httpCode} />);
            });



            this.setState({
                list: comp,
                loadingTable: false,
                isDisable: false,
                isDoneTest: false
            });
        });
    }


    render() {
        return (
            <Segment.Group>
                <Segment basic>
                    <Button icon primary labelPosition='right' disabled={this.state.isDisable} onClick={() => this._doPageTest()}>
                        Check
                       <Icon name='right arrow' />
                    </Button>
                    {this.state.isDoneTest ? <Button icon color="green" labelPosition='right' onClick={() => this._saveReport()}>
                        Save <Icon name='check' />
                    </Button> : ""}
                    <div style={{ marginBottom: '10px', float: 'right' }}>

                        <Dropdown style={{ marginRight: 10, zIndex: 9999 }} placeholder='Select history' selection defaultValue={this.state.dateValue} options={this.state.dateOption} value={this.state.dateValue} onClick={() => this._clickUpdateListDate()} onChange={(event, data) => this._changeDate(event, data)} />
                        <ReactToExcel
                            className="btn1"
                            table="table-to-xls"
                            filename="pages_test_file"
                            sheet="sheet 1"
                            buttonText={<Button color="green" ><Icon name="print" />Export</Button>}
                        />
                    </div>
                    <div style={{ float: 'right', marginBottom: '10px' }}>
                        {/* <Input icon='search' placeholder='Search...' /> */}
                    </div>

                </Segment>
                <Segment.Group horizontal style={{ maxHeight: '63vh', overflow: "auto" }}>
                    <Segment basic loading={this.state.loadingTable}>


                        <Table singleLine unstackable id="table-to-xls">
                            <Table.Header>
                                <Table.Row>
                                    <Table.HeaderCell>Title</Table.HeaderCell>
                                    <Table.HeaderCell>Web Address</Table.HeaderCell>
                                    <Table.HeaderCell>Canonical URL</Table.HeaderCell>
                                    <Table.HeaderCell>HTTP</Table.HeaderCell>
                                    {/* <Table.HeaderCell>Last checked</Table.HeaderCell> */}
                                </Table.Row>
                            </Table.Header>
                            <Table.Body>
                                {this.state.list.length === 0 ? <Table.Row><Table.Cell>This page haven't test yet, please try to test</Table.Cell></Table.Row> : this.state.list}

                            </Table.Body>
                        </Table>
                    </Segment>

                </Segment.Group>
            </Segment.Group>
        );
    }
}