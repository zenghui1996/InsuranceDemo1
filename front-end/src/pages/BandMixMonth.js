import React, { useState } from 'react';
import { Button, Form, Space, DatePicker } from 'antd';
import { DualAxes } from '@ant-design/plots';
import OrganizationCascader from '../component/OrganizationCascader';
import BandMixMonthlyTable from './BandMixMonthlyTable';
import { post } from '../utils/Request';


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

const BandMixMonth = () => {
  const [data1, setData1] = useState([]);
  const [data2, setData2] = useState([]);

  const onSearch = (values) => {
    post('/getBandmixMonthly', values)
      .then((res) => {
        if (res.success) {
          setData1(res.data);
          const obltable = [];
          obltable.push({
            time: 'Jan',
            SumFTE: parseInt(res.data[8].dataJan),
            BandMix: parseFloat(res.data[7].dataJan),
          },
            {
              time: 'Feb',
              SumFTE: parseInt(res.data[8].dataFeb),
              BandMix: parseFloat(res.data[7].dataFeb),
            }, {
            time: 'Mar',
            SumFTE: parseInt(res.data[8].dataMar),
            BandMix: parseFloat(res.data[7].dataMar),
          }, {
            time: 'Apr',
            SumFTE: parseInt(res.data[8].dataApr),
            BandMix: parseFloat(res.data[7].dataApr),
          }, {
            time: 'May',
            SumFTE: parseInt(res.data[8].dataMay),
            BandMix: parseFloat(res.data[7].dataMay),
          }, {
            time: 'Jun',
            SumFTE: parseInt(res.data[8].dataJun),
            BandMix: parseFloat(res.data[7].dataJun),
          }, {
            time: 'Jul',
            SumFTE: parseInt(res.data[8].dataJul),
            BandMix: parseFloat(res.data[7].dataJul),
          }, {
            time: 'Aug',
            SumFTE: parseInt(res.data[8].dataAug),
            BandMix: parseFloat(res.data[7].dataAug),
          }, {
            time: 'Sep',
            SumFTE: parseInt(res.data[8].dataSep),
            BandMix: parseFloat(res.data[7].dataSep),
          }, {
            time: 'Oct',
            SumFTE: parseInt(res.data[8].dataOct),
            BandMix: parseFloat(res.data[7].dataOct),
          }, {
            time: 'Nov',
            SumFTE: parseInt(res.data[8].dataNov),
            BandMix: parseFloat(res.data[7].dataNov),
          }, {
            time: 'Dec',
            SumFTE: parseInt(res.data[8].dataDec),
            BandMix: parseFloat(res.data[7].dataDec),
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
    title: "BandMix Forecast (Monthly)",
    data: [data2, data2],
    xField: 'time',
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


  return (
    <><SearchBar onSearch={onSearch} />
      <br />
      <div>
        <Form.Item>
          <BandMixMonthlyTable data={data1} />
        </Form.Item>
        <Form.Item>
          <DualAxes {...config} />
        </Form.Item>
      </div></>
  );
};

export default BandMixMonth;