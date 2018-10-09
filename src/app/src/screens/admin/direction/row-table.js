import React, { Component } from 'react';
import {Table,} from 'semantic-ui-react'


export default class TableRow extends Component {
    render() {
        return (<Table.Row>
           
           <Table.Cell style={{ width: '100px', whiteSpace: 'normal', wordBreak: 'break-all' }}><a href={this.props.webAddress}>{this.props.webAddress}</a></Table.Cell>
            <Table.Cell style={{ width: '100px', whiteSpace: 'normal', wordBreak: 'break-all' }}><a href={this.props.redirectUrl}>{this.props.redirectUrl}</a></Table.Cell>
            <Table.Cell style={{ width: '100px', whiteSpace: 'normal', wordBreak: 'break-all' }}>{this.props.type}</Table.Cell>
            <Table.Cell style={{ width: '100px', whiteSpace: 'normal', wordBreak: 'break-all' }}>{this.props.httpcode}</Table.Cell>
            </Table.Row>
        );
    }
}