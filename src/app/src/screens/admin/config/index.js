import React, { Component } from 'react';
import 'semantic-ui-css/semantic.min.css';
import { Segment, Statistic, Icon} from 'semantic-ui-react'
import { Cookies } from "react-cookie";

const cookies = new Cookies();



class ConfigScreen extends Component {

    state = {  isLoading: false, isDisable: false };


    componentDidMount() {


    }

    _onClickRedo() {
        this.setState({isLoading:true});
        var param = {"userId":cookies.get("u_id"),"userToken":cookies.get("u_token"),"websiteId":cookies.get("u_w_id")};
        fetch("/api/sitemap/getNewVer", {
            method: 'POST',
            headers: {
                'Accept': 'application/json',
                'Content-Type': 'application/json'
            },
            body: JSON.stringify(param)
        }).then(response => response.json()).then((data) => {
            if(data.action==="SUCCESS")
            {
                this.setState({isLoading:false});
            } else {
                alert("Thất bại");
            }
        });
    }
    

    render() {
        return (
            <Segment.Group>
                <Segment>                    <Icon circular inverted name='redo' loading={this.state.isLoading} onClick={()=>this._onClickRedo()} />

                    <Statistic>
                        <Statistic.Label>SITEMAP VERSION</Statistic.Label>
                        <Statistic.Value>1</Statistic.Value>
                    </Statistic>

                </Segment>
            </Segment.Group>

        );
    }



}

export default ConfigScreen;