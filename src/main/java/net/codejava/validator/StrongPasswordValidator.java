package net.codejava.validator;

import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;

import net.codejava.annotation.StrongPassword;

public class StrongPasswordValidator implements ConstraintValidator<StrongPassword, String> {
    private final String PASSWORD_PATTERN = "^(?=.*[A-Za-z])(?=.*\\d)[A-Za-z\\d]{7,}$";

    @Override
    public boolean isValid(String value, ConstraintValidatorContext context) {
        return value.matches(PASSWORD_PATTERN);
    }
}
