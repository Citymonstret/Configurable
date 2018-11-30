package com.intellectualsites.configurable.implementation;

import com.intellectualsites.configurable.Config;
import com.intellectualsites.configurable.ConfigurationImplementation;
import com.intellectualsites.configurable.ConfigurationSection;
import com.intellectualsites.configurable.reflection.IField;
import com.moandjiezana.toml.Toml;
import com.moandjiezana.toml.TomlWriter;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import lombok.NonNull;

/**
 * A TOML implementation of {@link Config}
 * {@inheritDoc}
 */
public final class TomlConfig<T> extends Config<T> {

  public TomlConfig(String name, Class<T> clazz, T instance, Map<String, IField<T>> fields) {
    super(name, clazz, instance, fields);
  }

  @Override
  protected void readInternal(@NonNull final File file) {
    try (final FileInputStream inputStream = new FileInputStream(file)) {
      final Toml toml = new Toml().read(inputStream);
      for (final Map.Entry<String, Object> entry : toml.entrySet()) {
        final Object value = entry.getValue();
        if (value instanceof Map) {
          final Map<?, ?> map = (Map) value;
          for (Map.Entry innerEntry : map.entrySet()) {
            set(entry.getKey() + "." + innerEntry.getKey(), innerEntry.getValue());
          }
        } else {
          if (!getField(entry.getKey()).isPresent()) {
            System.out.println("Skipping non-defined key: " + entry.getKey());
            continue;
          }
          set(entry.getKey(), entry.getValue());
        }
      }
    } catch (final Exception e) {
      e.printStackTrace();
    }
  }

  @Override
  protected void saveInternal(@NonNull final File file) {
    final TomlWriter tomlWriter = new TomlWriter();
    try {
      tomlWriter.write(fixMap(), file);
    } catch (final IOException e) {
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
    return ConfigurationImplementation.TOML;
  }

}
