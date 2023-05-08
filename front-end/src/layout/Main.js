import React from 'react'
import { Layout, Row, Col } from 'antd';
import { Link, Outlet } from 'react-router-dom'
import '../index.css';
import Logout from '../component/UserOperate';
import MyMenu from './MyMenu';

const { Header, Footer, Sider, Content } = Layout;

class Main extends React.Component {
  render() {
    return (
      <><Layout>
        <Header>
          <Row>
            <Col span={4}>
              <div className="logo" >
                <Link to="/">Home</Link>
              </div>
            </Col>
            <Col span={19}></Col>
            <Col span={1}>
              <div >
                <Logout />
              </div>
            </Col>
          </Row>
        </Header>
        <Layout>
          <Sider>
            <MyMenu />
          </Sider>
          <Content
            className="site-layout-background"
            style={{
              padding: 24,
              margin: 0,
              minHeight: '100%',
            }}
          >
            <Outlet />
          </Content>
        </Layout>
        <Footer style={{ textAlign: 'center' }}>xxxx ©2022 Created by IBM CIC SJ</Footer>
      </Layout></>
    );
  }
}

export default Main;