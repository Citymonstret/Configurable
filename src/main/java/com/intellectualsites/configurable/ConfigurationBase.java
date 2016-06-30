package com.intellectualsites.configurable;

import lombok.NonNull;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

public abstract class ConfigurationBase {

    protected final Map<String, Object> internalMap;
    private final Map<String, Object> defaults;

    private ConfigurationBase(@NonNull final Map<String, Object> mapInstance) {
        this.internalMap = mapInstance;
        this.defaults = new HashMap<>();
    }

    ConfigurationBase() {
        this(new HashMap<>());
    }

    final void setDefault(@NonNull final String key, @NonNull final Object value) {
        this.defaults.put(key, value);
    }

    final public ConfigurationSection getConfigurationSection(final String key) {
        return (ConfigurationSection) internalMap.get(key);
    }

    final public <T> Optional<T> get(final String key, @NonNull final Class<T> clazz) {
        if (key.contains(".")) {
            final String[] parts = key.split("\\.");
            if (!contains(parts[0])) {
                return Optional.empty();
            }
            return getConfigurationSection(parts[0]).get(parts[1], clazz);
        }
        if (contains(key)) {
            return Optional.of(clazz.cast(this.internalMap.get(key)));
        }
        return Optional.empty();
    }

    final public Optional<Object> get(@NonNull final String key) {
        if (key.contains(".")) {
            final String[] parts = key.split(".");
            if (!contains(parts[0])) {
                return Optional.empty();
            }
            return getConfigurationSection(parts[0]).get(parts[1]);
        }
        if (contains(key)) {
            return Optional.of(this.internalMap.get(key));
        }
        return Optional.empty();
    }

    final public boolean contains(@NonNull final String key) {
        if (key.contains(".")) {
            final String[] parts = key.split("\\.");
            if (!contains(parts[0])) {
                return false;
            }
            ConfigurationSection section = (get(parts[0], ConfigurationSection.class)).get();
            return section.contains(parts[1]);
        }
        return this.internalMap.containsKey(key);
    }

    final public boolean contains(@NonNull final String key, @NonNull final Class<?> clazz) {
        Optional<Object> optional = get(key);
        return optional.isPresent() && clazz.isInstance(clazz);
    }

    final protected void set(@NonNull String key, @NonNull final Object value) {
        if (key.contains(".")) {
            final String[] parts = key.split("\\.");
            if (!contains(parts[0])) {
                set(parts[0], new ConfigurationSection(this, parts[0]));
            }
            ConfigurationSection section = (get(parts[0], ConfigurationSection.class)).get();
            section.set(parts[1], value);
        } else {
            this.internalMap.put(key, value);
            if (value instanceof ConfigurationSection) {
                return;
            }
            this.updateField(key, value);
        }
    }

    boolean containsDefaults() {
        for (final String key : defaults.keySet()) {
            if (!contains(key)) {
                return false;
            }
        }
        return true;
    }

    protected abstract void updateField(String key, Object value);

    void copyDefaults() {
        defaults.entrySet().stream().filter(entry -> !contains(entry.getKey())).forEach(entry -> set(entry.getKey(), entry.getValue()));
    }


    public Object map() {
        return this.internalMap;
    }
}
