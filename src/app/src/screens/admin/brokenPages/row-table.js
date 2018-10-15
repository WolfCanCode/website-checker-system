import React, { Component } from 'react';
import { Table, Label } from 'semantic-ui-react'


export default class TableRow extends Component {
    render() {
        return (<Table.Row>

            <Table.Cell style={{ width: '100px', whiteSpace: 'normal', wordBreak: 'break-all' }}><a href={this.props.urlPage}>{this.props.urlPage.split("https://www.")[1]}</a></Table.Cell>
            <Table.Cell style={{ width: '100px', whiteSpace: 'normal', wordBreak: 'break-all' }}><Label style={{ fontSize: '13px' }} color='red' horizontal>{this.props.stt}</Label></Table.Cell>
            <Table.Cell style={{ width: '100px', whiteSpace: 'normal', wordBreak: 'break-all' }}><Label style={{ fontSize: '13px' }} >{this.props.httpCode}</Label></Table.Cell>

            
            {/* <Table.Cell style={{ width: '100px', whiteSpace: 'normal', wordBreak: 'break-all' }}>{this.props.httpcode}</Table.Cell> */}
        </Table.Row>
        );
    }
}