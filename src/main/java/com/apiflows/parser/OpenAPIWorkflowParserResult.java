package com.apiflows.parser;

import com.apiflows.model.OpenAPIWorkflow;

public class OpenAPIWorkflowParserResult {

    private boolean valid = true;
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
}
