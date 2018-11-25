import React, { Component } from 'react';
import { Table, Button, Input, Modal, Transition, Dropdown } from 'semantic-ui-react'
import { Cookies } from "react-cookie";

const cookies = new Cookies();

export default class TableRow extends Component {
    state = {
        open: false, open1: false, oldWebName: this.props.name, webName: this.props.name,
        isDisable: true, editLoading: false, options: [], userAssign: [], defValue: [], loadingDelete: false,
        urlRoot: "", map: [], typeMap: [], urlMap: [], selectedUrl: ""
    }
    constructor(props) {
        super(props);
        this._makeNewver = this._makeNewver.bind(this);
        this._viewSitemap = this._viewSitemap.bind(this);
    }
    show = size => () => this.setState({ size, open: true })
    close1 = () => this.setState({ open1: false })

    closeConfigShow = (closeOnEscape, closeOnDimmerClick) => () => {
        this.setState({ closeOnEscape, closeOnDimmerClick, open1: true })
    }
    close = () => this.setState({ open: false })

    _makeNewver(id) {
        this.props.loadingTable(true);

        var param = { "userId": cookies.get("u_id"), "userToken": cookies.get("u_token"), "websiteId": id };
        fetch("/api/sitemap/makeVer", {
            method: 'POST',
            headers: {
                'Accept': 'application/json',
                'Content-Type': 'application/json'
            },
            body: JSON.stringify(param)
        }).then(response => response.json()).then((data) => {
            if (data.action === "SUCCESS") {
                this.setState({ version: data.version, time: data.time, isLoading: false });
                this.props.refreshTable(this.state.version);
            } else {
                alert("Thất bại");
            }
        });
    }

