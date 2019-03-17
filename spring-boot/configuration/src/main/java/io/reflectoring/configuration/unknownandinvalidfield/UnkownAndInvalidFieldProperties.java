package io.reflectoring.configuration.unknownandinvalidfield;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

@Component
@ConfigurationProperties(
        prefix = "myapp.unknown-and-invalid-field-module",
        ignoreUnknownFields = false,
        ignoreInvalidFields = true)
class UnkownAndInvalidFieldProperties {

  private Boolean enabled;

  public Boolean getEnabled() {
    return enabled;
  }

  public void setEnabled(Boolean enabled) {
    this.enabled = enabled;
  }
}
