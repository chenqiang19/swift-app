package com.smartcore.domain;

import lombok.Data;

import java.io.Serializable;

@Data
public class ObjectDetail implements Serializable {

    public String name;
    public String hash;
    public String contentType;
    public String created;
    public String contentLength;
    public String originFilename;
}
