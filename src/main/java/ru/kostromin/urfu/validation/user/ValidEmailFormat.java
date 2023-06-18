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
@Constraint(validatedBy = ValidEmailFormatValidator.class)
@Documented
public @interface ValidEmailFormat {
  String message() default "Недопустимый формат адреса электронной почты";
  Class<?>[] groups() default {};
  Class<? extends Payload>[] payload() default {};
}
