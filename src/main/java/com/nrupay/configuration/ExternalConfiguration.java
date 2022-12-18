package com.nrupay.configuration;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.dataformat.yaml.YAMLFactory;

import java.io.IOException;
import java.io.InputStream;

public class ExternalConfiguration {
    private static class HelperHolder {
        protected static final ExternalConfiguration externalConfig = new ExternalConfiguration();
    }

    private ConfigurationProperties configurationProperties;

    private ExternalConfiguration() {
        initConfigurationProperties();
    }

    public static ExternalConfiguration getInstance() {
        return HelperHolder.externalConfig;
    }

    public ConfigurationProperties getConfigurationProperties() {
        return configurationProperties;
    }

    private void initConfigurationProperties() {
        try {
            ObjectMapper mapper = new ObjectMapper(new YAMLFactory());
            mapper.findAndRegisterModules();
            //mapper.configure(DeserializationFeature.UNWRAP_ROOT_VALUE, true);
            InputStream inputStream = ExternalConfiguration.class.getClassLoader().getResourceAsStream("config.yaml");
            this.configurationProperties = mapper.readValue(inputStream, ConfigurationProperties.class);
        } catch (IOException exception) {
            throw new IllegalStateException("Config not initialized properly.");
        }
    }
}
