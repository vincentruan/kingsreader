package com.github.springtg.bot.platform.client.utils;

import com.fasterxml.jackson.databind.JavaType;
import com.fasterxml.jackson.databind.type.TypeFactory;

import java.util.List;
import java.util.Map;

/**
 * @author Sergey Kuptsov
 * @since 21/01/2016
 */
public class JavaTypeUtils {

    private static final TypeFactory TYPE_FACTORY = TypeFactory.defaultInstance();

    public static JavaType listTypeOf(Class<?> parameterClass) {
        return TYPE_FACTORY.constructCollectionType(List.class, parameterClass);
    }

    public static JavaType simpleTypeOf(Class<?> parameterClass) {
        return TYPE_FACTORY.constructType(parameterClass);
    }

    public static JavaType mapTypeOf(Class<?> keyClass, Class<?> valueClass) {
        return TYPE_FACTORY.constructMapType(Map.class, keyClass, valueClass);
    }

}
