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
          return (<TableRow key={index} page={item.page} type={item.type} messages={item.messages} />);
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
        { "url": "https://gaana.com/discover" },
        { "url": "https://gaana.com/browser" },
        { "url": "https://tuoitre.vn/" },
        { "url": "https://www.dcpxsuvi.com/t%E1%BA%A5t-c%E1%BA%A3-c%C3%A1c-s%E1%BA%A3n-ph%E1%BA%A9m/51/da-gi%E1%BA%A3.catalog" }];
    fetch("/api/jsTest", {
      method: 'POST',
      headers: {
        'Accept': 'application/json',
        'Content-Type': 'application/json'
      },
      body: JSON.stringify(param)
    }).then(response => response.json()).then((data) => {
      comp = data.map((item, index) => {
        return (<TableRow key={index} page={item.page} type={item.type} messages={item.messages} />);
      });

      this.setState({ list: comp });
      this.setState({ loadingTable: false });
      this.setState({ isDisable: false });

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
              <Table.Body>
                {this.state.list.length === 0 ? <Table.Row><Table.Cell>This page haven't test yet, please try to test</Table.Cell></Table.Row> : this.state.list}

              </Table.Body>
            </Table>
          </Segment>
          {/* <Segment basic>
                            
                        </Segment> */}
        </Segment.Group>
      </SegmentGroup>
    );
  }
}