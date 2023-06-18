package ru.kostromin.urfu.validation.user;

import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;
import lombok.NoArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import ru.kostromin.urfu.data.repository.UserRepository;

@NoArgsConstructor
public class UsernameNotDuplicateValidation implements ConstraintValidator<UsernameNotDuplicate, String> {

  private UserRepository userRepository;

  @Autowired
  public void setUserRepository(UserRepository userRepository) {
    this.userRepository = userRepository;
  }

  @Override
  public void initialize(UsernameNotDuplicate constraintAnnotation) {
    ConstraintValidator.super.initialize(constraintAnnotation);
  }

  @Override
  public boolean isValid(String username, ConstraintValidatorContext constraintValidatorContext) {
    return userRepository == null || !userRepository.existsByUsername(username);
  }
}
