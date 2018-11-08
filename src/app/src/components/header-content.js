import React, { Component } from 'react';
import { Segment, Header, Button, Image, Modal, Icon, Transition, Checkbox } from 'semantic-ui-react';
import { Cookies } from "react-cookie";
const cookies = new Cookies();


export default class HeaderContent extends Component {
    state = { openPageOption: false, pageNum: 0, message: null, listPage: null, currPage: null, pageCheck: [] };

    componentDidMount() {
        if (cookies.get("u_isManager") !== "true") {
            fetch("/api/page/pageOption", {
                method: 'POST',
                headers: {
                    'Accept': 'application/json',
                    'Content-Type': 'application/json'
                },
                body: JSON.stringify({ "userId": cookies.get("u_id"), "userToken": cookies.get("u_token"), "websiteId": cookies.get("u_w_id") })
            }).then(async response => response.json()).then(async (data) => {
                if (data.action === "SUCCESS") {
                    if (data.pageNum === 0) {
                    } else {
                        this.setState({ pageNum: data.pageNum, currPage: data.currentPageOption });
                    }
                    var list = data.listPage.map((item, index) => {
                        return (<div key={index}> <Checkbox toggle onClick={() => this._addPage(item.id)} /> <a href={item.url}>{item.url}</a> </div>);
                    });
                    this.setState({ listPage: list });
                } else if (data.action === "INCORRECT") {
                    this.state.message = data.message;
                }
            });
        }
    }

    _addPage(id) {
        var pages = this.state.pageCheck;
        pages.push(id);
        this.setState({ pageCheck: pages });
        console.log(pages);
    }

    _onOpenPageOptionMode() {
        this.setState({ openPageOption: true })
    }

    render() {
        return (
            <Segment basic>
                <Header as='h1' >{this.props.title} </Header>
                <span style={{ fontStyle: 'italic' }}>{this.props.alt}</span><br />
                {cookies.get("u_isManager") !== "true" ?
                    <Segment>
                        <Button
                            color='grey'
                            icon='settings'
                            content="Page option"
                            label={{ basic: true, color: 'green', pointing: 'left', content: this.state.pageNum }}
                            onClick={() => this._onOpenPageOptionMode()}
                        />
                    </Segment> : ""}
                <Transition duration={200} divided size='huge' verticalAlign='middle' visible={this.state.openPageOption}>
                    <Modal open={this.state.openPageOption}>
                        <Modal.Header>Page Option</Modal.Header>
                        <Modal.Content image>
                            <Image wrapped size='medium' src='https://api2.insites.com/images/backlinks.png' />
                            <Modal.Description>
                                <Header>Pages</Header>
                                {this.state.message !== null && this.state.message !== "" ? <font color="red">{this.state.message}</font> :
                                    this.state.listPage
                                }
                            </Modal.Description>
                        </Modal.Content>
                        <Modal.Actions>
                            <Button primary onClick={() => this.setState({ openPageOption: false })}>
                                Proceed <Icon name='right chevron' />
                            </Button>
                        </Modal.Actions>
                    </Modal>
                </Transition>

            </Segment>
        )
    }
}
