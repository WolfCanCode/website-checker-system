import React, { Component } from 'react';
import { Table, Label } from 'semantic-ui-react'


export default class TableRow extends Component {
    render() {
        return (
            <Table.Row>
                <Table.Cell style={{ width: '300px', whiteSpace: 'normal', wordBreak: 'break-all' }}>{this.props.messages}</Table.Cell>
                <Table.Cell style={{ width: '100px', whiteSpace: 'normal', wordBreak: 'break-all' }}><Label style={{ fontSize: '14px' }} color={this.props.type==="WARNING"? "yellow": "red"} horizontal>{this.props.type}</Label></Table.Cell>
                <Table.Cell style={{ width: '100px', whiteSpace: 'normal', wordBreak: 'break-all' }}><a href={this.props.page}>{this.props.page}</a></Table.Cell>
            </Table.Row>
        );
    }
}