package com.blah;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

@ControllerAdvice
public class RestResponseEntityExceptionHandler extends ResponseEntityExceptionHandler {

    private static final Logger LOGGER = LoggerFactory.getLogger(RestResponseEntityExceptionHandler.class);

    @ExceptionHandler(value = {IllegalArgumentException.class})
    protected ResponseEntity<Object> handleBadArgument(RuntimeException ex, WebRequest request) {
        return handleException(request, ex, HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(value = {IllegalStateException.class})
    protected ResponseEntity<Object> handleConflict(RuntimeException ex, WebRequest request) {
        return handleException(request, ex, HttpStatus.CONFLICT);
    }

    @ExceptionHandler(value = {NotFoundException.class})
    protected ResponseEntity<Object> handleNotFound(RuntimeException ex, WebRequest request) {
        return handleException(request, ex, HttpStatus.NOT_FOUND);
    }

    @ExceptionHandler(value = {Exception.class})
    protected ResponseEntity<Object> handleAllOtherProblems(RuntimeException ex, WebRequest request) {
        return handleException(request,
                new RuntimeException("We are sorry but there was a problem with the service. " +
                        "We are aware of the problem and will fix it as soon as we can."),
                HttpStatus.INTERNAL_SERVER_ERROR);
    }

    private ResponseEntity<Object> handleException(WebRequest request, RuntimeException ex, HttpStatus status) {
        LOGGER.error("There was a problem with resource: " + request.getDescription(true), ex);
        return new ResponseEntity(ex.getMessage(), new HttpHeaders(), status);
    }
}