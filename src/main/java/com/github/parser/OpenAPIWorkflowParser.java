package com.github.parser;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.dataformat.yaml.YAMLFactory;
import com.github.model.OpenAPIWorkflow;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;

public class OpenAPIWorkflowParser {

    public OpenAPIWorkflowParserResult parse(String location) {

        OpenAPIWorkflowParserResult result = new OpenAPIWorkflowParserResult();

        try {
            String content = null;

            if (isUrl(location)) {
                content = getFromUrl(location);
            } else {
                content = getFromFile(location);
            }

            final ObjectMapper mapper = getObjectMapper(content);
            result.setOpenAPIWorkflow(mapper.readValue(content, OpenAPIWorkflow.class));

        } catch (Exception e) {
            result.setValid(false);
            throw new RuntimeException(e);
        }

        return result;
    }

    boolean isUrl(String url) {
        return url != null && url.startsWith("http");
    }
    String getFromUrl(String url) {
        return null;
    }

    String getFromFile(String filepath) {
        String content = null;
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
