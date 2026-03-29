package com.apiflows.model;

import java.util.List;

public class RequestBody {

    private String contentType;
    private Object payload;
    private List<PayloadReplacement> replacements;

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

    public List<PayloadReplacement> getReplacements() {
        return replacements;
    }

    public void setReplacements(List<PayloadReplacement> replacements) {
        this.replacements = replacements;
    }
}
