package com.smartcore.common;

public interface Response {
    String getBody();
    int getCode();
    String getHeader();
    boolean isSuccess();
}