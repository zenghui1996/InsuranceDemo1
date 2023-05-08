package com.bandmix.oauthapi.controller;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.oauth2.common.OAuth2AccessToken;
import org.springframework.security.oauth2.provider.endpoint.TokenEndpoint;
import org.springframework.web.HttpRequestMethodNotSupportedException;
import org.springframework.web.bind.annotation.*;

import java.security.Principal;
import java.util.LinkedHashMap;
import java.util.Map;

@RestController
@RequestMapping("/oauth")
@Slf4j
public class OauthController {

    @Autowired
    private TokenEndpoint tokenEndpoint;

    /**
     * =====================================
     * 描   述 : 自定义返回信息添加基本信息
     * 参   数 :  [principal, parameters]
     * 返 回 值 : top.qinxq.single.entity.vo.R
     * =====================================
     */
    @PostMapping("/token")
    public Map<String, Object> postAccessTokenWithUserInfo(Principal principal, @RequestParam Map<String, String> parameters) throws HttpRequestMethodNotSupportedException {
        OAuth2AccessToken accessToken = null;
        Map<String, Object> data = new LinkedHashMap();
        try {
            accessToken = tokenEndpoint.postAccessToken(principal, parameters).getBody();

            data.put("accessToken", accessToken.getValue());
            if (accessToken.getRefreshToken() != null) {
                data.put("refreshToken", accessToken.getRefreshToken().getValue());
            }
        } catch (Exception e) {
            e.printStackTrace();
            data.put("error_description",e.getMessage());
        }

        return data;
    }

}
