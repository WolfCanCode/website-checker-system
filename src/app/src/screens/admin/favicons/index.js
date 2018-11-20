import React, { Component } from 'react';
import 'semantic-ui-css/semantic.min.css';
import { Segment, Button, Table, Icon, Input } from 'semantic-ui-react'
import TableRow from './row-table';
import { Cookies } from "react-cookie";

const cookies = new Cookies();








class faviconScreen extends Component {
    state = { list: [], loadingTable: false, isDisable: false, faviconCount: 0, faviconMissingCount: 0 };
    //"http://hiccupsteahouse.com/","http://hiccupsteahouse.com/franchising/","http://hiccupsteahouse.com/hiccups-locations/"
    //"https://vnexpress.net/", "https://vnexpress.net/tin-tuc/thoi-su", "https://vnexpress.net/tin-tuc/goc-nhin", "https://giaitri.vnexpress.net/"
    //"https://insites.com/" , "https://insites.com/tests/","https://insites.com/content/redirections/"
    //"https://thanhnien.vn","https://thanhnien.vn/thoi-su/" "https://vnexpress.net/", "https://vnexpress.net/tin-tuc/thoi-su", "https://vnexpress.net/tin-tuc/goc-nhin", "https://giaitri.vnexpress.net/"


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
        console.log("U-Option"+cookies.get("u_option"))
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

                return (<TableRow key={index} image={item.faviconUrl} url={item.faviconUrl} sizeFav={item.sizeFavicon} webAddress={item.url} />);
            });
            this.setState({ faviconMissingCount: faviconMissCount })
            this.setState({ faviconCount: favUrlCount });
            this.setState({ list: comp });
            this.setState({ loadingTable: false });
        });


    }
    _doFaviconTest() {
        this.setState({ loadingTable: true, isDisable: true });
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
        };
        console.log("U-Option"+cookies.get("u_option"))
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
                return (<TableRow key={index} image={item.faviconUrl} url={item.faviconUrl} sizeFav={item.sizeFavicon} webAddress={item.url} />);
            });
            this.setState({ faviconCount: favUrlCount });
            this.setState({ faviconMissingCount: faviconMissCount });
            this.setState({ list: comp });
            this.setState({ loadingTable: false });
            this.setState({ isDisable: false })
        });
    }
    render() {
        return (
            <div>
                <Segment.Group>
                    <Segment>
                        <Button icon labelPosition='right' disabled={this.state.isDisable} onClick={() => this._doFaviconTest()}>
                            Check
                       <Icon name='right arrow' />
                        </Button>
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

                        <Segment basic style={{ minWidth: "auto" }}>
                            <h4>All Favicons</h4>
                            {/* <Segment basic style={{ minWidth:"350px" }}>
                                <Button floated='right' ><Icon name="print" />Export</Button>

                                <Input icon='search' placeholder='Search...' />
                            </Segment> */}
                            <Segment basic>

                                <div style={{ marginBottom: '50px' }}>

                                    <Button style={{ float: 'right' }}><Icon name="print" />Export</Button>

                                    <Input icon='search' placeholder='Search...' style={{ float: 'right' }} />
                                </div>
                            </Segment>
                            <Table unstackable singleLine textAlign='center' style={{ tableLayout: 'auto' }} loading={this.state.loadingTable}>
                                <Table.Header >
                                    <Table.Row>
                                        <Table.HeaderCell>Favicon</Table.HeaderCell>
                                        <Table.HeaderCell>URL</Table.HeaderCell>
                                        <Table.HeaderCell>Width</Table.HeaderCell>
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