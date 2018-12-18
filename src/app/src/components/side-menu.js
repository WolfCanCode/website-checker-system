import React, { Component } from 'react';
import {
    Link
} from "react-router-dom";
import { Menu, Sidebar, Image } from 'semantic-ui-react';
import { menu, menuMan, menuGuest } from '../config/menu';
import logo from '../assets/icon-wsc.png';
import sidebarBg from '../assets/sidebar.jpg';
import { Cookies } from "react-cookie";

const cookies = new Cookies();

export default class SideMenu extends Component {
    constructor(props) {
        super(props);
        this.state = { menuActive: "d", listMenu: [] };
    }

    componentDidMount() {
        this.setState({ menuActive: this.props.currKey })
        this._mapItem();
    }

    componentWillReceiveProps(props) {
        console.log(props);
        if (props.currKey !== null && props.currKey !== "") {
            this.setState({ menuActive: props.currKey })
        }
    }

    async _mapItem() {
        var menuList = null;
        if (cookies.get("u_isManager") === "true") {
            menuList = menuMan;
        } else
            if (cookies.get("u_guest_token") !== undefined) {
                menuList = menuGuest;
            }
            else {
                menuList = menu;
            }
        this.listMenuItem = menuList.map((item, index) => {
            if (item.items === null) {
                return (
                    <Menu.Item style={{ fontSize: 18, fontWeight: 'bold' }} key={index} as={Link} to={item.to} active={this.state.menuActive === item.key ? true : false} onClick={() => this._doActiveChange(item)}>
                        {item.name}
                    </Menu.Item>);
            } else {
                var childItem = item.items.map((itemC, indexC) => {
                    return (
                        <Menu.Item style={{ fontSize: 15 }} key={indexC} as={Link} to={itemC.to} active={this.state.menuActive === itemC.key ? true : false} onClick={() => this._doActiveChange(itemC)}>
                            {itemC.name}
                        </Menu.Item>
                    );
                });
                return (
                    <Menu.Item key={index}>
                        <Menu.Header style={{ fontSize: 18 }}>{item.name}</Menu.Header>
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
        this.props.updateHeader(item.name, item.alt, item.image);
    }

    render() {
        return (
            <Sidebar as={Menu} animation='overlay' inverted vertical visible={this.props.visible} style={{ background: `url(${sidebarBg})`, backgroundSize: 'cover', width: 180 }}>
                <Menu.Item>
                    <Image src={logo} style={{ width: '65px', height: 'auto', margin: 'auto' }} />
                </Menu.Item>
                {this.state.listMenu}
            </Sidebar>
        );
    }
}