import React, { Component } from 'react';
import 'semantic-ui-css/semantic.min.css';
import { Segment, Button, Table, Icon, Input } from 'semantic-ui-react'
import TableRow from './row-table';
import { Cookies } from "react-cookie";
import ReactToExcel from "react-html-table-to-excel";


const cookies = new Cookies();





class brokenPagesScreen extends Component {

    state = { list: [], loadingTable: false, isDisable: false, tested: false , countErrorPage : 0 , countMissingPage : 0, isDoneTest: false, listReportId: []};


    componentDidMount() {
        var comp = [];
        this.setState({ loadingTable: true });
        var param = {
            "userId": cookies.get("u_id"),
            "userToken": cookies.get("u_token"),
            "websiteId": cookies.get("u_w_id"),
            "pageOptionId": cookies.get("u_option"),
        }
        fetch("/api/brokenPage/lastest", {
            method: 'POST',
            headers: {
                'Accept': 'application/json',
                'Content-Type': 'application/json'
            },
            body: JSON.stringify(param)
        }).then(response => response.json()).then((data) => {
            comp = data.brokenPageReport.map((item, index) => {
                return (<TableRow key={index} urlPage={item.urlPage} stt={item.stt} httpCode={item.httpCode} httpMessage={item.httpMessage} />);
            });
            let countErrorPage = 0;
            let countMissingPage = 0;

             countErrorPage = data.brokenPageReport.reduce((stt,item) => {
                 if(item.stt === 'Error Page'){
                    countErrorPage++;
                 }

                return countErrorPage;
            }, 0)

            countMissingPage = data.brokenPageReport.reduce((stt,item) => {
                if(item.stt === 'Missing Page'){
                    countMissingPage++;
                }

               return countMissingPage;
           }, 0)
            
            this.setState({ countErrorPage: countErrorPage })
            this.setState({ countMissingPage: countMissingPage })
            this.setState({ list: comp });
            this.setState({ loadingTable: false });
        });


    }


    _doBrokenPage() {
        
        var comp = [];
        this.setState({ loadingTable: true, isDisable: true });
        var param = {
            "userId": cookies.get("u_id"),
            "userToken": cookies.get("u_token"),
            "websiteId": cookies.get("u_w_id"),
            "pageOptionId": cookies.get("u_option"),
        }

        fetch("/api/brokenPage", {
            method: 'POST',
            headers: {
                'Accept': 'application/json',
                'Content-Type': 'application/json'
            },
            body: JSON.stringify(param)
        }).then(response => response.json()).then((data) => {
            var listReport = [];
            comp = data.brokenPageReport.map((item, index) => {
                listReport.push(item.id);
                return (<TableRow key={index} urlPage={item.urlPage} stt={item.stt} httpCode={item.httpCode} httpMessage={item.httpMessage} />);
            });
            this.setState({ list: comp });
            if (this.state.list.length === 0) {
                this.setState({ tested: true });
            }
            let countErrorPage = 0;
            let countMissingPage = 0;

             countErrorPage = data.brokenPageReport.reduce((stt,item) => {
                 if(item.stt === 'Error Page'){
                    countErrorPage++;
                 }

                return countErrorPage;
            }, 0)

            countMissingPage = data.brokenPageReport.reduce((stt,item) => {
                if(item.stt === 'Missing Page'){
                    countMissingPage++;
                }

               return countMissingPage;
           }, 0)
            
            this.setState({ countErrorPage: countErrorPage })
            this.setState({ countMissingPage: countMissingPage })

           


            this.setState({ loadingTable: false, isDisable: false, isDoneTest: true, listReportId: listReport });
        });


    }

