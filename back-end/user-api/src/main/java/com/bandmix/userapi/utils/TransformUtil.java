package com.bandmix.userapi.utils;

import org.springframework.beans.BeanUtils;

import com.bandmix.commonapi.model.dto.UserDto;
import com.bandmix.userapi.entity.UserInfo;

public class TransformUtil {

    public static UserDto generateUserDto(UserInfo userInfo) {

        UserDto userDto = new UserDto();
        BeanUtils.copyProperties(userInfo, userDto);
        return userDto;
    }
}
