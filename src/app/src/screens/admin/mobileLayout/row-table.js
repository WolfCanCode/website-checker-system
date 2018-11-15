import React, { Component } from 'react';
import {Table, Label } from 'semantic-ui-react'


export default class TableRow extends Component {
    render() {

            let issues = this.props.issue.split(',').map(function (issue, index) {
                return <p key={index} ><Label color='red' style={{ fontSize: '14px'}} >{ issue }</Label></p>; 
            });
            
        return (<Table.Row>
         
           <Table.Cell style={{ width: '600px', textAlign :'center'}} ><img src={this.props.screenShot} alt='screenShot' style={{height : "350px", width : "230px"}}></img></Table.Cell>
            <Table.Cell style={{ width: '600px'}} ><br></br>{this.props.title}<br></br>
            <br></br><a href={this.props.url}>{this.props.url}</a></Table.Cell>
            <Table.Cell style={{ textAlign :'center'}}>  <div >{issues}</div> </Table.Cell>
            


            </Table.Row>
        );
    }
}
