package ru.kostromin.urfu.data.repository;

import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.lang.NonNull;
import ru.kostromin.urfu.data.entity.User;

public interface UserRepository extends JpaRepository<User, Long> {

  @Query("SELECT u FROM users u JOIN FETCH u.roles WHERE u.username = :username")
  User findByUsername(String username);

  @NonNull
  @Query("SELECT u FROM users u JOIN FETCH u.roles WHERE u.id = :id")
  Optional<User> findById(@NonNull Long id);

  boolean existsByUsername(String username);
}
