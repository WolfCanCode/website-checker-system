import React, { Component } from 'react';
import 'semantic-ui-css/semantic.min.css';
import { Segment, Button, Table, Icon, Dropdown } from 'semantic-ui-react'
import TableRow from './row-table';
import { Cookies } from "react-cookie";
import ReactToExcel from "react-html-table-to-excel";

const cookies = new Cookies();








class faviconScreen extends Component {
    state = {
        list: [],
        loadingTable: false,
        isDisable: false,
        faviconCount: 0,
        faviconMissingCount: 0,
        isDoneTest: false,
        listReportID: [],
        dateOption: [],
        dateValue: null
    };
    componentDidMount() {
        var comp = [];
        var listCom = [];
        var favUrlCount = 0;
        var faviconMissCount = 0;
        var flag = false;
        this.setState({ loadingTable: true });
        var param = {
            "userId": cookies.get("u_id"),
            "userToken": cookies.get("u_token"),
            "websiteId": cookies.get("u_w_id"),
            "pageOptionId": cookies.get("u_option"),
        };

        fetch("/api/faviconTest/getHistoryList", {
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

        console.log("U-Option" + cookies.get("u_option"))
        fetch("/api/faviconTest/lastest", {
            method: 'POST',
            headers: {
                'Accept': 'application/json',
                'Content-Type': 'application/json'
            },
            body: JSON.stringify(param)
        }).then(response => response.json()).then((data) => {
            console.log(data);
            comp = data.favicontestReport.map((item, index) => {
                for (let i = 0; i < listCom.length; i++) {
                    if (item.faviconUrl === listCom[i]) {
                        flag = true;
                    }
                }
                if (flag === false && item.faviconUrl !== "Missing Favicon") {
                    listCom.push(item.faviconUrl);
                    favUrlCount++;
                }
                else {
                    flag = false;
                }
                if (item.faviconUrl === "Missing Favicon") {
                    faviconMissCount++;
                }

                return (<TableRow key={index} image={item.faviconUrl} url={item.faviconUrl} sizeFav={item.sizeFavicon} typeFavicon={item.typeFavicon} webAddress={item.url} />);
            });
            this.setState({ faviconMissingCount: faviconMissCount })
            this.setState({ faviconCount: favUrlCount });
            this.setState({ list: comp });
            this.setState({ loadingTable: false });
        });


    }

    _changeDate(event, data) {
        var listCom = [];
        var favUrlCount = 0;
        var faviconMissCount = 0;
        var flag = false;
        this.setState({ dateValue: data.value, loadingTable: true });
        var comp = [];
        fetch("/api/faviconTest/getHistoryReport", {
            method: 'POST',
            headers: {
                'Accept': 'application/json',
                'Content-Type': 'application/json'
            },
            body: JSON.stringify({ reportTime: data.value })
        }).then(response => response.json()).then((res) => {
            comp = res.data.map((item, index) => {
                for (let i = 0; i < listCom.length; i++) {
                    if (item.faviconUrl === listCom[i]) {
                        flag = true;
                    }
                }
                if (flag === false && item.faviconUrl !== "Missing Favicon") {
                    listCom.push(item.faviconUrl);
                    favUrlCount++;
                }
                else {
                    flag = false;
                }
                if (item.faviconUrl === "Missing Favicon") {
                    faviconMissCount++;
                }

                return (<TableRow key={index} image={item.faviconUrl} url={item.faviconUrl} sizeFav={item.sizeFavicon} typeFavicon={item.typeFavicon} webAddress={item.url} />);
            });
            this.setState({ faviconMissingCount: faviconMissCount })
            this.setState({ faviconCount: favUrlCount });
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

        fetch("/api/faviconTest/getHistoryList", {
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

    _doFaviconTest() {
        this.setState({ loadingTable: true, isDisable: true });
        var comp = [];
        var listCom = [];
        var listReport = [];
        var favUrlCount = 0;
        var flag = false;
        var faviconMissCount = 0;
        var param = {
            "userId": cookies.get("u_id"),
            "userToken": cookies.get("u_token"),
            "websiteId": cookies.get("u_w_id"),
            "pageOptionId": cookies.get("u_option"),
        };
        console.log("U-Option" + cookies.get("u_option"))
        fetch("/api/faviconTest", {
            method: 'POST',
            headers: {
                'Accept': 'application/json',
                'Content-Type': 'application/json'
            },
            body: JSON.stringify(param)
        }).then(response => response.json()).then((data) => {
            console.log(data)
            comp = data.favicontestReport.map((item, index) => {
                listReport.push(item.id)
                for (let i = 0; i < listCom.length; i++) {
                    if (item.faviconUrl === listCom[i]) {
                        flag = true;
                    }
                }
                if (flag === false && item.faviconUrl !== "Missing Favicon") {
                    listCom.push(item.faviconUrl);
                    favUrlCount++;
                }
                else {
                    flag = false;
                }
                if (item.faviconUrl === "Missing Favicon") {
                    faviconMissCount++;
                }
                return (<TableRow key={index} image={item.faviconUrl} url={item.faviconUrl} sizeFav={item.sizeFavicon} typeFavicon={item.typeFavicon} w webAddress={item.url} />);
            });
            this.setState({ faviconCount: favUrlCount });
            this.setState({ faviconMissingCount: faviconMissCount });
            this.setState({
                list: comp,
                listReportID: listReport,
                isDoneTest: true
            });

            this.setState({ loadingTable: false });
            this.setState({ isDisable: false })
        });
    }

    _saveReport() {
        this.setState({ loadingTable: true });
        this.setState({ isDisable: true });
        var comp = [];
        var listCom = [];
        var favUrlCount = 0;
        var flag = false;
        var faviconMissCount = 0;
        var param = {
            "userId": cookies.get("u_id"),
            "userToken": cookies.get("u_token"),
            "websiteId": cookies.get("u_w_id"),
            "pageOptionId": cookies.get("u_option"),
            "listReportId": this.state.listReportID
        };

        fetch("/api/faviconTest/saveReport", {
            method: 'POST',
            headers: {
                'Accept': 'application/json',
                'Content-Type': 'application/json'
            },
            body: JSON.stringify(param)
        }).then(response => response.json()).then((data) => {
            comp = data.favicontestReport.map((item, index) => {
                for (let i = 0; i < listCom.length; i++) {
                    if (item.faviconUrl === listCom[i]) {
                        flag = true;
                    }
                }
                if (flag === false && item.faviconUrl !== "Missing Favicon") {
                    listCom.push(item.faviconUrl);
                    favUrlCount++;
                }
                else {
                    flag = false;
                }
                if (item.faviconUrl === "Missing Favicon") {
                    faviconMissCount++;
                }
                return (<TableRow key={index} image={item.faviconUrl} url={item.faviconUrl} sizeFav={item.sizeFavicon} typeFavicon={item.typeFavicon} w webAddress={item.url} />);
            });



            this.setState({ faviconCount: favUrlCount });
            this.setState({ faviconMissingCount: faviconMissCount });
            this.setState({
                list: comp,
                isDoneTest: false
            });
            this.setState({ loadingTable: false });
            this.setState({ isDisable: false })
        });
    }
    render() {
        return (
            <div>
                <Segment.Group>
                    <Segment>

                    </Segment>
                    <Segment.Group horizontal >
                        <Segment style={{ margin: 'auto', textAlign: 'center', padding: 0, size: 'mini' }}>
                            <Icon className="star" size='huge' color='violet' /></Segment>
                        <Segment>
                            <p style={{ fontSize: 24 }}>{this.state.faviconCount}<br /> Different Favicons</p>
                        </Segment>

                        <Segment style={{ margin: 'auto', textAlign: 'center', padding: 0 }}>
                            <Icon className="check" size='huge' color='green' />
                        </Segment>
                        <Segment style={{ paddingLeft: '10px' }}>
                            <p style={{ fontSize: 24 }}>{this.state.faviconMissingCount}<br />
                                Pages missing Favicons</p>
                        </Segment >

                        {/* <Segment basic style={{ textAlign: 'center', margin: 'auto' }} >
                            <div ><strong >Broken Links </strong><Icon name='question circle' size='large'></Icon></div>
                            <Image src={FaviconsLink} size='medium' style={{ margin: 'auto' }} />
                        </Segment> */}

                    </Segment.Group>
                    <Segment.Group horizontal>

                        <Segment basic loading={this.state.loadingTable} style={{ minWidth: "auto" }} >
                            {/* <h4>All Favicons</h4> */}
                            {/* <Segment basic style={{ minWidth:"350px" }}>
                                <Button floated='right' ><Icon name="print" />Export</Button>

                                <Input icon='search' placeholder='Search...' />
                            </Segment> */}
                            <Segment basic>

                                <div style={{ marginBottom: '30px' }}>
                                    <Button icon primary labelPosition='right' disabled={this.state.isDisable} onClick={() => this._doFaviconTest()}>
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
                                            filename="favicons_test_file"
                                            sheet="sheet 1"
                                            buttonText={<Button color="green"><Icon name="print" />Export</Button>}
                                        />
                                    </div>

                                    {/* <Input icon='search' placeholder='Search...' style={{ float: 'right' }} /> */}
                                </div>
                            </Segment>
                            <Table unstackable singleLine textAlign='center' style={{ tableLayout: 'auto' }} loading={this.state.loadingTable} id="table-to-xls">
                                <Table.Header >
                                    <Table.Row>
                                        <Table.HeaderCell>Favicon</Table.HeaderCell>
                                        <Table.HeaderCell>URL</Table.HeaderCell>
                                        <Table.HeaderCell>Width</Table.HeaderCell>
                                        <Table.HeaderCell>Type</Table.HeaderCell>
                                        <Table.HeaderCell>Pages</Table.HeaderCell>

                                    </Table.Row>
                                </Table.Header>
                                <Table.Body>
                                    {/* <Table.Row>
                                        <Table.Cell style={{ width: '100px', whiteSpace: 'normal', wordBreak: 'break-all' }}><Image src={icon} size='tiny' style={{ margin: 'auto' }} /></Table.Cell>
                                        <Table.Cell style={{ width: '100px', whiteSpace: 'normal', wordBreak: 'break-all' }}><a href='https://react.semantic-ui.com/Contact'>www.react.semantic-ui.com/Contact</a></Table.Cell>
                                        <Table.Cell style={{ width: '100px', whiteSpace: 'normal', wordBreak: 'break-all' }}>0</Table.Cell>
                                        <Table.Cell style={{ width: '100px', whiteSpace: 'normal', wordBreak: 'break-all' }}> <Label style={{ fontSize: '13px' }} >5</Label></Table.Cell>


                                    </Table.Row> */}

                                    {this.state.list.length === 0 ? <Table.Row><Table.Cell>This page haven't test yet, please try to test</Table.Cell></Table.Row> : this.state.list}


                                </Table.Body>
                            </Table>

                        </Segment>


                        {/* <Segment basic >
                            <h4>Pages With Favicons</h4>

                            <div style={{ marginBottom: '60px', marginRight: 'auto' }}>
                                <Button floated='right' ><Icon name="print" />Export</Button>

                                <Input icon='search' placeholder='Search...' style={{ float: 'right' }} />
                            </div>
                            <Segment.Group horizontal >
                                <Segment basic>
                                    <div style={{ marginBottom: '10px', fontSize: '15px', textAlign:'center' }}>Browse Top Albums, MP3 Songs, Latest Playlists on Gaana.</div>
                                    <div><Image src={image} size='medium' style={{margin:'auto'}} /></div>

                                </Segment >
                                <Segment basic>
                                    <div style={{ marginBottom: '10px', fontSize: '15px', textAlign:'center' }}>Browse Top Albums, MP3 Songs, Latest Playlists on Gaana.</div>
                                    <div><Image src={image} size='medium' style={{margin:'auto'}} /></div>

                                </Segment>

                                <Segment basic>
                                    <div style={{ marginBottom: '10px', fontSize: '15px', textAlign:'center' }}>Browse Top Albums, MP3 Songs, Latest Playlists on Gaana.</div>
                                    <div><Image src={image} size='medium' style={{margin:'auto'}}/></div>

                                </Segment>
                            </Segment.Group>


                        </Segment> */}






                    </Segment.Group>
                </Segment.Group>
            </div>

        );
    }



}

export default faviconScreen;