package ru.kostromin.urfu.data.repository;

import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import ru.kostromin.urfu.data.entity.Book;

@Repository
public interface BookRepository extends JpaRepository<Book, Long> {

  List<Book> findByUserAddedById(Long id);
}
