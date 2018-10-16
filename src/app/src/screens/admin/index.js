import React, { Component } from 'react';
import { Segment, Sidebar } from 'semantic-ui-react'
import { RouteAdmin } from '../../config/routes';
import { BrowserRouter as Router } from "react-router-dom";
import SideMenu from '../../components/side-menu';
import HeaderContent from '../../components/header-content';
import HeaderAdmin from '../../components/header-admin';

export default class AdminScreen extends Component {
    state = {
        titleHeader: "Dashboard",
        altHeader: "This is Dashboard"
    }

    _onUpdateHeader(title, alt) {
        this.setState({ titleHeader: title, altHeader: alt });
    }

    render() {
        return (
            <Router>
                <div style={{ height: '100vh' }}>
                    <Sidebar.Pushable as={Segment} style={{background:"#E0E0E0"}}>
                        <SideMenu updateHeader={(title, alt) => this._onUpdateHeader(title, alt)} />
                        <HeaderAdmin/>
                        <Sidebar.Pusher>
                            <Segment style={{background:"#F5F5F5", marginLeft: '160px', marginRight: '10px', marginTop: '60px', boxShadow:"0 5px 15px rgba(0,0,0,0.2)" , height: 685}}>
                                <HeaderContent title={this.state.titleHeader} alt={this.state.altHeader} />
                                <RouteAdmin />
                            </Segment>
                        </Sidebar.Pusher>
                    </Sidebar.Pushable></div>
            </Router >
        );
    }
}