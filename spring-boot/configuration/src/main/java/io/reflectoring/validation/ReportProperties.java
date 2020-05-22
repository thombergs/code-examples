package io.reflectoring.validation;

import javax.validation.constraints.Email;
import javax.validation.constraints.Max;
import javax.validation.constraints.Min;

class ReportProperties {

    private Boolean sendEmails = Boolean.FALSE;

    private ReportType type = ReportType.HTML;

    @Min(value = 7)
    @Max(value = 30)
    private Integer intervalInDays;

    @Email
    private String emailAddress;

    public Boolean getSendEmails() {
        return sendEmails;
    }

    public void setSendEmails(Boolean sendEmails) {
        this.sendEmails = sendEmails;
    }

    public ReportType getType() {
        return type;
    }

    public void setType(ReportType type) {
        this.type = type;
    }

    public Integer getIntervalInDays() {
        return intervalInDays;
    }

    public void setIntervalInDays(Integer intervalInDays) {
        this.intervalInDays = intervalInDays;
    }

    public String getEmailAddress() {
        return emailAddress;
    }

    public void setEmailAddress(String emailAddress) {
        this.emailAddress = emailAddress;
    }
}
