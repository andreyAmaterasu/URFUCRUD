package ru.kostromin.urfu.data.dto;

import lombok.Data;
import ru.kostromin.urfu.data.entity.User;

@Data
public class Dto {

  private User user;
  private String roleName;
}
