import React, { Component } from 'react';
import {Table,Label} from 'semantic-ui-react'


export default class TableRow extends Component {
    render() {
        return (<Table.Row>
           {/* <Table.Cell style={{ width: '100px', whiteSpace: 'normal', wordBreak: 'break-all' }}><a href={this.props.urlPage}>{this.props.urlPage.split("www.")[1]}</a></Table.Cell>
            <Table.Cell style={{ width: '100px', whiteSpace: 'normal', wordBreak: 'break-all' }}><a href={this.props.urlLink}>{this.props.urlLink.split("www.")[1]}</a></Table.Cell> */}
           <Table.Cell style={{ width: '100px', whiteSpace: 'normal', wordBreak: 'break-all' }}><a href={this.props.urlPage}>{this.props.urlLink}</a></Table.Cell>
            <Table.Cell style={{ width: '100px', whiteSpace: 'normal', wordBreak: 'break-all' }}><a href={this.props.urlLink}>{this.props.urlPage}</a></Table.Cell>
            <Table.Cell style={{ width: '100px', whiteSpace: 'normal', wordBreak: 'break-all' }}><Label style={{ fontSize: '13px' }} >{this.props.httpMessage} ({this.props.httpCode}) </Label></Table.Cell>
            {/* <Table.Cell style={{ width: '100px', whiteSpace: 'normal', wordBreak: 'break-all' }}>{this.props.httpcode}</Table.Cell> */}
            </Table.Row>
        );
    }
}