package com.intellectualsites.configurable;

import com.intellectualsites.configurable.annotations.ConfigSection;
import lombok.RequiredArgsConstructor;

/**
 * See {@link ConfigSection}
 */
@RequiredArgsConstructor
public class ConfigurationSection extends ConfigurationBase {

    private final ConfigurationBase owner;
    private final String name;

    @Override
    protected void updateField(String key, Object value) {
        owner.updateField(name + "." + key, value);
    }

}
