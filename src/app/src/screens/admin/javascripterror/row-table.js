import React, { Component } from 'react';
import { Table, Label } from 'semantic-ui-react'


export default class TableRow extends Component {

    render() {
        let comp = this.props.messages.map((item, index) => {
            if (index !== 0) {
                var text = item.split("(")[0];
                var link = item.replace(text, "");
                link = link.replace("(", "");
                link = link.replace(")", "");
                var pathSeparate = link.split("/");
                var fileNameWithLine = pathSeparate[pathSeparate.length - 1];
                var fileName = fileNameWithLine.split(":")[0];
                link = link.split(":")[1];


                return (<Table.Row key={index} style={{ background: "#ffebee" }}>
                    <Table.Cell key={index} style={{ width: '300px', whiteSpace: 'normal', wordBreak: 'break-all', color: '#ef5350' }}>|--------------- at {text} <a href={link}>({fileName})</a></Table.Cell><Table.Cell></Table.Cell><Table.Cell></Table.Cell>
                </Table.Row>);
            };
            return "";
        });
        return (<Table.Body>
            <Table.Row style={{ background: this.props.type === "WARNING"? "#FFFDE7" : "#ffebee"}}>
                <Table.Cell style={{ width: '300px', whiteSpace: 'normal', wordBreak: 'break-all', color: this.props.type === "WARNING"? "#F9A825" : "#ef5350", fontWeight:'bold' }}>{this.props.messages[0]}</Table.Cell>
                <Table.Cell style={{ width: '100px', whiteSpace: 'normal', wordBreak: 'break-all' }}><Label style={{ fontSize: '14px' }} color={this.props.type === "WARNING" ? "yellow" : "red"} horizontal>{this.props.type}</Label></Table.Cell>
                <Table.Cell style={{ width: '100px', whiteSpace: 'normal', wordBreak: 'break-all' }}><a href={this.props.page}>{this.props.page}</a></Table.Cell>
            </Table.Row>
            {comp}
        </Table.Body>
        );
    }
}