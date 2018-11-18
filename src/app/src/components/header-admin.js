import React, { Component } from 'react';
import { Input, Dropdown, Menu, Icon } from 'semantic-ui-react';
// import { Redirect } from "react-router-dom";
import { Cookies } from "react-cookie";

const cookies = new Cookies();


class HeaderAdmin extends Component {
    state = { valWebpage: null, listWeb: null, logout: false, account_name: "", marginBody: 160 };



    componentDidMount() {
        if (cookies.get("u_isManager") !== "true") {
            //binding dropdown
            fetch("http://localhost:8080/api/headerStaff", {
                method: 'POST',
                headers: {
                    'Accept': 'application/json',
                    'Content-Type': 'application/json'
                },
                body: JSON.stringify({ "userId": cookies.get("u_id"), "userToken": cookies.get("u_token") })
            }).then(response => response.json()).then((data) => {
                if (data.action === "SUCCESS") {
                    if (data.website.length === 0) {
                        this._doLogout();
                    }
                    var list = data.website.map((item, index) => {
                        return { key: index, value: item.id, text: item.url };

                    });
                    this.setState({ listWeb: list, account_name: data.fullname }, () => {
                        if (cookies.get('u_w_id') !== null && cookies.get('u_w_id') !== undefined) {
                            for (let i = 0; i < list.length; i++) {
                                // eslint-disable-next-line
                                if (list[i].value === parseInt(cookies.get('u_w_id'), 10)) {
                                    this.setState({ valWebpage: list[i].value, });
                                }
                            }
                        } else {
                            cookies.set('u_w_id', list[0].value, { path: '/' });
                            this.setState({ valWebpage: list[0].value, });
                        }
                    });
                } else {
                    this._doLogout();
                }
            });
        }
        else {
            fetch("http://localhost:8080/api/headerManager", {
                method: 'POST',
                headers: {
                    'Accept': 'application/json',
                    'Content-Type': 'application/json'
                },
                body: JSON.stringify({ "userId": cookies.get("u_id"), "userToken": cookies.get("u_token") })
            }).then(response => response.json()).then((data) => {
                if (data.action === "SUCCESS") {
                    this.setState({ account_name: data.fullname });
                } else {
                    this._doLogout();
                }
            });
        }

    }

    _changeWebPage(event, data) {
        this.setState({
            valWebpage: data.value
        });
        this.props.changeWebsite(data.value);
    }



    _doLogout = () => {
        cookies.remove("u_id", { path: '/' });
        cookies.remove("u_token", { path: '/' });
        cookies.remove("u_w_id", { path: '/' });
        cookies.remove("u_isManager", { path: '/' });
        cookies.remove("u_option", { path: '/' });
        this.props.logout(true);

    }

    _hideShowSideBar() {
        if (this.state.marginBody === 10) {
            this.setState({ marginBody: 160 });
        } else {
            this.setState({ marginBody: 10 });
        }
        this.props.hideShowSideBar();
    }



    render() {
        return (
            <Menu className="top" style={{ background: 'rgb(55, 33, 173)', position: 'absolute', width: '100%', zIndex: '4', margin: '0', height: '50px' }}>
                <Menu.Item><Icon className='bars' style={{ color: "white", cursor: "pointer", marginLeft: `${this.state.marginBody}px`, transition: "all 0.6s" }} onClick={() => this._hideShowSideBar()} /></Menu.Item>
                {cookies.get("u_isManager") !== "true" ? <Menu.Item><Dropdown key="1" onChange={(event, data) => this._changeWebPage(event, data)} options={this.state.listWeb} value={this.state.valWebpage} style={{ color: 'white', fontSize: '18px' }} /></Menu.Item> : ""}
                <Menu.Menu position='right'>
                    <Menu.Item>
                        <Input icon='search' placeholder='Search...' />
                    </Menu.Item>
                    <Menu.Item style={{ color: 'white', fontWeight: 'bold' }}>
                        Hi, {this.state.account_name}
                    </Menu.Item>
                    <Menu.Item style={{ color: 'white', fontWeight: 'bold' }}
                        name='logout'
                        onClick={() => this._doLogout()}
                    />
                </Menu.Menu>
            </Menu>
        )
    }

}
export default HeaderAdmin;