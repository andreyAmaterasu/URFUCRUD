package ru.kostromin.urfu.controller;

import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ModelAttribute;
import ru.kostromin.urfu.data.entity.User;

@ControllerAdvice
public class UrfuControllerAdvice {

  @ModelAttribute("loggedUser")
  public User addAttributes() {
    if (SecurityContextHolder.getContext().getAuthentication().getPrincipal() instanceof User user) {
      return user;
    }
    return null;
  }
}
