package com.smartcore.service;

import com.smartcore.domain.Content;
import com.smartcore.domain.ObjectDetail;

public interface ObjectsService {

    Content getObjects(String account, String container, String object) throws Exception;

    ObjectDetail getObjectDetail(String account, String container, String object) throws Exception;

    void updateObject(String account, String container, String object, String content) throws Exception;

    void removeObject(String account, String container, String object) throws Exception;

    void createObject(String path, String desination, String payload) throws Exception;

    String copyObject(String sourceUrl, String destinationUrl) throws Exception;
}
