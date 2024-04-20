package com.apiflows.model;

import com.fasterxml.jackson.annotation.JsonProperty;

public class Parameter {

    private String name;
    private String in;
    private String value;
    private String style;

    @JsonProperty("name")
    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    @JsonProperty("in")
    public String getIn() {
        return in;
    }

    public void setIn(String in) {
        this.in = in;
    }

    @JsonProperty("value")
    public String getValue() {
        return value;
    }

    public void setValue(String value) {
        this.value = value;
    }

    public String getStyle() {
        return style;
    }

    public void setStyle(String style) {
        this.style = style;
    }

    public Parameter name(String name) {
        this.name = name;
        return this;
    }

    public Parameter in(String in) {
        this.in = in;
        return this;
    }

    public Parameter value(String value) {
        this.value = value;
        return this;
    }

    public Parameter style(String style) {
        this.style = style;
        return this;
    }

}
