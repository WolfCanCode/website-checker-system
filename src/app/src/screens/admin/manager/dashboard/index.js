import React, { Component } from 'react';
import { Segment, Feed, Card, Dimmer, Loader, Image } from 'semantic-ui-react'
import { Cookies } from "react-cookie";
import FeedRow from './feed-row';
const cookies = new Cookies();
export default class DashboardManager extends Component {
    state = {
        activeItem: 'home',
        spelling: null,
        grammar: null,
        brokenLink: null,
        brokenPage: null,
        missingFile: null,
        prohibited: null,
        mobileLayout: null,
        speed: null,
        javascript: null,
        favicon: null,
        cookie: null,
        server: null,
        contact: null,
        redirection: null,
        page: null
    };

    componentDidMount() {
        fetch("/api/report/viewAll", {
            method: 'POST',
            headers: {
                'Accept': 'application/json',
                'Content-Type': 'application/json'
            },
            body: JSON.stringify({ "managerId": cookies.get("u_id"), "managerToken": cookies.get("u_token") })
        }).then(async response => response.json()).then(async (res) => {
            var brokenLinkComp = res.data.brokenLinkTest.map((item, index) => {
                var dateTime = new Date(item.reportDate);
                let dateView = dateTime.toLocaleString();
                return <FeedRow key={index} type="brokenLink" userName={item.userName} dateTime={dateView} pageOptionName={item.pageOptionName} pageOptionId={item.pageOptionId} websiteName={item.websiteName} reportDate={item.reportDate} />
            })
            this.setState({ brokenLink: brokenLinkComp });

            var brokenPageComp = res.data.brokenPageTest.map((item, index) => {
                var dateTime = new Date(item.reportDate);
                let dateView = dateTime.toLocaleString();
                return <FeedRow key={index} type="brokenPage" userName={item.userName} dateTime={dateView} pageOptionName={item.pageOptionName} pageOptionId={item.pageOptionId} websiteName={item.websiteName} reportDate={item.reportDate} />
            })
            this.setState({ brokenPage: brokenPageComp });

            var missingComp = res.data.missingFileTest.map((item, index) => {
                var dateTime = new Date(item.reportDate);
                let dateView = dateTime.toLocaleString();
                return <FeedRow key={index} type="missingFile" userName={item.userName} dateTime={dateView} pageOptionName={item.pageOptionName} pageOptionId={item.pageOptionId} websiteName={item.websiteName} reportDate={item.reportDate} />
            })
            this.setState({ missingFile: missingComp });

            var prohibitedComp = res.data.prohibitedTest.map((item, index) => {
                var dateTime = new Date(item.reportDate);
                let dateView = dateTime.toLocaleString();
                return <FeedRow key={index} type="prohibited" userName={item.userName} dateTime={dateView} pageOptionName={item.pageOptionName} pageOptionId={item.pageOptionId} websiteName={item.websiteName} reportDate={item.reportDate} />
            })
            this.setState({ prohibited: prohibitedComp });

            var mobileLayoutComp = res.data.mobileLayoutTest.map((item, index) => {
                var dateTime = new Date(item.reportDate);
                let dateView = dateTime.toLocaleString();
                return <FeedRow key={index} type="mobileLayout" userName={item.userName} dateTime={dateView} pageOptionName={item.pageOptionName} pageOptionId={item.pageOptionId} websiteName={item.websiteName} reportDate={item.reportDate} />
            })
            this.setState({ mobileLayout: mobileLayoutComp });

            var speedComp = res.data.speedTest.map((item, index) => {
                var dateTime = new Date(item.reportDate);
                let dateView = dateTime.toLocaleString();
                return <FeedRow key={index} type="speed" userName={item.userName} dateTime={dateView} pageOptionName={item.pageOptionName} pageOptionId={item.pageOptionId} websiteName={item.websiteName} reportDate={item.reportDate} />
            })
            this.setState({ speed: speedComp });

            var javascriptComp = res.data.javascriptTest.map((item, index) => {
                var dateTime = new Date(item.reportDate);
                let dateView = dateTime.toLocaleString();
                return <FeedRow key={index} type="javascript" userName={item.userName} dateTime={dateView} pageOptionName={item.pageOptionName} pageOptionId={item.pageOptionId} websiteName={item.websiteName} reportDate={item.reportDate} />
            })
            this.setState({ javascript: javascriptComp });

            var faviconComp = res.data.faviconTest.map((item, index) => {
                var dateTime = new Date(item.reportDate);
                let dateView = dateTime.toLocaleString();
                return <FeedRow key={index} type="favicon" userName={item.userName} dateTime={dateView} pageOptionName={item.pageOptionName} pageOptionId={item.pageOptionId} websiteName={item.websiteName} reportDate={item.reportDate} />
            })
            this.setState({ favicon: faviconComp });

            var cookieComp = res.data.cookieTest.map((item, index) => {
                var dateTime = new Date(item.reportDate);
                let dateView = dateTime.toLocaleString();
                return <FeedRow key={index} type="cookie" userName={item.userName} dateTime={dateView} pageOptionName={item.pageOptionName} pageOptionId={item.pageOptionId} websiteName={item.websiteName} reportDate={item.reportDate} />
            })
            this.setState({ cookie: cookieComp });

            var serverComp = res.data.serverBehaviorTest.map((item, index) => {
                var dateTime = new Date(item.reportDate);
                let dateView = dateTime.toLocaleString();
                return <FeedRow key={index} type="server" userName={item.userName} dateTime={dateView} pageOptionName={item.pageOptionName} pageOptionId={item.pageOptionId} websiteName={item.websiteName} reportDate={item.reportDate} />
            })
            this.setState({ server: serverComp });

            var contactComp = res.data.contactTest.map((item, index) => {
                var dateTime = new Date(item.reportDate);
                let dateView = dateTime.toLocaleString();
                return <FeedRow key={index} type="contact" userName={item.userName} dateTime={dateView} pageOptionName={item.pageOptionName} pageOptionId={item.pageOptionId} websiteName={item.websiteName} reportDate={item.reportDate} />
            })
            this.setState({ contact: contactComp });

            var redirectionComp = res.data.redirectionTest.map((item, index) => {
                var dateTime = new Date(item.reportDate);
                let dateView = dateTime.toLocaleString();
                return <FeedRow key={index} type="redirection" userName={item.userName} dateTime={dateView} pageOptionName={item.pageOptionName} pageOptionId={item.pageOptionId} websiteName={item.websiteName} reportDate={item.reportDate} />
            })
            this.setState({ redirection: redirectionComp });

            var pageComp = res.data.pageTest.map((item, index) => {
                var dateTime = new Date(item.reportDate);
                let dateView = dateTime.toLocaleString();
                return <FeedRow key={index} type="page" userName={item.userName} dateTime={dateView} pageOptionName={item.pageOptionName} pageOptionId={item.pageOptionId} websiteName={item.websiteName} reportDate={item.reportDate} />
            })
            this.setState({ page: pageComp });



        });

    }

