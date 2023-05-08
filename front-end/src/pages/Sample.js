import React from 'react';
import {Button,Row,Col,Table,Space, Input} from 'antd';
import { Menu, Dropdown, message } from 'antd';
import { Select } from 'antd';
import { Checkbox } from 'antd';
import { Radio } from 'antd';
import '../mystyle.less'
import { DualAxes } from '@ant-design/plots';

class Sample extends React.Component {

  render () {
    return (
      <div style={{width:'960px'}}>
        <Row style={{borderBottom:'1px solid'}}>
          <Col span={4}><h1>组件</h1></Col>
          <Col span={20}><h1>例子</h1></Col>
        </Row>
        {/* <Divider/> */}
        <Row style={{padding:'5px'}}>
          <Col span={4}><h1>Input输入框</h1></Col>
          <Col span={20}>
            <Space size="middle">  
              <Input bordered={true} allowClear></Input>
            </Space>
          </Col>
        </Row>
        <Row style={{padding:'5px'}}>
          <Col span={4}><h1>Button按钮</h1></Col>
          <Col span={20}>
            <Space size="middle">
              {/* <Button type="primary" size="large">large</Button> */}
              <Button type="primary" size="middle">Primary</Button>
              <Button type="primary" size="middle" danger>削除</Button>
              {/* <Button type="primary" size="small">small</Button> */}
            </Space>
          </Col>
        </Row>
        <Row style={{padding:'5px'}}>
          <Col span={4}><h1>Dropdown下拉菜单</h1></Col>
          <Col span={20}>
            <Space size="middle">  
              <Dropdown.Button onClick={handleButtonClick} overlay={menu}>
                Dropdown下拉菜单
              </Dropdown.Button>
            </Space>
          </Col>
        </Row>
        <Row style={{padding:'5px'}}>
          <Col span={4}><h1>Select选择器</h1></Col>
          <Col span={4}>
            <Space size="middle">  
              <Select defaultValue="1" style={{ width: 120 }} onChange={handleChange}>
                <Option value="1">选项1</Option>
                <Option value="2">选项2</Option>
                <Option value="3" disabled>
                选项3
                </Option>
                <Option value="4">选项4</Option>
              </Select>
            </Space>
          </Col>
          <Col span={16}>
            <Space>
              <Select
                mode="multiple"
                style={{ width: '240px' }}
                placeholder="select one option"
                defaultValue={['1']}
                onChange={handleChange}
                optionLabelProp="label"
              >
                <Option value="1" label="选项1">
                  <div>
                    选项1
                  </div>
                </Option>
                <Option value="2" label="选项2">
                  <div >
                    选项2
                  </div>
                </Option>
                <Option value="3" label="选项3">
                  <div>
                    选项3
                  </div>
                </Option>
              </Select>
            </Space>
          </Col>
        </Row>
        <Row style={{padding:'5px'}}>
          <Col span={4}><h1>Checkbox多选框</h1></Col>
          <Col span={20}>
            <Space size="middle">  
            <Checkbox.Group options={options} defaultValue={['Pear']} onChange={onChange} />
            </Space>
          </Col>
        </Row>
        <Row style={{padding:'5px'}}>
          <Col span={4}><h1>Radio单选框</h1></Col>
          <Col span={20}>
            <Space size="middle">  
              <Radio.Group name="radiogroup" defaultValue={1}>
                <Radio value={1}>A</Radio>
                <Radio value={2}>B</Radio>
                <Radio value={3}>C</Radio>
                <Radio value={4}>D</Radio>
              </Radio.Group>
            </Space>
          </Col>
        </Row>
        <Row style={{padding:'5px'}}>
          <Col span={4}><h1>Table表格</h1></Col>
          <Col span={20}>
            <Space size="middle">  
              <Table
                columns={columns1}
                dataSource={data}
                bordered
              />
            </Space>
          </Col>
        </Row>
        <Row style={{padding:'5px'}}>
          <Col span={4}><h1>图表</h1></Col>
          <Col span={20}>
            <Space size="middle">  
              <DualAxes {...config}/>
            </Space>
          </Col>
        </Row>
        
      </div>
    );
  }
}

function handleButtonClick(e) {
  message.info('Click on left button.');
  console.log('click left button', e);
}

function handleMenuClick(e) {
  message.info('Click on menu item.');
  console.log('click', e);
}

const menu = (
  <Menu onClick={handleMenuClick}>
    <Menu.Item key="1">
      1st menu item
    </Menu.Item>
    <Menu.Item key="2">
      2nd menu item
    </Menu.Item>
    <Menu.Item key="3" >
      3rd menu item
    </Menu.Item>
  </Menu>
);

const { Option } = Select;

function handleChange(value) {
  console.log(`selected ${value}`);
}

function onChange(e) {
  console.log(`checked = ${e.target.checked}`);
}

const options = [
  { label: 'Apple', value: 'Apple' },
  { label: 'Pear', value: 'Pear' },
  { label: 'Orange', value: 'Orange' },
];

const columns1 = [
  {
    title: 'Name',
    dataIndex: 'name',
    align:'center'
  },
  {
    title: 'Cash Assets',
    className: 'column-money',
    dataIndex: 'money',
    align:'center'
  },
  {
    title: 'Address',
    dataIndex: 'address',
    align:'center'
  },
];

const data = [
  {
    key: '1',
    name: 'John Brown',
    money: '￥300,000.00',
    address: 'New York No. 1 Lake Park',
  },
  {
    key: '2',
    name: 'Jim Green',
    money: '￥1,256,000.00',
    address: 'London No. 1 Lake Park',
  },
  {
    key: '3',
    name: 'Joe Black',
    money: '￥120,000.00',
    address: 'Sidney No. 1 Lake Park',
  },
];

const data1 = [
  {
    time: 'Jan',
    value: 55,
    count: 6.511,
  },
  {
    time: 'Feb',
    value: 65,
    count: 7.011,
  },{
    time: 'Mar',
    value: 65,
    count: 7.011,
  },{
    time: 'Apr',
    value: 63,
    count: 6.311
  },{
    time: 'May',
    value: 70,
    count: 7.011
  }
];
const config = {
  data: [data1, data1],
  xField: 'time',
  yField: ['value', 'count'],
  yAxis:{
    value:{
      tickInterval:10
    },
    count:{
      tickInterval:1
    }
  },
  meta: {
    value: {
      alias: 'Sum FTE',
      nice:true,
    },
    count: {
      alias: 'BandMix',
      min:6,
      max:10,
      type:'linear',
    },
    
  },
  geometryOptions: [
    {
      geometry: 'column',
      color: '#40A9FF',
      columnWidthRatio: 0.4,
      label: {
        position: 'middle',
      },
    },
    {
      geometry: 'line',
      smooth: false,
      color: '#A8071A',
      label: {
        position: 'middle',
      }
    },
  ],
  // 更改柱线交互，默认为 [{type: 'active-region'}]
  interactions: [
    {
      type: 'element-highlight',
    },
    {
      type: 'active-region',
    },
  ],
};

export default Sample;