package com.github.parser.source;

import com.github.model.Workflow;
import com.github.parser.OpenAPIWorkflowParser;
import com.github.parser.OpenAPIWorkflowParserResult;
import io.swagger.v3.oas.models.Operation;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class WorkflowBinderTest {

    private WorkflowBinder binder = new WorkflowBinder();

    @Test
    void bind() {
        final String WORKFLOWS_SPEC_FILE = "src/test/java/resources/1.0.0/pet-coupons.workflow.yaml";

        OpenAPIWorkflowParserResult result = new OpenAPIWorkflowParser().parse(WORKFLOWS_SPEC_FILE);

        binder.bind(result.getOpenAPIWorkflow());

        assertNotNull(result);
        Workflow workflowApplyCoupon = result.getOpenAPIWorkflow().getWorkflows().get(0);
        assertNotNull(workflowApplyCoupon);
        assertEquals(3, workflowApplyCoupon.getSteps().size());
        // step 3 (workflow)
        assertNull(workflowApplyCoupon.getSteps().get(2).getOperationId());
        assertEquals("place-order", workflowApplyCoupon.getSteps().get(2).getWorkflowId());
        assertEquals("place-order", workflowApplyCoupon.getSteps().get(2).getWorkflow().getWorkflowId());

    }
}