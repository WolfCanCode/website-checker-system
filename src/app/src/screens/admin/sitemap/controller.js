import React, { Component } from 'react';
import { Segment, Input, Button } from 'semantic-ui-react';
import ModelSitemap from './modal';
import Canvas from './canvas';

export default class SiteMap extends Component {
    constructor(props) {
        super(props);
        this.state = { urlRoot: "", map: [], typeMap: [], urlMap: [], isDisabled: false, isLoading: false };
        this.handleChange = this.handleChange.bind(this);
        this.handleSubmit = this.handleSubmit.bind(this);
    }


    handleChange(event) {
        this.setState({ urlRoot: event.target.value });
    }

    handleSubmit(event) {
        // alert('A URL was submitted: ' + this.state.urlRoot);
        event.preventDefault();
    }

    getSitemap() {
        this.setState({ isDisabled: true, isLoading: true });
        var inputParam = { "url": this.state.urlRoot };
        var result = [];

        fetch("/api/sitemap", {
            method: 'POST',
            headers: {
                'Accept': 'application/json',
                'Content-Type': 'application/json'
            },
            body: JSON.stringify(inputParam)
        }).then(response => response.json()).then((data) => {
            // console.log("Data: " + data);
            // var ex = JSON.parse(JSON.stringify(data));
            // console.log("EX: " + ex);
            // data[9]
            result = data.map((item, index) => {
                console.log("Index: " + index);
                console.log("Map: " + item.map);
                console.log("Type: " + item.typeMap);
                console.log("Url: " + item.urlMap);

                var mapArray = eval("[" + item.map + "]");
                // console.log("[0]: " + mapArray[0]);
                // console.log("[1]: " + mapArray[1]);
                // console.log("[2]: " + mapArray[2]);
                var typeArray = eval("[" + item.typeMap + "]");
                var urlArray = eval("[" + item.urlMap + "]");

                // console.log("Is array: " + Array.isArray(array_of_objects));

                this.setState({ map: mapArray, typeMap: typeArray, urlMap: urlArray });
                // console.log("[0]: " + this.state.map[0]);
                // console.log("[1]: " + this.state.map[1]);
                // console.log("[2]: " + this.state.map[2]);
                // this.setState({ typeMap: typeArray });
                // this.setState({ urlMap: urlArray });

                // alert("Map: " + this.state.map);
                // alert("Type: " + this.state.typeMap);
                // alert("Url: " + this.state.urlMap);

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
            context.font = '12px serif';

            var boxW = 300;
            var boxH = 25;

            var halfBoxH = boxH / 2;

            var nfloor = map.length;

            var mxWidth = nfloor * boxW + (nfloor - 1) * 50 + 10 + 10;
            var mxHeight = map[nfloor - 1].length * (boxH + 10) + 20;

            var StartDrawingX = mxWidth - 10 - boxW;
            var StartDrawingY = 10;
            function addTextToBox(coordX, coordY, fontType, content) {
                context.save();
                context.fillStyle = "black";
                context.font = content;
                context.textAlign = 'left';
                context.translate(coordX + 10, coordY + boxH - 10);
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
                            addTextToBox(deep, curRow, '12px serif', urlMap[floor][i]);
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
                            addTextToBox(deep, coord[i], '12px serif', urlMap[floor][i]);
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
                    // pop-up the url
                    alert("Your URL: " + rectCoord[id].url);
                }
            });
            
        });

    }

    render() {
        return (
            <Segment basic style={{ minHeight: '450px' }}>
                <form onSubmit={this.handleSubmit}>
                    <Input type="text" value={this.state.urlRoot} placeholder='Input your website URL...'
                        onChange={this.handleChange}
                        focus ref="urlInput"
                        style={{ margin: '5px', width: '400px' }} />
                    <Button content="Get Sitemap" primary type="Submit"
                        disabled={this.state.isDisabled} onClick={() => this.getSitemap()} />
                </form>
                <Segment loading={this.state.isLoading} secondary style={{ maxHeight: '350px', overflow: 'scroll' }} >
                   <Canvas/>
                </Segment>
                
            </Segment>
        );

    }
}