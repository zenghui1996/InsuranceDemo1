import React, { useState } from 'react';
import {Table, Space } from 'antd';
import '../mystyle.less'
import {Input, Form, Button } from 'antd';
import OrganizationCascader from '../component/OrganizationCascader';
import { post } from '../utils/Request';


const SearchBar = (props) => {
  const onFinish = (values) => {
    props.onSearch(values);
  };

  return (
    <div style={{ width: '960px' }}>
      <Form
      name="search"
      layout="inline"
      onFinish={onFinish}
      >
      <OrganizationCascader />
      <Form.Item 
        label="所在年份" 
        rules={[
          {
            required: true,
            message: '请输入所在年份',
          },
        ]}
        name="belongingYear"
        >
        <Input  placeholder="2022" />
      </Form.Item>
      <Form.Item>
        <Space size="middle">
          <Button type="primary"  htmlType="submit">查询</Button>
        </Space>
      </Form.Item>
      </Form > 
    </div>
  );
};

const NewHireDate =() => {
  //按月份统计的数据
  const [data1, setData1] = useState([]);
  //按季度统计的数据
  const [data2, setData2] = useState([]);

  //通过查询发出请求
  const onSearch = (values) => {
    post('/newHire', values)
      .then((res) => {
        if (res.success) {
          setData1(res.data.listMonth);
          setData2(res.data.listQuarter);
        } else { 
          if (res.errMsg) {
            alert(res.errMsg);
          }
        }
      })
  };

  return (
    <>
      <SearchBar onSearch={onSearch}  />
      <br /> 
      {/* 按月份统计的表格   */}
      <Table
        columns={columns1}
        dataSource={data1}
        pagination={false}
        bordered
      />
      <br />
      <br />
      <br />
      {/* 按季度统计的表格   */}
      <Table style = {{width:'50%'}}
        columns={columns2}
        dataSource={data2}
        pagination={false}
        bordered
      />  
    </>
  ); 
}

//月份统计的表结构
const columns1 = [
  {
    title: 'New Hire(人数)',
    dataIndex: 'title',
    align:'center'
  },
  {
    title: 'Jan',
    dataIndex: 'jan',
    align:'center'
  },
  {
    title: 'Feb',
    dataIndex: 'feb',
    align:'center'
  },
  {
    title: 'Mar',
    dataIndex: 'mar',
    align:'center'
  },
  {
    title: 'Apr',
    dataIndex: 'apr',
    align:'center'
  },
  {
    title: 'May',
    dataIndex: 'may',
    align:'center'
  },
  {
    title: 'Jun',
    dataIndex: 'jun',
    align:'center'
  },
  {
    title: 'Jul',
    dataIndex: 'jul',
    align:'center'
  },
  {
    title: 'Aug',
    dataIndex: 'aug',
    align:'center'
  },
  {
    title: 'Sep',
    dataIndex: 'sep',
    align:'center'
  },
  {
    title: 'Oct',
    dataIndex: 'oct',
    align:'center'
  },
  {
    title: 'Nov',
    dataIndex: 'nov',
    align:'center'
  },
  {
    title: 'Dec',
    dataIndex: 'dec',
    align:'center'
  }
];

//季度统计的表结构
const columns2 = [
  {
    title: 'New Hire(人数)',
    dataIndex: 'title',
    width:'34%',
    align:'center'
  },
  {
    title: '1Q',
    dataIndex: 'quarter1',
    align:'center'
  },
  {
    title: '2Q',
    dataIndex: 'quarter2',
    align:'center'
  },
  {
    title: '3Q',
    dataIndex: 'quarter3',
    align:'center'
  },
  {
    title: '4Q',
    dataIndex: 'quarter4',
    align:'center'
  },
]



export default NewHireDate;