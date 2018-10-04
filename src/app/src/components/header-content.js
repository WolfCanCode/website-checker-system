import React, { Component } from 'react';
import { Segment, Header } from 'semantic-ui-react';


export default class HeaderContent extends Component {
    render() {
        return (
            <Segment basic>
                <Header as='h1' >{this.props.title}</Header>
                <span style={{ fontStyle: 'italic' }}>{this.props.alt}</span>
            </Segment>
        )
    }
}
