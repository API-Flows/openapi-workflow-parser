package com.apiflows.model;

public class RequestBody {

    private String contentType;
    private Object payload;
    private PayoadReplacement payoadReplacement;

    public String getContentType() {
        return contentType;
    }

    public void setContentType(String contentType) {
        this.contentType = contentType;
    }

    public Object getPayload() {
        return payload;
    }

    public void setPayload(Object payload) {
        this.payload = payload;
    }

    public PayoadReplacement getPayoadReplacement() {
        return payoadReplacement;
    }

    public void setPayoadReplacement(PayoadReplacement payoadReplacement) {
        this.payoadReplacement = payoadReplacement;
    }
}
