package com.deanhayden.webhook.model;

import com.deanhayden.webhook.validator.FrequencyConstraint;
import lombok.AllArgsConstructor;
import lombok.Getter;

import javax.validation.constraints.NotEmpty;
import java.time.Duration;

@Getter
@AllArgsConstructor
public class ClockSubscription {
    // TODO: validation of URL format
    @NotEmpty(message = "Url must not be empty")
    private final String url;
    @FrequencyConstraint
    private final Duration frequency;
}
