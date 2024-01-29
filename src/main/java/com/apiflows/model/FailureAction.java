package com.apiflows.model;

import io.swagger.v3.oas.models.media.IntegerSchema;

import java.util.ArrayList;
import java.util.List;

public class FailureAction {

    private String type;
    private String workflowId;
    private String stepId;
    private Long retryAfter;
    private Integer retryLimit;
    private List<Criterion> criteria;

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getWorkflowId() {
        return workflowId;
    }

    public void setWorkflowId(String workflowId) {
        this.workflowId = workflowId;
    }

    public String getStepId() {
        return stepId;
    }

    public void setStepId(String stepId) {
        this.stepId = stepId;
    }

    public Long getRetryAfter() {
        return retryAfter;
    }

    public void setRetryAfter(Long retryAfter) {
        this.retryAfter = retryAfter;
    }

    public Integer getRetryLimit() {
        return retryLimit;
    }

    public void setRetryLimit(Integer retryLimit) {
        this.retryLimit = retryLimit;
    }

    public List<Criterion> getCriteria() {
        return criteria;
    }

    public void setCriteria(List<Criterion> criteria) {
        this.criteria = criteria;
    }

    public FailureAction type(String type) {
        this.type = type;
        return this;
    }

    public FailureAction stepId(String stepId) {
        this.stepId = stepId;
        return this;
    }

    public FailureAction workflowId(String workflowId) {
        this.workflowId = workflowId;
        return this;
    }

    public FailureAction retryAfter(Long retryAfter) {
        this.retryAfter = retryAfter;
        return this;
    }

    public FailureAction retryLimit(Integer retryLimit) {
        this.retryLimit = retryLimit;
        return this;
    }

    public void addCriteria(Criterion criterion) {
        if(this.criteria == null) {
            this.criteria = new ArrayList<>();
        }
        this.criteria.add(criterion);
    }
}
