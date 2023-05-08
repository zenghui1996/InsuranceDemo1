import React, { useState } from 'react';
import { message, Input, Form, Space, Button, Row, Col } from 'antd';
import EditableTable from '../component/EditableTable';
import OrganizationCascader from '../component/OrganizationCascader';
import { put, del, post} from '../utils/Request';


const SearchBar = (props) => {
  const [form] = Form.useForm();

  const onFinish = (values) => {
    props.onSearch(values);
  };

  const div2 = {
    float: "right",
  };

  return (
    <div>
      <Form
        form={form}
        name="search"
        layout="inline"
        onFinish={onFinish}
      >
        <Col >
          <OrganizationCascader />
          <br></br>
          <Row>
            <Form.Item label="Current PeM" name="searchPemNotesId">
              <Input placeholder="请输入Pem的NotesId：" />
            </Form.Item>
            <Form.Item label="所在年份" name="searchBelongYear">
              <Input placeholder="请输入所在年份：" />
            </Form.Item>
            <Form.Item>
              <Space size="middle" >
                {/* <div style={div1}> */}
                  <Button type="primary" style={div2} htmlType="submit">查询</Button>
                {/* </div> */}
              </Space>
            </Form.Item>
          </Row>
        </Col>
      </Form >
    </div>
  );
}

const DataList = () => {
  const [data, setData] = useState([]);
  const [searchBelongYear, setSearchBelongYear] = useState('');
  const [organizationCode, setOrganizationCode] = useState('');
  const [currentPeM, setCurrentPeM] = useState('');
  const [editingKey, setEditingKey] = useState('');
  const [visibility, setVisibility] = useState('none');
  const interval = String.fromCharCode(160);

  const inColumns = [
    {
      title: 'No',
      dataIndex: 'key',
      align: 'center',
      editable: false,
    },
    {
      title: interval.repeat(8)+'SN' +interval.repeat(8),
      dataIndex: 'employeeSn',
      align: 'center',
      editable: true,
    },
    {
      title: interval.repeat(7)+'Notes'+interval+'ID'+interval.repeat(7),
      dataIndex: 'employeeNotesId',
      align: 'center',
      editable: true,
    },
    {
      title: interval.repeat(3)+'Band'+interval.repeat(3),
      dataIndex: 'level',
      align: 'center',
      editable: true,
    },
    {
      title: interval.repeat(6)+'Location'+interval.repeat(6),
      dataIndex: 'location',
      align: 'center',
      editable: true,
    },
    {
      title: interval.repeat(3)+'M/F'+interval.repeat(3),
      dataIndex: 'sex',
      align: 'center',
      editable: true,
    },
    {
      title: interval.repeat(4)+'Current'+interval+'PeM'+interval.repeat(4),
      dataIndex: 'peopleManagerNotesId',
      align: 'center',
      editable: true,
    },
    {
      title: interval.repeat(5)+'Current'+interval+'Project'+interval.repeat(5),
      dataIndex: 'currentProject',
      align: 'center',
      editable: true,
    },
    {
      title: interval.repeat(3)+'End'+interval+'Date'+interval.repeat(3),
      dataIndex: 'currentProjectEnddate',
      align: 'center',
      editable: true,
    },
    {
      title: searchBelongYear,
      children: [
        {
          title: interval.repeat(3)+'Jan'+interval.repeat(3),
          dataIndex: 'januaryLevel',
          align: 'center',
          editable: true,
        },
        {
          title: interval.repeat(3)+'Feb'+interval.repeat(3),
          dataIndex: 'februaryLevel',
          align: 'center',
          editable: true,
        },
        {
          title: interval.repeat(3)+'Mar'+interval.repeat(3),
          dataIndex: 'marchLevel',
          align: 'center',
          editable: true,
        },
        {
          title: interval.repeat(3)+'Apr'+interval.repeat(3),
          dataIndex: 'aprilLevel',
          align: 'center',
          editable: true,
        },
        {
          title: interval.repeat(3)+'May'+interval.repeat(3),
          dataIndex: 'mayLevel',
          align: 'center',
          editable: true,
        },
        {
          title: interval.repeat(3)+'Jun'+interval.repeat(3),
          dataIndex: 'juneLevel',
          align: 'center',
          editable: true,
        },
        {
          title: interval.repeat(3)+'Jul'+interval.repeat(3),
          dataIndex: 'julyLevel',
          align: 'center',
          editable: true,
        },
        {
          title: interval.repeat(3)+'Aug'+interval.repeat(3),
          dataIndex: 'augustLevel',
          align: 'center',
          editable: true,
        },
        {
          title: interval.repeat(3)+'Sep'+interval.repeat(3),
          dataIndex: 'septemberLevel',
          align: 'center',
          editable: true,
        },
        {
          title: interval.repeat(3)+'Oct'+interval.repeat(3),
          dataIndex: 'octoberLevel',
          align: 'center',
          editable: true,
        },
        {
          title: interval.repeat(3)+'Nov'+interval.repeat(3),
          dataIndex: 'novemberLevel',
          align: 'center',
          editable: true,
        },
        {
          title: interval.repeat(3)+'Dec'+interval.repeat(3),
          dataIndex: 'decemberLevel',
          align: 'center',
          editable: true,
        }
      ]
    },
    {
      title: parseInt(searchBelongYear)+1,
      align: 'center',
      children: [
        {
          title: interval.repeat(3)+'Jan'+interval.repeat(3),
          dataIndex: 'nextJanuaryLevel',
          align: 'center',
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

  post('/dataList',values) 
  .then((res) => {
      if (res.success) {
          setVisibility('block');
          setData(res.data);
          setSearchBelongYear(res.data[0].searchBelongYear);
          setOrganizationCode(res.data[0].searchBelongTowerCode);
          setCurrentPeM(res.data[0].searchPemNotesId);
        
      } else {
        setVisibility('none');
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
}

  const onSave = (key, row) => {
    const newData = [...data];
    const index = newData.findIndex((item) => key === item.key);
    const item = newData[index];
    if (key === 'new') {
      item.key = data.length
    }
    newData.splice(index, 1, { ...item, ...row });
  put('/employeesave/', newData[index])
  .then((res) => {
    if (res.success) {
      // setData(res.data);
      setData(newData);
      setEditingKey('');
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

  const onDel = (key) => {
    const newData = [...data];
    const index = newData.findIndex((item) => key === item.key);

    del('/employeedelete/', newData[index])
    .then((res) => {
      if (res.success) {
        newData.splice(index, 1);
        setData(newData);
        setEditingKey('');
        alert("员工数据删除成功！")
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

  const onAdd = () => {
    const newData = [];
    newData.push({
      key: 'new',
      searchBelongYear: searchBelongYear,
      searchBelongTowerCode: organizationCode,
      searchPemNotesId: currentPeM,
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

      <div style={{ display: visibility }}>
        <EditableTable data={data} editingKey={editingKey} inColumns={inColumns}
          rowKey={item => item.DataList.inColumns.title}
          isEditable={true} 
          onSave={onSave} onDel={onDel} onAdd={onAdd}
          onEdit={onEdit} onCancel={onCancel} />
      </div>
    </>

  );
};

export default DataList;