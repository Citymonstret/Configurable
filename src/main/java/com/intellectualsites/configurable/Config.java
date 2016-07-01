package com.intellectualsites.configurable;

import com.intellectualsites.configurable.annotations.Configuration;
import com.intellectualsites.configurable.reflection.IField;
import lombok.Getter;
import lombok.NonNull;

import java.io.File;
import java.util.Map;
import java.util.Optional;

/**
 * A configuration file, generated from {@link ConfigurationFactory}
 *
 * <pre><code>
 *     Config&lt;Foo&gt; config = ConfigurationFactory.from(Foo.class);
 * </code></pre>
 *
 * @param <T> The class used in the config
 */
public abstract class Config<T> extends ConfigurationBase {

    @Getter private final Class<T> clazz;
    private final Map<String, IField<T>> fields;
    private final T instance;
    @Getter private final String name;

    protected Config(String name, Class<T> clazz, T instance, Map<String, IField<T>> fields) {
        this.clazz = clazz;
        this.fields = fields;
        this.instance = instance;
        this.name = name;
        for (final Map.Entry<String, IField<T>> fieldEntry : fields.entrySet()) {
            try {
                setDefault(fieldEntry.getKey(), fieldEntry.getValue().getValue());
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    protected Config(T instance) {
        this.clazz = null;
        this.fields = null;
        this.instance = instance;
        this.name = null;
    }

    protected abstract void readInternal(File file);
    protected abstract void saveInternal(File file);

    public abstract ConfigurationImplementation getImplementation();

    /**
     * Read into the current config, the file name
     * is automatically generated in the {@link ConfigurationFactory}
     * from the {@link Configuration}
     * annotation
     *
     * @param path The path to the file (the parent folder)
     * @throws Exception If something goes wrong, as we're doing
     *      IO you will have to expect that to happen from time to time
     */
    public void read(@NonNull final File path) throws Exception {
        final File file = new File(path, name + "." + getImplementation().getFileExtension());
        boolean fileNew = false;
        if (!file.exists()) {
            if (!file.createNewFile()) {
                throw new RuntimeException("Failed to create " + path.getName() + File.separator + file.getName());
            } else {
                fileNew = true;
            }
        }
        if (!fileNew) {
            this.readInternal(file);
        }
        if (!super.containsDefaults()) {
            super.copyDefaults();
            this.saveInternal(file);
        }
    }

    @Override
    protected void updateField(String key, Object value) {
        try {
            getField(key).get().setValue(value);
        } catch (Exception e) {
            new Exception("Error for: " + key, e).printStackTrace();
        }
    }

    /**
     * Save the current file, the file name
     * is automatically generated in the {@link ConfigurationFactory}
     * from the {@link Configuration}
     * annotation
     *
     * This should rarely be used, as you can easily change the default
     * values in your implementation
     *
     * @param path The path to the file (the parent folder)
     * @throws Exception If something goes wrong, as we're doing
     *      IO you will have to expect that to happen from time to time
     */
    public void save(@NonNull final File path) throws Exception {
        final File file = new File(path, name + "." + getImplementation().getFileExtension());
        if (!file.exists() && !file.createNewFile()) {
            throw new RuntimeException("Failed to create " + path.getName() + File.separator + file.getName());
        }
        this.saveInternal(file);
    }

    /**
     * Get the instance of the {@link Configuration}
     * implementation
     *
     * @return Underlying instance
     */
    public T get() {
        return this.instance;
    }

    protected Optional<IField> getField(@NonNull final String key) {
        if (!fields.containsKey(key)) {
            return Optional.empty();
        }
        return Optional.of(fields.get(key));
    }
}
