import React, { useState } from 'react';
import { useNavigate } from "react-router-dom";
import { Popover, Avatar } from 'antd';

export default function UserOperate() {
  const [visible, setVisible] = useState(false);
  const navigate = useNavigate()
  const user = localStorage.getItem('user_name');

  const handleVisibleChange = (visible) => {
    setVisible(visible);
  };

  const content = (
    <div>
      <p><a onClick={logout}>注销</a></p>
      <p><a onClick={resetPassword}>密码重置</a></p>
    </div>
  );

  function logout() {
    localStorage.clear()
    navigate('/Login')
  };

  function resetPassword() {
    setVisible(false);
    navigate('/ResetPassword')
  };

  return (
    <Popover
      content={content}
      trigger="click"
      visible={visible}
      onVisibleChange={handleVisibleChange}
      placement="bottomRight"
    >
      <Avatar style={{
        backgroundColor: '#7265e6',
        verticalAlign: 'middle',
      }}>
        {user}
      </Avatar>
    </Popover>
  )
}