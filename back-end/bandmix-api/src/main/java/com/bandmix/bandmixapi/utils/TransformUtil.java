package com.bandmix.bandmixapi.utils;

import org.springframework.beans.BeanUtils;

import com.bandmix.bandmixapi.entity.BandmixInfo;
import com.bandmix.commonapi.model.dto.BandmixDto;

public class TransformUtil {

    public static BandmixDto generateUserDto(BandmixInfo bandmixInfo) {

        BandmixDto bandmixDto = new BandmixDto();
        BeanUtils.copyProperties(bandmixInfo, bandmixDto);
        return bandmixDto;
    }
}
