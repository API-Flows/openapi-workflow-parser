package com.apiflows.model;

import java.util.ArrayList;
import java.util.List;

public class OpenAPIWorkflow {

    public enum Format {
        JSON, YAML
    }

    private String location;
    private String content;
    private Format format;

    private String workflowsSpec;
    private Info info;
    private List<SourceDescription> sourceDescriptions = new ArrayList<>();
    private List<Workflow> workflows = new ArrayList<>();
    private Components components;

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

    public String getWorkflowsSpec() {
        return workflowsSpec;
    }

    public void setWorkflowsSpec(String workflowsSpec) {
        this.workflowsSpec = workflowsSpec;
    }

    public Info getInfo() {
        return info;
    }

    public void setInfo(Info info) {
        this.info = info;
    }

    public List<SourceDescription> getSourceDescriptions() {
        return sourceDescriptions;
    }

    public void setSourceDescriptions(List<SourceDescription> sourceDescriptions) {
        this.sourceDescriptions = sourceDescriptions;
    }

    public List<Workflow> getWorkflows() {
        return workflows;
    }

    public void setWorkflows(List<Workflow> workflows) {
        this.workflows = workflows;
    }

    public Components getComponents() {
        return components;
    }

    public void setComponents(Components components) {
        this.components = components;
    }

}
