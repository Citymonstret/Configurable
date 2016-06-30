package com.intellectualsites.configurable.annotations;

import com.intellectualsites.configurable.ConfigurationFactory;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * <p>
 *     Sections allow you to separate values, and make them easier
 *     to work with. Sections are created as inner classes
 * </p>
 * <p>
 *     If the inner class isn't static, you have to declare a
 *     variable with the same name with the instance you want to
 *     use, in the parent class (see example below). If the inner
 *     class is static, however, then this isn't necessary
 * </p>
 * <pre><code>
 *   &#64;Configuration
 *   public class Foo {
 *
 *       // Use this to access the section
 *       public final Bar inner = new Bar();
 *
 *        &#64;ConfigSection(name = bar)
 *        public class Bar {
 *
 *       }
 * }
 * </code></pre>
 */
@Target(ElementType.TYPE)
@Retention(RetentionPolicy.RUNTIME)
@SuppressWarnings("unused")
public @interface ConfigSection {

    /**
     * The name of the section

     * @return The name of the section, defaults to {@link ConfigurationFactory#DEFAULT_CONFIG_NAME}
     */
    String name() default ConfigurationFactory.DEFAULT_CONFIG_NAME;
}