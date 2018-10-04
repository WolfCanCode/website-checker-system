import React, { Component } from 'react';
import 'semantic-ui-css/semantic.min.css';
import {Segment, Label } from 'semantic-ui-react'


class brokenLinksScreen extends Component {

    render() {
        return (
            <div style={{ height: 'auto', marginTop: '20px' }}>
                <Segment.Group>
                    <Segment><span>Done</span> <Label style={{ fontSize: '13px' }} >4</Label></Segment>
                    <Segment><div style={{ fontSize: '25px' }}>Website redirects from www to non-www.</div>
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
                    </Segment>
                    


                </Segment.Group>
            </div>

        );
    }



}

export default brokenLinksScreen;