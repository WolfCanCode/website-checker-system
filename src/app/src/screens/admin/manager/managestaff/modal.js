import React, { Component } from 'react'
import { Button, Header, Icon, Modal, Form} from 'semantic-ui-react'

export default class ModalExampleControlled extends Component {
  state = { modalOpen: false ,name:'',email:'',submittedName:'',submittedEmail:'', selectWeb:'',submittedWeb:''}

  handleOpen = () => this.setState({ modalOpen: true })

  handleClose = () => this.setState({ modalOpen: false })
  handleChange = (e, { name, value }) => this.setState({ [name]: value })

  handleSubmit = () => {
    const { name, email, selectWeb } = this.state
    
    this.setState({ submittedName: name, submittedEmail: email, submittedWeb:selectWeb })
    this.setState({name:'',email:''})
    this.setState({modalOpen:false})
    
  }

  render() {
    // const { name, email, submittedName, submittedEmail } = this.state
    console.log("Test "+this.state.submittedEmail+" "+this.state.submittedName)
    return (
      <Modal
        trigger={<Button onClick={this.handleOpen}>Show Modal</Button>}
        open={this.state.modalOpen}
        onClose={this.handleClose}
        
        size='small'
      >
        <Header icon='browser' content='Cookies policy' />
        <Modal.Content>
        <Form onSubmit={this.handleSubmit}>
          {/* <Form.Group> */}
            <Form.Input placeholder='Name' name='name' value={this.state.name} onChange={this.handleChange} />
            
            <Form.Input
              placeholder='Email'
              name='email'
              value={this.state.email}
              onChange={this.handleChange}
            />
            <Form.Field label='An HTML <select>' control='select'>
        <option value='https://thanhnien.vn'>https://thanhnien.vn</option>
        <option value='https://vnexpress.net'>https://vnexpress.net</option>
      </Form.Field>
           
          {/* </Form.Group> */}
        </Form>
        </Modal.Content>
        <Modal.Actions>
          <Button color='red' onClick={this.handleClose} inverted>
            <Icon name='checkmark' /> Cancel
          </Button>
          <Button color='red' onClick={this.handleSubmit} inverted>
            <Icon name='checkmark' /> Submit
          </Button>
        </Modal.Actions>
      </Modal>
    )
  }
}