package ru.kostromin.urfu.validation.user;

import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;
import java.util.regex.Pattern;

public class ValidEmailFormatValidator implements ConstraintValidator<ValidEmailFormat, String> {

  @Override
  public void initialize(ValidEmailFormat constraintAnnotation) {
    ConstraintValidator.super.initialize(constraintAnnotation);
  }

  @Override
  public boolean isValid(String email, ConstraintValidatorContext constraintValidatorContext) {
    String regexPattern = "(.+)@(.+\\..+)";
    return Pattern.compile(regexPattern)
        .matcher(email)
        .matches();
  }
}
