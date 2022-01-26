package com.yyl.myblog.shiro;

import org.apache.shiro.authc.AuthenticationToken;

//用于token在各种代码之间转化传输
public class JwtToken implements AuthenticationToken {

    private String token;

    //构造器
    public JwtToken(String token) {
        this.token = token;
    }

    //传过来一个AuthenticationToken，返回一个字符串类型的token
    @Override
    public Object getPrincipal() {
        return token;
    }

    //传过来一个AuthenticationToken，返回一个字符串类型的token
    @Override
    public Object getCredentials() {
        return token;
    }

}
