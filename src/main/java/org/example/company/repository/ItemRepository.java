package org.example.company.repository;

import java.util.Optional;
import org.example.company.model.Item;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ItemRepository extends JpaRepository<Item, Long> {

    Optional<Item> findByName(String name);
    boolean existsById(long id);
}
