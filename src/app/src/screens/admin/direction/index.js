import React, { Component } from 'react';
import { Segment, Button, Table, Input, Icon } from 'semantic-ui-react'
import TableRow from './row-table';
export default class Direction extends Component {
    state = { list: [], loadingTable: false, isDisable: false };


    componentDidMount() {
        var comp = [];
        this.setState({ loadingTable: true });
        var param = [{ "url": "https://twitter.com/hashtag/hiccupsteahouse?lang=en" },
        { "url": "http://www.cungmua.vn" },
        { "url": "http://www.google.com" },
        { "url": "http://www.apple.com/us/shop/go/bag" },
        { "url": "http://www.facebook.com" },
        { "url": "https://hiccupsteahouse.com/contact-us/" },
        { "url": "https://hiccupsteahouse.com/careers/" },
        { "url": "https://www.churroholic.com/" },
        { "url": "https://www.apple.com/us/shop/goto/giftcards" },
        { "url": "http://www.apple.com/today/camp/" },
        { "url": "https://www.apple.com/retail/camp/notify.html" },
        { "url": "https://www.apple.com/us/shop/goto/account" },

        ];
        fetch("/api/redirectiontest/lastest", {
            method: 'POST',
            headers: {
                'Accept': 'application/json',
                'Content-Type': 'application/json'
            },
            body: JSON.stringify(param)
        }).then(response => response.json()).then((data) => {
            comp = data.map((item, index) => {
                return (<TableRow key={index} webAddress={item.url} redirectUrl={item.driectToUrl} type={item.type} httpcode={item.httpCode} />);
            });
            this.setState({ list: comp });
            this.setState({ loadingTable: false });
        });


    }
    _doLinkRedirection() {
        this.setState({ loadingTable: true, isDisable: true });
        var comp = [];
        var param = [{ "url": "https://twitter.com/hashtag/hiccupsteahouse?lang=en" },
        { "url": "http://www.cungmua.vn" },
        { "url": "http://www.google.com" },
        { "url": "http://www.apple.com/us/shop/go/bag" },
        { "url": "http://www.facebook.com" },
        { "url": "https://hiccupsteahouse.com/contact-us/" },
        { "url": "https://hiccupsteahouse.com/careers/" },
        { "url": "https://www.churroholic.com/" },
        { "url": "https://www.apple.com/us/shop/goto/giftcards" },
        { "url": "http://www.apple.com/today/camp/" },
        { "url": "https://www.apple.com/retail/camp/notify.html" },
        { "url": "https://www.apple.com/us/shop/goto/account" },

        ];
        fetch("/api/redirectiontest", {
            method: 'POST',
            headers: {
                'Accept': 'application/json',
                'Content-Type': 'application/json'
            },
            body: JSON.stringify(param)
        }).then(response => response.json()).then((data) => {
            comp = data.map((item, index) => {
                return (<TableRow key={index} webAddress={item.url} redirectUrl={item.driectToUrl} type={item.type} httpcode={item.httpCode} />);
            });

            this.setState({ list: comp });
            this.setState({ loadingTable: false });
        });
    }
    render() {
        return (

            <Segment.Group horizontal style={{margin:0}}>

                <Segment basic loading={this.state.loadingTable} >
                    <Button icon labelPosition='right' disabled={this.state.isDisable} onClick={() => this._doLinkRedirection()}>
                        Check
                       <Icon name='right arrow' />
                    </Button>
                    <div style={{ float: 'right', marginBottom: '10px' }}>
                        <Input icon='search' placeholder='Search...' />
                    </div>
                    <Button style={{ marginRight: '10px' }} floated='right'><Icon name="print" />Export</Button>
                    <Table singleLine>
                        <Table.Header>
                            <Table.Row>
                                <Table.HeaderCell>Web Address</Table.HeaderCell>
                                <Table.HeaderCell>Directs to</Table.HeaderCell>
                                <Table.HeaderCell>Type</Table.HeaderCell>
                                <Table.HeaderCell>Code</Table.HeaderCell>
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
        );
    }
}