import React, { Component } from 'react';
import { Segment, Header, Button } from 'semantic-ui-react';
import { Cookies } from "react-cookie";
const cookies = new Cookies();


export default class HeaderContent extends Component {
    render() {
        return (
            <Segment basic>
                <Header as='h1' >{this.props.title} </Header>
                <span style={{ fontStyle: 'italic' }}>{this.props.alt}</span><br />
                {cookies.get("u_isManager")!=="true"?
                <Segment>
                    <Button
                        color='grey'
                        icon='settings'
                        content="Pages"
                        label={{ basic: true, color: 'green', pointing: 'left', content: '25' }}
                    />
                </Segment>: ""}


            </Segment>
        )
    }
}
