import React, { useState } from 'react';
import { Table, Space, Row, Col, Select, Form, Button, Input} from 'antd';
// import moment from 'moment';
import '../mystyle.less'
import { post } from '../utils/Request';


const { Option } = Select;

const SearchBar = (props) => {
  const onFinish = (values) => {
    props.onSearch(values);
  };

  return (
    <div>
      <header>
        <h1>各Ratio
          <br />
          {/* Promote率/ Promote率（无associate）/Associate采用率/中途采用率/离职率 */}
        </h1>
      </header>
      <Row style={{ padding: '5px' }}>
        <Col span={15} offset={6}>
          <Space size="middle">
            <Form
              name="search"
              layout="inline"
              onFinish={onFinish}

              labelCol={{
                span: 6,
              }}
              wrapperCol={{
                span: 14,
              }}
              initialValues={{
                remember: true,
              }}
            >

              <Form.Item label="所在年份"
                name="belongingYear"
                rules={[
                  {
                    required: true,
                    message: '请输入所在年份!',
                  },
                  {
                    len:4,
                    message: "输入长度必须是4位",    
                  },
                ]}>
                <Input  style={{ width: 250 }} placeholder="2022"/>
                {/* <Select style={{ width: 250 }} placeholder="Select belongingYear">
                  <Option value={moment().year()}>{moment().year()}</Option>
                  <Option value={moment().subtract(1,'year').year()}>{moment().subtract(1,'year').year()}</Option>
                </Select> */}
              </Form.Item>

              <Form.Item
                wrapperCol={{
                  offset: 5,
                  span: 4,
                }}
              >
                <Button style={{ width: 120 }} type="primary" size="middle" htmlType="submit">查询</Button>
              </Form.Item>

            </Form>
          </Space>
        </Col>
      </Row>

    </div>

  );
};

const RatioList = () => {
  //返回的数据
  const [data1, setData1] = useState([]);

  //通过查询发出请求
  const onSearch = (values) => {
    post('/ratioStatics', values)
      .then((res) => {
        if (res.success) {
          setData1(res.data.ratioDto);
        } else {
          if (res.errMsg) {
            alert(res.errMsg);
          }
        }
      })
  };

  return (
    <>
      <SearchBar onSearch={onSearch} />
      <br />
      {/* 计算结果的表格 */}
      <Table
        columns={columns1}
        dataSource={data1}
        pagination={false}
        bordered
      />
    </>
  );
}

//计算结果的表结构
const columns1 = [
  {
    title: '比例类型',
    dataIndex: 'promotionDetail',
    align: 'center'
  },
  {
    title: 'Promotion ratio(All)',
    dataIndex: 'promotAllRatio',
    align: 'center'
  },
  {
    title: 'Promotion ratio(WithoutGH)',
    dataIndex: 'promotWithoutGhRatio',
    align: 'center'
  },
  {
    title: 'GH Hire ratio',
    dataIndex: 'ghHireRatio',
    align: 'center'
  },
  {
    title: 'New Hire ratio',
    dataIndex: 'newHireRatio',
    align: 'center'
  },
  {
    title: 'Resign ratio',
    dataIndex: 'resignRatio',
    align: 'center'
  }

];


export default RatioList;