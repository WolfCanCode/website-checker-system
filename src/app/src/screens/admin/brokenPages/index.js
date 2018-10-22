import React, { Component } from 'react';
import 'semantic-ui-css/semantic.min.css';
import { Segment, Button, Table,Icon, Input } from 'semantic-ui-react'
import TableRow from './row-table';






class brokenPagesScreen extends Component {

    state = { list: [], loadingTable: false, isDisable: false };


    componentDidMount() {
        var comp = [];
        this.setState({ loadingTable: true });
        var param = [];
        fetch("/api/brokenPage/lastest", {
            method: 'POST',
            headers: {
                'Accept': 'application/json',
                'Content-Type': 'application/json'
            },
            body: JSON.stringify(param)
        }).then(response => response.json()).then((data) => {
            comp = data.map((item, index) => {
                return (<TableRow key={index} urlPage={item.urlPage} stt={item.stt} httpCode={item.httpCode} httpMessage={item.httpMessage} />);
            });
            this.setState({ list: comp });
            this.setState({ loadingTable: false });
        });


    }

    _doBrokenPage() {
        var comp = [];
        this.setState({ loadingTable: true, isDisable: true });
        var param = [{ "url": "https://www.nottingham.ac.uk/about/campuses/campuses.aspx" },
        { "url": "https://www.nottingham.ac.uk/about/visitorinformation/information.aspx" },
        { "url": "http://info.block.vn/course-quality-review/" },
        { "url": "http://..info.block.vn/course-quality-review/" },
        { "url": "htt://info.block.vn/course-quality-review/" },
        { "url": "http://fpt.edu.vn/tin-tuc/25509/pv-doc-quyen-hotboy-con-lai-viet-nga-bong-noi-len-sau-mot-dem-gia-gai-nhung-toi-van-chuan-men" },
        { "url": "http://fpt.edu.vn/tin-tuc/24167/kho%3fi-da%60u-tu%60-con-so%C2%B4-0" },
        { "url": "http://fpt.edu.vn/tin-tuc/241" },
        { "url": "http://www.neucoin.org/en/" },
        { "url": "https://www.nottingham.ac.uk/about/facts/factsandfigures.aspx" },
        { "url": "https://www.nottingham.ac.uk/about/structure/universitystructure.aspx" },
        { "url": "https://www.nottingham.ac.uk/about/structure/universitystructure1.aspx" },
        { "url": "https://www.nottingham.ac.uk/about/facts/factsandfigures1.aspx" },
        { "url": "https://www.nottingham.ac.uk/about/keydates/index1.aspx" },

        ];
        fetch("/api/brokenPage", {
            method: 'POST',
            headers: {
                'Accept': 'application/json',
                'Content-Type': 'application/json'
            },
            body: JSON.stringify(param)
        }).then(response => response.json()).then((data) => {
            comp = data.map((item, index) => {
                return (<TableRow key={index} urlPage={item.urlPage} stt={item.stt} httpCode={item.httpCode} httpMessage={item.httpMessage} />);
            });
            this.setState({ list: comp });
            this.setState({ loadingTable: false, isDisable: false });
        });


    }



    render() {
        return (
            <Segment.Group>
                <Segment basic>
                    <Button icon labelPosition='right' disabled={this.state.isDisable} onClick={() => this._doBrokenPage()}>
                        Check
                       <Icon name='right arrow' />
                    </Button></Segment>
                <Segment.Group horizontal style={{ margin: 0 }}>

                    <Segment basic loading={this.state.loadingTable}>

                        <Segment.Group horizontal >

                            <Segment style={{ margin: 'auto', textAlign: 'center', padding: 0 }}>
                                <Icon className="warning sign" size='huge' color='red' />
                            </Segment>
                            <Segment style={{ paddingLeft: '10px' }}>
                                <p style={{ fontSize: 24 }}>10<br />
                                    Missing pages</p>
                            </Segment >

                            <Segment style={{ margin: 'auto', textAlign: 'center', padding: 0 }}>
                                <Icon className="warning sign" size='huge' color='black' /></Segment>
                            <Segment>
                                <p style={{ fontSize: 24 }}>2 <br /> Error pages</p>
                            </Segment>
                        </Segment.Group>
                        <Segment basic style={{ marginBottom: '60px', marginRight: '20px' }}>
                            <Button floated='right' ><Icon name="print" />Export</Button>
                            <Input icon='search' placeholder='Search...' style={{ float: 'right' }} />
                        </Segment>
                        <Segment style={{ maxHeight: '30vh', overflow: "auto" }}>

                            <Table singleLine textAlign='center' style={{ tableLayout: 'auto' }}>
                                <Table.Header >
                                    <Table.Row>
                                        <Table.HeaderCell>Page</Table.HeaderCell>
                                        <Table.HeaderCell>Status</Table.HeaderCell>
                                        <Table.HeaderCell>Issue</Table.HeaderCell>
                                    </Table.Row>
                                </Table.Header>
                                <Table.Body>
                                    {this.state.list.length === 0 ? <Table.Row><Table.Cell>This page haven't test yet, please try to test</Table.Cell> </Table.Row> : this.state.list}
                                </Table.Body>
                            </Table>
                        </Segment>
                    </Segment>

                </Segment.Group></Segment.Group>

        );
    }



}

export default brokenPagesScreen;