import React, { Component } from 'react';
import { Segment, Button, SegmentGroup, Table, Input, Icon } from 'semantic-ui-react'
import TableRow from './row-table';

export default class JavascriptErrorScreen extends Component {
  state = { list: [], loadingTable: false, isDisable: false };

  componentDidMount() {
    var comp = [];
    this.setState({ loadingTable: true });
    var param = [];
    fetch("/api/jsTest/lastest", {
      method: 'POST',
      headers: {
        'Accept': 'application/json',
        'Content-Type': 'application/json'
      },
      body: JSON.stringify(param)
    }).then(response => response.json()).then((data) => {
      if (data.length !== 0) {
        comp = data.map((item, index) => {
          var msg = item.messages.replace("-", "");
          msg = msg.replace(msg.split(" ")[0], "");
          var messages = msg.split(" at");
          return (<TableRow key={index} page={item.page} type={item.type} messages={messages} />);
        });
        this.setState({ list: comp });
      }
      this.setState({ loadingTable: false });
    });

  }

  _doJSTest() {
    this.setState({ loadingTable: true });
    this.setState({ isDisable: true });
    var comp = [];
    var param = [{ "url": "http://fpt.edu.vn/" },
    { "url": "https://dcpxsuvi.com/" },
    { "url": "https://gaana.com/browser" },
    { "url": "https://tuoitre.vn/" },
    { "url": "genk.vn" }];
    fetch("/api/jsTest", {
      method: 'POST',
      headers: {
        'Accept': 'application/json',
        'Content-Type': 'application/json'
      },
      body: JSON.stringify(param)
    }).then(response => response.json()).then((data) => {
      if (data.length !== 0) {
        comp = data.map((item, index) => {
          var msg = item.messages.replace("-", "");
          msg = msg.replace(msg.split(" ")[0], "");
          var messages = msg.split(" at");
          return (<TableRow key={index} page={item.page} type={item.type} messages={messages} />);
        });
        this.setState({ list: comp });
      }
      this.setState({ loadingTable: false });

    });
  }
  render() {
    return (
      <SegmentGroup vertical='true'>
        <Segment.Group horizontal>

          <Segment basic loading={this.state.loadingTable}>
            <Button icon labelPosition='right' disabled={this.state.isDisable} onClick={() => this._doJSTest()}>
              Check
                       <Icon name='right arrow' />
            </Button>
            <Button style={{ marginLeft: '10px' }} floated='right'><Icon name="print" />Export</Button>
            <div style={{ marginBottom: '10px', float: 'right' }}>
              <Input icon='search' placeholder='Search...' />
            </div>
            <Table singleLine style={{ fontSize: '14px' }}>
              <Table.Header>
                <Table.Row>
                  <Table.HeaderCell>Error Message</Table.HeaderCell>
                  <Table.HeaderCell>Type</Table.HeaderCell>
                  <Table.HeaderCell>Pages</Table.HeaderCell>
                </Table.Row>
              </Table.Header>
              {this.state.list.length === 0 ? <Table.Body><Table.Row><Table.Cell>This page haven't test yet, please try to test</Table.Cell></Table.Row></Table.Body> : this.state.list}
            </Table>
          </Segment>
        </Segment.Group>
      </SegmentGroup>
    );
  }
}