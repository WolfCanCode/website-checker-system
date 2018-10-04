import React from 'react'
import { Button, Form, Grid,  Header, Image, Message, Segment } from 'semantic-ui-react'
import 'semantic-ui-css/semantic.min.css';
import logo from '../../assets/icon-wsc.png';
import Gender from './gender';

const RegisterScreen = () => (
  <div className='login-form'>
    {/*
      Heads up! The styles below are necessary for the correct render of this example.
      You can do same with CSS, the main idea is that all the elements up to the `Grid`
      below must have a height of 100%.
    */}
    <style>{`
      body > div,
      body > div > div,
      body > div > div > div.login-form {
        height: 100%;
      }
    `}</style>
    <Grid textAlign='center' style={{ height: '100%' }} >
      <Grid.Column style={{ maxWidth: 600, }}>
        <Header as='h2' color='teal' textAlign='center'>
          <Image src={logo} style={{ width: '100px', height: 'auto', marginTop: '-100px', position: 'absolute', marginLeft: '-65px' }} /> <h1>Sign up with your email address</h1>
        </Header>
       
        <Form size='large'>
        <Segment stacked>
            <Form.Input fluid icon='user' iconPosition='left' placeholder='Firstname' />
            <Form.Input fluid icon='user' iconPosition='left' placeholder='Lastname' type='text' />
            <Form.Input fluid icon='user' iconPosition='left' placeholder='Username' type='text' />
            <Form.Input fluid icon='mail' iconPosition='left' placeholder='Email' type='text' />
            <Form.Input fluid icon='lock' iconPosition='left' placeholder='Password' type='password' />
            <Form.Input fluid icon='lock' iconPosition='left' placeholder='Confirm password' type='password' />

            <div className='field' style={{ width: '200px' }}>
              <Gender />
            </div>
        
            <div className='ui checkbox' style={{ marginLeft:'-330px' }}>
              <input type='checkbox'  />
              <label>Send notifications to my email.</label>

            </div>
            <p style={{ marginTop: '14px', }} className="notice">By clicking on Sign up, you agree to WCS's <a target="_blank"> Terms and Conditions of Use</a></p>
            <p>To learn more about how WCS collects, uses, shares and protects your personal data please read WCS's <a target="_blank">Privacy Policy</a></p>
            <Button color='teal' fluid size='large'style={{ width: '300px', marginTop : '14px', marginLeft : '90px', }}>
              SIGN UP
            </Button>
            </Segment>
        </Form>
        <Message>
        Already have an account ? <a href='/login'>Login</a>
        </Message>

      </Grid.Column>
    </Grid>
  </div>
)

export default RegisterScreen