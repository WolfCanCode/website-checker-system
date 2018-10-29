import React, { Component } from 'react';
import 'semantic-ui-css/semantic.min.css';
import {Segment, Button, Table, Icon, Input} from 'semantic-ui-react'
import TableRow from '../missingFiles/row-table'




class missingFilesScreen extends Component {
    state = { list: [], 
        loadingTable: false, 
        isDisable: false, 
        countMissingFile:0, 
        countPageAffected:0,
        listType:[],
       isActiveImg:false,
       isActiveCss:false,
       isActiveDoc:false,
       isActiveARCHIRES:false
        
    };

    componentDidMount() {
        var comp = [];
        var countPageAffected1=0;
        var countMissingFile1=0;
        let flag =false;
        var listCom=[];
        var flagMissingFile=false;
        var listMissingFileCount =[];
        this.setState({ loadingTable: true });
        var param = [{ "url": "https://www.bhcosmetics.com/" },
        { "url": "http://www.sggp.org.vn/" },
        ];
        fetch("/api/missingtest/lastest", {
            method: 'POST',
            headers: {
                'Accept': 'application/json',
                'Content-Type': 'application/json'
            },
            body: JSON.stringify(param)
        }).then(response => response.json()).then((data) => {
            comp = data.map((item, index) => {
               for(let i=0; i<listCom.length;i++){
                   if(item.pages===listCom[i]){
                    flag=true;
                   }
               }
               if(flag===false){
                   listCom.push(item.pages);
                   countPageAffected1++;
               }else{
                   flag=false;
               }

               for(let i=0;i<listMissingFileCount.length;i++){
                   if(item.fileMissing===listMissingFileCount[i]){
                       flagMissingFile=true;
                   }
               }
               if(flagMissingFile===false){
                   listMissingFileCount.push(item.fileMissing);
                   countMissingFile1++;
               }
               else{
                    flagMissingFile=false;
               }

                return (<TableRow key={index} fileMissing={item.fileMissing} description={item.description} pages={item.pages} />);
            });

            
            console.log(comp.length)
            this.setState({countMissingFile: countMissingFile1})
            this.setState({countPageAffected: countPageAffected1})
            this.setState({ list: comp });
            this.setState({ loadingTable: false });
        });

    }
    _doMissingFilePagesTest() {
        this.setState({ loadingTable: true, isDisable: true });
        var comp = [];
        var param = this.state.listType ;
        var countPageAffected1=0;
        var countMissingFile1=0;
        let flag =false;
        var listCom=[];
        var flagMissingFile=false;
        var listMissingFileCount =[];
        console.log(param)
        fetch("/api/missingtest", {
            method: 'POST',
            headers: {
                'Accept': 'application/json',
                'Content-Type': 'application/json'
            },
            body: JSON.stringify(param)
        }).then(response => response.json()).then((data) => {
            comp = data.map((item, index) => {
                for(let i=0; i<listCom.length;i++){
                    if(item.pages===listCom[i]){
                     flag=true;
                    }
                }
                if(flag===false){
                    listCom.push(item.pages);
                    countPageAffected1++;
                }else{
                    flag=false;
                }
 
                for(let i=0;i<listMissingFileCount.length;i++){
                    if(item.fileMissing===listMissingFileCount[i]){
                        flagMissingFile=true;
                    }
                }
                if(flagMissingFile===false){
                    listMissingFileCount.push(item.fileMissing);
                    countMissingFile1++;
                }
                else{
                     flagMissingFile=false;
                }
                return (<TableRow key={index} fileMissing={item.fileMissing} description={item.description} pages={item.pages} />);
            });
            this.setState({countMissingFile: countMissingFile1});
            this.setState({countPageAffected: countPageAffected1});
            this.setState({ list: comp });
            this.setState({ loadingTable: false });
            this.setState({ isDisable: false });

        });
    }

