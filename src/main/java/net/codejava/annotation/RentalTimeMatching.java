package net.codejava.annotation;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

import jakarta.validation.Constraint;
import jakarta.validation.Payload;

import net.codejava.validator.RentalTimeMatchingValidator;

@Constraint(validatedBy = RentalTimeMatchingValidator.class)
@Target({ElementType.TYPE})
@Retention(RetentionPolicy.RUNTIME)
public @interface RentalTimeMatching {
    String startTime();

    String endTime();

    String message() default "End rental time must after Start rental time!";

    Class<?>[] groups() default {};

    Class<? extends Payload>[] payload() default {};

    @Target({ElementType.TYPE})
    @Retention(RetentionPolicy.RUNTIME)
    @interface List {
        RentalTimeMatching[] value();
    }
}
