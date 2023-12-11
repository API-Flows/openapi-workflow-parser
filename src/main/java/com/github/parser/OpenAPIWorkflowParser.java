package com.github.parser;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.dataformat.yaml.YAMLFactory;
import com.github.model.OpenAPIWorkflow;
import com.github.parser.source.OperationBinder;
import com.github.parser.source.WorkflowBinder;
import com.github.parser.util.HttpUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;

public class OpenAPIWorkflowParser {

    private static final Logger LOGGER = LoggerFactory.getLogger(OpenAPIWorkflowParser.class);

    public OpenAPIWorkflowParserResult parse(String location) {

        OpenAPIWorkflowParserResult result = new OpenAPIWorkflowParserResult();

        try {
            String content;

            if (isUrl(location)) {
                content = getFromUrl(location);
            } else {
                content = getFromFile(location);
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

    boolean isUrl(String url) {
        return url != null && url.startsWith("http");
    }
    String getFromUrl(String url) {
        return new HttpUtil().call(url);
    }

    String getFromFile(String filepath) {
        String content;
        try {
            content = new String(Files.readAllBytes(Paths.get(filepath)));
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

        return content;
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
