import React, { Component } from 'react';
import { Segment, Input, Button } from 'semantic-ui-react';
// import ModelSitemap from './modal';
import Canvas from './canvas';
import { Cookies } from "react-cookie";

const cookies = new Cookies();
export default class SiteMap extends Component {
    constructor(props) {
        super(props);
        this.state = { urlRoot: "", map: [], typeMap: [], urlMap: [], isDisabled: false, isLoading: false };
        
       
    }

    componentDidMount() {
        this._viewSitemap()
    }
    _viewSitemap() {
        // eslint-disable-next-line
        var result = [];
        var urlRoot = "";
        
        var param = { "userId": cookies.get("u_id"), "userToken": cookies.get("u_token"), "websiteId": cookies.get("u_w_id"), };
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
            urlRoot = this.state.urlMap[0];
            var urlLength = urlRoot.length;

            var canvas = document.getElementById("myCanvas1");
            var context = canvas.getContext("2d");
            var rectCoord = [];
            context.beginPath();
            context.font = '18px serif';

            // var boxW = 300;
            // var boxH = 25;
            var boxW = 280;
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

                if (content === urlRoot) {

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
            // this.props.loadingTable(false);
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

                    // that.props.setSelectedRectValue(selectedRectangleValue);
                }
            });

        });
    }
    render() {
        
        return (
            <canvas id="myCanvas1" ref="myCanvas" >
                The canvas display Website's sitemap
        </canvas>
        );

    }
}