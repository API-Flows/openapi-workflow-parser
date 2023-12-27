package com.apiflows.model;

import com.fasterxml.jackson.annotation.JsonProperty;
import io.swagger.v3.oas.models.media.Schema;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Workflow {

    private String workflowId;
    private String summary;
    private String description;
    private Schema inputs;
    private List<Step> steps = new ArrayList<>();
    private Map<String, String> outputs = new HashMap<>();

    @JsonProperty("workflowId")
    public String getWorkflowId() {
        return workflowId;
    }

    public void setWorkflowId(String workflowId) {
        this.workflowId = workflowId;
    }

    @JsonProperty("summary")
    public String getSummary() {
        return summary;
    }

    public void setSummary(String summary) {
        this.summary = summary;
    }

    @JsonProperty("description")
    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    @JsonProperty("inputs")
    public Schema getInputs() {
        return inputs;
    }

    public void setInputs(Schema inputs) {
        this.inputs = inputs;
    }

    @JsonProperty("steps")
    public List<Step> getSteps() {
        return steps;
    }

    public void setSteps(List<Step> steps) {
        this.steps = steps;
    }

    @JsonProperty("outputs")
    public Map<String, String> getOutputs() {
        return outputs;
    }

    public void setOutputs(Map<String, String> outputs) {
        this.outputs = outputs;
    }
}
