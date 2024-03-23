package com.smartcore.controller;

import com.alibaba.fastjson.JSONObject;
import com.smartcore.domain.Container;
import com.smartcore.domain.ContainerView;
import com.smartcore.service.ContainersService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.util.AntPathMatcher;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.HandlerMapping;

import javax.servlet.http.HttpServletRequest;
import java.util.List;
import java.util.Map;

@RestController
public class ContainersController {

    private AntPathMatcher antPathMatcher = new AntPathMatcher();

    @Autowired
    private ContainersService containersService;

//    @GetMapping(path = {"/containers/{account}/{container}", "/containers/{container}", "/containers"})
//    public ResponseEntity<List<Container>> listContainers(@PathVariable(value = "", required = false) String account,
//                                          @PathVariable(value = "", required = false) String container) throws Exception {
//        ContainerView containerView = containersService.listContainers(account, container);
//        HttpHeaders httpHeaders = new HttpHeaders();
//        Map jsonToMap =  JSONObject.parseObject(containerView.getHeaders());
//        for (Object key : jsonToMap.keySet()) {
//            httpHeaders.set(key.toString(), (String) jsonToMap.get(key.toString()));
//        }
//        return ResponseEntity.accepted().headers(httpHeaders).body(containerView.getContainerList());
//    }

    @GetMapping("/containers/**")
    public ResponseEntity<String> listContainers(HttpServletRequest request) throws Exception {
        String path = (String) request.getAttribute(HandlerMapping.PATH_WITHIN_HANDLER_MAPPING_ATTRIBUTE);
        Integer index = "/containers/".length();
        String realPath = path.substring(index);
        String params = request.getQueryString();
        ContainerView containerView = containersService.listContainers(realPath, params);
        HttpHeaders httpHeaders = new HttpHeaders();
        Map jsonToMap =  JSONObject.parseObject(containerView.getHeaders());
        for (Object key : jsonToMap.keySet()) {
            httpHeaders.set(key.toString(), (String) jsonToMap.get(key.toString()));
        }
        return ResponseEntity.accepted().headers(httpHeaders).body(containerView.getContainerDO());
    }
    @RequestMapping(path = {"/containers/{account}/{container}", "/containers/{container}"}, method = RequestMethod.HEAD)
    public ResponseEntity<String> getContainerDetail(@PathVariable(value = "", required = false) String account,
                                  @PathVariable(value = "", required = false) String container,
                                  @PathVariable(value = "", required = false) String object) throws Exception {
        String headerJson = containersService.getContainerDetail(account, container);
        HttpHeaders httpHeaders = new HttpHeaders();
        Map jsonToMap =  JSONObject.parseObject(headerJson);
        for (Object key : jsonToMap.keySet()) {
            httpHeaders.set(key.toString(), (String) jsonToMap.get(key.toString()));
        }
        return ResponseEntity.accepted().headers(httpHeaders).body("");
    }

    @PutMapping("/containers/**")
    public void updateContainers(HttpServletRequest request) throws Exception {
        String path = (String) request.getAttribute(HandlerMapping.PATH_WITHIN_HANDLER_MAPPING_ATTRIBUTE);
        Integer index = "/containers/".length();
        String realPath = path.substring(index);

        containersService.updateContainers(realPath);
    }

    @DeleteMapping(path = {"/containers/{account}/{container}", "/containers/{container}"})
    public void removeContainers(@PathVariable(value = "", required = false) String account,
                                 @PathVariable String container) throws Exception {
        containersService.removeContainers(account, container);
    }
}
