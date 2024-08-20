package net.codejava.annotation;

import static java.lang.annotation.ElementType.*;
import static java.lang.annotation.RetentionPolicy.RUNTIME;

import java.lang.annotation.Documented;
import java.lang.annotation.Retention;
import java.lang.annotation.Target;

import jakarta.validation.Constraint;
import jakarta.validation.Payload;

import net.codejava.validator.EnumValidator;

@Target({METHOD, FIELD, ANNOTATION_TYPE, CONSTRUCTOR, PARAMETER, TYPE_USE})
@Retention(RUNTIME)
@Documented
@Constraint(validatedBy = EnumValidator.class)
public @interface EnumValid {
    String name();

    String message() default "{name} must be any of enum {enumClass}";

    Class<? extends Enum<?>> enumClass();

    Class<?>[] groups() default {};

    Class<? extends Payload>[] payload() default {};
}
