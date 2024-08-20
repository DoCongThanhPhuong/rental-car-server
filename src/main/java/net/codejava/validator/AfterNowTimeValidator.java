package net.codejava.validator;

import java.time.LocalDateTime;

import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;

import net.codejava.annotation.AfterNowTime;
import net.codejava.utility.TimeUtil;

public class AfterNowTimeValidator implements ConstraintValidator<AfterNowTime, String> {
    @Override
    public boolean isValid(String value, ConstraintValidatorContext context) {
        return TimeUtil.convertToDateTime(value).isAfter(LocalDateTime.now());
    }
}
