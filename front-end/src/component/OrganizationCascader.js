import React, { useState, useEffect } from 'react';
import { Cascader, Form } from 'antd';
import { get } from '../utils/Request';
import { getMsg } from '../utils/Message';

const OrganizationCascader = (props) => {
    const [options, setOptions] = useState([]);
    const { required } = props;

    useEffect(() => {
        if (options.length !== 0) {
            return;
        }

        const sessionOptions = localStorage.getItem("options")
        if (sessionOptions != null) {
            setOptions(JSON.parse(sessionOptions));
            return;
        }

        get('/organizationCode')
            .then((res) => {
                if (res.success) {
                    //树形结构保存
                    var tree = trans_tree(res.data)
                    setOptions(tree);
                    localStorage.setItem("options", JSON.stringify(tree));
                } else {
                    if (res.checkList) {
                        res.checkList.forEach(item => {
                            alert(item);
                        });
                    }
                    if (res.errMsg) {
                        alert(getMsg(res.errMsg));
                    }
                }
            })
    }, [options.length]);

    const trans_tree = (jsonData) => {
        //temp为临时对象，将json数据按照id值排序.
        var result = [], temp = {}, len = jsonData.length

        for (var i = 0; i < len; i++) {
            // 以id作为索引存储元素，可以无需遍历直接快速定位元素
            temp[jsonData[i]['value']] = jsonData[i]
        }
        for (var j = 0; j < len; j++) {
            var list = jsonData[j]
            // 临时变量里面的当前元素的父元素，即pid的值，与找对应id值
            var sonlist = temp[list['pid']]
            // 如果存在父元素，即如果有pid属性
            if (sonlist) {
                // 如果父元素没有children键
                if (!sonlist['children']) {
                    // 设上父元素的children键
                    sonlist['children'] = []
                }
                // 给父元素加上当前元素作为子元素
                sonlist['children'].push(list)
            }
            // 不存在父元素，意味着当前元素是一级元素
            else {
                result.push(list);
            }
        }
        return result;
    }

    return (
        <>
            {required === 'true' ?
                <Form.Item label="组织部门" name="organization"
                    rules={[
                        {
                            required: true,
                            message: `请选择组织部门!`,
                        },
                    ]}
                >
                    <Cascader options={options} placeholder="请选择" style={{ width: 240 }} />
                </Form.Item>
                :
                <Form.Item label="组织部门" name="organization" >
                    <Cascader options={options} placeholder="请选择" style={{ width: 240 }} />
                </Form.Item>
            }
        </>
    );
};
export default OrganizationCascader;