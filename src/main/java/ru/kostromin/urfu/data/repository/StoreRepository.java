package ru.kostromin.urfu.data.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import ru.kostromin.urfu.data.entity.Store;

@Repository
public interface StoreRepository extends JpaRepository<Store, Long> {

}
