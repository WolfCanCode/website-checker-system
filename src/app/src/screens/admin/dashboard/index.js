import React, { Component } from 'react';
import {Segment, SegmentGroup } from 'semantic-ui-react'

export default class Dashboard extends Component {

    render() {
        return (
            <div style={{ height: 'auto' }}>

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