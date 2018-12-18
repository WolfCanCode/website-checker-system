import React, { Component } from 'react';
import { Feed, Segment, Modal, Transition, Button, Table } from 'semantic-ui-react'
import BrokenLinkRow from '../../brokenLinks/row-table';
import BrokenPageRow from '../../brokenPages/row-table';
import MissingFileRow from '../../missingFiles/row-table';
import ProhibitedRow from '../../prohibitedcontent/row-table';
import MobileLayoutRow from '../../mobileLayout/row-table';
import SpeedRow from '../../speedTest/row-table';
import JavascriptRow from '../../javascripterror/row-table';
import FaviconRow from '../../favicons/row-table';
import CookieRow from '../../cookielaw/row-table';
import ServerRow from '../../serverBehavior/row-table';
import ContactRow from '../../contact/row-table';
import RedirectionRow from '../../direction/row-table';
import PageRow from '../../pages/row-table';

import { Cookies } from "react-cookie";
const cookies = new Cookies();




export default class FeedRow extends Component {
    state = { showReport: false, isDone: false, compList: [] }
    _clickShowPageOption() {

    }

    _clickShowReport() {
        this.setState({ showReport: true });
        var list = [];
        fetch("/api/report/detail", {
            method: 'POST',
            headers: {
                'Accept': 'application/json',
                'Content-Type': 'application/json'
            },
            body: JSON.stringify({ "managerId": cookies.get("u_id"), "managerToken": cookies.get("u_token"), reportDate: this.props.reportDate, reportType: this.props.type, pageOptionId: this.props.pageOptionId })
        }).then(response => response.json()).then((res) => {
            switch (this.props.type) {
                case "brokenLink":
                    list = res.data.map((item, index) => {
                        return (<BrokenLinkRow key={index} urlPage={item.urlPage} urlLink={item.urlLink} type={item.type} httpCode={item.httpCode} httpMessage={item.httpMessage} />);
                    })
                    this.setState({
                        compList: list
                    });
                    break;
                case "brokenPage":
                    list = res.data.map((item, index) => {
                        return (<BrokenPageRow key={index} urlPage={item.urlPage} stt={item.stt} httpCode={item.httpCode} httpMessage={item.httpMessage} />);
                    })
                    this.setState({
                        compList: list
                    });
                    break;
                case "missingFile":
                    var listCom = [];
                    let flag = false;
                    var listMissingFileCount = [];
                    var flagMissingFile = false;
                    list = res.data.map((item, index) => {
                        for (let i = 0; i < listCom.length; i++) {
                            if (item.pages === listCom[i]) {
                                flag = true;
                            }
                        }
                        if (flag === false) {
                            listCom.push(item.pages);
                        } else {
                            flag = false;
                        }

                        for (let i = 0; i < listMissingFileCount.length; i++) {
                            if (item.fileMissing === listMissingFileCount[i]) {
                                flagMissingFile = true;
                            }
                        }
                        if (flagMissingFile === false) {
                            listMissingFileCount.push(item.fileMissing);
                        }
                        else {
                            flagMissingFile = false;
                        }

                        return (<MissingFileRow key={index} fileMissing={item.fileMissing} description={item.description} pages={item.pages} />);
                    })
                    this.setState({
                        compList: list
                    });
                    break;
                case "prohibited":
                    list = res.data.map((item, index) => {
                        return (<ProhibitedRow key={index} urlPage={item.urlPage} word={item.word} fragment={item.fragment} type={item.type} />);
                    })
                    this.setState({
                        compList: list
                    });
                    break;
                case "mobileLayout":
                    list = res.data.map((item, index) => {
                        return (<MobileLayoutRow key={index} url={item.url} title={item.title} screenShot={item.screenShot} issue={item.issue} />);
                    })
                    this.setState({
                        compList: list
                    });
                    break;
                case "speed":
                    list = res.data.map((item, index) => {
                        console.log(item);
                        return (<SpeedRow key={index} url={item.url} interactiveTime={item.interactiveTime} pageLoadTime={item.pageLoadTime} size={item.size} />);
                    });
                    console.log(res.data);
                    console.log(list);
                    this.setState({
                        compList: list
                    });
                    break;
                case "javascript":
                    list = res.data.map((item, index) => {
                        return (<JavascriptRow key={index} page={item.pages} type={item.type} messages={item.messages} />);
                    })
                    this.setState({
                        compList: list
                    });
                    break;
                case "favicon":
                    list = res.data.map((item, index) => {

                        return (<FaviconRow key={index} image={item.faviconUrl} url={item.faviconUrl} sizeFav={item.sizeFavicon} typeFavicon={item.typeFavicon} w webAddress={item.url} />);
                    })
                    this.setState({
                        compList: list
                    });
                    break;
                case "cookie":
                    list = res.data.map((item, index) => {
                        return (<CookieRow key={index} cookieName={item.cookieName} category={item.category} party={item.party} description={item.description} />);
                    })
                    this.setState({
                        compList: list
                    });
                    break;
                case "server":
                    list = res.data.map((item, index) => {
                        return (<ServerRow key={index} url={item.url} isPageSSL={item.pageSSL} isRedirectHTTPS={item.redirectHTTPS} isRedirectWWW={item.redirectWWW} />);
                    })
                    this.setState({
                        compList: list
                    });
                    break;
                case "page":
                    list = res.data.map((item, index) => {
                        return (<PageRow key={index} url={item.url} titleWeb={item.titleWeb} canonicalUrl={item.canonicalUrl} httpCode={item.httpCode} />);
                    })
                    this.setState({
                        compList: list
                    });
                    break;
                case "contact":
                    var listContact = [];  // 2620184173 , [https://thanhnien.vn/van-hoa/,https://thanhnien.vn/the-gioi/quan-su/]

                    var checkExist = false;
                    var pos = 0;

                    for (let i = 0; i < res.data.length; i++) {

                        for (let j = 0; j < listContact.length; j++) {
                            pos = j;
                            if (listContact[j].phoneMail === res.data[i].phoneMail) {
                                checkExist = true;
                                break;
                            }
                        }

                        if (checkExist) {
                            checkExist = false;

                            listContact[pos].url.push(res.data[i].url);
                        }

                        else {
                            listContact.push({ phoneMail: res.data[i].phoneMail, url: [res.data[i].url], type: res.data[i].type });
                        }

                    }
                    list = listContact.map((item, index) => {
                        return (<ContactRow key={index} phoneMail={item.phoneMail} url={item.url} type={item.type} />);
                    });
                    this.setState({
                        compList: list
                    });
                    break;
                case "redirection":
                    list = res.data.redirectiontestReport.map((item, index) => {
                        return (<RedirectionRow key={index} webAddress={item.url} redirectUrl={item.driectToUrl} type={item.type} httpcode={item.httpCode} />);
                    })
                    this.setState({
                        compList: list
                    });
                    break;
                default: break;
            }
            this.setState({ isDone: true });
        });
    }

