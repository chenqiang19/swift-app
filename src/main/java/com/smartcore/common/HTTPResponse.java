package com.smartcore.common;

import java.util.Objects;

public class HTTPResponse implements Response{
    private String body = "";
    private int code = -1;
    private byte[] data;
    private String header = "";
    private byte[] headByte;
    public HTTPResponse() {

    }
    @Override
    public String getBody() {
        if (body == null) {
            Objects.requireNonNull(data);
            return data.toString();
        }
        return body;
    }
    @Override
    public int getCode() {
        return this.code;
    }
    public void setBody(String body) {
        this.body = body;
        this.data = body.getBytes();
    }
    public void setBody(byte[] data) {
        this.data = data;
        this.body = data.toString();
    }

    public void setCode(int code) {
        this.code = code;
    }

    @Override
    public boolean isSuccess() {
        if (code == -1)
            return true;
        return code >= 200 && code <= 300;
    }

    public void setHeader(String header){
        this.header = header;
        this.headByte = header.getBytes();
    }

    @Override
    public String getHeader() { return this.header; }
}

