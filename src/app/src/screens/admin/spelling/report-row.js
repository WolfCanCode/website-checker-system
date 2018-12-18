import React, { Component } from 'react';
import { Table} from 'semantic-ui-react';

export default class ReportRow extends Component {
    

    render() {
        return (


            <Table.Row>
                <Table.Cell style={{ width: '10px', whiteSpace: 'normal', wordBreak: 'break-all' }}>{this.props.no + 1} </Table.Cell>
                <Table.Cell style={{ width: '100px', whiteSpace: 'normal', wordBreak: 'break-all' }}>{this.props.word} </Table.Cell>
                <Table.Cell style={{ width: '10px', whiteSpace: 'normal', wordBreak: 'break-all' }}>{this.props.type} </Table.Cell>
                <Table.Cell style={{ width: '300px', whiteSpace: 'normal', wordBreak: 'break-all' }}>{this.props.definition} </Table.Cell>
                <Table.Cell style={{ width: '300px', whiteSpace: 'normal', wordBreak: 'break-all' }}>{this.props.decision} </Table.Cell>
                <Table.Cell style={{ width: '300px', whiteSpace: 'normal', wordBreak: 'break-all' }}>{this.props.selectedSuggestion} </Table.Cell>
            </Table.Row>
        );
    }
}