import React, { Component } from 'react';
import { Segment, Header, Button, Modal, Icon, Transition, Checkbox, Table, Menu, Label, Input } from 'semantic-ui-react';
import { Cookies } from "react-cookie";
import _ from 'lodash'

const cookies = new Cookies();


export default class HeaderContent extends Component {
    state = {
        openPageOption: false,
        pageNum: 0,
        message: null,
        listPage: null,
        currPage: null,
        pageCheck: [],
        activeItem: "",
        websiteName: "",
        editPageOption: false,
        addPageOption: false,
        pageOptions: [],
        addPOName: "",
        editPOId: 0,
        editPOName: "",
        isLoading: false
    };

    componentDidMount() {
        fetch("/api/page/pageOption/size", {
            method: 'POST',
            headers: {
                'Accept': 'application/json',
                'Content-Type': 'application/json'
            },
            body: JSON.stringify({
                "userId": cookies.get("u_id"),
                "userToken": cookies.get("u_token"),
                "websiteId": cookies.get("u_w_id"),
                "pageOptionId": cookies.get("u_option"),
            })
        }).then(async response => response.json()).then(async (data) => {
            if (data.action === "SUCCESS") {
                if (data.size === undefined) {
                    this.setState({ pageNum: 0 });
                } else
                    this.setState({ pageNum: data.size });
            } else if (data.action === "INCORRECT") {
            }
        });
    }

    /* add page to page option */
    _addPage(id) {
        var pages = this.state.pageCheck;
        let flag = false;
        for (let i = 0; i < pages.length; i++) {
            if (pages[i] === id) {
                pages.splice(i, 1);
                flag = true;
            }
        }
        if (!flag) {
            pages.push(id);
        }
        fetch("/api/page/pageOption/updatePage", {
            method: 'POST',
            headers: {
                'Accept': 'application/json',
                'Content-Type': 'application/json'
            },
            body: JSON.stringify({
                "userId": cookies.get("u_id"),
                "userToken": cookies.get("u_token"),
                "websiteId": cookies.get("u_w_id"),
                "pageOptionId": this.state.activeItem,
                "listPageId": pages
            })
        }).then(async response => response.json()).then(async (data) => {
            if (data.action === "SUCCESS") {
                console.log("Add thành công");
            }
        });
        flag = false;
        this.setState({ pageCheck: pages });
    }

    /* onChange value name */
    _changeEditPOName(event) {
        this.setState({ editPOName: event.target.value });
    }

    /* onChange value name */
    _changeAddPOName(event) {
        this.setState({ addPOName: event.target.value });
    }

