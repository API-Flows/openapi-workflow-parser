package com.github.model;

import com.fasterxml.jackson.annotation.JsonProperty;

public class Outputs {

    private String key;

    // Getters and setters

    @JsonProperty("key")
    public String getKey() {
        return key;
    }

    public void setKey(String key) {
        this.key = key;
    }
}