    _doClickImage(){
        var active= false;

        var flag = false;
        var listTemp =this.state.listType;
        for(let i=0; i<listTemp.length; i++){
            if(listTemp[i].type===1){
                listTemp.splice(i,1);
                flag=true;
                
            }
           
        }
        if(!flag){
            listTemp.push({type:1});
            active=true;
        }
        

       
        this.setState({listType:listTemp, isActiveImg:active})
        
            console.log(listTemp);
        
    }

    _doClickCSS(){
        var active=false;
        var flag = false;
        var listTemp =this.state.listType;
        
        for(let i=0; i<listTemp.length; i++){
            if(listTemp[i].type===2){
                listTemp.splice(i,1);
                flag=true;
            }
           
        }
        if(!flag){
            listTemp.push({type:2});
            active=true;
        }
        

       
        this.setState({listType:listTemp, isActiveCss:active})
            console.log(listTemp);
    }

    _doClickDOC(){
        var active=false;
        var flag = false;
        var listTemp =this.state.listType;
        
        for(let i=0; i<listTemp.length; i++){
            if(listTemp[i].type===3){
                listTemp.splice(i,1);
                flag=true;
            }
           
        }
        if(!flag){
            listTemp.push({type:3});
            active=true;
        }
        

       
        this.setState({listType:listTemp, isActiveDoc:active})
            console.log(listTemp);
    }

    _doClickARCHIRES(){
        var active=false;
        var flag = false;
        var listTemp =this.state.listType;
        
        for(let i=0; i<listTemp.length; i++){
            if(listTemp[i].type===4){
                listTemp.splice(i,1);
                flag=true;
            }
           
        }
        if(!flag){
            listTemp.push({type:4});
            active=true;
        }
        

       
        this.setState({listType:listTemp, isActiveARCHIRES:active})
            console.log(listTemp);
    }

    
    
    
    render() {
        return (
            <div style={{ height: 'auto'}}>
                <Segment.Group>
                <Segment>
                        <Button icon labelPosition='right' disabled={this.state.isDisable} onClick={() => this._doMissingFilePagesTest()}>
                        Check
                       <Icon name='right arrow' />
                    </Button>
                        </Segment>
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
                        
                        <Segment basic>
                    
                            <div style={{marginBottom:'50px'}}>
                            <Button.Group style={{float:'left'}}>
    <Button onClick={()=>this._doClickImage()} active={this.state.isActiveImg}>Image</Button>
    <Button onClick={()=>this._doClickCSS()} active={this.state.isActiveCss}>CSS</Button>
    <Button onClick={()=>this._doClickDOC()} active={this.state.isActiveDoc}>DOC</Button>
    <Button onClick={()=>this._doClickARCHIRES()} active={this.state.isActiveARCHIRES}>ARCHIRES</Button>
  </Button.Group>
                                <Button  style={{ float: 'right'}}><Icon name="print" />Export</Button>

                                <Input icon='search' placeholder='Search...' style={{ float: 'right' }} />
                            </div>
                        </Segment>
                            
                            <Table singleLine textAlign='center' style={{ tableLayout: 'auto' }}>
                                <Table.Header >
                                    <Table.Row>
                                        <Table.HeaderCell>Files</Table.HeaderCell>
                                        <Table.HeaderCell>Description</Table.HeaderCell>
                                        <Table.HeaderCell>Pages</Table.HeaderCell>
                                        <Table.HeaderCell>Action</Table.HeaderCell>

                                    </Table.Row>
                                </Table.Header>
                                <Table.Body>
                                {this.state.list.length === 0 ? <Table.Row><Table.Cell>This page haven't test yet, please try to test</Table.Cell></Table.Row> : this.state.list}
                                   
                                   
                                    

                                </Table.Body>
                            </Table>

                        </Segment>



                        


                    </Segment.Group>
                </Segment.Group>
            </div>

        );
    }



}

export default missingFilesScreen;