package cn.sk.api.sys.shiro;

import org.apache.shiro.authc.AuthenticationToken;
/**
 *@Deseription
 *@Author zhoucp
 *@Date 2020/1/6 15:50
 **/
public class JwtToken implements AuthenticationToken {
    private String token;

    public JwtToken(String token) {
        this.token = token;
    }

    @Override
    public Object getPrincipal() {
        return token;
    }

    @Override
    public Object getCredentials() {
        return token;
    }
}
