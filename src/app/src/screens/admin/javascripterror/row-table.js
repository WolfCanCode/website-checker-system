import React, { Component } from 'react';
import { Table, Label } from 'semantic-ui-react'


export default class TableRow extends Component {

    render() {
        var toList = [];
        let data = {};
        if (this.props.type !== "WARNING") {
            data.text = this.props.messages.split("at")[0];
            data.type = this.props.type;
            data.page = this.props.page;
            data.isParent = true;
            toList.push(data);
            console.log(this.props.messages);
            this.props.messages.map(async (item, index) => {
                if (index !== 0) {
                    var text = item.split("(")[0];
                    var link = item.replace(text, "");
                    link = link.replace("(", "");
                    link = link.replace(")", "");
                    var pathSeparate = link.split("/");
                    var fileNameWithLine = pathSeparate[pathSeparate.length - 1];
                    var fileName = fileNameWithLine.split(":")[0];
                    link = link.split(":")[1];
                    let data = {};
                    data.text = text;
                    data.link = link;
                    data.fileName = fileName;
                    data.isParent = false;
                    toList.push(data);
                };
            });
            var comp = toList.map((item, index) => {
                //parrent node
                if (!item.isParent) {
                    return (<Table.Row key={index} style={{ background: "#ffebee" }}>
                        <Table.Cell key={index} style={{ width: '300px', whiteSpace: 'normal', wordBreak: 'break-all', color: '#ef5350' }}>|--------------- at {item.text} <a href={item.link}>({item.fileName})</a></Table.Cell><Table.Cell></Table.Cell><Table.Cell></Table.Cell>
                    </Table.Row>
                    );
                } else {
                    //child node
                    return (<Table.Row key={index} style={{ background: item.type === "WARNING" ? "#FFFDE7" : "#ffebee" }}>
                        <Table.Cell style={{ width: '300px', whiteSpace: 'normal', wordBreak: 'break-all', color: this.props.type === "WARNING" ? "#F9A825" : "#ef5350", fontWeight: 'bold' }}>{item.text}</Table.Cell>
                        <Table.Cell style={{ width: '100px', whiteSpace: 'normal', wordBreak: 'break-all' }}><Label style={{ fontSize: '14px' }} color={item.type === "WARNING" ? "yellow" : "red"} horizontal>{item.type}</Label></Table.Cell>
                        <Table.Cell style={{ width: '100px', whiteSpace: 'normal', wordBreak: 'break-all' }}><a href={item.page}>{item.page}</a></Table.Cell>
                    </Table.Row>);
                }

            });
        } else {
            data.text = this.props.messages;
            data.type = this.props.type;
            data.page = this.props.page;
            data.isParent = true;
            toList.push(data);

            comp = toList.map((item, index) => {

                return (<Table.Row key={index} style={{ background: item.type === "WARNING" ? "#FFFDE7" : "#ffebee" }}>
                    <Table.Cell style={{ width: '300px', whiteSpace: 'normal', wordBreak: 'break-all', color: this.props.type === "WARNING" ? "#F9A825" : "#ef5350", fontWeight: 'bold' }}>{item.text}</Table.Cell>
                    <Table.Cell style={{ width: '100px', whiteSpace: 'normal', wordBreak: 'break-all' }}><Label style={{ fontSize: '14px' }} color={item.type === "WARNING" ? "yellow" : "red"} horizontal>{item.type}</Label></Table.Cell>
                    <Table.Cell style={{ width: '100px', whiteSpace: 'normal', wordBreak: 'break-all' }}><a href={item.page}>{item.page}</a></Table.Cell>
                </Table.Row>);

            });
        }

        return (<React.Fragment>
            {comp}</ React.Fragment> //blank tag
        );
    }
}