    _saveReport(){
        var comp = [];
        this.setState({ loadingTable: true, isDisable: true });
        var param = {
            "userId": cookies.get("u_id"),
            "userToken": cookies.get("u_token"),
            "websiteId": cookies.get("u_w_id"),
            "pageOptionId": cookies.get("u_option"),
            "listReportId": this.state.listReportId
        }

        fetch("/api/brokenPage/SaveReport", {
            method: 'POST',
            headers: {
                'Accept': 'application/json',
                'Content-Type': 'application/json'
            },
            body: JSON.stringify(param)
        }).then(response => response.json()).then((data) => {
            comp = data.brokenPageReport.map((item, index) => {
                return (<TableRow key={index} urlPage={item.urlPage} stt={item.stt} httpCode={item.httpCode} httpMessage={item.httpMessage} />);
            });
            this.setState({ list: comp });
            if (this.state.list.length === 0) {
                this.setState({ tested: true });
            }
            let countErrorPage = 0;
            let countMissingPage = 0;

             countErrorPage = data.brokenPageReport.reduce((stt,item) => {
                 if(item.stt === 'Error Page'){
                    countErrorPage++;
                 }

                return countErrorPage;
            }, 0)

            countMissingPage = data.brokenPageReport.reduce((stt,item) => {
                if(item.stt === 'Missing Page'){
                    countMissingPage++;
                }

               return countMissingPage;
           }, 0)
            
            this.setState({ countErrorPage: countErrorPage })
            this.setState({ countMissingPage: countMissingPage })

           


            this.setState({ loadingTable: false, isDisable: false, isDoneTest: false });
        });
    }



    render() {
        return (
            <Segment.Group>
                
                <Segment.Group horizontal style={{ margin: 0 }}>

                    <Segment basic loading={this.state.loadingTable}>

                        <Segment.Group horizontal >

                            <Segment style={{ margin: 'auto', textAlign: 'center', padding: 0 }}>
                                <Icon className="warning sign" size='huge' color='red' />
                            </Segment>
                            <Segment style={{ paddingLeft: '10px' }}>
                                <p style={{ fontSize: 24 }}>{this.state.countMissingPage}<br />
                                    Missing pages</p>
                            </Segment >

                            <Segment style={{ margin: 'auto', textAlign: 'center', padding: 0 }}>
                                <Icon className="warning sign" size='huge' color='black' /></Segment>
                            <Segment>
                                <p style={{ fontSize: 24 }}>{this.state.countErrorPage} <br /> Error pages</p>
                            </Segment>
                        </Segment.Group>
                        <Segment basic style={{ marginBottom: '-18px', marginTop: '-18px' }}>
                        <Button icon primary labelPosition='right' disabled={this.state.isDisable} onClick={() => this._doBrokenPage()}>
                        Check
                       <Icon name='right arrow' />
                    </Button>
                    {this.state.isDoneTest && this.state.list.length !== 0 ? <Button icon color="green" labelPosition='right' onClick={() => this._saveReport()}>
                        Save <Icon name='check' />
                    </Button> : ""}
                            <div style={{ marginBottom: '10px', float: 'right' }}>


                                <ReactToExcel
                                    className="btn1"
                                    table="table-to-xls"
                                    filename="broken_page_test_file"
                                    sheet="sheet 1"
                                    buttonText={<Button color="green"><Icon name="print" />Export</Button>}
                                />
                            </div>
                            <Input icon='search' placeholder='Search...' style={{ float: 'right' }} />
                        </Segment>
                        <Segment style={{ maxHeight: '50vh', overflow: "auto" }}>

                            <Table singleLine unstackable textAlign='center' style={{ tableLayout: 'auto' }} id="table-to-xls">
                                <Table.Header >
                                    <Table.Row>
                                        <Table.HeaderCell>Page</Table.HeaderCell>
                                        <Table.HeaderCell>Status</Table.HeaderCell>
                                        <Table.HeaderCell>Issue</Table.HeaderCell>
                                    </Table.Row>
                                </Table.Header>
                                <Table.Body>
                                    {this.state.list.length === 0 ? <Table.Row><Table.Cell>{this.state.tested ? "This site has no broken pages!" : "This page haven't test yet, please try to test!"}</Table.Cell> </Table.Row> : this.state.list}
                                </Table.Body>
                            </Table>
                        </Segment>
                    </Segment>

                </Segment.Group></Segment.Group>

        );
    }



}

export default brokenPagesScreen;