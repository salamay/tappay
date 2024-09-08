package com.tappay.service.webservice.utils;


import java.lang.reflect.Field;

public class NullFieldChecker {


    public static boolean containsNullField(Object object) throws IllegalAccessException {
        for (Field field : object.getClass().getDeclaredFields()) {
            field.setAccessible(true);
            Object value = field.get(object);
            if (value == null) {
                return true;
            }
        }
        return false;
    }
}