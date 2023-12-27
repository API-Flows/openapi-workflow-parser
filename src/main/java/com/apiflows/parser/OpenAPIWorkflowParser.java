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

    public OpenAPIWorkflowParserResult parse(String location) {

        OpenAPIWorkflowParserResult result = new OpenAPIWorkflowParserResult();

        PathUtil pathUtil = new PathUtil();
        HttpUtil httpUtil = new HttpUtil();

        try {
            String content;

            if (httpUtil.isUrl(location)) {
                content = httpUtil.call(location);
            } else {
                content = pathUtil.getFromFile(location);
            }

            final ObjectMapper mapper = getObjectMapper(content);

            OpenAPIWorkflow openAPIWorkflow = mapper.readValue(content, OpenAPIWorkflow.class);
            openAPIWorkflow.setLocation(location);

            result.setOpenAPIWorkflow(openAPIWorkflow);

            new OpenAPIWorkflowValidator().validate(openAPIWorkflow);

            new OperationBinder().bind(openAPIWorkflow);

            new WorkflowBinder().bind(openAPIWorkflow);

        } catch (Exception e) {
            LOGGER.error(e.getMessage(), e);
            result.setValid(false);
        }

        return result;
    }




    private ObjectMapper getObjectMapper(String content) {
        ObjectMapper objectMapper = null;
        if (content.trim().startsWith("{")) {
            objectMapper =  new ObjectMapper();
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
