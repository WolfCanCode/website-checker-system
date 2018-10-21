import React, { Component } from 'react';
import {
    Link
} from "react-router-dom";
import { Menu, Sidebar, Image } from 'semantic-ui-react';
import menu from '../config/menu';
import logo from '../assets/icon-wsc.png';
import sidebarBg from '../assets/sidebar.jpg';

export default class SideMenu extends Component {
    constructor(props) {
        super(props);
        this.state = { menuActive: "d", listMenu: [] };
    }

    componentDidMount() {
        this._mapItem();
    }

    async _mapItem() {
        this.listMenuItem = menu.map((item, index) => {
            if (item.items === null) {
                return (
                    <Menu.Item style={{fontSize:15,fontWeight:'bold'}} key={index} as={Link} to={item.to} active={this.state.menuActive === item.key ? true : false} onClick={() => this._doActiveChange(item)}>
                        {item.name}
                    </Menu.Item>);
            } else {
                var childItem = item.items.map((itemC, indexC) => {
                    return (
                        <Menu.Item style={{fontSize:13}} key={indexC} as={Link} to={itemC.to} active={this.state.menuActive === itemC.key ? true : false} onClick={() => this._doActiveChange(itemC)}>
                            {itemC.name}
                        </Menu.Item>
                    );
                });
                return (
                    <Menu.Item key={index}>
                        <Menu.Header style={{fontSize:15}}>{item.name}</Menu.Header>
                        <Menu.Menu>
                            {childItem}
                        </Menu.Menu>
                    </Menu.Item>);
            }
        });

        await this.setState({ listMenu: this.listMenuItem });
    }


    async _doActiveChange(item) {
        await this.setState({ menuActive: item.key });
        this._mapItem();
        this.props.updateHeader(item.name, item.alt,item.image);
    }

    render() {
        return (
            <Sidebar as={Menu} animation='overlay' inverted vertical visible width='thin' style={{ background: `url(${sidebarBg})`, backgroundSize: 'cover' }}>
                <Menu.Item>
                    <Image src={logo} style={{ width: '65px', height: 'auto', margin: 'auto' }} />
                </Menu.Item>
                {this.state.listMenu}
            </Sidebar>
        );
    }
}