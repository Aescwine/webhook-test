package com.deanhayden.webhook.model;

import com.deanhayden.webhook.exception.SubscriptionError;
import org.springframework.http.HttpStatus;

public class ErrorResponse {
    private final HttpStatus httpStatus;
    private final SubscriptionError error;
    private final String details;

    public ErrorResponse(HttpStatus httpStatus, SubscriptionError error, String details) {
        this.httpStatus = httpStatus;
        this.error = error;
        this.details = details;
    }

    public SubscriptionError getError() {
        return error;
    }

    public HttpStatus getHttpStatus() {
        return httpStatus;
    }

    public String getDetails() {
        return details;
    }
}
