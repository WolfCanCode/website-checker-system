import React, { Component } from 'react';
import 'semantic-ui-css/semantic.min.css';
import { Segment, Table, Icon, SegmentGroup } from 'semantic-ui-react'
import TableRow from '../missingFiles/row-table'
import { Cookies } from "react-cookie";

const cookies = new Cookies();



class missingFilesScreen extends Component {
    state = {
        list: [],
        loadingTable: false,
        isDisable: false,
        countMissingFile: 0,
        countPageAffected: 0,
        listType: [],
        isActiveImg: false,
        isActiveCss: false,
        isActiveDoc: false,
        isActiveARCHIRES: false,
        statusNoResult: "",
        isDoneTest: false,
        listReportId: []

    };

    componentDidMount() {
        var comp = [];
        var countPageAffected1 = 0;
        var countMissingFile1 = 0;
        let flag = false;
        var listCom = [];
        var flagMissingFile = false;
        var listMissingFileCount = [];
        var statusResult = "";
        var count = 0;
        this.setState({ loadingTable: true });
        var param = {
            "pageOptionName": cookies.get("u_guest_token")
        }
        var x = setInterval(() => {
            fetch("/api/guest/missingFile", {
                method: 'POST',
                headers: {
                    'Accept': 'application/json',
                    'Content-Type': 'application/json'
                },
                body: JSON.stringify(param)
            }).then(response => response.json()).then((res) => {
                count++;
                if (count === 5) {
                    clearInterval(x);
                    this.setState({ loadingTable: false, statusNoResult: "This website has no missing file" });
                }
                if (res.data.length !== 0) {
                    clearInterval(x);
                    comp = res.data.map((item, index) => {
                        for (let i = 0; i < listCom.length; i++) {
                            if (item.pages === listCom[i]) {
                                flag = true;
                            }
                        }
                        if (flag === false) {
                            listCom.push(item.pages);
                            countPageAffected1++;
                        } else {
                            flag = false;
                        }

                        for (let i = 0; i < listMissingFileCount.length; i++) {
                            if (item.fileMissing === listMissingFileCount[i]) {
                                flagMissingFile = true;
                            }
                        }
                        if (flagMissingFile === false) {
                            listMissingFileCount.push(item.fileMissing);
                            countMissingFile1++;
                        }
                        else {
                            flagMissingFile = false;
                        }

                        return (<TableRow key={index} fileMissing={item.fileMissing} description={item.description} pages={item.pages} />);
                    });

                    if (comp.length === 0) {
                        statusResult = "This website has no missing file";
                    }
                    console.log(comp.length)
                    this.setState({ statusNoResult: statusResult })
                    this.setState({ countMissingFile: countMissingFile1 })
                    this.setState({ countPageAffected: countPageAffected1 })
                    this.setState({ list: comp });
                    this.setState({ loadingTable: false });
                }
            });
        }, 1000);
    }




    render() {
        return (
            <div style={{ height: 'auto' }} >
                <Segment.Group>

                    <Segment.Group horizontal>

                        <Segment basic>

                            <Segment.Group horizontal >
                                <Segment style={{ margin: 'auto', textAlign: 'center', padding: 0 }}>
                                    <Icon className="warning sign" size='huge' color='red' />
                                </Segment>
                                <Segment style={{ paddingLeft: '10px' }}>
                                    <p style={{ fontSize: 24 }}>{this.state.countMissingFile} <br />
                                        Missing Files</p>
                                </Segment >
                                <Segment style={{ margin: 'auto', textAlign: 'center', padding: 0 }}>
                                    <Icon className="file outline" size='huge' color='grey' /></Segment>
                                <Segment>
                                    <p style={{ fontSize: 24 }}>{this.state.countPageAffected} <br /> Affected Pages</p>
                                </Segment>
                            </Segment.Group>


                            {/* <Segment.Group  horizontal basic>
                                <Segment  basic style={{ float:'left', width:'70%'}}>
                                <div attached style={{ float:'left', width:'50%', marginTop:'5%'}}>
                                    <h2>Don't go missing</h2>
                                    <Divider />
                                    <p style={{color:'grey'}}>A missing file is part of a webpage that has gone missing, such as an image or css.</p>

                                   <p style={{color:'grey'}}>Missing files often cause pages to appear broken, and should be fixed.</p>

                                </div>
                                
                                
                                </Segment>
                                <Segment basic style={{ textAlign: 'center', margin: 'auto',width:'30%' } }>
                                <strong >Missing Files </strong><Icon name='question circle' size='large'></Icon>
                                <Image src={MissingFiles} size='medium' style={{ margin:'auto' }} />
                                </Segment>

                            </Segment.Group> */}
                            {/* <Segment basic style={{ textAlign: 'center', margin: 'auto' } } horizontal >
                            <div style={{ float:'left' }}></div>
                            <div style={{ float:'right', marginRight:'0' }}>
                            <div style={{ margin:'auto' }} ></div>
                          
                            </div>
                           
                        </Segment> */}
                            <SegmentGroup basic>
                                <Segment basic>


                                </Segment>
                            </SegmentGroup>
                        </Segment>
                    </Segment.Group>
                </Segment.Group>
                <Segment singleLine loading={this.state.loadingTable}>
                    <Table singleLine unstackable textAlign='center' style={{ tableLayout: 'auto' }} id="table-to-xls">
                        <Table.Header >
                            <Table.Row>
                                <Table.HeaderCell>Files</Table.HeaderCell>
                                <Table.HeaderCell>Description</Table.HeaderCell>
                                <Table.HeaderCell>Pages</Table.HeaderCell>
                                {/* <Table.HeaderCell>Action</Table.HeaderCell> */}

                            </Table.Row>
                        </Table.Header>
                        <Table.Body>
                            {this.state.list.length === 0 ? <Table.Row><Table.Cell>{this.state.statusNoResult}</Table.Cell></Table.Row> : this.state.list}




                        </Table.Body>
                    </Table>

                </Segment>
            </div >

        );
    }



}

export default missingFilesScreen;