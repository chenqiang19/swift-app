package com.smartcore.service.Impl;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.smartcore.Authenticate.Auth;
import com.smartcore.Authenticate.Keystone;
import com.smartcore.common.Response;
import com.smartcore.domain.Content;
import com.smartcore.domain.ObjectDetail;
import com.smartcore.service.ObjectsService;
import com.smartcore.utils.HttpUtils;
import com.smartcore.utils.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.BodyInserters;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;

import java.text.SimpleDateFormat;


@Service
public class ObjectsServiceImpl implements ObjectsService {

    @Autowired
    private Auth auth;

    @Autowired
    private Keystone keystone;

    @Override
    public Content getObjects(String account, String container, String object) throws Exception {
        String query = "?format=json&delimiter=%2F";
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
        if (StringUtils.isNotBlank(object) && !object.equals("")) {
            updateUrl += "/" + object;
        }
        Response result = HttpUtils.httpGet(updateUrl + query, token);
        if (result.getCode()<200 || result.getCode() > 300) {
            throw new Exception("请求失败");
        }
        //JSONArray containers = JSON.parseArray(result.getBody());
        //List<Object> containersList = JSON.parseObject(containers.toJSONString(), new TypeReference<List<Object>>() {});
        Content content = new Content();
        content.setContent(result.getBody());
        return content;
    }

    @Override
    public ObjectDetail getObjectDetail(String account, String container, String object) throws Exception {
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
        if (StringUtils.isNotBlank(object) && !object.equals("")) {
            updateUrl += "/" + object;
        }
        Response result = HttpUtils.httpHeader(updateUrl, token);
        if (result.getCode()<200 || result.getCode() > 300) {
            throw new Exception("请求失败");
        }
        ObjectDetail objectDetail = new ObjectDetail();
        JSONObject detail = JSON.parseObject(result.getBody());
        objectDetail.setContentLength(detail.getString("Content-Length"));
        objectDetail.setContentType(detail.getString("Content-Type"));
        objectDetail.setHash(detail.getString("etag"));
        objectDetail.setName(detail.getString("X-Object-Meta-Orig-Filename"));
        objectDetail.setOriginFilename(detail.getString("X-Object-Meta-Orig-Filename"));
        String objectDate = detail.getString("Date");
        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        objectDetail.setCreated(objectDate);

        return objectDetail;
    }

    @Override
    public void updateObject(String account, String container, String object, String content) throws Exception {
        if (container.equals("")) {
            throw new Exception("Container param is missing.");
        }

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
        if (StringUtils.isNotBlank(object) && !object.equals("")) {
            updateUrl += "/" + object;
        }
        String result = HttpUtils.httpPut(updateUrl, token, content.toString());
    }

    @Override
    public void removeObject(String account, String container, String object) throws Exception {
        if (StringUtils.isBlank(container)) {
            throw new Exception("Container param is missing.");
        }

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
        if (StringUtils.isNotBlank(object) && !object.equals("")) {
            updateUrl += "/" + object;
        }
        String result = HttpUtils.httpDelete(updateUrl, token);
    }

    @Override
    public void createObject(String path, String desination, String payload) throws Exception {
        if (path.equals("")) {
            throw new Exception("Container param is missing.");
        }

        String token = auth.getToken();

        String serviceURL = keystone.getEndpoint();
        if (serviceURL.equals("")) {
            throw new Exception("Service Endpoint is missing.");
        }
        String updateUrl = serviceURL + "/" + path;

        Response result = HttpUtils.httpPostWithHeader(updateUrl, token, payload.toString());
    }

    @Override
    public String copyObject(String sourcePath, String destinationPath) throws Exception {
        String token = auth.getToken();

        String serviceURL = keystone.getEndpoint();

        String sourceUrl = serviceURL + "/" + sourcePath;
        String destinationUrl = serviceURL + "/" + destinationPath;

        WebClient webClient = WebClient.create();
        Mono<Void> copyRequest = webClient.get()
                .uri(sourceUrl)
                .retrieve()
                .bodyToMono(byte[].class)
                .flatMap(data -> webClient.put()
                        .uri(destinationUrl)
                        .body(BodyInserters.fromObject(data))
                        .exchange()
                        .then());
        copyRequest.block();
        return "Rename successfully";
    }
}
