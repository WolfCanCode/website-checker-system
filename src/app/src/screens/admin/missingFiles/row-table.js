import React, { Component } from 'react';
import {Table,Button} from 'semantic-ui-react'


export default class TableRow extends Component {
    render() {
        return (<Table.Row>
           
            
            <Table.Cell style={{ width: '100px', whiteSpace: 'normal', wordBreak: 'break-all' }}>{this.props.fileMissing}</Table.Cell>
            <Table.Cell style={{ width: '100px', whiteSpace: 'normal', wordBreak: 'break-all' }}>{this.props.description}</Table.Cell>
            <Table.Cell style={{ width: '100px', whiteSpace: 'normal', wordBreak: 'break-all' }}><a href={this.props.pages}>{this.props.pages}</a></Table.Cell>
            <Table.Cell style={{ width: '100px', whiteSpace: 'normal', wordBreak: 'break-all' }}><Button>Ignore</Button></Table.Cell>
            </Table.Row>
        );
    }
}