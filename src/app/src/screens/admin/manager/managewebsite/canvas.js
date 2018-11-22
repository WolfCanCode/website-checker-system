import React, { Component } from 'react';
export default class Canvas extends Component {
    render() {
        return (
            <canvas id="myCanvas" ref="myCanvas" >
                The canvas display Website's sitemap
        </canvas>
        );
    }
}