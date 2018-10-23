import React, { Component } from 'react';
import { Segment, Sidebar, Image } from 'semantic-ui-react'
import { RouteAdmin } from '../../config/routes';
import { BrowserRouter as Router } from "react-router-dom";
import SideMenu from '../../components/side-menu';
import HeaderContent from '../../components/header-content';
import HeaderAdmin from '../../components/header-admin';
import menu from '../../config/menu';

export default class AdminScreen extends Component {
    state = {
        titleHeader: "Dashboard",
        altHeader: "This is Dashboard",
        imgSrc: ""
    }

    _onUpdateHeader(title, alt, image) {
        this.setState({ titleHeader: title, altHeader: alt, imageSrc: image});
    }

    componentDidMount() {
        var check = false;
        for (let i = 0; i < menu.length; i++) {
            if (menu[i].items === null) {
                if (menu[i].to === this.props.location.pathname) {
                    this.setState({ titleHeader: menu[i].name, altHeader: menu[i].alt });
                    check = true;
                }
            } else {
                for (let j = 0; j < menu[i].items.length; j++) {
                    if (menu[i].items[j].to === this.props.location.pathname) {
                        this.setState({ titleHeader: menu[i].items[j].name, altHeader: menu[i].items[j].alt, imageSrc: menu[i].items[j].image });
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
                <div style={{ height: '100vh' }}>
                    <Sidebar.Pushable as={Segment} style={{ background: "#E0E0E0" }}>
                        <SideMenu updateHeader={(title, alt, image) => this._onUpdateHeader(title, alt, image)} />
                        <HeaderAdmin />
                        <Sidebar.Pusher>
                            <Image src={this.state.imageSrc} size='medium' style={{ margin: 'auto', position: 'absolute', zIndex: '999999', marginLeft: '82vw', marginTop: '8vh', height: 150, width: 'auto', transition:'all 1s' }} />
                            <Segment style={{ background: "#F5F5F5", marginLeft: '160px', marginRight: '10px', marginTop: '60px', height: '91vh' }}>
                                <HeaderContent title={this.state.titleHeader} alt={this.state.altHeader} />
                                <RouteAdmin />
                            </Segment>
                        </Sidebar.Pusher>
                    </Sidebar.Pushable></div>
            </Router >
        );
    }
}