package com.apiflows.parser;

import com.apiflows.model.OpenAPIWorkflow;

import java.util.List;

public class OpenAPIWorkflowParserResult {

    public enum Format {
        JSON, YAML
    }
    private boolean valid = true;
    private List<String> errors = null;
    private OpenAPIWorkflow openAPIWorkflow;

    private String location;
    private String content;
    private Format format;

    public boolean isValid() {
        return valid;
    }

    public void setValid(boolean valid) {
        this.valid = valid;
    }

    public OpenAPIWorkflow getOpenAPIWorkflow() {
        return openAPIWorkflow;
    }

    public void setOpenAPIWorkflow(OpenAPIWorkflow openAPIWorkflow) {
        this.openAPIWorkflow = openAPIWorkflow;
    }

    public List<String> getErrors() {
        return errors;
    }

    public void setErrors(List<String> errors) {
        this.errors = errors;
    }

    public void  addError(String error) {
        this.errors.add(error);
    }

    public String getLocation() {
        return location;
    }

    public void setLocation(String location) {
        this.location = location;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public Format getFormat() {
        return format;
    }

    public void setFormat(Format format) {
        this.format = format;
    }

    public boolean isJson() {
        return Format.JSON.equals(this.format);
    }

    public boolean isYaml() {
        return Format.YAML.equals(this.format);
    }

}
