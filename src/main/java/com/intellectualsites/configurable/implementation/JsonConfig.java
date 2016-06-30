package com.intellectualsites.configurable.implementation;

import com.cedarsoftware.util.io.JsonObject;
import com.cedarsoftware.util.io.JsonReader;
import com.cedarsoftware.util.io.JsonWriter;
import com.google.common.collect.ImmutableMap;
import com.intellectualsites.configurable.Config;
import com.intellectualsites.configurable.ConfigurationImplementation;
import com.intellectualsites.configurable.ConfigurationSection;
import com.intellectualsites.configurable.reflection.IField;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.util.HashMap;
import java.util.Map;

/**
 * A JSON implementation of {@link Config}
 * {@inheritDoc}
 */
public final class JsonConfig<T> extends Config<T> {

    public JsonConfig(String name, Class<T> clazz, T instance, ImmutableMap<String, IField<T>> fields) {
        super(name, clazz, instance, fields);
    }

    @Override
    public void readInternal(File file) {
        try (FileInputStream inputStream = new FileInputStream(file)) {
            JsonReader jsonReader = new JsonReader(inputStream);
            JsonObject object = (JsonObject) jsonReader.readObject();
            for (Object key : object.keySet()) {
                Object value = object.get(key);
                if (value instanceof Map) {
                    Map<?, ?> map = (Map) value;
                    for (Map.Entry entry : map.entrySet()) {
                        set(key + "." + entry.getKey(), entry.getValue());
                    }
                } else {
                    if (!getField(key.toString()).isPresent()) {
                        System.out.println("Skipping non-defined key: " + key);
                        continue;
                    }
                    set(key.toString(), object.get(key));
                }
            }
        } catch (final Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public void saveInternal(File file) {
        final JsonObject<String, Object> jsonObject = new JsonObject<>();
        jsonObject.putAll(fixMap());
        try (FileOutputStream outputStream = new FileOutputStream(file)) {
            Map<String, Object> args = new HashMap<>();
            args.put(JsonWriter.PRETTY_PRINT, true);
            JsonWriter writer = new JsonWriter(outputStream, args);
            writer.write(jsonObject);
        } catch (final Exception e) {
            e.printStackTrace();
        }
    }

    private Map<? extends String,?> fixMap() {
        Map<String, Object> map = new HashMap<>();
        for (Map.Entry<String, Object> entry : internalMap.entrySet()) {
            if (entry.getValue() instanceof ConfigurationSection) {
                map.put(entry.getKey(), ((ConfigurationSection) entry.getValue()).map());
            } else {
                map.put(entry.getKey(), entry.getValue());
            }
        }
        return map;
    }

    @Override
    public ConfigurationImplementation getImplementation() {
        return ConfigurationImplementation.JSON;
    }

}
