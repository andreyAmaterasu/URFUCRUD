package ru.kostromin.urfu.controller;

import jakarta.servlet.http.HttpSession;
import jakarta.validation.Valid;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
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
import ru.kostromin.urfu.data.dto.BookDto;
import ru.kostromin.urfu.data.dto.BookFormDto;
import ru.kostromin.urfu.data.dto.Dto;
import ru.kostromin.urfu.data.entity.Book;
import ru.kostromin.urfu.data.entity.BookStore;
import ru.kostromin.urfu.data.entity.Role;
import ru.kostromin.urfu.data.entity.Store;
import ru.kostromin.urfu.data.repository.BookRepository;
import ru.kostromin.urfu.data.repository.RoleRepository;
import ru.kostromin.urfu.data.entity.User;
import ru.kostromin.urfu.data.repository.StoreRepository;
import ru.kostromin.urfu.data.repository.UserRepository;

@Controller
@ControllerAdvice
@RequiredArgsConstructor
public class UrfuController {

  private final UserRepository userRepository;

  private final RoleRepository roleRepository;

  private final BookRepository bookRepository;

  private final StoreRepository storeRepository;

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
  public String createUser(@ModelAttribute("user") @Valid User user, BindingResult bindingResult) {

    if (bindingResult.hasErrors()) {
      return "createUser";
    }

    user.setRoles(Set.of(roleRepository.getByName("ROLE_READ_ONLY")));
    user.setPassword(passwordEncoder.encode(user.getPassword()));
    user.setEnabled(true);
    userRepository.save(user);
    return "redirect:/users";
  }

  @GetMapping("users/edit/{id}")
  public String showEditUserForm(@PathVariable("id") Long id, Model model, HttpSession session) {
    User user = userRepository.findById(id)
        .orElseThrow(() -> new IllegalArgumentException("Invalid user Id:" + id));
    List<Role> roles = roleRepository.findAll();
    model.addAttribute("roles", roles);
    Dto dto = new Dto();
    dto.setUser(user);
    model.addAttribute("dto", dto);
    session.setAttribute("userEditedId", user.getId());
    return "editUser";
  }

  @PostMapping("/users/save")
  public String saveUser(@ModelAttribute("dto") Dto dto, BindingResult bindingResult, SessionStatus sessionStatus, HttpSession session) {
    User user = dto.getUser();
    user.setId((Long) session.getAttribute("userEditedId"));
    User savedUser = userRepository.findById(user.getId())
        .orElseThrow(() -> new IllegalArgumentException("Invalid user Id:" + user.getId()));
    user.setRoles(savedUser.getRoles());
    if (StringUtils.hasText(dto.getRoleName())) {
      user.getRoles().add(roleRepository.getByName(dto.getRoleName()));
    }
    if (StringUtils.hasText(user.getPassword()) &&
        !passwordEncoder.matches(user.getPassword(), savedUser.getPassword())) {
      user.setPassword(passwordEncoder.encode(user.getPassword()));
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

  @GetMapping("main")
  public String showMainPage(Model model, Authentication authentication) {
    User user = (User) authentication.getPrincipal();
    List<Book> books = bookRepository.findByUserAddedById(user.getId());
    List<BookDto> dtos = new ArrayList<>();
    books.forEach(book ->
      dtos.add(new BookDto(book, book.getBookStores().stream().map(BookStore::getPrice).findAny().orElse(0))));
    model.addAttribute("dtos", dtos);
    return "main";
  }

  @GetMapping("/books/create")
  public String showCreateBookForm(Model model) {
    model.addAttribute("bookFormDto", new BookFormDto());
    List<Store> stores = storeRepository.findAll();
    model.addAttribute("stores", stores);
    BookFormDto dto = new BookFormDto();
    model.addAttribute("dto", dto);
    return "createBook";
  }

  @PostMapping("/books/createBook")
  public String createBook(@ModelAttribute("dto") BookFormDto dto, BindingResult bindingResult, Authentication authentication) {

    if (bindingResult.hasErrors()) {
      return "createBook";
    }

    Book book = dto.getBook();
    User user = (User) authentication.getPrincipal();
    book.setUserAddedById(user.getId());
    if (book.getBookStores() == null) {
      book.setBookStores(new ArrayList<>());
    }
    Store store = storeRepository.findById(dto.getStoreId())
        .orElseThrow(() -> new IllegalArgumentException("Invalid book Id"));
    BookStore bookStore = new BookStore();
    bookStore.setBook(book);
    bookStore.setStore(store);
    bookStore.setPrice(dto.getPrice());
    book.getBookStores().add(bookStore);
    bookRepository.save(book);
    return "redirect:/main";
  }

  @GetMapping("books/delete/{id}")
  public String deleteBook(@PathVariable("id") Long id) {
    Book book = bookRepository.findById(id)
        .orElseThrow(() -> new IllegalArgumentException("Invalid book Id:" + id));
    bookRepository.delete(book);
    return "redirect:/main";
  }

  @GetMapping("about")
  public String showAboutPage() {

    return "about";
  }
}
