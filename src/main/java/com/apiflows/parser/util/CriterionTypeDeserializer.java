package com.apiflows.parser.util;

import com.apiflows.model.CriterionExpressionType;
import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.deser.std.StdDeserializer;

import java.io.IOException;

public class CriterionTypeDeserializer extends StdDeserializer<Object> {

    public CriterionTypeDeserializer() {
        super(Object.class);
    }

    @Override
    public Object deserialize(JsonParser p, DeserializationContext ctx) throws IOException {
        JsonNode node = p.getCodec().readTree(p);
        if (node.isTextual()) {
            return node.asText();
        }
        if (node.isObject()) {
            CriterionExpressionType expressionType = new CriterionExpressionType();
            if (node.has("type")) {
                expressionType.setType(node.get("type").asText());
            }
            if (node.has("version")) {
                expressionType.setVersion(node.get("version").asText());
            }
            return expressionType;
        }
        return null;
    }
}
