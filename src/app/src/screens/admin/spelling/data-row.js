import React, { Component } from 'react';
import { Button, Table, Icon } from 'semantic-ui-react';


export default class TableRow extends Component {
    render() {
        return (
            <Table.Row>
                <Table.Cell style={{ width: '10px', whiteSpace: 'normal', wordBreak: 'break-all' }}>{this.props.no}</Table.Cell>
                <Table.Cell style={{ width: '200px', whiteSpace: 'normal', wordBreak: 'break-all' }}>{this.props.word}</Table.Cell>
                <Table.Cell style={{ width: '300px', whiteSpace: 'normal', wordBreak: 'break-all' }}>{this.props.excerpt}</Table.Cell>
                <Table.Cell style={{ width: '100px', whiteSpace: 'normal', wordBreak: 'break-all' }}>{this.props.page}</Table.Cell>
                <Table.Cell style={{ width: '300px', whiteSpace: 'normal', wordBreak: 'break-all' }}>
                    <Table.Cell > <Button primary> Ignore </Button> </Table.Cell>
                    <Table.Cell > <Button color='grey' > <Icon name='eye' />View suggestions</Button> </Table.Cell>
                </Table.Cell>
            </Table.Row>
        );
    }
}