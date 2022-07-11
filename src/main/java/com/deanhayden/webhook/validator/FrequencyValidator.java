package com.deanhayden.webhook.validator;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;
import java.time.Duration;

public class FrequencyValidator implements ConstraintValidator<FrequencyConstraint, Duration>
{
    @Override
    public boolean isValid(Duration frequency, ConstraintValidatorContext constraintValidatorContext) {
        if (frequency != null) {
            return frequency.getSeconds() >= 5 && frequency.getSeconds() <= 14400;
        }
        return false;
    }
}