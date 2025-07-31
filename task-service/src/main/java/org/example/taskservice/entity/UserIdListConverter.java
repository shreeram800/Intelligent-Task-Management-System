package org.example.taskservice.entity;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.persistence.AttributeConverter;
import jakarta.persistence.Converter;

import java.io.IOException;
import java.util.List;

@Converter
public class UserIdListConverter implements AttributeConverter<List<Long>, String> {

    private final ObjectMapper objectMapper = new ObjectMapper();

    @Override
    public String convertToDatabaseColumn(List<Long> userIds) {
        try {
            return objectMapper.writeValueAsString(userIds);
        } catch (JsonProcessingException e) {
            throw new RuntimeException("Error converting list to JSON", e);
        }
    }

    @Override
    public List<Long> convertToEntityAttribute(String json) {
        try {
            return objectMapper.readValue(json, new TypeReference<List<Long>>() {});
        } catch (IOException e) {
            throw new RuntimeException("Error converting JSON to list", e);
        }
    }
}
