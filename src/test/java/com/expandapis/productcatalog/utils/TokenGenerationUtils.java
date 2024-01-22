package com.expandapis.productcatalog.utils;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.io.IOException;

public class TokenGenerationUtils {

    public static String extractTokenFromResponse(String response) throws IOException {
        ObjectMapper objectMapper = new ObjectMapper();
        JsonNode jsonNode = objectMapper.readTree(response);
        return jsonNode.get("accessToken").asText();
    }

    public static String asJsonString(Object obj) throws Exception {
        return new ObjectMapper().writeValueAsString(obj);
    }
}
