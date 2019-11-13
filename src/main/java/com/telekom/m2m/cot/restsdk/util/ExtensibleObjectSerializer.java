package com.telekom.m2m.cot.restsdk.util;

import com.google.gson.*;
import com.telekom.m2m.cot.restsdk.inventory.ManagedObject;
import com.telekom.m2m.cot.restsdk.users.DevicePermission;

import java.lang.reflect.Type;
import java.util.*;

/**
 * Created by Patrick Steinert on 31.01.16.
 */
public class ExtensibleObjectSerializer implements JsonSerializer<ExtensibleObject>, JsonDeserializer<ExtensibleObject> {

    @Override
    public JsonElement serialize(ExtensibleObject src, Type typeOfSrc,
                                 JsonSerializationContext context) {
        if (src == null) {
            return null;
        }

        JsonObject object = new JsonObject();
        Map<String, Object> attributes = src.getAttributes();
        for (Map.Entry<String, Object> entry : attributes.entrySet()) {
            String key = entry.getKey();
            Object value = entry.getValue();
            if ("source".equals(key) && (value instanceof ManagedObject)) {
                JsonPrimitive primitive = new JsonPrimitive(((ManagedObject) value).getId());
                JsonObject sourceObject = new JsonObject();
                sourceObject.add("id", primitive);
                object.add(key, sourceObject);
                continue;
            }
            // devicePermissions contain a list of DevicePermission objects which are not extending ExtensibleObject class
            // and should be handled separately
            if("devicePermissions".equals(key)) {
                JsonObject sourceObject = new JsonObject();
                for (Map.Entry<String, List<DevicePermission>> permissionsEntry : ((Map<String, List<DevicePermission>>)value).entrySet()) {
                    JsonArray permissions = new JsonArray();
                    for(DevicePermission permission : permissionsEntry.getValue()) {
                        JsonPrimitive jsonPermission = new JsonPrimitive(permission.toString());
                        permissions.add(jsonPermission);
                    }
                    sourceObject.add(permissionsEntry.getKey(), permissions);
                }
                object.add(key, sourceObject);
                continue;
            }
            object.add(key, context.serialize(value));
        }
        return object;

    }

    @Override
    public ExtensibleObject deserialize(JsonElement jsonElement, Type type, JsonDeserializationContext jsonDeserializationContext) throws JsonParseException {

        JsonObject object = jsonElement.getAsJsonObject();
        ExtensibleObject mo = new ExtensibleObject();

        Iterator<Map.Entry<String, JsonElement>> objectElementIterator = object.entrySet().iterator();
        while (objectElementIterator.hasNext()) {
            Map.Entry<String, JsonElement> element = objectElementIterator.next();
            String key = element.getKey();
            JsonElement value = element.getValue();

            try {
                Class foundClass = Class.forName(key.replace('_', '.'));
                if (foundClass != null) {
                    mo.set(key, jsonDeserializationContext.deserialize(value, foundClass));
                    continue;
                }
            } catch (ClassNotFoundException e) {
            }

            JsonPrimitive tmp;

            if (value.isJsonPrimitive()) {
                tmp = (JsonPrimitive) value;
                Object converted = null;
                if (tmp.isBoolean()) {
                    converted = tmp.getAsBoolean();
                } else if (tmp.isString()) {
                    try {
                        String tmpString = tmp.getAsString();
                        // in the CoT plattform the stored date time objects has different formatted time zones
                        switch(tmpString.length()) {
                            case 24:
                                // e.g. 2017-09-05T17:19:32.601Z
                            case 26:
                                // e.g. 2017-09-05T17:19:32.601+02
                                converted = DateTimeUtil.convertStringToDate(tmp.getAsString());
                                break;
                            case 28:
                                // e.g. 2017-09-05T17:19:32.601+0200
                                converted = DateTimeUtil.convertStringToDate(tmp.getAsString());
                                break;
                            case 29:
                                // e.g. 2017-09-05T17:19:32.601+02:00
                                converted = DateTimeUtil.convertStringToDate(tmp.getAsString());
                                break;
                            default:
                                converted = tmp.getAsString();
                        }
                    } catch (IllegalArgumentException e) {
                        converted = tmp.getAsString();
                    }

                } else if (tmp.isNumber()) {
                    converted = tmp.getAsNumber();
                }
                mo.set(key, converted);
            } else if (value.isJsonObject()) {
                // Special case for User to avoid crappy nested ExtensibleObjects...
                if (key.equals("devicePermissions")) {
                    mo.set(key, deserializeDevicePermissions((JsonObject)value));
                } else {
                    mo.set(key, jsonDeserializationContext.deserialize(value, type));
                }
            } else if (value.isJsonArray()) {
                // Some of the library fragments are arrays, but they don't need special treatment because all
                // fragments are stored as simple JsonElements in the ExtensibleObject, and not as themselves.
                // We just list them for documentation purposes, in case someone wants to change that in the future.
                switch (key) {
                    case "c8y_SoftwareList":
                    case "c8y_SupportedOperations":
                    default:
                        mo.set(key, value);
                }
            }
        }

        return mo;
    }


    /**
     * Special method to deserialize the devicePermissions of a User which look like this:
     * {"deviceId" : ["*:*:*", "ALARM:*:READ"], "deviceId" : ["*:*:*"]}
     */
    private Map<String, List<DevicePermission>> deserializeDevicePermissions(JsonObject object) {
        Map<String, List<DevicePermission>> permissions = new HashMap<>();
        for (Map.Entry<String, JsonElement> entry : object.entrySet()) {
            List<DevicePermission> devicePermissions = new ArrayList<>();
            JsonArray value = (JsonArray)entry.getValue();
            for (JsonElement permission : value) {
                devicePermissions.add(new DevicePermission(permission.getAsString()));
            }
            permissions.put(entry.getKey(), devicePermissions);
        }
        return permissions;
    }
}
