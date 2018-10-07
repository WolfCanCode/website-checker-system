import React, { Component } from 'react';
import {Table,} from 'semantic-ui-react'


export default class TableRow extends Component {
    render() {
        return (<Table.Row>
            <Table.Cell style={{ width: '100px', whiteSpace: 'normal', wordBreak: 'break-all' }}><a href={this.props.url}>{this.props.url}</a></Table.Cell>
            <Table.Cell style={{ width: '100px', whiteSpace: 'normal', wordBreak: 'break-all' }}>{this.props.interactiveTime}</Table.Cell>
            <Table.Cell style={{ width: '100px', whiteSpace: 'normal', wordBreak: 'break-all' }}>{this.props.pageLoadTime}</Table.Cell>
            <Table.Cell style={{ width: '100px', whiteSpace: 'normal', wordBreak: 'break-all' }}>{this.props.size}</Table.Cell>
            </Table.Row>
        );
    }
}