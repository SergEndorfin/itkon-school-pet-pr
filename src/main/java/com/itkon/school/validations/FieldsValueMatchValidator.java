package com.itkon.school.validations;

import com.itkon.school.annotation.FieldsValueMatch;
import org.springframework.beans.BeanWrapperImpl;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;

public class FieldsValueMatchValidator
        implements ConstraintValidator<FieldsValueMatch, Object> {

    private String field;
    private String fieldMatch;

    @Override
    public void initialize(FieldsValueMatch constraintAnnotation) {
        this.field = constraintAnnotation.field();
        this.fieldMatch = constraintAnnotation.fieldMatch();
    }

    @Override
    public boolean isValid(Object value, ConstraintValidatorContext cvCxt) {
        Object filedValue = new BeanWrapperImpl(value).getPropertyValue(field);
        Object filedValueMatch = new BeanWrapperImpl(value).getPropertyValue(fieldMatch);
        if (filedValue != null) {
            return filedValue.equals(filedValueMatch);
        }
        return false;
    }
}
