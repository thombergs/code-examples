package io.reflectoring.user.deactivation.scheduler;

import org.springframework.http.HttpStatus;
import org.springframework.http.ProblemDetail;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

import lombok.NonNull;
import lombok.extern.slf4j.Slf4j;
import software.amazon.awssdk.services.scheduler.model.ConflictException;
import software.amazon.awssdk.services.scheduler.model.ResourceNotFoundException;

@Slf4j
@RestControllerAdvice
class ApiExceptionHandler extends ResponseEntityExceptionHandler {

    @ExceptionHandler(ResourceNotFoundException.class)
    public ProblemDetail handle(final ResourceNotFoundException exception) {
        logException(exception);
        return ProblemDetail.forStatusAndDetail(HttpStatus.BAD_REQUEST, "User account not scheduled for deactivation.");
    }

    @ExceptionHandler(ConflictException.class)
    public ProblemDetail handle(final ConflictException exception) {
        logException(exception);
        return ProblemDetail.forStatusAndDetail(HttpStatus.CONFLICT, "User account already scheduled for deactivation.");
    }

    @ExceptionHandler(Exception.class)
    public ProblemDetail handle(final Exception exception) {
        logException(exception);
        return ProblemDetail.forStatusAndDetail(HttpStatus.NOT_IMPLEMENTED, "Something went wrong.");
    }

    private void logException(final @NonNull Exception exception) {
        log.error("Exception encountered: {}", exception.getMessage(), exception);
    }

}