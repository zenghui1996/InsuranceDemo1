import React from 'react'
import { useNavigate } from "react-router-dom";
import { Button, Form, Input, Card, Row, Col } from 'antd';
import {
  UserOutlined,
  LockOutlined
} from '@ant-design/icons';
import { post } from '../utils/Request';
import { getMsg } from '../utils/Message';

export default function Login() {

  const navigate = useNavigate()

  function onFinish(values) {
    post('/login', values)
      .then((res) => {
        if (res.success) {
          localStorage.setItem('user_id', res.data.userId);
          localStorage.setItem('user_name', res.data.userName);
          localStorage.setItem('adminnistrator_flag', res.data.adminnistratorFlag);
          localStorage.setItem('organization_authority_code', res.data.organizationAuthorityCode);
          localStorage.setItem('access_token', `Bearer` + res.data.accessToken);
          localStorage.setItem('login', 'true')
          navigate('/')
        } else {
          if (res.errMsg) {
            alert(getMsg(res.errMsg));
          }
        }
      })
  }

  return (
    <>
      <Row gutter={[16, 128]}>
        <Col span={8}></Col>
        <Col span={8}></Col>
        <Col span={8}></Col>
        <Col span={8}></Col>
        <Col span={8}>
          <Card style={{ backgroundColor: '#F2F2F2' }}>
            <Form
              name="basic"
              autoComplete="off"
              onFinish={onFinish}
            >
              <Form.Item
                name="userId"
                rules={[
                  {
                    required: true,
                    message: '请输入用户ID!',
                  },
                ]}
              >
                <Input size="large" placeholder="用户ID" prefix={<UserOutlined />} />
              </Form.Item>

              <Form.Item
                name="password"
                rules={[
                  {
                    required: true,
                    message: '请输入密码!',
                  },
                ]}
              >
                <Input.Password size="large" placeholder="密码" prefix={<LockOutlined />} />
              </Form.Item>

              <Form.Item>
                <Button type="primary" htmlType="submit" size="large" block>
                  Login
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