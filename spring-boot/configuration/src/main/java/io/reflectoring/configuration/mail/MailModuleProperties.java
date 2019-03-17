package io.reflectoring.configuration.mail;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;

import java.time.Duration;
import java.util.List;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.context.properties.DeprecatedConfigurationProperty;
import org.springframework.util.unit.DataSize;
import org.springframework.validation.annotation.Validated;

@ConfigurationProperties(prefix = "myapp.mail", ignoreInvalidFields = true, ignoreUnknownFields = false)
@Validated
class MailModuleProperties {

  @NotNull
  private Boolean enabled = Boolean.TRUE;

  @NotEmpty
  private String defaultSubject;

  @NotNull
  private Duration pauseBetweenMails;

  @NotNull
  private DataSize maxAttachmentSize;

  @NotNull
  private Weight maxAttachmentWeight;

  @NotEmpty
  private List<String> smtpServers;

  @DeprecatedConfigurationProperty(reason = "not needed anymore", replacement = "none")
  public String getDefaultSubject() {
    return defaultSubject;
  }

  public Boolean getEnabled() {
    return enabled;
  }

  public void setEnabled(Boolean enabled) {
    this.enabled = enabled;
  }

  public void setDefaultSubject(String defaultSubject) {
    this.defaultSubject = defaultSubject;
  }

  public Duration getPauseBetweenMails() {
    return pauseBetweenMails;
  }

  public void setPauseBetweenMails(Duration pauseBetweenMails) {
    this.pauseBetweenMails = pauseBetweenMails;
  }

  public DataSize getMaxAttachmentSize() {
    return maxAttachmentSize;
  }

  public void setMaxAttachmentSize(DataSize maxAttachmentSize) {
    this.maxAttachmentSize = maxAttachmentSize;
  }

  public List<String> getSmtpServers() {
    return smtpServers;
  }

  public void setSmtpServers(List<String> smtpServers) {
    this.smtpServers = smtpServers;
  }

  public Weight getMaxAttachmentWeight() {
    return maxAttachmentWeight;
  }

  public void setMaxAttachmentWeight(Weight maxAttachmentWeight) {
    this.maxAttachmentWeight = maxAttachmentWeight;
  }
}
