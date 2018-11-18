import React, { Component } from 'react';
import { Segment, Button, Table, Input, Icon } from 'semantic-ui-react'
import TableRow from '../pages/row-table';
import { Cookies } from "react-cookie";

const cookies = new Cookies();
export default class Pages extends Component {
    state = { list: [], loadingTable: false, isDisable: false };


    componentDidMount() {
        var comp = [];
        this.setState({ loadingTable: true });
        var param = {
            "userId": cookies.get("u_id"),
            "userToken": cookies.get("u_token"),
            "websiteId": cookies.get("u_w_id"),
            "pageOptionId": cookies.get("u_option"),
        };

        fetch("http://localhost:8080/api/pagestest/lastest", {
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
    _doPageTest() {
        this.setState({ loadingTable: true, isDisable: true });
        var comp = [];
        var param = {
            "userId": cookies.get("u_id"),
            "userToken": cookies.get("u_token"),
            "websiteId": cookies.get("u_w_id"),
            "pageOptionId": cookies.get("u_option"),
        }
        fetch("http://localhost:8080/api/pagestest", {
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

    render() {
        return (
            <Segment.Group>
                <Segment basic>
                    <Button icon labelPosition='right' disabled={this.state.isDisable} onClick={() => this._doPageTest()}>
                        Check
                       <Icon name='right arrow' />
                    </Button>
                    <div style={{ float: 'right', marginBottom: '10px' }}>
                        <Input icon='search' placeholder='Search...' />
                    </div>
                    <Button style={{ marginRight: '10px' }} floated='right'><Icon name="print" />Export</Button>
                </Segment>
                <Segment.Group horizontal style={{ maxHeight: '63vh', overflow: "auto" }}>
                    <Segment basic loading={this.state.loadingTable}>


                        <Table singleLine unstackable>
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