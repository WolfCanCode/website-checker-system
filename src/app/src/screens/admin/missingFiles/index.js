import React, { Component } from 'react';
import 'semantic-ui-css/semantic.min.css';
import { Segment, Button, Table, Icon, Input, SegmentGroup } from 'semantic-ui-react'
import TableRow from '../missingFiles/row-table'
import { Cookies } from "react-cookie";
import ReactToExcel from "react-html-table-to-excel";

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
        listReportId:[]

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
        this.setState({ loadingTable: true });
        var param = {
            "userId": cookies.get("u_id"),
            "userToken": cookies.get("u_token"),
            "websiteId": cookies.get("u_w_id"),
            "pageOptionId": cookies.get("u_option"),
        };

        fetch("/api/missingtest/lastest", {
            method: 'POST',
            headers: {
                'Accept': 'application/json',
                'Content-Type': 'application/json'
            },
            body: JSON.stringify(param)
        }).then(response => response.json()).then((data) => {
            comp = data.missingFileReport.map((item, index) => {
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
                statusResult = "This page haven't test yet, please try to test";
            }
            console.log(comp.length)
            this.setState({ statusNoResult: statusResult })
            this.setState({ countMissingFile: countMissingFile1 })
            this.setState({ countPageAffected: countPageAffected1 })
            this.setState({ list: comp });
            this.setState({ loadingTable: false });
        });

    }
    _doMissingFilePagesTest() {
        this.setState({ loadingTable: true, isDisable: true });
        var comp = [];
        var listReport=[];
        var param = {
            "userId": cookies.get("u_id"),
            "userToken": cookies.get("u_token"),
            "websiteId": cookies.get("u_w_id"),
            "pageOptionId": cookies.get("u_option"),
            "listType": this.state.listType
        };
        var countPageAffected1 = 0;
        var countMissingFile1 = 0;
        let flag = false;
        var listCom = [];
        var flagMissingFile = false;
        var listMissingFileCount = [];
        var statusResult = "";
        console.log(param)
        fetch("/api/missingtest", {
            method: 'POST',
            headers: {
                'Accept': 'application/json',
                'Content-Type': 'application/json'
            },
            body: JSON.stringify(param)
        }).then(response => response.json()).then((data) => {
            comp = data.missingFileReport.map((item, index) => {
                listReport.push(item.id)
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
                statusResult = "No Missing File Found";
            }
            if(comp.length !== 0){
                this.setState({isDoneTest:true})
            }
            console.log(comp.length)
            this.setState({ statusNoResult: statusResult })
            this.setState({ countMissingFile: countMissingFile1 });
            this.setState({ countPageAffected: countPageAffected1 });
            this.setState({ list: comp });
            this.setState({ loadingTable: false });
            this.setState({ isDisable: false });
            this.setState({listReportId:listReport})
           

        });
    }

    _doClickImage() {
        var active = false;

        var flag = false;
        var listTemp = this.state.listType;
        for (let i = 0; i < listTemp.length; i++) {
            if (listTemp[i] === 1) {
                listTemp.splice(i, 1);

                flag = true;

            }

        }
        if (!flag) {
            listTemp.push(1);
            active = true;
        }
        this.setState({ listType: listTemp, isActiveImg: active })

    }

    _doClickCSS() {
        var active = false;
        var flag = false;
        var listTemp = this.state.listType;

        for (let i = 0; i < listTemp.length; i++) {
            if (listTemp[i] === 2) {
                listTemp.splice(i, 1);
                flag = true;
            }

        }
        if (!flag) {
            listTemp.push(2);
            active = true;
        }



        this.setState({ listType: listTemp, isActiveCss: active })
        console.log(listTemp);
    }

    _doClickDOC() {
        var active = false;
        var flag = false;
        var listTemp = this.state.listType;

        for (let i = 0; i < listTemp.length; i++) {
            if (listTemp[i] === 3) {
                listTemp.splice(i, 1);
                flag = true;
            }

        }
        if (!flag) {
            listTemp.push(3);
            active = true;
        }



        this.setState({ listType: listTemp, isActiveDoc: active })
        console.log(listTemp);
    }

    _doClickARCHIRES() {
        var active = false;
        var flag = false;
        var listTemp = this.state.listType;

        for (let i = 0; i < listTemp.length; i++) {
            if (listTemp[i] === 4) {
                listTemp.splice(i, 1);
                flag = true;
            }

        }
        if (!flag) {
            listTemp.push(4);
            active = true;
        }



        this.setState({ listType: listTemp, isActiveARCHIRES: active })
        console.log(listTemp);
    }

    _saveReport() {
        this.setState({ loadingTable: true });
        this.setState({ isDisable: true });
        var comp = [];
        var countPageAffected1 = 0;
        var countMissingFile1 = 0;
        let flag = false;
        var listCom = [];
        var flagMissingFile = false;
        var listMissingFileCount = [];
        var statusResult = "";
        var param = {
            "userId": cookies.get("u_id"),
            "userToken": cookies.get("u_token"),
            "websiteId": cookies.get("u_w_id"),
            "pageOptionId": cookies.get("u_option"),
            "listReportId": this.state.listReportId
        };

        fetch("/api/missingtest/saveReport", {
            method: 'POST',
            headers: {
                'Accept': 'application/json',
                'Content-Type': 'application/json'
            },
            body: JSON.stringify(param)
        }).then(response => response.json()).then((data) => {
            comp = data.missingFileReport.map((item, index) => {
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
                statusResult = "No Missing File Found";
            }
           
            console.log(comp.length)
            this.setState({ statusNoResult: statusResult })
            this.setState({ countMissingFile: countMissingFile1 });
            this.setState({ countPageAffected: countPageAffected1 });
            this.setState({ list: comp });
            this.setState({ loadingTable: false });
            this.setState({ isDisable: false });
            this.setState({isDoneTest:false})
        });
    }



    render() {
        return (
            <div style={{ height: 'auto' }}>
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
                                <div style={{ marginBottom: '10px' }}>
                                <Button icon primary labelPosition='right' disabled={this.state.isDisable} onClick={() => this._doMissingFilePagesTest()}>
                                        Check
                       <Icon name='right arrow' />
                                    </Button>
                                    {this.state.isDoneTest ? <Button icon color="green" labelPosition='right' onClick={() => this._saveReport()}>
                                    Save <Icon name='check' />
                                </Button> : ""}
                                </div>
                                
                                    <div style={{ marginBottom: '50px' }}>
                                        <Button.Group style={{ float: 'left' }}>
                                            <Button onClick={() => this._doClickImage()} active={this.state.isActiveImg}>Image</Button>
                                            <Button onClick={() => this._doClickCSS()} active={this.state.isActiveCss}>CSS</Button>
                                            <Button onClick={() => this._doClickDOC()} active={this.state.isActiveDoc}>DOC</Button>
                                            <Button onClick={() => this._doClickARCHIRES()} active={this.state.isActiveARCHIRES}>ARCHIRES</Button>
                                        </Button.Group>
                                        <div style={{ marginBottom: '10px', float: 'right' }}>
                                            <ReactToExcel
                                                className="btn1"
                                                table="table-to-xls"
                                                filename="missingFiles_test_file"
                                                sheet="sheet 1"
                                                buttonText={<Button color="green"><Icon name="print" />Export</Button>}
                                            />
                                        </div>
                                        <Input icon='search' placeholder='Search...' style={{ float: 'right' }} />
                                    </div>
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
            </div>

        );
    }



}

export default missingFilesScreen;