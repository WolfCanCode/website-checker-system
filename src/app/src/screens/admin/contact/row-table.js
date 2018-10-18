import React, { Component } from 'react';
import { Table,Button } from 'semantic-ui-react'


export default class TableRow extends Component {
    render() {
        return (<Table.Row>

            <Table.Cell style={{ width: '100px', whiteSpace: 'normal', wordBreak: 'break-all' }}>{this.props.phoneMail}</Table.Cell>
            <Table.Cell style={{ width: '100px', whiteSpace: 'normal', wordBreak: 'break-all' }}>{this.props.url}</Table.Cell>
            <Table.Cell style={{ width: '100px', whiteSpace: 'normal', wordBreak: 'break-all' }}><Button>Edit</Button></Table.Cell>
            

            
            {/* <Table.Cell style={{ width: '100px', whiteSpace: 'normal', wordBreak: 'break-all' }}>{this.props.httpcode}</Table.Cell> */}
        </Table.Row>
        );
    }
}