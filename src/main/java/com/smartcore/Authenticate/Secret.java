package com.smartcore.Authenticate;

import com.smartcore.utils.StringUtils;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

public class Secret {
    private String username;
    private String password;
    private String tenantId;
    private String tenantName;
    private String token;
    public Secret() {
        super();
    }
    public void setUsername(String username) {
        this.username = username;
    }
    public void setPassword(String password) {
        this.password = password;
    }
    public void setTenantId(String tenantId) {
        this.tenantId = tenantId;
    }
    public void setTenantName(String tenantName) {
        this.tenantName = tenantName;
    }
    public void setToken(String token) {
        this.token = token;
    }
    public boolean isValid() {
        boolean hasCredencial = false;
        boolean hasTenant = false;
        if (StringUtils.isBlank(token)) {
            if (StringUtils.isNotBlank(username) && StringUtils.isNotBlank(password)) {
                hasCredencial = true;
            } else {
                hasCredencial = false;
            }
        } else {
            hasCredencial = true;
        }
        hasTenant = StringUtils.isNotBlank(tenantId) || StringUtils.isNotBlank(tenantName);
        return hasCredencial && hasTenant;
    }

    @Override
    public String toString() {
        JSONObject secret = new JSONObject();
        JSONObject auth = new JSONObject();
        JSONObject identity = new JSONObject();
        JSONObject password = new JSONObject();
        JSONObject user = new JSONObject();
        JSONObject domain = new JSONObject();
        JSONObject scope = new JSONObject();
        JSONObject project = new JSONObject();
        List<String> list = new ArrayList<String>();
        list.add("password");

        domain.put("name", "Default");
        user.put("domain",domain);
        user.put("name",this.username);
        user.put("password",this.password);

        password.put("user", user);
        identity.put("password", password);
        identity.put("methods",list);

        project.put("domain",domain);
        project.put("name",this.tenantName);
        scope.put("project",project);

        auth.put("identity", identity);
        auth.put("scope",scope);
        secret.put("auth",auth);
        return secret.toString();
    }
}
