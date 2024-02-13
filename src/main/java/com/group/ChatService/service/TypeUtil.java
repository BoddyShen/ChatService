package com.group.ChatService.service;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.group.ChatService.constants.ProfileConstants;
import org.bson.types.ObjectId;

import java.lang.reflect.Field;
import java.lang.reflect.Type;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;


public class TypeUtil {
    public static ObjectId objectIdConverter(String id) {
        return new ObjectId(id);
    }

    public static List<String> jsonStringArray(String jsonArray) {
        Gson converter = new Gson();
        Type type = new TypeToken<List<String>>(){}.getType();
        return converter.fromJson(jsonArray, type);
    }

    public static List<ObjectId> objectIdArray(String jsonArray) {
        List<String> stringList = jsonStringArray(jsonArray);
        List<ObjectId> objIds = new ArrayList<>();

        for (String element: stringList) {
            objIds.add(objectIdConverter(element));
        }

        return objIds;
    }


    public static void setField(Object object, Field field, Object value) {
        try {
            field.setAccessible(true);
            Class<?> fieldType = field.getType();

            if (fieldType.equals(Integer.class) || fieldType.equals(int.class)) {
                field.set(object, Integer.parseInt(value.toString()));
            } else if (fieldType.equals(LocalDate.class)) {
                field.set(object, DateUtil.dateFormatter(value.toString())); // string to localdate
            } else if (fieldType.equals(ObjectId.class)) {
                field.set(object, objectIdArray(value.toString()));
            } else {
                field.set(object, value);
            }
        } catch (IllegalAccessException e) {
            throw new RuntimeException("Error setting field: " + field.getName(), e);
        }
    }


    public static Integer getGender(String gender) {
        return ProfileConstants.Gender.valueOf(gender.toUpperCase()).ordinal();
    }


    public static List<String> objectToListString(Object object) {
        return new ObjectMapper().convertValue(object, new TypeReference<List<String>>() {});
    }
}
