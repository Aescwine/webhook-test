package com.deanhayden.webhook.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.BAD_REQUEST)
public class SubscriptionException extends RuntimeException {

    private final SubscriptionError subscriptionError;

    public SubscriptionException(SubscriptionError subscriptionError) {
        this.subscriptionError = subscriptionError;
    }

    public SubscriptionException(SubscriptionError subscriptionError, String message) {
        super(message);
        this.subscriptionError = subscriptionError;
    }

    public SubscriptionException(SubscriptionError subscriptionError, String message, Throwable cause) {
        super(message, cause);
        this.subscriptionError = subscriptionError;
    }

    public SubscriptionError getSubscriptionError() {
        return subscriptionError;
    }
}
