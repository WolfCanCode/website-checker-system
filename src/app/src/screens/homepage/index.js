import PropTypes from 'prop-types'
import React, { Component } from 'react'
import {
  Button,
  Container,
  Grid,
  Header,
  Icon,
  Image,
  List,
  Menu,
  Responsive,
  Segment,
  Sidebar,
  Visibility,
  Input
} from 'semantic-ui-react'
import 'semantic-ui-css/semantic.min.css';
import logo from '../../assets/icon-wsc.png';
import wallpaper from '../../assets/wallpaperfinal.jpg';
import {
    Link
} from "react-router-dom";
//layout 
const HomepageHeading = ({ mobile }) => (
  <Container text>
    <Header
      as='h1'
      content='Check your website first'
      inverted
      style={{
        fontSize: mobile ? '2em' : '4em',
        fontWeight: 'normal',
        marginBottom: 0,
        marginTop: mobile ? '1.5em' : '1.0em',
      }}
    />
    <Header
      as='h2'
      content='Please give it to us, we make it better. Improve your SEO, spelling checker, your security, any risks...'
      inverted
      style={{
        fontSize: mobile ? '1.1em' : '1.2em',
        fontWeight: 'normal',
        marginTop: mobile ? '0.5em' : '1.5em',
      }}
    />
  </Container>
)



HomepageHeading.propTypes = {
  mobile: PropTypes.bool,
}


class DesktopContainer extends Component {
  state = { txtSearch: "", searchLoading: false }

  hideFixedMenu = () => this.setState({ fixed: false })
  showFixedMenu = () => this.setState({ fixed: true })

  render() {
    const { children } = this.props
    const { fixed } = this.state

    return (
      <Responsive minWidth={Responsive.onlyTablet.minWidth}>
        <Visibility
          once={false}
          onBottomPassed={this.showFixedMenu}
          onBottomPassedReverse={this.hideFixedMenu}
        >
          <Segment
            textAlign='center'
            style={{ minHeight: 860, padding: '1em 0em', background: `url(${wallpaper})`, backgroundSize: 'cover' }}
            vertical
          >
            <Menu
              fixed={fixed ? 'top' : null}
              inverted={!fixed}
              pointing={!fixed}
              secondary={!fixed}
              size='large'
            >
              <Container>

                <Menu.Item as='a' position="left" >
                  <Image src={logo} style={{ width: '50px', height: 'auto' }} />
                  <font style={{ marginLeft: '10px', fontSize: '20px', color: 'white' }}>
                    WEBSITE CHECKER SYSTEM
                    </font>
                </Menu.Item>
                <Menu.Item as='a' active>
                  HOME
                </Menu.Item>
                <Menu.Item as='a' >TESTS WHAT?</Menu.Item>
                <Menu.Item as='a' >PRICING</Menu.Item>
                <Menu.Item >
                  <Button inverted={!fixed}>
                      <Link to={'/login'} style={{color:'white'}}>Log in</Link>
                  </Button>
                  <Button  inverted={!fixed} primary={fixed} style={{ marginLeft: '0.5em' }}>
                      <Link to={'/register'} style={{color:'white'}}>Sign up</Link>
                  </Button>
                </Menu.Item>
              </Container>
            </Menu>
            <HomepageHeading />
            <Input size='big' loading={this.state.searchLoading} label='http://' icon={<Icon name='search' inverted circular link onClick={() => this.onClickSearch()} />} placeholder='mysite.com' style={{ marginTop: '30px' }} value={this.state.txtSearch} onChange={event => this.onChangeText(event)} />          </Segment>
        </Visibility>

        {children}
      </Responsive>
    )
  }



  onChangeText(event) {
    this.setState({
      txtSearch: event.target.value
    });
  }

  onClickSearch() {
    if (this.state.txtSearch === "") { alert('Please input exact your website'); }
    else {
      this.setState({
        searchLoading: true
      });
    }
  }
}

