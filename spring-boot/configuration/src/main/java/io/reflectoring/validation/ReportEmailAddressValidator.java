package io.reflectoring.validation;

import org.springframework.validation.Errors;
import org.springframework.validation.ValidationUtils;
import org.springframework.validation.Validator;

class ReportEmailAddressValidator implements Validator {

    private static final String EMAIL_DOMAIN = "@analysisapp.com";

    public boolean supports(Class clazz) {
        return ReportProperties.class.isAssignableFrom(clazz);
    }

    public void validate(Object target, Errors errors) {

        ValidationUtils.rejectIfEmptyOrWhitespace(errors, "emailAddress", "field.required");

        ReportProperties reportProperties = (ReportProperties) target;
        if (!reportProperties.getEmailAddress().endsWith(EMAIL_DOMAIN)) {
            errors.rejectValue("emailAddress", "field.domain.required",
                    new Object[]{EMAIL_DOMAIN},
                    "The email address must contain [" + EMAIL_DOMAIN + "] domain");
        }

    }
}