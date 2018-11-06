import React, { Component } from 'react';
import { Segment, Sidebar, Image } from 'semantic-ui-react'
import { RouteManager, RouteStaff } from '../../config/routes';
import { BrowserRouter as Router } from "react-router-dom";
import SideMenu from '../../components/side-menu';
import HeaderContent from '../../components/header-content';
import HeaderAdmin from '../../components/header-admin';
import { menu, menuMan } from '../../config/menu';
import { Cookies } from "react-cookie";
const cookies = new Cookies();

export default class AdminScreen extends Component {
    state = {
        titleHeader: "Dashboard",
        altHeader: "This is Dashboard",
        imgSrc: "",
        redirect: null,
        listWeb: [],
        txtWebpage: "",
        showConfirmModal: false,
        logout: false,
        currKey: ""
    }

    componentWillMount() {
        fetch("/api/auth", {
            method: 'POST',
            headers: {
                'Accept': 'application/json',
                'Content-Type': 'application/json'
            },
            body: JSON.stringify({ "id": cookies.get("u_id"), "token": cookies.get("u_token") })
        }).then(async response => response.json()).then(async (data) => {
            if (data.action === "SUCCESS") {
                await cookies.set("u_token", data.token, { path: "/" });
                await cookies.set("u_isManager", data.isManager, { path: "/" });
            } else {
                alert("Phiên đăng nhập hết hạn");
                return window.location = "../login";
            }
        });
    }

    _onUpdateHeader(title, alt, image) {
        this.setState({ titleHeader: title, altHeader: alt, imageSrc: image });
    }

    _onConfirm(result) {
        alert(result);
        this.setState({ showConfirmModal: false });
    }

    _changeWebsiteConfirm(id) {
        cookies.set("u_w_id", id, { path: '/' });
    }

    _logoutLoading(load) {
        if (load) {
            this.setState({ logout: true });
            window.location.href = "../login"
        }
    }

    componentDidMount() {
        var check = false;
        var menuList = null;
        if (cookies.get("u_isManager") === "true") {
            menuList = menuMan;
        } else {
            menuList = menu;
        }
        for (let i = 0; i < menuList.length; i++) {
            if (menuList[i].items === null) {
                if (menuList[i].to === this.props.location.pathname) {
                    this.setState({ titleHeader: menuList[i].name, altHeader: menuList[i].alt, currKey: menu[i].key });
                    check = true;
                }
            } else {
                for (let j = 0; j < menuList[i].items.length; j++) {
                    if (menuList[i].items[j].to === this.props.location.pathname) {
                        this.setState({ titleHeader: menuList[i].items[j].name, altHeader: menuList[i].items[j].alt, imageSrc: menuList[i].items[j].image, currKey: menu[i].key });
                        check = true;
                    }
                }
            }
            if (check === false) {
                this.setState({ titleHeader: "ERROR 404", altHeader: "" });
            }
        }
    }

    render() {
        return (
            <Router>

                <Segment style={{ height: '100vh', padding: 0, border: 0 }} loading={this.state.logout}>
                    <Sidebar.Pushable as={Segment} style={{ background: "#E0E0E0" }}>
                        <SideMenu currKey={this.state.currKey} updateHeader={(title, alt, image) => this._onUpdateHeader(title, alt, image)} />
                        <HeaderAdmin changeWebsite={(id) => this._changeWebsiteConfirm(id)} logout={(load) => this._logoutLoading(load)} />
                        <Sidebar.Pusher>
                            <Image src={this.state.imageSrc} size='medium' style={{ margin: 'auto', position: 'absolute', zIndex: '999999', marginLeft: '82vw', marginTop: '8vh', height: 150, width: 'auto', transition: 'all 1s' }} />
                            <Segment style={{ background: "#F5F5F5", marginLeft: '160px', marginRight: '10px', marginTop: '60px', height: '91vh' }}>
                                <HeaderContent title={this.state.titleHeader} alt={this.state.altHeader} />
                                {cookies.get("u_isManager") === "true" ? <RouteManager /> : <RouteStaff />}
                            </Segment>
                        </Sidebar.Pusher>
                    </Sidebar.Pushable>
                </Segment>
            </Router >
        );
    }
}