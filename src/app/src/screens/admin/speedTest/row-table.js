import React, { Component } from 'react';
import {Table,} from 'semantic-ui-react'


export default class TableRow extends Component {
    render() {
        return (<Table.Row>
            <Table.Cell style={{ width: '100px', whiteSpace: 'normal', wordBreak: 'break-all' }}><a href={this.props.url}>{this.props.url}</a></Table.Cell>
            <Table.Cell style={{ width: '100px', whiteSpace: 'normal', wordBreak: 'break-all' }}>{this.props.interactiveTime}s</Table.Cell>
            <Table.Cell style={{ width: '100px', whiteSpace: 'normal', wordBreak: 'break-all' }}>{this.props.pageLoadTime}s</Table.Cell>
            <Table.Cell style={{ width: '100px', whiteSpace: 'normal', wordBreak: 'break-all' }}>{this.props.size} MB</Table.Cell>
            </Table.Row>
        );
    }
}