import React, { Component } from 'react';
import {Table,} from 'semantic-ui-react'


export default class TableRow extends Component {
    render() {
        return (<Table.Row>
           
           <Table.Cell style={{ width: '100px', whiteSpace: 'normal', wordBreak: 'break-all' }}><a href={this.props.urlPage}>{this.props.urlPage.split("www.")[1]}</a></Table.Cell>
            <Table.Cell style={{ width: '100px', whiteSpace: 'normal', wordBreak: 'break-all' }}><a href={this.props.urlLink}>{this.props.urlLink.split("www.")[1]}</a></Table.Cell>
            <Table.Cell style={{ width: '100px', whiteSpace: 'normal', wordBreak: 'break-all' }}>{this.props.urlLink.split("www.")[1]} does not exits</Table.Cell>
            {/* <Table.Cell style={{ width: '100px', whiteSpace: 'normal', wordBreak: 'break-all' }}>{this.props.httpcode}</Table.Cell> */}
            </Table.Row>
        );
    }
}