    render() {
        return (
            <Segment.Group>
                <Segment.Group style={{ border: 0, margin: 'auto', marginTop: 10 }} horizontal>
                    <Card color='red' style={{ margin: 'auto', marginTop: 10 }} >
                        <Card.Content style={{ background: '#ef5350' }}>
                            <Card.Header style={{ color: 'white' }}>Spelling test</Card.Header>
                        </Card.Content>
                        <Card.Content style={{ minHeight: 300, maxHeight: 300, overflow: 'auto', padding: 0, background: '#EEEEEE' }} >
                            {this.state.spelling === null ? <Feed>
                                <Dimmer active inverted>
                                    <Loader inverted />
                                </Dimmer>
                                <Feed.Event>
                                    <Image src='https://react.semantic-ui.com/images/wireframe/short-paragraph.png' />
                                </Feed.Event>
                                <Feed.Event>
                                    <Image src='https://react.semantic-ui.com/images/wireframe/short-paragraph.png' />
                                </Feed.Event>
                                <Feed.Event>
                                    <Image src='https://react.semantic-ui.com/images/wireframe/short-paragraph.png' />
                                </Feed.Event>

                            </Feed> : <Feed>{this.state.spelling.length === 0 ? <Feed.Event style={{ margin: 'auto', marginTop: 10, padding: 5, background: '#FFF', borderRadius: 10, minHeight: 90 }}><h3 style={{ fontSize: 17, margin: 'auto' }}>This test function is haven't test yet</h3></Feed.Event> : this.state.spelling}</Feed>}
                        </Card.Content>
                    </Card>
                    <Card color='orange' style={{ margin: 'auto', marginTop: 10 }}>
                        <Card.Content style={{ background: '#FF7043' }}>
                            <Card.Header style={{ color: 'white' }}>Grammar test</Card.Header>
                        </Card.Content>
                        <Card.Content style={{ minHeight: 300, maxHeight: 300, overflow: 'auto', padding: 0, background: '#EEEEEE' }} >
                            {this.state.grammar === null ? <Feed>
                                <Dimmer active inverted>
                                    <Loader inverted />
                                </Dimmer>
                                <Feed.Event>
                                    <Image src='https://react.semantic-ui.com/images/wireframe/short-paragraph.png' />
                                </Feed.Event>
                                <Feed.Event>
                                    <Image src='https://react.semantic-ui.com/images/wireframe/short-paragraph.png' />
                                </Feed.Event>
                                <Feed.Event>
                                    <Image src='https://react.semantic-ui.com/images/wireframe/short-paragraph.png' />
                                </Feed.Event>

                            </Feed> : <Feed>{this.state.grammar.length === 0 ? <Feed.Event style={{ margin: 'auto', marginTop: 10, padding: 5, background: '#FFF', borderRadius: 10, minHeight: 90 }}><h3 style={{ fontSize: 17, margin: 'auto' }}>This test function is haven't test yet</h3></Feed.Event> : this.state.grammar}</Feed>}
                        </Card.Content>
                    </Card>
                    <Card color='yellow' style={{ margin: 'auto', marginTop: 10 }}>
                        <Card.Content style={{ background: '#FFA726' }}>
                            <Card.Header style={{ color: 'white' }}>Broken links test</Card.Header>
                        </Card.Content>
                        <Card.Content style={{ minHeight: 300, maxHeight: 300, overflow: 'auto', padding: 0, background: '#EEEEEE' }} >
                            {this.state.brokenLink === null ? <Feed>
                                <Dimmer active inverted>
                                    <Loader inverted />
                                </Dimmer>
                                <Feed.Event>
                                    <Image src='https://react.semantic-ui.com/images/wireframe/short-paragraph.png' />
                                </Feed.Event>
                                <Feed.Event>
                                    <Image src='https://react.semantic-ui.com/images/wireframe/short-paragraph.png' />
                                </Feed.Event>
                                <Feed.Event>
                                    <Image src='https://react.semantic-ui.com/images/wireframe/short-paragraph.png' />
                                </Feed.Event>

                            </Feed> : <Feed>{this.state.brokenLink.length === 0 ? <Feed.Event style={{ margin: 'auto', marginTop: 10, padding: 5, background: '#FFF', borderRadius: 10, minHeight: 90 }}><h3 style={{ fontSize: 17, margin: 'auto' }}>This test function is haven't test yet</h3></Feed.Event> : this.state.brokenLink}</Feed>}
                        </Card.Content>
                    </Card>
                    <Card color='olive' style={{ margin: 'auto', marginTop: 10 }}>
                        <Card.Content style={{ background: '#9CCC65' }}>
                            <Card.Header style={{ color: 'white' }}>Broken pages test</Card.Header>
                        </Card.Content>
                        <Card.Content style={{ minHeight: 300, maxHeight: 300, overflow: 'auto', padding: 0, background: '#EEEEEE' }} >
                            {this.state.brokenPage === null ? <Feed>
                                <Dimmer active inverted>
                                    <Loader inverted />
                                </Dimmer>
                                <Feed.Event>
                                    <Image src='https://react.semantic-ui.com/images/wireframe/short-paragraph.png' />
                                </Feed.Event>
                                <Feed.Event>
                                    <Image src='https://react.semantic-ui.com/images/wireframe/short-paragraph.png' />
                                </Feed.Event>
                                <Feed.Event>
                                    <Image src='https://react.semantic-ui.com/images/wireframe/short-paragraph.png' />
                                </Feed.Event>

                            </Feed> : <Feed>{this.state.brokenPage.length === 0 ? <Feed.Event style={{ margin: 'auto', marginTop: 10, padding: 5, background: '#FFF', borderRadius: 10, minHeight: 90 }}><h3 style={{ fontSize: 17, margin: 'auto' }}>This test function is haven't test yet</h3></Feed.Event> : this.state.brokenPage}</Feed>}
                        </Card.Content>
                    </Card>
                    <Card color='green' style={{ margin: 'auto', marginTop: 10 }}>
                        <Card.Content style={{ background: '#66BB6A' }}>
                            <Card.Header style={{ color: 'white' }}>Missing files test</Card.Header>
                        </Card.Content>
                        <Card.Content style={{ minHeight: 300, maxHeight: 300, overflow: 'auto', padding: 0, background: '#EEEEEE' }} >
                            {this.state.missingFile === null ? <Feed>
                                <Dimmer active inverted>
                                    <Loader inverted />
                                </Dimmer>
                                <Feed.Event>
                                    <Image src='https://react.semantic-ui.com/images/wireframe/short-paragraph.png' />
                                </Feed.Event>
                                <Feed.Event>
                                    <Image src='https://react.semantic-ui.com/images/wireframe/short-paragraph.png' />
                                </Feed.Event>
                                <Feed.Event>
                                    <Image src='https://react.semantic-ui.com/images/wireframe/short-paragraph.png' />
                                </Feed.Event>

                            </Feed> : <Feed>{this.state.missingFile.length === 0 ? <Feed.Event style={{ margin: 'auto', marginTop: 10, padding: 5, background: '#FFF', borderRadius: 10, minHeight: 90 }}><h3 style={{ fontSize: 17, margin: 'auto' }}>This test function is haven't test yet</h3></Feed.Event> : this.state.missingFile}</Feed>}
                        </Card.Content>
                    </Card>

                </Segment.Group>
                <Segment.Group style={{ border: 0, margin: 'auto', marginTop: 10 }} horizontal>
                    <Card color='teal' style={{ margin: 'auto', marginTop: 10 }}>
                        <Card.Content style={{ background: '#26C6DA' }}>
                            <Card.Header style={{ color: 'white' }}>Prohibited content test</Card.Header>
                        </Card.Content>
                        <Card.Content style={{ minHeight: 300, maxHeight: 300, overflow: 'auto', padding: 0, background: '#EEEEEE' }} >
                            {this.state.prohibited === null ? <Feed>
                                <Dimmer active inverted>
                                    <Loader inverted />
                                </Dimmer>
                                <Feed.Event>
                                    <Image src='https://react.semantic-ui.com/images/wireframe/short-paragraph.png' />
                                </Feed.Event>
                                <Feed.Event>
                                    <Image src='https://react.semantic-ui.com/images/wireframe/short-paragraph.png' />
                                </Feed.Event>
                                <Feed.Event>
                                    <Image src='https://react.semantic-ui.com/images/wireframe/short-paragraph.png' />
                                </Feed.Event>

                            </Feed> : <Feed>{this.state.prohibited.length === 0 ? <Feed.Event style={{ margin: 'auto', marginTop: 10, padding: 5, background: '#FFF', borderRadius: 10, minHeight: 90 }}><h3 style={{ fontSize: 17, margin: 'auto' }}>This test function is haven't test yet</h3></Feed.Event> : this.state.prohibited}</Feed>}
                        </Card.Content>
                    </Card>
                    <Card color='blue' style={{ margin: 'auto', marginTop: 10 }}>
                        <Card.Content style={{ background: '#29B6F6' }}>
                            <Card.Header style={{ color: 'white' }}>Mobile layout test</Card.Header>
                        </Card.Content>
                        <Card.Content style={{ minHeight: 300, maxHeight: 300, overflow: 'auto', padding: 0, background: '#EEEEEE' }} >
                            {this.state.mobileLayout === null ? <Feed>
                                <Dimmer active inverted>
                                    <Loader inverted />
                                </Dimmer>
                                <Feed.Event>
                                    <Image src='https://react.semantic-ui.com/images/wireframe/short-paragraph.png' />
                                </Feed.Event>
                                <Feed.Event>
                                    <Image src='https://react.semantic-ui.com/images/wireframe/short-paragraph.png' />
                                </Feed.Event>
                                <Feed.Event>
                                    <Image src='https://react.semantic-ui.com/images/wireframe/short-paragraph.png' />
                                </Feed.Event>

                            </Feed> : <Feed>{this.state.mobileLayout.length === 0 ? <Feed.Event style={{ margin: 'auto', marginTop: 10, padding: 5, background: '#FFF', borderRadius: 10, minHeight: 90 }}><h3 style={{ fontSize: 17, margin: 'auto' }}>This test function is haven't test yet</h3></Feed.Event> : this.state.mobileLayout}</Feed>}
                        </Card.Content>
                    </Card>

                    <Card color='violet' style={{ margin: 'auto', marginTop: 10 }}>
                        <Card.Content style={{ background: '#7E57C2' }}>
                            <Card.Header style={{ color: 'white' }}>Speed test</Card.Header>
                        </Card.Content>
                        <Card.Content style={{ minHeight: 300, maxHeight: 300, overflow: 'auto', padding: 0, background: '#EEEEEE' }} >
                            {this.state.speed === null ? <Feed>
                                <Dimmer active inverted>
                                    <Loader inverted />
                                </Dimmer>
                                <Feed.Event>
                                    <Image src='https://react.semantic-ui.com/images/wireframe/short-paragraph.png' />
                                </Feed.Event>
                                <Feed.Event>
                                    <Image src='https://react.semantic-ui.com/images/wireframe/short-paragraph.png' />
                                </Feed.Event>
                                <Feed.Event>
                                    <Image src='https://react.semantic-ui.com/images/wireframe/short-paragraph.png' />
                                </Feed.Event>

                            </Feed> : <Feed>{this.state.speed.length === 0 ? <Feed.Event style={{ margin: 'auto', marginTop: 10, padding: 5, background: '#FFF', borderRadius: 10, minHeight: 90 }}><h3 style={{ fontSize: 17, margin: 'auto' }}>This test function is haven't test yet</h3></Feed.Event> : this.state.speed}</Feed>}
                        </Card.Content>
                    </Card>

                    <Card color='purple' style={{ margin: 'auto', marginTop: 10 }}>
                        <Card.Content style={{ background: '#AB47BC' }}>
                            <Card.Header style={{ color: 'white' }}>Javascript test</Card.Header>
                        </Card.Content>
                        <Card.Content style={{ minHeight: 300, maxHeight: 300, overflow: 'auto', padding: 0, background: '#EEEEEE' }} >
                            {this.state.javascript === null ? <Feed>
                                <Dimmer active inverted>
                                    <Loader inverted />
                                </Dimmer>
                                <Feed.Event>
                                    <Image src='https://react.semantic-ui.com/images/wireframe/short-paragraph.png' />
                                </Feed.Event>
                                <Feed.Event>
                                    <Image src='https://react.semantic-ui.com/images/wireframe/short-paragraph.png' />
                                </Feed.Event>
                                <Feed.Event>
                                    <Image src='https://react.semantic-ui.com/images/wireframe/short-paragraph.png' />
                                </Feed.Event>

                            </Feed> : <Feed>{this.state.javascript.length === 0 ? <Feed.Event style={{ margin: 'auto', marginTop: 10, padding: 5, background: '#FFF', borderRadius: 10, minHeight: 90 }}><h3 style={{ fontSize: 17, margin: 'auto' }}>This test function is haven't test yet</h3></Feed.Event> : this.state.javascript}</Feed>}
                        </Card.Content>
                    </Card>

                    <Card color='pink' style={{ margin: 'auto', marginTop: 10 }}>
                        <Card.Content style={{ background: '#EC407A' }}>
                            <Card.Header style={{ color: 'white' }}>Favicon test</Card.Header>
                        </Card.Content>
                        <Card.Content style={{ minHeight: 300, maxHeight: 300, overflow: 'auto', padding: 0, background: '#EEEEEE' }} >
                            {this.state.favicon === null ? <Feed>
                                <Dimmer active inverted>
                                    <Loader inverted />
                                </Dimmer>
                                <Feed.Event>
                                    <Image src='https://react.semantic-ui.com/images/wireframe/short-paragraph.png' />
                                </Feed.Event>
                                <Feed.Event>
                                    <Image src='https://react.semantic-ui.com/images/wireframe/short-paragraph.png' />
                                </Feed.Event>
                                <Feed.Event>
                                    <Image src='https://react.semantic-ui.com/images/wireframe/short-paragraph.png' />
                                </Feed.Event>

                            </Feed> : <Feed>{this.state.favicon.length === 0 ? <Feed.Event style={{ margin: 'auto', marginTop: 10, padding: 5, background: '#FFF', borderRadius: 10, minHeight: 90 }}><h3 style={{ fontSize: 17, margin: 'auto' }}>This test function is haven't test yet</h3></Feed.Event> : this.state.favicon}</Feed>}
                        </Card.Content>
                    </Card>


                </Segment.Group>
                <Segment.Group style={{ border: 0, margin: 'auto', marginTop: 10 }} horizontal>
                    <Card color='brown' style={{ margin: 'auto', marginTop: 10 }}>
                        <Card.Content style={{ background: '#8D6E63' }}>
                            <Card.Header style={{ color: 'white' }}>Law cookie test</Card.Header>
                        </Card.Content>
                        <Card.Content style={{ minHeight: 300, maxHeight: 300, overflow: 'auto', padding: 0, background: '#EEEEEE' }} >
                            {this.state.cookie === null ? <Feed>
                                <Dimmer active inverted>
                                    <Loader inverted />
                                </Dimmer>
                                <Feed.Event>
                                    <Image src='https://react.semantic-ui.com/images/wireframe/short-paragraph.png' />
                                </Feed.Event>
                                <Feed.Event>
                                    <Image src='https://react.semantic-ui.com/images/wireframe/short-paragraph.png' />
                                </Feed.Event>
                                <Feed.Event>
                                    <Image src='https://react.semantic-ui.com/images/wireframe/short-paragraph.png' />
                                </Feed.Event>

                            </Feed> : <Feed>{this.state.cookie.length === 0 ? <Feed.Event style={{ margin: 'auto', marginTop: 10, padding: 5, background: '#FFF', borderRadius: 10, minHeight: 90 }}><h3 style={{ fontSize: 17, margin: 'auto' }}>This test function is haven't test yet</h3></Feed.Event> : this.state.cookie}</Feed>}
                        </Card.Content>
                    </Card>

                    <Card color='grey' style={{ margin: 'auto', marginTop: 10 }}>
                        <Card.Content style={{ background: '#9E9E9E' }}>
                            <Card.Header style={{ color: 'white' }}>Server behavior test</Card.Header>
                        </Card.Content>
                        <Card.Content style={{ minHeight: 300, maxHeight: 300, overflow: 'auto', padding: 0, background: '#EEEEEE' }} >
                            {this.state.server === null ? <Feed>
                                <Dimmer active inverted>
                                    <Loader inverted />
                                </Dimmer>
                                <Feed.Event>
                                    <Image src='https://react.semantic-ui.com/images/wireframe/short-paragraph.png' />
                                </Feed.Event>
                                <Feed.Event>
                                    <Image src='https://react.semantic-ui.com/images/wireframe/short-paragraph.png' />
                                </Feed.Event>
                                <Feed.Event>
                                    <Image src='https://react.semantic-ui.com/images/wireframe/short-paragraph.png' />
                                </Feed.Event>

                            </Feed> : <Feed>{this.state.server.length === 0 ? <Feed.Event style={{ margin: 'auto', marginTop: 10, padding: 5, background: '#FFF', borderRadius: 10, minHeight: 90 }}><h3 style={{ fontSize: 17, margin: 'auto' }}>This test function is haven't test yet</h3></Feed.Event> : this.state.server}</Feed>}
                        </Card.Content>
                    </Card>

                    <Card color='red' style={{ margin: 'auto', marginTop: 10 }}>
                        <Card.Content style={{ background: '#f44336' }}>
                            <Card.Header style={{ color: 'white' }}>Contact detail test</Card.Header>
                        </Card.Content>
                        <Card.Content style={{ minHeight: 300, maxHeight: 300, overflow: 'auto', padding: 0, background: '#EEEEEE' }} >
                            {this.state.contact === null ? <Feed>
                                <Dimmer active inverted>
                                    <Loader inverted />
                                </Dimmer>
                                <Feed.Event>
                                    <Image src='https://react.semantic-ui.com/images/wireframe/short-paragraph.png' />
                                </Feed.Event>
                                <Feed.Event>
                                    <Image src='https://react.semantic-ui.com/images/wireframe/short-paragraph.png' />
                                </Feed.Event>
                                <Feed.Event>
                                    <Image src='https://react.semantic-ui.com/images/wireframe/short-paragraph.png' />
                                </Feed.Event>

                            </Feed> : <Feed>{this.state.contact.length === 0 ? <Feed.Event style={{ margin: 'auto', marginTop: 10, padding: 5, background: '#FFF', borderRadius: 10, minHeight: 90 }}><h3 style={{ fontSize: 17, margin: 'auto' }}>This test function is haven't test yet</h3></Feed.Event> : this.state.contact}</Feed>}
                        </Card.Content>
                    </Card>

                    <Card color='orange' style={{ margin: 'auto', marginTop: 10 }}>
                        <Card.Content style={{ background: '#FF5722' }}>
                            <Card.Header style={{ color: 'white' }}>Redirection test</Card.Header>
                        </Card.Content>
                        <Card.Content style={{ minHeight: 300, maxHeight: 300, overflow: 'auto', padding: 0, background: '#EEEEEE' }} >
                            {this.state.redirection === null ? <Feed>
                                <Dimmer active inverted>
                                    <Loader inverted />
                                </Dimmer>
                                <Feed.Event>
                                    <Image src='https://react.semantic-ui.com/images/wireframe/short-paragraph.png' />
                                </Feed.Event>
                                <Feed.Event>
                                    <Image src='https://react.semantic-ui.com/images/wireframe/short-paragraph.png' />
                                </Feed.Event>
                                <Feed.Event>
                                    <Image src='https://react.semantic-ui.com/images/wireframe/short-paragraph.png' />
                                </Feed.Event>

                            </Feed> : <Feed>{this.state.redirection.length === 0 ? <Feed.Event style={{ margin: 'auto', marginTop: 10, padding: 5, background: '#FFF', borderRadius: 10, minHeight: 90 }}><h3 style={{ fontSize: 17, margin: 'auto' }}>This test function is haven't test yet</h3></Feed.Event> : this.state.redirection}</Feed>}
                        </Card.Content>
                    </Card>

                    <Card color='yellow' style={{ margin: 'auto', marginTop: 10 }}>
                        <Card.Content style={{ background: '#FDD835' }}>
                            <Card.Header style={{ color: 'white' }}>Pages test</Card.Header>
                        </Card.Content>
                        <Card.Content style={{ minHeight: 300, maxHeight: 300, overflow: 'auto', padding: 0, background: '#EEEEEE' }} >
                            {this.state.page === null ? <Feed>
                                <Dimmer active inverted>
                                    <Loader inverted />
                                </Dimmer>
                                <Feed.Event>
                                    <Image src='https://react.semantic-ui.com/images/wireframe/short-paragraph.png' />
                                </Feed.Event>
                                <Feed.Event>
                                    <Image src='https://react.semantic-ui.com/images/wireframe/short-paragraph.png' />
                                </Feed.Event>
                                <Feed.Event>
                                    <Image src='https://react.semantic-ui.com/images/wireframe/short-paragraph.png' />
                                </Feed.Event>

                            </Feed> : <Feed>{this.state.page.length === 0 ? <Feed.Event style={{ margin: 'auto', marginTop: 10, padding: 5, background: '#FFF', borderRadius: 10, minHeight: 90 }}><h3 style={{ fontSize: 17, margin: 'auto' }}>This test function is haven't test yet</h3></Feed.Event> : this.state.page}</Feed>}
                        </Card.Content>
                    </Card>

                </Segment.Group>
            </Segment.Group>
        );
    }
}