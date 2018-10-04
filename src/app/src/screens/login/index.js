import React, { Component } from 'react';
import { Button, Form, Grid, Header, Image, Segment, Message } from 'semantic-ui-react';
import 'semantic-ui-css/semantic.min.css';
import bg1 from '../../assets/bg-login-layout1.jpg';
import bg2 from '../../assets/bg-login-layout2.jpg';
import divLogin from '../../assets/divider-login.png';
import { Redirect } from "react-router-dom";

class LoginScreen extends Component {
    constructor(props) {
        super(props);
        this.state = { userName: "", password: "", isLoading: false, isLogin: false };
    }

    render() {
        return (
            <div className='login-form'>
                <style>{`
                    body > div,
                    body > div > div,
                    body > div > div > div.login-form {
                    height: 100%;
                    } 
                `}
                </style>
                <Segment.Group horizontal style={{ height: '100%', margin: 0 }}>
                    <Segment basic style={{ flex: 1, background: `url(${bg1})`, backgroundSize: 'cover' }}>
                        <Grid style={{ height: 150 }}></Grid>
                        <Grid style={{ height: 'auto' }} >
                            <Grid.Column style={{ width: '100%', height: 'auto' }}>
                                <Header as='h2' color='blue' textAlign='center' >
                                    <Segment padded basic style={{ width: '100%', height: 'auto' }}>
                                        <p style={{ fontSize: 60 }}>Login</p>
                                        <Image src={divLogin} style={{ margin: 'auto' }} />
                                    </Segment>
                                </Header>
                                <Form size='large'>
                                    <Segment stacked basic>
                                        <Form.Input
                                            fluid
                                            icon='user'
                                            iconPosition='left'
                                            size='large'
                                            placeholder='E-mail address'
                                            value={this.state.userName}
                                            onChange={event => this._onUpdateUsername(event)}
                                        />
                                        <Form.Input
                                            fluid
                                            icon='lock'
                                            iconPosition='left'
                                            size='large'
                                            placeholder='Password'
                                            type='password'
                                            value={this.state.password}
                                            onChange={event => this._onUpdatePassword(event)}
                                        />
                                        {this.renderRedirect()}
                                        <Button color='blue' fluid size='large' loading={this.state.isLoading} onClick={() => this._doLogin()}>
                                            Login
                                </Button>
                                        <div style={{ height: 30 }}>

                                            <a style={{ float: 'right', marginTop: 15 }}>Forgot Password</a>
                                        </div>

                                    </Segment>
                                </Form>
                                <Message>
                                    Don't have account? <a href='/register'>Sign Up</a>
                                </Message>
                            </Grid.Column>
                        </Grid>
                    </Segment>
                    <Segment basic style={{ flex: 2.5, background: `url(${bg2})`, backgroundSize: 'cover' }}></Segment>
                </Segment.Group>
            </div>
        );
    }

    _onUpdateUsername(event) {
        this.setState({ userName: event.target.value })
    }

    _onUpdatePassword(event) {
        this.setState({ password: event.target.value })
    }

    renderRedirect = () => {
        if (this.state.isLogin) {
            return <Redirect to='/authenticate' />
        }
    }

    async _doLogin() {
        this.setState({ isLoading: true });
        var param = { "email": this.state.userName, "password": this.state.password };
        const response = await fetch('/api/login', {
            method: 'POST',
            headers: {
                'Accept': 'application/json',
                'Content-Type': 'application/json'
            },
            body: JSON.stringify(param)
        });
        var user = null;
        try { user = await response.json(); }
        catch (e) {
            alert("Sai email hoặc password");
        }

        if (user === null) {
        } else {
            this.setState({ isLogin: true });
        }
        this.setState({ isLoading: false });
    }
}

export default LoginScreen;