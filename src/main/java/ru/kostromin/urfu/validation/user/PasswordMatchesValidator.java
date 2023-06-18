package ru.kostromin.urfu.validation.user;

import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;
import ru.kostromin.urfu.data.entity.User;

public class PasswordMatchesValidator implements ConstraintValidator<PasswordMatches, User> {

  private String messageField;

  @Override
  public void initialize(PasswordMatches constraintAnnotation) {
    this.messageField = constraintAnnotation.messageField();
  }

  @Override
  public boolean isValid(User user, ConstraintValidatorContext constraintValidatorContext) {

    boolean isValid = user.getPassword().equals(user.getMatchingPassword());
    if (!isValid) {
      constraintValidatorContext.disableDefaultConstraintViolation();
      constraintValidatorContext.buildConstraintViolationWithTemplate(
          constraintValidatorContext.getDefaultConstraintMessageTemplate())
          .addPropertyNode(this.messageField).addConstraintViolation();
    }
    return isValid;
  }
}
