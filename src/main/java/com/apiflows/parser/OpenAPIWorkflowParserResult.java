package com.apiflows.parser;

import com.apiflows.model.OpenAPIWorkflow;

import java.util.List;

public class OpenAPIWorkflowParserResult {

    private boolean valid = true;
    private List<String> errors = null;
    private OpenAPIWorkflow openAPIWorkflow;

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
}
