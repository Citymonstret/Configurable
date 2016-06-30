package com.intellectualsites.configurable.implementation;

import com.intellectualsites.configurable.Config;
import com.intellectualsites.configurable.ConfigurationImplementation;
import com.intellectualsites.configurable.ConfigurationSection;
import com.intellectualsites.configurable.reflection.IField;
import org.yaml.snakeyaml.DumperOptions;
import org.yaml.snakeyaml.Yaml;
import org.yaml.snakeyaml.representer.Representer;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileWriter;
import java.util.HashMap;
import java.util.Map;

/**
 * A JSON implementation of {@link Config}
 * {@inheritDoc}
 */
public final class YamlConfig<T> extends Config<T> {

    private final DumperOptions dumperOptions = new DumperOptions();
    private final Representer representer = new Representer();
    private final Yaml yaml = new Yaml(representer, dumperOptions);

    public YamlConfig(String name, Class<T> clazz, T instance, Map<String, IField<T>> fields) {
        super(name, clazz, instance, fields);
        this.dumperOptions.setIndent(2);
        this.dumperOptions.setDefaultFlowStyle(DumperOptions.FlowStyle.BLOCK);
        this.dumperOptions.setAllowUnicode(true);
        this.representer.setDefaultFlowStyle(DumperOptions.FlowStyle.BLOCK);
    }

    @Override
    public void readInternal(File file) {
        try (FileInputStream inputStream = new FileInputStream(file)) {
            Map<String, ?> map = (Map<String, ?>) yaml.load(inputStream);
            for (String key : map.keySet()) {
                Object value = map.get(key);
                if (value instanceof Map) {
                    Map<?, ?> smap = (Map) value;
                    for (Map.Entry entry : smap.entrySet()) {
                        set(key + "." + entry.getKey(), entry.getValue());
                    }
                } else {
                    if (!getField(key).isPresent()) {
                        System.out.println("Skipping non-defined key: " + key);
                        continue;
                    }
                    set(key, map.get(key));
                }
            }
        } catch (final Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public void saveInternal(File file) {
        try (FileWriter outputStream = new FileWriter(file)) {
            yaml.dump(fixMap(), outputStream);
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
        return ConfigurationImplementation.YAML;
    }

}