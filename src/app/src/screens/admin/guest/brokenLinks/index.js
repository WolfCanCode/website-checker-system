import React, { Component } from 'react';
import 'semantic-ui-css/semantic.min.css';
import { Segment, Table, Icon } from 'semantic-ui-react'
import TableRow from './row-table';
import { Cookies } from "react-cookie";

const cookies = new Cookies();



class brokenLinksScreen extends Component {

    state = { list: [], loadingTable: false, isDisable: false, tested: false, isDoneTest: false, listReportId: [], countInternalLink: 0, countExternalLink: 0 };


    componentDidMount() {
        var comp = [];
        this.setState({ loadingTable: true });
        var param = {
            "pageOptionName": cookies.get("u_guest_token")
        }

        setInterval(() => {
            fetch("/api/guest/brokenLink", {
                method: 'POST',
                headers: {
                    'Accept': 'application/json',
                    'Content-Type': 'application/json'
                },
                body: JSON.stringify(param)
            }).then(response => response.json()).then((res) => {
                if (res.data.length !== 0) {
                    clearInterval();
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
                }
            });
        }, 1000);



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