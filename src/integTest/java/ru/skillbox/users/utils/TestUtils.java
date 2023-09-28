package ru.skillbox.users.utils;


import com.fasterxml.jackson.databind.ObjectMapper;

public abstract class TestUtils {

    protected static final ObjectMapper MAPPER = new ObjectMapper();

    public static <T> T stringToObject(String str, Class<T> clazz) throws Exception {
        return MAPPER.readValue(str, clazz);
    }
}