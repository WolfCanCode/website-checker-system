import React, { Component } from 'react';
import { Input, Dropdown, Menu } from 'semantic-ui-react';
import { Redirect } from "react-router-dom";
import { Cookies } from "react-cookie";

const cookies = new Cookies();


class HeaderAdmin extends Component {
    state = { txtWebpage: "", listWeb: [""], logout: false, account_name:"" };



    componentDidMount() {
        //binding dropdown
        fetch("/api/website/all", {
            method: 'POST',
            headers: {
                'Accept': 'application/json',
                'Content-Type': 'application/json'
            },
            body: JSON.stringify({ "id": cookies.get("u_id") })
        }).then(response => response.json()).then((data) => {
            if (data.action === "SUCCESS") {
                var list = data.website.map((item, index) => {
                    return { key: index, value: item.url, text: item.name };

                });
                this.setState({ listWeb: list, txtWebpage: list[0].text });
            } else {
                this.setState({
                    redirect: <Redirect to='/logout' />
                });
            }
        });

        fetch("/api/user/name", {
            method: 'POST',
            headers: {
                'Accept': 'application/json',
                'Content-Type': 'application/json'
            },
            body: JSON.stringify({ "id": cookies.get("u_id") })
        }).then(response => response.json()).then((data) => {
            if (data.action === "SUCCESS") {
                this.setState({account_name: data.name});
            } else {
                this.setState({
                    redirect: <Redirect to='/logout' />
                });
            }
        });
    }

        _changeWebPage(event) {
            this.setState({
                txtWebpage: event.target.text
            });
        }



        _doLogout = () => {
            cookies.remove("u_token");
            this.setState({
                logout: true
            });
        }
        renderRedirect = () => {
            if (this.state.logout) {
                return <Redirect to='/logout' />
            }
        }


        render() {
            return (
                <Menu className="top" style={{ background: 'rgb(55, 33, 173)', position: 'absolute', width: '100%', zIndex: '4', margin: '0', height: '50px' }}>
                    <Menu.Item><Dropdown options={this.state.listWeb} text={this.state.txtWebpage} defaultValue={this.state.listWeb.length === 0 ? "" : this.state.listWeb[0].value} onChange={event => this._changeWebPage(event)} style={{ marginLeft: '160px', color: 'white', fontSize: '18px' }} /></Menu.Item>
                    <Menu.Menu position='right'>
                        <Menu.Item>
                            <Input icon='search' placeholder='Search...' />
                        </Menu.Item>
                        <Menu.Item style={{ color: 'white', fontWeight: 'bold' }}>
                            Hi, {this.state.account_name}
                    </Menu.Item>
                        {this.renderRedirect()}
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