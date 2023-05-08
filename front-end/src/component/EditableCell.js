import React from 'react';
import { Input, InputNumber, Form, Cascader, Select } from 'antd';

const EditableCell = ({
  editing,
  dataIndex,
  title,
  inputType,
  record,
  index,
  children,
  ...restProps
}) => {
  const getInputNode = () => {
    switch (inputType) {
      case "number": return <InputNumber />;
      case "disabled": return <Input disabled="true"/>;
      case "adminnistratorFlagStr":
        return <Select placeholder="请选择"
          style={{ width: 120 }}
          options={[
            {
              value: '1',
              label: '普通用户',
            },
            {
              value: '2',
              label: 'Acount管理者',
            },
            {
              value: '3',
              label: '超级管理者',
            },
          ]}
        />;
      case "organizationAuthorityName":
        return <Cascader
          options={JSON.parse(localStorage.getItem("options"))}
          placeholder="请选择"
          style={{ width: 240 }} />;
      default: return <Input />;
    }
  };

  return (
    <td {...restProps}>
      {editing ? (
        <Form.Item
          name={dataIndex}
          style={{
            margin: 0,
          }}
          rules={[
            {
              required: true,
              message: `Please Input ${title}!`,
            },
          ]}
        >
          {getInputNode()}
        </Form.Item>
      ) : (
        children
      )}
    </td>
  );
};
export default EditableCell;