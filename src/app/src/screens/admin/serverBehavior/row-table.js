import React, { Component } from 'react';
import { Table, } from 'semantic-ui-react'


export default class TableRow extends Component {
    render() {
        return (<Table.Row>
            <Table.Cell ><a href={this.props.url}>{this.props.url}</a></Table.Cell>
            <Table.Cell >{this.props.isPageSSL === true ? <font style={{ color: 'green' }}>YES</font> : <font style={{ color: 'red' }}>NO</font>}</Table.Cell>
            <Table.Cell >{this.props.isRedirectHTTPS === true ? <font style={{ color: 'green' }}>YES</font> : <font style={{ color: 'red' }}>NO</font>}</Table.Cell>
            <Table.Cell >{this.props.isRedirectWWW === true ? <font style={{ color: 'green' }}>YES</font> : <font style={{ color: 'red' }}>NO</font>}</Table.Cell>
        </Table.Row>
        );
    }
}