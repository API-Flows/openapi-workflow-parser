package com.apiflows.parser;


import java.util.ArrayList;
import java.util.List;

public class OpenAPIWorkflowValidatorResult {

    private boolean valid = true;
    private List<String> errors = new ArrayList<>();

    public boolean isValid() {
        return valid;
    }

    public void setValid(boolean valid) {
        this.valid = valid;
    }

    public List<String> getErrors() {
        return errors;
    }

    public void setErrors(List<String> errors) {
        this.errors = errors;
    }

    public void addError(String error) {
        this.errors.add(error);
        this.valid = false;
    }
}
