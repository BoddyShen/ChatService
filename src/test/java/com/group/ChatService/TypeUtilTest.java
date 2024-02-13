package com.group.ChatService;
import org.junit.jupiter.api.Test;

import com.group.ChatService.service.TypeUtil;

import static org.junit.jupiter.api.Assertions.assertEquals;
import java.util.List;

public class TypeUtilTest {

    @Test
    public void testJsonStringArray_WithValidJsonArray_ReturnsList() {
        // Arrange
        String jsonArray = "[\"apple\", \"banana\", \"orange\"]";

        // Act
        List<String> result = TypeUtil.jsonStringArray(jsonArray);

        // Assert
        assertEquals(3, result.size());
        assertEquals("apple", result.get(0));
        assertEquals("banana", result.get(1));
        assertEquals("orange", result.get(2));
    }

    @Test
    public void testJsonStringArray_WithEmptyJsonArray_ReturnsEmptyList() {
        // Arrange
        String jsonArray = "[]";

        // Act
        List<String> result = TypeUtil.jsonStringArray(jsonArray);

        // Assert
        assertEquals(0, result.size());
    }
}