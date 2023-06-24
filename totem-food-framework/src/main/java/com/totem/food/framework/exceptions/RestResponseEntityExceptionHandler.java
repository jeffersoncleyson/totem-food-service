package com.totem.food.framework.exceptions;

import com.totem.food.application.exceptions.ElementExistsException;
import com.totem.food.application.exceptions.ElementNotFoundException;
import com.totem.food.application.exceptions.ExternalCommunicationInvalid;
import com.totem.food.application.exceptions.InvalidInput;
import com.totem.food.application.exceptions.response.ResponseError;
import com.totem.food.application.exceptions.response.ResponseWrapper;
import com.totem.food.domain.exceptions.InvalidEnum;
import com.totem.food.domain.exceptions.InvalidStatusException;
import com.totem.food.domain.exceptions.InvalidStatusTransition;
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

    @ExceptionHandler(value = InvalidStatusException.class )
    protected ResponseEntity<Object> handleConflict(
            InvalidStatusException ex, WebRequest request) {
        final var responseError = ResponseError.builder()
                .title("Invalid Status")
                .description(ex.getMessage()).build();
        return handleExceptionInternal(ex, new ResponseWrapper<ResponseError>(responseError),
                new HttpHeaders(), HttpStatus.BAD_REQUEST, request);
    }

    @ExceptionHandler(value = InvalidStatusTransition.class )
    protected ResponseEntity<Object> handleConflict(
            InvalidStatusTransition ex, WebRequest request) {
        final var responseError = ResponseError.builder()
                .title("Invalid Status Transition")
                .description(ex.getMessage()).build();
        return handleExceptionInternal(ex, new ResponseWrapper<ResponseError>(responseError),
                new HttpHeaders(), HttpStatus.BAD_REQUEST, request);
    }


    @ExceptionHandler(value = ExternalCommunicationInvalid.class )
    protected ResponseEntity<Object> handleConflict(
            ExternalCommunicationInvalid ex, WebRequest request) {
        final var responseError = ResponseError.builder()
                .title("Invalid Communication")
                .description(ex.getMessage()).build();
        return handleExceptionInternal(ex, new ResponseWrapper<ResponseError>(responseError),
                new HttpHeaders(), HttpStatus.BAD_REQUEST, request);
    }

    @ExceptionHandler(value = InvalidEnum.class )
    protected ResponseEntity<Object> handleConflict(
            InvalidEnum ex, WebRequest request) {
        final var responseError = ResponseError.builder()
                .title("Invalid Enum")
                .description(ex.getMessage()).build();
        return handleExceptionInternal(ex, new ResponseWrapper<ResponseError>(responseError),
                new HttpHeaders(), HttpStatus.BAD_REQUEST, request);
    }

    @ExceptionHandler(value = InvalidInput.class )
    protected ResponseEntity<Object> handleConflict(
            InvalidInput ex, WebRequest request) {
        final var responseError = ResponseError.builder()
                .title("Invalid Input")
                .description(ex.getMessage()).build();
        return handleExceptionInternal(ex, new ResponseWrapper<ResponseError>(responseError),
                new HttpHeaders(), HttpStatus.BAD_REQUEST, request);
    }




}
