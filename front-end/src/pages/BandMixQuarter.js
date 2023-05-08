import { Button, Form, Input, Space,DatePicker} from 'antd';
import { DualAxes } from '@ant-design/plots';
import OrganizationCascader from '../component/OrganizationCascader';
import BandMixQuartlyTable from './BandMixQuartlyTable';
import { post } from '../utils/Request';
import React, { useState } from 'react';


const SearchBar = (props) => {
  const [form] = Form.useForm();

  const onFinish = (values) => {
    props.onSearch(values);
  };

  return (
    <Form
      form={form}
      name="search"
      layout="inline"
      onFinish={onFinish}
    >
      <OrganizationCascader />
      <Form.Item label="所在年份" name="yearMouth"
        rules={[
          {
              required: true,
              message: `请选择所在年份!`,
          },
      ]}>
      <DatePicker picker="year" style={{ width: 240 }} />
      </Form.Item>
      <Form.Item>
        <Space size="middle">
          <Button type="primary" htmlType="submit">查询</Button>
        </Space>
      </Form.Item>
    </Form >
  );
};


const BandMixQuarter = () => {
  const [data1, setData1] = useState([]);
  const [data2, setData2] = useState([]);

  const onSearch = (values) => {
    post('/getBandMixByQuarter', values)
      .then((res) => {
        if (res.success && res.data.length>0) {
          setData1(res.data);
          const obltable = [];
          obltable.push({
            quarterly: '1Q',
            SumFTE : parseInt(res.data[8].level1Q),
            BandMix: parseFloat(res.data[7].level1Q),
          },
          {
            quarterly: '2Q',
            SumFTE : parseInt(res.data[8].level2Q),
            BandMix: parseFloat(res.data[7].level2Q),
          },{
            quarterly: '3Q',
            SumFTE : parseInt(res.data[8].level3Q),
            BandMix: parseFloat(res.data[7].level3Q),
          },{
            quarterly: '4Q',
            SumFTE : parseInt(res.data[8].level4Q),
            BandMix: parseFloat(res.data[7].level4Q),
          });
          setData2(obltable);
        } else {
          if (res.checkList) {
            res.checkList.forEach(item => {
              alert(item);
            })
          }
          if (res.errMsg) {
            alert(res.errMsg);
          }
        }
      })
  };

  const config = {
    title:"BandMix Forecast (Quarterly)",
    data: [data2, data2],
    xField: 'quarterly',
    yField: ['SumFTE', 'BandMix'],
    autoFit: true,
    yAxis: {
      SumFTE: {
        tickInterval:5
      },
      BandMix: {
        tickInterval:0.1
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

    interactions: [
      {
        type: 'element-highlight',
      },
      {
        type: 'active-region',
      },
    ],
    };

  return (
  <><SearchBar onSearch={onSearch}/>
    <br />
    <div>
      <Form.Item>
        <BandMixQuartlyTable data={data1} />
      </Form.Item>
      <Form.Item>
        <DualAxes {...config}/>
      </Form.Item>
    </div></>
  );
};

export default BandMixQuarter;
