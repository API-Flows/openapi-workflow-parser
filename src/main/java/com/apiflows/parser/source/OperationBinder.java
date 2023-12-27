package com.apiflows.parser.source;

import com.apiflows.model.OpenAPIWorkflow;
import com.apiflows.model.SourceDescription;
import com.apiflows.model.Step;
import com.apiflows.model.Workflow;
import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.Operation;
import io.swagger.v3.oas.models.PathItem;
import io.swagger.v3.parser.OpenAPIV3Parser;
import io.swagger.v3.parser.core.models.ParseOptions;
import io.swagger.v3.parser.core.models.SwaggerParseResult;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;

public class OperationBinder {

    private static final Logger LOGGER = LoggerFactory.getLogger(OperationBinder.class);

    /**
     * Binds workflow operations
     * @param openAPIWorkflow
     */
    public void bind(OpenAPIWorkflow openAPIWorkflow) {
        List<Operation> operations = new ArrayList<>();

        for(SourceDescription source : openAPIWorkflow.getSourceDescriptions()) {
            String filename = getRootFolder(openAPIWorkflow.getLocation()) + "/" + source.getUrl();
            operations.addAll(getOperations(filename));
        }

        for(Workflow workflow : openAPIWorkflow.getWorkflows()) {
            for(Step step : workflow.getSteps()) {
                if(step.getOperationId() != null) {
                    step.setOperation(findOperationById(step.getOperationId(), operations));
                } else if(step.getOperationRef() != null) {
                    step.setOperation(findOperationByRef(step.getOperationRef(), operations));
                }
            }
        }
    }

    List<Operation> getOperations(String openapi) {
        List<Operation> operations = new ArrayList<>();
        OpenAPIV3Parser openApiParser = new OpenAPIV3Parser();

        ParseOptions options = new ParseOptions();
        options.setResolve(true);

        SwaggerParseResult parseResult = openApiParser.readLocation(openapi, null, options);
        OpenAPI openAPI = parseResult.getOpenAPI();

        for(PathItem pathItem : openAPI.getPaths().values()) {
            operations.addAll(pathItem.readOperations());
        }

        return operations;

    }

    Operation findOperationById(String operationId, List<Operation> operations) {
        Operation operation = null;

        for(Operation o : operations) {
            if(operationId != null && operationId.equals(o.getOperationId())) {
                operation = o;
            }
        }

        return operation;
    }

    Operation findOperationByRef(String operationRef, List<Operation> operations) {
        Operation operation = null;

        // TODO

//        for(Operation o : operations) {
//
//            if(operationRef != null && operationRef.equals(o.getOperationId())) {
//                operation = o;
//            }
//        }

        return operation;
    }


//
//    String getJsonReference(String jsonReference) throws IOException {
//        ObjectMapper objectMapper = new ObjectMapper();
//
//        JsonNode jsonNode;
//
//        if(isUrl(jsonReference)) {
//            jsonNode = objectMapper.readTree(new URL(jsonReference));
//        } else {
//            jsonNode = objectMapper.readTree(new URL(jsonReference));
//        }
//
//        // Navigate to the node containing the reference
//        JsonNode referenceNode = jsonNode.at("/paths/users/~1findbystatus~1{status}/get");
//
//        // Get the JSON reference as a string
//        String r = referenceNode.toString();
//
//
//        // Unescape the JSON reference
//        return jsonNode.isTextual() ? ((TextNode) jsonNode).asText() : jsonReference;
//
//    }

    String getRootFolder(String location) {
        if(isUrl(location)) {
            return location.substring(0, location.lastIndexOf("/") + 1);
        } else {
            Path filePath = Paths.get(location);

            return filePath.getParent().toString();
        }
    }

    boolean isUrl(String url) {
        return url != null && url.startsWith("http");
    }
}
