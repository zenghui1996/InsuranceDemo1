import React from 'react';
import { Table, Popconfirm, Form, Space, Button, message } from 'antd';
import EditableCell from '../component/EditableCell';

const EditableTable = (props) => {
    const [form] = Form.useForm();
    const { data, editingKey, inColumns, isEditable } = props;

    const isEditing = (record) => record.key === editingKey;

    const save = async (key) => {
        try {
            const row = await form.validateFields();
            props.onSave(key, row);
        } catch (errInfo) {
            console.log('Validate Failed:', errInfo);
        }
    };

    const add = () => {
        if (editingKey !== '') {
            message.error('请先保存');
            return;
        }
        props.onAdd();
        form.resetFields();
    };

    const del = (key) => {
        props.onDel(key);
    };

    const edit = (record) => {
        let name;
        if (record.organizationAuthorityCode) {
            if (record.organizationAuthorityCode.length === 3) {
                name = [record.organizationAuthorityCode];
            }
            if (record.organizationAuthorityCode.length === 6) {
                name = [record.organizationAuthorityCode.substring(0, 3),
                record.organizationAuthorityCode.substring(3, 6)];
            }
            if (record.organizationAuthorityCode.length === 9) {
                name = [record.organizationAuthorityCode.substring(0, 3),
                record.organizationAuthorityCode.substring(3, 6),
                record.organizationAuthorityCode.substring(6, 9)];
            }
        }
        form.setFieldsValue({
            ...record,
            organizationAuthorityName: name,
            adminnistratorFlagStr: record.adminnistratorFlag
        });

        props.onEdit(record.key);
    };

    const cancel = () => {
        const key = editingKey;
        props.onCancel(key);
    };

    const columns = !isEditable ? inColumns : [
        ...inColumns,
        {
            title: '操作',
            dataIndex: 'operation',
            align: 'center',
            render: (_, record) => {
                const editable = isEditing(record);
                return editable ? (
                    <Space size="middle">
                        <Button type="primary" size="middle" onClick={() => save(record.key)}>
                            保存
                        </Button>
                        <Popconfirm title="确定要取消吗?" okText="是" cancelText="否" onConfirm={cancel}>
                            <Button type="primary" size="middle" danger>
                                取消
                            </Button>
                        </Popconfirm>
                    </Space>
                ) : (
                    <Space size="middle">
                        <Button type="primary" size="middle" disabled={editingKey !== ''} onClick={() => edit(record)}>
                            编辑
                        </Button>
                        <Popconfirm title="确定要删除吗?" okText="是" cancelText="否" disabled={editingKey !== ''} onConfirm={() => del(record.key)}>
                            <Button type="primary" size="middle" danger disabled={editingKey !== ''} >
                                削除
                            </Button>
                        </Popconfirm>
                    </Space>
                );
            },
        }
    ];

    const merged = (col) => {
        if (!col.editable) {
            return col;
        }

        return {
            ...col,
            onCell: (record) => ({
                record,
                inputType: (col.inputType === 'disabled' && record.key === 'new') ? '' : col.inputType,
                dataIndex: col.dataIndex,
                title: col.title,
                editing: isEditing(record),
            }),
        };
    };

    const mergedColumns = columns.map((col) => {
        if ('children' in col) {
            const children = col.children.map((children) => {
                return merged(children);
            })

            col.children = children;
        }
        return merged(col);
    });

    return (
        <>
            <Button type="primary" htmlType="button" onClick={add}>添加</Button>
            <Form form={form} component={false} name="data">
                <Table
                    components={{
                        body: {
                            cell: EditableCell,
                        },
                    }}
                    bordered
                    dataSource={data}
                    columns={mergedColumns}
                    rowClassName="editable-row"
                    pagination={{
                        onChange: cancel,
                    }} 
                    scroll={{x:'100%'}}
                    />
            </Form></>
    );
};
export default EditableTable;