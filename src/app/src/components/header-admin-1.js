import React, { Component } from 'react';
import { Input,  Menu } from 'semantic-ui-react';
import { Redirect } from "react-router-dom";

export default class HeaderAdmin extends Component {
    state = { logout:false };



    _doLogout = () => {
        this.setState({
            logout: true
        })
    }
    renderRedirect = () => {
        if (this.state.logout) {
            return <Redirect to='/logout' />
        }
    }


    render() {
        return (
            <Menu className="top" style={{ background: '#1E88E5', position: 'absolute', width: '100%', zIndex: '4', margin: '0', height: '50px' }}>
                <Menu.Menu position='right'>
                    <Menu.Item>
                        <Input icon='search' placeholder='Search...' />
                    </Menu.Item>
                    <Menu.Item style={{ color: 'white', fontWeight: 'bold' }}>
                        Hi, Trường
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
