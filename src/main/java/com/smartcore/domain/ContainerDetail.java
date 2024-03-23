package com.smartcore.domain;

import lombok.Data;

import java.io.Serializable;

@Data
public class ContainerDetail implements Serializable {
    private static final long serialVersionUID = -1386027535620023870L;

    public Integer ObjectCount;
    public Integer BytesAcutal;
    public String created;
    public String StoragePolicy;
    public String Asl;
}
