import React, { Component } from 'react';
import {Segment, SegmentGroup } from 'semantic-ui-react'
// import { Cookies } from "react-cookie";

// const cookies = new Cookies();
export default class Dashboard extends Component {

    render() {
        return (
            <div>

                <SegmentGroup vertical='true'>
                    <Segment><h3>Test</h3></Segment>
                    <Segment.Group horizontal>
                        <Segment basic>
                            <Segment.Group horizontal>
                                <Segment>left</Segment>
                                <Segment>right</Segment>
                            </Segment.Group>
                        </Segment>
                        <Segment basic>

                        </Segment>
                    </Segment.Group>
                </SegmentGroup>
            </div>
        );
    }
}