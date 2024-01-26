package ru.kostromin.urfu.data.entity;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.JoinTable;
import jakarta.persistence.ManyToMany;
import jakarta.persistence.OneToMany;
import jakarta.persistence.OneToOne;
import jakarta.persistence.Transient;
import java.util.List;
import lombok.Data;

@Entity(name = "book")
@Data
public class Book {

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  @Column(name = "id")
  private Long id;

  @Column(name = "name")
  private String name;

  @Column(name = "description")
  private String description;

  @Column(name = "genre")
  private String genre;

  @Column(name = "fk_user_added_by_id")
  private Long userAddedById;

  @OneToMany(mappedBy = "book", cascade = CascadeType.ALL)
  private List<BookStore> bookStores;

//  @ManyToMany
//  @JoinTable(
//      name = "book_store",
//      joinColumns = @JoinColumn(name = "fk_book_id"),
//      inverseJoinColumns = @JoinColumn(name = "fk_store_id"))
//  private List<Store> stores;
}
