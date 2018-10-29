import React, { Component } from 'react';
import {Table,Image} from 'semantic-ui-react'


export default class TableRow extends Component {
    render() {
        return ( <Table.Row>
            <Table.Cell style={{ width: '100px', whiteSpace: 'normal', wordBreak: 'break-all' }}><Image src={this.props.image} size='tiny' style={{ margin: 'auto' }} /></Table.Cell>
            <Table.Cell style={{ width: '100px', whiteSpace: 'normal', wordBreak: 'break-all' }}><a href='{this.props.url}'>{this.props.url}</a></Table.Cell>
            <Table.Cell style={{ width: '100px', whiteSpace: 'normal', wordBreak: 'break-all' }}>{this.props.sizeFav}</Table.Cell>
            <Table.Cell style={{ width: '100px', whiteSpace: 'normal', wordBreak: 'break-all' }}>{this.props.webAddress}</Table.Cell>


        </Table.Row>

        );
    }
}