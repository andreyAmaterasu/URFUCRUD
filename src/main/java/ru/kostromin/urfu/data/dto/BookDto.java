package ru.kostromin.urfu.data.dto;

import lombok.Data;
import lombok.Getter;
import lombok.Setter;
import ru.kostromin.urfu.data.entity.Book;

public record BookDto(Book book, Integer price) {

}
