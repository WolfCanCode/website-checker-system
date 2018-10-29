import React, { Component } from 'react';
import {Table,Label } from 'semantic-ui-react'


export default class TableRow extends Component {
    render() {
        return (<Table.Row>
         
           <Table.Cell style={{ width: '100px', whiteSpace: 'normal', wordBreak: 'break-all' }}><Label style={{ fontSize: '13px' }} >{this.props.cookieName} </Label></Table.Cell>
            <Table.Cell style={{ width: '100px', whiteSpace: 'normal', wordBreak: 'break-all' }}><Label style={{ fontSize: '13px' }} >{this.props.category} </Label></Table.Cell>
            <Table.Cell style={{ width: '100px', whiteSpace: 'normal', wordBreak: 'break-all' }}> {this.props.party} </Table.Cell>
            <Table.Cell style={{ width: '100px', whiteSpace: 'normal', wordBreak: 'break-all' }}>{this.props.description}</Table.Cell>


            </Table.Row>
        );
    }
}