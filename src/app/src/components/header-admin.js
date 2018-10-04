import React, { Component } from 'react';
import { Input, Dropdown, Menu } from 'semantic-ui-react';
import { Redirect } from "react-router-dom";

export default class HeaderAdmin extends Component {
    state = { txtWebpage: "www.tests.vn", logout:false };
    data = [{ key: 'block', value: 'block', text: 'www.block.vn' }, { key: 'bmag', value: 'bmag', text: 'www.bmag.vn' }];

    _changeWebPage(event) {
        this.setState({
            txtWebpage: event.target.text
        });
    }



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
                <Menu.Item><Dropdown options={this.data} text={this.state.txtWebpage} onChange={event => this._changeWebPage(event)} style={{ marginLeft: '160px', color: 'white', fontSize: '18px' }} /></Menu.Item>
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
