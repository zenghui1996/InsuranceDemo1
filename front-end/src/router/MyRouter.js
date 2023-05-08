import React from 'react';
import { BrowserRouter as Router, Routes, Route, Navigate } from 'react-router-dom'

import Main from '../layout/Main';
import Home from '../pages/Home';
import Login from '../pages/Login';
import UserManage from '../pages/UserManage';
import RoleManage from '../pages/RoleManage';
import DataManage from '../pages/DataManage';
import DataList from '../pages/DataList';
import BandMixMonth from '../pages/BandMixMonth';
import BandMixQuarter from '../pages/BandMixQuarter';
import PromoteData from '../pages/PromoteData';
import NewHireDate from '../pages/NewHireDate';
import ResignationData from '../pages/ResignationData';
import RatioList from '../pages/RatioList';
import Sample from '../pages/Sample';
import DataListSample from '../pages/DataListSample';
import ResetPassword from '../pages/ResetPassword';

// RequireAuth 组件相当于一个拦截器，是否返回被拦截的组件要听他的
function RequireAuth({ children }) {
  const authed = localStorage.getItem('login')
  return authed === 'true' ? ( // 判断 localstorage 中登录状态是否为 true
    children
  ) : (
    <Navigate to="Login" replace /> // 跳转到登录
  );
}

function RoleAuth({ children }) {
  const roleCode = localStorage.getItem('adminnistrator_flag')
  return (roleCode === '2' || roleCode === '3' ) ? (
    children
  ) : (
    <Navigate to="../Login" replace /> // 跳转到登录
  );
}

class MyRouter extends React.Component {
  render() {
    return (
      <Router>
        <Routes>
          <Route path="Login" element={<Login />} />
          <Route path="/" element={
            <RequireAuth>
              <Main />
            </RequireAuth>
          } >
            <Route index element={<Home />} />
            <Route path="UserManage" element={
              <RoleAuth>
                <UserManage />
              </RoleAuth>
            } />
            <Route path="RoleManage" element={
              <RoleAuth>
                <RoleManage />
              </RoleAuth>
            } />
            <Route path="DataManage" element={
              <RoleAuth>
                <DataManage />
              </RoleAuth>} />
            <Route path="Sample" element={<Sample />} />
            <Route path="DataListSample" element={<DataListSample />} />
            <Route path="DataList" element={<DataList />} />
            <Route path="BandMixMonth" element={<BandMixMonth />} />
            <Route path="BandMixQuarter" element={<BandMixQuarter />} />
            <Route path="PromoteData" element={<PromoteData />} />
            <Route path="NewHireDate" element={<NewHireDate />} />
            <Route path="ResignationData" element={<ResignationData />} />
            <Route path="RatioList" element={<RatioList />} />
            <Route path="ResetPassword" element={<ResetPassword />} />
          </Route>
        </Routes>
      </Router >
    );
  }
}

export default MyRouter;