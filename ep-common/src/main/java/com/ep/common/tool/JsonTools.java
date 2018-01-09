package com.ep.common.tool;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonElement;
import com.google.gson.JsonParser;

/**
 * Created by jw on 2017/3/15.
 */
public class JsonTools {

    private static Gson gson = new GsonBuilder().create();

    private static JsonParser jsonParser = new JsonParser();

    public static String encode(Object obj) {
        return gson.toJson(obj);
    }

    public static <T> T decode(String json, Class<T> classOfT) {
        return gson.fromJson(json, classOfT);
    }

    public static <T> T decode(JsonElement jsonElement, Class<T> classOfT) {

        return gson.fromJson(jsonElement, classOfT);
    }

}
