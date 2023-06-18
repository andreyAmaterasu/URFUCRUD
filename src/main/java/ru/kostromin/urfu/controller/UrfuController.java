package ru.kostromin.urfu.controller;

import jakarta.servlet.http.HttpSession;
import jakarta.validation.Valid;
import java.util.Set;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.util.StringUtils;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.support.SessionStatus;
import ru.kostromin.urfu.data.repository.RoleRepository;
import ru.kostromin.urfu.data.entity.User;
import ru.kostromin.urfu.data.repository.UserRepository;

@Controller
@ControllerAdvice
@RequiredArgsConstructor
public class UrfuController {

  private final UserRepository userRepository;

  private final RoleRepository roleRepository;

  private final BCryptPasswordEncoder passwordEncoder;

  @GetMapping("/login")
  public String login() {
    return "login";
  }

  @GetMapping("/users")
  public String users(Model model) {
    model.addAttribute("users", userRepository.findAll());
    return "users";
  }

  @GetMapping("users/createUser")
  public String showCreateUserForm(Model model) {
    model.addAttribute("user", new User());
    return "createUser";
  }

  @PostMapping("/users/create")
  public String createUser(@ModelAttribute("user") @Valid User user, BindingResult bindingResult, Model model) {

    if (bindingResult.hasErrors()) {
      return "createUser";
    }

    user.setRoles(Set.of(roleRepository.getByName("ROLE_USER")));
    user.setPassword(passwordEncoder.encode(user.getPassword()));
    user.setEnabled(true);
    userRepository.save(user);
    return "redirect:/users";
  }

  @GetMapping("users/edit/{id}")
  public String showEditUserForm(@PathVariable("id") Long id, Model model, HttpSession session) {
    User user = userRepository.findById(id)
        .orElseThrow(() -> new IllegalArgumentException("Invalid user Id:" + id));
    model.addAttribute("user", user);
    session.setAttribute("userEditedId", user.getId());
    return "editUser";
  }

  @PostMapping("/users/save")
  public String saveUser(@ModelAttribute("user") User user, BindingResult bindingResult, SessionStatus sessionStatus, HttpSession session) {

    user.setId((Long) session.getAttribute("userEditedId"));
    User savedUser = userRepository.findById(user.getId())
        .orElseThrow(() -> new IllegalArgumentException("Invalid user Id:" + user.getId()));
    user.setRoles(savedUser.getRoles());
    savedUser.setMatchingPassword(savedUser.getPassword());
    if (StringUtils.hasText(user.getPassword()) &&
        !passwordEncoder.matches(user.getPassword(), savedUser.getPassword())) {
      user.setPassword(passwordEncoder.encode(user.getPassword()));
      savedUser.setPassword(user.getPassword());
    } else {
      user.setPassword(savedUser.getPassword());
    }
    userRepository.save(user);
    sessionStatus.setComplete();
    session.removeAttribute("userEditedId");
    return "redirect:/users";
  }

  @GetMapping("users/delete/{id}")
  public String deleteUser(@PathVariable("id") Long id) {
    User user = userRepository.findById(id)
        .orElseThrow(() -> new IllegalArgumentException("Invalid user Id:" + id));
    userRepository.delete(user);
    return "redirect:/users";
  }

  @GetMapping("profile/{id}")
  public String showProfile(@PathVariable("id") Long id, Model model) {
    User user = userRepository.findById(id)
        .orElseThrow(() -> new IllegalArgumentException("Invalid user Id:" + id));
    model.addAttribute("user", user);
    return "profile";
  }
}
