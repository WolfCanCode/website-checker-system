import React, { Component } from 'react';
import { Segment, Button, Table, Icon } from 'semantic-ui-react'
import TableRow from './row-table';
import { Cookies } from "react-cookie";

const cookies = new Cookies();
export default class Contact extends Component {
  state = { list: [], loadingTable: false, isDisable: false, countPhone: 0, countEmail: 0, statusNoResult: "" };


  componentDidMount() {
    var comp = [];
    var statusNotFound = "";
    this.setState({ loadingTable: true });
    var param = {
      "userId": cookies.get("u_id"),
      "userToken": cookies.get("u_token"),
      "websiteId": cookies.get("u_w_id"),
      "pageOptionId": cookies.get("u_option"),
    }

    fetch("/api/contactDetail/lastest", {
      method: 'POST',
      headers: {
        'Accept': 'application/json',
        'Content-Type': 'application/json'
      },
      body: JSON.stringify(param)
    }).then(response => response.json()).then((data) => {
      var list = [];  // 2620184173 , [https://thanhnien.vn/van-hoa/,https://thanhnien.vn/the-gioi/quan-su/]
     
      var checkExist = false;
      var pos = 0;

      for (let i = 0; i < data.contactReport.length; i++) {
        for (let j = 0; j < list.length; j++) {
          pos = j;
          if (list[j].phoneMail === data.contactReport[i].phoneMail) {
            checkExist = true;
            break;
          }
        }
        
        if (checkExist) {
          checkExist = false;

            list[pos].url.push(data.contactReport[i].url);
          }
          
         else {
          list.push({phoneMail:data.contactReport[i].phoneMail, url:[data.contactReport[i].url],type:data.contactReport[i].type});
        }

      }
      console.log(list);
      comp =list.map((item, index) => {
        return (<TableRow key={index} phoneMail={item.phoneMail} url={item.url} type={item.type} />);
      });
      let countP = 0;
      list.map((item) => {
        if (item.type === 'Phone') {
          countP++;
        }
        return countP;
      });
      let countE = 0;
      list.map((item) => {
        if (item.type === 'Mail') {
          countE++;
        }
        return countE;
      })
      if (comp.length === 0) {
        statusNotFound = "This page haven't test yet, please try to test";

      }

      this.setState({ statusNoResult: statusNotFound })
      this.setState({ countEmail: countE })
      this.setState({ countPhone: countP })
      this.setState({ list: comp });
      this.setState({ loadingTable: false });
    });


  }
  _doContactDetailTest() {
    this.setState({ loadingTable: true, isDisable: true });
    var comp = [];
    var statusNotFound = "";
    var param = {
      "userId": cookies.get("u_id"),
      "userToken": cookies.get("u_token"),
      "websiteId": cookies.get("u_w_id"),
      "pageOptionId": cookies.get("u_option"),
    }
    fetch("/api/contactDetail", {
      method: 'POST',
      headers: {
        'Accept': 'application/json',
        'Content-Type': 'application/json'
      },
      body: JSON.stringify(param)
    }).then(response => response.json()).then((data) => {
      var list = [];  // 2620184173 , [https://thanhnien.vn/van-hoa/,https://thanhnien.vn/the-gioi/quan-su/]
     
      var checkExist = false;
      var pos = 0;

      for (let i = 0; i < data.contactReport.length; i++) {
        for (let j = 0; j < list.length; j++) {
          pos = j;
          if (list[j].phoneMail === data.contactReport[i].phoneMail) {
            checkExist = true;
            break;
          }
        }
        
        if (checkExist) {
          checkExist = false;

            list[pos].url.push(data.contactReport[i].url);
          }
          
         else {
          list.push({phoneMail:data.contactReport[i].phoneMail, url:[data.contactReport[i].url],type:data.contactReport[i].type});
        }

      }
      console.log(list);
      comp =list.map((item, index) => {
        return (<TableRow key={index} phoneMail={item.phoneMail} url={item.url} type={item.type} />);
      });
      let countP = 0;
      list.map((item) => {
        if (item.type === 'Phone') {
          countP++;
        }
        return countP;
      });
      let countE = 0;
      list.map((item) => {
        if (item.type === 'Mail') {
          countE++;
        }
        return countE;
      })
      if (comp.length === 0) {
        statusNotFound = "This page haven't test yet, please try to test";

      }

      this.setState({ statusNoResult: statusNotFound })

      this.setState({ countEmail: countE })
      this.setState({ countPhone: countP })
      this.setState({ list: comp });
      this.setState({ loadingTable: false });
      this.setState({ isDisable: false });
    });
  }
  render() {
    return (
      <div >
        <Segment.Group horizontal style={{ margin: 0 }} >
          <Segment basic>
            <Button icon labelPosition='right' disabled={this.state.isDisable} onClick={() => this._doContactDetailTest()}>
              Check
                       <Icon name='right arrow' />
            </Button>
            <Segment.Group horizontal>
              <Segment style={{ margin: 'auto', textAlign: 'center', padding: 0 }}>
                <Icon name='phone square' size='huge' />
              </Segment>
              <Segment>
                <p style={{ fontSize: 24 }}>{isNaN(this.state.countPhone) ? 0 : (this.state.countPhone)} <br />
                  Phone Numbers</p>
              </Segment>
              <Segment style={{ margin: 'auto', textAlign: 'center', padding: 0 }}>
                <Icon name="mail" size='huge' />
              </Segment>
              <Segment>
                <p style={{ fontSize: 24 }}>{isNaN(this.state.countEmail) ? 0 : (this.state.countEmail)}<br /> Emails</p>
              </Segment>
            </Segment.Group>
            <Button style={{ marginBottom: '10px' }} floated='right'><Icon name="print" />Export</Button>
            <Table singleLine>
              <Table.Header>
                <Table.Row>
                  <Table.HeaderCell>Contact</Table.HeaderCell>
                  <Table.HeaderCell>Page affected</Table.HeaderCell>
                  <Table.HeaderCell>Action</Table.HeaderCell>
                </Table.Row>
              </Table.Header>
              <Table.Body>
                {this.state.list.length === 0 ? <Table.Row><Table.Cell>{this.state.statusNoResult}</Table.Cell></Table.Row> : this.state.list}
              </Table.Body>
            </Table>
            {/* <Table singleLine>
                <Table.Header>
                  <Table.Row>
                    <Table.HeaderCell>Email</Table.HeaderCell>
                    <Table.HeaderCell>Page affected</Table.HeaderCell>
                    <Table.HeaderCell>Action</Table.HeaderCell>
                  </Table.Row>
                </Table.Header>
                <Table.Body>
                  <Table.Row>
                    <Table.Cell style={{ width: '100px', whiteSpace: 'normal', wordBreak: 'break-all' }}>franchising@hiccupsteahouse.com</Table.Cell>
                    <Table.Cell style={{ width: '100px', whiteSpace: 'normal', wordBreak: 'break-all' }}> 3 <Icon name="clone" /></Table.Cell>
                    <Table.Cell style={{ width: '100px', whiteSpace: 'normal', wordBreak: 'break-all' }}><Button>Edit</Button></Table.Cell>

                  </Table.Row>
                  <Table.Row>
                    <Table.Cell style={{ width: '100px', whiteSpace: 'normal', wordBreak: 'break-all' }}>franchising@hiccupsteahouse.com</Table.Cell>
                    <Table.Cell style={{ width: '100px', whiteSpace: 'normal', wordBreak: 'break-all' }}> 2 <Icon name="clone" /></Table.Cell>
                    <Table.Cell style={{ width: '100px', whiteSpace: 'normal', wordBreak: 'break-all' }}><Button>Edit</Button></Table.Cell>
                  </Table.Row>
                </Table.Body>
              </Table> */}

          </Segment>
          {/* <Segment basic>
                            
                        </Segment> */}
        </Segment.Group>
      </div>
    );
  }
}