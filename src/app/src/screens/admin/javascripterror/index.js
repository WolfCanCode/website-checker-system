import React, { Component } from 'react';
import { Segment, Button, SegmentGroup, Table, Input, Icon } from 'semantic-ui-react'
import TableRow from './row-table';
import { Cookies } from "react-cookie";
import ReactToExcel from "react-html-table-to-excel";

const cookies = new Cookies();
export default class JavascriptErrorScreen extends Component {
  state = {
    list: [], loadingTable: false, isDisable: false, listReportId: [], isDoneTest: false, messages: "This page haven't test yet, please try to test"
  };

  componentDidMount() {
    var comp = [];
    this.setState({ loadingTable: true });

    var param = {
      "userId": cookies.get("u_id"),
      "userToken": cookies.get("u_token"),
      "websiteId": cookies.get("u_w_id"),
      "pageOptionId": cookies.get("u_option"),
    };
    fetch("/api/jsTest/lastest", {
      method: 'POST',
      headers: {
        'Accept': 'application/json',
        'Content-Type': 'application/json'
      },
      body: JSON.stringify(param)
    }).then(response => response.json()).then((data) => {
      console.log(data)
      // if (data.jsErrorReport.length !== 0) {
      if (data.jsErrorReport !== null) {
        comp = data.jsErrorReport.map((item, index) => {
          // var msg = item.messages.replace(item.messages.split(" ")[0], "");
          // alert(msg);
          return (<TableRow key={index} page={item.pages} type={item.type} messages={item.messages} />);
        });
      }
      this.setState({ list: comp });
      // }
      this.setState({ loadingTable: false });
    });

  }

  _doJSTest() {
    this.setState({ loadingTable: true });
    this.setState({ isDisable: true });
    var comp = [];
    var param = {
      "userId": cookies.get("u_id"),
      "userToken": cookies.get("u_token"),
      "websiteId": cookies.get("u_w_id"),
      "pageOptionId": cookies.get("u_option"),
    };

    fetch("/api/jsTest", {
      method: 'POST',
      headers: {
        'Accept': 'application/json',
        'Content-Type': 'application/json'
      },
      body: JSON.stringify(param)
    }).then(response => response.json()).then((data) => {
      console.log(data.jsErrorReport)
      // if (data.jsErrorReport.length !== 0) {
      var listReport = [];
      if (data.jsErrorReport.length === 0) {
        this.setState({ list: comp, loadingTable: false, isDisable: false, messages: "The selected pageoption don't have any page with js error" });
      } else {
        comp = data.jsErrorReport.map((item, index) => {
          listReport.push(item.id);

          // var msg = item.messages.replace("-", "");
          // msg = msg.replace(msg.split(" ")[0], "");
          // if (data.type !== "WARNING") {
          //   var messages = msg.split(" at");
          // }
          return (<TableRow key={index} page={item.pages} type={item.type} messages={item.messages} />);
        });
        this.setState({ list: comp, listReportId: listReport, loadingTable: false, isDoneTest: true, isDisable: false });

      }
    });
  }

  _saveReport() {
    this.setState({ loadingTable: true });
    this.setState({ isDisable: true });
    var param = {
      "userId": cookies.get("u_id"),
      "userToken": cookies.get("u_token"),
      "websiteId": cookies.get("u_w_id"),
      "pageOptionId": cookies.get("u_option"),
      "listReportId": this.state.listReportId
    };

    fetch("/api/jsTest/saveReport", {
      method: 'POST',
      headers: {
        'Accept': 'application/json',
        'Content-Type': 'application/json'
      },
      body: JSON.stringify(param)
    }).then(response => response.json()).then((data) => {
      var comp = data.jsErrorReport.map((item, index) => {
        return (<TableRow key={index} page={item.pages} type={item.type} messages={item.messages} />);
      });
      this.setState({ list: comp, loadingTable: false, isDoneTest: false, isDisable: false });

    });
  }


  render() {
    return (
      <SegmentGroup vertical='true'>
        <Segment.Group horizontal>

          <Segment basic loading={this.state.loadingTable}>
            <Button icon primary labelPosition='right' disabled={this.state.isDisable} onClick={() => this._doJSTest()}>
              Check
                       <Icon name='right arrow' />
            </Button>
            {this.state.isDoneTest ? <Button icon color="green" labelPosition='right' onClick={() => this._saveReport()}>
              Save <Icon name='check' />
            </Button> : ""}
            <div style={{ marginBottom: '10px', float: 'right' }}>


              <ReactToExcel
                className="btn1"
                table="table-to-xls"
                filename="js_test_file"
                sheet="sheet 1"
                buttonText={<Button color="green"><Icon name="print" />Export</Button>}
              />
            </div>
            <div style={{ marginBottom: '10px', float: 'right' }}>
              <Input icon='search' placeholder='Search...' />
            </div>
            <Table unstackable singleLine style={{ fontSize: '14px' }} id="table-to-xls">
              <Table.Header>
                <Table.Row>
                  <Table.HeaderCell>Error Message</Table.HeaderCell>
                  <Table.HeaderCell>Type</Table.HeaderCell>
                  <Table.HeaderCell>Pages</Table.HeaderCell>
                </Table.Row>
              </Table.Header>
              <Table.Body>
                {this.state.list.length === 0 ? <Table.Row><Table.Cell>{this.state.messages}</Table.Cell></Table.Row> : this.state.list}
              </Table.Body>
            </Table>
          </Segment>
        </Segment.Group>
      </SegmentGroup>
    );
  }
}