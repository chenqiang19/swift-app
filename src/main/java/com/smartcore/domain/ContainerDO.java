package com.smartcore.domain;

import lombok.Data;

@Data
public class ContainerDO {
    private String name;
    private String hash;
    private Integer bytes;
    private String content_type;
}
