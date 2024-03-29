package com.apiflows.model;

import com.fasterxml.jackson.annotation.JsonProperty;

public class Info {

    private String title;
    private String version;
    private String description;

    @JsonProperty("title")
    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    @JsonProperty("version")
    public String getVersion() {
        return version;
    }

    public void setVersion(String version) {
        this.version = version;
    }

    @JsonProperty("description")
    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public Info title(String title) {
        this.title = title;
        return this;
    }

    public Info version(String version) {
        this.version = version;
        return this;
    }

    public Info description(String description) {
        this.description = description;
        return this;
    }

}
