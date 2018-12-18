import React, { Component } from 'react';
import { Table } from 'semantic-ui-react'


export default class TableRow extends Component {
    state = { dataListCon: [] }
    render() {

        return (<Table.Row colSpan='5'>

            <Table.Cell style={{ width: '100px', whiteSpace: 'normal', wordBreak: 'break-all' }}>{this.props.phoneMail}</Table.Cell>
            <Table.Cell style={{ width: '100px', whiteSpace: 'normal', wordBreak: 'break-all' }}>
                {this.props.url.map((item, index) => {
                    return (<div key={index}>
                        <a href={item}>{item}</a>
                    </div>);
                })}
            </Table.Cell>
            {/* <Table.Cell style={{ width: '100px', whiteSpace: 'normal', wordBreak: 'break-all' }}><Button>Edit</Button></Table.Cell> */}



            {/* <Table.Cell style={{ width: '100px', whiteSpace: 'normal', wordBreak: 'break-all' }}>{this.props.httpcode}</Table.Cell> */}
        </Table.Row>
        );
    }
}