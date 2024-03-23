package com.smartcore.Authenticate;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

@Component
public class Auth {
    @Autowired
    private Keystone keystone;

    @Value("${keystone.username}")
    private String username;

    @Value("${keystone.password}")
    private String password;

    @Value("${keystone.region}")
    private String region;

    @Value("${keystone.domain}")
    private String domain;

    public String getToken() throws Exception {
        Secret secret = new Secret();
        secret.setPassword(password);
        secret.setTenantName(username);
        secret.setUsername(username);
        /** 必须保证secret合法 **/
        assert(secret.isValid());
        return keystone.authenticate(secret);
    }
}