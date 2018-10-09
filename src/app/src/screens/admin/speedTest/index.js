import React, { Component } from 'react';
import 'semantic-ui-css/semantic.min.css';
import { Segment, Button, Table, Image, Icon, Input } from 'semantic-ui-react'
import SpeedTest from '../../../assets/desktopSpeed.png';
import TableRow from './row-table';





class speedTestScreen extends Component {
    state = { list: [], loadingTable: false, averageInteractiveTime : "", averagePageLoadTime : "", averageSize : "" };


    componentDidMount() {
        var comp = [];
        this.setState({ loadingTable: true });
        var param = [{ "url": "https://gaana.com" },
        { "url": "https://gaana.com/discover" },
        { "url": "https://gaana.com/browser" }];
        fetch("/api/speedTest", {
            method: 'POST',
            headers: {
                'Accept': 'application/json',
                'Content-Type': 'application/json'
            },
            body: JSON.stringify(param)
        }).then(response => response.json()).then((data) => {
            comp = data.map((item, index) => {
                return (<TableRow key={index} url={item.url} interactiveTime={item.interactiveTime} pageLoadTime={item.pageLoadTime} size={item.size} />);
            });

            let totalInteractiveTime = data.reduce((interactiveTime, item) => {
                return interactiveTime = +interactiveTime + +item.interactiveTime
            }, 0)

            let totalPageLoadTime = data.reduce((pageLoadTime, item) => {
                return pageLoadTime = +pageLoadTime + +item.pageLoadTime
            }, 0)

            let totalSize = data.reduce((sizePage, item) => {
                return sizePage = +sizePage + +item.size
            }, 0)

            let listLength = comp.length;
            let averageInteractiveTime = (totalInteractiveTime/listLength).toFixed(1);
            let averagePageLoadTime = (totalPageLoadTime/listLength).toFixed(1);
            let averageSize = (totalSize/listLength).toFixed(1);

            this.setState({ averageInteractiveTime: averageInteractiveTime });
            this.setState({ averagePageLoadTime: averagePageLoadTime });
            this.setState({ averageSize: averageSize });
            this.setState({ list: comp });
            this.setState({ loadingTable: false });
        });


    }

    render() {
        return (
            <div style={{ height: 'auto', marginTop: '20px' }}>
                <Segment.Group>
                    <Segment><h3>Desktop Speed Test</h3></Segment>
                    <Segment.Group horizontal>

                        <Segment basic loading={this.state.loadingTable}>


                            <Segment.Group horizontal >

                                <Segment style={{ paddingLeft: '10px' }}>
                                    <p style={{ fontSize: 24 }}>{this.state.averageInteractiveTime}s <br />
                                        Page interactive time</p>

                                </Segment >

                                <Segment>
                                    <p style={{ fontSize: 24 }}>{this.state.averagePageLoadTime}s <br /> Page load time</p>
                                </Segment>
                                <Segment>
                                    <p style={{ fontSize: 24 }}>{this.state.averageSize} MB <br /> Average page size</p>
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
                                        <Table.HeaderCell>Interactive time</Table.HeaderCell>
                                        <Table.HeaderCell>Load time</Table.HeaderCell>
                                        <Table.HeaderCell>Size</Table.HeaderCell>

                                    </Table.Row>
                                </Table.Header>
                                <Table.Body>
                                    {this.state.list}



                                </Table.Body>
                            </Table>

                        </Segment>



                        <Segment basic style={{ textAlign: 'center', margin: 'auto' }} >
                            <div ><strong >Desktop Speed </strong><Icon name='question circle' size='large'></Icon></div>
                            <Image src={SpeedTest} size='medium' style={{ margin: 'auto' }} />
                        </Segment>


                    </Segment.Group>
                </Segment.Group>
            </div>

        );
    }



}

export default speedTestScreen;