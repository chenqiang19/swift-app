package com.smartcore.domain;

import lombok.Data;

import java.io.Serializable;

@Data
public class Object implements Serializable {
    private static final long serialVersionUID = 7720036214634652282L;

    public String subdir;
}
