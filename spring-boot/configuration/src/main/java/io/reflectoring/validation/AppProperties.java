package io.reflectoring.validation;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.validation.annotation.Validated;

import javax.validation.Valid;
import javax.validation.constraints.NotBlank;

@Validated
@ConfigurationProperties(prefix = "app.properties")
class AppProperties {

    @NotBlank
    private String name;

    @Valid
    private ReportProperties report;

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