import React, { Component } from 'react';
import 'semantic-ui-css/semantic.min.css';
import { Segment, Button, Table, Icon, Dropdown } from 'semantic-ui-react'
import { Cookies } from "react-cookie";
import TableRow from './row-table';
import TableRow1 from './row-table1';

import ReactToExcel from "react-html-table-to-excel";
import './style.css';


const cookies = new Cookies();



class mobileLayoutScreen extends Component {
    state = { list: [], list1: [], loadingTable: false, isDisable: false, isDoneTest: false, listReportId: [], dateOption: [], dateValue: null };


    componentDidMount() {
        var comp = [];
        var comp1 = [];

        this.setState({ loadingTable: true });
        var param = {
            "userId": cookies.get("u_id"),
            "userToken": cookies.get("u_token"),
            "websiteId": cookies.get("u_w_id"),
            "pageOptionId": cookies.get("u_option"),
        }

        fetch("/api/mobileLayoutTest/getHistoryList", {
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

        fetch("/api/mobileLayoutTest/lastest", {
            method: 'POST',
            headers: {
                'Accept': 'application/json',
                'Content-Type': 'application/json'
            },
            body: JSON.stringify(param)
        }).then(response => response.json()).then((data) => {
            comp = data.mobileLayoutTestReport.map((item, index) => {
                return (<TableRow key={index} url={item.url} title={item.title} screenShot={item.screenShot} issue={item.issue} />);
            });
            comp1 = data.mobileLayoutTestReport.map((item, index) => {
                return (<TableRow1 key={index} url={item.url} title={item.title} screenShot={item.screenShot} issue={item.issue} />);
            });
            this.setState({ list: comp });
            this.setState({ list1: comp1 });

            this.setState({ loadingTable: false });
        });


    }

    _changeDate(event, data) {
        var comp = [];
        var comp1 = [];
        this.setState({ dateValue: data.value, loadingTable: true });
        fetch("/api/mobileLayoutTest/getHistoryReport", {
            method: 'POST',
            headers: {
                'Accept': 'application/json',
                'Content-Type': 'application/json'
            },
            body: JSON.stringify({ reportTime: data.value })
        }).then(response => response.json()).then((res) => {
            comp = res.data.map((item, index) => {
                return (<TableRow key={index} url={item.url} title={item.title} screenShot={item.screenShot} issue={item.issue} />);
            });
            comp1 = res.data.map((item, index) => {
                return (<TableRow1 key={index} url={item.url} title={item.title} screenShot={item.screenShot} issue={item.issue} />);
            });
            this.setState({ list: comp });
            this.setState({ list1: comp1 });

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

        fetch("/api/mobileLayoutTest/getHistoryList", {
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

    _doMobileLayoutTest() {
        var comp = [];
        var comp1 = [];

        this.setState({ loadingTable: true, isDisable: true });
        var param = {
            "userId": cookies.get("u_id"),
            "userToken": cookies.get("u_token"),
            "websiteId": cookies.get("u_w_id"),
            "pageOptionId": cookies.get("u_option"),
        }

        fetch("/api/mobileLayoutTest", {
            method: 'POST',
            headers: {
                'Accept': 'application/json',
                'Content-Type': 'application/json'
            },
            body: JSON.stringify(param)
        }).then(response => response.json()).then((data) => {
            var listReport = [];
            comp = data.mobileLayoutTestReport.map((item, index) => {
                return (<TableRow key={index} url={item.url} title={item.title} screenShot={item.screenShot} issue={item.issue} />);
            });
            comp1 = data.mobileLayoutTestReport.map((item, index) => {
                listReport.push(item.id);
                return (<TableRow1 key={index} url={item.url} title={item.title} screenShot={item.screenShot} issue={item.issue} />);
            });
            this.setState({ list: comp });
            this.setState({ list1: comp1 });

            this.setState({ loadingTable: false, isDisable: false, isDoneTest: true, listReportId: listReport });
        });

    }

    _saveReport() {
        this.setState({ loadingTable: true });
        this.setState({ isDisable: true });
        // var comp = [];
        var comp1 = [];
        var param = {
            "userId": cookies.get("u_id"),
            "userToken": cookies.get("u_token"),
            "websiteId": cookies.get("u_w_id"),
            "pageOptionId": cookies.get("u_option"),
            "listReportId": this.state.listReportId
        };
        fetch("/api/mobileLayoutTest/SaveReport", {
            method: 'POST',
            headers: {
                'Accept': 'application/json',
                'Content-Type': 'application/json'
            },
            body: JSON.stringify(param)
        }).then(response => response.json()).then((data) => {
            // comp = data.mobileLayoutTestReport.map((item, index) => {
            //     return (<TableRow key={index} url={item.url} title={item.title} screenShot={item.screenShot} issue={item.issue} />);
            // });
            comp1 = data.mobileLayoutTestReport.map((item, index) => {
                return (<TableRow1 key={index} url={item.url} title={item.title} screenShot={item.screenShot} issue={item.issue} />);
            });
            //this.setState({ list: comp });
            this.setState({ list1: comp1 });

            this.setState({ loadingTable: false, isDisable: false, isDoneTest: false });
        });



    }


    render() {
        return (

            <Segment.Group horizontal style={{ margin: 0 }}>

                <Segment basic loading={this.state.loadingTable} >
                    <Button icon primary labelPosition='right' disabled={this.state.isDisable} onClick={() => this._doMobileLayoutTest()}>
                        Check
                   <Icon name='right arrow' />
                    </Button>
                    {this.state.isDoneTest && this.state.list.length !== 0 ? <Button icon color="green" labelPosition='right' onClick={() => this._saveReport()}>
                        Save <Icon name='check' />
                    </Button> : ""}

                    <div style={{ marginBottom: '10px', float: 'right' }}>
                        <Dropdown style={{ marginRight: 10, zIndex: 9999 }} placeholder='Select history' selection defaultValue={this.state.dateValue} options={this.state.dateOption} value={this.state.dateValue} onClick={() => this._clickUpdateListDate()} onChange={(event, data) => this._changeDate(event, data)} />
                        <ReactToExcel
                            className="btn1"
                            table="table-to-xls1"
                            filename="mobile_layout_test_file"
                            sheet="sheet 1"
                            buttonText={<Button color="green"><Icon name="print" />Export</Button>}
                        />
                    </div>
                    <div style={{ marginBottom: '10px', float: 'right' }}>
                        {/* <Input icon='search' placeholder='Search...' /> */}
                    </div>
                    <Table singleLine unstackable style={{ fontSize: '16px', }} id="table-to-xls">
                        <Table.Header style={{ textAlign: 'center' }} >
                            <Table.Row>
                                <Table.HeaderCell>ScreenShot</Table.HeaderCell>
                                <Table.HeaderCell style={{ textAlign: 'left' }}>Page</Table.HeaderCell>
                                <Table.HeaderCell>Issue</Table.HeaderCell>


                            </Table.Row>
                        </Table.Header>
                        <Table.Body>
                            {this.state.list.length === 0 ? <Table.Row><Table.Cell>This page haven't test yet, please try to test</Table.Cell></Table.Row> : this.state.list}


                        </Table.Body>


                    </Table>
                    <Table singleLine unstackable style={{ fontSize: '16px', }} id="table-to-xls1">
                        <Table.Header style={{ textAlign: 'center' }} >
                            <Table.Row>
                                <Table.HeaderCell>ScreenShot</Table.HeaderCell>
                                <Table.HeaderCell style={{ textAlign: 'left' }}>Page</Table.HeaderCell>
                                <Table.HeaderCell>Issue</Table.HeaderCell>


                            </Table.Row>
                        </Table.Header>
                        <Table.Body>
                            {this.state.list1.length === 0 ? <Table.Row><Table.Cell>This page haven't test yet, please try to test</Table.Cell></Table.Row> : this.state.list1}


                        </Table.Body>


                    </Table>
                </Segment>

            </Segment.Group>

        );
    }



}

export default mobileLayoutScreen;