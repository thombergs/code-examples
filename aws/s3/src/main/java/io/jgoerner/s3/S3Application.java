package io.jgoerner.s3;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.thymeleaf.ThymeleafProperties;
import org.springframework.cloud.aws.autoconfigure.context.ContextInstanceDataAutoConfiguration;
import org.springframework.context.annotation.Bean;
import org.thymeleaf.templateresolver.FileTemplateResolver;
import org.thymeleaf.templateresolver.ITemplateResolver;

@SpringBootApplication(exclude = ContextInstanceDataAutoConfiguration.class)
public class S3Application {

  private final ThymeleafProperties properties;

  @Value("${spring.thymeleaf.templates_root:}")
  private String templatesRoot;

  public S3Application(ThymeleafProperties properties) {
    this.properties = properties;
  }

  public static void main(String[] args) {
    SpringApplication.run(S3Application.class, args);
  }

  @Bean
  public ITemplateResolver defaultTemplateResolver() {
    FileTemplateResolver resolver = new FileTemplateResolver();
    resolver.setSuffix(properties.getSuffix());
    resolver.setPrefix(templatesRoot);
    resolver.setTemplateMode(properties.getMode());
    resolver.setCacheable(properties.isCache());
    return resolver;
  }

}
