import React, { Component } from 'react';
import 'semantic-ui-css/semantic.min.css';
import { Segment, Label } from 'semantic-ui-react'
import { Cookies } from "react-cookie";

const cookies = new Cookies();

class brokenLinksScreen extends Component {
    state = { done: 0, check: { redirectWWW: false, allPageSSL: false, existErrorPage: false, redirectHTTPS: false } };
    componentDidMount() {
        this.setState({ loadingTable: true });
        var param = { "userId": cookies.get("u_id"), "userToken": cookies.get("u_token"), "websiteId": cookies.get("u_w_id") };

        fetch("http://localhost:8080/api/svbehavior", {
            method: 'POST',
            headers: {
                'Accept': 'application/json',
                'Content-Type': 'application/json'
            },
            body: JSON.stringify(param)
        }).then(response => response.json()).then((data) => {
            var doned = 0;
            if (data.redirectWWW) doned++;
            if (data.allPageSSL) doned++;
            if (data.existErrorPage) doned++;
            if (data.redirectHTTPS) doned++;

            this.setState({ done: doned, check: data });
        });
    }

    render() {
        return (
            <div>
                {this.state.check !== null ? <Segment.Group>
                    <Segment><span>Done</span> <Label style={{ fontSize: '13px', background: 'green', color: 'white' }} >{this.state.done}</Label></Segment>
                    <Segment><div style={{ fontSize: '25px', color: this.state.check.redirectWWW === false ? "red" : "green" }}>Website redirects from www to non-www.</div>
                        <div style={{ fontSize: '15px', marginTop: '10px' }}>The website should handle URLs with a www prefix and redirect them to a non-www prefix wherever possible.</div>
                    </Segment>
                    <Segment><div style={{ fontSize: '25px', color: this.state.check.redirectHTTPS === false ? "red" : "green" }}>Website redirects HTTP to HTTPS.</div>
                        <div style={{ fontSize: '15px', marginTop: '10px' }}>The website should redirect non-SSL pages to SSL pages wherever possible.</div>
                    </Segment>
                    <Segment><div style={{ fontSize: '25px', color: this.state.check.existErrorPage === false ? "red" : "green" }}>Website has a working "Page Not Found" page.</div>
                        <div style={{ fontSize: '15px', marginTop: '10px' }}>The website should display an error page when pages are not found, which must return a 404 error code.</div>
                    </Segment>
                    <Segment><div style={{ fontSize: '25px', color: this.state.check.allPageSSL === false ? "red" : "green" }}>All pages use SSL.</div>
                        <div style={{ fontSize: '15px', marginTop: '10px' }}>Google is progressively penalising websites which avoid SSL, in both search results and the Chrome browser.</div>
                    </Segment>



                </Segment.Group> : ""}
            </div>

        );
    }



}

export default brokenLinksScreen;