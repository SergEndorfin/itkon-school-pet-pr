package com.itkon.school.validations;

import com.itkon.school.annotation.PasswordValidator;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;
import java.util.List;

public class PasswordStrengthValidator implements
        ConstraintValidator<PasswordValidator, String> {

    private List<String> weakPasswords;

    @Override
    public void initialize(PasswordValidator constraintAnnotation) {
        weakPasswords = List.of("12345", "pass", "qwerty");
    }

    @Override
    public boolean isValid(String passwordField, ConstraintValidatorContext cvCxt) {
        return passwordField != null && !weakPasswords.contains(passwordField);
    }
}
