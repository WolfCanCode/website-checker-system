import React, { Component } from 'react';
import 'semantic-ui-css/semantic.min.css';
import {Segment, Button, Table, Icon, Input,  } from 'semantic-ui-react'
 import { Cookies } from "react-cookie";
 import TableRow from './row-table';


 const cookies = new Cookies();


class mobileLayoutScreen extends Component {
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
        fetch("/api/mobileLayoutTest/lastest", {
            method: 'POST',
            headers: {
                'Accept': 'application/json',
                'Content-Type': 'application/json'
            },
            body: JSON.stringify(param)
        }).then(response => response.json()).then((data) => {
            comp = data.mobileLayoutTestReport.map((item, index) => {
                return (<TableRow key={index} url={item.url} title={item.title} screenShot={item.screenShot} issue={item.issue} />);
            });
            this.setState({ list: comp });
            this.setState({ loadingTable: false });
        });


    }

    _doMobileLayoutTest() {
        var comp = [];
        this.setState({ loadingTable: true, isDisable: true });
        var param = {
            "userId": cookies.get("u_id"),
            "userToken": cookies.get("u_token"),
            "websiteId": cookies.get("u_w_id"),
            "pageOptionId": cookies.get("u_option"),
        }

        fetch("/api/mobileLayoutTest", {
            method: 'POST',
            headers: {
                'Accept': 'application/json',
                'Content-Type': 'application/json'
            },
            body: JSON.stringify(param)
        }).then(response => response.json()).then((data) => {
            comp = data.mobileLayoutTestReport.map((item, index) => {
                return (<TableRow key={index} url={item.url} title={item.title} screenShot={item.screenShot} issue={item.issue} />);
            });
            this.setState({ list: comp });
            this.setState({ loadingTable: false, isDisable: false });
        });

    }

    render() {
        return (
           
            <Segment.Group horizontal style={{ margin: 0 }}>

            <Segment basic loading={this.state.loadingTable} >
                <Button icon labelPosition='right' disabled={this.state.isDisable} onClick={() => this._doMobileLayoutTest()}>
                    Check
                   <Icon name='right arrow' />
                </Button>
                
                <Button style={{ marginLeft: '10px' }} floated='right'><Icon name="print" />Export</Button>
        <div style={{ marginBottom: '10px', float: 'right' }}>
          <Input icon='search' placeholder='Search...' />
        </div>
                <Table singleLine unstackable  style={{ fontSize: '16px',  }}>
                    <Table.Header style={{ textAlign :'center'  }} >
                        <Table.Row>
                            <Table.HeaderCell>ScreenShot</Table.HeaderCell>
                            <Table.HeaderCell style={{textAlign :'left' }}>Page</Table.HeaderCell>
                            <Table.HeaderCell>Issue</Table.HeaderCell>
                            

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

export default mobileLayoutScreen;