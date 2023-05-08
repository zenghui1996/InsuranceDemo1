import React from 'react'
import { Menu } from 'antd';
import { Link } from 'react-router-dom'
import {
  SettingOutlined,
  DatabaseOutlined,
  BarChartOutlined,
  SmileOutlined,
  TrophyOutlined,
  UserAddOutlined,
  UserDeleteOutlined,
  PercentageOutlined
} from '@ant-design/icons';
import '../index.css';

const { SubMenu } = Menu;

class MyMenu extends React.Component {
  constructor(props) {
    super(props)
    this.state = { menuNodes: this.getMenuNodes() }
  }

  getMenuNodes() {
    if ('2' === localStorage.getItem('adminnistrator_flag')
      || '3' === localStorage.getItem('adminnistrator_flag')) {
      return (
        <SubMenu key="sub1" icon={<SettingOutlined />} title="系统管理" >
          <Menu.Item key="11">
            <Link to="UserManage">用户管理</Link>
          </Menu.Item>
          <Menu.Item key="13">
            <Link to="DataManage">数据导入</Link>
          </Menu.Item>
        </SubMenu>
      )
    }
  }

  render() {
    return (
      <>
        <Menu
          mode="inline"
          defaultOpenKeys={['sub1', 'sub2']}
          style={{ height: '100%', borderRight: 0 }}
        >
          <Menu.Item key="01" icon={<SmileOutlined />}>
            <Link to="/">Welcome</Link>
          </Menu.Item>
          {this.state.menuNodes}
          <Menu.Item key="21" icon={<DatabaseOutlined />}>
            <Link to="DataList">数据一览</Link>
          </Menu.Item>
          <SubMenu key="sub2" title="BandMix统计" icon={<BarChartOutlined />}>
            <Menu.Item key="22">
              <Link to="BandMixMonth">BandMix月别</Link>
            </Menu.Item>
            <Menu.Item key="23">
              <Link to="BandMixQuarter ">BandMix季度别</Link>
            </Menu.Item>
          </SubMenu>
          <Menu.Item key="24" icon={<TrophyOutlined />}>
            <Link to="PromoteData">Promote统计</Link>
          </Menu.Item>
          <Menu.Item key="25" icon={<UserAddOutlined />}>
            <Link to="NewHireDate">NewHire统计</Link>
          </Menu.Item>
          <Menu.Item key="26" icon={<UserDeleteOutlined />}>
            <Link to="ResignationData">离职统计</Link>
          </Menu.Item>
          <Menu.Item key="27" icon={<PercentageOutlined />}>
            <Link to="RatioList">Rate统计</Link>
          </Menu.Item>
          <Menu.Item key="90">
            <Link to="Sample">sample</Link>
          </Menu.Item>
          <Menu.Item key="91">
            <Link to="DataListSample">DataListSample</Link>
          </Menu.Item>
        </Menu></>
    );
  }
}

export default MyMenu;