    _viewSitemap(id, name, url) {
        // eslint-disable-next-line
        var result = [];
        var urlLength = url.length;
        this.props.loadingTable(true);
        this.props.showingModal(true);
        this.props.getSelectedWebName(name);
        var param = { "userId": cookies.get("u_id"), "userToken": cookies.get("u_token"), "websiteId": id };
        fetch("/api/sitemap/getVisualSitemap", {
            method: 'POST',
            headers: {
                'Accept': 'application/json',
                'Content-Type': 'application/json'
            },
            body: JSON.stringify(param)
        }).then(response => response.json()).then((data) => {
            // eslint-disable-next-line
            result = data.map((item, index) => {
                // eslint-disable-next-line
                var mapArray = eval("[" + item.map + "]");
                // eslint-disable-next-line
                var typeArray = eval("[" + item.typeMap + "]");
                // eslint-disable-next-line
                var urlArray = eval("[" + item.urlMap + "]");
                this.setState({ map: mapArray, typeMap: typeArray, urlMap: urlArray });
                console.log("Map: " + mapArray);
                console.log("Type: " + typeArray);
                console.log("Url: " + urlArray);
            });
            var map = [];
            var typeMap = [];
            var urlMap = [];
            map = this.state.map;
            typeMap = this.state.typeMap;
            urlMap = this.state.urlMap;

            var canvas = document.getElementById("myCanvas");
            var context = canvas.getContext("2d");
            var rectCoord = [];
            context.beginPath();
            context.font = '18px serif';

            // var boxW = 300;
            // var boxH = 25;
            var boxW = 270;
            var boxH = 30;

            var halfBoxH = boxH / 2;

            var nfloor = map.length;

            var mxWidth = nfloor * boxW + (nfloor - 1) * 50 + 10 + 10;
            var mxHeight = map[nfloor - 1].length * (boxH + 10) + 20;

            var StartDrawingX = mxWidth - 10 - boxW;
            var StartDrawingY = 10;

            function addTextToBox(coordX, coordY, fontType, content, type) {
                context.save();
                context.fillStyle = "black";
                context.font = fontType;
                context.textAlign = 'left';
                context.translate(coordX + 10, coordY + boxH - 10);

                if (content === url) {

                }

                else if (type === 1 || type === 3) {
                    var distance = content.length - urlLength;
                    if (distance > 30) {
                        content = content.substring(urlLength, urlLength + 30) + '...';
                    } else {
                        content = content.substring(urlLength, urlLength + 30);
                    }

                    // alert(content);
                }
                else if (type === 2) {
                    if (distance > 30) {
                        content = content.substring(0, 30) + "...";
                    } else {
                        content = content.substring(0, 30);
                    }

                }
                context.fillText(content, 0, 0);
                context.restore();
            }

            function initialMap() {
                // resize to fix map
                canvas.width = mxWidth;
                canvas.height = mxHeight;
            }

            function buildTreeMap(floor, deep, coord) {
                // eslint-disable-next-line 
                var rect = new Array();

                if (floor === nfloor - 1) {

                    var curRow = StartDrawingY;

                    for (var i = 0; i < map[floor].length; i++) {
                        if (map[floor][i] > 0) {
                            context.fillStyle = "black";
                            context.fillRect(deep, curRow, boxW, boxH);
                            context.stroke();

                            // save the rectangle
                            rectCoord.push({ x: deep, y: curRow, url: urlMap[floor][i] });

                            // analysis color of type
                            context.fillStyle = "#79c5fc";

                            if (typeMap[floor][i] === 3) context.fillStyle = "red";
                            if (typeMap[floor][i] === 2) context.fillStyle = "gray";

                            context.fillRect(deep + 2, curRow + 2, boxW - 4, boxH - 4);
                            context.stroke();

                            // add text

                            addTextToBox(deep, curRow, '18px serif', urlMap[floor][i], typeMap[floor][i]);


                            //addTextToBox(deep, curRow, '12px serif', 'x = ' + deep + " y = " + curRow);
                        }
                        rect.push(curRow);
                        curRow = curRow + boxH + 10;
                    }
                }
                else {
                    for (let i = 0; i < map[floor].length; i++) {
                        if (map[floor][i] > 0) {

                            context.fillStyle = "black";
                            context.fillRect(deep, coord[i], boxW, boxH);
                            context.stroke();

                            // save the rectangle
                            rectCoord.push({ x: deep, y: coord[i], url: urlMap[floor][i] });

                            // analysis color of type
                            context.fillStyle = "#79c5fc";
                            if (typeMap[floor][i] === 3) context.fillStyle = "red";
                            if (typeMap[floor][i] === 2) context.fillStyle = "gray";


                            context.fillRect(deep + 2, coord[i] + 2, boxW - 4, boxH - 4);
                            context.stroke();

                            // add text
                            addTextToBox(deep, coord[i], '18px serif', urlMap[floor][i], typeMap[floor][i]);
                            //addTextToBox(deep, coord[i], '12px serif', 'x = ' + deep + "y = " + coord[i]);
                        }
                        rect.push(coord[i]);
                    }
                }

                // Draw linked lines
                if (floor === 0) return;
                // eslint-disable-next-line 
                coord = new Array();
                var bgSeg = 0;
                for (let i = 0; i < map[floor].length; i++) {
                    if (i === map[floor].length - 1 || map[floor][i] === 0 || (map[floor][i] !== map[floor][i + 1])) {

                        var endSeg = i;

                        if (map[floor][i] !== 0) {

                            for (var j = bgSeg; j <= endSeg; j++) {
                                context.moveTo(deep, rect[j] + halfBoxH);
                                context.lineTo(deep - 25, rect[j] + halfBoxH);
                                context.stroke();
                            }

                            // line begin to end
                            context.moveTo(deep - 25, rect[bgSeg] + halfBoxH);
                            context.lineTo(deep - 25, rect[endSeg] + halfBoxH);
                            context.stroke();

                            // line to parent
                            var mid = rect[bgSeg] + halfBoxH + (rect[endSeg] - rect[bgSeg]) / 2
                            context.moveTo(deep - 25, mid);
                            context.lineTo(deep - 50, mid);
                            context.stroke();
                        }

                        coord.push(rect[bgSeg] + (rect[endSeg] - rect[bgSeg]) / 2);
                        bgSeg = i + 1;
                    }
                }

                buildTreeMap(floor - 1, deep - 50 - boxW, coord);
            }
            initialMap();
            buildTreeMap(nfloor - 1, StartDrawingX, []);
            ////Update UI
            this.setState({ isDisabled: false, isLoading: false });
            this.props.loadingTable(false);
            var selectedRectangleValue = "";
            var that = this;
            // add 'click event' to canvas
            canvas.addEventListener('click', function (event) {

                // correct the coordinate
                var rect = canvas.getBoundingClientRect();
                let x = event.clientX - rect.left;
                let y = event.clientY - rect.top;

                //alert(x + ", " + y);

                var isInside = false;
                var id = -1;
                // loop in all rectangle
                for (var i = 0; i < rectCoord.length; i++) {

                    var minX = rectCoord[i].x;
                    var minY = rectCoord[i].y;
                    var maxX = minX + boxW - 1;
                    var maxY = minY + boxH - 1;

                    if (minX <= x && x <= maxX && minY <= y && y <= maxY) {
                        //alert("Inside!");
                        isInside = true;
                        id = i;
                        break;
                    }
                }

                if (isInside === true) {

                    // alert("Your URL: " + rectCoord[id].url);
                    selectedRectangleValue = rectCoord[id].url;
                    console.log("Selected: " + selectedRectangleValue);

                    // that.setState({selectedUrl : selectedRectangleValue});
                    // console.log("State: " + that.state.selectedUrl);
                    that.props.setSelectedRectValue(selectedRectangleValue);
                }
            });

        });
    }

