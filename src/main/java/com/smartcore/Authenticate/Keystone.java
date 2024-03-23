package com.smartcore.Authenticate;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.smartcore.common.Response;
import com.smartcore.utils.HttpUtils;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

@Component
public class Keystone {

    @Value("${keystone.url}")
    private String url;

    @Value("${service.object}")
    private String serviceName;

    @Value("${service.interface}")
    private String interfaceOfEndpoint;

    private String body;

    public String authenticate(Secret secret) throws Exception {
        Response result = HttpUtils.httpPost(url, secret.toString());
        this.body = result.getBody();
        return result.getHeader();
    }

    public String getEndpoint() {
        String endpointURL="";
        Boolean flag = false;
        JSONObject tokenJson =  JSON.parseObject(this.body);
        JSONObject tokenMap = tokenJson.getJSONObject("token");
        JSONArray catalogs = tokenMap.getJSONArray("catalog");
         for (Object object : catalogs) {
            JSONObject catalog = JSON.parseObject(object.toString());
            if (catalog.get("name").equals(serviceName)) {
                JSONArray endpoints = catalog.getJSONArray("endpoints");
                for (Object endpoint : endpoints) {
                    JSONObject endpointJson = JSON.parseObject(endpoint.toString());
                    if (endpointJson.get("interface").equals(interfaceOfEndpoint)) {
                        endpointURL = endpointJson.get("url").toString();
                        flag = true;
                        break;
                    }
                }
                if (flag) {
                    break;
                }
            }
        }
        return endpointURL;
    }
}
