package io.refactoring.http5.client.example.config;

import lombok.NonNull;
import org.apache.commons.configuration2.Configuration;
import org.apache.commons.configuration2.FileBasedConfiguration;
import org.apache.commons.configuration2.PropertiesConfiguration;
import org.apache.commons.configuration2.builder.FileBasedConfigurationBuilder;
import org.apache.commons.configuration2.builder.fluent.Parameters;
import org.apache.commons.configuration2.ex.ConfigurationException;
import org.apache.commons.configuration2.io.ClasspathLocationStrategy;

/** Utility to load configurations. */
public class ConfigurationUtils {
  private Configuration config;

  @NonNull
  private Configuration getConfiguration() {
    if (config == null) {
      try {
        final FileBasedConfigurationBuilder<FileBasedConfiguration> builder =
            new FileBasedConfigurationBuilder<FileBasedConfiguration>(PropertiesConfiguration.class)
                .configure(
                    new Parameters()
                        .properties()
                        .setLocationStrategy(new ClasspathLocationStrategy())
                        .setFileName("application-test.properties"));
        config = builder.getConfiguration();
      } catch (ConfigurationException e) {
        throw new RuntimeException("Failed to load properties into configuration.", e);
      }
    }
    return config;
  }

  /**
   * Gets string property.
   *
   * @param key property key
   * @return property value
   * @throws NullPointerException if {@code key} is null
   */
  public String getString(@NonNull final String key) {
    return getConfiguration().getString(key);
  }

  /**
   * Gets long property.
   *
   * @param key property key
   * @return property value
   * @throws NullPointerException if {@code key} is null
   */
  public long getLong(@NonNull final String key) {
    return getConfiguration().getLong(key);
  }
}
