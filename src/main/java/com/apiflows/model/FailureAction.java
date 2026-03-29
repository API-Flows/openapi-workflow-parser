package com.apiflows.model;

public class FailureAction extends Action {

    private Double retryAfter;
    private Integer retryLimit;

    public Double getRetryAfter() {
        return retryAfter;
    }

    public void setRetryAfter(Double retryAfter) {
        this.retryAfter = retryAfter;
    }

    public Integer getRetryLimit() {
        return retryLimit;
    }

    public void setRetryLimit(Integer retryLimit) {
        this.retryLimit = retryLimit;
    }

    public FailureAction name(String name) {
        setName(name);
        return this;
    }

    public FailureAction type(String type) {
        setType(type);
        return this;
    }

    public FailureAction stepId(String stepId) {
        setStepId(stepId);
        return this;
    }

    public FailureAction workflowId(String workflowId) {
        setWorkflowId(workflowId);
        return this;
    }

    public FailureAction retryAfter(Double retryAfter) {
        this.retryAfter = retryAfter;
        return this;
    }

    public FailureAction retryLimit(Integer retryLimit) {
        this.retryLimit = retryLimit;
        return this;
    }
}
