import React, { Component } from 'react';
import {Segment, Button, SegmentGroup, Table, Input, Icon } from 'semantic-ui-react'
import TableRow from'../pages/row-table';
export default class Pages extends Component {
    state = { list: [], loadingTable: false,  isDisable: false  };


    componentDidMount() {
        var comp = [];
        this.setState({ loadingTable: true });
        var param = [{ "url": "https://twitter.com/hashtag/hiccupsteahouse?lang=en" },
                    { "url": "https://www.facebook.com/hiccupsteahouse"},
                    { "url": "https://www.instagram.com/hiccupsteahouse/" },
                    { "url": "http://hiccupsteahouse.com/" },
                    { "url": "https://www.orderhiccupsteahouse.com/" },
                    { "url": "http://hiccupsteahouse.com/wp-content/uploads/Hiccups-TeaHouse-Menu-9-18.pdf" },
                    { "url": "http://hiccupsteahouse.com/hiccups-locations/" },
                    { "url": "http://hiccupsteahouse.com/contact-us/" },
                    { "url": "http://hiccupsteahouse.com/careers/" },
                    { "url": "http://www.churroholic.com/"},
        ];
        fetch("/api/pagestest/lastest", {
            method: 'POST',
            headers: {
                'Accept': 'application/json',
                'Content-Type': 'application/json'
            },
            body: JSON.stringify(param)
        }).then(response => response.json()).then((data) => {
            comp = data.map((item, index) => {
                return (<TableRow key={index} url={item.url} titleWeb={item.titleWeb}  canonicalUrl={item.canonicalUrl} httpCode={item.httpCode} />);
            });
            this.setState({ list: comp });
            this.setState({ loadingTable: false });
        });


    }
    _doPageTest() {
        this.setState({ loadingTable: true,  isDisable: true  });
        var comp = [];
        var param = [{ "url": "https://twitter.com/hashtag/hiccupsteahouse?lang=en" },
                    { "url": "https://www.facebook.com/hiccupsteahouse"},
                    { "url": "https://www.instagram.com/hiccupsteahouse/" },
                    { "url": "http://hiccupsteahouse.com/" },
                    { "url": "https://www.orderhiccupsteahouse.com/" },
                    { "url": "http://hiccupsteahouse.com/wp-content/uploads/Hiccups-TeaHouse-Menu-9-18.pdf" },
                    { "url": "http://hiccupsteahouse.com/hiccups-locations/" },
                    { "url": "http://hiccupsteahouse.com/contact-us/" },
                    { "url": "http://hiccupsteahouse.com/careers/" },
                    { "url": "http://www.churroholic.com/"},
        ];
        fetch("/api/pagestest", {
            method: 'POST',
            headers: {
                'Accept': 'application/json',
                'Content-Type': 'application/json'
            },
            body: JSON.stringify(param)
        }).then(response => response.json()).then((data) => {
            comp = data.map((item, index) => {
                return (<TableRow key={index} url={item.url} titleWeb={item.titleWeb}  canonicalUrl={item.canonicalUrl} httpCode={item.httpCode} />);
            });

            this.setState({ list: comp });
            this.setState({ loadingTable: false });
        });
    }

    render() {
        return (
            <div >
                <SegmentGroup vertical='true'>
                    <Segment><h3>Pages Test</h3>  
                    <Button icon labelPosition='right' disabled={this.state.isDisable} onClick={()=>this._doPageTest()}>
                        Check
                       <Icon name='right arrow' />
                    </Button>
                    <div style={{ float: 'right', marginBottom: '10px' }}>
                                <Input icon='search' placeholder='Search...' />
                            </div>


                            <Button style={{ marginRight: '10px' }} floated='right'><Icon name="print" />Export</Button></Segment>
                    <Segment.Group horizontal style={{maxHeight:450, overflow:"auto" }}>
                        <Segment basic loading={this.state.loadingTable}>
                            

                            <Table singleLine>
                                <Table.Header>
                                    <Table.Row>
                                        <Table.HeaderCell>Title</Table.HeaderCell>
                                        <Table.HeaderCell>Web Address</Table.HeaderCell>
                                        <Table.HeaderCell>Canonical URL</Table.HeaderCell>
                                        <Table.HeaderCell>HTTP</Table.HeaderCell>
                                        {/* <Table.HeaderCell>Last checked</Table.HeaderCell> */}
                                    </Table.Row>
                                </Table.Header>
                                <Table.Body>
                                {this.state.list.length === 0 ? <b>This page haven't test yet, please try to test</b> : this.state.list}

                                </Table.Body>
                            </Table>


                        </Segment>
                        {/* <Segment basic>
                            
                        </Segment> */}
                    </Segment.Group>
                </SegmentGroup>
            </div>
        );
    }
}