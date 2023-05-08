import {Table} from 'antd';
import React from 'react';
import EditableCell from '../component/EditableCell';

const BandMixMonthlyTable = (props) => {

const columns = [
  {
    title: 'Band Level',
    dataIndex: 'bandLevel',
    align:'center'
  },{
    title: 'Jan',
    dataIndex: 'dataJan',
    align:'center'
  },{
    title: 'Feb',
    dataIndex: 'dataFeb',
    align:'center'
  },{
    title: 'Mar',
    dataIndex: 'dataMar',
    align:'center'
  },{
    title: 'Apr',
    dataIndex: 'dataApr',
    align:'center'
  },{
    title: 'May',
    dataIndex: 'dataMay',
    align:'center'
  },{
    title: 'Jun',
    dataIndex: 'dataJun',
    align:'center'
  },{
    title: 'Jul',
    dataIndex: 'dataJul',
    align:'center'
  },{
    title: 'Aug',
    dataIndex: 'dataAug',
    align:'center'
  },{
    title: 'Sep',
    dataIndex: 'dataSep',
    align:'center'
  },{
    title: 'Oct',
    dataIndex: 'dataOct',
    align:'center'
  },{
    title: 'Nov',
    dataIndex: 'dataNov',
    align:'center'
  },{
    title: 'Dec',
    dataIndex: 'dataDec',
    align:'center'
  },
];

  const { data } = props;

  return (
    <>
    <Table 
      components={{
        body: {
            cell: EditableCell,
        },
      }}
      rowKey="bandLevel" 
      columns={columns} 
      dataSource={data}
      rowClassName="bandMixMonthlyTable-row"
      pagination />
    </>
  );
};

export default BandMixMonthlyTable;