    /* open modal page option send init */
    _onOpenPageOptionMode() {
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

                    if (data.allPageOption.length === 0) {
                        cookies.set('u_option', -1, { path: '/' });
                    }
                    this.setState({ activeItem: parseInt(cookies.get('u_option'), 10) })

                    this.setState({
                        openPageOption: true,
                        websiteName: data.websiteName,
                        listDataPage: data.listPage,
                        pageOptionsList: data.allPageOption,
                        isLoading: true
                    }, () => {
                        this._selectPageOption(parseInt(cookies.get("u_option"), 10))
                    });
                } else if (data.action === "INCORRECT") {
                    this.state.message = data.message;
                }
            });
        }
    }

    /* add page option */
    _addPageOption() {

        if (this.state.addPOName !== null && this.state.addPOName !== "") {
            this.setState({
                isLoading: true
            })
            fetch("/api/page/pageOption/add", {
                method: 'POST',
                headers: {
                    'Accept': 'application/json',
                    'Content-Type': 'application/json'
                },
                body: JSON.stringify({ "userId": cookies.get("u_id"), "userToken": cookies.get("u_token"), "websiteId": cookies.get("u_w_id"), "pageOptionName": this.state.addPOName })
            }).then(async response => response.json()).then(async (data) => {
                //Refresh
                fetch("/api/page/pageOption", {
                    method: 'POST',
                    headers: {
                        'Accept': 'application/json',
                        'Content-Type': 'application/json'
                    },
                    body: JSON.stringify({ "userId": cookies.get("u_id"), "userToken": cookies.get("u_token"), "websiteId": cookies.get("u_w_id") })
                }).then(async response => response.json()).then(async (data) => {
                    if (data.action === "SUCCESS") {
                        var defaultCheckedList = data.currentPageOption;
                        var pages = [];
                        if (defaultCheckedList != null) {
                            for (let i = 0; i < defaultCheckedList.length; i++) {
                                pages.push(defaultCheckedList[i].id);
                            }
                        }
                        this.setState({ activeItem: data.allPageOption[0].id })

                        var pageOptions = data.allPageOption.map((item, index) => {
                            return (<Menu.Item
                                key={index}
                                name={item.name} id={item.id}
                                active={this.state.activeItem === item.id ? true : false}
                                onClick={(event, context) => this.handleClick(event, context)}
                                style={{ fontSize: 18, fontWeight: 'bolder' }}
                            />);
                        })

                        var list = [];
                        this.setState({ listPage: [] });

                        if (defaultCheckedList != null) {
                            list = data.listPage.map((item, index) => {
                                if (!this._checkIfItsInList(defaultCheckedList, item)) {
                                    return (
                                        <Table.Row key={index}>
                                            <Table.Cell collapsing>
                                                <Checkbox toggle onClick={() => this._addPage(item.id)} />
                                            </Table.Cell>
                                            <Table.Cell>
                                                <a href={item.url} style={{ fontSize: 16 }}>{item.url}</a>
                                            </Table.Cell>
                                        </Table.Row>
                                    );
                                } else return (
                                    <Table.Row key={index}>
                                        <Table.Cell collapsing>
                                            <Checkbox toggle onClick={() => this._addPage(item.id)} checked={true} />
                                        </Table.Cell>
                                        <Table.Cell>
                                            <a href={item.url} style={{ fontSize: 16 }}>{item.url}</a>
                                        </Table.Cell>
                                    </Table.Row>
                                );
                            });
                        } else {
                            list = this.state.listDataPage.map((item, index) => {
                                return (
                                    <Table.Row key={index}>
                                        <Table.Cell collapsing>
                                            <Checkbox toggle onClick={() => this._addPage(item.id)} />
                                        </Table.Cell>
                                        <Table.Cell>
                                            <a href={item.url} style={{ fontSize: 16 }}>{item.url}</a>
                                        </Table.Cell>
                                    </Table.Row>
                                );
                            });
                        }
                        this.setState({
                            listPage: list,
                            websiteName: data.websiteName,
                            pageCheck: pages,
                            pageOptions: pageOptions,
                            addPageOption: false,
                            pageOptionsList: data.allPageOption,
                            addPOName: "",
                            isLoading: false,


                        });
                    } else if (data.action === "INCORRECT") {
                        this.state.message = data.message;
                    }
                });
            });
        } else {
            alert('Please input something');
        }
    }

    /* delete page option */
    _deletePageOption() {
        this.setState({
            isLoading: true
        })
        fetch("/api/page/pageOption/delete", {
            method: 'POST',
            headers: {
                'Accept': 'application/json',
                'Content-Type': 'application/json'
            },
            body: JSON.stringify({
                "userId": cookies.get("u_id"),
                "userToken": cookies.get("u_token"),
                "websiteId": cookies.get("u_w_id"),
                "pageOptionId": this.state.editPOId,
            })
        }).then(async response => response.json()).then(async (data) => {
            if (data.action === "SUCCESS") {

                fetch("/api/page/pageOption", {
                    method: 'POST',
                    headers: {
                        'Accept': 'application/json',
                        'Content-Type': 'application/json'
                    },
                    body: JSON.stringify({ "userId": cookies.get("u_id"), "userToken": cookies.get("u_token"), "websiteId": cookies.get("u_w_id") })
                }).then(async response => response.json()).then(async (data) => {
                    if (data.action === "SUCCESS") {
                        var defaultCheckedList = data.currentPageOption;
                        var pages = [];
                        if (defaultCheckedList != null) {
                            for (let i = 0; i < defaultCheckedList.length; i++) {
                                pages.push(defaultCheckedList[i].id);
                            }
                        }
                        if (data.allPageOption.size > 0) {
                            this.setState({ activeItem: data.allPageOption[0].id })
                        }
                        var pageOptions = data.allPageOption.map((item, index) => {
                            return (<Menu.Item
                                key={index}
                                name={item.name} id={item.id}
                                active={this.state.activeItem === item.id ? true : false}
                                onClick={(event, context) => this.handleClick(event, context)}
                                style={{ fontSize: 18, fontWeight: 'bolder' }}
                            />);
                        })
                        var list = [];
                        this.setState({ listPage: [] });

                        if (defaultCheckedList !== null) {
                            list = this.state.listDataPage.map((item, index) => {
                                if (!this._checkIfItsInList(defaultCheckedList, item)) {
                                    return (
                                        <Table.Row key={index}>
                                            <Table.Cell collapsing>
                                                <Checkbox toggle onClick={() => this._addPage(item.id)} />
                                            </Table.Cell>
                                            <Table.Cell>
                                                <a href={item.url} style={{ fontSize: 16 }}>{item.url}</a>
                                            </Table.Cell>
                                        </Table.Row>
                                    );
                                } else return (
                                    <Table.Row key={index}>
                                        <Table.Cell collapsing>
                                            <Checkbox toggle onClick={() => this._addPage(item.id)} checked={true} />
                                        </Table.Cell>
                                        <Table.Cell>
                                            <a href={item.url} style={{ fontSize: 16 }}>{item.url}</a>
                                        </Table.Cell>
                                    </Table.Row>
                                );
                            });
                        } else {
                            list = this.state.listDataPage.map((item, index) => {
                                return (
                                    <Table.Row key={index}>
                                        <Table.Cell collapsing>
                                            <Checkbox toggle onClick={() => this._addPage(item.id)} />
                                        </Table.Cell>
                                        <Table.Cell>
                                            <a href={item.url} style={{ fontSize: 16 }}>{item.url}</a>
                                        </Table.Cell>
                                    </Table.Row>
                                );
                            });
                        }

                        this.setState({
                            pageCheck: pages,
                            isLoading: false,
                            editPageOption: false,
                            listPage: list,
                            listDataPage: data.listPage,
                            pageOptions: pageOptions,
                            pageOptionsList: data.allPageOption
                        });
                    } else if (data.action === "INCORRECT") {
                        this.state.message = data.message;
                    }
                });
            }

        });
    }

    /* edit page option name */
    _editPageOption() {
        this.setState({
            isLoading: true
        })
        fetch("/api/page/pageOption/updateName", {
            method: 'POST',
            headers: {
                'Accept': 'application/json',
                'Content-Type': 'application/json'
            },
            body: JSON.stringify({
                "userId": cookies.get("u_id"),
                "userToken": cookies.get("u_token"),
                "websiteId": cookies.get("u_w_id"),
                "pageOptionId": this.state.editPOId,
                "pageOptionName": this.state.editPOName
            })
        }).then(async response => response.json()).then(async (data) => {
            if (data.action === "SUCCESS") {

                fetch("/api/page/pageOption", {
                    method: 'POST',
                    headers: {
                        'Accept': 'application/json',
                        'Content-Type': 'application/json'
                    },
                    body: JSON.stringify({ "userId": cookies.get("u_id"), "userToken": cookies.get("u_token"), "websiteId": cookies.get("u_w_id") })
                }).then(async response => response.json()).then(async (data) => {
                    if (data.action === "SUCCESS") {
                        var defaultCheckedList = data.currentPageOption;
                        var pages = [];
                        if (defaultCheckedList != null) {
                            for (let i = 0; i < defaultCheckedList.length; i++) {
                                pages.push(defaultCheckedList[i].id);
                            }
                        }
                        this.setState({ activeItem: this.state.editPOId })

                        var pageOptions = data.allPageOption.map((item, index) => {
                            return (<Menu.Item
                                key={index}
                                name={item.name} id={item.id}
                                active={this.state.activeItem === item.id ? true : false}
                                onClick={(event, context) => this.handleClick(event, context)}
                                style={{ fontSize: 18, fontWeight: 'bolder' }}
                            />);
                        })


                        this.setState({
                            pageCheck: pages,
                            pageOptions: pageOptions,
                            isLoading: false,
                            editPageOption: false,
                            pageOptionsList: data.allPageOption
                        });
                    } else if (data.action === "INCORRECT") {
                        this.state.message = data.message;
                    }
                });
            }

        });
    }

    /* select page option */
    _selectPageOption(id) {
        fetch("/api/page/pageOption/select", {
            method: 'POST',
            headers: {
                'Accept': 'application/json',
                'Content-Type': 'application/json'
            },
            body: JSON.stringify({
                "userId": cookies.get("u_id"),
                "userToken": cookies.get("u_token"),
                "websiteId": cookies.get("u_w_id"),
                "pageOptionId": id
            })
        }).then(async response => response.json()).then(async (data) => {
            if (data.action === "SUCCESS") {
                var defaultCheckedList = data.currentPageOption;
                var pages = [];
                if (defaultCheckedList != null) {
                    for (let i = 0; i < defaultCheckedList.length; i++) {
                        pages.push(defaultCheckedList[i].id);
                    }
                }

                var pageOptions = this.state.pageOptionsList.map((item, index) => {
                    return (<Menu.Item
                        key={index}
                        name={item.name} id={item.id}
                        active={this.state.activeItem === item.id ? true : false}
                        onClick={(event, context) => this.handleClick(event, context)}
                        style={{ fontSize: 18, fontWeight: 'bolder' }}
                    />);
                })
                var list = [];
                this.setState({ listPage: [] });

                if (defaultCheckedList !== null) {
                    list = this.state.listDataPage.map((item, index) => {
                        if (!this._checkIfItsInList(defaultCheckedList, item)) {
                            return (
                                <Table.Row key={index}>
                                    <Table.Cell collapsing>
                                        <Checkbox toggle onClick={() => this._addPage(item.id)} />
                                    </Table.Cell>
                                    <Table.Cell>
                                        <a href={item.url} style={{ fontSize: 16 }}>{item.url}</a>
                                    </Table.Cell>
                                </Table.Row>
                            );
                        } else return (
                            <Table.Row key={index}>
                                <Table.Cell collapsing>
                                    <Checkbox toggle onClick={() => this._addPage(item.id)} defaultChecked />
                                </Table.Cell>
                                <Table.Cell>
                                    <a href={item.url} style={{ fontSize: 16 }}>{item.url}</a>
                                </Table.Cell>
                            </Table.Row>
                        );
                    });
                } else {
                    list = this.state.listDataPage.map((item, index) => {
                        return (
                            <Table.Row key={index}>
                                <Table.Cell collapsing>
                                    <Checkbox toggle onClick={() => this._addPage(item.id)} />
                                </Table.Cell>
                                <Table.Cell>
                                    <a href={item.url} style={{ fontSize: 16 }}>{item.url}</a>
                                </Table.Cell>
                            </Table.Row>
                        );
                    });
                }
                this.setState({
                    listPage: list,
                    pageCheck: pages,
                    pageOptions: pageOptions,
                    isLoading: false
                });
            } else if (data.action === "INCORRECT") {
                this.state.message = data.message;
            }
        });
    }

    /* proceed */
    _proceedPageOption() {
        this.setState({ openPageOption: false });
        if (this.state.pageOptionsList.length === 0 || isNaN(this.state.activeItem)) {
            cookies.set('u_option', -1, { path: '/' });
        } else {
            cookies.set("u_option", this.state.activeItem, { path: '/' });
        }
        window.location.reload();
    }
    /* handle dbclick */
    handleClick(e, item) {
        this.setState({ activeItem: item.id, isLoading: true })
        this._selectPageOption(item.id);
        if (!this._delayedClick) {
            this._delayedClick = _.debounce(this.doClick, 100);
        }
        if (this.clickedOnce) {
            this._delayedClick.cancel();
            this.clickedOnce = false;
            this.setState({ editPageOption: true, editPOName: item.name, editPOId: item.id, isLoading: false });
        } else {
            this._delayedClick(e);
            this.clickedOnce = true;
        }
    }

    /* handle single click */
    doClick(e) {
        this.clickedOnce = undefined;
    }

    /* check in list checked pageoption */
    _checkIfItsInList(list, item) {
        if (list != null) {
            for (let i = 0; i < list.length; i++) {
                if (list[i].id === item.id) {
                    return true;
                }
            }
        } else return false;
        return false;
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
                <Transition animation="scale" duration={500} divided size='huge' verticalAlign='middle' visible={this.state.openPageOption}>
                    <Modal open={this.state.openPageOption} dimmer="blurring" onClose={() => this.setState({ openPageOption: false })} >
                        <Modal.Header>Page Option - <Label color='grey'>{this.state.websiteName}</Label></Modal.Header>
                        <Modal.Content style={{ margin: 0, padding: 0, border: 0 }}>
                            <Segment.Group vertical="true" style={{ margin: 0, padding: 0, border: 0 }}>

                                <Segment basic style={{ margin: 0, padding: 0, border: 0 }} loading={this.state.isLoading}>

                                    {/* Edit page option */}
                                    <Transition animation="scale" duration={500} divided size='huge' verticalAlign='middle' visible={this.state.editPageOption}>
                                        <Modal
                                            open={this.state.editPageOption}
                                            dimmer="blurring"
                                            size="tiny"
                                            onClose={() => this.setState({ editPageOption: false })}
                                            style={{ width: 265 }}>
                                            <Modal.Header>Edit name</Modal.Header>
                                            <Modal.Content >
                                                <Modal.Description>
                                                    <Input type='text' value={this.state.editPOName} onChange={(event) => this._changeEditPOName(event)} style={{ width: '100%' }}></Input>

                                                </Modal.Description>

                                            </Modal.Content>
                                            <Modal.Actions>
                                                <Button primary onClick={() => this._editPageOption()}>
                                                    Apply <Icon name='right chevron' />
                                                </Button>
                                                <Button negative onClick={() => this._deletePageOption()}>
                                                    Delete <Icon name='right chevron' />
                                                </Button>
                                            </Modal.Actions>
                                        </Modal>
                                    </Transition>

                                    {/* Add page option */}
                                    <Transition animation="scale" duration={500} divided size='huge' verticalAlign='middle' visible={this.state.addPageOption}>
                                        <Modal
                                            open={this.state.addPageOption}
                                            dimmer="blurring"
                                            size="tiny"
                                            onClose={() => this.setState({ addPageOption: false })}
                                            style={{ width: 225 }}>
                                            <Modal.Header>Add Page Option</Modal.Header>
                                            <Modal.Content >
                                                <Modal.Description>
                                                    <Input type='text' value={this.state.addPOName} onChange={(event) => this._changeAddPOName(event)}></Input>

                                                </Modal.Description>

                                            </Modal.Content>
                                            <Modal.Actions>
                                                <Button primary onClick={() => this._addPageOption()}>
                                                    Apply <Icon name='right chevron' />
                                                </Button>
                                            </Modal.Actions>
                                        </Modal>
                                    </Transition>
                                    <Menu secondary style={{ background: '#F5F5F5', margin: '0vw' }}>
                                        <Menu.Item name='Add' as={Button} onClick={() => this.setState({ addPageOption: true })}>
                                            <Icon name='plus' circular color='green' inverted style={{ margin: 0, fontSize: 18 }} />
                                        </Menu.Item>
                                        <Menu style={{ overflowX: 'scroll', padding: 5, minWidth: '54vw' }}>
                                            {this.state.pageOptions}
                                        </Menu></Menu>
                                </Segment>

                            </Segment.Group>
                            <Modal.Description style={{ maxHeight: '55vh', overflowY: 'scroll' }} >
                                <Segment basic style={{ margin: 0, padding: 0, border: 0 }} loading={this.state.isLoading} >
                                    {this.state.activeItem !== -1 ?
                                        <Table unstackable>
                                            <Table.Body>
                                                {this.state.message !== null && this.state.message !== "" ? <Table.Row><font color="red">{this.state.message}</font></Table.Row> :
                                                    this.state.listPage
                                                }</Table.Body>
                                        </Table> : ""}
                                </Segment>
                            </Modal.Description>
                        </Modal.Content>
                        <Modal.Actions>
                            <Button primary onClick={() => this._proceedPageOption()}>
                                Proceed <Icon name='right chevron' />
                            </Button>
                        </Modal.Actions>
                    </Modal>

                </Transition>

            </Segment >
        )
    }
}
