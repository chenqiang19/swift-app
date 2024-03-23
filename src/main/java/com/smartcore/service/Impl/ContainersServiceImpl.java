package com.smartcore.service.Impl;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.alibaba.fastjson.TypeReference;
import com.smartcore.Authenticate.Auth;
import com.smartcore.Authenticate.Keystone;
import com.smartcore.common.Response;
import com.smartcore.domain.Container;
import com.smartcore.domain.ContainerDetail;
import com.smartcore.domain.ContainerView;
import com.smartcore.domain.ObjectDetail;
import com.smartcore.service.ContainersService;
import com.smartcore.utils.HttpUtils;
import com.smartcore.utils.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;

@Service
public class ContainersServiceImpl implements ContainersService {

    @Autowired
    private Auth auth;

    @Autowired
    private Keystone keystone;

    @Override
    public ContainerView listContainers(String path, String params) throws Exception {
        String token = auth.getToken();
        String serviceURL = keystone.getEndpoint();

        if (serviceURL.equals("")) {
            throw new Exception("Service Endpoint is missing.");
        }
        String updateUrl = serviceURL + "/" + path + "?" + params;

        Response result = HttpUtils.httpGet(updateUrl, token);
        if (result.getCode()<200 || result.getCode() > 300) {
            return new ContainerView();
        }
        JSONArray containers = JSON.parseArray(result.getBody());

        ContainerView containerView = new ContainerView();
        containerView.setContainerDO(containers.toJSONString());
        containerView.setHeaders(result.getHeader());
        return containerView;
    }

    @Override
    public String getContainerDetail(String account, String container) throws Exception {
        String token = auth.getToken();
        String serviceURL = keystone.getEndpoint();

        if (serviceURL.equals("")) {
            throw new Exception("Service Endpoint is missing.");
        }
        String updateUrl = serviceURL;
        if (StringUtils.isNotBlank(account) && !account.equals("")) {
            updateUrl += "/" + account;
        }
        if (StringUtils.isNotBlank(container) && !container.equals("")) {
            updateUrl += "/" + container;
        }

        Response result = HttpUtils.httpHeader(updateUrl, token);
        if (result.getCode()<200 || result.getCode() > 300) {
            throw new Exception("请求失败");
        }

        return result.getBody();
    }

    @Override
    public void updateContainers(String path) throws Exception {

        if (path.equals("")) {
            throw new Exception("Container param is missing.");
        }

        String token = auth.getToken();

        String serviceURL = keystone.getEndpoint();
        if (serviceURL.equals("")) {
            throw new Exception("Service Endpoint is missing.");
        }
        String updateUrl = serviceURL;
        updateUrl += "/" + path;
        String result = HttpUtils.httpPut(updateUrl, token, "");
    }

    @Override
    public void removeContainers(String projectId, String container) throws Exception {
        if (StringUtils.isBlank(container)) {
            throw new Exception("Container param is missing.");
        }

        String token = auth.getToken();

        String serviceURL = keystone.getEndpoint();
        if (serviceURL.equals("")) {
            throw new Exception("Service Endpoint is missing.");
        }
        String updateUrl = serviceURL;
        if (StringUtils.isNotBlank(projectId) && !projectId.equals("")) {
            updateUrl += "/" + projectId;
        }
        updateUrl += "/" + container;
        String result = HttpUtils.httpDelete(updateUrl, token);
    }
}
