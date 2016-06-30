package com.intellectualsites.configurable.exception;

public class ConfigurationFactoryException extends RuntimeException {

    public ConfigurationFactoryException(String error, Throwable throwable) {
        super ("ConfigurationFactory generated an exception: " + error, throwable);
    }

    public ConfigurationFactoryException(Class<?> clazz, String error) {
        super (clazz.getName() + " generated exception: " + error);
    }

    public ConfigurationFactoryException(Class<?> clazz, String error, Throwable throwable) {
        super (clazz.getName() + " generated exception: " + error, throwable);
    }

}