    _editWebsite() {
        this.setState({ editLoading: true, isDisable: false });
        var param = {
            "managerId": cookies.get("u_id"), "managerToken": cookies.get("u_token"), website: {
                "id": this.props.id, "name": this.state.webName
            }
        };
        fetch("/api/manager/editWebsite", {
            method: 'POST',
            headers: {
                'Accept': 'application/json',
                'Content-Type': 'application/json'
            },
            body: JSON.stringify(param)
        }).then(response => response.json()).then((data) => {
            if (data.action === "SUCCESS") {
                this.setState({ editLoading: false });
                this.setState({ open: false });
                this.props.refreshTable();
            } if (data.action === "DUPLICATE ERROR") {
                alert("This system is existed");
            }
        });
    }

    _deleteWebsite() {
        this.setState({ loadingDelete: true });
        var param = {
            "managerId": cookies.get("u_id"), "managerToken": cookies.get("u_token"), website: {
                "id": this.props.id
            }
        };
        fetch("/api/manager/deleteWebsite", {
            method: 'POST',
            headers: {
                'Accept': 'application/json',
                'Content-Type': 'application/json'
            },
            body: JSON.stringify(param)
        }).then(response => response.json()).then((data) => {
            if (data.action === "SUCCESS") {
                this.setState({ open1: false, close1: true, loadingDelete: true });
                this.props.refreshTable();
            }
        });
    }

    _assignModal() {
        var param = {
            "managerId": cookies.get("u_id"), "managerToken": cookies.get("u_token"), website: {
                "id": this.props.id
            }
        };
        fetch("/api/manager/defaultAssign", {
            method: 'POST',
            headers: {
                'Accept': 'application/json',
                'Content-Type': 'application/json'
            },
            body: JSON.stringify(param)
        }).then(response => response.json()).then((data) => {
            var op = data.staffs.map((item, index) => {
                return { key: item.name, value: item.id, text: item.email };
            })
            var def = data.defStaffs.map((item, index) => {
                return item.id;
            });
            // var defAss = [];
            // for (let i = 0; i < op.length; i++) {
            //     for (let j = 0; j < def.length; j++) {
            //         if (op[i].value === def[j].value) {
            //             defAss.push(op[i]);
            //         }
            //     }
            // }
            this.setState({ options: op, assignModal: true, userAssign: def });
        });
    }

    _changeAssign(event, data) {
        this.setState({
            userAssign: data.value
        }, () => this._checkAssignBtn());
    }

    _checkAssignBtn() {
        if (this.state.userAssign.length === 0) {
            this.setState({ isDisable: true });
        } else {
            this.setState({ isDisable: false });
        }
    }

    _doAssignUser() {
        var param = {
            "managerId": cookies.get("u_id"), "managerToken": cookies.get("u_token"), website: {
                "id": this.props.id
            }, listStaffId: this.state.userAssign
        };
        fetch("/api/manager/assignWebsite", {
            method: 'POST',
            headers: {
                'Accept': 'application/json',
                'Content-Type': 'application/json'
            },
            body: JSON.stringify(param)
        }).then(response => response.json()).then((data) => {
            if (data.action === "SUCCESS") {
                this.setState({ assignModal: false });
            }
        });
    }

    _onchangeName(event) {
        this.setState({ webName: event.target.value }, () => this._checkAddBtn());
    }

    _checkAddBtn() {
        var result = false;
        if (this.state.webName === "" || this.state.webName === this.state.oldWebName) {
            result = true;
        }
        this.setState({ isDisable: result });
    }

