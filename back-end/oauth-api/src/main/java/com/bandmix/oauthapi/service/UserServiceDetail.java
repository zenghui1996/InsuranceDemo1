package com.bandmix.oauthapi.service;

import com.bandmix.oauthapi.entity.UserInfo;
import com.bandmix.oauthapi.repository.UserInfoRepository;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import java.util.ArrayList;
import java.util.List;

@Service("userDetailService")
public class UserServiceDetail implements UserDetailsService{

    @Autowired
    private UserInfoRepository userInfoRepository;

    @Override
    public UserDetails loadUserByUsername(String userName) {
        UserInfo userInfo = userInfoRepository.findByUserId(userName);
        // 查询用户是否存在
        if (userInfo != null && !StringUtils.isEmpty(userInfo.getUserId())) {
            // 查询用户拥有的角色
            List<GrantedAuthority> list = new ArrayList<GrantedAuthority>();
            list.add(new SimpleGrantedAuthority(userInfo.getAdminnistratorFlag()));
            org.springframework.security.core.userdetails.User authUser = new org.springframework.security.core.userdetails.User(
                    userInfo.getUserId(), userInfo.getPasswordEncrypt(), list);

            return authUser;
        }
        throw new UsernameNotFoundException("用户不存在");
    }
}
