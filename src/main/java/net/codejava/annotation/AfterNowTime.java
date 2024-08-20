package net.codejava.annotation;

import java.lang.annotation.*;

import jakarta.validation.Constraint;
import jakarta.validation.Payload;

import net.codejava.validator.AfterNowTimeValidator;

@Constraint(validatedBy = AfterNowTimeValidator.class)
@Target({ElementType.METHOD, ElementType.FIELD})
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface AfterNowTime {
    String message() default "The Start rental time must is after now";

    Class<?>[] groups() default {};

    Class<? extends Payload>[] payload() default {};
}
