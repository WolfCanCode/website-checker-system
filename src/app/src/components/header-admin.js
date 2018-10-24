import React, { Component } from 'react';
import { Input, Dropdown, Menu } from 'semantic-ui-react';
import { Redirect } from "react-router-dom";

export default class HeaderAdmin extends Component {
    state = { txtWebpage: "", listWeb:[], logout:false };

    componentDidMount(){
        fetch("/api/website/all").then(response => response.json()).then((data)=>{
            var list = data.map((item,index)=>{
                return { key: index, value: item.url, text: item.name };

            });
            this.setState({listWeb:list, txtWebpage: list[0].text});
        });
    }

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
            <Menu className="top" style={{ background: 'rgb(55, 33, 173)', position: 'absolute', width: '100%', zIndex: '4', margin: '0', height: '50px' }}>
                <Menu.Item><Dropdown options={this.state.listWeb} text={this.state.txtWebpage} defaultValue={this.state.listWeb.length===0 ? "" : this.state.listWeb[0].value}  onChange={event => this._changeWebPage(event)} style={{ marginLeft: '160px', color: 'white', fontSize: '18px' }} /></Menu.Item>
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
