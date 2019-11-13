package com.telekom.m2m.cot.restsdk.util;

import com.google.gson.JsonElement;

abstract public class ObjectMapper<T> {
    public abstract T apply(JsonElement jsonElement);
}
