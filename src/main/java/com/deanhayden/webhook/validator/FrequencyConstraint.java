package com.deanhayden.webhook.validator;

import javax.validation.Constraint;
import javax.validation.Payload;
import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;
import java.time.Duration;

import static java.lang.annotation.ElementType.FIELD;
import static java.lang.annotation.ElementType.PARAMETER;

@Target({FIELD, PARAMETER})
@Retention(RetentionPolicy.RUNTIME)
@Documented
@Constraint(validatedBy = FrequencyValidator.class)
public @interface FrequencyConstraint {

    String message() default "Frequency must be a duration between 5 seconds and 4 hours, inclusive.";
    Class<?>[] groups() default {};
    Class<? extends Payload>[] payload() default {};
}
