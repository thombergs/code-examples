package io.reflectoring.configuration.unknownfield;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

@Component
@ConfigurationProperties(prefix = "myapp.unknownfield", ignoreUnknownFields = false)
class UnkownFieldProperties {

  private Boolean enabled = Boolean.TRUE;

  public Boolean getEnabled() {
    return enabled;
  }

  public void setEnabled(Boolean enabled) {
    this.enabled = enabled;
  }
}
