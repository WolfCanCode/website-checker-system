import React, { Component } from 'react';
import { Segment, Button, Table, Icon, Dropdown } from 'semantic-ui-react'
import TableRow from './row-table';
import { Cookies } from "react-cookie";
import ReactToExcel from "react-html-table-to-excel";

const cookies = new Cookies();
export default class Contact extends Component {
  state = {
    list: [],
    loadingTable: false,
    isDisable: false,
    countPhone: 0,
    countEmail: 0,
    statusNoResult: "",
    isDoneTest: false,
    listReportId: [],
    dateOption: [],
    dateValue: null
  };


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

    fetch("/api/contactDetail/getHistoryList", {
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
          list.push({ phoneMail: data.contactReport[i].phoneMail, url: [data.contactReport[i].url], type: data.contactReport[i].type });
        }

      }
      console.log(list);
      comp = list.map((item, index) => {
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

  _changeDate(event, data) {
    var statusNotFound = "";
    this.setState({ dateValue: data.value, loadingTable: true });
    var comp = [];
    fetch("/api/contactDetail/getHistoryReport", {
      method: 'POST',
      headers: {
        'Accept': 'application/json',
        'Content-Type': 'application/json'
      },
      body: JSON.stringify({ reportTime: data.value })
    }).then(response => response.json()).then((res) => {
      var list = [];  // 2620184173 , [https://thanhnien.vn/van-hoa/,https://thanhnien.vn/the-gioi/quan-su/]

      var checkExist = false;
      var pos = 0;

      for (let i = 0; i < res.data.length; i++) {
        for (let j = 0; j < list.length; j++) {
          pos = j;
          if (list[j].phoneMail === res.data[i].phoneMail) {
            checkExist = true;
            break;
          }
        }

        if (checkExist) {
          checkExist = false;

          list[pos].url.push(res.data[i].url);
        }

        else {
          list.push({ phoneMail: res.data[i].phoneMail, url: [res.data[i].url], type: res.data[i].type });
        }

      }
      console.log(list);
      comp = list.map((item, index) => {
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

  _clickUpdateListDate() {
    var param = {
      "userId": cookies.get("u_id"),
      "userToken": cookies.get("u_token"),
      "websiteId": cookies.get("u_w_id"),
      "pageOptionId": cookies.get("u_option"),
    }

    fetch("/api/contactDetail/getHistoryList", {
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

  _doContactDetailTest() {
    this.setState({ loadingTable: true, isDisable: true });
    var comp = [];
    var listReport = [];
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
        listReport.push(data.contactReport[i].id);
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
          list.push({ phoneMail: data.contactReport[i].phoneMail, url: [data.contactReport[i].url], type: data.contactReport[i].type });
        }

      }
      console.log(list);
      comp = list.map((item, index) => {
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
      if (comp.length !== 0) {
        this.setState({ isDoneTest: true });
      }

      this.setState({ statusNoResult: statusNotFound })

      this.setState({ countEmail: countE })
      this.setState({ countPhone: countP })
      this.setState({ list: comp });
      this.setState({ loadingTable: false });
      this.setState({ isDisable: false });
      this.setState({ listReportId: listReport });
    });
  }

  _saveReport() {
    this.setState({ loadingTable: true });
    this.setState({ isDisable: true });
    var comp = [];
    var param = {
      "userId": cookies.get("u_id"),
      "userToken": cookies.get("u_token"),
      "websiteId": cookies.get("u_w_id"),
      "pageOptionId": cookies.get("u_option"),
      "listReportId": this.state.listReportId
    };

    fetch("/api/contactDetail/saveReport", {
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
          list.push({ phoneMail: data.contactReport[i].phoneMail, url: [data.contactReport[i].url], type: data.contactReport[i].type });
        }

      }
      console.log(list);
      comp = list.map((item, index) => {
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




      this.setState({ countEmail: countE })
      this.setState({ countPhone: countP })
      this.setState({ list: comp });
      this.setState({
        loadingTable: false,
        isDisable: false,
        isDoneTest: false
      });

    });
  }
  render() {
    return (
      <div >
        <Segment.Group horizontal style={{ margin: 0 }} >
          <Segment basic>


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

            <Segment loading={this.state.loadingTable} basic>
              <Button icon primary labelPosition='right' disabled={this.state.isDisable} onClick={() => this._doContactDetailTest()}>
                Check
                       <Icon name='right arrow' />
              </Button>
              {this.state.isDoneTest ? <Button icon color="green" labelPosition='right' onClick={() => this._saveReport()}>
                Save <Icon name='check' />
              </Button> : ""}
              <div style={{ marginBottom: '10px', float: 'right' }}>

                <Dropdown style={{ marginRight: 10, zIndex: 9999 }} placeholder='Select history' selection defaultValue={this.state.dateValue} options={this.state.dateOption} value={this.state.dateValue} onClick={() => this._clickUpdateListDate()} onChange={(event, data) => this._changeDate(event, data)} />

                <ReactToExcel
                  className="btn1"
                  table="table-to-xls"
                  filename="contact_test_file"
                  sheet="sheet 1"
                  buttonText={<Button color="green" ><Icon name="print" />Export</Button>}
                />
              </div>
              <Table singleLine id="table-to-xls">
                <Table.Header>
                  <Table.Row>
                    <Table.HeaderCell>Contact</Table.HeaderCell>
                    <Table.HeaderCell>Page affected</Table.HeaderCell>
                    {/* <Table.HeaderCell>Action</Table.HeaderCell> */}
                  </Table.Row>
                </Table.Header>
                <Table.Body>
                  {this.state.list.length === 0 ? <Table.Row><Table.Cell>{this.state.statusNoResult}</Table.Cell></Table.Row> : this.state.list}
                </Table.Body>
              </Table>
            </Segment>

          </Segment>
        </Segment.Group>
      </div>
    );
  }
}