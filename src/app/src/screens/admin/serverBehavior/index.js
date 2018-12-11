import React, { Component } from 'react';
import 'semantic-ui-css/semantic.min.css';
import { Segment, Label } from 'semantic-ui-react'
import { Cookies } from "react-cookie";

const cookies = new Cookies();

class brokenLinksScreen extends Component {
    state = { done: 0, check: { redirectWWW: false, allPageSSL: false, existErrorPage: false, redirectHTTPS: false } };
    componentDidMount() {

    }

    render() {
        return (
            <div>

            </div>

        );
    }



}

export default brokenLinksScreen;