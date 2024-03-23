package com.smartcore.domain;

import lombok.Data;

import java.io.Serializable;

@Data
public class Content implements Serializable {
    private static final long serialVersionUID = 1264882787729986117L;

    private String content;
}
