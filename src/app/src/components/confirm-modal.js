import React, { Component } from 'react';
// import { Cookies } from "react-cookie";
import { Button, Header, Icon, Modal } from 'semantic-ui-react';

// const cookies = new Cookies();

class ConfirmModal extends Component {

    _onClickYes(){
        this.props.choose(true);
    }

    _onClickNo(){
        this.props.choose(false);
    }

    render() {
        return (
            <Modal open={this.props.show} basic size='small'>
                <Header icon={this.props.icon} content={this.props.title} />
                <Modal.Content>
                    <p>
                        {this.props.content}
                    </p>
                </Modal.Content>
                <Modal.Actions>
                    <Button basic color='red' inverted onClick={()=>this._onClickNo()}>
                        <Icon name='remove' /> No
      </Button>
                    <Button color='green' inverted onClick={()=>this._onClickYes()}>
                        <Icon name='checkmark' /> Yes
      </Button>
                </Modal.Actions>
            </Modal>
        )
    };
}

export default ConfirmModal;