    render() {
        const { open, size } = this.state;
        const { open1, closeOnEscape, closeOnDimmerClick } = this.state;
        const renderLabel = label => ({
            color: 'blue',
            content: `Staff: ${label.key} - ${label.text}`,
            icon: 'check',
        })
        return (<Table.Row>
            {/* Delete */}
            <Transition duration={600} divided size='huge' verticalAlign='middle' visible={open1}>
                <Modal
                    open={open1}
                    closeOnEscape={closeOnEscape}
                    closeOnDimmerClick={closeOnDimmerClick}
                    onClose={this.close1}
                    dimmer="blurring"
                >
                    <Modal.Header>Delete Your Website</Modal.Header>
                    <Modal.Content>
                        <p>Are you sure to delete this website ?</p>
                    </Modal.Content>
                    <Modal.Actions>
                        <Button onClick={this.close1}>No</Button>
                        <Button color="blue" content='Yes' onClick={() => this._deleteWebsite()} loading={this.state.loadingDelete}
                        />
                    </Modal.Actions>
                </Modal>
            </Transition>
            {/* Edit */}
            <Transition duration={600} divided size='huge' verticalAlign='middle' visible={open}>
                <Modal
                    size={size}
                    open={open}
                    dimmer="blurring" >
                    <Modal.Header>Edit Website</Modal.Header>
                    <Modal.Content >
                        <p >Website Name</p>
                    </Modal.Content>
                    <Input type="text" style={{ marginLeft: '20px', width: '90%' }} onChange={(event) => this._onchangeName(event)} value={this.state.webName}></Input>
                    <Modal.Content>
                        <p>Website URL</p>
                    </Modal.Content>
                    <Input type="text" style={{ marginLeft: '20px', marginBottom: '20px', width: '90%' }} defaultValue={this.props.url} disabled={true}></Input>
                    <Modal.Actions>
                        <Button onClick={this.close}>Cancel</Button>
                        <Button content='Done' color='blue' disabled={this.state.isDisable} onClick={() => this._editWebsite()} />
                    </Modal.Actions>
                </Modal>
            </Transition>


            {/* Assign */}
            <Transition duration={600} divided size='huge' verticalAlign='middle' visible={this.state.assignModal}>
                <Modal
                    size={"large"}
                    open={this.state.assignModal}
                    dimmer="blurring">
                    <Modal.Header>Assign Website</Modal.Header>
                    <Modal.Content >
                        <p >Staffs:</p>
                    </Modal.Content>
                    <Dropdown
                        multiple
                        selection
                        fluid
                        options={this.state.options}
                        placeholder='Choose an option'
                        renderLabel={renderLabel}
                        onChange={(event, data) => this._changeAssign(event, data)}
                        value={this.state.userAssign}
                    />                    <Modal.Actions>
                        <Button onClick={() => this.setState({ assignModal: false })}>Cancel</Button>
                        <Button content='Done' color='blue' disabled={this.state.isDisable} onClick={() => this._doAssignUser()} />
                    </Modal.Actions>
                </Modal>
            </Transition>

            {/* <Transition duration={600} divided size='huge' verticalAlign='middle' visible={open}>
                <Modal
                    size={size}
                    open={open}
                    dimmer="blurring" >
                    <Modal.Header>Bla bla</Modal.Header>
                    <Modal.Content >
                    </Modal.Content>
                    <Modal.Content>
                    </Modal.Content>
                    <Modal.Actions>
                    </Modal.Actions>
                </Modal>
            </Transition> */}
            <Table.Cell ><a >{this.props.no + 1}</a></Table.Cell>
            <Table.Cell ><a >{this.props.name}</a></Table.Cell>
            <Table.Cell ><a >{this.props.url}</a></Table.Cell>
            <Table.Cell ><a >{this.props.version}</a></Table.Cell>
            <Table.Cell ><a >{this.props.time}</a></Table.Cell>
            <Table.Cell ><Button onClick={() => this._makeNewver(this.props.id)} color="green">Make</Button></Table.Cell>
            <Table.Cell ><Button onClick={() => this._viewSitemap(this.props.id, this.props.name, this.props.url)} color="blue">View Sitemap</Button></Table.Cell>
            <Table.Cell ><Button primary onClick={() => this._assignModal()} > Assign </Button>
                <Button color="orange" onClick={this.show('mini')} > Edit </Button>
                <Button negative onClick={this.closeConfigShow(false, true)}> Delete</Button></Table.Cell>
        </Table.Row >
        );
    }
}