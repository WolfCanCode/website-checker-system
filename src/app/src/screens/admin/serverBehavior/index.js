import React, { Component } from 'react';
import 'semantic-ui-css/semantic.min.css';
import { Segment, Label } from 'semantic-ui-react'


class brokenLinksScreen extends Component {
    state = {done:0};
    componentDidMount() {
        this.setState({ loadingTable: true });
        var param = { "url": "https://www.dcpxsuvi.com/" }
        ;
        fetch("/api/svbehavior", {
            method: 'POST',
            headers: {
                'Accept': 'application/json',
                'Content-Type': 'application/json'
            },
            body: JSON.stringify(param)
        }).then(response => response.json()).then((data) => {
            this.setState({done:4});
        });
    }

    render() {
        return (
            <div>
                <Segment.Group>
                    <Segment><span>Done</span> <Label style={{ fontSize: '13px',background:'green', color:'white' }} >{this.state.done}</Label></Segment>
                    {this.state.done ===0 ? "" : <div><Segment><div style={{ fontSize: '25px' }}>Website redirects from www to non-www.</div>
                        <div style={{ fontSize: '15px', marginTop: '10px' }}>The website should handle URLs with a www prefix and redirect them to a non-www prefix wherever possible.</div>
                    </Segment>
                    <Segment><div style={{ fontSize: '25px' }}>Website redirects HTTP to HTTPS.</div>
                        <div style={{ fontSize: '15px', marginTop: '10px' }}>The website should redirect non-SSL pages to SSL pages wherever possible.</div>
                    </Segment>
                    <Segment><div style={{ fontSize: '25px' }}>Website has a working "Page Not Found" page.</div>
                        <div style={{ fontSize: '15px', marginTop: '10px' }}>The website should display an error page when pages are not found, which must return a 404 error code.</div>
                    </Segment>
                    <Segment><div style={{ fontSize: '25px' }}>All pages use SSL.</div>
                        <div style={{ fontSize: '15px', marginTop: '10px' }}>Google is progressively penalising websites which avoid SSL, in both search results and the Chrome browser.</div>
                    </Segment></div>}



                </Segment.Group>
            </div>

        );
    }



}

export default brokenLinksScreen;