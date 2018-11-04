import React, { Component } from 'react';
import { Input, Dropdown, Menu } from 'semantic-ui-react';
import { Redirect } from "react-router-dom";
import { Cookies } from "react-cookie";

const cookies = new Cookies();


class HeaderAdmin extends Component {
    state = {valWebpage:null, listWeb: null, logout: false, account_name:"" };



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
                    return { key: index, value: item.id, text: item.url };

                });
                this.setState({ listWeb: list, valWebpage: list[0].value });
                this.props.changeWebsite(list[0].value);
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

        _changeWebPage(event,data) {
            this.setState({
                valWebpage: data.value
            });
            this.props.changeWebsite(data.value);
        }



        _doLogout = () => {
            cookies.remove("u_id");
            cookies.remove("u_token");
            cookies.remove("u_w_id");
            cookies.remove("u_isManager");
            this.setState({
                logout: true
            });
        }
        renderRedirect = () => {
            if (this.state.logout) {
                return  window.location = './login';

            }
        }


        render() {
            return (
                <Menu className="top" style={{ background: 'rgb(55, 33, 173)', position: 'absolute', width: '100%', zIndex: '4', margin: '0', height: '50px' }}>
                    <Menu.Item><Dropdown key="1" onChange={(event,data)=>this._changeWebPage(event,data)} options={this.state.listWeb} value={this.state.valWebpage} style={{ marginLeft: '160px', color: 'white', fontSize: '18px' }} /></Menu.Item>
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