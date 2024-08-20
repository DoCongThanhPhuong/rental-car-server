package net.codejava.validator;

import java.util.Objects;

import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;

import org.springframework.beans.BeanWrapperImpl;

import net.codejava.annotation.PasswordMatching;

public class PasswordMatchingValidator implements ConstraintValidator<PasswordMatching, Object> {
    private String password;
    private String confirmPassword;

    @Override
    public void initialize(PasswordMatching constraintAnnotation) {
        this.password = constraintAnnotation.password();
        this.confirmPassword = constraintAnnotation.confirmPassword();
    }

    @Override
    public boolean isValid(Object value, ConstraintValidatorContext context) {
        Object passwordValue = new BeanWrapperImpl(value).getPropertyValue(password);
        Object confirmPasswordValue = new BeanWrapperImpl(value).getPropertyValue(confirmPassword);

        return Objects.equals(passwordValue, confirmPasswordValue);
    }
}
