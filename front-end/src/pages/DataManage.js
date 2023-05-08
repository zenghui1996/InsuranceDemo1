import React, { useState } from 'react';
import * as XLSX from 'xlsx';
import { Button, Upload, Radio, DatePicker, Form, message } from 'antd';
import OrganizationCascader from '../component/OrganizationCascader';
import { post } from '../utils/Request';
import { UploadOutlined } from '@ant-design/icons';

const DataManage = () => {
    const [form] = Form.useForm();
    const [fileList, setFileList] = useState([]);

    const dateFormat = (date) => {
        if (date) {
            var Y = date.getFullYear();
            var M = date.getMonth() + 1 < 10 ? '0' + (date.getMonth() + 1) : date.getMonth() + 1;
            var D = date.getDate() < 10 ? '0' + date.getDate() : date.getDate();

            return M + '/' + D + '/' + Y;
        } else {
            return '';
        }
    }

    const onImportExcel = (organization, belongingYear, importPattern) => {
        if (fileList.length === 0) {
            return message.error('请上传数据')
        } else {
            let towerCode = ''
            if (organization && organization.length > 0) {
                towerCode = organization.toString().replace(/[^0-9]/ig, "")
            }
            post('/dataManage', {
                towerCode: towerCode,
                csvDataDtoList: fileList,
                belongingYear: belongingYear,
                importPattern: importPattern
            }).then((res) => {
                if (res.success) {
                    alert(res.data);
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
    };

    const beforeUpload = async (file) => {
        let wb = ''
        let res = await readFile(file, '')
        let data = res.target.result;
        wb = XLSX.read(data, {
            type: "binary",
            cellDates: true
        });
        const arr = XLSX.utils.sheet_to_json(wb.Sheets[wb.SheetNames[0]]);
        for (let i = 0; i < arr.length; i++) {
            arr[i]['End Date'] = dateFormat(arr[i]['End Date']);

            if (!arr[i].hasOwnProperty('Number')) {
                message.error(`第 ${i + 1} 条数据的 Number 为空`);
                return false
            }
            if (!arr[i].hasOwnProperty('SN')) {
                message.error(`第 ${i + 1} 条数据的 SN 为空`);
                return false
            }
            if (!arr[i].hasOwnProperty('Notes ID')) {
                message.error(`第 ${i + 1} 条数据的 Notes ID 为空`);
                return false
            }
            if (!arr[i].hasOwnProperty('Band')) {
                message.error(`第 ${i + 1} 条数据的 Band 为空`);
                return false
            }
            if (!arr[i].hasOwnProperty('Location')) {
                message.error(`第 ${i + 1} 条数据的 Location 为空`);
                return false
            }
            if (!arr[i].hasOwnProperty('M/F')) {
                message.error(`第 ${i + 1} 条数据的 M/F 为空`);
                return false
            }
            if (!arr[i].hasOwnProperty('Current PeM')) {
                message.error(`第 ${i + 1} 条数据的 Current PeM 为空`);
                return false
            }
            for (let [index, data] of Object.entries(arr[i])) {
                let result = isOffendsRelus(data.toString(), index)
                if (!result.state) {
                    message.error(`第 ${i + 1} 条数据的 ${result.type} 格式不正确`);
                    return result.state
                }
            }
        }
        setFileList(arr);

        return false;
    }

    // 判断数据是否符合规则
    const isOffendsRelus = (data, type) => {
        let result = {
            type: type,
            state: true,
            message: ''
        }
        let pattern = /6(A|B|G)|(7|8|9)(A|B)|-/
        switch (type) {
            case 'Number':
                if (data.toString().split("").length > 128 || data.toString().split("") < 0) {
                    result.state = false
                    return result
                }
                return result
            case 'SN':
                if (data.toString().split("").length > 9 || data.toString().split("") < 0) {
                    result.state = false
                    return result
                }
                return result
            case 'Notes ID':
                return result
            case 'Band':
                return result
            case 'Location':
                return result
            case 'M/F':
                return result
            case 'Current PeM':
                return result
            case 'Current Project':
                return result
            case 'End Date':
                return result
            case 'Next Project':
                return result
            case 'currentYearJan':
                if (!pattern.test(data.toString())) {
                    result.state = false
                    return result
                }
                return result
            case 'currentYearFeb':
                return result
            case 'currentYearMar':
                return result
            case 'currentYearApr':
                return result
            case 'currentYearMay':
                return result
            case 'currentYearJun':
                return result
            case 'currentYearJul':
                return result
            case 'currentYearAug':
                return result
            case 'currentYearSep':
                return result
            case 'currentYearOct':
                return result
            case 'currentYearNov':
                return result
            case 'currentYearDec':
                return result
            case 'nextYearJan':
                return result
            case 'Comment':
                return result
            default:
                return result
        }
    }

    const readFile = (file, type) => {
        return new Promise((resolve, reject) => {
            let reader = new FileReader(); // 专门用来读取文件

            reader.onload = (e) => {
                resolve(e);
            };
            reader.onerror = (e) => {
                reject(e);
            };
            if (type === "text") {
                reader.readAsText(file);
            } else if (type === "binary") {
                reader.readAsBinaryString(file);
            } else {
                reader.readAsArrayBuffer(file);
            }
        });
    }

    const onFinish = (values) => {
        onImportExcel(values.organization, values.condition2.format('YYYY'), values.condition3);
    };

    return (
        <Form
            form={form}
            name="search"
            layout="horizontal"
            onFinish={onFinish}
        >
            <OrganizationCascader required='true' />
            <Form.Item label='文件导入' name="condition1"
                rules={[
                    {
                        required: true,
                        message: `请选择文件!`,
                    },
                ]}>
                <Upload beforeUpload={beforeUpload} >
                    <Button icon={<UploadOutlined />}>选择文件</Button>
                </Upload>
            </Form.Item>
            <Form.Item label='所在年份' name="condition2"
                rules={[
                    {
                        required: true,
                        message: `请选择所在年份!`,
                    },
                ]}>
                <DatePicker picker="year" style={{ width: 240 }} />
            </Form.Item>
            <Form.Item label='导入方式' name="condition3"
                rules={[
                    {
                        required: true,
                        message: `请选择导入方式!`,
                    },
                ]}>
                <Radio.Group>
                    <Radio value={1}>差分导入</Radio>
                    <Radio value={2}>全量导入</Radio>
                </Radio.Group>
            </Form.Item>
            <Form.Item>
                <Button type="primary" htmlType="submit">导入</Button>
            </Form.Item>
        </Form >
    );
};

export default DataManage;