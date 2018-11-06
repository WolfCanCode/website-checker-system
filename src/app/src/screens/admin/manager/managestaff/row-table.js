import React, { Component } from 'react';
import {Table,Button, Modal,Input, Dropdown} from 'semantic-ui-react'



  class TableRow extends Component {
    state = { open: false, open1 : false }

    show = size => () => this.setState({ size, open: true })
    close1 = () => this.setState({ open1: false })

    closeConfigShow = (closeOnEscape, closeOnDimmerClick) => () => {
        this.setState({ closeOnEscape, closeOnDimmerClick, open1: true })
    }
    close = () => this.setState({ open: false })
      
    render() {
        const options = [
            { key: 1, text: 'www.thanhnien.vn', value: 1 },
            { key: 2, text: 'vnexpress.net', value: 2 },
            { key: 3, text: 'bhcosmestics.com', value: 3 },
          ]
        const { open, size } = this.state
        const {  open1 ,closeOnEscape, closeOnDimmerClick } = this.state
        return (<Table.Row>
            {/* Delete */}
            <Modal
                open={open1}
                closeOnEscape={closeOnEscape}
                closeOnDimmerClick={closeOnDimmerClick}
                onClose={this.close1}
            >
                <Modal.Header>Assign New Website</Modal.Header>
                <Modal.Content >
                    <p >Staff Name</p>
                </Modal.Content>
                <Input type="text" style={{ marginLeft: '20px', width: '90%' }} defaultValue={this.props.nameStaff}></Input>
                <Modal.Content>
                    <p>Staff email</p>
                </Modal.Content>
                <Input type="text" style={{ marginLeft: '20px', marginBottom: '20px', width: '90%' }} defaultValue={this.props.emailStaff}></Input>
                <Modal.Content>
                    <p>Website email</p>
                </Modal.Content>
                <Dropdown style={{ marginLeft: '20px', marginBottom: '20px', width: '40%' }}clearable options={options} selection />
                <Modal.Actions>
                    <Button onClick={this.close1}>Cancel</Button>
                    <Button color="blue" content='Assign'/>
                </Modal.Actions>
            </Modal>

            {/* Edit */}
            <Modal size={size} open={open} onClose={this.close}>
                <Modal.Header>Edit Staff</Modal.Header>
                <Modal.Content >
                    <p >Staff Name</p>
                </Modal.Content>
                <Input type="text" style={{ marginLeft: '20px', width: '90%' }} defaultValue={this.props.nameStaff}></Input>
                <Modal.Content>
                    <p>Staff email</p>
                </Modal.Content>
                <Input type="text" style={{ marginLeft: '20px', marginBottom: '20px', width: '90%' }} defaultValue={this.props.emailStaff}></Input>
                <Modal.Content>
                    <p>Staff password</p>
                </Modal.Content>
                <Input type="text" style={{ marginLeft: '20px', marginBottom: '20px', width: '90%' }} defaultValue={this.props.passwordStaff}></Input>
                <Modal.Actions>
                    <Button onClick={this.close}>Cancel</Button>
                    <Button content='Done' color='blue' />
                </Modal.Actions>
            </Modal>
            
            <Table.Cell style={{ width: '100px', whiteSpace: 'normal', wordBreak: 'break-all' }}>{this.props.idStaff}</Table.Cell>
            <Table.Cell style={{ width: '100px', whiteSpace: 'normal', wordBreak: 'break-all' }}>Name: {this.props.nameStaff}<br/> Username: {this.props.emailStaff}</Table.Cell>
             <Table.Cell style={{ width: '100px', whiteSpace: 'normal', wordBreak: 'break-all' }}>
           
             </Table.Cell>
            <Table.Cell style={{ width: '100px', whiteSpace: 'normal', wordBreak: 'break-all' }}><Button onClick={this.show('mini')} > Edit </Button><Button onClick={this.closeConfigShow(false, true)}> New Assign</Button></Table.Cell> 
            </Table.Row>
        );
    }
}
export default TableRow