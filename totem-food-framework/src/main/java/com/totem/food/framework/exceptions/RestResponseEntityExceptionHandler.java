package com.totem.food.framework.exceptions;

import com.totem.food.application.exceptions.ElementExistsException;
import com.totem.food.application.exceptions.ElementNotFoundException;
import com.totem.food.application.exceptions.response.ResponseError;
import com.totem.food.application.exceptions.response.ResponseWrapper;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

@ControllerAdvice
public class RestResponseEntityExceptionHandler
        extends ResponseEntityExceptionHandler {

    @ExceptionHandler(value = ElementNotFoundException.class )
    protected ResponseEntity<Object> handleConflict(
            ElementNotFoundException ex, WebRequest request) {
        final var responseError = ResponseError.builder()
                .title("Element Not Found")
                .description(ex.getMessage()).build();
        return handleExceptionInternal(ex, new ResponseWrapper<ResponseError>(responseError),
                new HttpHeaders(), HttpStatus.BAD_REQUEST, request);
    }

    @ExceptionHandler(value = ElementExistsException.class )
    protected ResponseEntity<Object> handleConflict(
            ElementExistsException ex, WebRequest request) {
        final var responseError = ResponseError.builder()
                .title("Element Exists")
                .description(ex.getMessage()).build();
        return handleExceptionInternal(ex, new ResponseWrapper<ResponseError>(responseError),
                new HttpHeaders(), HttpStatus.BAD_REQUEST, request);
    }

}
