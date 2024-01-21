package com.apiflows.parser.source;

import com.apiflows.model.Step;
import com.apiflows.model.OpenAPIWorkflow;
import com.apiflows.model.Workflow;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;

public class WorkflowBinder {

    private static final Logger LOGGER = LoggerFactory.getLogger(WorkflowBinder.class);

    /**
     * Binds workflows
     *
     * @param openAPIWorkflow
     */
    public void bind(OpenAPIWorkflow openAPIWorkflow) {
        for (Workflow workflow : openAPIWorkflow.getWorkflows()) {
            for (Step step : workflow.getSteps()) {
                if(step.getWorkflowId() != null) {
                    step.setWorkflow(findWorkflowById(step.getWorkflowId(), openAPIWorkflow.getWorkflows()));
                }
            }
        }
    }

    Workflow findWorkflowById(String workflowId, List<Workflow> workflows) {
        Workflow workflow = null;

        for(Workflow w: workflows) {
            if(workflowId.equals(w.getWorkflowId())) {
                workflow = w;
            }
        }

        return workflow;
    }

}
