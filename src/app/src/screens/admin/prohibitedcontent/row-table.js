import React, { Component } from 'react';
import { Table, Label } from 'semantic-ui-react'


export default class TableRow extends Component {
    state = { fragment: "" };
    componentWillMount() {
        let preFrag = this.props.fragment;
        var fragment = [];
        var frag = {};
        frag.text = preFrag.replace(preFrag.split(" ")[0], "");
        frag.prohi = preFrag.split(" ")[0];
        fragment.push(frag);
        var comp = fragment.map((item, index) => {
            return (<span><font style={{ background: 'yellow' }}>{item.prohi}</font> {item.text}...</span>)
        })

        this.setState({ fragment: comp });
    }
    render() {

        return (<Table.Row>

            <Table.Cell style={{ width: '100px', whiteSpace: 'normal', wordBreak: 'break-all' }}><Label style={{ fontSize: '14px', }} >{this.props.word} </Label></Table.Cell>
            <Table.Cell style={{ width: '100px', whiteSpace: 'normal', wordBreak: 'break-all' }}> {this.props.type} </Table.Cell>
            <Table.Cell style={{ width: '100px', whiteSpace: 'normal', wordBreak: 'break-all' }}> {this.state.fragment} </Table.Cell>
            <Table.Cell style={{ width: '100px', whiteSpace: 'normal', wordBreak: 'break-all' }}><a href={this.props.urlPage}>{this.props.urlPage}</a></Table.Cell>


        </Table.Row>
        );
    }
}