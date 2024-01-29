package com.apiflows.parser;

import com.apiflows.model.OpenAPIWorkflow;
import com.apiflows.parser.source.OperationBinder;
import com.apiflows.parser.source.WorkflowBinder;
import com.apiflows.parser.util.HttpUtil;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.dataformat.yaml.YAMLFactory;
import com.apiflows.parser.util.PathUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class OpenAPIWorkflowParser {

    private static final Logger LOGGER = LoggerFactory.getLogger(OpenAPIWorkflowParser.class);

    /**
     * Parse an OpenAPI Workflow file
     * @param input url, filepath or content (as string)
     * @return instance of OpenAPIWorkflowParserResult
     */
    public OpenAPIWorkflowParserResult parse(String input) {
        return parse(input, new ParseOptions());
    }

    /**
     * Parse an OpenAPI Workflow file
     * @param input url, filepath or content (as string)
     * @param options Options
     * @return instance of OpenAPIWorkflowParserResult
     */
    public OpenAPIWorkflowParserResult parse(String input, ParseOptions options) {

        OpenAPIWorkflowParserResult result = new OpenAPIWorkflowParserResult();

        try {

            PathUtil pathUtil = new PathUtil();
            HttpUtil httpUtil = new HttpUtil();

            String content;

            if (httpUtil.isUrl(input)) {
                content = httpUtil.call(input);
                result.setLocation(input);
            } else if (pathUtil.isFile(input)) {
                content = pathUtil.getFromFile(input);
                result.setLocation(input);
            } else {
                // content as string
                content = input;
                result.setLocation(null);
            }

            result.setContent(content);
            result.setFormat(getFormat(content));

            try {

                final ObjectMapper mapper = getObjectMapper(content);

                OpenAPIWorkflow openAPIWorkflow = mapper.readValue(content, OpenAPIWorkflow.class);

                result.setOpenAPIWorkflow(openAPIWorkflow);

                if(options != null && options.isApplyValidation()) {
                    OpenAPIWorkflowValidatorResult validatorResult = new OpenAPIWorkflowValidator(openAPIWorkflow).validate();
                    result.setValid(validatorResult.isValid());
                    result.setErrors(validatorResult.getErrors());
                }

                new OperationBinder().bind(openAPIWorkflow, result.getLocation());

                new WorkflowBinder().bind(openAPIWorkflow);

            } catch (Exception e) {
                LOGGER.error(e.getMessage(), e);
                result.setValid(false);
                result.addError(e.getMessage());
            }


        } catch (Exception e) {
            LOGGER.error(e.getMessage(), e);
            result.setValid(false);
        }

        return result;
    }

    OpenAPIWorkflowParserResult.Format getFormat(String content) {
        if (content.trim().startsWith("{")) {
            return OpenAPIWorkflowParserResult.Format.JSON;
        } else {
            return OpenAPIWorkflowParserResult.Format.YAML;
        }
    }

    private ObjectMapper getObjectMapper(String content) {
        ObjectMapper objectMapper = null;
        if (content.trim().startsWith("{")) {
            objectMapper = new ObjectMapper();
        } else {
            objectMapper = new ObjectMapper(new YAMLFactory());
        }

        objectMapper.setSerializationInclusion(JsonInclude.Include.NON_NULL);
        objectMapper.configure(SerializationFeature.FAIL_ON_EMPTY_BEANS, false);
        objectMapper.configure(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS, false);
        objectMapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);

        return objectMapper;
    }

}
