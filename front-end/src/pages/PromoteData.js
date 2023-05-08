import React, { useState } from 'react';
import { Table, Space } from 'antd';
import '../mystyle.less'
import { Input, Form, Button } from 'antd';
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

      <Form.Item 
        label="所在年份" 
        rules={[
          {
            required: true,
            message: '请输入所在年份',
          },
          {
            len:4,
            message: "输入长度必须是4位",    
          },
        ]}
        name="belongingYear"
        >
        <Input  placeholder="2022" />
      </Form.Item>
      <Form.Item>
        <Space size="middle">
          <Button type="primary" htmlType="submit" name="searchBtn" >查询</Button>
        </Space>
      </Form.Item>
      </Form > 
    </div>
  );
};

const PromoteData =() => {
  const [data1, setData1] = useState([]);
  const [data2, setData2] = useState([]);

  const onSearch = (values) => {
    post('/promotion', values)
      .then((res) => {
        if (res.success) {
          setData1(res.data.promotionDtoMonthList);
          setData2(res.data.promotionDtoQuarterList);
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
      <Table
        columns={columns1}
        dataSource={data1}
        pagination={false}
        size="small"
        bordered
      />
      <br />
      <br />
      <Table style = {{width:'50%'}}
        columns={columns2}
        dataSource={data2}
        pagination={false}
        size="small"
        bordered
      />  
    </>
  ); 
}


const columns1 = [
  {
    title: 'Promotion detail',
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

const columns2 = [
  {
    title: 'Promotion detail',
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

export default PromoteData;
