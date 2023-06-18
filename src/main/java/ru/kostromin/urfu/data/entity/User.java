package ru.kostromin.urfu.data.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.JoinTable;
import jakarta.persistence.ManyToMany;
import jakarta.persistence.Transient;
import java.util.Collection;
import java.util.Set;
import lombok.Data;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import ru.kostromin.urfu.validation.user.UsernameNotDuplicate;
import ru.kostromin.urfu.validation.user.ValidEmailFormat;
import ru.kostromin.urfu.validation.user.PasswordMatches;

@Entity(name = "users")
@Data
@PasswordMatches(messageField = "matchingPassword")
public class User implements UserDetails {

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  @Column(name = "id")
  private Long id;

  @Column(name = "username")
  @UsernameNotDuplicate
  private String username;

  @Column(name = "lastname")
  private String lastname;

  @Column(name = "firstname")
  private String firstname;

  @Column(name = "patronymic")
  private String patronymic;

  @Column(name = "password")
  private String password;

  @Transient
  private String matchingPassword;

  @Column(name = "email")
  @ValidEmailFormat
  private String email;

  @Column(name = "enabled")
  private boolean enabled;

  @ManyToMany
  @JoinTable(
      name = "user_role",
      joinColumns = @JoinColumn(name = "fk_user_id"),
      inverseJoinColumns = @JoinColumn(name = "fk_role_id"))
  private Set<Role> roles;

  @Override
  public Collection<? extends GrantedAuthority> getAuthorities() {
    return roles;
  }

  @Override
  public boolean isAccountNonExpired() {
    return true;
  }

  @Override
  public boolean isAccountNonLocked() {
    return true;
  }

  @Override
  public boolean isCredentialsNonExpired() {
    return true;
  }

  @Override
  public boolean isEnabled() {
    return true;
  }

  public void setPassword(String password) {
    this.password = password;
    this.matchingPassword = password;
  }
}