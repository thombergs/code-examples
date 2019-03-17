package io.reflectoring.configuration.unknownfield;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;

import java.time.Duration;
import java.util.List;

import io.reflectoring.configuration.mail.Weight;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.context.properties.DeprecatedConfigurationProperty;
import org.springframework.stereotype.Component;
import org.springframework.util.unit.DataSize;
import org.springframework.validation.annotation.Validated;

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
