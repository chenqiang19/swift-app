package com.smartcore.controller;

import com.smartcore.domain.Content;
import com.smartcore.domain.ObjectDetail;
import com.smartcore.service.ObjectsService;
import com.smartcore.utils.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.reactive.function.BodyInserters;
import org.springframework.web.reactive.function.client.WebClient;
import org.springframework.web.servlet.HandlerMapping;
import reactor.core.publisher.Mono;

import javax.servlet.http.HttpServletRequest;


@RestController
public class ObjectsController {

    @Autowired
    private ObjectsService objectsService;

    @GetMapping(path = {"/objects/{account}/{container}/{object}", "/objects/{container}/{object}", "/objects/{object}", "/objects"})
    public Content getObjects(@PathVariable(value = "", required = false) String account,
                                          @PathVariable(value = "", required = false) String container,
                                          @PathVariable(value = "", required = false) String object) throws Exception {
        return objectsService.getObjects(account, container, object);
    }

    @RequestMapping(path = {"/objects/{account}/{container}/{object}", "/objects/{container}/{object}", "/objects/{object}"}, method = RequestMethod.HEAD)
    public ResponseEntity<String> getObjectDetail(@PathVariable(value = "", required = false) String account,
                                                      @PathVariable(value = "", required = false) String container,
                                                      @PathVariable(value = "", required = false) String object) throws Exception {
        ObjectDetail objectDetail = objectsService.getObjectDetail(account, container, object);
        HttpHeaders httpHeaders = new HttpHeaders();
        httpHeaders.set("Content-Type", objectDetail.getContentType());
        httpHeaders.set("X-Object-Meta-Orig-Filename", objectDetail.originFilename);
        httpHeaders.set("Date", objectDetail.created);
        return ResponseEntity.accepted().headers(httpHeaders).body("");
    }

    @PostMapping("/objects/**")
    public void createObject(@RequestBody String payload, HttpServletRequest request) throws Exception {
        String path = (String) request.getAttribute(HandlerMapping.PATH_WITHIN_HANDLER_MAPPING_ATTRIBUTE);
        Integer index = "/containers/".length();
        String realPath = path.substring(index);
        String destination = request.getHeader("Destination");
        objectsService.createObject(realPath, destination, payload);
    }

    @PutMapping(path = {"/objects/{account}/{container}/{object}", "/objects/{container}/{object}", "/objects/{object}"})
    public void updateObject(@PathVariable(value = "", required = false) String account,
                                 @PathVariable(value = "", required = false) String container,
                                 @PathVariable String object,
                                 @RequestBody String payload) throws Exception {
        objectsService.updateObject(account, container, object, payload);
    }

    @DeleteMapping(path = {"/objects/{account}/{container}/{object}", "/objects/{container}/{object}", "/objects/{object}"})
    public void removeObject(@PathVariable(value = "", required = false) String account,
                                 @PathVariable(value = "", required = false) String container,
                                 @PathVariable String object) throws Exception {
        objectsService.removeObject(account, container, object);
    }

    @PatchMapping("/objects/copy/**")
    public ResponseEntity<String> copyFile(HttpServletRequest request) throws Exception {
        String destination = request.getHeader("Destination");
        if (StringUtils.isBlank(destination)) {
            return ResponseEntity.badRequest().body("Destination is not in header");
        }

        String sourceUrl = request.getServletPath();
        sourceUrl = sourceUrl.substring(sourceUrl.indexOf("copy/") + 5);
        Integer sourceIndex = sourceUrl.lastIndexOf("/");

        Integer destinationIndex = destination.lastIndexOf("/");
        String destinationUrl = sourceUrl.substring(0, sourceIndex) + destination.substring(destinationIndex);
        String result = objectsService.copyObject(sourceUrl, destinationUrl);
        return ResponseEntity.accepted().body(result);
    }
}
