import React, { Component } from 'react';
import 'semantic-ui-css/semantic.min.css';
import { Segment, Button, Table, Icon, Dropdown } from 'semantic-ui-react'
import TableRow from './row-table';
import { Cookies } from "react-cookie";
import ReactToExcel from "react-html-table-to-excel";

const cookies = new Cookies();



class brokenLinksScreen extends Component {

    state = { list: [], loadingTable: false, isDisable: false, tested: false, isDoneTest: false, listReportId: [], countInternalLink: 0, countExternalLink: 0, dateOption: [], dateValue: null };


    componentDidMount() {
        var comp = [];
        this.setState({ loadingTable: true });
        var param = {
            "userId": cookies.get("u_id"),
            "userToken": cookies.get("u_token"),
            "websiteId": cookies.get("u_w_id"),
            "pageOptionId": cookies.get("u_option"),
        }

        fetch("/api/brokenLink/getHistoryList", {
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




        fetch("/api/brokenLink/lastest", {
            method: 'POST',
            headers: {
                'Accept': 'application/json',
                'Content-Type': 'application/json'
            },
            body: JSON.stringify(param)
        }).then(response => response.json()).then((data) => {
            comp = data.brokenLinkReport.map((item, index) => {
                return (<TableRow key={index} urlPage={item.urlPage} urlLink={item.urlLink} type={item.type} httpCode={item.httpCode} httpMessage={item.httpMessage} />);
            });
            let countInternalLink = 0;
            let countExternalLink = 0;

            countInternalLink = data.brokenLinkReport.reduce((type, item) => {
                if (item.type === '1') {
                    countInternalLink++;
                }

                return countInternalLink;
            }, 0)

            countExternalLink = data.brokenLinkReport.reduce((type, item) => {
                if (item.type === '2') {
                    countExternalLink++;
                }

                return countExternalLink;
            }, 0)

            this.setState({ countInternalLink: countInternalLink })
            this.setState({ countExternalLink: countExternalLink })

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

        fetch("/api/brokenLink/getHistoryList", {
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

    _changeDate(event, data) {
        this.setState({ dateValue: data.value, loadingTable: true });
        var comp = [];
        fetch("/api/brokenLink/getHistoryReport", {
            method: 'POST',
            headers: {
                'Accept': 'application/json',
                'Content-Type': 'application/json'
            },
            body: JSON.stringify({ reportTime: data.value })
        }).then(response => response.json()).then((res) => {
            comp = res.data.map((item, index) => {
                return (<TableRow key={index} urlPage={item.urlPage} urlLink={item.urlLink} type={item.type} httpCode={item.httpCode} httpMessage={item.httpMessage} />);
            });
            let countInternalLink = 0;
            let countExternalLink = 0;

            countInternalLink = res.data.reduce((type, item) => {
                if (item.type === '1') {
                    countInternalLink++;
                }

                return countInternalLink;
            }, 0)

            countExternalLink = res.data.reduce((type, item) => {
                if (item.type === '2') {
                    countExternalLink++;
                }

                return countExternalLink;
            }, 0)

            this.setState({ countInternalLink: countInternalLink })
            this.setState({ countExternalLink: countExternalLink })

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
            var listReport = [];
            comp = data.brokenLinkReport.map((item, index) => {
                listReport.push(item.id);
                return (<TableRow key={index} urlPage={item.urlPage} urlLink={item.urlLink} type={item.type} httpCode={item.httpCode} httpMessage={item.httpMessage} />);
            });
            let countInternalLink = 0;
            let countExternalLink = 0;

            countInternalLink = data.brokenLinkReport.reduce((type, item) => {
                if (item.type === '1') {
                    countInternalLink++;
                }

                return countInternalLink;
            }, 0)

            countExternalLink = data.brokenLinkReport.reduce((type, item) => {
                if (item.type === '2') {
                    countExternalLink++;
                }

                return countExternalLink;
            }, 0)

            this.setState({ countInternalLink: countInternalLink })
            this.setState({ countExternalLink: countExternalLink })


            this.setState({ list: comp });
            if (this.state.list.length === 0) {
                this.setState({ tested: true });
            }

            this.setState({ loadingTable: false, isDisable: false, isDoneTest: true, listReportId: listReport });
        });

    }

    _saveReport() {
        var comp = [];
        this.setState({ loadingTable: true, isDisable: true });
        var param = {
            "userId": cookies.get("u_id"),
            "userToken": cookies.get("u_token"),
            "websiteId": cookies.get("u_w_id"),
            "pageOptionId": cookies.get("u_option"),
            "listReportId": this.state.listReportId
        };

        fetch("/api/brokenLink/SaveReport", {
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

            this.setState({ loadingTable: false, isDisable: false, isDoneTest: false });
        });

    }


    render() {
        return (
            <Segment.Group>
                <Segment basic>

                </Segment>
                <Segment.Group horizontal style={{ margin: 0 }}>

                    <Segment loading={this.state.loadingTable}>
                        <Segment.Group horizontal >
                            <Segment style={{ margin: 'auto', textAlign: 'center', padding: 0 }}>
                                <Icon className="check" size='huge' color='green' />
                            </Segment>
                            <Segment style={{ paddingLeft: '10px' }}>
                                <p style={{ fontSize: 24 }}>{this.state.countInternalLink} <br />
                                    Internal broken links</p>
                            </Segment >
                            <Segment style={{ margin: 'auto', textAlign: 'center', padding: 0 }}>
                                <Icon className="broken chain" size='huge' color='red' /></Segment>
                            <Segment>
                                <p style={{ fontSize: 24 }}>{this.state.countExternalLink} <br /> External broken links</p>
                            </Segment>
                        </Segment.Group>
                        <Segment basic style={{ marginBottom: '-18px', marginTop: '-18px' }}>
                            <Button icon primary labelPosition='right' disabled={this.state.isDisable} onClick={() => this._doBrokenLink()}>
                                Check
                       <Icon name='right arrow' />
                            </Button>
                            {this.state.isDoneTest && this.state.list.length !== 0 ? <Button icon color="green" labelPosition='right' onClick={() => this._saveReport()}>
                                Save <Icon name='check' />
                            </Button> : ""}
                            <div style={{ float: 'right' }}>
                                <Dropdown style={{ marginRight: 10, zIndex: 9999 }} placeholder='Select history' selection defaultValue={this.state.dateValue} options={this.state.dateOption} value={this.state.dateValue} onClick={() => this._clickUpdateListDate()} onChange={(event, data) => this._changeDate(event, data)} />
                                <ReactToExcel
                                    className="btn1"
                                    table="table-to-xls"
                                    filename="broken_link_test_file"
                                    sheet="sheet 1"
                                    buttonText={<Button color="green"><Icon name="print" />Export</Button>}
                                />
                            </div>

                            {/* <Input icon='search' placeholder='Search...' style={{ float: 'right' }} /> */}
                        </Segment>
                        <Segment style={{ maxHeight: '50vh', overflow: "auto" }}>

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
                                    {this.state.list.length === 0 ? <Table.Row><Table.Cell>{this.state.tested ? "This site has no broken links!" : "This page haven't test yet, please try to test!"}</Table.Cell></Table.Row> : this.state.list}
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