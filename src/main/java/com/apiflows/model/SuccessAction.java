package com.apiflows.model;

import java.util.ArrayList;
import java.util.List;

public class SuccessAction {

    private String type;
    private String workflowId;
    private String stepId;
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

    public List<Criterion> getCriteria() {
        return criteria;
    }

    public void setCriteria(List<Criterion> criteria) {
        this.criteria = criteria;
    }

    public void addCriteria(Criterion criterion) {
        if(this.criteria == null) {
            this.criteria = new ArrayList<>();
        }
        this.criteria.add(criterion);
    }

    public SuccessAction type(String type) {
        this.type = type;
        return this;
    }

    public SuccessAction workflowId(String workflowId) {
        this.workflowId = workflowId;
        return this;
    }

    public SuccessAction stepId(String stepId) {
        this.stepId = stepId;
        return this;
    }
}
