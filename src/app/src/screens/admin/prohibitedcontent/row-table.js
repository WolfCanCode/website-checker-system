import React, { Component } from 'react';
import {Table,Label } from 'semantic-ui-react'


export default class TableRow extends Component {
    render() {
        return (<Table.Row>
         
           <Table.Cell style={{ width: '100px', whiteSpace: 'normal', wordBreak: 'break-all' }}><Label style={{ fontSize: '14px', }} >{this.props.word} </Label></Table.Cell>         
            <Table.Cell style={{ width: '100px', whiteSpace: 'normal', wordBreak: 'break-all' }}> {this.props.type} </Table.Cell>
            <Table.Cell style={{ width: '100px', whiteSpace: 'normal', wordBreak: 'break-all' }}><a href={this.props.urlPage}>{this.props.urlPage}</a></Table.Cell>


            </Table.Row>
        );
    }
}