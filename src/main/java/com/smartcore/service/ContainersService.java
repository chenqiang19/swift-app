package com.smartcore.service;

import com.smartcore.domain.ContainerView;


public interface ContainersService {
    ContainerView listContainers(String path, String params) throws Exception;

    String getContainerDetail(String projectId, String container) throws Exception;

    void updateContainers(String path) throws Exception;

    void removeContainers(String projectId, String container) throws Exception;
}
