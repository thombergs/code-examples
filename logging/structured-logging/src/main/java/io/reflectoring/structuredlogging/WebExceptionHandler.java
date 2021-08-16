package io.reflectoring.structuredlogging;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.slf4j.MDC;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;

@ControllerAdvice
public class WebExceptionHandler {

    private static final Logger logger = LoggerFactory.getLogger(WebExceptionHandler.class);

    @ExceptionHandler(Exception.class)
    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    public void internalServerError(Exception e){
        MDC.put("rootCause", getRootCause(e).getClass().getName());
        logger.error("returning 500 (internal server error).", e);
        MDC.remove("rootCause");
    }

    private Throwable getRootCause(Exception e){
        Throwable rootCause = e;
        while(e.getCause() != null && rootCause.getCause() != rootCause){
            rootCause = e.getCause();
        }
        return rootCause;
    }

}
