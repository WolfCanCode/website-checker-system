import React, { Component } from 'react';
import {
    BrowserRouter as Router,
    Route,
    Switch,
    withRouter
} from "react-router-dom";

//Import component
import HomeScreen from '../screens/homepage/index';
import LAB from '../screens/techlab/index';

import AdminScreen from '../screens/admin/index';
import LoginScreen from '../screens/login/index';
import RegisterScreen from '../screens/register/index';
import Dashboard from '../screens/admin/dashboard/index';

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
import ConfigScreen from '../screens/admin/config/index';

import DashboardManager from '../screens/admin/manager/dashboard/index';
import ManageStaffManagerScreen from '../screens/admin/manager/managestaff/index';
import ManageWebsiteManagerScreen from '../screens/admin/manager/managewebsite/index';
import ManageWordListManagerScreen from '../screens/admin/manager/managewordlist/index';

import Error404 from '../screens/admin/error404/index';
import { Cookies } from "react-cookie";


import { TransitionGroup, CSSTransition } from "react-transition-group";
import "./styles.css";

const cookies = new Cookies();


const PrivateRoute = ({ component: Component, ...rest }) => {
    var isManager = cookies.get("u_isManager");
    var isUser = cookies.get("u_id");
    if (isUser === undefined) {

    } else {
        if (isManager === "true") {
            return (
                window.location = './manager/home');
        }
        else {
            return (
                window.location = './admin/home');
        }
    }
}

// const _doLogout = () => {
//     cookies.remove("u_id");
//     cookies.remove("u_token");
//     cookies.remove("u_w_id");
//     cookies.remove("u_isManager");
//     return window.location = "/"
// }

// const StaffRoute = ({ component: Component, ...rest })  => {
//     fetch("/api/auth", {
//         method: 'POST',
//         headers: {
//             'Accept': 'application/json',
//             'Content-Type': 'application/json'
//         },
//         body: JSON.stringify({ "id": cookies.get("u_id"), "token": cookies.get("u_token") })
//     }).then(async response => response.json()).then(async (data) => {
//         if (data.action === "SUCCESS") {
//             await cookies.set("u_token", data.token, { path: "/" });
//             await cookies.set("u_isManager", data.isManager, { path: "/" });
//         } else {
//             _doLogout();
//         }
//     });
//         return(<Route {...rest} render={(props) => (
//         <Component {...props} />)} />);





// }

export class RouteClient extends Component {
    render() {
        return (<Router>
            <Switch>
                {/* Router for client page*/}
                <Route path="/" exact component={HomeScreen} />
                <Route path="/admin" component={AdminScreen} />
                <Route path="/wcs-lab" component={LAB} />
                <Route path="/manager" component={AdminScreen} />
                <Route path="/login" exact component={LoginScreen} />
                <Route path="/register" component={RegisterScreen} />
                <PrivateRoute path='/authenticate' />
                <Route path="*" component={Error404} />

            </Switch>
        </Router>
        )
    }
}

export const RouteStaff = withRouter(({ location }) => (
    <div style={{ background: "#fff", boxShadow: "-5px 5px 15px rgba(0,0,0,0.1)", maxHeight: '74%', overflowX: "auto", margin: 'auto' }}>
        <TransitionGroup>
            <CSSTransition
                key={location.key}
                classNames='fade'
                timeout={800}
            >
                <Switch>
                    {/* Router for admin page*/}

                    <Route path="/admin/home" exact component={Dashboard} />
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
                    <Route path='/admin/logout' component={() => window.location = './../login'} />
                    <Route path="*" component={Error404} />
                </Switch>
            </CSSTransition>
        </TransitionGroup>
    </div>


));


export const RouteManager = withRouter(({ location }) => (
    <div style={{ background: "#fff", boxShadow: "-5px 5px 15px rgba(0,0,0,0.1)", maxHeight: '74%', overflowX: "auto", margin: 'auto' }}>
        <TransitionGroup>
            <CSSTransition
                key={location.key}
                classNames='fade'
                timeout={800}
            >
                <Switch>
                    {/* Router for admin page*/}

                    <Route path="/manager/home" exact component={DashboardManager} />
                    <Route path="/manager/managestaff" component={ManageStaffManagerScreen} />
                    <Route path="/manager/managewebsite" component={ManageWebsiteManagerScreen} />
                    <Route path="/manager/managewordlist" component={ManageWordListManagerScreen} />
                    <Route path="/manager/setting" component={ConfigScreen} />
                    <Route path='/manager/logout' component={() => window.location = '../../login'} />
                    <Route path="*" component={Error404} />
                </Switch>
            </CSSTransition>
        </TransitionGroup>
    </div>


))






export default [RouteClient, RouteStaff, RouteManager]