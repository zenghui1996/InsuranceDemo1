package com.bandmix.oauthapi.service;

import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.factory.PasswordEncoderFactories;

public class Test {

    public static void main(String[] args){
        String temp = PasswordEncoderFactories.createDelegatingPasswordEncoder().encode("123456");
        System.out.println("temp==="+temp);

        String password = "{bcrypt}" + new BCryptPasswordEncoder().encode("123456");
        System.out.println("password==="+password);
    }
}
