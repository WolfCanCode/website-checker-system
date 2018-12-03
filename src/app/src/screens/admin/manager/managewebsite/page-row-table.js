import React, { Component } from 'react';
import { Table, Button, Input, Modal, Transition, Dropdown } from 'semantic-ui-react'
import { Cookies } from "react-cookie";

const cookies = new Cookies();

export default class PageTableRow extends Component {
    

    render() {
        return (
            
            
            <Table.Row>
                <Table.Cell style={{ width: '400', whiteSpace: 'normal', wordBreak: 'break-all' }}><a href={this.props.desUrl}>{this.props.desUrl}</a></Table.Cell>
                {this.props.desType === 1 ? <Table.Cell style={{ width: '100px', whiteSpace: 'normal', wordBreak: 'break-all' }}>Internal link</Table.Cell> : <Table.Cell style={{ width: '100px', whiteSpace: 'normal', wordBreak: 'break-all' }}>External link</Table.Cell>} 
            </Table.Row>
        );
    }
}