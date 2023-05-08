package com.bandmix.commonapi.model.dto;

import lombok.Data;

@Data
public class LoginDto {

    private String userId;
    
    private String userName;

	private String adminnistratorFlag;

	private String organizationAuthorityCode;

    private String accessToken;

    private String refreshToken;
}
