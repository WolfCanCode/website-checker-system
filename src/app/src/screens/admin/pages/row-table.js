import React, { Component } from 'react';
import {Table,} from 'semantic-ui-react'


export default class TableRow extends Component {
    render() {
        return (<Table.Row>
           
            
            <Table.Cell style={{ width: '100px', whiteSpace: 'normal', wordBreak: 'break-all' }}>{this.props.titleWeb}</Table.Cell>
            <Table.Cell style={{ width: '100px', whiteSpace: 'normal', wordBreak: 'break-all' }}><a href={this.props.url}>{this.props.url}</a></Table.Cell>
            <Table.Cell style={{ width: '100px', whiteSpace: 'normal', wordBreak: 'break-all' }}>{this.props.canonicalUrl}</Table.Cell>
            <Table.Cell style={{ width: '100px', whiteSpace: 'normal', wordBreak: 'break-all' }}>{this.props.httpCode}</Table.Cell>
            </Table.Row>
        );
    }
}