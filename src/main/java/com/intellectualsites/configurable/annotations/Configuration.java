package com.intellectualsites.configurable.annotations;

import com.intellectualsites.configurable.ConfigurationFactory;
import com.intellectualsites.configurable.ConfigurationImplementation;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * This is used to declare a configuration file
 */
@Target(ElementType.TYPE)
@Retention(RetentionPolicy.RUNTIME)
@SuppressWarnings("unused")
public @interface Configuration {

    /**
     * The name of the configuration file

     * @return The name of the configuration file, defaults to {@link ConfigurationFactory#DEFAULT_CONFIG_NAME}
     */
    String name() default ConfigurationFactory.DEFAULT_CONFIG_NAME;

    /**
     * The implementation type used for the configuration file
     *
     * @return The implemention, defaults to {@link ConfigurationImplementation#JSON}
     */
    ConfigurationImplementation implementation() default ConfigurationImplementation.JSON;
}
