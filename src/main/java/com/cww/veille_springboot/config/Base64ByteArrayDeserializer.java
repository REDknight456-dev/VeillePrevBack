package com.cww.veille_springboot.config;

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JsonDeserializer;
import com.fasterxml.jackson.databind.JsonNode;

import java.io.IOException;
import java.util.Base64;

public class Base64ByteArrayDeserializer extends JsonDeserializer<byte[]> {
    @Override
    public byte[] deserialize(JsonParser jp, DeserializationContext ctxt) throws IOException {
        JsonNode node = jp.getCodec().readTree(jp);

        // If node is null or empty, return null
        if (node == null || node.isNull() || node.asText().trim().isEmpty()) {
            return null;
        }

        String base64String = node.asText().trim();

        try {
            // Direct decoding of the base64 string without any prefix handling
            return Base64.getDecoder().decode(base64String);
        } catch (IllegalArgumentException e) {
            throw new IOException("Invalid Base64 encoding: " + e.getMessage(), e);
        }
    }
}