package io.reflectoring.validation;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.validation.Errors;
import org.springframework.validation.Validator;
import org.springframework.validation.annotation.Validated;

import javax.validation.Valid;
import javax.validation.constraints.NotBlank;

@Validated
@ConfigurationProperties(prefix = "app.properties")
class AppProperties implements Validator {

    @NotBlank
    private String name;

    @Valid
    private ReportProperties report;

    private static final String APP_BASE_NAME = "Application";

    public boolean supports(Class clazz) {
        return AppProperties.class.isAssignableFrom(clazz);
    }

    public void validate(Object target, Errors errors) {

        AppProperties appProperties = (AppProperties) target;
        if (!appProperties.getName().endsWith(APP_BASE_NAME)) {
            errors.rejectValue("name", "field.name.malformed",
                    new Object[]{APP_BASE_NAME},
                    "The application name must contain [" + APP_BASE_NAME + "] base name");
        }

    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public ReportProperties getReport() {
        return report;
    }

    public void setReport(ReportProperties report) {
        this.report = report;
    }
}