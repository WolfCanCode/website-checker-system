import React, { Component } from 'react';
import { Segment, SegmentGroup } from 'semantic-ui-react'
import PieChart from 'react-minimal-pie-chart';

export default class Dashboard1 extends Component {

    render() {
        return (
            <div style={{ height: 'auto' }}>

                <SegmentGroup vertical='true'>
                    <Segment.Group horizontal>

                        <Segment basic>
                            <PieChart
                                ratio={2}
                                radius={20}
                                cx={20}
                                cy={20}
                                data={[
                                    { title: 'One', value: 10, color: '#E38627' },
                                    { title: 'Two', value: 15, color: '#C13C37' },
                                    { title: 'Three', value: 20, color: '#6A2135' },
                                ]}
                            />
                        </Segment>
                        <Segment basic>
                        </Segment>
                        <Segment >

                        </Segment>
                        <Segment >

                        </Segment>
                    </Segment.Group>
                </SegmentGroup>
            </div>
        );
    }
}