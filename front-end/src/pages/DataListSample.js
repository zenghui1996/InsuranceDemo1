import React, { useState } from 'react';
import { message, Input, Form, Space, Button } from 'antd';
import EditableTable from '../component/EditableTable';
import OrganizationCascader from '../component/OrganizationCascader';
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
      <Form.Item label="检索条件1" name="condition1">
        <Input placeholder="input select1" />
      </Form.Item>
      <Form.Item label="检索条件2" name="condition2">
        <Input placeholder="input select2" />
      </Form.Item>
      <Form.Item>
        <Space size="middle">
          <Button type="primary" htmlType="submit">查询</Button>
        </Space>
      </Form.Item>
    </Form >
  );
};

const DataListSample = () => {
  const [data, setData] = useState([]);
  const [editingKey, setEditingKey] = useState('');

  const inColumns = [
    {
      title: 'name',
      dataIndex: 'name',
      width: '25%',
      editable: true,
    },
    {
      title: '2022',
      children: [
        {
          title: 'age',
          dataIndex: 'age',
          width: '15%',
          editable: true,
        },
        {
          title: 'address',
          dataIndex: 'address',
          width: '40%',
          editable: true,
        }
      ]
    }
  ];

  const onSearch = (values) => {
    if (editingKey !== '') {
      message.error('请先保存');
      return;
    }
    post('/dataList', values)
      .then((res) => {
        if (res.success) {
          setData(res.data);
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

  const onSave = (key, row) => {
    const newData = [...data];
    const index = newData.findIndex((item) => key === item.key);
    const item = newData[index];
    if (key === 'new') {
      item.key = data.length
    }
    newData.splice(index, 1, { ...item, ...row });
    setData(newData);
    setEditingKey('');
  };

  const onDel = (key) => {
    const newData = [...data];
    const index = newData.findIndex((item) => key === item.key);
    newData.splice(index, 1);
    setData(newData);
    setEditingKey('');
  };

  const onAdd = () => {
    const newData = [];
    newData.push({
      key: 'new',
      name: '',
      age: '',
      address: '',
    });
    setData(newData);
    setEditingKey('new');
  };

  const onEdit = (key) => {
    setEditingKey(key);
  };

  const onCancel = () => {
    setEditingKey('');
  };

  return (
    <><SearchBar onSearch={onSearch} />
      <br />
      <EditableTable data={data} editingKey={editingKey} inColumns={inColumns}
        isEditable={true}
        onSave={onSave} onDel={onDel} onAdd={onAdd}
        onEdit={onEdit} onCancel={onCancel} /></>
  );
};
export default DataListSample;