DesktopContainer.propTypes = {
  children: PropTypes.node,
}

class MobileContainer extends Component {
  state = {}

  handlePusherClick = () => {
    const { sidebarOpened } = this.state

    if (sidebarOpened) this.setState({ sidebarOpened: false })
  }

  handleToggle = () => this.setState({ sidebarOpened: !this.state.sidebarOpened })

  render() {
    const { children } = this.props
    const { sidebarOpened } = this.state

    return (
      <Responsive maxWidth={Responsive.onlyMobile.maxWidth}>
        <Sidebar.Pushable>
          <Sidebar as={Menu} animation='uncover' inverted vertical visible={sidebarOpened}>
            <Menu.Item as='a'>
              <Image src={logo} style={{ width: '50px', height: 'auto', margin: 'auto' }} />
            </Menu.Item>
            <Menu.Item as='a'>Website Checker System</Menu.Item>
            <Menu.Item as='a' active>
              Home
            </Menu.Item>
            <Menu.Item as='a'>Tests What</Menu.Item>
            <Menu.Item as='a'>Pricing</Menu.Item>
            <Menu.Item as='a'>Log in</Menu.Item>
            <Menu.Item as='a'>Sign Up</Menu.Item>
          </Sidebar>

          <Sidebar.Pusher
            dimmed={sidebarOpened}
            onClick={this.handlePusherClick}
            style={{ minHeight: '100vh' }}
          >
            <Segment
              inverted
              textAlign='center'
              style={{ minHeight: 750, padding: '1em 0em', background: `url(${wallpaper})`, backgroundSize: 'cover' }}
              vertical
            >
              <Container>
                <Menu inverted pointing secondary size='large'>
                  <Menu.Item onClick={this.handleToggle}>
                    <Icon name='sidebar' />
                  </Menu.Item>
                  <Menu.Item position='right'>
                    <Button as='a' inverted>
                      Log in
                    </Button>
                    <Button as='a' inverted style={{ marginLeft: '0.5em' }}>
                      Sign Up
                    </Button>
                  </Menu.Item>
                </Menu>
              </Container>
              <HomepageHeading mobile />
              <Input size='big' label='http://' icon={<Icon name='search' inverted circular link />} placeholder='mysite.com' style={{ marginTop: '30px' }} />
            </Segment>

            {children}
          </Sidebar.Pusher>
        </Sidebar.Pushable>
      </Responsive>
    )
  }
}

MobileContainer.propTypes = {
  children: PropTypes.node,
}

const ResponsiveContainer = ({ children }) => (
  <div>
    <DesktopContainer>{children}</DesktopContainer>
    <MobileContainer>{children}</MobileContainer>
  </div>
)

// ResponsiveContainer.propTypes = {
//   children: PropTypes.node,
// }

const HomepageLayout = () => (
  <ResponsiveContainer>

    <Segment inverted vertical style={{ padding: '5em 0em' }}>
      <Container>
        <Grid divided inverted stackable>
          <Grid.Row>
            <Grid.Column width={3}>
              <Header inverted as='h4' content='About' />
              <List link inverted>
                <List.Item as='a'>Sitemap</List.Item>
                <List.Item as='a'>Contact Us</List.Item>
              </List>
            </Grid.Column>
            <Grid.Column width={3}>
              <Header inverted as='h4' content='Services' />
              <List link inverted>
                <List.Item as='a'>Pricing</List.Item>
                <List.Item as='a'>DNA FAQ</List.Item>
                <List.Item as='a'>How To Test</List.Item>
              </List>
            </Grid.Column>
            <Grid.Column width={7}>
              <Header as='h4' inverted>
                Website Checker System
              </Header>
              <p>
                Check your site, make it better, grow your bussiness
              </p>
            </Grid.Column>
          </Grid.Row>
        </Grid>
      </Container>
    </Segment>
  </ResponsiveContainer>
)
export default HomepageLayout