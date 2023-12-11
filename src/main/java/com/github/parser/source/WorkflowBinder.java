package com.github.parser.source;

import com.github.model.OpenAPIWorkflow;
import com.github.model.Step;
import com.github.model.Workflow;
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
                if(workflow == null) {
                    workflow = w;
                } else {
                    // workflowId already found!?
                    // TODO validation
                    LOGGER.warn("operationId already found {}", workflowId);
                }
            }
        }

        return workflow;
    }

}
