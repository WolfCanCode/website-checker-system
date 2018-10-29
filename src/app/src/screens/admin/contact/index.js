import React, { Component } from 'react';
import {Segment, Button, Table, Icon} from 'semantic-ui-react'
import TableRow from './row-table';
export default class Contact extends Component {
  state = { list: [], loadingTable: false, isDisable: false, countPhone: 0, countEmail: 0 , statusNoResult:""};


  componentDidMount() {
      var comp = [];
      var statusNotFound="";
      this.setState({ loadingTable: true });
      var param = [{ "url": "https://twitter.com/hashtag/hiccupsteahouse?lang=en" },
      { "url": "https://www.facebook.com/hiccupsteahouse" },
      { "url": "https://www.instagram.com/hiccupsteahouse/" },
      { "url": "http://hiccupsteahouse.com/" },
      { "url": "https://www.orderhiccupsteahouse.com/" },
      { "url": "http://hiccupsteahouse.com/wp-content/uploads/Hiccups-TeaHouse-Menu-9-18.pdf" },
      { "url": "http://hiccupsteahouse.com/hiccups-locations/" },
      { "url": "http://hiccupsteahouse.com/contact-us/" },
      { "url": "http://hiccupsteahouse.com/careers/" },
      { "url": "http://www.churroholic.com/" },
      ];
      fetch("/api/contactDetail/lastest", {
          method: 'POST',
          headers: {
              'Accept': 'application/json',
              'Content-Type': 'application/json'
          },
          body: JSON.stringify(param)
      }).then(response => response.json()).then((data) => {
          comp = data.map((item, index) => {
              return (<TableRow key={index} phoneMail={item.phoneMail} url={item.url} type={item.type} />);
          });
          let countP = 0;
         data.map((item) => {
            if(item.type==='Phone'){ 
                countP++;
            }
            return countP;
        });
          let countE=0;
        data.map((item)=>{
          if(item.type==='Mail'){
            countE++;
          }
          return countE;
        })
        if(comp.length===0){
          statusNotFound="This page haven't test yet, please try to test";

        }
        
          this.setState({statusNoResult:statusNotFound})
         this.setState({countEmail:countE})
          this.setState({countPhone:countP})
          this.setState({ list: comp });
          this.setState({ loadingTable: false });
      });


  }
  _doContactDetailTest() {
      this.setState({ loadingTable: true, isDisable: true });
      var comp = [];
      var statusNotFound="";
      var param = [{ "url": "https://twitter.com/hashtag/hiccupsteahouse?lang=en" },
      { "url": "https://www.facebook.com/hiccupsteahouse" },
      { "url": "https://www.instagram.com/hiccupsteahouse/" },
      { "url": "http://hiccupsteahouse.com/" },
      { "url": "https://www.orderhiccupsteahouse.com/" },
      { "url": "http://hiccupsteahouse.com/wp-content/uploads/Hiccups-TeaHouse-Menu-9-18.pdf" },
      { "url": "http://hiccupsteahouse.com/hiccups-locations/" },
      { "url": "http://hiccupsteahouse.com/contact-us/" },
      { "url": "http://hiccupsteahouse.com/careers/" },
      { "url": "http://www.churroholic.com/" },
      ];
      fetch("/api/contactDetail", {
          method: 'POST',
          headers: {
              'Accept': 'application/json',
              'Content-Type': 'application/json'
          },
          body: JSON.stringify(param)
      }).then(response => response.json()).then((data) => {
          comp = data.map((item, index) => {
                

              return (<TableRow  key={index} phoneMail={item.phoneMail} url={item.url} type={item.type} />);
          });

          let countP = 0;
          data.map((item) => {
             if(item.type==='Phone'){ 
                 countP++;
             }
             return countP;
         });
           let countE=0;
         data.map((item)=>{
           if(item.type==='Mail'){
             countE++;
           }
           return countE;
         })
        //  var 
        //  listTest = data.map((item, index)=>{
        //    return item.url;
        //  });

        if(comp.length===0){
          statusNotFound="No Contact Detail Found";

        }
        
          this.setState({statusNoResult:statusNotFound})
         
          this.setState({countEmail:countE})
           this.setState({countPhone:countP})
          this.setState({ list: comp });
          this.setState({ loadingTable: false });
          this.setState({ isDisable: false });
      });
  }
  render() {
    return (
      <div >
          <Segment.Group horizontal  style={{margin:0}} >
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
                  <p style={{ fontSize: 24 }}>{isNaN(this.state.countPhone)?0:(this.state.countPhone)} <br />
                    Phone Numbers</p>
                </Segment>
                <Segment style={{ margin: 'auto', textAlign: 'center', padding: 0 }}>
                  <Icon name="mail" size='huge' />
                </Segment>
                <Segment>
                  <p style={{ fontSize: 24 }}>{isNaN(this.state.countEmail)?0:(this.state.countEmail)}<br /> Emails</p>
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