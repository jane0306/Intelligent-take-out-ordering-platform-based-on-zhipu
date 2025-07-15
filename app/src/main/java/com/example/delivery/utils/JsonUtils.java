package com.example.delivery.utils;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.ToNumberPolicy;
import com.google.gson.reflect.TypeToken;

/**
 * Json转换工具类
 */
public class JsonUtils {
    /**
     * 将对象序列化成json字符串
     *
     * @param object javaBean
     * @return jsonString json字符串
     */
    public static String toJson(Object object) {
        return getInstance().toJson(object);
    }

    /**
     * 将json反序列化成对象
     *
     * @param json      jsonString
     * @param valueType class
     * @param <T>       T 泛型标记
     * @return Bean
     */
    public static <T> T parse(String json, Class<T> valueType) {
        return getInstance().fromJson(json, valueType);
    }

    /**
     * 将json array反序列化为对象
     */
    public static <T> T parse(String json, TypeToken<T> typeToken) {
        return getInstance().fromJson(json, typeToken.getType());
    }

    private static Gson getInstance() {
        return GsonHolder.instance;
    }

    private static class GsonHolder {
        private static final Gson instance;

        static {
            instance = new GsonBuilder()
                    .setDateFormat("yyyy-MM-dd HH:mm:ss")
                    .setObjectToNumberStrategy(ToNumberPolicy.LAZILY_PARSED_NUMBER)
                    .serializeNulls()
                    .create();

        }
    }

}
