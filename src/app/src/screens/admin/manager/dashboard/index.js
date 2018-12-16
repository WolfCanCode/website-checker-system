import React, { Component } from 'react';
import { Segment, Feed, Card } from 'semantic-ui-react'
// import { Cookies } from "react-cookie";

// const cookies = new Cookies();
export default class DashboardManager extends Component {
    state = { activeItem: 'home' };

    render() {
        return (
            <Segment.Group>
                <Segment.Group style={{ border: 0, margin: 'auto', marginTop: 10 }} horizontal>
                    <Card color='red' style={{ margin: 'auto', marginTop: 10 }}>
                        <Card.Content style={{ background: '#ef5350' }}>
                            <Card.Header style={{ color: 'white' }}>Spelling test</Card.Header>
                        </Card.Content>
                        <Card.Content>
                            <Feed>
                                <Feed.Event>
                                    <Feed.Label style={{ color: 'blue', fontSize: 18 }}><strong>Staff1</strong></Feed.Label>
                                    <Feed.Content>
                                        <Feed.Date content='16/12/2018 19:30:50' />
                                        <Feed.Summary>
                                            made a <a>report </a> with page option <a>test123</a> at website <a>tiki.vn</a>.
                                         </Feed.Summary>
                                    </Feed.Content>
                                </Feed.Event>

                                <Feed.Event>
                                    <Feed.Label style={{ color: 'blue', fontSize: 18 }}><strong>Staff1</strong></Feed.Label>
                                    <Feed.Content>
                                        <Feed.Date content='16/12/2018 19:30:50' />
                                        <Feed.Summary>
                                            made a <a>report</a> with page option <a>test123</a> at website <a>tiki.vn</a>.
                                         </Feed.Summary>
                                    </Feed.Content>
                                </Feed.Event><Feed.Event>
                                    <Feed.Label style={{ color: 'blue', fontSize: 18 }}><strong>Staff1</strong></Feed.Label>
                                    <Feed.Content>
                                        <Feed.Date content='16/12/2018 19:30:50' />
                                        <Feed.Summary>
                                            made a <a>report </a> with page option <a>test123</a> at website <a>tiki.vn</a>.
                                         </Feed.Summary>
                                    </Feed.Content>
                                </Feed.Event><Feed.Event>
                                    <Feed.Label style={{ color: 'blue', fontSize: 18 }}><strong>Staff1</strong></Feed.Label>
                                    <Feed.Content>
                                        <Feed.Date content='16/12/2018 19:30:50' />
                                        <Feed.Summary>
                                            made a <a>report </a> with page option <a>test123</a> at website <a>tiki.vn</a>.
                                         </Feed.Summary>
                                    </Feed.Content>
                                </Feed.Event>
                            </Feed>
                        </Card.Content>
                    </Card>
                    <Card color='orange' style={{ margin: 'auto', marginTop: 10 }}>
                        <Card.Content style={{ background: '#FF7043' }}>
                            <Card.Header style={{ color: 'white' }}>Grammar test</Card.Header>
                        </Card.Content>
                        <Card.Content>
                            <Feed>
                                <Feed.Event>
                                    <Feed.Label style={{ color: 'blue', fontSize: 18 }}><strong>Staff1</strong></Feed.Label>
                                    <Feed.Content>
                                        <Feed.Date content='16/12/2018 19:30:50' />
                                        <Feed.Summary>
                                            made a <a>report </a> with page option <a>test123</a> at website <a>tiki.vn</a>.
                                         </Feed.Summary>
                                    </Feed.Content>
                                </Feed.Event>

                                <Feed.Event>
                                    <Feed.Label style={{ color: 'blue', fontSize: 18 }}><strong>Staff1</strong></Feed.Label>
                                    <Feed.Content>
                                        <Feed.Date content='16/12/2018 19:30:50' />
                                        <Feed.Summary>
                                            made a <a>report</a> with page option <a>test123</a> at website <a>tiki.vn</a>.
                                         </Feed.Summary>
                                    </Feed.Content>
                                </Feed.Event><Feed.Event>
                                    <Feed.Label style={{ color: 'blue', fontSize: 18 }}><strong>Staff1</strong></Feed.Label>
                                    <Feed.Content>
                                        <Feed.Date content='16/12/2018 19:30:50' />
                                        <Feed.Summary>
                                            made a <a>report </a> with page option <a>test123</a> at website <a>tiki.vn</a>.
                                         </Feed.Summary>
                                    </Feed.Content>
                                </Feed.Event><Feed.Event>
                                    <Feed.Label style={{ color: 'blue', fontSize: 18 }}><strong>Staff1</strong></Feed.Label>
                                    <Feed.Content>
                                        <Feed.Date content='16/12/2018 19:30:50' />
                                        <Feed.Summary>
                                            made a <a>report </a> with page option <a>test123</a> at website <a>tiki.vn</a>.
                                         </Feed.Summary>
                                    </Feed.Content>
                                </Feed.Event>
                            </Feed>
                        </Card.Content>
                    </Card>
                    <Card color='yellow' style={{ margin: 'auto', marginTop: 10 }}>
                        <Card.Content style={{ background: '#FFA726' }}>
                            <Card.Header style={{ color: 'white' }}>Broken links test</Card.Header>
                        </Card.Content>
                        <Card.Content>
                            <Feed>
                                <Feed.Event>
                                    <Feed.Label style={{ color: 'blue', fontSize: 18 }}><strong>Staff1</strong></Feed.Label>
                                    <Feed.Content>
                                        <Feed.Date content='16/12/2018 19:30:50' />
                                        <Feed.Summary>
                                            made a <a>report </a> with page option <a>test123</a> at website <a>tiki.vn</a>.
                                         </Feed.Summary>
                                    </Feed.Content>
                                </Feed.Event>

                                <Feed.Event>
                                    <Feed.Label style={{ color: 'blue', fontSize: 18 }}><strong>Staff1</strong></Feed.Label>
                                    <Feed.Content>
                                        <Feed.Date content='16/12/2018 19:30:50' />
                                        <Feed.Summary>
                                            made a <a>report</a> with page option <a>test123</a> at website <a>tiki.vn</a>.
                                         </Feed.Summary>
                                    </Feed.Content>
                                </Feed.Event><Feed.Event>
                                    <Feed.Label style={{ color: 'blue', fontSize: 18 }}><strong>Staff1</strong></Feed.Label>
                                    <Feed.Content>
                                        <Feed.Date content='16/12/2018 19:30:50' />
                                        <Feed.Summary>
                                            made a <a>report </a> with page option <a>test123</a> at website <a>tiki.vn</a>.
                                         </Feed.Summary>
                                    </Feed.Content>
                                </Feed.Event><Feed.Event>
                                    <Feed.Label style={{ color: 'blue', fontSize: 18 }}><strong>Staff1</strong></Feed.Label>
                                    <Feed.Content>
                                        <Feed.Date content='16/12/2018 19:30:50' />
                                        <Feed.Summary>
                                            made a <a>report </a> with page option <a>test123</a> at website <a>tiki.vn</a>.
                                         </Feed.Summary>
                                    </Feed.Content>
                                </Feed.Event>
                            </Feed>
                        </Card.Content>
                    </Card>
                    <Card color='olive' style={{ margin: 'auto', marginTop: 10 }}>
                        <Card.Content style={{ background: '#9CCC65' }}>
                            <Card.Header style={{ color: 'white' }}>Broken pages test</Card.Header>
                        </Card.Content>
                        <Card.Content>
                            <Feed>
                                <Feed.Event>
                                    <Feed.Label style={{ color: 'blue', fontSize: 18 }}><strong>Staff1</strong></Feed.Label>
                                    <Feed.Content>
                                        <Feed.Date content='16/12/2018 19:30:50' />
                                        <Feed.Summary>
                                            made a <a>report </a> with page option <a>test123</a> at website <a>tiki.vn</a>.
                                         </Feed.Summary>
                                    </Feed.Content>
                                </Feed.Event>

                                <Feed.Event>
                                    <Feed.Label style={{ color: 'blue', fontSize: 18 }}><strong>Staff1</strong></Feed.Label>
                                    <Feed.Content>
                                        <Feed.Date content='16/12/2018 19:30:50' />
                                        <Feed.Summary>
                                            made a <a>report</a> with page option <a>test123</a> at website <a>tiki.vn</a>.
                                         </Feed.Summary>
                                    </Feed.Content>
                                </Feed.Event><Feed.Event>
                                    <Feed.Label style={{ color: 'blue', fontSize: 18 }}><strong>Staff1</strong></Feed.Label>
                                    <Feed.Content>
                                        <Feed.Date content='16/12/2018 19:30:50' />
                                        <Feed.Summary>
                                            made a <a>report </a> with page option <a>test123</a> at website <a>tiki.vn</a>.
                                         </Feed.Summary>
                                    </Feed.Content>
                                </Feed.Event><Feed.Event>
                                    <Feed.Label style={{ color: 'blue', fontSize: 18 }}><strong>Staff1</strong></Feed.Label>
                                    <Feed.Content>
                                        <Feed.Date content='16/12/2018 19:30:50' />
                                        <Feed.Summary>
                                            made a <a>report </a> with page option <a>test123</a> at website <a>tiki.vn</a>.
                                         </Feed.Summary>
                                    </Feed.Content>
                                </Feed.Event>
                            </Feed>
                        </Card.Content>
                    </Card>
                    <Card color='green' style={{ margin: 'auto', marginTop: 10 }}>
                        <Card.Content style={{ background: '#66BB6A' }}>
                            <Card.Header style={{ color: 'white' }}>Missing files test</Card.Header>
                        </Card.Content>
                        <Card.Content>
                            <Feed>
                                <Feed.Event>
                                    <Feed.Label style={{ color: 'blue', fontSize: 18 }}><strong>Staff1</strong></Feed.Label>
                                    <Feed.Content>
                                        <Feed.Date content='16/12/2018 19:30:50' />
                                        <Feed.Summary>
                                            made a <a>report </a> with page option <a>test123</a> at website <a>tiki.vn</a>.
                                         </Feed.Summary>
                                    </Feed.Content>
                                </Feed.Event>

                                <Feed.Event>
                                    <Feed.Label style={{ color: 'blue', fontSize: 18 }}><strong>Staff1</strong></Feed.Label>
                                    <Feed.Content>
                                        <Feed.Date content='16/12/2018 19:30:50' />
                                        <Feed.Summary>
                                            made a <a>report</a> with page option <a>test123</a> at website <a>tiki.vn</a>.
                                         </Feed.Summary>
                                    </Feed.Content>
                                </Feed.Event><Feed.Event>
                                    <Feed.Label style={{ color: 'blue', fontSize: 18 }}><strong>Staff1</strong></Feed.Label>
                                    <Feed.Content>
                                        <Feed.Date content='16/12/2018 19:30:50' />
                                        <Feed.Summary>
                                            made a <a>report </a> with page option <a>test123</a> at website <a>tiki.vn</a>.
                                         </Feed.Summary>
                                    </Feed.Content>
                                </Feed.Event><Feed.Event>
                                    <Feed.Label style={{ color: 'blue', fontSize: 18 }}><strong>Staff1</strong></Feed.Label>
                                    <Feed.Content>
                                        <Feed.Date content='16/12/2018 19:30:50' />
                                        <Feed.Summary>
                                            made a <a>report </a> with page option <a>test123</a> at website <a>tiki.vn</a>.
                                         </Feed.Summary>
                                    </Feed.Content>
                                </Feed.Event>
                            </Feed>
                        </Card.Content>
                    </Card>

                </Segment.Group>
                <Segment.Group style={{ border: 0, margin: 'auto', marginTop: 10 }} horizontal>
                    <Card color='teal' style={{ margin: 'auto', marginTop: 10 }}>
                        <Card.Content style={{ background: '#26C6DA' }}>
                            <Card.Header style={{ color: 'white' }}>Prohibited content test</Card.Header>
                        </Card.Content>
                        <Card.Content>
                            <Feed>
                                <Feed.Event>
                                    <Feed.Label style={{ color: 'blue', fontSize: 18 }}><strong>Staff1</strong></Feed.Label>
                                    <Feed.Content>
                                        <Feed.Date content='16/12/2018 19:30:50' />
                                        <Feed.Summary>
                                            made a <a>report </a> with page option <a>test123</a> at website <a>tiki.vn</a>.
                                         </Feed.Summary>
                                    </Feed.Content>
                                </Feed.Event>

                                <Feed.Event>
                                    <Feed.Label style={{ color: 'blue', fontSize: 18 }}><strong>Staff1</strong></Feed.Label>
                                    <Feed.Content>
                                        <Feed.Date content='16/12/2018 19:30:50' />
                                        <Feed.Summary>
                                            made a <a>report</a> with page option <a>test123</a> at website <a>tiki.vn</a>.
                                         </Feed.Summary>
                                    </Feed.Content>
                                </Feed.Event><Feed.Event>
                                    <Feed.Label style={{ color: 'blue', fontSize: 18 }}><strong>Staff1</strong></Feed.Label>
                                    <Feed.Content>
                                        <Feed.Date content='16/12/2018 19:30:50' />
                                        <Feed.Summary>
                                            made a <a>report </a> with page option <a>test123</a> at website <a>tiki.vn</a>.
                                         </Feed.Summary>
                                    </Feed.Content>
                                </Feed.Event><Feed.Event>
                                    <Feed.Label style={{ color: 'blue', fontSize: 18 }}><strong>Staff1</strong></Feed.Label>
                                    <Feed.Content>
                                        <Feed.Date content='16/12/2018 19:30:50' />
                                        <Feed.Summary>
                                            made a <a>report </a> with page option <a>test123</a> at website <a>tiki.vn</a>.
                                         </Feed.Summary>
                                    </Feed.Content>
                                </Feed.Event>
                            </Feed>
                        </Card.Content>
                    </Card>
                    <Card color='blue' style={{ margin: 'auto', marginTop: 10 }}>
                        <Card.Content style={{ background: '#29B6F6' }}>
                            <Card.Header style={{ color: 'white' }}>Mobile layout test</Card.Header>
                        </Card.Content>
                        <Card.Content>
                            <Feed>
                                <Feed.Event>
                                    <Feed.Label style={{ color: 'blue', fontSize: 18 }}><strong>Staff1</strong></Feed.Label>
                                    <Feed.Content>
                                        <Feed.Date content='16/12/2018 19:30:50' />
                                        <Feed.Summary>
                                            made a <a>report </a> with page option <a>test123</a> at website <a>tiki.vn</a>.
                                         </Feed.Summary>
                                    </Feed.Content>
                                </Feed.Event>

                                <Feed.Event>
                                    <Feed.Label style={{ color: 'blue', fontSize: 18 }}><strong>Staff1</strong></Feed.Label>
                                    <Feed.Content>
                                        <Feed.Date content='16/12/2018 19:30:50' />
                                        <Feed.Summary>
                                            made a <a>report</a> with page option <a>test123</a> at website <a>tiki.vn</a>.
                                         </Feed.Summary>
                                    </Feed.Content>
                                </Feed.Event><Feed.Event>
                                    <Feed.Label style={{ color: 'blue', fontSize: 18 }}><strong>Staff1</strong></Feed.Label>
                                    <Feed.Content>
                                        <Feed.Date content='16/12/2018 19:30:50' />
                                        <Feed.Summary>
                                            made a <a>report </a> with page option <a>test123</a> at website <a>tiki.vn</a>.
                                         </Feed.Summary>
                                    </Feed.Content>
                                </Feed.Event><Feed.Event>
                                    <Feed.Label style={{ color: 'blue', fontSize: 18 }}><strong>Staff1</strong></Feed.Label>
                                    <Feed.Content>
                                        <Feed.Date content='16/12/2018 19:30:50' />
                                        <Feed.Summary>
                                            made a <a>report </a> with page option <a>test123</a> at website <a>tiki.vn</a>.
                                         </Feed.Summary>
                                    </Feed.Content>
                                </Feed.Event>
                            </Feed>
                        </Card.Content>
                    </Card>

                    <Card color='violet' style={{ margin: 'auto', marginTop: 10 }}>
                        <Card.Content style={{ background: '#7E57C2' }}>
                            <Card.Header style={{ color: 'white' }}>Speed test</Card.Header>
                        </Card.Content>
                        <Card.Content>
                            <Feed>
                                <Feed.Event>
                                    <Feed.Label style={{ color: 'blue', fontSize: 18 }}><strong>Staff1</strong></Feed.Label>
                                    <Feed.Content>
                                        <Feed.Date content='16/12/2018 19:30:50' />
                                        <Feed.Summary>
                                            made a <a>report </a> with page option <a>test123</a> at website <a>tiki.vn</a>.
                                         </Feed.Summary>
                                    </Feed.Content>
                                </Feed.Event>

                                <Feed.Event>
                                    <Feed.Label style={{ color: 'blue', fontSize: 18 }}><strong>Staff1</strong></Feed.Label>
                                    <Feed.Content>
                                        <Feed.Date content='16/12/2018 19:30:50' />
                                        <Feed.Summary>
                                            made a <a>report</a> with page option <a>test123</a> at website <a>tiki.vn</a>.
                                         </Feed.Summary>
                                    </Feed.Content>
                                </Feed.Event><Feed.Event>
                                    <Feed.Label style={{ color: 'blue', fontSize: 18 }}><strong>Staff1</strong></Feed.Label>
                                    <Feed.Content>
                                        <Feed.Date content='16/12/2018 19:30:50' />
                                        <Feed.Summary>
                                            made a <a>report </a> with page option <a>test123</a> at website <a>tiki.vn</a>.
                                         </Feed.Summary>
                                    </Feed.Content>
                                </Feed.Event><Feed.Event>
                                    <Feed.Label style={{ color: 'blue', fontSize: 18 }}><strong>Staff1</strong></Feed.Label>
                                    <Feed.Content>
                                        <Feed.Date content='16/12/2018 19:30:50' />
                                        <Feed.Summary>
                                            made a <a>report </a> with page option <a>test123</a> at website <a>tiki.vn</a>.
                                         </Feed.Summary>
                                    </Feed.Content>
                                </Feed.Event>
                            </Feed>
                        </Card.Content>
                    </Card>

                    <Card color='purple' style={{ margin: 'auto', marginTop: 10 }}>
                        <Card.Content style={{ background: '#AB47BC' }}>
                            <Card.Header style={{ color: 'white' }}>Javascript test</Card.Header>
                        </Card.Content>
                        <Card.Content>
                            <Feed>
                                <Feed.Event>
                                    <Feed.Label style={{ color: 'blue', fontSize: 18 }}><strong>Staff1</strong></Feed.Label>
                                    <Feed.Content>
                                        <Feed.Date content='16/12/2018 19:30:50' />
                                        <Feed.Summary>
                                            made a <a>report </a> with page option <a>test123</a> at website <a>tiki.vn</a>.
                                         </Feed.Summary>
                                    </Feed.Content>
                                </Feed.Event>

                                <Feed.Event>
                                    <Feed.Label style={{ color: 'blue', fontSize: 18 }}><strong>Staff1</strong></Feed.Label>
                                    <Feed.Content>
                                        <Feed.Date content='16/12/2018 19:30:50' />
                                        <Feed.Summary>
                                            made a <a>report</a> with page option <a>test123</a> at website <a>tiki.vn</a>.
                                         </Feed.Summary>
                                    </Feed.Content>
                                </Feed.Event><Feed.Event>
                                    <Feed.Label style={{ color: 'blue', fontSize: 18 }}><strong>Staff1</strong></Feed.Label>
                                    <Feed.Content>
                                        <Feed.Date content='16/12/2018 19:30:50' />
                                        <Feed.Summary>
                                            made a <a>report </a> with page option <a>test123</a> at website <a>tiki.vn</a>.
                                         </Feed.Summary>
                                    </Feed.Content>
                                </Feed.Event><Feed.Event>
                                    <Feed.Label style={{ color: 'blue', fontSize: 18 }}><strong>Staff1</strong></Feed.Label>
                                    <Feed.Content>
                                        <Feed.Date content='16/12/2018 19:30:50' />
                                        <Feed.Summary>
                                            made a <a>report </a> with page option <a>test123</a> at website <a>tiki.vn</a>.
                                         </Feed.Summary>
                                    </Feed.Content>
                                </Feed.Event>
                            </Feed>
                        </Card.Content>
                    </Card>

                    <Card color='pink' style={{ margin: 'auto', marginTop: 10 }}>
                        <Card.Content style={{ background: '#EC407A' }}>
                            <Card.Header style={{ color: 'white' }}>Favicon test</Card.Header>
                        </Card.Content>
                        <Card.Content>
                            <Feed>
                                <Feed.Event>
                                    <Feed.Label style={{ color: 'blue', fontSize: 18 }}><strong>Staff1</strong></Feed.Label>
                                    <Feed.Content>
                                        <Feed.Date content='16/12/2018 19:30:50' />
                                        <Feed.Summary>
                                            made a <a>report </a> with page option <a>test123</a> at website <a>tiki.vn</a>.
                                         </Feed.Summary>
                                    </Feed.Content>
                                </Feed.Event>

                                <Feed.Event>
                                    <Feed.Label style={{ color: 'blue', fontSize: 18 }}><strong>Staff1</strong></Feed.Label>
                                    <Feed.Content>
                                        <Feed.Date content='16/12/2018 19:30:50' />
                                        <Feed.Summary>
                                            made a <a>report</a> with page option <a>test123</a> at website <a>tiki.vn</a>.
                                         </Feed.Summary>
                                    </Feed.Content>
                                </Feed.Event><Feed.Event>
                                    <Feed.Label style={{ color: 'blue', fontSize: 18 }}><strong>Staff1</strong></Feed.Label>
                                    <Feed.Content>
                                        <Feed.Date content='16/12/2018 19:30:50' />
                                        <Feed.Summary>
                                            made a <a>report </a> with page option <a>test123</a> at website <a>tiki.vn</a>.
                                         </Feed.Summary>
                                    </Feed.Content>
                                </Feed.Event><Feed.Event>
                                    <Feed.Label style={{ color: 'blue', fontSize: 18 }}><strong>Staff1</strong></Feed.Label>
                                    <Feed.Content>
                                        <Feed.Date content='16/12/2018 19:30:50' />
                                        <Feed.Summary>
                                            made a <a>report </a> with page option <a>test123</a> at website <a>tiki.vn</a>.
                                         </Feed.Summary>
                                    </Feed.Content>
                                </Feed.Event>
                            </Feed>
                        </Card.Content>
                    </Card>


                </Segment.Group>
                <Segment.Group style={{ border: 0, margin: 'auto', marginTop: 10 }} horizontal>
                    <Card color='brown' style={{ margin: 'auto', marginTop: 10 }}>
                        <Card.Content style={{ background: '#8D6E63' }}>
                            <Card.Header style={{ color: 'white' }}>Law cookie test</Card.Header>
                        </Card.Content>
                        <Card.Content>
                            <Feed>
                                <Feed.Event>
                                    <Feed.Label style={{ color: 'blue', fontSize: 18 }}><strong>Staff1</strong></Feed.Label>
                                    <Feed.Content>
                                        <Feed.Date content='16/12/2018 19:30:50' />
                                        <Feed.Summary>
                                            made a <a>report </a> with page option <a>test123</a> at website <a>tiki.vn</a>.
                                         </Feed.Summary>
                                    </Feed.Content>
                                </Feed.Event>

                                <Feed.Event>
                                    <Feed.Label style={{ color: 'blue', fontSize: 18 }}><strong>Staff1</strong></Feed.Label>
                                    <Feed.Content>
                                        <Feed.Date content='16/12/2018 19:30:50' />
                                        <Feed.Summary>
                                            made a <a>report</a> with page option <a>test123</a> at website <a>tiki.vn</a>.
                                         </Feed.Summary>
                                    </Feed.Content>
                                </Feed.Event><Feed.Event>
                                    <Feed.Label style={{ color: 'blue', fontSize: 18 }}><strong>Staff1</strong></Feed.Label>
                                    <Feed.Content>
                                        <Feed.Date content='16/12/2018 19:30:50' />
                                        <Feed.Summary>
                                            made a <a>report </a> with page option <a>test123</a> at website <a>tiki.vn</a>.
                                         </Feed.Summary>
                                    </Feed.Content>
                                </Feed.Event><Feed.Event>
                                    <Feed.Label style={{ color: 'blue', fontSize: 18 }}><strong>Staff1</strong></Feed.Label>
                                    <Feed.Content>
                                        <Feed.Date content='16/12/2018 19:30:50' />
                                        <Feed.Summary>
                                            made a <a>report </a> with page option <a>test123</a> at website <a>tiki.vn</a>.
                                         </Feed.Summary>
                                    </Feed.Content>
                                </Feed.Event>
                            </Feed>
                        </Card.Content>
                    </Card>

                    <Card color='grey' style={{ margin: 'auto', marginTop: 10 }}>
                        <Card.Content style={{ background: '#9E9E9E' }}>
                            <Card.Header style={{ color: 'white' }}>Server behavior test</Card.Header>
                        </Card.Content>
                        <Card.Content>
                            <Feed>
                                <Feed.Event>
                                    <Feed.Label style={{ color: 'blue', fontSize: 18 }}><strong>Staff1</strong></Feed.Label>
                                    <Feed.Content>
                                        <Feed.Date content='16/12/2018 19:30:50' />
                                        <Feed.Summary>
                                            made a <a>report </a> with page option <a>test123</a> at website <a>tiki.vn</a>.
                                         </Feed.Summary>
                                    </Feed.Content>
                                </Feed.Event>

                                <Feed.Event>
                                    <Feed.Label style={{ color: 'blue', fontSize: 18 }}><strong>Staff1</strong></Feed.Label>
                                    <Feed.Content>
                                        <Feed.Date content='16/12/2018 19:30:50' />
                                        <Feed.Summary>
                                            made a <a>report</a> with page option <a>test123</a> at website <a>tiki.vn</a>.
                                         </Feed.Summary>
                                    </Feed.Content>
                                </Feed.Event><Feed.Event>
                                    <Feed.Label style={{ color: 'blue', fontSize: 18 }}><strong>Staff1</strong></Feed.Label>
                                    <Feed.Content>
                                        <Feed.Date content='16/12/2018 19:30:50' />
                                        <Feed.Summary>
                                            made a <a>report </a> with page option <a>test123</a> at website <a>tiki.vn</a>.
                                         </Feed.Summary>
                                    </Feed.Content>
                                </Feed.Event><Feed.Event>
                                    <Feed.Label style={{ color: 'blue', fontSize: 18 }}><strong>Staff1</strong></Feed.Label>
                                    <Feed.Content>
                                        <Feed.Date content='16/12/2018 19:30:50' />
                                        <Feed.Summary>
                                            made a <a>report </a> with page option <a>test123</a> at website <a>tiki.vn</a>.
                                         </Feed.Summary>
                                    </Feed.Content>
                                </Feed.Event>
                            </Feed>
                        </Card.Content>
                    </Card>

                    <Card color='red' style={{ margin: 'auto', marginTop: 10 }}>
                        <Card.Content style={{ background: '#f44336' }}>
                            <Card.Header style={{ color: 'white' }}>Contact detail test</Card.Header>
                        </Card.Content>
                        <Card.Content>
                            <Feed>
                                <Feed.Event>
                                    <Feed.Label style={{ color: 'blue', fontSize: 18 }}><strong>Staff1</strong></Feed.Label>
                                    <Feed.Content>
                                        <Feed.Date content='16/12/2018 19:30:50' />
                                        <Feed.Summary>
                                            made a <a>report </a> with page option <a>test123</a> at website <a>tiki.vn</a>.
                                         </Feed.Summary>
                                    </Feed.Content>
                                </Feed.Event>

                                <Feed.Event>
                                    <Feed.Label style={{ color: 'blue', fontSize: 18 }}><strong>Staff1</strong></Feed.Label>
                                    <Feed.Content>
                                        <Feed.Date content='16/12/2018 19:30:50' />
                                        <Feed.Summary>
                                            made a <a>report</a> with page option <a>test123</a> at website <a>tiki.vn</a>.
                                         </Feed.Summary>
                                    </Feed.Content>
                                </Feed.Event><Feed.Event>
                                    <Feed.Label style={{ color: 'blue', fontSize: 18 }}><strong>Staff1</strong></Feed.Label>
                                    <Feed.Content>
                                        <Feed.Date content='16/12/2018 19:30:50' />
                                        <Feed.Summary>
                                            made a <a>report </a> with page option <a>test123</a> at website <a>tiki.vn</a>.
                                         </Feed.Summary>
                                    </Feed.Content>
                                </Feed.Event><Feed.Event>
                                    <Feed.Label style={{ color: 'blue', fontSize: 18 }}><strong>Staff1</strong></Feed.Label>
                                    <Feed.Content>
                                        <Feed.Date content='16/12/2018 19:30:50' />
                                        <Feed.Summary>
                                            made a <a>report </a> with page option <a>test123</a> at website <a>tiki.vn</a>.
                                         </Feed.Summary>
                                    </Feed.Content>
                                </Feed.Event>
                            </Feed>
                        </Card.Content>
                    </Card>

                    <Card color='orange' style={{ margin: 'auto', marginTop: 10 }}>
                        <Card.Content style={{ background: '#FF5722' }}>
                            <Card.Header style={{ color: 'white' }}>Redirection test</Card.Header>
                        </Card.Content>
                        <Card.Content>
                            <Feed>
                                <Feed.Event>
                                    <Feed.Label style={{ color: 'blue', fontSize: 18 }}><strong>Staff1</strong></Feed.Label>
                                    <Feed.Content>
                                        <Feed.Date content='16/12/2018 19:30:50' />
                                        <Feed.Summary>
                                            made a <a>report </a> with page option <a>test123</a> at website <a>tiki.vn</a>.
                                         </Feed.Summary>
                                    </Feed.Content>
                                </Feed.Event>

                                <Feed.Event>
                                    <Feed.Label style={{ color: 'blue', fontSize: 18 }}><strong>Staff1</strong></Feed.Label>
                                    <Feed.Content>
                                        <Feed.Date content='16/12/2018 19:30:50' />
                                        <Feed.Summary>
                                            made a <a>report</a> with page option <a>test123</a> at website <a>tiki.vn</a>.
                                         </Feed.Summary>
                                    </Feed.Content>
                                </Feed.Event><Feed.Event>
                                    <Feed.Label style={{ color: 'blue', fontSize: 18 }}><strong>Staff1</strong></Feed.Label>
                                    <Feed.Content>
                                        <Feed.Date content='16/12/2018 19:30:50' />
                                        <Feed.Summary>
                                            made a <a>report </a> with page option <a>test123</a> at website <a>tiki.vn</a>.
                                         </Feed.Summary>
                                    </Feed.Content>
                                </Feed.Event><Feed.Event>
                                    <Feed.Label style={{ color: 'blue', fontSize: 18 }}><strong>Staff1</strong></Feed.Label>
                                    <Feed.Content>
                                        <Feed.Date content='16/12/2018 19:30:50' />
                                        <Feed.Summary>
                                            made a <a>report </a> with page option <a>test123</a> at website <a>tiki.vn</a>.
                                         </Feed.Summary>
                                    </Feed.Content>
                                </Feed.Event>
                            </Feed>
                        </Card.Content>
                    </Card>

                    <Card color='yellow' style={{ margin: 'auto', marginTop: 10 }}>
                        <Card.Content style={{ background: '#FDD835' }}>
                            <Card.Header style={{ color: 'white' }}>Pages test</Card.Header>
                        </Card.Content>
                        <Card.Content>
                            <Feed>
                                <Feed.Event>
                                    <Feed.Label style={{ color: 'blue', fontSize: 18 }}><strong>Staff1</strong></Feed.Label>
                                    <Feed.Content>
                                        <Feed.Date content='16/12/2018 19:30:50' />
                                        <Feed.Summary>
                                            made a <a>report </a> with page option <a>test123</a> at website <a>tiki.vn</a>.
                                         </Feed.Summary>
                                    </Feed.Content>
                                </Feed.Event>

                                <Feed.Event>
                                    <Feed.Label style={{ color: 'blue', fontSize: 18 }}><strong>Staff1</strong></Feed.Label>
                                    <Feed.Content>
                                        <Feed.Date content='16/12/2018 19:30:50' />
                                        <Feed.Summary>
                                            made a <a>report</a> with page option <a>test123</a> at website <a>tiki.vn</a>.
                                         </Feed.Summary>
                                    </Feed.Content>
                                </Feed.Event><Feed.Event>
                                    <Feed.Label style={{ color: 'blue', fontSize: 18 }}><strong>Staff1</strong></Feed.Label>
                                    <Feed.Content>
                                        <Feed.Date content='16/12/2018 19:30:50' />
                                        <Feed.Summary>
                                            made a <a>report </a> with page option <a>test123</a> at website <a>tiki.vn</a>.
                                         </Feed.Summary>
                                    </Feed.Content>
                                </Feed.Event><Feed.Event>
                                    <Feed.Label style={{ color: 'blue', fontSize: 18 }}><strong>Staff1</strong></Feed.Label>
                                    <Feed.Content>
                                        <Feed.Date content='16/12/2018 19:30:50' />
                                        <Feed.Summary>
                                            made a <a>report </a> with page option <a>test123</a> at website <a>tiki.vn</a>.
                                         </Feed.Summary>
                                    </Feed.Content>
                                </Feed.Event>
                            </Feed>
                        </Card.Content>
                    </Card>

                </Segment.Group>
            </Segment.Group>
        );
    }
}