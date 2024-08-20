package net.codejava.validator;

import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;

import org.springframework.beans.BeanWrapperImpl;

import net.codejava.annotation.RentalTimeMatching;
import net.codejava.utility.TimeUtil;

public class RentalTimeMatchingValidator implements ConstraintValidator<RentalTimeMatching, Object> {
    String startTime;
    String endTime;

    @Override
    public void initialize(RentalTimeMatching constraintAnnotation) {
        this.startTime = constraintAnnotation.startTime();
        this.endTime = constraintAnnotation.endTime();
    }

    @Override
    public boolean isValid(Object value, ConstraintValidatorContext context) {
        String startTimeValue = (String) new BeanWrapperImpl(value).getPropertyValue(startTime);
        String endTimeValue = (String) new BeanWrapperImpl(value).getPropertyValue(endTime);
        return TimeUtil.convertToDateTime(endTimeValue).isAfter(TimeUtil.convertToDateTime(startTimeValue));
    }
}
