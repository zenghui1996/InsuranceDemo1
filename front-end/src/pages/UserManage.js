import React, { useState, useEffect } from 'react';
import { message, Input, Form, Space, Button } from 'antd';
import EditableTable from '../component/EditableTable';
import { get, post } from '../utils/Request';
import OrganizationCascader from '../component/OrganizationCascader';

const SearchBar = (props) => {
  const [form] = Form.useForm();

  const onFinish = (values) => {
    props.onSearch(values);
  }

  return (
    <Form
      form={form}
      name="search"
      layout="inline"
      onFinish={onFinish}
    >
      <OrganizationCascader/>
      <Form.Item label="用户ID" name="userId">
        <Input placeholder="请输入用户ID" />
      </Form.Item>
      <Form.Item>
        <Space size="middle">
          <Button type="primary" htmlType="submit">查询</Button>
        </Space>
      </Form.Item>
    </Form >
  );
};

const UserManage = () => {
  const [data, setData] = useState([]);
  const [editingKey, setEditingKey] = useState('');

  useEffect(() => {
    onSearch()
  }, [])

  const inColumns = [
    {
      title: '用户ID',
      dataIndex: 'userId',
      align: 'center',
      editable: true,
      inputType: 'disabled',
    },
    {
      title: '用户名',
      className: '用户名',
      dataIndex: 'userName',
      align: 'center',
      editable: true,
    },
    {
      title: '管理者Flag',
      dataIndex: 'adminnistratorFlagStr',
      align: 'center',
      editable: true,
      inputType: 'adminnistratorFlagStr',
    },
    {
      title: '组织code',
      dataIndex: 'organizationAuthorityName',
      align: 'center',
      editable: true,
      inputType: 'organizationAuthorityName',
    },
    {
      title: '员工NotesID',
      dataIndex: 'employeeNotesId',
      align: 'center',
      editable: true,
    },
  ];

  const onSearch = (values) => {
    if (editingKey !== '') {
      message.error('请先保存');
      return;
    }

    let url = ''
    if (values === undefined) {
      url = '/userManagementSearch?organizationCode=&userId='
    } else {
      let organization = ''
      let userId = ''
      if (values.organization && values.organization.length > 0) {
        organization = values.organization.toString().replace(/[^0-9]/ig, "")
      }
      if (values.userId) {
        userId = values.userId
      }
      url = `/userManagementSearch?organizationCode=${organization}&userId=${userId}`
    }

    get(url).then(res => {
      if (res.success) {
        for (var i = 0; i < res.data.length; i++) {
          res.data[i].key = i;
        }
        setData(res.data);
      } else {
        if (res.checkList) {
          res.checkList.forEach(item => {
            alert(item);
          });
        }
        if (res.errMsg) {
          alert(res.errMsg);
        }
      }
    })
  }

  const onSave = (key, row) => {
    let organization = row.organizationAuthorityName[0]
      + row.organizationAuthorityName[1] + row.organizationAuthorityName[2];
    organization = organization.toString().replace(/[^0-9]/ig, "")

    row.organizationAuthorityName = "";
    row.organizationAuthorityCode = organization;
    row.adminnistratorFlag = row.adminnistratorFlagStr

    post('/userManagementSave', row).then(res => {
      if (res.success) {
        const newData = [...data];
        const index = newData.findIndex((item) => key === item.key);
        res.data[0].key = index;
        newData[index] = res.data[0];

        setData(newData);
      } else {
        if (res.checkList) {
          res.checkList.forEach(item => {
            alert(item);
          });
        }
        if (res.errMsg) {
          alert(res.errMsg);
        }
      }
    })

    setEditingKey('');
  };

  const onDel = (key) => {
    const newData = [...data];
    const index = newData.findIndex((item) => key === item.key);
    let userId = newData[index].userId;
    newData.splice(index, 1);

    let url = `/userManagementDelete?userId=${userId}`

    get(url).then(res => {
      if (res.success) {
        alert(res.data);
        setData(newData);
      } else {
        if (res.checkList) {
          res.checkList.forEach(item => {
            alert(item);
          });
        }
        if (res.errMsg) {
          alert(res.errMsg);
        }
      }
    })

    setEditingKey('');
  };

  const onAdd = () => {
    const newData = [];
    newData.push({
      key: 'new',
      userId: '',
      userName: '',
      adminnistratorFlag: '',
      organizationAuthorityCode: '',
      organizationAuthorityName: '',
      employeeNotesId: '',
    });
    setData(newData);
    setEditingKey('new')
  };

  const onEdit = (key) => {
    setEditingKey(key);
  };

  const onCancel = (key) => {
    setEditingKey('');
    if (key === 'new') {
      const newData = [];
      setData(newData);
    }
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

export default UserManage;