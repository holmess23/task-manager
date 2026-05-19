package tasks.manager.model.converter;


import java.util.Arrays;
import java.util.List;

import jakarta.persistence.AttributeConverter;

public class StringListConverter implements AttributeConverter<List<String>, String> {

    @Override
    public String convertToDatabaseColumn(List<String> attribute) {
        if (attribute == null || attribute.isEmpty()) {
            return "";
        }
        return String.join(",", attribute);
    }

    @Override
    public List<String> convertToEntityAttribute(String dbData) {
        if(dbData == null || dbData.isEmpty()) {
            return List.of();
        }
        return Arrays.asList(dbData.split(","));
    }

    
}