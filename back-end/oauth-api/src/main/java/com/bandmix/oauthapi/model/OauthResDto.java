package com.bandmix.oauthapi.model;

import lombok.Data;

@Data
public class OauthResDto {

    private boolean isOauth;

    private String userName;

    private String accessToken;

    private String refreshToken;

    private String errCode;

    private Exception exception;

    public static OauthResDto errorOauth(String errCode, Exception exception) {
        OauthResDto oauthResDto = new OauthResDto();
        oauthResDto.setOauth(false);
        oauthResDto.setErrCode(errCode);
        oauthResDto.setException(exception);
        return oauthResDto;
    }
}
