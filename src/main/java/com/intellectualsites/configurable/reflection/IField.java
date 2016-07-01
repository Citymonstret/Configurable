package com.intellectualsites.configurable.reflection;

import lombok.Getter;
import lombok.NonNull;

import java.lang.reflect.Field;
import java.lang.reflect.Modifier;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Optional;

public class IField<T> {

    @Getter
    private final Class<? extends T> fieldClass;

    @Getter
    private final Collection<FieldProperty> fieldProperties;

    private T instance;
    private String name;

    public IField(@NonNull final Class<? extends T> clazz) {
        this.fieldClass = clazz;
        this.fieldProperties = new ArrayList<>();
    }

    public IField<T> withProperty(@NonNull final FieldProperty property) {
        this.fieldProperties.add(property);
        return this;
    }

    public IField<T> withProperties(@NonNull final FieldProperty ... properties) {
        this.fieldProperties.addAll(Arrays.asList(properties));
        return this;
    }

    public IField<T> fromInstance(T instance) {
        this.instance = instance;
        return this;
    }

    public IField<T> named(@NonNull final String name) {
        this.name = name;
        return this;
    }


    public Field getField() throws Exception {
        if (!getInstance().isPresent() && !fieldProperties.contains(FieldProperty.STATIC)) {
            throw new RuntimeException("Cannot access static field, that isn't static (Missing FieldProperty: STATIC)");
        }
        Field field = fieldClass.getDeclaredField(name);
        if (fieldProperties.contains(FieldProperty.ACCESS_GRANT)) {
            field.setAccessible(true);
        }
        return field;
    }

    public Object getValue() throws Exception {
        if (fieldProperties.contains(FieldProperty.STATIC)) {
            return getField().get(null);
        }
        return getField().get(this.instance);
    }

    public IField<T> setValue(Object newValue) throws Exception {
        Field field = getField();
        /*
        Disabled for now, as it doesn't always work (when values are inlined)
        if (fieldProperties.contains(FieldProperty.CONSTANT)) {
            Field modifiers = field.getClass().getDeclaredField("modifiers");
            modifiers.setAccessible(true);
            modifiers.setInt(field, field.getModifiers() & ~Modifier.FINAL);
        } */
        if (!Modifier.isStatic(field.getModifiers())) {
            field.set(instance, newValue);
        } else {
            field.set(null, newValue);
        }
        return this;
    }

    public Optional<T> getInstance() {
        return Optional.ofNullable(this.instance);
    }

    public Optional<String> getName() {
        return Optional.ofNullable(this.name);
    }
}
