package ru.kostromin.urfu.data.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import ru.kostromin.urfu.data.entity.Role;

public interface RoleRepository extends JpaRepository<Role, Long> {

  Role getByName(String name);
}
