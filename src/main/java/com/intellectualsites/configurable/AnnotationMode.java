package com.intellectualsites.configurable;

public enum AnnotationMode {

    /**
     * {@link com.intellectualsites.configurable.annotations.ConfigValue} is required
     * for each field that is supposed to be configurable
     */
    REQUIRED,

    /**
     * {@link com.intellectualsites.configurable.annotations.ConfigValue} isn't required.
     * Make fields transient to exclude them from the config
     */
    NOT_REQUIRED,

    /**
     * The AnnotationMode will be inherited from the parent {@link com.intellectualsites.configurable.annotations.Configuration},
     * can only be used on {@link com.intellectualsites.configurable.annotations.ConfigSection}
     */
    INHERIT
}
