import React, { Component } from 'react';
import 'semantic-ui-css/semantic.min.css';
import { Segment, Button, Table, Icon } from 'semantic-ui-react';
import { Cookies } from "react-cookie";
import TableRow from './data-row';

const cookies = new Cookies();
class spellingScreen extends Component {
    state = { list: [], loadingTable: false, isDisable: false, listReportId: [], isDoneTest: false };

    componentDidMount() {
        var comp = [];
        this.setState({ loadingTable: true });
        var param = {
            "userId": cookies.get("u_id"),
            "userToken": cookies.get("u_token"),
            "websiteId": cookies.get("u_w_id"),
            "pageOptionId": cookies.get("u_option"),
        };

        fetch("/api/spelling/loadData", {
            method: 'POST',
            headers: {
                'Accept': 'application/json',
                'Content-Type': 'application/json'
            },
            body: JSON.stringify(param)
        }).then(response => response.json()).then((data) => {

            comp = data.spellingReport.map((item, index) => {

                return (<TableRow no={index} word={item.wrongWord} excerpt={item.excerpt}
                    page={item.page} decision={item.decision} selectedSuggestion={item.selectedSuggestion} />);
            });

            this.setState({ list: comp, loadingTable: false, isDisable: false, isDoneTest: true });
        });


    }

    _doSpellingTest() {
        var comp = [];
        this.setState({ loadingTable: true });
        var param = {
            "userId": cookies.get("u_id"),
            "userToken": cookies.get("u_token"),
            "websiteId": cookies.get("u_w_id"),
            "pageOptionId": cookies.get("u_option"),
        };

        fetch("/api/spelling/getWrongWords", {
            method: 'POST',
            headers: {
                'Accept': 'application/json',
                'Content-Type': 'application/json'
            },
            body: JSON.stringify(param)
        }).then(response => response.json()).then((data) => {
            var idList = []
            comp = data.spellingReport.map((item, index) => {
                idList.push(item.id);
                return (<TableRow no={index} word={item.wrongWord} excerpt={item.excerpt} page={item.page} />);
            });
            this.setState({ list: comp, loadingTable: false, isDisable: false, isDoneTest: true, listReportId: idList });
        });

    }
    _saveSpellingReport() {
        var comp = [];
        this.setState({ loadingTable: true, isDisable: true });
        var param = {
            "userId": cookies.get("u_id"),
            "userToken": cookies.get("u_token"),
            "websiteId": cookies.get("u_w_id"),
            "pageOptionId": cookies.get("u_option"),
            "listReportId": this.state.listReportId,
        }

        fetch("/api/spelling/save", {
            method: 'POST',
            headers: {
                'Accept': 'application/json',
                'Content-Type': 'application/json'
            },
            body: JSON.stringify(param)
        }).then(response => response.json()).then((data) => {
            comp = data.spellingSaveResult.map((item, index) => {
                return (<TableRow no={index} word={item.wrongWord} excerpt={item.excerpt} page={item.page} />);
            });

            this.setState({ list: comp, loadingTable: false, isDisable: false, isDoneTest: false });
        });
    }
    render() {
        return (
            <div>
                <Segment.Group>
                    <Segment basic loading={this.state.loadingTable} >
                        <Button icon primary labelPosition='right' disabled={this.state.isDisable} onClick={() => this._doSpellingTest()}>
                            Check
                       <Icon name='right arrow' />
                        </Button>
                        {this.state.isDoneTest ? <Button icon color="green" labelPosition='right' onClick={() => this._saveSpellingReport()}>
                            Save <Icon name='check' />
                        </Button> : ""}
                        <div style={{ marginBottom: '10px', float: 'right' }}>


                            {/* <ReactToExcel
                            className="btn1"
                            table="table-to-xls"
                            filename="pages_test_file"
                            sheet="sheet 1"
                            buttonText={<Button color="green" ><Icon name="print" />Export</Button>}
                        /> */}
                        </div>
                        <div style={{ float: 'right', marginBottom: '10px' }}>
                            {/* <Input icon='search' placeholder='Search...' /> */}
                        </div>

                    </Segment>
                    <Segment.Group horizontal>
                        <Segment basic>
                            <Table singleLine unstackable textAlign='center' style={{ tableLayout: 'auto' }}>
                                <Table.Header >
                                    <Table.Row>
                                        <Table.HeaderCell>No.</Table.HeaderCell>
                                        <Table.HeaderCell>Word</Table.HeaderCell>
                                        <Table.HeaderCell>Excerpt</Table.HeaderCell>
                                        <Table.HeaderCell>Page</Table.HeaderCell>
                                        <Table.HeaderCell>Action</Table.HeaderCell>
                                    </Table.Row>
                                </Table.Header>
                                <Table.Body>
                                    {this.state.list.length === 0 ? <Table.Row><Table.Cell>This page haven't test yet, please try to test</Table.Cell></Table.Row> : this.state.list}
                                </Table.Body>
                            </Table>

                        </Segment>

                    </Segment.Group>
                </Segment.Group>
            </div>

        );
    }



}

export default spellingScreen;