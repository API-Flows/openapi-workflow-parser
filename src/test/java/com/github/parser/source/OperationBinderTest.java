package com.github.parser.source;

import com.github.model.Workflow;
import com.github.parser.OpenAPIWorkflowParser;
import com.github.parser.OpenAPIWorkflowParserResult;
import io.swagger.v3.oas.models.Operation;
import org.junit.jupiter.api.Test;

import java.net.MalformedURLException;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class OperationBinderTest {

    private OperationBinder binder = new OperationBinder();

    @Test
    void getOperations() {
        final String OPENAPI_FILE = "src/test/java/resources/1.0.0/pet-coupons.openapi.yaml";
        List<Operation> operations = binder.getOperations(OPENAPI_FILE);

        assertNotNull(operations);
        assertEquals(11, operations.size());
    }

    @Test
    void getOperationsFromUrl() {
        final String OPENAPI_FILE = "https://raw.githubusercontent.com/OAI/sig-workflows/main/examples/1.0.0/pet-coupons.openapi.yaml";
        List<Operation> operations = binder.getOperations(OPENAPI_FILE);

        assertNotNull(operations);
        assertEquals(11, operations.size());
    }


    @Test
    void bind() {
        final String WORKFLOWS_SPEC_FILE = "src/test/java/resources/1.0.0/pet-coupons.workflow.yaml";

        OpenAPIWorkflowParserResult result = new OpenAPIWorkflowParser().parse(WORKFLOWS_SPEC_FILE);

        binder.bind(result.getOpenAPIWorkflow());

        assertNotNull(result);
        Workflow workflowApplyCoupon = result.getOpenAPIWorkflow().getWorkflows().get(0);
        assertNotNull(workflowApplyCoupon);
        assertEquals(3, workflowApplyCoupon.getSteps().size());
        // step 1 (operation)
        assertEquals("findPetsByTags", workflowApplyCoupon.getSteps().get(0).getOperation().getOperationId());
        assertNotNull(workflowApplyCoupon.getSteps().get(0).getOperation());
        // step 2 (operation)
        assertEquals("getPetCoupons", workflowApplyCoupon.getSteps().get(1).getOperation().getOperationId());
        assertNotNull(workflowApplyCoupon.getSteps().get(1).getOperation());
        // step 3 (workflow)
        assertNull(workflowApplyCoupon.getSteps().get(2).getOperationId());
        assertEquals("place-order", workflowApplyCoupon.getSteps().get(2).getWorkflowId());
    }

    @Test
    void bindFromUrl() {
        final String WORKFLOWS_SPEC_FILE = "https://raw.githubusercontent.com/OAI/sig-workflows/main/examples/1.0.0/pet-coupons.workflow.yaml";

        OpenAPIWorkflowParserResult result = new OpenAPIWorkflowParser().parse(WORKFLOWS_SPEC_FILE);

        binder.bind(result.getOpenAPIWorkflow());

        assertNotNull(result);
        Workflow workflowApplyCoupon = result.getOpenAPIWorkflow().getWorkflows().get(0);
        assertNotNull(workflowApplyCoupon);
        assertEquals(3, workflowApplyCoupon.getSteps().size());
        // step 1 (operation)
        assertEquals("findPetsByTags", workflowApplyCoupon.getSteps().get(0).getOperation().getOperationId());
        assertNotNull(workflowApplyCoupon.getSteps().get(0).getOperation());
        // step 2 (operation)
        assertEquals("getPetCoupons", workflowApplyCoupon.getSteps().get(1).getOperation().getOperationId());
        assertNotNull(workflowApplyCoupon.getSteps().get(1).getOperation());
        // step 3 (workflow)
        assertNull(workflowApplyCoupon.getSteps().get(2).getOperationId());
        assertEquals("place-order", workflowApplyCoupon.getSteps().get(2).getWorkflowId());
    }


}
