import React from 'react'
import { useNavigate } from "react-router-dom";
import { Button, Form, Input, Card, Row, Col } from 'antd';
import { LockOutlined } from '@ant-design/icons';
import { post } from '../utils/Request';

export default function ResetPassword() {

  const navigate = useNavigate()

  function onFinish(values) {
    values.userId = localStorage.getItem('user_id')
    post('/user/resetPwd', values)
      .then((res) => {
        if (res.success) {
          alert("密码重置成功,请重新登录!");
          localStorage.clear()
          navigate('/Login')
        } else {
          if (res.errMsg) {
            alert(res.errMsg);
          }
        }
      })
  }

  return (
    <>
      <Row gutter={[16, 80]}>
        <Col span={8}></Col>
        <Col span={8}></Col>
        <Col span={8}></Col>
        <Col span={8}></Col>
        <Col span={8}>
          <Card title="密码重置">
            <Form
              name="basic"
              autoComplete="off"
              onFinish={onFinish}
            >
              <Form.Item
                name="oldPassword"
                rules={[
                  {
                    required: true,
                    message: '请输入旧密码!',
                  },
                ]}
              >
                <Input.Password size="large" placeholder="旧密码" prefix={<LockOutlined />} />
              </Form.Item>
              <Form.Item
                name="newPassword"
                rules={[
                  {
                    required: true,
                    message: '请输入新密码!',
                  },
                  ({ getFieldValue }) => ({
                    validator(rule, value) {
                      if (value && getFieldValue('oldPassword') === value) {
                        return Promise.reject("不能与旧密码一致!")
                      }
                      return Promise.resolve()
                    }
                  })
                ]}
              >
                <Input.Password size="large" placeholder="新密码" prefix={<LockOutlined />} />
              </Form.Item>
              <Form.Item
                name="confirmPassword"
                rules={[
                  {
                    required: true,
                    message: '请输入确认新密码!',
                  },
                  ({ getFieldValue }) => ({
                    validator(rule, value) {
                      if (value && getFieldValue('newPassword') !== value) {
                        return Promise.reject("两次密码输入不一致!")
                      }
                      return Promise.resolve()
                    }
                  })
                ]}
              >
                <Input.Password size="large" placeholder="确认新密码" prefix={<LockOutlined />} />
              </Form.Item>

              <Form.Item>
                <Button type="primary" htmlType="submit" size="large" block>
                  Reset
                </Button>
              </Form.Item>
            </Form>
          </Card>
        </Col>
        <Col span={8}></Col>
      </Row>
    </>
  )
}