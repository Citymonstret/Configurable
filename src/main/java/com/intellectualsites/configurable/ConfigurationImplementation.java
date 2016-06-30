package com.intellectualsites.configurable;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public enum ConfigurationImplementation {
    JSON("json"),
    YAML("yml");
    
    @Getter
    private final String fileExtension;
}