    render() {
        return (
            <Feed.Event style={{ margin: 'auto', marginTop: 10, padding: 5, background: '#FFF', borderRadius: 10, minHeight: 90 }} >
                <Feed.Label style={{ color: 'blue', fontSize: 18, margin: 'auto' }}><strong>{this.props.userName}</strong></Feed.Label>
                <Feed.Content>
                    <Feed.Date style={{ color: 'rgba(0,0,0,.7)', background: '#EEEEEE', borderRadius: 10, textAlign: 'center', fontWeight: 'bold' }} content={this.props.dateTime} />
                    <Feed.Summary>
                        made a <a onClick={() => this._clickShowReport()}>report </a> <br />with page option <a onClick={() => this._clickShowPageOption()}>{this.props.pageOptionName}</a> <br />at website <br /><a>{this.props.websiteName}</a>.
                                         </Feed.Summary>
                </Feed.Content>

                <Transition duration={600} divided size='huge' verticalAlign='middle' visible={this.state.showReport}>
                    <Modal open={this.state.showReport} size="large" >
                        <Modal.Header>Report {this.props.dateTime}</Modal.Header>
                        <Modal.Content style={{ padding: 0 }}>
                            <Segment loading={!this.state.isDone} style={{ minHeight: 100, padding: 0, margin: 0 }}>
                                <Table singleLine unstackable>
                                    {this.props.type === "brokenLink" ? <Table.Header>
                                        <Table.Row>
                                            <Table.HeaderCell>Page</Table.HeaderCell>
                                            <Table.HeaderCell>Broken link</Table.HeaderCell>
                                            <Table.HeaderCell>Issue</Table.HeaderCell>

                                        </Table.Row>
                                    </Table.Header> : ""}

                                    {this.props.type === "brokenPage" ? <Table.Header>
                                        <Table.Row>
                                            <Table.HeaderCell>Page</Table.HeaderCell>
                                            <Table.HeaderCell>Status</Table.HeaderCell>
                                            <Table.HeaderCell>Issue</Table.HeaderCell>

                                        </Table.Row>
                                    </Table.Header> : ""}

                                    {this.props.type === "missingFile" ? <Table.Header>
                                        <Table.Row>
                                            <Table.HeaderCell>Files</Table.HeaderCell>
                                            <Table.HeaderCell>Description</Table.HeaderCell>
                                            <Table.HeaderCell>Pages</Table.HeaderCell>
                                            <Table.HeaderCell>Action</Table.HeaderCell>

                                        </Table.Row>
                                    </Table.Header> : ""}

                                    {this.props.type === "prohibited" ? <Table.Header>
                                        <Table.Row>
                                            <Table.HeaderCell>Word</Table.HeaderCell>
                                            <Table.HeaderCell>Type</Table.HeaderCell>
                                            <Table.HeaderCell>Fragment</Table.HeaderCell>
                                            <Table.HeaderCell>Page</Table.HeaderCell>

                                        </Table.Row>
                                    </Table.Header> : ""}

                                    {this.props.type === "mobileLayout" ? <Table.Header>
                                        <Table.Row>
                                            <Table.HeaderCell>ScreenShot</Table.HeaderCell>
                                            <Table.HeaderCell style={{ textAlign: 'left' }}>Page</Table.HeaderCell>
                                            <Table.HeaderCell>Issue</Table.HeaderCell>

                                        </Table.Row>
                                    </Table.Header> : ""}

                                    {this.props.type === "speed" ? <Table.Header>
                                        <Table.Row>
                                            <Table.HeaderCell>Page</Table.HeaderCell>
                                            <Table.HeaderCell>Interactive time</Table.HeaderCell>
                                            <Table.HeaderCell>Load time</Table.HeaderCell>
                                            <Table.HeaderCell>Size</Table.HeaderCell>

                                        </Table.Row>
                                    </Table.Header> : ""}

                                    {this.props.type === "javascript" ? <Table.Header>
                                        <Table.Row>
                                            <Table.HeaderCell>Error Message</Table.HeaderCell>
                                            <Table.HeaderCell>Type</Table.HeaderCell>
                                            <Table.HeaderCell>Pages</Table.HeaderCell>

                                        </Table.Row>
                                    </Table.Header> : ""}
                                    {this.props.type === "favicon" ? <Table.Header>
                                        <Table.Row>
                                            <Table.HeaderCell>Favicon</Table.HeaderCell>
                                            <Table.HeaderCell>URL</Table.HeaderCell>
                                            <Table.HeaderCell>Width</Table.HeaderCell>
                                            <Table.HeaderCell>Type</Table.HeaderCell>
                                            <Table.HeaderCell>Pages</Table.HeaderCell>

                                        </Table.Row>
                                    </Table.Header> : ""}

                                    {this.props.type === "cookie" ? <Table.Header>
                                        <Table.Row>
                                            <Table.HeaderCell>Cookie Name</Table.HeaderCell>
                                            <Table.HeaderCell>Category</Table.HeaderCell>
                                            <Table.HeaderCell>Party</Table.HeaderCell>
                                            <Table.HeaderCell>Description</Table.HeaderCell>

                                        </Table.Row>
                                    </Table.Header> : ""}

                                    {this.props.type === "server" ? <Table.Header>
                                        <Table.Row>
                                            <Table.HeaderCell>Page</Table.HeaderCell>
                                            <Table.HeaderCell>SSL</Table.HeaderCell>
                                            <Table.HeaderCell>HTTPS redirection</Table.HeaderCell>
                                            <Table.HeaderCell>WWW redirection</Table.HeaderCell>

                                        </Table.Row>
                                    </Table.Header> : ""}

                                    {this.props.type === "contact" ? <Table.Header>
                                        <Table.Row>
                                            <Table.HeaderCell>Contact</Table.HeaderCell>
                                            <Table.HeaderCell>Page affected</Table.HeaderCell>
                                            <Table.HeaderCell>Action</Table.HeaderCell>

                                        </Table.Row>
                                    </Table.Header> : ""}

                                    {this.props.type === "redirection" ? <Table.Header>
                                        <Table.Row>
                                            <Table.HeaderCell>Web Address</Table.HeaderCell>
                                            <Table.HeaderCell>Directs to</Table.HeaderCell>
                                            <Table.HeaderCell>Type</Table.HeaderCell>
                                            <Table.HeaderCell>Code</Table.HeaderCell>

                                        </Table.Row>
                                    </Table.Header> : ""}

                                    {this.props.type === "page" ? <Table.Header>
                                        <Table.Row>
                                            <Table.HeaderCell>Title</Table.HeaderCell>
                                            <Table.HeaderCell>Web Address</Table.HeaderCell>
                                            <Table.HeaderCell>Canonical URL</Table.HeaderCell>
                                            <Table.HeaderCell>HTTP</Table.HeaderCell>
                                            {/* <Table.HeaderCell>Last checked</Table.HeaderCell> */}
                                        </Table.Row>
                                    </Table.Header> : ""}


                                    <Table.Body>
                                        {this.state.compList}
                                    </Table.Body>
                                </Table>
                            </Segment>
                        </Modal.Content>
                        <Modal.Actions>
                            <Button onClick={() => this.setState({ showReport: false })}> Cancel</Button>
                        </Modal.Actions>
                    </Modal>
                </Transition>
            </Feed.Event >

        );
    }
}
