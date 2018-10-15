import React, { Component } from 'react';
import 'semantic-ui-css/semantic.min.css';
import { Segment, Button, Table, Image, Icon, Input } from 'semantic-ui-react'
import BrokenPages from '../../../assets/brokenPage.png';
import TableRow from './row-table';






class brokenPagesScreen extends Component {

    state = { list: [], loadingTable: false, isDisable: false  };


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
                return (<TableRow key={index} urlPage={item.urlPage} stt={item.stt} httpCode={item.httpCode} />);
            });
            this.setState({ list: comp });
            this.setState({ loadingTable: false });
        });


    }

    _doBrokenPage(){
        var comp = [];
        this.setState({ loadingTable: true , isDisable: true });
        var param = [{ "url": "https://www.nottingham.ac.uk/about/campuses/campuses.aspx" },
        { "url": "https://www.nottingham.ac.uk/about/visitorinformation/information.aspx" },
        { "url": "https://www.nottingham.ac.uk/about/keydates/index.aspx" },
        { "url": "https://www.nottingham.ac.uk/about/facilities/facilities.aspx" },
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
                return (<TableRow key={index} urlPage={item.urlPage} stt={item.stt} httpCode={item.httpCode} />);
            });
            this.setState({ list: comp });
            this.setState({ loadingTable: false , isDisable: false });
        });


    }
        


    render() {
        return (
            <div style={{ height: 'auto', marginTop: '20px' }}>

                <Segment.Group>
                    <Segment><h3>Broken Pages test</h3>
                    <Button icon labelPosition='right' disabled={this.state.isDisable} onClick={()=>this._doBrokenPage()}>
                        Check
                       <Icon name='right arrow' />
                    </Button>
                    </Segment>
                    <Segment.Group horizontal>

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
                                    <Icon className="check" size='huge' color='green' />
                                </Segment>
                                <Segment style={{ paddingLeft: '10px' }}>
                                    <p style={{ fontSize: 24 }}>0 <br />
                                        Empty pages</p>
                                </Segment >
                                <Segment style={{ margin: 'auto', textAlign: 'center', padding: 0 }}>
                                    <Icon className="warning sign" size='huge' color='black' /></Segment>
                                <Segment>
                                    <p style={{ fontSize: 24 }}>2 <br /> Error pages</p>
                                </Segment>
                            </Segment.Group>
                            <div style={{ marginBottom: '60px', marginRight: '20px' }}>
                                <Button floated='right' ><Icon name="print" />Export</Button>

                                <Input icon='search' placeholder='Search...' style={{ float: 'right' }} />
                            </div>
                            <Table singleLine textAlign='center' style={{ tableLayout: 'auto' }}>
                                <Table.Header >
                                    <Table.Row>
                                        <Table.HeaderCell>Page</Table.HeaderCell>
                                        <Table.HeaderCell>Status</Table.HeaderCell>
                                        <Table.HeaderCell>HTTP Code</Table.HeaderCell>


                                    </Table.Row>
                                </Table.Header>
                                <Table.Body>

                                    {/* {this.state.list} */}
                                {this.state.list.length === 0 ? <b>This page haven't test yet, please try to test</b> : this.state.list}




                                </Table.Body>
                            </Table>

                        </Segment>



                        <Segment basic style={{ textAlign: 'center', margin: 'auto' }} >
                            <div ><strong >Broken Pages </strong><Icon name='question circle' size='large'></Icon></div>
                            <Image src={BrokenPages} size='medium' style={{ margin: 'auto' }} />
                        </Segment>


                    </Segment.Group>
                </Segment.Group>
            </div>

        );
    }



}

export default brokenPagesScreen;