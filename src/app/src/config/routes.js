import React, { Component } from 'react';
import {
    BrowserRouter as Router,
    Route,
    Switch,
    withRouter
} from "react-router-dom";

//Import component
import HomeScreen from '../screens/homepage/index';
import AdminScreen from '../screens/admin/index';
import AdminScreen1 from '../screens/admin/index1';
import LoginScreen from '../screens/login/index';
import RegisterScreen from '../screens/register/index';
import Dashboard from '../screens/admin/dashboard/index';
import Dashboard1 from '../screens/admin/dashboard/index1';

import SiteMapScreen from '../screens/admin/sitemap/controller';
import brokenLinksScreen from '../screens/admin/brokenLinks/index';
import brokenPagesScreen from '../screens/admin/brokenPages/index';
import missingFilesScreen from '../screens/admin/missingFiles/index';
import spellingScreen from '../screens/admin/spelling/index';
import speedTestScreen from '../screens/admin/speedTest/index';
import faviconScreen from '../screens/admin/favicons/index';
import ContactScreen from '../screens/admin/contact/index';
import DireactionScreen from "../screens/admin/direction/index";
import PagesScreen from '../screens/admin/pages/index';
import JavascriptErrorScreen from '../screens/admin/javascripterror/index';
import GrammarScreen from '../screens/admin/grammar/index';
import MobileLayoutScreen from '../screens/admin/mobileLayout/index';

import ProhibitedScreen from '../screens/admin/prohibitedcontent/index';
import serverBehaviorScreen from '../screens/admin/serverBehavior/index';
import CookieLawScreen from '../screens/admin/cookielaw/index';

import Error404 from '../screens/admin/error404/index';

import { TransitionGroup, CSSTransition } from "react-transition-group";
import "./styles.css";

export class RouteClient extends Component {
    render() {
        return (<Router>
            <Switch>
                {/* Router for client page*/}
                <Route path="/" exact component={HomeScreen} />
                <Route path="/admin" component={AdminScreen} />
                <Route path="/admin1" component={AdminScreen1} />
                <Route path="/login" exact component={LoginScreen} />
                <Route path="/register" component={RegisterScreen} />
                <Route path='/authenticate' component={() => window.location = '/admin'} />
            </Switch>
        </Router>
        )
    }
}

export const RouteAdmin = withRouter(({ location }) => (
    <div style={{ background: "#fff", boxShadow: "-5px 5px 15px rgba(0,0,0,0.1)", maxHeight: '72vh', overflowX: "auto" }}>
        <TransitionGroup>
        <CSSTransition
            key={location.key}
            classNames='fade'
            timeout={800}
        >
            <Switch>
                {/* Router for admin page*/}
                <Route path="/admin" exact component={Dashboard} />
                <Route path="/admin/sitemap" component={SiteMapScreen} />
                <Route path="/admin/brokenLinks" component={brokenLinksScreen} />
                <Route path="/admin/brokenPages" component={brokenPagesScreen} />
                <Route path="/admin/missingFiles" component={missingFilesScreen} />
                <Route path="/admin/speedTest" component={speedTestScreen} />
                <Route path="/admin/spelling" component={spellingScreen} />
                <Route path="/admin/favicons" component={faviconScreen} />
                <Route path="/admin/contact" component={ContactScreen} />
                <Route path="/admin/mobileLayout" component={MobileLayoutScreen} />
                <Route path="/admin/serverBehavior" component={serverBehaviorScreen} />
                <Route path="/admin/direction" component={DireactionScreen} />
                <Route path="/admin/pages" component={PagesScreen} />
                <Route path="/admin/javascripterror" component={JavascriptErrorScreen} />
                <Route path="/admin/grammar" component={GrammarScreen} />
                <Route path="/admin/prohibited" component={ProhibitedScreen} />
                <Route path="/admin/cookielaw" component={CookieLawScreen} />
                <Route path='/logout' component={() => window.location = '../login'} />
                <Route path="/admin1" exact component={Dashboard1} />
                <Route path="*" component={Error404}/>
            </Switch>
            </CSSTransition>
            </TransitionGroup>
            </div>
        ))
    




export default [RouteClient, RouteAdmin]