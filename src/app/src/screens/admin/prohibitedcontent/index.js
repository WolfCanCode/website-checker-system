import React, { Component } from 'react';
import { Segment, Button, Icon, Table, Input } from 'semantic-ui-react'
import TableRow from './row-table';
import { Cookies } from "react-cookie";
import ReactToExcel from "react-html-table-to-excel";



const cookies = new Cookies();
export default class ProhibitedContent extends Component {
    

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
        fetch("/api/prohibitedContent/lastest", {
            method: 'POST',
            headers: {
                'Accept': 'application/json',
                'Content-Type': 'application/json'
            },
            body: JSON.stringify(param)
        }).then(response => response.json()).then((data) => {
            comp = data.prohibitedContentReport.map((item, index) => {
                return (<TableRow key={index} urlPage={item.urlPage} word={item.word} fragment={item.fragment} type={item.type} />);
            });
            this.setState({ list: comp });
            this.setState({ loadingTable: false });
        });


    }

    _doProhibitedContent() {
        var comp = [];
        this.setState({ loadingTable: true, isDisable: true });
        var param = {
            "userId": cookies.get("u_id"),
            "userToken": cookies.get("u_token"),
            "websiteId": cookies.get("u_w_id"),
            "pageOptionId": cookies.get("u_option"),
        }

        fetch("/api/prohibitedContent", {
            method: 'POST',
            headers: {
                'Accept': 'application/json',
                'Content-Type': 'application/json'
            },
            body: JSON.stringify(param)
        }).then(response => response.json()).then((data) => {
            comp = data.prohibitedContentReport.map((item, index) => {
                return (<TableRow key={index} urlPage={item.urlPage} word={item.word} fragment={item.fragment} type={item.type} />);
            });
            this.setState({ list: comp });
            this.setState({ loadingTable: false, isDisable: false });
        });

    }





    render() {
        return (

            <Segment.Group horizontal style={{ margin: 0 }}>

                <Segment basic loading={this.state.loadingTable} >
                    <Button icon primary labelPosition='right' disabled={this.state.isDisable} onClick={() => this._doProhibitedContent()}>
                        Check
                       <Icon name='right arrow' />
                    </Button>
                    <div style={{ marginBottom: '10px', float: 'right' }}>


                        <ReactToExcel
                            className="btn1"
                            table="table-to-xls"
                            filename="prohibited_test_file"
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
                               
                                <Table.HeaderCell>Word</Table.HeaderCell>
                                <Table.HeaderCell>Type</Table.HeaderCell>
                                <Table.HeaderCell>Fragment</Table.HeaderCell>
                                <Table.HeaderCell>UrlPage</Table.HeaderCell>
                                

                            </Table.Row>
                        </Table.Header>
                        <Table.Body>
                            {this.state.list.length === 0 ? <Table.Row><Table.Cell>This page haven't test yet, please try to test</Table.Cell></Table.Row> : this.state.list}
                           

                        </Table.Body>
                    </Table>
                </Segment>


            </Segment.Group>

        );
    }
}