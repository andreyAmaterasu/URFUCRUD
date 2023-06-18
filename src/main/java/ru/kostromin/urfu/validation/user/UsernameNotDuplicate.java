package ru.kostromin.urfu.validation.user;

import static java.lang.annotation.ElementType.FIELD;
import static java.lang.annotation.RetentionPolicy.RUNTIME;

import jakarta.validation.Constraint;
import jakarta.validation.Payload;
import java.lang.annotation.Documented;
import java.lang.annotation.Retention;
import java.lang.annotation.Target;

@Target({ FIELD })
@Retention(RUNTIME)
@Constraint(validatedBy = UsernameNotDuplicateValidation.class)
@Documented
public @interface UsernameNotDuplicate {
  String message() default "Пользователь с указанным логином уже существует";
  Class<?>[] groups() default {};
  Class<? extends Payload>[] payload() default {};
}
