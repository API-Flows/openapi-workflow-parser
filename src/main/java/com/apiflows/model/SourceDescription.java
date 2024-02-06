package com.apiflows.model;

import com.fasterxml.jackson.annotation.JsonProperty;

public class SourceDescription {

    private String name;
    private String url;
    private String type;

    @JsonProperty("name")
    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    @JsonProperty("url")
    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    @JsonProperty("type")
    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public boolean isOpenApi() {
        return "openapi".equals(this.type);
    }

    public boolean isWorkflowsSpec() {
        return "workflowsSpec".equals(this.type);
    }

    public SourceDescription name(String name) {
        this.name = name;
        return this;
    }

    public SourceDescription url(String url) {
        this.url = url;
        return this;
    }

    public SourceDescription type(String type) {
        this.type = type;
        return this;
    }

}
