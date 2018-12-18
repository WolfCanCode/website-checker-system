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
  Input,
  Dimmer,
  Progress
} from 'semantic-ui-react'
import 'semantic-ui-css/semantic.min.css';
import logo from '../../assets/icon-wsc.png';
import wallpaper from '../../assets/wallpaper.jpg';
import laptop from '../../assets/laptop.png';
import {
  Link
} from "react-router-dom";
import { Cookies } from "react-cookie";
const cookies = new Cookies();
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
  state = { txtSearch: "", searchLoading: false, percent: 0 }

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
            style={{ height: '100%', minHeight: '100vh', padding: '1em 0em', background: `url(${wallpaper})`, backgroundSize: 'cover' }}
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
                {this.state.searchLoading ? <Dimmer active>
                  <h1>Wait a little bit...</h1>
                  <Progress style={{ width: 600 }} percent={this.state.percent} indicating />

                </Dimmer> : ""}

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
                <Menu.Item as={Link} to={'/wcs-lab'} >WCS LAB</Menu.Item>
                <Menu.Item >
                  <Button inverted={!fixed} as={Link} to={'/login'}>
                    {/* <Link to={'/login'} style={{color:'white'}}>Log in</Link> */}
                    Log in
                  </Button>
                  <Button inverted={!fixed} primary={fixed} style={{ marginLeft: '0.5em' }}>
                    Sign up
                  </Button>
                </Menu.Item>
              </Container>
            </Menu>
            <HomepageHeading />
            <Input size='big' type="" icon={<Icon name='search' inverted circular link onClick={() => this.onClickSearch()} />} placeholder='mysite.com' style={{ marginTop: '30px' }} value={this.state.txtSearch} onChange={event => this.onChangeText(event)} />
            <Image style={{ position: 'relative', width: '80vh', margin: 'auto', top: '5vh', }} src={laptop} />

          </Segment>

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
    if (this.state.txtSearch === "" || !this.isURL(this.state.txtSearch)) { alert('Please input exact your website'); }
    else {
      cookies.remove("u_id");
      this.setState({
        searchLoading: true
      });
      setInterval(() => {
        this.setState({
          percent: this.state.percent + 1
        });
        if (this.state.percent >= 90) {
          clearInterval();
        }
      }, 1000);

      fetch("/api/guest/get", {
        method: 'POST',
        headers: {
          'Accept': 'application/json',
          'Content-Type': 'application/json'
        },
        body: JSON.stringify({ "url": this.state.txtSearch })
      }).then(response => response.json()).then((res) => {
        if (res.token !== undefined && res.token !== null) {
          this.setState({
            percent: 100
          });
          cookies.set("u_guest_token", res.token, { path: "/" });
          window.location.href = "../guest/brokenLink";
        }
      });
    }


  }

  isURL(str) {
    var pattern = new RegExp('^(https?:\\/\\/)?' + // protocol
      '((([a-z\\d]([a-z\\d-]*[a-z\\d])*)\\.)+[a-z]{2,}|' + // domain name and extension
      '((\\d{1,3}\\.){3}\\d{1,3}))' + // OR ip (v4) address
      '(\\:\\d+)?' + // port
      '(\\/[-a-z\\d%@_.~+&:]*)*' + // path
      '(\\?[;&a-z\\d%@_.,~+&:=-]*)?' + // query string
      '(\\#[-a-z\\d_]*)?$', 'i'); // fragment locator
    return pattern.test(str);
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
            <Menu.Item as={Link} to="/login">Log in</Menu.Item>
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
              style={{ height: 'auto', minHeight: '100vh', padding: '1em 0em', background: `url(${wallpaper})`, backgroundSize: 'cover' }}
              vertical
            >
              <Container>

                <Menu inverted pointing secondary size='large'>
                  <Menu.Item onClick={this.handleToggle}>
                    <Icon name='sidebar' />
                  </Menu.Item>
                  <Menu.Item position='right'>
                    <Button as={Link} to="/login" inverted>
                      Log in
                    </Button>
                    <Button as='a' inverted style={{ marginLeft: '0.5em' }}>
                      Sign Up
                    </Button>
                  </Menu.Item>
                </Menu>
              </Container>
              <HomepageHeading mobile />

              <Input size='big' label='https://' icon={<Icon name='search' inverted circular link />} placeholder='mysite.com' style={{ marginTop: '30px' }} />
              <Image style={{ position: 'relative', width: '80vh', margin: 'auto', top: '5vh', }} src={laptop} />

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