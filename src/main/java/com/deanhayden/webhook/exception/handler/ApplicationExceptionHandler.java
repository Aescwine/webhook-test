package com.deanhayden.webhook.exception.handler;

import com.deanhayden.webhook.exception.SubscriptionException;
import com.deanhayden.webhook.model.ErrorResponse;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.bind.support.WebExchangeBindException;

@RestControllerAdvice
public class ApplicationExceptionHandler {

    @ExceptionHandler(WebExchangeBindException.class)
    ResponseEntity<?> handleValidationError(WebExchangeBindException ex) {
        if (ex.getFieldError() != null) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(ex.getFieldError().getDefaultMessage());
        }
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).build();
    }

    @ExceptionHandler(SubscriptionException.class)
    ResponseEntity<?> handleSubscriptionException(SubscriptionException ex) {
        ErrorResponse errorResponse = new ErrorResponse(HttpStatus.BAD_REQUEST, ex.getSubscriptionError(), ex.getMessage());

        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(errorResponse);
    }
}
