import React, { Component } from 'react';
import { Segment, Image } from 'semantic-ui-react';
import lock from '../../.././assets/lock.png';








class Error404 extends Component {

    render() {
        return (
            <Segment><h1>404</h1><h3>This page is not found or you dont have permission to show it</h3><Image src={lock} /></Segment>



        );
    }
    s


}

export default Error404;