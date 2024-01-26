package ru.kostromin.urfu.data.dto;

import lombok.Data;
import ru.kostromin.urfu.data.entity.Book;

@Data
public class BookFormDto {

  private Book book;
  private Long storeId;
  private Integer price;
}
