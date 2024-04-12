package com.reflectoring.security.exception;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import org.zalando.problem.AbstractThrowableProblem;
import org.zalando.problem.StatusType;

import static com.fasterxml.jackson.annotation.JsonInclude.Include.NON_EMPTY;
import static org.zalando.problem.Status.*;

@JsonInclude(NON_EMPTY)
@JsonIgnoreProperties({"stackTrace", "type", "title", "message", "localizedMessage", "parameters"})
public class CommonException extends AbstractThrowableProblem {

    private CommonException(StatusType status, String detail) {
        super(null, null, status, detail, null, null, null);
    }

    public static CommonException unauthorized() {
        return new CommonException(UNAUTHORIZED, "Unauthorised or Bad Credentials");
    }

    public static CommonException forbidden() {
        return new CommonException(FORBIDDEN, "Forbidden");
    }

    public static CommonException headerError() {
        return new CommonException(FORBIDDEN, "Missing Header");
    }


}
