import { Table } from 'antd';
import React from 'react';

const BandMixQuartlyTable = (props) => {

const columns = [
  {
    title: 'Band Level',
    dataIndex: 'bandLevel',
    align:'center'
  },{
    title: 'Q1',
    dataIndex: 'level1Q',
    align:'center'
  },{
    title: 'Q2',
    dataIndex: 'level2Q',
    align:'center'
  },{
    title: 'Q3',
    dataIndex: 'level3Q',
    align:'center'
  },{
    title: 'Q4',
    dataIndex: 'level4Q',
    align:'center'
  }
];

  const { data } = props;
  return (
    <>
      <Table rowKey="bandLevel" columns={columns} dataSource={data} />
    </>
  )
};

export default BandMixQuartlyTable;
